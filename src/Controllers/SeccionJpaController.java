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
import Entities.Seccion;
import Entities.SeccionPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class SeccionJpaController implements Serializable {

    public SeccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seccion seccion) throws PreexistingEntityException, Exception {
        if (seccion.getSeccionPK() == null) {
            seccion.setSeccionPK(new SeccionPK());
        }
        if (seccion.getGradoList() == null) {
            seccion.setGradoList(new ArrayList<Grado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Grado> attachedGradoList = new ArrayList<Grado>();
            for (Grado gradoListGradoToAttach : seccion.getGradoList()) {
                gradoListGradoToAttach = em.getReference(gradoListGradoToAttach.getClass(), gradoListGradoToAttach.getGradoPK());
                attachedGradoList.add(gradoListGradoToAttach);
            }
            seccion.setGradoList(attachedGradoList);
            em.persist(seccion);
            for (Grado gradoListGrado : seccion.getGradoList()) {
                Seccion oldSeccionOfGradoListGrado = gradoListGrado.getSeccion();
                gradoListGrado.setSeccion(seccion);
                gradoListGrado = em.merge(gradoListGrado);
                if (oldSeccionOfGradoListGrado != null) {
                    oldSeccionOfGradoListGrado.getGradoList().remove(gradoListGrado);
                    oldSeccionOfGradoListGrado = em.merge(oldSeccionOfGradoListGrado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeccion(seccion.getSeccionPK()) != null) {
                throw new PreexistingEntityException("Seccion " + seccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getSeccionPK());
            List<Grado> gradoListOld = persistentSeccion.getGradoList();
            List<Grado> gradoListNew = seccion.getGradoList();
            List<String> illegalOrphanMessages = null;
            for (Grado gradoListOldGrado : gradoListOld) {
                if (!gradoListNew.contains(gradoListOldGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grado " + gradoListOldGrado + " since its seccion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Grado> attachedGradoListNew = new ArrayList<Grado>();
            for (Grado gradoListNewGradoToAttach : gradoListNew) {
                gradoListNewGradoToAttach = em.getReference(gradoListNewGradoToAttach.getClass(), gradoListNewGradoToAttach.getGradoPK());
                attachedGradoListNew.add(gradoListNewGradoToAttach);
            }
            gradoListNew = attachedGradoListNew;
            seccion.setGradoList(gradoListNew);
            seccion = em.merge(seccion);
            for (Grado gradoListNewGrado : gradoListNew) {
                if (!gradoListOld.contains(gradoListNewGrado)) {
                    Seccion oldSeccionOfGradoListNewGrado = gradoListNewGrado.getSeccion();
                    gradoListNewGrado.setSeccion(seccion);
                    gradoListNewGrado = em.merge(gradoListNewGrado);
                    if (oldSeccionOfGradoListNewGrado != null && !oldSeccionOfGradoListNewGrado.equals(seccion)) {
                        oldSeccionOfGradoListNewGrado.getGradoList().remove(gradoListNewGrado);
                        oldSeccionOfGradoListNewGrado = em.merge(oldSeccionOfGradoListNewGrado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SeccionPK id = seccion.getSeccionPK();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SeccionPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getSeccionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Grado> gradoListOrphanCheck = seccion.getGradoList();
            for (Grado gradoListOrphanCheckGrado : gradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Seccion (" + seccion + ") cannot be destroyed since the Grado " + gradoListOrphanCheckGrado + " in its gradoList field has a non-nullable seccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(SeccionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
