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
import Entities.MatPorCurso;
import Entities.ProfPorMateria;
import Entities.Profesor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class ProfPorMateriaJpaController implements Serializable {

    public ProfPorMateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProfPorMateria profPorMateria) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso codMatPorCurso = profPorMateria.getCodMatPorCurso();
            if (codMatPorCurso != null) {
                codMatPorCurso = em.getReference(codMatPorCurso.getClass(), codMatPorCurso.getCodigo());
                profPorMateria.setCodMatPorCurso(codMatPorCurso);
            }
            Profesor duiProfesor = profPorMateria.getDuiProfesor();
            if (duiProfesor != null) {
                duiProfesor = em.getReference(duiProfesor.getClass(), duiProfesor.getDui());
                profPorMateria.setDuiProfesor(duiProfesor);
            }
            em.persist(profPorMateria);
            if (codMatPorCurso != null) {
                codMatPorCurso.getProfPorMateriaList().add(profPorMateria);
                codMatPorCurso = em.merge(codMatPorCurso);
            }
            if (duiProfesor != null) {
                duiProfesor.getProfPorMateriaList().add(profPorMateria);
                duiProfesor = em.merge(duiProfesor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfPorMateria(profPorMateria.getCodigo()) != null) {
                throw new PreexistingEntityException("ProfPorMateria " + profPorMateria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProfPorMateria profPorMateria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfPorMateria persistentProfPorMateria = em.find(ProfPorMateria.class, profPorMateria.getCodigo());
            MatPorCurso codMatPorCursoOld = persistentProfPorMateria.getCodMatPorCurso();
            MatPorCurso codMatPorCursoNew = profPorMateria.getCodMatPorCurso();
            Profesor duiProfesorOld = persistentProfPorMateria.getDuiProfesor();
            Profesor duiProfesorNew = profPorMateria.getDuiProfesor();
            if (codMatPorCursoNew != null) {
                codMatPorCursoNew = em.getReference(codMatPorCursoNew.getClass(), codMatPorCursoNew.getCodigo());
                profPorMateria.setCodMatPorCurso(codMatPorCursoNew);
            }
            if (duiProfesorNew != null) {
                duiProfesorNew = em.getReference(duiProfesorNew.getClass(), duiProfesorNew.getDui());
                profPorMateria.setDuiProfesor(duiProfesorNew);
            }
            profPorMateria = em.merge(profPorMateria);
            if (codMatPorCursoOld != null && !codMatPorCursoOld.equals(codMatPorCursoNew)) {
                codMatPorCursoOld.getProfPorMateriaList().remove(profPorMateria);
                codMatPorCursoOld = em.merge(codMatPorCursoOld);
            }
            if (codMatPorCursoNew != null && !codMatPorCursoNew.equals(codMatPorCursoOld)) {
                codMatPorCursoNew.getProfPorMateriaList().add(profPorMateria);
                codMatPorCursoNew = em.merge(codMatPorCursoNew);
            }
            if (duiProfesorOld != null && !duiProfesorOld.equals(duiProfesorNew)) {
                duiProfesorOld.getProfPorMateriaList().remove(profPorMateria);
                duiProfesorOld = em.merge(duiProfesorOld);
            }
            if (duiProfesorNew != null && !duiProfesorNew.equals(duiProfesorOld)) {
                duiProfesorNew.getProfPorMateriaList().add(profPorMateria);
                duiProfesorNew = em.merge(duiProfesorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profPorMateria.getCodigo();
                if (findProfPorMateria(id) == null) {
                    throw new NonexistentEntityException("The profPorMateria with id " + id + " no longer exists.");
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
            ProfPorMateria profPorMateria;
            try {
                profPorMateria = em.getReference(ProfPorMateria.class, id);
                profPorMateria.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profPorMateria with id " + id + " no longer exists.", enfe);
            }
            MatPorCurso codMatPorCurso = profPorMateria.getCodMatPorCurso();
            if (codMatPorCurso != null) {
                codMatPorCurso.getProfPorMateriaList().remove(profPorMateria);
                codMatPorCurso = em.merge(codMatPorCurso);
            }
            Profesor duiProfesor = profPorMateria.getDuiProfesor();
            if (duiProfesor != null) {
                duiProfesor.getProfPorMateriaList().remove(profPorMateria);
                duiProfesor = em.merge(duiProfesor);
            }
            em.remove(profPorMateria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProfPorMateria> findProfPorMateriaEntities() {
        return findProfPorMateriaEntities(true, -1, -1);
    }

    public List<ProfPorMateria> findProfPorMateriaEntities(int maxResults, int firstResult) {
        return findProfPorMateriaEntities(false, maxResults, firstResult);
    }

    private List<ProfPorMateria> findProfPorMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProfPorMateria.class));
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

    public ProfPorMateria findProfPorMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProfPorMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfPorMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProfPorMateria> rt = cq.from(ProfPorMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
