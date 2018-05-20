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
import Entities.MatPorCurso;
import Entities.Materia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) throws PreexistingEntityException, Exception {
        if (materia.getMatPorCursoList() == null) {
            materia.setMatPorCursoList(new ArrayList<MatPorCurso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MatPorCurso> attachedMatPorCursoList = new ArrayList<MatPorCurso>();
            for (MatPorCurso matPorCursoListMatPorCursoToAttach : materia.getMatPorCursoList()) {
                matPorCursoListMatPorCursoToAttach = em.getReference(matPorCursoListMatPorCursoToAttach.getClass(), matPorCursoListMatPorCursoToAttach.getCodigo());
                attachedMatPorCursoList.add(matPorCursoListMatPorCursoToAttach);
            }
            materia.setMatPorCursoList(attachedMatPorCursoList);
            em.persist(materia);
            for (MatPorCurso matPorCursoListMatPorCurso : materia.getMatPorCursoList()) {
                Materia oldCodMateriaOfMatPorCursoListMatPorCurso = matPorCursoListMatPorCurso.getCodMateria();
                matPorCursoListMatPorCurso.setCodMateria(materia);
                matPorCursoListMatPorCurso = em.merge(matPorCursoListMatPorCurso);
                if (oldCodMateriaOfMatPorCursoListMatPorCurso != null) {
                    oldCodMateriaOfMatPorCursoListMatPorCurso.getMatPorCursoList().remove(matPorCursoListMatPorCurso);
                    oldCodMateriaOfMatPorCursoListMatPorCurso = em.merge(oldCodMateriaOfMatPorCursoListMatPorCurso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateria(materia.getCodigo()) != null) {
                throw new PreexistingEntityException("Materia " + materia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getCodigo());
            List<MatPorCurso> matPorCursoListOld = persistentMateria.getMatPorCursoList();
            List<MatPorCurso> matPorCursoListNew = materia.getMatPorCursoList();
            List<String> illegalOrphanMessages = null;
            for (MatPorCurso matPorCursoListOldMatPorCurso : matPorCursoListOld) {
                if (!matPorCursoListNew.contains(matPorCursoListOldMatPorCurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MatPorCurso " + matPorCursoListOldMatPorCurso + " since its codMateria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MatPorCurso> attachedMatPorCursoListNew = new ArrayList<MatPorCurso>();
            for (MatPorCurso matPorCursoListNewMatPorCursoToAttach : matPorCursoListNew) {
                matPorCursoListNewMatPorCursoToAttach = em.getReference(matPorCursoListNewMatPorCursoToAttach.getClass(), matPorCursoListNewMatPorCursoToAttach.getCodigo());
                attachedMatPorCursoListNew.add(matPorCursoListNewMatPorCursoToAttach);
            }
            matPorCursoListNew = attachedMatPorCursoListNew;
            materia.setMatPorCursoList(matPorCursoListNew);
            materia = em.merge(materia);
            for (MatPorCurso matPorCursoListNewMatPorCurso : matPorCursoListNew) {
                if (!matPorCursoListOld.contains(matPorCursoListNewMatPorCurso)) {
                    Materia oldCodMateriaOfMatPorCursoListNewMatPorCurso = matPorCursoListNewMatPorCurso.getCodMateria();
                    matPorCursoListNewMatPorCurso.setCodMateria(materia);
                    matPorCursoListNewMatPorCurso = em.merge(matPorCursoListNewMatPorCurso);
                    if (oldCodMateriaOfMatPorCursoListNewMatPorCurso != null && !oldCodMateriaOfMatPorCursoListNewMatPorCurso.equals(materia)) {
                        oldCodMateriaOfMatPorCursoListNewMatPorCurso.getMatPorCursoList().remove(matPorCursoListNewMatPorCurso);
                        oldCodMateriaOfMatPorCursoListNewMatPorCurso = em.merge(oldCodMateriaOfMatPorCursoListNewMatPorCurso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getCodigo();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MatPorCurso> matPorCursoListOrphanCheck = materia.getMatPorCursoList();
            for (MatPorCurso matPorCursoListOrphanCheckMatPorCurso : matPorCursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MatPorCurso " + matPorCursoListOrphanCheckMatPorCurso + " in its matPorCursoList field has a non-nullable codMateria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
