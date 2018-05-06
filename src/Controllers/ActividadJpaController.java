/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Actividad;
import Entities.ActividadPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.MatPorCurso;
import Entities.NomActividad;
import Entities.Promedio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) throws PreexistingEntityException, Exception {
        if (actividad.getActividadPK() == null) {
            actividad.setActividadPK(new ActividadPK());
        }
        actividad.getActividadPK().setCodigo(actividad.getNomActividad().getCodigo());
        actividad.getActividadPK().setCodPromedio(actividad.getPromedio().getPromedioPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso codMatCurso = actividad.getCodMatCurso();
            if (codMatCurso != null) {
                codMatCurso = em.getReference(codMatCurso.getClass(), codMatCurso.getMatPorCursoPK());
                actividad.setCodMatCurso(codMatCurso);
            }
            NomActividad nomActividad = actividad.getNomActividad();
            if (nomActividad != null) {
                nomActividad = em.getReference(nomActividad.getClass(), nomActividad.getCodigo());
                actividad.setNomActividad(nomActividad);
            }
            Promedio promedio = actividad.getPromedio();
            if (promedio != null) {
                promedio = em.getReference(promedio.getClass(), promedio.getPromedioPK());
                actividad.setPromedio(promedio);
            }
            em.persist(actividad);
            if (codMatCurso != null) {
                codMatCurso.getActividadList().add(actividad);
                codMatCurso = em.merge(codMatCurso);
            }
            if (nomActividad != null) {
                nomActividad.getActividadList().add(actividad);
                nomActividad = em.merge(nomActividad);
            }
            if (promedio != null) {
                promedio.getActividadList().add(actividad);
                promedio = em.merge(promedio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActividad(actividad.getActividadPK()) != null) {
                throw new PreexistingEntityException("Actividad " + actividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws NonexistentEntityException, Exception {
        actividad.getActividadPK().setCodigo(actividad.getNomActividad().getCodigo());
        actividad.getActividadPK().setCodPromedio(actividad.getPromedio().getPromedioPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getActividadPK());
            MatPorCurso codMatCursoOld = persistentActividad.getCodMatCurso();
            MatPorCurso codMatCursoNew = actividad.getCodMatCurso();
            NomActividad nomActividadOld = persistentActividad.getNomActividad();
            NomActividad nomActividadNew = actividad.getNomActividad();
            Promedio promedioOld = persistentActividad.getPromedio();
            Promedio promedioNew = actividad.getPromedio();
            if (codMatCursoNew != null) {
                codMatCursoNew = em.getReference(codMatCursoNew.getClass(), codMatCursoNew.getMatPorCursoPK());
                actividad.setCodMatCurso(codMatCursoNew);
            }
            if (nomActividadNew != null) {
                nomActividadNew = em.getReference(nomActividadNew.getClass(), nomActividadNew.getCodigo());
                actividad.setNomActividad(nomActividadNew);
            }
            if (promedioNew != null) {
                promedioNew = em.getReference(promedioNew.getClass(), promedioNew.getPromedioPK());
                actividad.setPromedio(promedioNew);
            }
            actividad = em.merge(actividad);
            if (codMatCursoOld != null && !codMatCursoOld.equals(codMatCursoNew)) {
                codMatCursoOld.getActividadList().remove(actividad);
                codMatCursoOld = em.merge(codMatCursoOld);
            }
            if (codMatCursoNew != null && !codMatCursoNew.equals(codMatCursoOld)) {
                codMatCursoNew.getActividadList().add(actividad);
                codMatCursoNew = em.merge(codMatCursoNew);
            }
            if (nomActividadOld != null && !nomActividadOld.equals(nomActividadNew)) {
                nomActividadOld.getActividadList().remove(actividad);
                nomActividadOld = em.merge(nomActividadOld);
            }
            if (nomActividadNew != null && !nomActividadNew.equals(nomActividadOld)) {
                nomActividadNew.getActividadList().add(actividad);
                nomActividadNew = em.merge(nomActividadNew);
            }
            if (promedioOld != null && !promedioOld.equals(promedioNew)) {
                promedioOld.getActividadList().remove(actividad);
                promedioOld = em.merge(promedioOld);
            }
            if (promedioNew != null && !promedioNew.equals(promedioOld)) {
                promedioNew.getActividadList().add(actividad);
                promedioNew = em.merge(promedioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ActividadPK id = actividad.getActividadPK();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ActividadPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getActividadPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            MatPorCurso codMatCurso = actividad.getCodMatCurso();
            if (codMatCurso != null) {
                codMatCurso.getActividadList().remove(actividad);
                codMatCurso = em.merge(codMatCurso);
            }
            NomActividad nomActividad = actividad.getNomActividad();
            if (nomActividad != null) {
                nomActividad.getActividadList().remove(actividad);
                nomActividad = em.merge(nomActividad);
            }
            Promedio promedio = actividad.getPromedio();
            if (promedio != null) {
                promedio.getActividadList().remove(actividad);
                promedio = em.merge(promedio);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(ActividadPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
