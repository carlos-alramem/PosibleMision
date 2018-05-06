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
import Entities.ProfPorCurso;
import java.util.ArrayList;
import java.util.List;
import Entities.ProfPorMateria;
import Entities.Profesor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlos
 */
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) throws PreexistingEntityException, Exception {
        if (profesor.getProfPorCursoList() == null) {
            profesor.setProfPorCursoList(new ArrayList<ProfPorCurso>());
        }
        if (profesor.getProfPorMateriaList() == null) {
            profesor.setProfPorMateriaList(new ArrayList<ProfPorMateria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProfPorCurso> attachedProfPorCursoList = new ArrayList<ProfPorCurso>();
            for (ProfPorCurso profPorCursoListProfPorCursoToAttach : profesor.getProfPorCursoList()) {
                profPorCursoListProfPorCursoToAttach = em.getReference(profPorCursoListProfPorCursoToAttach.getClass(), profPorCursoListProfPorCursoToAttach.getProfPorCursoPK());
                attachedProfPorCursoList.add(profPorCursoListProfPorCursoToAttach);
            }
            profesor.setProfPorCursoList(attachedProfPorCursoList);
            List<ProfPorMateria> attachedProfPorMateriaList = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListProfPorMateriaToAttach : profesor.getProfPorMateriaList()) {
                profPorMateriaListProfPorMateriaToAttach = em.getReference(profPorMateriaListProfPorMateriaToAttach.getClass(), profPorMateriaListProfPorMateriaToAttach.getProfPorMateriaPK());
                attachedProfPorMateriaList.add(profPorMateriaListProfPorMateriaToAttach);
            }
            profesor.setProfPorMateriaList(attachedProfPorMateriaList);
            em.persist(profesor);
            for (ProfPorCurso profPorCursoListProfPorCurso : profesor.getProfPorCursoList()) {
                Profesor oldProfesorOfProfPorCursoListProfPorCurso = profPorCursoListProfPorCurso.getProfesor();
                profPorCursoListProfPorCurso.setProfesor(profesor);
                profPorCursoListProfPorCurso = em.merge(profPorCursoListProfPorCurso);
                if (oldProfesorOfProfPorCursoListProfPorCurso != null) {
                    oldProfesorOfProfPorCursoListProfPorCurso.getProfPorCursoList().remove(profPorCursoListProfPorCurso);
                    oldProfesorOfProfPorCursoListProfPorCurso = em.merge(oldProfesorOfProfPorCursoListProfPorCurso);
                }
            }
            for (ProfPorMateria profPorMateriaListProfPorMateria : profesor.getProfPorMateriaList()) {
                Profesor oldProfesorOfProfPorMateriaListProfPorMateria = profPorMateriaListProfPorMateria.getProfesor();
                profPorMateriaListProfPorMateria.setProfesor(profesor);
                profPorMateriaListProfPorMateria = em.merge(profPorMateriaListProfPorMateria);
                if (oldProfesorOfProfPorMateriaListProfPorMateria != null) {
                    oldProfesorOfProfPorMateriaListProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListProfPorMateria);
                    oldProfesorOfProfPorMateriaListProfPorMateria = em.merge(oldProfesorOfProfPorMateriaListProfPorMateria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfesor(profesor.getDui()) != null) {
                throw new PreexistingEntityException("Profesor " + profesor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getDui());
            List<ProfPorCurso> profPorCursoListOld = persistentProfesor.getProfPorCursoList();
            List<ProfPorCurso> profPorCursoListNew = profesor.getProfPorCursoList();
            List<ProfPorMateria> profPorMateriaListOld = persistentProfesor.getProfPorMateriaList();
            List<ProfPorMateria> profPorMateriaListNew = profesor.getProfPorMateriaList();
            List<String> illegalOrphanMessages = null;
            for (ProfPorCurso profPorCursoListOldProfPorCurso : profPorCursoListOld) {
                if (!profPorCursoListNew.contains(profPorCursoListOldProfPorCurso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProfPorCurso " + profPorCursoListOldProfPorCurso + " since its profesor field is not nullable.");
                }
            }
            for (ProfPorMateria profPorMateriaListOldProfPorMateria : profPorMateriaListOld) {
                if (!profPorMateriaListNew.contains(profPorMateriaListOldProfPorMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProfPorMateria " + profPorMateriaListOldProfPorMateria + " since its profesor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProfPorCurso> attachedProfPorCursoListNew = new ArrayList<ProfPorCurso>();
            for (ProfPorCurso profPorCursoListNewProfPorCursoToAttach : profPorCursoListNew) {
                profPorCursoListNewProfPorCursoToAttach = em.getReference(profPorCursoListNewProfPorCursoToAttach.getClass(), profPorCursoListNewProfPorCursoToAttach.getProfPorCursoPK());
                attachedProfPorCursoListNew.add(profPorCursoListNewProfPorCursoToAttach);
            }
            profPorCursoListNew = attachedProfPorCursoListNew;
            profesor.setProfPorCursoList(profPorCursoListNew);
            List<ProfPorMateria> attachedProfPorMateriaListNew = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListNewProfPorMateriaToAttach : profPorMateriaListNew) {
                profPorMateriaListNewProfPorMateriaToAttach = em.getReference(profPorMateriaListNewProfPorMateriaToAttach.getClass(), profPorMateriaListNewProfPorMateriaToAttach.getProfPorMateriaPK());
                attachedProfPorMateriaListNew.add(profPorMateriaListNewProfPorMateriaToAttach);
            }
            profPorMateriaListNew = attachedProfPorMateriaListNew;
            profesor.setProfPorMateriaList(profPorMateriaListNew);
            profesor = em.merge(profesor);
            for (ProfPorCurso profPorCursoListNewProfPorCurso : profPorCursoListNew) {
                if (!profPorCursoListOld.contains(profPorCursoListNewProfPorCurso)) {
                    Profesor oldProfesorOfProfPorCursoListNewProfPorCurso = profPorCursoListNewProfPorCurso.getProfesor();
                    profPorCursoListNewProfPorCurso.setProfesor(profesor);
                    profPorCursoListNewProfPorCurso = em.merge(profPorCursoListNewProfPorCurso);
                    if (oldProfesorOfProfPorCursoListNewProfPorCurso != null && !oldProfesorOfProfPorCursoListNewProfPorCurso.equals(profesor)) {
                        oldProfesorOfProfPorCursoListNewProfPorCurso.getProfPorCursoList().remove(profPorCursoListNewProfPorCurso);
                        oldProfesorOfProfPorCursoListNewProfPorCurso = em.merge(oldProfesorOfProfPorCursoListNewProfPorCurso);
                    }
                }
            }
            for (ProfPorMateria profPorMateriaListNewProfPorMateria : profPorMateriaListNew) {
                if (!profPorMateriaListOld.contains(profPorMateriaListNewProfPorMateria)) {
                    Profesor oldProfesorOfProfPorMateriaListNewProfPorMateria = profPorMateriaListNewProfPorMateria.getProfesor();
                    profPorMateriaListNewProfPorMateria.setProfesor(profesor);
                    profPorMateriaListNewProfPorMateria = em.merge(profPorMateriaListNewProfPorMateria);
                    if (oldProfesorOfProfPorMateriaListNewProfPorMateria != null && !oldProfesorOfProfPorMateriaListNewProfPorMateria.equals(profesor)) {
                        oldProfesorOfProfPorMateriaListNewProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListNewProfPorMateria);
                        oldProfesorOfProfPorMateriaListNewProfPorMateria = em.merge(oldProfesorOfProfPorMateriaListNewProfPorMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = profesor.getDui();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getDui();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProfPorCurso> profPorCursoListOrphanCheck = profesor.getProfPorCursoList();
            for (ProfPorCurso profPorCursoListOrphanCheckProfPorCurso : profPorCursoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the ProfPorCurso " + profPorCursoListOrphanCheckProfPorCurso + " in its profPorCursoList field has a non-nullable profesor field.");
            }
            List<ProfPorMateria> profPorMateriaListOrphanCheck = profesor.getProfPorMateriaList();
            for (ProfPorMateria profPorMateriaListOrphanCheckProfPorMateria : profPorMateriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the ProfPorMateria " + profPorMateriaListOrphanCheckProfPorMateria + " in its profPorMateriaList field has a non-nullable profesor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
