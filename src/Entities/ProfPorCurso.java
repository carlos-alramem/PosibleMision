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
@Table(name = "prof_por_curso", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProfPorCurso.findAll", query = "SELECT p FROM ProfPorCurso p")
    , @NamedQuery(name = "ProfPorCurso.findByCodigo", query = "SELECT p FROM ProfPorCurso p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "ProfPorCurso.findByDuiProfesor", query = "SELECT p FROM ProfPorCurso p WHERE p.profPorCursoPK.duiProfesor = :duiProfesor")
    , @NamedQuery(name = "ProfPorCurso.findByCodSecPorCurso", query = "SELECT p FROM ProfPorCurso p WHERE p.profPorCursoPK.codSecPorCurso = :codSecPorCurso")
    , @NamedQuery(name = "ProfPorCurso.findByAnioInicio", query = "SELECT p FROM ProfPorCurso p WHERE p.profPorCursoPK.anioInicio = :anioInicio")
    , @NamedQuery(name = "ProfPorCurso.findByAnioFin", query = "SELECT p FROM ProfPorCurso p WHERE p.anioFin = :anioFin")})
public class ProfPorCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProfPorCursoPK profPorCursoPK;
    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "anio_fin")
    private int anioFin;
    @JoinColumn(name = "dui_profesor", referencedColumnName = "dui", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Profesor profesor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profPorCurso")
    private List<Matricula> matriculaList;

    public ProfPorCurso() {
    }

    public ProfPorCurso(ProfPorCursoPK profPorCursoPK) {
        this.profPorCursoPK = profPorCursoPK;
    }

    public ProfPorCurso(ProfPorCursoPK profPorCursoPK, int codigo, int anioFin) {
        this.profPorCursoPK = profPorCursoPK;
        this.codigo = codigo;
        this.anioFin = anioFin;
    }

    public ProfPorCurso(String duiProfesor, int codSecPorCurso, int anioInicio) {
        this.profPorCursoPK = new ProfPorCursoPK(duiProfesor, codSecPorCurso, anioInicio);
    }

    public ProfPorCursoPK getProfPorCursoPK() {
        return profPorCursoPK;
    }

    public void setProfPorCursoPK(ProfPorCursoPK profPorCursoPK) {
        this.profPorCursoPK = profPorCursoPK;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(int anioFin) {
        this.anioFin = anioFin;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profPorCursoPK != null ? profPorCursoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfPorCurso)) {
            return false;
        }
        ProfPorCurso other = (ProfPorCurso) object;
        if ((this.profPorCursoPK == null && other.profPorCursoPK != null) || (this.profPorCursoPK != null && !this.profPorCursoPK.equals(other.profPorCursoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ProfPorCurso[ profPorCursoPK=" + profPorCursoPK + " ]";
    }
    
}
