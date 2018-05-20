/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "prof_por_materia", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProfPorMateria.findAll", query = "SELECT p FROM ProfPorMateria p")
    , @NamedQuery(name = "ProfPorMateria.findByCodigo", query = "SELECT p FROM ProfPorMateria p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "ProfPorMateria.findByAnioInicio", query = "SELECT p FROM ProfPorMateria p WHERE p.anioInicio = :anioInicio")
    , @NamedQuery(name = "ProfPorMateria.findByAnioFin", query = "SELECT p FROM ProfPorMateria p WHERE p.anioFin = :anioFin")})
public class ProfPorMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;
    @Basic(optional = false)
    @Column(name = "anio_fin")
    private int anioFin;
    @JoinColumn(name = "cod_mat_por_curso", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private MatPorCurso codMatPorCurso;
    @JoinColumn(name = "dui_profesor", referencedColumnName = "dui")
    @ManyToOne(optional = false)
    private Profesor duiProfesor;

    public ProfPorMateria() {
    }

    public ProfPorMateria(Integer codigo) {
        this.codigo = codigo;
    }

    public ProfPorMateria(Integer codigo, int anioInicio, int anioFin) {
        this.codigo = codigo;
        this.anioInicio = anioInicio;
        this.anioFin = anioFin;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public int getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(int anioFin) {
        this.anioFin = anioFin;
    }

    public MatPorCurso getCodMatPorCurso() {
        return codMatPorCurso;
    }

    public void setCodMatPorCurso(MatPorCurso codMatPorCurso) {
        this.codMatPorCurso = codMatPorCurso;
    }

    public Profesor getDuiProfesor() {
        return duiProfesor;
    }

    public void setDuiProfesor(Profesor duiProfesor) {
        this.duiProfesor = duiProfesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfPorMateria)) {
            return false;
        }
        ProfPorMateria other = (ProfPorMateria) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ProfPorMateria[ codigo=" + codigo + " ]";
    }
    
}
