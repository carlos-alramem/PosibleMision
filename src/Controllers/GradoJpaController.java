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
import Entities.Curso;
import Entities.Grado;
import Entities.Seccion;
import Entities.Guia;
import Entities.Matricula;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
 */
public class GradoJpaController implements Serializable {

    public GradoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) throws PreexistingEntityException, Exception {
        if (grado.getMatriculaList() == null) {
            grado.setMatriculaList(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso codCurso = grado.getCodCurso();
            if (codCurso != null) {
                codCurso = em.getReference(codCurso.getClass(), codCurso.getCodigo());
                grado.setCodCurso(codCurso);
            }
            Seccion codSeccion = grado.getCodSeccion();
            if (codSeccion != null) {
                codSeccion = em.getReference(codSeccion.getClass(), codSeccion.getCodigo());
                grado.setCodSeccion(codSeccion);
            }
            Guia guia = grado.getGuia();
            if (guia != null) {
                guia = em.getReference(guia.getClass(), guia.getCodigo());
                grado.setGuia(guia);
            }
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : grado.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaPK());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            grado.setMatriculaList(attachedMatriculaList);
            em.persist(grado);
            if (codCurso != null) {
                codCurso.getGradoList().add(grado);
                codCurso = em.merge(codCurso);
            }
            if (codSeccion != null) {
                codSeccion.getGradoList().add(grado);
                codSeccion = em.merge(codSeccion);
            }
            if (guia != null) {
                Grado oldGrado1OfGuia = guia.getGrado1();
                if (oldGrado1OfGuia != null) {
                    oldGrado1OfGuia.setGuia(null);
                    oldGrado1OfGuia = em.merge(oldGrado1OfGuia);
                }
                guia.setGrado1(grado);
                guia = em.merge(guia);
            }
            for (Matricula matriculaListMatricula : grado.getMatriculaList()) {
                Grado oldGradoOfMatriculaListMatricula = matriculaListMatricula.getGrado();
                matriculaListMatricula.setGrado(grado);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldGradoOfMatriculaListMatricula != null) {
                    oldGradoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldGradoOfMatriculaListMatricula = em.merge(oldGradoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrado(grado.getCodigo()) != null) {
                throw new PreexistingEntityException("Grado " + grado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getCodigo());
            Curso codCursoOld = persistentGrado.getCodCurso();
            Curso codCursoNew = grado.getCodCurso();
            Seccion codSeccionOld = persistentGrado.getCodSeccion();
            Seccion codSeccionNew = grado.getCodSeccion();
            Guia guiaOld = persistentGrado.getGuia();
            Guia guiaNew = grado.getGuia();
            List<Matricula> matriculaListOld = persistentGrado.getMatriculaList();
            List<Matricula> matriculaListNew = grado.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            if (guiaOld != null && !guiaOld.equals(guiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Guia " + guiaOld + " since its grado1 field is not nullable.");
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its grado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codCursoNew != null) {
                codCursoNew = em.getReference(codCursoNew.getClass(), codCursoNew.getCodigo());
                grado.setCodCurso(codCursoNew);
            }
            if (codSeccionNew != null) {
                codSeccionNew = em.getReference(codSeccionNew.getClass(), codSeccionNew.getCodigo());
                grado.setCodSeccion(codSeccionNew);
            }
            if (guiaNew != null) {
                guiaNew = em.getReference(guiaNew.getClass(), guiaNew.getCodigo());
                grado.setGuia(guiaNew);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaPK());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            grado.setMatriculaList(matriculaListNew);
            grado = em.merge(grado);
            if (codCursoOld != null && !codCursoOld.equals(codCursoNew)) {
                codCursoOld.getGradoList().remove(grado);
                codCursoOld = em.merge(codCursoOld);
            }
            if (codCursoNew != null && !codCursoNew.equals(codCursoOld)) {
                codCursoNew.getGradoList().add(grado);
                codCursoNew = em.merge(codCursoNew);
            }
            if (codSeccionOld != null && !codSeccionOld.equals(codSeccionNew)) {
                codSeccionOld.getGradoList().remove(grado);
                codSeccionOld = em.merge(codSeccionOld);
            }
            if (codSeccionNew != null && !codSeccionNew.equals(codSeccionOld)) {
                codSeccionNew.getGradoList().add(grado);
                codSeccionNew = em.merge(codSeccionNew);
            }
            if (guiaNew != null && !guiaNew.equals(guiaOld)) {
                Grado oldGrado1OfGuia = guiaNew.getGrado1();
                if (oldGrado1OfGuia != null) {
                    oldGrado1OfGuia.setGuia(null);
                    oldGrado1OfGuia = em.merge(oldGrado1OfGuia);
                }
                guiaNew.setGrado1(grado);
                guiaNew = em.merge(guiaNew);
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Grado oldGradoOfMatriculaListNewMatricula = matriculaListNewMatricula.getGrado();
                    matriculaListNewMatricula.setGrado(grado);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldGradoOfMatriculaListNewMatricula != null && !oldGradoOfMatriculaListNewMatricula.equals(grado)) {
                        oldGradoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldGradoOfMatriculaListNewMatricula = em.merge(oldGradoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grado.getCodigo();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
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
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Guia guiaOrphanCheck = grado.getGuia();
            if (guiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the Guia " + guiaOrphanCheck + " in its guia field has a non-nullable grado1 field.");
            }
            List<Matricula> matriculaListOrphanCheck = grado.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable grado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso codCurso = grado.getCodCurso();
            if (codCurso != null) {
                codCurso.getGradoList().remove(grado);
                codCurso = em.merge(codCurso);
            }
            Seccion codSeccion = grado.getCodSeccion();
            if (codSeccion != null) {
                codSeccion.getGradoList().remove(grado);
                codSeccion = em.merge(codSeccion);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
