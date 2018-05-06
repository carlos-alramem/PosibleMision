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
import Entities.Materia;
import Entities.Actividad;
import Entities.MatPorCurso;
import Entities.MatPorCursoPK;
import java.util.ArrayList;
import java.util.List;
import Entities.ProfPorMateria;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class MatPorCursoJpaController implements Serializable {

    public MatPorCursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MatPorCurso matPorCurso) throws PreexistingEntityException, Exception {
        if (matPorCurso.getMatPorCursoPK() == null) {
            matPorCurso.setMatPorCursoPK(new MatPorCursoPK());
        }
        if (matPorCurso.getActividadList() == null) {
            matPorCurso.setActividadList(new ArrayList<Actividad>());
        }
        if (matPorCurso.getProfPorMateriaList() == null) {
            matPorCurso.setProfPorMateriaList(new ArrayList<ProfPorMateria>());
        }
        matPorCurso.getMatPorCursoPK().setCodCurso(matPorCurso.getCurso().getCursoPK().getCodigo());
        matPorCurso.getMatPorCursoPK().setCodMateria(matPorCurso.getMateria().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso curso = matPorCurso.getCurso();
            if (curso != null) {
                curso = em.getReference(curso.getClass(), curso.getCursoPK());
                matPorCurso.setCurso(curso);
            }
            Materia materia = matPorCurso.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getCodigo());
                matPorCurso.setMateria(materia);
            }
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : matPorCurso.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getActividadPK());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            matPorCurso.setActividadList(attachedActividadList);
            List<ProfPorMateria> attachedProfPorMateriaList = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListProfPorMateriaToAttach : matPorCurso.getProfPorMateriaList()) {
                profPorMateriaListProfPorMateriaToAttach = em.getReference(profPorMateriaListProfPorMateriaToAttach.getClass(), profPorMateriaListProfPorMateriaToAttach.getProfPorMateriaPK());
                attachedProfPorMateriaList.add(profPorMateriaListProfPorMateriaToAttach);
            }
            matPorCurso.setProfPorMateriaList(attachedProfPorMateriaList);
            em.persist(matPorCurso);
            if (curso != null) {
                curso.getMatPorCursoList().add(matPorCurso);
                curso = em.merge(curso);
            }
            if (materia != null) {
                materia.getMatPorCursoList().add(matPorCurso);
                materia = em.merge(materia);
            }
            for (Actividad actividadListActividad : matPorCurso.getActividadList()) {
                MatPorCurso oldCodMatCursoOfActividadListActividad = actividadListActividad.getCodMatCurso();
                actividadListActividad.setCodMatCurso(matPorCurso);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldCodMatCursoOfActividadListActividad != null) {
                    oldCodMatCursoOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldCodMatCursoOfActividadListActividad = em.merge(oldCodMatCursoOfActividadListActividad);
                }
            }
            for (ProfPorMateria profPorMateriaListProfPorMateria : matPorCurso.getProfPorMateriaList()) {
                MatPorCurso oldMatPorCursoOfProfPorMateriaListProfPorMateria = profPorMateriaListProfPorMateria.getMatPorCurso();
                profPorMateriaListProfPorMateria.setMatPorCurso(matPorCurso);
                profPorMateriaListProfPorMateria = em.merge(profPorMateriaListProfPorMateria);
                if (oldMatPorCursoOfProfPorMateriaListProfPorMateria != null) {
                    oldMatPorCursoOfProfPorMateriaListProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListProfPorMateria);
                    oldMatPorCursoOfProfPorMateriaListProfPorMateria = em.merge(oldMatPorCursoOfProfPorMateriaListProfPorMateria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMatPorCurso(matPorCurso.getMatPorCursoPK()) != null) {
                throw new PreexistingEntityException("MatPorCurso " + matPorCurso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MatPorCurso matPorCurso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        matPorCurso.getMatPorCursoPK().setCodCurso(matPorCurso.getCurso().getCursoPK().getCodigo());
        matPorCurso.getMatPorCursoPK().setCodMateria(matPorCurso.getMateria().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso persistentMatPorCurso = em.find(MatPorCurso.class, matPorCurso.getMatPorCursoPK());
            Curso cursoOld = persistentMatPorCurso.getCurso();
            Curso cursoNew = matPorCurso.getCurso();
            Materia materiaOld = persistentMatPorCurso.getMateria();
            Materia materiaNew = matPorCurso.getMateria();
            List<Actividad> actividadListOld = persistentMatPorCurso.getActividadList();
            List<Actividad> actividadListNew = matPorCurso.getActividadList();
            List<ProfPorMateria> profPorMateriaListOld = persistentMatPorCurso.getProfPorMateriaList();
            List<ProfPorMateria> profPorMateriaListNew = matPorCurso.getProfPorMateriaList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its codMatCurso field is not nullable.");
                }
            }
            for (ProfPorMateria profPorMateriaListOldProfPorMateria : profPorMateriaListOld) {
                if (!profPorMateriaListNew.contains(profPorMateriaListOldProfPorMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProfPorMateria " + profPorMateriaListOldProfPorMateria + " since its matPorCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cursoNew != null) {
                cursoNew = em.getReference(cursoNew.getClass(), cursoNew.getCursoPK());
                matPorCurso.setCurso(cursoNew);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getCodigo());
                matPorCurso.setMateria(materiaNew);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getActividadPK());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            matPorCurso.setActividadList(actividadListNew);
            List<ProfPorMateria> attachedProfPorMateriaListNew = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListNewProfPorMateriaToAttach : profPorMateriaListNew) {
                profPorMateriaListNewProfPorMateriaToAttach = em.getReference(profPorMateriaListNewProfPorMateriaToAttach.getClass(), profPorMateriaListNewProfPorMateriaToAttach.getProfPorMateriaPK());
                attachedProfPorMateriaListNew.add(profPorMateriaListNewProfPorMateriaToAttach);
            }
            profPorMateriaListNew = attachedProfPorMateriaListNew;
            matPorCurso.setProfPorMateriaList(profPorMateriaListNew);
            matPorCurso = em.merge(matPorCurso);
            if (cursoOld != null && !cursoOld.equals(cursoNew)) {
                cursoOld.getMatPorCursoList().remove(matPorCurso);
                cursoOld = em.merge(cursoOld);
            }
            if (cursoNew != null && !cursoNew.equals(cursoOld)) {
                cursoNew.getMatPorCursoList().add(matPorCurso);
                cursoNew = em.merge(cursoNew);
            }
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMatPorCursoList().remove(matPorCurso);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMatPorCursoList().add(matPorCurso);
                materiaNew = em.merge(materiaNew);
            }
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    MatPorCurso oldCodMatCursoOfActividadListNewActividad = actividadListNewActividad.getCodMatCurso();
                    actividadListNewActividad.setCodMatCurso(matPorCurso);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldCodMatCursoOfActividadListNewActividad != null && !oldCodMatCursoOfActividadListNewActividad.equals(matPorCurso)) {
                        oldCodMatCursoOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldCodMatCursoOfActividadListNewActividad = em.merge(oldCodMatCursoOfActividadListNewActividad);
                    }
                }
            }
            for (ProfPorMateria profPorMateriaListNewProfPorMateria : profPorMateriaListNew) {
                if (!profPorMateriaListOld.contains(profPorMateriaListNewProfPorMateria)) {
                    MatPorCurso oldMatPorCursoOfProfPorMateriaListNewProfPorMateria = profPorMateriaListNewProfPorMateria.getMatPorCurso();
                    profPorMateriaListNewProfPorMateria.setMatPorCurso(matPorCurso);
                    profPorMateriaListNewProfPorMateria = em.merge(profPorMateriaListNewProfPorMateria);
                    if (oldMatPorCursoOfProfPorMateriaListNewProfPorMateria != null && !oldMatPorCursoOfProfPorMateriaListNewProfPorMateria.equals(matPorCurso)) {
                        oldMatPorCursoOfProfPorMateriaListNewProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListNewProfPorMateria);
                        oldMatPorCursoOfProfPorMateriaListNewProfPorMateria = em.merge(oldMatPorCursoOfProfPorMateriaListNewProfPorMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MatPorCursoPK id = matPorCurso.getMatPorCursoPK();
                if (findMatPorCurso(id) == null) {
                    throw new NonexistentEntityException("The matPorCurso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MatPorCursoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso matPorCurso;
            try {
                matPorCurso = em.getReference(MatPorCurso.class, id);
                matPorCurso.getMatPorCursoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matPorCurso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = matPorCurso.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MatPorCurso (" + matPorCurso + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable codMatCurso field.");
            }
            List<ProfPorMateria> profPorMateriaListOrphanCheck = matPorCurso.getProfPorMateriaList();
            for (ProfPorMateria profPorMateriaListOrphanCheckProfPorMateria : profPorMateriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MatPorCurso (" + matPorCurso + ") cannot be destroyed since the ProfPorMateria " + profPorMateriaListOrphanCheckProfPorMateria + " in its profPorMateriaList field has a non-nullable matPorCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso curso = matPorCurso.getCurso();
            if (curso != null) {
                curso.getMatPorCursoList().remove(matPorCurso);
                curso = em.merge(curso);
            }
            Materia materia = matPorCurso.getMateria();
            if (materia != null) {
                materia.getMatPorCursoList().remove(matPorCurso);
                materia = em.merge(materia);
            }
            em.remove(matPorCurso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MatPorCurso> findMatPorCursoEntities() {
        return findMatPorCursoEntities(true, -1, -1);
    }

    public List<MatPorCurso> findMatPorCursoEntities(int maxResults, int firstResult) {
        return findMatPorCursoEntities(false, maxResults, firstResult);
    }

    private List<MatPorCurso> findMatPorCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MatPorCurso.class));
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

    public MatPorCurso findMatPorCurso(MatPorCursoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MatPorCurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatPorCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MatPorCurso> rt = cq.from(MatPorCurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
