/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Ajustes;
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
public class AjustesJpaController implements Serializable {

    public AjustesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ajustes ajustes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ajustes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAjustes(ajustes.getCodigo()) != null) {
                throw new PreexistingEntityException("Ajustes " + ajustes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ajustes ajustes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ajustes = em.merge(ajustes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ajustes.getCodigo();
                if (findAjustes(id) == null) {
                    throw new NonexistentEntityException("The ajustes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ajustes ajustes;
            try {
                ajustes = em.getReference(Ajustes.class, id);
                ajustes.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ajustes with id " + id + " no longer exists.", enfe);
            }
            em.remove(ajustes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ajustes> findAjustesEntities() {
        return findAjustesEntities(true, -1, -1);
    }

    public List<Ajustes> findAjustesEntities(int maxResults, int firstResult) {
        return findAjustesEntities(false, maxResults, firstResult);
    }

    private List<Ajustes> findAjustesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ajustes.class));
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

    public Ajustes findAjustes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ajustes.class, id);
        } finally {
            em.close();
        }
    }

    public int getAjustesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ajustes> rt = cq.from(Ajustes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
