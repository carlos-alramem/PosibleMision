/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Nivel;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Promedio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class NivelJpaController implements Serializable {

    public NivelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel nivel) throws PreexistingEntityException, Exception {
        if (nivel.getPromedioList() == null) {
            nivel.setPromedioList(new ArrayList<Promedio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Promedio> attachedPromedioList = new ArrayList<Promedio>();
            for (Promedio promedioListPromedioToAttach : nivel.getPromedioList()) {
                promedioListPromedioToAttach = em.getReference(promedioListPromedioToAttach.getClass(), promedioListPromedioToAttach.getPromedioPK());
                attachedPromedioList.add(promedioListPromedioToAttach);
            }
            nivel.setPromedioList(attachedPromedioList);
            em.persist(nivel);
            for (Promedio promedioListPromedio : nivel.getPromedioList()) {
                Nivel oldNivel1OfPromedioListPromedio = promedioListPromedio.getNivel1();
                promedioListPromedio.setNivel1(nivel);
                promedioListPromedio = em.merge(promedioListPromedio);
                if (oldNivel1OfPromedioListPromedio != null) {
                    oldNivel1OfPromedioListPromedio.getPromedioList().remove(promedioListPromedio);
                    oldNivel1OfPromedioListPromedio = em.merge(oldNivel1OfPromedioListPromedio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNivel(nivel.getCodigo()) != null) {
                throw new PreexistingEntityException("Nivel " + nivel + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel nivel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel persistentNivel = em.find(Nivel.class, nivel.getCodigo());
            List<Promedio> promedioListOld = persistentNivel.getPromedioList();
            List<Promedio> promedioListNew = nivel.getPromedioList();
            List<String> illegalOrphanMessages = null;
            for (Promedio promedioListOldPromedio : promedioListOld) {
                if (!promedioListNew.contains(promedioListOldPromedio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Promedio " + promedioListOldPromedio + " since its nivel1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Promedio> attachedPromedioListNew = new ArrayList<Promedio>();
            for (Promedio promedioListNewPromedioToAttach : promedioListNew) {
                promedioListNewPromedioToAttach = em.getReference(promedioListNewPromedioToAttach.getClass(), promedioListNewPromedioToAttach.getPromedioPK());
                attachedPromedioListNew.add(promedioListNewPromedioToAttach);
            }
            promedioListNew = attachedPromedioListNew;
            nivel.setPromedioList(promedioListNew);
            nivel = em.merge(nivel);
            for (Promedio promedioListNewPromedio : promedioListNew) {
                if (!promedioListOld.contains(promedioListNewPromedio)) {
                    Nivel oldNivel1OfPromedioListNewPromedio = promedioListNewPromedio.getNivel1();
                    promedioListNewPromedio.setNivel1(nivel);
                    promedioListNewPromedio = em.merge(promedioListNewPromedio);
                    if (oldNivel1OfPromedioListNewPromedio != null && !oldNivel1OfPromedioListNewPromedio.equals(nivel)) {
                        oldNivel1OfPromedioListNewPromedio.getPromedioList().remove(promedioListNewPromedio);
                        oldNivel1OfPromedioListNewPromedio = em.merge(oldNivel1OfPromedioListNewPromedio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel.getCodigo();
                if (findNivel(id) == null) {
                    throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.");
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
            Nivel nivel;
            try {
                nivel = em.getReference(Nivel.class, id);
                nivel.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Promedio> promedioListOrphanCheck = nivel.getPromedioList();
            for (Promedio promedioListOrphanCheckPromedio : promedioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Nivel (" + nivel + ") cannot be destroyed since the Promedio " + promedioListOrphanCheckPromedio + " in its promedioList field has a non-nullable nivel1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(nivel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel> findNivelEntities() {
        return findNivelEntities(true, -1, -1);
    }

    public List<Nivel> findNivelEntities(int maxResults, int firstResult) {
        return findNivelEntities(false, maxResults, firstResult);
    }

    private List<Nivel> findNivelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel.class));
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

    public Nivel findNivel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel> rt = cq.from(Nivel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
