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
import java.util.ArrayList;
import java.util.List;
import Entities.ProfPorMateria;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
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
        if (matPorCurso.getActividadList() == null) {
            matPorCurso.setActividadList(new ArrayList<Actividad>());
        }
        if (matPorCurso.getProfPorMateriaList() == null) {
            matPorCurso.setProfPorMateriaList(new ArrayList<ProfPorMateria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso codCurso = matPorCurso.getCodCurso();
            if (codCurso != null) {
                codCurso = em.getReference(codCurso.getClass(), codCurso.getCodigo());
                matPorCurso.setCodCurso(codCurso);
            }
            Materia codMateria = matPorCurso.getCodMateria();
            if (codMateria != null) {
                codMateria = em.getReference(codMateria.getClass(), codMateria.getCodigo());
                matPorCurso.setCodMateria(codMateria);
            }
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : matPorCurso.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getCodigo());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            matPorCurso.setActividadList(attachedActividadList);
            List<ProfPorMateria> attachedProfPorMateriaList = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListProfPorMateriaToAttach : matPorCurso.getProfPorMateriaList()) {
                profPorMateriaListProfPorMateriaToAttach = em.getReference(profPorMateriaListProfPorMateriaToAttach.getClass(), profPorMateriaListProfPorMateriaToAttach.getCodigo());
                attachedProfPorMateriaList.add(profPorMateriaListProfPorMateriaToAttach);
            }
            matPorCurso.setProfPorMateriaList(attachedProfPorMateriaList);
            em.persist(matPorCurso);
            if (codCurso != null) {
                codCurso.getMatPorCursoList().add(matPorCurso);
                codCurso = em.merge(codCurso);
            }
            if (codMateria != null) {
                codMateria.getMatPorCursoList().add(matPorCurso);
                codMateria = em.merge(codMateria);
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
                MatPorCurso oldCodMatPorCursoOfProfPorMateriaListProfPorMateria = profPorMateriaListProfPorMateria.getCodMatPorCurso();
                profPorMateriaListProfPorMateria.setCodMatPorCurso(matPorCurso);
                profPorMateriaListProfPorMateria = em.merge(profPorMateriaListProfPorMateria);
                if (oldCodMatPorCursoOfProfPorMateriaListProfPorMateria != null) {
                    oldCodMatPorCursoOfProfPorMateriaListProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListProfPorMateria);
                    oldCodMatPorCursoOfProfPorMateriaListProfPorMateria = em.merge(oldCodMatPorCursoOfProfPorMateriaListProfPorMateria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMatPorCurso(matPorCurso.getCodigo()) != null) {
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso persistentMatPorCurso = em.find(MatPorCurso.class, matPorCurso.getCodigo());
            Curso codCursoOld = persistentMatPorCurso.getCodCurso();
            Curso codCursoNew = matPorCurso.getCodCurso();
            Materia codMateriaOld = persistentMatPorCurso.getCodMateria();
            Materia codMateriaNew = matPorCurso.getCodMateria();
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
                    illegalOrphanMessages.add("You must retain ProfPorMateria " + profPorMateriaListOldProfPorMateria + " since its codMatPorCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codCursoNew != null) {
                codCursoNew = em.getReference(codCursoNew.getClass(), codCursoNew.getCodigo());
                matPorCurso.setCodCurso(codCursoNew);
            }
            if (codMateriaNew != null) {
                codMateriaNew = em.getReference(codMateriaNew.getClass(), codMateriaNew.getCodigo());
                matPorCurso.setCodMateria(codMateriaNew);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getCodigo());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            matPorCurso.setActividadList(actividadListNew);
            List<ProfPorMateria> attachedProfPorMateriaListNew = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListNewProfPorMateriaToAttach : profPorMateriaListNew) {
                profPorMateriaListNewProfPorMateriaToAttach = em.getReference(profPorMateriaListNewProfPorMateriaToAttach.getClass(), profPorMateriaListNewProfPorMateriaToAttach.getCodigo());
                attachedProfPorMateriaListNew.add(profPorMateriaListNewProfPorMateriaToAttach);
            }
            profPorMateriaListNew = attachedProfPorMateriaListNew;
            matPorCurso.setProfPorMateriaList(profPorMateriaListNew);
            matPorCurso = em.merge(matPorCurso);
            if (codCursoOld != null && !codCursoOld.equals(codCursoNew)) {
                codCursoOld.getMatPorCursoList().remove(matPorCurso);
                codCursoOld = em.merge(codCursoOld);
            }
            if (codCursoNew != null && !codCursoNew.equals(codCursoOld)) {
                codCursoNew.getMatPorCursoList().add(matPorCurso);
                codCursoNew = em.merge(codCursoNew);
            }
            if (codMateriaOld != null && !codMateriaOld.equals(codMateriaNew)) {
                codMateriaOld.getMatPorCursoList().remove(matPorCurso);
                codMateriaOld = em.merge(codMateriaOld);
            }
            if (codMateriaNew != null && !codMateriaNew.equals(codMateriaOld)) {
                codMateriaNew.getMatPorCursoList().add(matPorCurso);
                codMateriaNew = em.merge(codMateriaNew);
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
                    MatPorCurso oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria = profPorMateriaListNewProfPorMateria.getCodMatPorCurso();
                    profPorMateriaListNewProfPorMateria.setCodMatPorCurso(matPorCurso);
                    profPorMateriaListNewProfPorMateria = em.merge(profPorMateriaListNewProfPorMateria);
                    if (oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria != null && !oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria.equals(matPorCurso)) {
                        oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListNewProfPorMateria);
                        oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria = em.merge(oldCodMatPorCursoOfProfPorMateriaListNewProfPorMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = matPorCurso.getCodigo();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MatPorCurso matPorCurso;
            try {
                matPorCurso = em.getReference(MatPorCurso.class, id);
                matPorCurso.getCodigo();
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
                illegalOrphanMessages.add("This MatPorCurso (" + matPorCurso + ") cannot be destroyed since the ProfPorMateria " + profPorMateriaListOrphanCheckProfPorMateria + " in its profPorMateriaList field has a non-nullable codMatPorCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso codCurso = matPorCurso.getCodCurso();
            if (codCurso != null) {
                codCurso.getMatPorCursoList().remove(matPorCurso);
                codCurso = em.merge(codCurso);
            }
            Materia codMateria = matPorCurso.getCodMateria();
            if (codMateria != null) {
                codMateria.getMatPorCursoList().remove(matPorCurso);
                codMateria = em.merge(codMateria);
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

    public MatPorCurso findMatPorCurso(Integer id) {
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
