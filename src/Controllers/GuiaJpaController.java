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
import Entities.Grado;
import Entities.Guia;
import Entities.Profesor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class GuiaJpaController implements Serializable {

    public GuiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Guia guia) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Grado grado1OrphanCheck = guia.getGrado1();
        if (grado1OrphanCheck != null) {
            Guia oldGuiaOfGrado1 = grado1OrphanCheck.getGuia();
            if (oldGuiaOfGrado1 != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Grado " + grado1OrphanCheck + " already has an item of type Guia whose grado1 column cannot be null. Please make another selection for the grado1 field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado grado1 = guia.getGrado1();
            if (grado1 != null) {
                grado1 = em.getReference(grado1.getClass(), grado1.getCodigo());
                guia.setGrado1(grado1);
            }
            Profesor duiProfesor = guia.getDuiProfesor();
            if (duiProfesor != null) {
                duiProfesor = em.getReference(duiProfesor.getClass(), duiProfesor.getDui());
                guia.setDuiProfesor(duiProfesor);
            }
            em.persist(guia);
            if (grado1 != null) {
                grado1.setGuia(guia);
                grado1 = em.merge(grado1);
            }
            if (duiProfesor != null) {
                duiProfesor.getGuiaList().add(guia);
                duiProfesor = em.merge(duiProfesor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGuia(guia.getCodigo()) != null) {
                throw new PreexistingEntityException("Guia " + guia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Guia guia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Guia persistentGuia = em.find(Guia.class, guia.getCodigo());
            Grado grado1Old = persistentGuia.getGrado1();
            Grado grado1New = guia.getGrado1();
            Profesor duiProfesorOld = persistentGuia.getDuiProfesor();
            Profesor duiProfesorNew = guia.getDuiProfesor();
            List<String> illegalOrphanMessages = null;
            if (grado1New != null && !grado1New.equals(grado1Old)) {
                Guia oldGuiaOfGrado1 = grado1New.getGuia();
                if (oldGuiaOfGrado1 != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Grado " + grado1New + " already has an item of type Guia whose grado1 column cannot be null. Please make another selection for the grado1 field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grado1New != null) {
                grado1New = em.getReference(grado1New.getClass(), grado1New.getCodigo());
                guia.setGrado1(grado1New);
            }
            if (duiProfesorNew != null) {
                duiProfesorNew = em.getReference(duiProfesorNew.getClass(), duiProfesorNew.getDui());
                guia.setDuiProfesor(duiProfesorNew);
            }
            guia = em.merge(guia);
            if (grado1Old != null && !grado1Old.equals(grado1New)) {
                grado1Old.setGuia(null);
                grado1Old = em.merge(grado1Old);
            }
            if (grado1New != null && !grado1New.equals(grado1Old)) {
                grado1New.setGuia(guia);
                grado1New = em.merge(grado1New);
            }
            if (duiProfesorOld != null && !duiProfesorOld.equals(duiProfesorNew)) {
                duiProfesorOld.getGuiaList().remove(guia);
                duiProfesorOld = em.merge(duiProfesorOld);
            }
            if (duiProfesorNew != null && !duiProfesorNew.equals(duiProfesorOld)) {
                duiProfesorNew.getGuiaList().add(guia);
                duiProfesorNew = em.merge(duiProfesorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = guia.getCodigo();
                if (findGuia(id) == null) {
                    throw new NonexistentEntityException("The guia with id " + id + " no longer exists.");
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
            Guia guia;
            try {
                guia = em.getReference(Guia.class, id);
                guia.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The guia with id " + id + " no longer exists.", enfe);
            }
            Grado grado1 = guia.getGrado1();
            if (grado1 != null) {
                grado1.setGuia(null);
                grado1 = em.merge(grado1);
            }
            Profesor duiProfesor = guia.getDuiProfesor();
            if (duiProfesor != null) {
                duiProfesor.getGuiaList().remove(guia);
                duiProfesor = em.merge(duiProfesor);
            }
            em.remove(guia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Guia> findGuiaEntities() {
        return findGuiaEntities(true, -1, -1);
    }

    public List<Guia> findGuiaEntities(int maxResults, int firstResult) {
        return findGuiaEntities(false, maxResults, firstResult);
    }

    private List<Guia> findGuiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Guia.class));
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

    public Guia findGuia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Guia.class, id);
        } finally {
            em.close();
        }
    }

    public int getGuiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Guia> rt = cq.from(Guia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
