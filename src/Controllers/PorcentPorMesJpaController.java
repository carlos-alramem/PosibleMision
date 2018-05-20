/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Nivel;
import Entities.PorcentPorMes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel nivel = porcentPorMes.getNivel();
            if (nivel != null) {
                nivel = em.getReference(nivel.getClass(), nivel.getCodigo());
                porcentPorMes.setNivel(nivel);
            }
            em.persist(porcentPorMes);
            if (nivel != null) {
                nivel.getPorcentPorMesList().add(porcentPorMes);
                nivel = em.merge(nivel);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPorcentPorMes(porcentPorMes.getCodigo()) != null) {
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
            PorcentPorMes persistentPorcentPorMes = em.find(PorcentPorMes.class, porcentPorMes.getCodigo());
            Nivel nivelOld = persistentPorcentPorMes.getNivel();
            Nivel nivelNew = porcentPorMes.getNivel();
            if (nivelNew != null) {
                nivelNew = em.getReference(nivelNew.getClass(), nivelNew.getCodigo());
                porcentPorMes.setNivel(nivelNew);
            }
            porcentPorMes = em.merge(porcentPorMes);
            if (nivelOld != null && !nivelOld.equals(nivelNew)) {
                nivelOld.getPorcentPorMesList().remove(porcentPorMes);
                nivelOld = em.merge(nivelOld);
            }
            if (nivelNew != null && !nivelNew.equals(nivelOld)) {
                nivelNew.getPorcentPorMesList().add(porcentPorMes);
                nivelNew = em.merge(nivelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = porcentPorMes.getCodigo();
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PorcentPorMes porcentPorMes;
            try {
                porcentPorMes = em.getReference(PorcentPorMes.class, id);
                porcentPorMes.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The porcentPorMes with id " + id + " no longer exists.", enfe);
            }
            Nivel nivel = porcentPorMes.getNivel();
            if (nivel != null) {
                nivel.getPorcentPorMesList().remove(porcentPorMes);
                nivel = em.merge(nivel);
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

    public PorcentPorMes findPorcentPorMes(Integer id) {
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
