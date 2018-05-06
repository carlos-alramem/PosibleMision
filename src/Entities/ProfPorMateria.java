/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "prof_por_materia", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProfPorMateria.findAll", query = "SELECT p FROM ProfPorMateria p")
    , @NamedQuery(name = "ProfPorMateria.findByCodigo", query = "SELECT p FROM ProfPorMateria p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "ProfPorMateria.findByDuiProfesor", query = "SELECT p FROM ProfPorMateria p WHERE p.profPorMateriaPK.duiProfesor = :duiProfesor")
    , @NamedQuery(name = "ProfPorMateria.findByCodMatPorCurso", query = "SELECT p FROM ProfPorMateria p WHERE p.profPorMateriaPK.codMatPorCurso = :codMatPorCurso")
    , @NamedQuery(name = "ProfPorMateria.findByAnioInicio", query = "SELECT p FROM ProfPorMateria p WHERE p.profPorMateriaPK.anioInicio = :anioInicio")
    , @NamedQuery(name = "ProfPorMateria.findByAnioFin", query = "SELECT p FROM ProfPorMateria p WHERE p.anioFin = :anioFin")})
public class ProfPorMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProfPorMateriaPK profPorMateriaPK;
    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Column(name = "anio_fin")
    private Integer anioFin;
    @JoinColumn(name = "cod_mat_por_curso", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MatPorCurso matPorCurso;
    @JoinColumn(name = "dui_profesor", referencedColumnName = "dui", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Profesor profesor;

    public ProfPorMateria() {
    }

    public ProfPorMateria(ProfPorMateriaPK profPorMateriaPK) {
        this.profPorMateriaPK = profPorMateriaPK;
    }

    public ProfPorMateria(ProfPorMateriaPK profPorMateriaPK, int codigo) {
        this.profPorMateriaPK = profPorMateriaPK;
        this.codigo = codigo;
    }

    public ProfPorMateria(String duiProfesor, int codMatPorCurso, int anioInicio) {
        this.profPorMateriaPK = new ProfPorMateriaPK(duiProfesor, codMatPorCurso, anioInicio);
    }

    public ProfPorMateriaPK getProfPorMateriaPK() {
        return profPorMateriaPK;
    }

    public void setProfPorMateriaPK(ProfPorMateriaPK profPorMateriaPK) {
        this.profPorMateriaPK = profPorMateriaPK;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }

    public MatPorCurso getMatPorCurso() {
        return matPorCurso;
    }

    public void setMatPorCurso(MatPorCurso matPorCurso) {
        this.matPorCurso = matPorCurso;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profPorMateriaPK != null ? profPorMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfPorMateria)) {
            return false;
        }
        ProfPorMateria other = (ProfPorMateria) object;
        if ((this.profPorMateriaPK == null && other.profPorMateriaPK != null) || (this.profPorMateriaPK != null && !this.profPorMateriaPK.equals(other.profPorMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ProfPorMateria[ profPorMateriaPK=" + profPorMateriaPK + " ]";
    }
    
}
