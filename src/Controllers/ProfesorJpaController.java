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
import Entities.ProfPorMateria;
import java.util.ArrayList;
import java.util.List;
import Entities.Guia;
import Entities.Profesor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carlo
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
        if (profesor.getProfPorMateriaList() == null) {
            profesor.setProfPorMateriaList(new ArrayList<ProfPorMateria>());
        }
        if (profesor.getGuiaList() == null) {
            profesor.setGuiaList(new ArrayList<Guia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProfPorMateria> attachedProfPorMateriaList = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListProfPorMateriaToAttach : profesor.getProfPorMateriaList()) {
                profPorMateriaListProfPorMateriaToAttach = em.getReference(profPorMateriaListProfPorMateriaToAttach.getClass(), profPorMateriaListProfPorMateriaToAttach.getCodigo());
                attachedProfPorMateriaList.add(profPorMateriaListProfPorMateriaToAttach);
            }
            profesor.setProfPorMateriaList(attachedProfPorMateriaList);
            List<Guia> attachedGuiaList = new ArrayList<Guia>();
            for (Guia guiaListGuiaToAttach : profesor.getGuiaList()) {
                guiaListGuiaToAttach = em.getReference(guiaListGuiaToAttach.getClass(), guiaListGuiaToAttach.getCodigo());
                attachedGuiaList.add(guiaListGuiaToAttach);
            }
            profesor.setGuiaList(attachedGuiaList);
            em.persist(profesor);
            for (ProfPorMateria profPorMateriaListProfPorMateria : profesor.getProfPorMateriaList()) {
                Profesor oldDuiProfesorOfProfPorMateriaListProfPorMateria = profPorMateriaListProfPorMateria.getDuiProfesor();
                profPorMateriaListProfPorMateria.setDuiProfesor(profesor);
                profPorMateriaListProfPorMateria = em.merge(profPorMateriaListProfPorMateria);
                if (oldDuiProfesorOfProfPorMateriaListProfPorMateria != null) {
                    oldDuiProfesorOfProfPorMateriaListProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListProfPorMateria);
                    oldDuiProfesorOfProfPorMateriaListProfPorMateria = em.merge(oldDuiProfesorOfProfPorMateriaListProfPorMateria);
                }
            }
            for (Guia guiaListGuia : profesor.getGuiaList()) {
                Profesor oldDuiProfesorOfGuiaListGuia = guiaListGuia.getDuiProfesor();
                guiaListGuia.setDuiProfesor(profesor);
                guiaListGuia = em.merge(guiaListGuia);
                if (oldDuiProfesorOfGuiaListGuia != null) {
                    oldDuiProfesorOfGuiaListGuia.getGuiaList().remove(guiaListGuia);
                    oldDuiProfesorOfGuiaListGuia = em.merge(oldDuiProfesorOfGuiaListGuia);
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
            List<ProfPorMateria> profPorMateriaListOld = persistentProfesor.getProfPorMateriaList();
            List<ProfPorMateria> profPorMateriaListNew = profesor.getProfPorMateriaList();
            List<Guia> guiaListOld = persistentProfesor.getGuiaList();
            List<Guia> guiaListNew = profesor.getGuiaList();
            List<String> illegalOrphanMessages = null;
            for (ProfPorMateria profPorMateriaListOldProfPorMateria : profPorMateriaListOld) {
                if (!profPorMateriaListNew.contains(profPorMateriaListOldProfPorMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProfPorMateria " + profPorMateriaListOldProfPorMateria + " since its duiProfesor field is not nullable.");
                }
            }
            for (Guia guiaListOldGuia : guiaListOld) {
                if (!guiaListNew.contains(guiaListOldGuia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Guia " + guiaListOldGuia + " since its duiProfesor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProfPorMateria> attachedProfPorMateriaListNew = new ArrayList<ProfPorMateria>();
            for (ProfPorMateria profPorMateriaListNewProfPorMateriaToAttach : profPorMateriaListNew) {
                profPorMateriaListNewProfPorMateriaToAttach = em.getReference(profPorMateriaListNewProfPorMateriaToAttach.getClass(), profPorMateriaListNewProfPorMateriaToAttach.getCodigo());
                attachedProfPorMateriaListNew.add(profPorMateriaListNewProfPorMateriaToAttach);
            }
            profPorMateriaListNew = attachedProfPorMateriaListNew;
            profesor.setProfPorMateriaList(profPorMateriaListNew);
            List<Guia> attachedGuiaListNew = new ArrayList<Guia>();
            for (Guia guiaListNewGuiaToAttach : guiaListNew) {
                guiaListNewGuiaToAttach = em.getReference(guiaListNewGuiaToAttach.getClass(), guiaListNewGuiaToAttach.getCodigo());
                attachedGuiaListNew.add(guiaListNewGuiaToAttach);
            }
            guiaListNew = attachedGuiaListNew;
            profesor.setGuiaList(guiaListNew);
            profesor = em.merge(profesor);
            for (ProfPorMateria profPorMateriaListNewProfPorMateria : profPorMateriaListNew) {
                if (!profPorMateriaListOld.contains(profPorMateriaListNewProfPorMateria)) {
                    Profesor oldDuiProfesorOfProfPorMateriaListNewProfPorMateria = profPorMateriaListNewProfPorMateria.getDuiProfesor();
                    profPorMateriaListNewProfPorMateria.setDuiProfesor(profesor);
                    profPorMateriaListNewProfPorMateria = em.merge(profPorMateriaListNewProfPorMateria);
                    if (oldDuiProfesorOfProfPorMateriaListNewProfPorMateria != null && !oldDuiProfesorOfProfPorMateriaListNewProfPorMateria.equals(profesor)) {
                        oldDuiProfesorOfProfPorMateriaListNewProfPorMateria.getProfPorMateriaList().remove(profPorMateriaListNewProfPorMateria);
                        oldDuiProfesorOfProfPorMateriaListNewProfPorMateria = em.merge(oldDuiProfesorOfProfPorMateriaListNewProfPorMateria);
                    }
                }
            }
            for (Guia guiaListNewGuia : guiaListNew) {
                if (!guiaListOld.contains(guiaListNewGuia)) {
                    Profesor oldDuiProfesorOfGuiaListNewGuia = guiaListNewGuia.getDuiProfesor();
                    guiaListNewGuia.setDuiProfesor(profesor);
                    guiaListNewGuia = em.merge(guiaListNewGuia);
                    if (oldDuiProfesorOfGuiaListNewGuia != null && !oldDuiProfesorOfGuiaListNewGuia.equals(profesor)) {
                        oldDuiProfesorOfGuiaListNewGuia.getGuiaList().remove(guiaListNewGuia);
                        oldDuiProfesorOfGuiaListNewGuia = em.merge(oldDuiProfesorOfGuiaListNewGuia);
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
            List<ProfPorMateria> profPorMateriaListOrphanCheck = profesor.getProfPorMateriaList();
            for (ProfPorMateria profPorMateriaListOrphanCheckProfPorMateria : profPorMateriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the ProfPorMateria " + profPorMateriaListOrphanCheckProfPorMateria + " in its profPorMateriaList field has a non-nullable duiProfesor field.");
            }
            List<Guia> guiaListOrphanCheck = profesor.getGuiaList();
            for (Guia guiaListOrphanCheckGuia : guiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the Guia " + guiaListOrphanCheckGuia + " in its guiaList field has a non-nullable duiProfesor field.");
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
