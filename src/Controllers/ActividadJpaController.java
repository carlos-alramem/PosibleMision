/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Actividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.MatPorCurso;
import Entities.NomActividad;
import Entities.Promedio;
import Entities.Nota;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
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
        if (actividad.getNotaList() == null) {
            actividad.setNotaList(new ArrayList<Nota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso codMatCurso = actividad.getCodMatCurso();
            if (codMatCurso != null) {
                codMatCurso = em.getReference(codMatCurso.getClass(), codMatCurso.getCodigo());
                actividad.setCodMatCurso(codMatCurso);
            }
            NomActividad codNomActividad = actividad.getCodNomActividad();
            if (codNomActividad != null) {
                codNomActividad = em.getReference(codNomActividad.getClass(), codNomActividad.getCodigo());
                actividad.setCodNomActividad(codNomActividad);
            }
            Promedio codPromedio = actividad.getCodPromedio();
            if (codPromedio != null) {
                codPromedio = em.getReference(codPromedio.getClass(), codPromedio.getCodigo());
                actividad.setCodPromedio(codPromedio);
            }
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : actividad.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getNotaPK());
                attachedNotaList.add(notaListNotaToAttach);
            }
            actividad.setNotaList(attachedNotaList);
            em.persist(actividad);
            if (codMatCurso != null) {
                codMatCurso.getActividadList().add(actividad);
                codMatCurso = em.merge(codMatCurso);
            }
            if (codNomActividad != null) {
                codNomActividad.getActividadList().add(actividad);
                codNomActividad = em.merge(codNomActividad);
            }
            if (codPromedio != null) {
                codPromedio.getActividadList().add(actividad);
                codPromedio = em.merge(codPromedio);
            }
            for (Nota notaListNota : actividad.getNotaList()) {
                Actividad oldActividadOfNotaListNota = notaListNota.getActividad();
                notaListNota.setActividad(actividad);
                notaListNota = em.merge(notaListNota);
                if (oldActividadOfNotaListNota != null) {
                    oldActividadOfNotaListNota.getNotaList().remove(notaListNota);
                    oldActividadOfNotaListNota = em.merge(oldActividadOfNotaListNota);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActividad(actividad.getCodigo()) != null) {
                throw new PreexistingEntityException("Actividad " + actividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getCodigo());
            MatPorCurso codMatCursoOld = persistentActividad.getCodMatCurso();
            MatPorCurso codMatCursoNew = actividad.getCodMatCurso();
            NomActividad codNomActividadOld = persistentActividad.getCodNomActividad();
            NomActividad codNomActividadNew = actividad.getCodNomActividad();
            Promedio codPromedioOld = persistentActividad.getCodPromedio();
            Promedio codPromedioNew = actividad.getCodPromedio();
            List<Nota> notaListOld = persistentActividad.getNotaList();
            List<Nota> notaListNew = actividad.getNotaList();
            List<String> illegalOrphanMessages = null;
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its actividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codMatCursoNew != null) {
                codMatCursoNew = em.getReference(codMatCursoNew.getClass(), codMatCursoNew.getCodigo());
                actividad.setCodMatCurso(codMatCursoNew);
            }
            if (codNomActividadNew != null) {
                codNomActividadNew = em.getReference(codNomActividadNew.getClass(), codNomActividadNew.getCodigo());
                actividad.setCodNomActividad(codNomActividadNew);
            }
            if (codPromedioNew != null) {
                codPromedioNew = em.getReference(codPromedioNew.getClass(), codPromedioNew.getCodigo());
                actividad.setCodPromedio(codPromedioNew);
            }
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getNotaPK());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            actividad.setNotaList(notaListNew);
            actividad = em.merge(actividad);
            if (codMatCursoOld != null && !codMatCursoOld.equals(codMatCursoNew)) {
                codMatCursoOld.getActividadList().remove(actividad);
                codMatCursoOld = em.merge(codMatCursoOld);
            }
            if (codMatCursoNew != null && !codMatCursoNew.equals(codMatCursoOld)) {
                codMatCursoNew.getActividadList().add(actividad);
                codMatCursoNew = em.merge(codMatCursoNew);
            }
            if (codNomActividadOld != null && !codNomActividadOld.equals(codNomActividadNew)) {
                codNomActividadOld.getActividadList().remove(actividad);
                codNomActividadOld = em.merge(codNomActividadOld);
            }
            if (codNomActividadNew != null && !codNomActividadNew.equals(codNomActividadOld)) {
                codNomActividadNew.getActividadList().add(actividad);
                codNomActividadNew = em.merge(codNomActividadNew);
            }
            if (codPromedioOld != null && !codPromedioOld.equals(codPromedioNew)) {
                codPromedioOld.getActividadList().remove(actividad);
                codPromedioOld = em.merge(codPromedioOld);
            }
            if (codPromedioNew != null && !codPromedioNew.equals(codPromedioOld)) {
                codPromedioNew.getActividadList().add(actividad);
                codPromedioNew = em.merge(codPromedioNew);
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Actividad oldActividadOfNotaListNewNota = notaListNewNota.getActividad();
                    notaListNewNota.setActividad(actividad);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldActividadOfNotaListNewNota != null && !oldActividadOfNotaListNewNota.equals(actividad)) {
                        oldActividadOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldActividadOfNotaListNewNota = em.merge(oldActividadOfNotaListNewNota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividad.getCodigo();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Nota> notaListOrphanCheck = actividad.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividad (" + actividad + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable actividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MatPorCurso codMatCurso = actividad.getCodMatCurso();
            if (codMatCurso != null) {
                codMatCurso.getActividadList().remove(actividad);
                codMatCurso = em.merge(codMatCurso);
            }
            NomActividad codNomActividad = actividad.getCodNomActividad();
            if (codNomActividad != null) {
                codNomActividad.getActividadList().remove(actividad);
                codNomActividad = em.merge(codNomActividad);
            }
            Promedio codPromedio = actividad.getCodPromedio();
            if (codPromedio != null) {
                codPromedio.getActividadList().remove(actividad);
                codPromedio = em.merge(codPromedio);
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

    public Actividad findActividad(Integer id) {
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
