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
import Entities.Actividad;
import Entities.Alumno;
import Entities.Nota;
import Entities.NotaPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class NotaJpaController implements Serializable {

    public NotaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nota nota) throws PreexistingEntityException, Exception {
        if (nota.getNotaPK() == null) {
            nota.setNotaPK(new NotaPK());
        }
        nota.getNotaPK().setCodAlumno(nota.getAlumno().getCodigo());
        nota.getNotaPK().setCodActividad(nota.getActividad().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad = nota.getActividad();
            if (actividad != null) {
                actividad = em.getReference(actividad.getClass(), actividad.getCodigo());
                nota.setActividad(actividad);
            }
            Alumno alumno = nota.getAlumno();
            if (alumno != null) {
                alumno = em.getReference(alumno.getClass(), alumno.getCodigo());
                nota.setAlumno(alumno);
            }
            em.persist(nota);
            if (actividad != null) {
                actividad.getNotaList().add(nota);
                actividad = em.merge(actividad);
            }
            if (alumno != null) {
                alumno.getNotaList().add(nota);
                alumno = em.merge(alumno);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNota(nota.getNotaPK()) != null) {
                throw new PreexistingEntityException("Nota " + nota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nota nota) throws NonexistentEntityException, Exception {
        nota.getNotaPK().setCodAlumno(nota.getAlumno().getCodigo());
        nota.getNotaPK().setCodActividad(nota.getActividad().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nota persistentNota = em.find(Nota.class, nota.getNotaPK());
            Actividad actividadOld = persistentNota.getActividad();
            Actividad actividadNew = nota.getActividad();
            Alumno alumnoOld = persistentNota.getAlumno();
            Alumno alumnoNew = nota.getAlumno();
            if (actividadNew != null) {
                actividadNew = em.getReference(actividadNew.getClass(), actividadNew.getCodigo());
                nota.setActividad(actividadNew);
            }
            if (alumnoNew != null) {
                alumnoNew = em.getReference(alumnoNew.getClass(), alumnoNew.getCodigo());
                nota.setAlumno(alumnoNew);
            }
            nota = em.merge(nota);
            if (actividadOld != null && !actividadOld.equals(actividadNew)) {
                actividadOld.getNotaList().remove(nota);
                actividadOld = em.merge(actividadOld);
            }
            if (actividadNew != null && !actividadNew.equals(actividadOld)) {
                actividadNew.getNotaList().add(nota);
                actividadNew = em.merge(actividadNew);
            }
            if (alumnoOld != null && !alumnoOld.equals(alumnoNew)) {
                alumnoOld.getNotaList().remove(nota);
                alumnoOld = em.merge(alumnoOld);
            }
            if (alumnoNew != null && !alumnoNew.equals(alumnoOld)) {
                alumnoNew.getNotaList().add(nota);
                alumnoNew = em.merge(alumnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                NotaPK id = nota.getNotaPK();
                if (findNota(id) == null) {
                    throw new NonexistentEntityException("The nota with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(NotaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nota nota;
            try {
                nota = em.getReference(Nota.class, id);
                nota.getNotaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nota with id " + id + " no longer exists.", enfe);
            }
            Actividad actividad = nota.getActividad();
            if (actividad != null) {
                actividad.getNotaList().remove(nota);
                actividad = em.merge(actividad);
            }
            Alumno alumno = nota.getAlumno();
            if (alumno != null) {
                alumno.getNotaList().remove(nota);
                alumno = em.merge(alumno);
            }
            em.remove(nota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nota> findNotaEntities() {
        return findNotaEntities(true, -1, -1);
    }

    public List<Nota> findNotaEntities(int maxResults, int firstResult) {
        return findNotaEntities(false, maxResults, firstResult);
    }

    private List<Nota> findNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nota.class));
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

    public Nota findNota(NotaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nota.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nota> rt = cq.from(Nota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
