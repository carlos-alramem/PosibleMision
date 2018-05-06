/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Entities.Alumno;
import Entities.AlumnoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Matricula;
import java.util.ArrayList;
import java.util.List;
import Entities.Nota;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) throws PreexistingEntityException, Exception {
        if (alumno.getAlumnoPK() == null) {
            alumno.setAlumnoPK(new AlumnoPK());
        }
        if (alumno.getMatriculaList() == null) {
            alumno.setMatriculaList(new ArrayList<Matricula>());
        }
        if (alumno.getNotaList() == null) {
            alumno.setNotaList(new ArrayList<Nota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : alumno.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaPK());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            alumno.setMatriculaList(attachedMatriculaList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : alumno.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getNotaPK());
                attachedNotaList.add(notaListNotaToAttach);
            }
            alumno.setNotaList(attachedNotaList);
            em.persist(alumno);
            for (Matricula matriculaListMatricula : alumno.getMatriculaList()) {
                Alumno oldAlumnoOfMatriculaListMatricula = matriculaListMatricula.getAlumno();
                matriculaListMatricula.setAlumno(alumno);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldAlumnoOfMatriculaListMatricula != null) {
                    oldAlumnoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldAlumnoOfMatriculaListMatricula = em.merge(oldAlumnoOfMatriculaListMatricula);
                }
            }
            for (Nota notaListNota : alumno.getNotaList()) {
                Alumno oldAlumnoOfNotaListNota = notaListNota.getAlumno();
                notaListNota.setAlumno(alumno);
                notaListNota = em.merge(notaListNota);
                if (oldAlumnoOfNotaListNota != null) {
                    oldAlumnoOfNotaListNota.getNotaList().remove(notaListNota);
                    oldAlumnoOfNotaListNota = em.merge(oldAlumnoOfNotaListNota);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlumno(alumno.getAlumnoPK()) != null) {
                throw new PreexistingEntityException("Alumno " + alumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getAlumnoPK());
            List<Matricula> matriculaListOld = persistentAlumno.getMatriculaList();
            List<Matricula> matriculaListNew = alumno.getMatriculaList();
            List<Nota> notaListOld = persistentAlumno.getNotaList();
            List<Nota> notaListNew = alumno.getNotaList();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its alumno field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its alumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaPK());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            alumno.setMatriculaList(matriculaListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getNotaPK());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            alumno.setNotaList(notaListNew);
            alumno = em.merge(alumno);
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Alumno oldAlumnoOfMatriculaListNewMatricula = matriculaListNewMatricula.getAlumno();
                    matriculaListNewMatricula.setAlumno(alumno);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldAlumnoOfMatriculaListNewMatricula != null && !oldAlumnoOfMatriculaListNewMatricula.equals(alumno)) {
                        oldAlumnoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldAlumnoOfMatriculaListNewMatricula = em.merge(oldAlumnoOfMatriculaListNewMatricula);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Alumno oldAlumnoOfNotaListNewNota = notaListNewNota.getAlumno();
                    notaListNewNota.setAlumno(alumno);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldAlumnoOfNotaListNewNota != null && !oldAlumnoOfNotaListNewNota.equals(alumno)) {
                        oldAlumnoOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldAlumnoOfNotaListNewNota = em.merge(oldAlumnoOfNotaListNewNota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AlumnoPK id = alumno.getAlumnoPK();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AlumnoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getAlumnoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matricula> matriculaListOrphanCheck = alumno.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable alumno field.");
            }
            List<Nota> notaListOrphanCheck = alumno.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable alumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(AlumnoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
