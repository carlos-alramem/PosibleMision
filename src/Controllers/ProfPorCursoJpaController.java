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
import Entities.Profesor;
import Entities.Matricula;
import Entities.ProfPorCurso;
import Entities.ProfPorCursoPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class ProfPorCursoJpaController implements Serializable {

    public ProfPorCursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProfPorCurso profPorCurso) throws PreexistingEntityException, Exception {
        if (profPorCurso.getProfPorCursoPK() == null) {
            profPorCurso.setProfPorCursoPK(new ProfPorCursoPK());
        }
        if (profPorCurso.getMatriculaList() == null) {
            profPorCurso.setMatriculaList(new ArrayList<Matricula>());
        }
        profPorCurso.getProfPorCursoPK().setDuiProfesor(profPorCurso.getProfesor().getDui());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor profesor = profPorCurso.getProfesor();
            if (profesor != null) {
                profesor = em.getReference(profesor.getClass(), profesor.getDui());
                profPorCurso.setProfesor(profesor);
            }
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : profPorCurso.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaPK());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            profPorCurso.setMatriculaList(attachedMatriculaList);
            em.persist(profPorCurso);
            if (profesor != null) {
                profesor.getProfPorCursoList().add(profPorCurso);
                profesor = em.merge(profesor);
            }
            for (Matricula matriculaListMatricula : profPorCurso.getMatriculaList()) {
                ProfPorCurso oldProfPorCursoOfMatriculaListMatricula = matriculaListMatricula.getProfPorCurso();
                matriculaListMatricula.setProfPorCurso(profPorCurso);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldProfPorCursoOfMatriculaListMatricula != null) {
                    oldProfPorCursoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldProfPorCursoOfMatriculaListMatricula = em.merge(oldProfPorCursoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfPorCurso(profPorCurso.getProfPorCursoPK()) != null) {
                throw new PreexistingEntityException("ProfPorCurso " + profPorCurso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProfPorCurso profPorCurso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        profPorCurso.getProfPorCursoPK().setDuiProfesor(profPorCurso.getProfesor().getDui());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfPorCurso persistentProfPorCurso = em.find(ProfPorCurso.class, profPorCurso.getProfPorCursoPK());
            Profesor profesorOld = persistentProfPorCurso.getProfesor();
            Profesor profesorNew = profPorCurso.getProfesor();
            List<Matricula> matriculaListOld = persistentProfPorCurso.getMatriculaList();
            List<Matricula> matriculaListNew = profPorCurso.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its profPorCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (profesorNew != null) {
                profesorNew = em.getReference(profesorNew.getClass(), profesorNew.getDui());
                profPorCurso.setProfesor(profesorNew);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaPK());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            profPorCurso.setMatriculaList(matriculaListNew);
            profPorCurso = em.merge(profPorCurso);
            if (profesorOld != null && !profesorOld.equals(profesorNew)) {
                profesorOld.getProfPorCursoList().remove(profPorCurso);
                profesorOld = em.merge(profesorOld);
            }
            if (profesorNew != null && !profesorNew.equals(profesorOld)) {
                profesorNew.getProfPorCursoList().add(profPorCurso);
                profesorNew = em.merge(profesorNew);
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    ProfPorCurso oldProfPorCursoOfMatriculaListNewMatricula = matriculaListNewMatricula.getProfPorCurso();
                    matriculaListNewMatricula.setProfPorCurso(profPorCurso);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldProfPorCursoOfMatriculaListNewMatricula != null && !oldProfPorCursoOfMatriculaListNewMatricula.equals(profPorCurso)) {
                        oldProfPorCursoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldProfPorCursoOfMatriculaListNewMatricula = em.merge(oldProfPorCursoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProfPorCursoPK id = profPorCurso.getProfPorCursoPK();
                if (findProfPorCurso(id) == null) {
                    throw new NonexistentEntityException("The profPorCurso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProfPorCursoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfPorCurso profPorCurso;
            try {
                profPorCurso = em.getReference(ProfPorCurso.class, id);
                profPorCurso.getProfPorCursoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profPorCurso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matricula> matriculaListOrphanCheck = profPorCurso.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProfPorCurso (" + profPorCurso + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable profPorCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Profesor profesor = profPorCurso.getProfesor();
            if (profesor != null) {
                profesor.getProfPorCursoList().remove(profPorCurso);
                profesor = em.merge(profesor);
            }
            em.remove(profPorCurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProfPorCurso> findProfPorCursoEntities() {
        return findProfPorCursoEntities(true, -1, -1);
    }

    public List<ProfPorCurso> findProfPorCursoEntities(int maxResults, int firstResult) {
        return findProfPorCursoEntities(false, maxResults, firstResult);
    }

    private List<ProfPorCurso> findProfPorCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProfPorCurso.class));
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

    public ProfPorCurso findProfPorCurso(ProfPorCursoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProfPorCurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfPorCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProfPorCurso> rt = cq.from(ProfPorCurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
