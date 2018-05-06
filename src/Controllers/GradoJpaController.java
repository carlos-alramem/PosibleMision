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
import Entities.Curso;
import Entities.Grado;
import Entities.GradoPK;
import Entities.Seccion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class GradoJpaController implements Serializable {

    public GradoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) throws PreexistingEntityException, Exception {
        if (grado.getGradoPK() == null) {
            grado.setGradoPK(new GradoPK());
        }
        grado.getGradoPK().setCodSeccion(grado.getSeccion().getSeccionPK().getCodigo());
        grado.getGradoPK().setCodCurso(grado.getCurso().getCursoPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso curso = grado.getCurso();
            if (curso != null) {
                curso = em.getReference(curso.getClass(), curso.getCursoPK());
                grado.setCurso(curso);
            }
            Seccion seccion = grado.getSeccion();
            if (seccion != null) {
                seccion = em.getReference(seccion.getClass(), seccion.getSeccionPK());
                grado.setSeccion(seccion);
            }
            em.persist(grado);
            if (curso != null) {
                curso.getGradoList().add(grado);
                curso = em.merge(curso);
            }
            if (seccion != null) {
                seccion.getGradoList().add(grado);
                seccion = em.merge(seccion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrado(grado.getGradoPK()) != null) {
                throw new PreexistingEntityException("Grado " + grado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws NonexistentEntityException, Exception {
        grado.getGradoPK().setCodSeccion(grado.getSeccion().getSeccionPK().getCodigo());
        grado.getGradoPK().setCodCurso(grado.getCurso().getCursoPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getGradoPK());
            Curso cursoOld = persistentGrado.getCurso();
            Curso cursoNew = grado.getCurso();
            Seccion seccionOld = persistentGrado.getSeccion();
            Seccion seccionNew = grado.getSeccion();
            if (cursoNew != null) {
                cursoNew = em.getReference(cursoNew.getClass(), cursoNew.getCursoPK());
                grado.setCurso(cursoNew);
            }
            if (seccionNew != null) {
                seccionNew = em.getReference(seccionNew.getClass(), seccionNew.getSeccionPK());
                grado.setSeccion(seccionNew);
            }
            grado = em.merge(grado);
            if (cursoOld != null && !cursoOld.equals(cursoNew)) {
                cursoOld.getGradoList().remove(grado);
                cursoOld = em.merge(cursoOld);
            }
            if (cursoNew != null && !cursoNew.equals(cursoOld)) {
                cursoNew.getGradoList().add(grado);
                cursoNew = em.merge(cursoNew);
            }
            if (seccionOld != null && !seccionOld.equals(seccionNew)) {
                seccionOld.getGradoList().remove(grado);
                seccionOld = em.merge(seccionOld);
            }
            if (seccionNew != null && !seccionNew.equals(seccionOld)) {
                seccionNew.getGradoList().add(grado);
                seccionNew = em.merge(seccionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                GradoPK id = grado.getGradoPK();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(GradoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getGradoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            Curso curso = grado.getCurso();
            if (curso != null) {
                curso.getGradoList().remove(grado);
                curso = em.merge(curso);
            }
            Seccion seccion = grado.getSeccion();
            if (seccion != null) {
                seccion.getGradoList().remove(grado);
                seccion = em.merge(seccion);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(GradoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
