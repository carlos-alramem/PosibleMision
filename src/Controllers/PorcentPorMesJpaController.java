/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.PorcentPorMes;
import Entities.PorcentPorMesPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author carlos
 */
public class PorcentPorMesJpaController implements Serializable {

    public PorcentPorMesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PorcentPorMes porcentPorMes) throws PreexistingEntityException, Exception {
        if (porcentPorMes.getPorcentPorMesPK() == null) {
            porcentPorMes.setPorcentPorMesPK(new PorcentPorMesPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(porcentPorMes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPorcentPorMes(porcentPorMes.getPorcentPorMesPK()) != null) {
                throw new PreexistingEntityException("PorcentPorMes " + porcentPorMes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PorcentPorMes porcentPorMes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            porcentPorMes = em.merge(porcentPorMes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PorcentPorMesPK id = porcentPorMes.getPorcentPorMesPK();
                if (findPorcentPorMes(id) == null) {
                    throw new NonexistentEntityException("The porcentPorMes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PorcentPorMesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PorcentPorMes porcentPorMes;
            try {
                porcentPorMes = em.getReference(PorcentPorMes.class, id);
                porcentPorMes.getPorcentPorMesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The porcentPorMes with id " + id + " no longer exists.", enfe);
            }
            em.remove(porcentPorMes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PorcentPorMes> findPorcentPorMesEntities() {
        return findPorcentPorMesEntities(true, -1, -1);
    }

    public List<PorcentPorMes> findPorcentPorMesEntities(int maxResults, int firstResult) {
        return findPorcentPorMesEntities(false, maxResults, firstResult);
    }

    private List<PorcentPorMes> findPorcentPorMesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PorcentPorMes.class));
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

    public PorcentPorMes findPorcentPorMes(PorcentPorMesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PorcentPorMes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPorcentPorMesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PorcentPorMes> rt = cq.from(PorcentPorMes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
