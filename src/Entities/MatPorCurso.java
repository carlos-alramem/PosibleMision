/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "mat_por_curso", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatPorCurso.findAll", query = "SELECT m FROM MatPorCurso m")
    , @NamedQuery(name = "MatPorCurso.findByCodigo", query = "SELECT m FROM MatPorCurso m WHERE m.codigo = :codigo")})
public class MatPorCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @JoinColumn(name = "cod_curso", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Curso codCurso;
    @JoinColumn(name = "cod_materia", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Materia codMateria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMatCurso")
    private List<Actividad> actividadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMatPorCurso")
    private List<ProfPorMateria> profPorMateriaList;

    public MatPorCurso() {
    }

    public MatPorCurso(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Curso getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(Curso codCurso) {
        this.codCurso = codCurso;
    }

    public Materia getCodMateria() {
        return codMateria;
    }

    public void setCodMateria(Materia codMateria) {
        this.codMateria = codMateria;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    @XmlTransient
    public List<ProfPorMateria> getProfPorMateriaList() {
        return profPorMateriaList;
    }

    public void setProfPorMateriaList(List<ProfPorMateria> profPorMateriaList) {
        this.profPorMateriaList = profPorMateriaList;
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
        if (!(object instanceof MatPorCurso)) {
            return false;
        }
        MatPorCurso other = (MatPorCurso) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MatPorCurso[ codigo=" + codigo + " ]";
    }
    
}
