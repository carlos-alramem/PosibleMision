/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Curso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Grado;
import java.util.ArrayList;
import java.util.List;
import Entities.MatPorCurso;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) throws PreexistingEntityException, Exception {
        if (curso.getGradoList() == null) {
            curso.setGradoList(new ArrayList<Grado>());
        }
        if (curso.getMatPorCursoList() == null) {
            curso.setMatPorCursoList(new ArrayList<MatPorCurso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Grado> attachedGradoList = new ArrayList<Grado>();
            for (Grado gradoListGradoToAttach : curso.getGradoList()) {
                gradoListGradoToAttach = em.getReference(gradoListGradoToAttach.getClass(), gradoListGradoToAttach.getCodigo());
                attachedGradoList.add(gradoListGradoToAttach);
            }
            curso.setGradoList(attachedGradoList);
            List<MatPorCurso> attachedMatPorCursoList = new ArrayList<MatPorCurso>();
            for (MatPorCurso matPorCursoListMatPorCursoToAttach : curso.getMatPorCursoList()) {
                matPorCursoListMatPorCursoToAttach = em.getReference(matPorCursoListMatPorCursoToAttach.getClass(), matPorCursoListMatPorCursoToAttach.getCodigo());
                attachedMatPorCursoList.add(matPorCursoListMatPorCursoToAttach);
            }
            curso.setMatPorCursoList(attachedMatPorCursoList);
            em.persist(curso);
            for (Grado gradoListGrado : curso.getGradoList()) {
                Curso oldCodCursoOfGradoListGrado = gradoListGrado.getCodCurso();
                gradoListGrado.setCodCurso(curso);
                gradoListGrado = em.merge(gradoListGrado);
                if (oldCodCursoOfGradoListGrado != null) {
                    oldCodCursoOfGradoListGrado.getGradoList().remove(gradoListGrado);
                    oldCodCursoOfGradoListGrado = em.merge(oldCodCursoOfGradoListGrado);
                }
            }
            for (MatPorCurso matPorCursoListMatPorCurso : curso.getMatPorCursoList()) {
                Curso oldCodCursoOfMatPorCursoListMatPorCurso = matPorCursoListMatPorCurso.getCodCurso();
                matPorCursoListMatPorCurso.setCodCurso(curso);
                matPorCursoListMatPorCurso = em.merge(matPorCursoListMatPorCurso);
                if (oldCodCursoOfMatPorCursoListMatPorCurso != null) {
                    oldCodCursoOfMatPorCursoListMatPorCurso.getMatPorCursoList().remove(matPorCursoListMatPorCurso);
                    oldCodCursoOfMatPorCursoListMatPorCurso = em.merge(oldCodCursoOfMatPorCursoListMatPorCurso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCurso(curso.getCodigo()) != null) {
                throw new PreexistingEntityException("Curso " + curso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getCodigo());
            List<Grado> gradoListOld = persistentCurso.getGradoList();
            List<Grado> gradoListNew = curso.getGradoList();
            List<MatPorCurso> matPorCursoListOld = persistentCurso.getMatPorCursoList();
            List<MatPorCurso> matPorCursoListNew = curso.getMatPorCursoList();
            List<String> illegalOrphanMessages = null;
            for (Grado gradoListOldGrado : gradoListOld) {
                if (!gradoListNew.contains(gradoListOldGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grado " + gradoListOldGrado + " since its codCurso field is not nullable.");
                }
            }
            for (MatPorCurso matPorCursoListOldMatPorCurso : matPorCursoListOld) {
                if (!matPorCursoListNew.contains(matPorCursoListOldMatPorCurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MatPorCurso " + matPorCursoListOldMatPorCurso + " since its codCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Grado> attachedGradoListNew = new ArrayList<Grado>();
            for (Grado gradoListNewGradoToAttach : gradoListNew) {
                gradoListNewGradoToAttach = em.getReference(gradoListNewGradoToAttach.getClass(), gradoListNewGradoToAttach.getCodigo());
                attachedGradoListNew.add(gradoListNewGradoToAttach);
            }
            gradoListNew = attachedGradoListNew;
            curso.setGradoList(gradoListNew);
            List<MatPorCurso> attachedMatPorCursoListNew = new ArrayList<MatPorCurso>();
            for (MatPorCurso matPorCursoListNewMatPorCursoToAttach : matPorCursoListNew) {
                matPorCursoListNewMatPorCursoToAttach = em.getReference(matPorCursoListNewMatPorCursoToAttach.getClass(), matPorCursoListNewMatPorCursoToAttach.getCodigo());
                attachedMatPorCursoListNew.add(matPorCursoListNewMatPorCursoToAttach);
            }
            matPorCursoListNew = attachedMatPorCursoListNew;
            curso.setMatPorCursoList(matPorCursoListNew);
            curso = em.merge(curso);
            for (Grado gradoListNewGrado : gradoListNew) {
                if (!gradoListOld.contains(gradoListNewGrado)) {
                    Curso oldCodCursoOfGradoListNewGrado = gradoListNewGrado.getCodCurso();
                    gradoListNewGrado.setCodCurso(curso);
                    gradoListNewGrado = em.merge(gradoListNewGrado);
                    if (oldCodCursoOfGradoListNewGrado != null && !oldCodCursoOfGradoListNewGrado.equals(curso)) {
                        oldCodCursoOfGradoListNewGrado.getGradoList().remove(gradoListNewGrado);
                        oldCodCursoOfGradoListNewGrado = em.merge(oldCodCursoOfGradoListNewGrado);
                    }
                }
            }
            for (MatPorCurso matPorCursoListNewMatPorCurso : matPorCursoListNew) {
                if (!matPorCursoListOld.contains(matPorCursoListNewMatPorCurso)) {
                    Curso oldCodCursoOfMatPorCursoListNewMatPorCurso = matPorCursoListNewMatPorCurso.getCodCurso();
                    matPorCursoListNewMatPorCurso.setCodCurso(curso);
                    matPorCursoListNewMatPorCurso = em.merge(matPorCursoListNewMatPorCurso);
                    if (oldCodCursoOfMatPorCursoListNewMatPorCurso != null && !oldCodCursoOfMatPorCursoListNewMatPorCurso.equals(curso)) {
                        oldCodCursoOfMatPorCursoListNewMatPorCurso.getMatPorCursoList().remove(matPorCursoListNewMatPorCurso);
                        oldCodCursoOfMatPorCursoListNewMatPorCurso = em.merge(oldCodCursoOfMatPorCursoListNewMatPorCurso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getCodigo();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Grado> gradoListOrphanCheck = curso.getGradoList();
            for (Grado gradoListOrphanCheckGrado : gradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Grado " + gradoListOrphanCheckGrado + " in its gradoList field has a non-nullable codCurso field.");
            }
            List<MatPorCurso> matPorCursoListOrphanCheck = curso.getMatPorCursoList();
            for (MatPorCurso matPorCursoListOrphanCheckMatPorCurso : matPorCursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the MatPorCurso " + matPorCursoListOrphanCheckMatPorCurso + " in its matPorCursoList field has a non-nullable codCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
