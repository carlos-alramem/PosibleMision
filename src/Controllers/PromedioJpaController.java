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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
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
        if (promedio.getActividadList() == null) {
            promedio.setActividadList(new ArrayList<Actividad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel nivel = promedio.getNivel();
            if (nivel != null) {
                nivel = em.getReference(nivel.getClass(), nivel.getCodigo());
                promedio.setNivel(nivel);
            }
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : promedio.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getCodigo());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            promedio.setActividadList(attachedActividadList);
            em.persist(promedio);
            if (nivel != null) {
                nivel.getPromedioList().add(promedio);
                nivel = em.merge(nivel);
            }
            for (Actividad actividadListActividad : promedio.getActividadList()) {
                Promedio oldCodPromedioOfActividadListActividad = actividadListActividad.getCodPromedio();
                actividadListActividad.setCodPromedio(promedio);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldCodPromedioOfActividadListActividad != null) {
                    oldCodPromedioOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldCodPromedioOfActividadListActividad = em.merge(oldCodPromedioOfActividadListActividad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPromedio(promedio.getCodigo()) != null) {
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Promedio persistentPromedio = em.find(Promedio.class, promedio.getCodigo());
            Nivel nivelOld = persistentPromedio.getNivel();
            Nivel nivelNew = promedio.getNivel();
            List<Actividad> actividadListOld = persistentPromedio.getActividadList();
            List<Actividad> actividadListNew = promedio.getActividadList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its codPromedio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nivelNew != null) {
                nivelNew = em.getReference(nivelNew.getClass(), nivelNew.getCodigo());
                promedio.setNivel(nivelNew);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getCodigo());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            promedio.setActividadList(actividadListNew);
            promedio = em.merge(promedio);
            if (nivelOld != null && !nivelOld.equals(nivelNew)) {
                nivelOld.getPromedioList().remove(promedio);
                nivelOld = em.merge(nivelOld);
            }
            if (nivelNew != null && !nivelNew.equals(nivelOld)) {
                nivelNew.getPromedioList().add(promedio);
                nivelNew = em.merge(nivelNew);
            }
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    Promedio oldCodPromedioOfActividadListNewActividad = actividadListNewActividad.getCodPromedio();
                    actividadListNewActividad.setCodPromedio(promedio);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldCodPromedioOfActividadListNewActividad != null && !oldCodPromedioOfActividadListNewActividad.equals(promedio)) {
                        oldCodPromedioOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldCodPromedioOfActividadListNewActividad = em.merge(oldCodPromedioOfActividadListNewActividad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = promedio.getCodigo();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Promedio promedio;
            try {
                promedio = em.getReference(Promedio.class, id);
                promedio.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The promedio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = promedio.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Promedio (" + promedio + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable codPromedio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Nivel nivel = promedio.getNivel();
            if (nivel != null) {
                nivel.getPromedioList().remove(promedio);
                nivel = em.merge(nivel);
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

    public Promedio findPromedio(Integer id) {
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
