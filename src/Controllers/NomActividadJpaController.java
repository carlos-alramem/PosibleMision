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
import Entities.Actividad;
import Entities.NomActividad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class NomActividadJpaController implements Serializable {

    public NomActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NomActividad nomActividad) throws PreexistingEntityException, Exception {
        if (nomActividad.getActividadList() == null) {
            nomActividad.setActividadList(new ArrayList<Actividad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : nomActividad.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getCodigo());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            nomActividad.setActividadList(attachedActividadList);
            em.persist(nomActividad);
            for (Actividad actividadListActividad : nomActividad.getActividadList()) {
                NomActividad oldCodNomActividadOfActividadListActividad = actividadListActividad.getCodNomActividad();
                actividadListActividad.setCodNomActividad(nomActividad);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldCodNomActividadOfActividadListActividad != null) {
                    oldCodNomActividadOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldCodNomActividadOfActividadListActividad = em.merge(oldCodNomActividadOfActividadListActividad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNomActividad(nomActividad.getCodigo()) != null) {
                throw new PreexistingEntityException("NomActividad " + nomActividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NomActividad nomActividad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            NomActividad persistentNomActividad = em.find(NomActividad.class, nomActividad.getCodigo());
            List<Actividad> actividadListOld = persistentNomActividad.getActividadList();
            List<Actividad> actividadListNew = nomActividad.getActividadList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its codNomActividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getCodigo());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            nomActividad.setActividadList(actividadListNew);
            nomActividad = em.merge(nomActividad);
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    NomActividad oldCodNomActividadOfActividadListNewActividad = actividadListNewActividad.getCodNomActividad();
                    actividadListNewActividad.setCodNomActividad(nomActividad);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldCodNomActividadOfActividadListNewActividad != null && !oldCodNomActividadOfActividadListNewActividad.equals(nomActividad)) {
                        oldCodNomActividadOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldCodNomActividadOfActividadListNewActividad = em.merge(oldCodNomActividadOfActividadListNewActividad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nomActividad.getCodigo();
                if (findNomActividad(id) == null) {
                    throw new NonexistentEntityException("The nomActividad with id " + id + " no longer exists.");
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
            NomActividad nomActividad;
            try {
                nomActividad = em.getReference(NomActividad.class, id);
                nomActividad.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nomActividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = nomActividad.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NomActividad (" + nomActividad + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable codNomActividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(nomActividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NomActividad> findNomActividadEntities() {
        return findNomActividadEntities(true, -1, -1);
    }

    public List<NomActividad> findNomActividadEntities(int maxResults, int firstResult) {
        return findNomActividadEntities(false, maxResults, firstResult);
    }

    private List<NomActividad> findNomActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NomActividad.class));
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

    public NomActividad findNomActividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NomActividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getNomActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NomActividad> rt = cq.from(NomActividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
