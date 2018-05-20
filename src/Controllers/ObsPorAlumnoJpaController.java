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
import Entities.Alumno;
import Entities.ObsPorAlumno;
import Entities.ObsPorAlumnoPK;
import Entities.Observacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class ObsPorAlumnoJpaController implements Serializable {

    public ObsPorAlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ObsPorAlumno obsPorAlumno) throws PreexistingEntityException, Exception {
        if (obsPorAlumno.getObsPorAlumnoPK() == null) {
            obsPorAlumno.setObsPorAlumnoPK(new ObsPorAlumnoPK());
        }
        obsPorAlumno.getObsPorAlumnoPK().setCodObservacion(obsPorAlumno.getObservacion().getCodigo());
        obsPorAlumno.getObsPorAlumnoPK().setCodAlumno(obsPorAlumno.getAlumno().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno alumno = obsPorAlumno.getAlumno();
            if (alumno != null) {
                alumno = em.getReference(alumno.getClass(), alumno.getCodigo());
                obsPorAlumno.setAlumno(alumno);
            }
            Observacion observacion = obsPorAlumno.getObservacion();
            if (observacion != null) {
                observacion = em.getReference(observacion.getClass(), observacion.getCodigo());
                obsPorAlumno.setObservacion(observacion);
            }
            em.persist(obsPorAlumno);
            if (alumno != null) {
                alumno.getObsPorAlumnoList().add(obsPorAlumno);
                alumno = em.merge(alumno);
            }
            if (observacion != null) {
                observacion.getObsPorAlumnoList().add(obsPorAlumno);
                observacion = em.merge(observacion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObsPorAlumno(obsPorAlumno.getObsPorAlumnoPK()) != null) {
                throw new PreexistingEntityException("ObsPorAlumno " + obsPorAlumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ObsPorAlumno obsPorAlumno) throws NonexistentEntityException, Exception {
        obsPorAlumno.getObsPorAlumnoPK().setCodObservacion(obsPorAlumno.getObservacion().getCodigo());
        obsPorAlumno.getObsPorAlumnoPK().setCodAlumno(obsPorAlumno.getAlumno().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObsPorAlumno persistentObsPorAlumno = em.find(ObsPorAlumno.class, obsPorAlumno.getObsPorAlumnoPK());
            Alumno alumnoOld = persistentObsPorAlumno.getAlumno();
            Alumno alumnoNew = obsPorAlumno.getAlumno();
            Observacion observacionOld = persistentObsPorAlumno.getObservacion();
            Observacion observacionNew = obsPorAlumno.getObservacion();
            if (alumnoNew != null) {
                alumnoNew = em.getReference(alumnoNew.getClass(), alumnoNew.getCodigo());
                obsPorAlumno.setAlumno(alumnoNew);
            }
            if (observacionNew != null) {
                observacionNew = em.getReference(observacionNew.getClass(), observacionNew.getCodigo());
                obsPorAlumno.setObservacion(observacionNew);
            }
            obsPorAlumno = em.merge(obsPorAlumno);
            if (alumnoOld != null && !alumnoOld.equals(alumnoNew)) {
                alumnoOld.getObsPorAlumnoList().remove(obsPorAlumno);
                alumnoOld = em.merge(alumnoOld);
            }
            if (alumnoNew != null && !alumnoNew.equals(alumnoOld)) {
                alumnoNew.getObsPorAlumnoList().add(obsPorAlumno);
                alumnoNew = em.merge(alumnoNew);
            }
            if (observacionOld != null && !observacionOld.equals(observacionNew)) {
                observacionOld.getObsPorAlumnoList().remove(obsPorAlumno);
                observacionOld = em.merge(observacionOld);
            }
            if (observacionNew != null && !observacionNew.equals(observacionOld)) {
                observacionNew.getObsPorAlumnoList().add(obsPorAlumno);
                observacionNew = em.merge(observacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ObsPorAlumnoPK id = obsPorAlumno.getObsPorAlumnoPK();
                if (findObsPorAlumno(id) == null) {
                    throw new NonexistentEntityException("The obsPorAlumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ObsPorAlumnoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObsPorAlumno obsPorAlumno;
            try {
                obsPorAlumno = em.getReference(ObsPorAlumno.class, id);
                obsPorAlumno.getObsPorAlumnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obsPorAlumno with id " + id + " no longer exists.", enfe);
            }
            Alumno alumno = obsPorAlumno.getAlumno();
            if (alumno != null) {
                alumno.getObsPorAlumnoList().remove(obsPorAlumno);
                alumno = em.merge(alumno);
            }
            Observacion observacion = obsPorAlumno.getObservacion();
            if (observacion != null) {
                observacion.getObsPorAlumnoList().remove(obsPorAlumno);
                observacion = em.merge(observacion);
            }
            em.remove(obsPorAlumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ObsPorAlumno> findObsPorAlumnoEntities() {
        return findObsPorAlumnoEntities(true, -1, -1);
    }

    public List<ObsPorAlumno> findObsPorAlumnoEntities(int maxResults, int firstResult) {
        return findObsPorAlumnoEntities(false, maxResults, firstResult);
    }

    private List<ObsPorAlumno> findObsPorAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ObsPorAlumno.class));
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

    public ObsPorAlumno findObsPorAlumno(ObsPorAlumnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ObsPorAlumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getObsPorAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ObsPorAlumno> rt = cq.from(ObsPorAlumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
