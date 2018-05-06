/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author carlos
 */
@Entity
@Table(name = "mat_por_curso", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatPorCurso.findAll", query = "SELECT m FROM MatPorCurso m")
    , @NamedQuery(name = "MatPorCurso.findByCodigo", query = "SELECT m FROM MatPorCurso m WHERE m.matPorCursoPK.codigo = :codigo")
    , @NamedQuery(name = "MatPorCurso.findByCodCurso", query = "SELECT m FROM MatPorCurso m WHERE m.matPorCursoPK.codCurso = :codCurso")
    , @NamedQuery(name = "MatPorCurso.findByCodMateria", query = "SELECT m FROM MatPorCurso m WHERE m.matPorCursoPK.codMateria = :codMateria")})
public class MatPorCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatPorCursoPK matPorCursoPK;
    @JoinColumn(name = "cod_curso", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Curso curso;
    @JoinColumn(name = "cod_materia", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMatCurso")
    private List<Actividad> actividadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matPorCurso")
    private List<ProfPorMateria> profPorMateriaList;

    public MatPorCurso() {
    }

    public MatPorCurso(MatPorCursoPK matPorCursoPK) {
        this.matPorCursoPK = matPorCursoPK;
    }

    public MatPorCurso(int codigo, int codCurso, int codMateria) {
        this.matPorCursoPK = new MatPorCursoPK(codigo, codCurso, codMateria);
    }

    public MatPorCursoPK getMatPorCursoPK() {
        return matPorCursoPK;
    }

    public void setMatPorCursoPK(MatPorCursoPK matPorCursoPK) {
        this.matPorCursoPK = matPorCursoPK;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
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
        hash += (matPorCursoPK != null ? matPorCursoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatPorCurso)) {
            return false;
        }
        MatPorCurso other = (MatPorCurso) object;
        if ((this.matPorCursoPK == null && other.matPorCursoPK != null) || (this.matPorCursoPK != null && !this.matPorCursoPK.equals(other.matPorCursoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MatPorCurso[ matPorCursoPK=" + matPorCursoPK + " ]";
    }
    
}
