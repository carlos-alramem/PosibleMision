/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.ObsPorAlumno;
import Entities.Observacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class ObservacionJpaController implements Serializable {

    public ObservacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Observacion observacion) throws PreexistingEntityException, Exception {
        if (observacion.getObsPorAlumnoList() == null) {
            observacion.setObsPorAlumnoList(new ArrayList<ObsPorAlumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ObsPorAlumno> attachedObsPorAlumnoList = new ArrayList<ObsPorAlumno>();
            for (ObsPorAlumno obsPorAlumnoListObsPorAlumnoToAttach : observacion.getObsPorAlumnoList()) {
                obsPorAlumnoListObsPorAlumnoToAttach = em.getReference(obsPorAlumnoListObsPorAlumnoToAttach.getClass(), obsPorAlumnoListObsPorAlumnoToAttach.getObsPorAlumnoPK());
                attachedObsPorAlumnoList.add(obsPorAlumnoListObsPorAlumnoToAttach);
            }
            observacion.setObsPorAlumnoList(attachedObsPorAlumnoList);
            em.persist(observacion);
            for (ObsPorAlumno obsPorAlumnoListObsPorAlumno : observacion.getObsPorAlumnoList()) {
                Observacion oldObservacionOfObsPorAlumnoListObsPorAlumno = obsPorAlumnoListObsPorAlumno.getObservacion();
                obsPorAlumnoListObsPorAlumno.setObservacion(observacion);
                obsPorAlumnoListObsPorAlumno = em.merge(obsPorAlumnoListObsPorAlumno);
                if (oldObservacionOfObsPorAlumnoListObsPorAlumno != null) {
                    oldObservacionOfObsPorAlumnoListObsPorAlumno.getObsPorAlumnoList().remove(obsPorAlumnoListObsPorAlumno);
                    oldObservacionOfObsPorAlumnoListObsPorAlumno = em.merge(oldObservacionOfObsPorAlumnoListObsPorAlumno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObservacion(observacion.getCodigo()) != null) {
                throw new PreexistingEntityException("Observacion " + observacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Observacion observacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Observacion persistentObservacion = em.find(Observacion.class, observacion.getCodigo());
            List<ObsPorAlumno> obsPorAlumnoListOld = persistentObservacion.getObsPorAlumnoList();
            List<ObsPorAlumno> obsPorAlumnoListNew = observacion.getObsPorAlumnoList();
            List<String> illegalOrphanMessages = null;
            for (ObsPorAlumno obsPorAlumnoListOldObsPorAlumno : obsPorAlumnoListOld) {
                if (!obsPorAlumnoListNew.contains(obsPorAlumnoListOldObsPorAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ObsPorAlumno " + obsPorAlumnoListOldObsPorAlumno + " since its observacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ObsPorAlumno> attachedObsPorAlumnoListNew = new ArrayList<ObsPorAlumno>();
            for (ObsPorAlumno obsPorAlumnoListNewObsPorAlumnoToAttach : obsPorAlumnoListNew) {
                obsPorAlumnoListNewObsPorAlumnoToAttach = em.getReference(obsPorAlumnoListNewObsPorAlumnoToAttach.getClass(), obsPorAlumnoListNewObsPorAlumnoToAttach.getObsPorAlumnoPK());
                attachedObsPorAlumnoListNew.add(obsPorAlumnoListNewObsPorAlumnoToAttach);
            }
            obsPorAlumnoListNew = attachedObsPorAlumnoListNew;
            observacion.setObsPorAlumnoList(obsPorAlumnoListNew);
            observacion = em.merge(observacion);
            for (ObsPorAlumno obsPorAlumnoListNewObsPorAlumno : obsPorAlumnoListNew) {
                if (!obsPorAlumnoListOld.contains(obsPorAlumnoListNewObsPorAlumno)) {
                    Observacion oldObservacionOfObsPorAlumnoListNewObsPorAlumno = obsPorAlumnoListNewObsPorAlumno.getObservacion();
                    obsPorAlumnoListNewObsPorAlumno.setObservacion(observacion);
                    obsPorAlumnoListNewObsPorAlumno = em.merge(obsPorAlumnoListNewObsPorAlumno);
                    if (oldObservacionOfObsPorAlumnoListNewObsPorAlumno != null && !oldObservacionOfObsPorAlumnoListNewObsPorAlumno.equals(observacion)) {
                        oldObservacionOfObsPorAlumnoListNewObsPorAlumno.getObsPorAlumnoList().remove(obsPorAlumnoListNewObsPorAlumno);
                        oldObservacionOfObsPorAlumnoListNewObsPorAlumno = em.merge(oldObservacionOfObsPorAlumnoListNewObsPorAlumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = observacion.getCodigo();
                if (findObservacion(id) == null) {
                    throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.");
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
            Observacion observacion;
            try {
                observacion = em.getReference(Observacion.class, id);
                observacion.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ObsPorAlumno> obsPorAlumnoListOrphanCheck = observacion.getObsPorAlumnoList();
            for (ObsPorAlumno obsPorAlumnoListOrphanCheckObsPorAlumno : obsPorAlumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Observacion (" + observacion + ") cannot be destroyed since the ObsPorAlumno " + obsPorAlumnoListOrphanCheckObsPorAlumno + " in its obsPorAlumnoList field has a non-nullable observacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(observacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Observacion> findObservacionEntities() {
        return findObservacionEntities(true, -1, -1);
    }

    public List<Observacion> findObservacionEntities(int maxResults, int firstResult) {
        return findObservacionEntities(false, maxResults, firstResult);
    }

    private List<Observacion> findObservacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Observacion.class));
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

    public Observacion findObservacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Observacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getObservacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Observacion> rt = cq.from(Observacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
