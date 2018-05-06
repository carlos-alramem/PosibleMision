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
@Table(name = "grado", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g")
    , @NamedQuery(name = "Grado.findByCodigo", query = "SELECT g FROM Grado g WHERE g.codigo = :codigo")
    , @NamedQuery(name = "Grado.findByCodSeccion", query = "SELECT g FROM Grado g WHERE g.gradoPK.codSeccion = :codSeccion")
    , @NamedQuery(name = "Grado.findByCodCurso", query = "SELECT g FROM Grado g WHERE g.gradoPK.codCurso = :codCurso")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GradoPK gradoPK;
    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @JoinColumn(name = "cod_curso", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Curso curso;
    @JoinColumn(name = "cod_seccion", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Seccion seccion;

    public Grado() {
    }

    public Grado(GradoPK gradoPK) {
        this.gradoPK = gradoPK;
    }

    public Grado(GradoPK gradoPK, int codigo) {
        this.gradoPK = gradoPK;
        this.codigo = codigo;
    }

    public Grado(int codSeccion, int codCurso) {
        this.gradoPK = new GradoPK(codSeccion, codCurso);
    }

    public GradoPK getGradoPK() {
        return gradoPK;
    }

    public void setGradoPK(GradoPK gradoPK) {
        this.gradoPK = gradoPK;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gradoPK != null ? gradoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.gradoPK == null && other.gradoPK != null) || (this.gradoPK != null && !this.gradoPK.equals(other.gradoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Grado[ gradoPK=" + gradoPK + " ]";
    }
    
}
