/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Nivel;
import Entities.Actividad;
import Entities.Promedio;
import Entities.PromedioPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class PromedioJpaController implements Serializable {

    public PromedioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Promedio promedio) throws PreexistingEntityException, Exception {
        if (promedio.getPromedioPK() == null) {
            promedio.setPromedioPK(new PromedioPK());
        }
        if (promedio.getActividadList() == null) {
            promedio.setActividadList(new ArrayList<Actividad>());
        }
        promedio.getPromedioPK().setNivel(promedio.getNivel1().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel nivel1 = promedio.getNivel1();
            if (nivel1 != null) {
                nivel1 = em.getReference(nivel1.getClass(), nivel1.getCodigo());
                promedio.setNivel1(nivel1);
            }
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : promedio.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getActividadPK());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            promedio.setActividadList(attachedActividadList);
            em.persist(promedio);
            if (nivel1 != null) {
                nivel1.getPromedioList().add(promedio);
                nivel1 = em.merge(nivel1);
            }
            for (Actividad actividadListActividad : promedio.getActividadList()) {
                Promedio oldPromedioOfActividadListActividad = actividadListActividad.getPromedio();
                actividadListActividad.setPromedio(promedio);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldPromedioOfActividadListActividad != null) {
                    oldPromedioOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldPromedioOfActividadListActividad = em.merge(oldPromedioOfActividadListActividad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPromedio(promedio.getPromedioPK()) != null) {
                throw new PreexistingEntityException("Promedio " + promedio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Promedio promedio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        promedio.getPromedioPK().setNivel(promedio.getNivel1().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Promedio persistentPromedio = em.find(Promedio.class, promedio.getPromedioPK());
            Nivel nivel1Old = persistentPromedio.getNivel1();
            Nivel nivel1New = promedio.getNivel1();
            List<Actividad> actividadListOld = persistentPromedio.getActividadList();
            List<Actividad> actividadListNew = promedio.getActividadList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its promedio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nivel1New != null) {
                nivel1New = em.getReference(nivel1New.getClass(), nivel1New.getCodigo());
                promedio.setNivel1(nivel1New);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getActividadPK());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            promedio.setActividadList(actividadListNew);
            promedio = em.merge(promedio);
            if (nivel1Old != null && !nivel1Old.equals(nivel1New)) {
                nivel1Old.getPromedioList().remove(promedio);
                nivel1Old = em.merge(nivel1Old);
            }
            if (nivel1New != null && !nivel1New.equals(nivel1Old)) {
                nivel1New.getPromedioList().add(promedio);
                nivel1New = em.merge(nivel1New);
            }
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    Promedio oldPromedioOfActividadListNewActividad = actividadListNewActividad.getPromedio();
                    actividadListNewActividad.setPromedio(promedio);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldPromedioOfActividadListNewActividad != null && !oldPromedioOfActividadListNewActividad.equals(promedio)) {
                        oldPromedioOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldPromedioOfActividadListNewActividad = em.merge(oldPromedioOfActividadListNewActividad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PromedioPK id = promedio.getPromedioPK();
                if (findPromedio(id) == null) {
                    throw new NonexistentEntityException("The promedio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PromedioPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Promedio promedio;
            try {
                promedio = em.getReference(Promedio.class, id);
                promedio.getPromedioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The promedio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = promedio.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Promedio (" + promedio + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable promedio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Nivel nivel1 = promedio.getNivel1();
            if (nivel1 != null) {
                nivel1.getPromedioList().remove(promedio);
                nivel1 = em.merge(nivel1);
            }
            em.remove(promedio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Promedio> findPromedioEntities() {
        return findPromedioEntities(true, -1, -1);
    }

    public List<Promedio> findPromedioEntities(int maxResults, int firstResult) {
        return findPromedioEntities(false, maxResults, firstResult);
    }

    private List<Promedio> findPromedioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Promedio.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Promedio findPromedio(PromedioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Promedio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPromedioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Promedio> rt = cq.from(Promedio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
