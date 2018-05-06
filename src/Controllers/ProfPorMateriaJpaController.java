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
import Entities.ProfPorMateriaPK;
import Entities.Profesor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
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
        if (profPorMateria.getProfPorMateriaPK() == null) {
            profPorMateria.setProfPorMateriaPK(new ProfPorMateriaPK());
        }
        profPorMateria.getProfPorMateriaPK().setCodMatPorCurso(profPorMateria.getMatPorCurso().getMatPorCursoPK().getCodigo());
        profPorMateria.getProfPorMateriaPK().setDuiProfesor(profPorMateria.getProfesor().getDui());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso matPorCurso = profPorMateria.getMatPorCurso();
            if (matPorCurso != null) {
                matPorCurso = em.getReference(matPorCurso.getClass(), matPorCurso.getMatPorCursoPK());
                profPorMateria.setMatPorCurso(matPorCurso);
            }
            Profesor profesor = profPorMateria.getProfesor();
            if (profesor != null) {
                profesor = em.getReference(profesor.getClass(), profesor.getDui());
                profPorMateria.setProfesor(profesor);
            }
            em.persist(profPorMateria);
            if (matPorCurso != null) {
                matPorCurso.getProfPorMateriaList().add(profPorMateria);
                matPorCurso = em.merge(matPorCurso);
            }
            if (profesor != null) {
                profesor.getProfPorMateriaList().add(profPorMateria);
                profesor = em.merge(profesor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfPorMateria(profPorMateria.getProfPorMateriaPK()) != null) {
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
        profPorMateria.getProfPorMateriaPK().setCodMatPorCurso(profPorMateria.getMatPorCurso().getMatPorCursoPK().getCodigo());
        profPorMateria.getProfPorMateriaPK().setDuiProfesor(profPorMateria.getProfesor().getDui());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfPorMateria persistentProfPorMateria = em.find(ProfPorMateria.class, profPorMateria.getProfPorMateriaPK());
            MatPorCurso matPorCursoOld = persistentProfPorMateria.getMatPorCurso();
            MatPorCurso matPorCursoNew = profPorMateria.getMatPorCurso();
            Profesor profesorOld = persistentProfPorMateria.getProfesor();
            Profesor profesorNew = profPorMateria.getProfesor();
            if (matPorCursoNew != null) {
                matPorCursoNew = em.getReference(matPorCursoNew.getClass(), matPorCursoNew.getMatPorCursoPK());
                profPorMateria.setMatPorCurso(matPorCursoNew);
            }
            if (profesorNew != null) {
                profesorNew = em.getReference(profesorNew.getClass(), profesorNew.getDui());
                profPorMateria.setProfesor(profesorNew);
            }
            profPorMateria = em.merge(profPorMateria);
            if (matPorCursoOld != null && !matPorCursoOld.equals(matPorCursoNew)) {
                matPorCursoOld.getProfPorMateriaList().remove(profPorMateria);
                matPorCursoOld = em.merge(matPorCursoOld);
            }
            if (matPorCursoNew != null && !matPorCursoNew.equals(matPorCursoOld)) {
                matPorCursoNew.getProfPorMateriaList().add(profPorMateria);
                matPorCursoNew = em.merge(matPorCursoNew);
            }
            if (profesorOld != null && !profesorOld.equals(profesorNew)) {
                profesorOld.getProfPorMateriaList().remove(profPorMateria);
                profesorOld = em.merge(profesorOld);
            }
            if (profesorNew != null && !profesorNew.equals(profesorOld)) {
                profesorNew.getProfPorMateriaList().add(profPorMateria);
                profesorNew = em.merge(profesorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProfPorMateriaPK id = profPorMateria.getProfPorMateriaPK();
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

    public void destroy(ProfPorMateriaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfPorMateria profPorMateria;
            try {
                profPorMateria = em.getReference(ProfPorMateria.class, id);
                profPorMateria.getProfPorMateriaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profPorMateria with id " + id + " no longer exists.", enfe);
            }
            MatPorCurso matPorCurso = profPorMateria.getMatPorCurso();
            if (matPorCurso != null) {
                matPorCurso.getProfPorMateriaList().remove(profPorMateria);
                matPorCurso = em.merge(matPorCurso);
            }
            Profesor profesor = profPorMateria.getProfesor();
            if (profesor != null) {
                profesor.getProfPorMateriaList().remove(profPorMateria);
                profesor = em.merge(profesor);
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

    public ProfPorMateria findProfPorMateria(ProfPorMateriaPK id) {
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
