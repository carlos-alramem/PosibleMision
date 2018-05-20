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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "grado", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g")
    , @NamedQuery(name = "Grado.findByCodigo", query = "SELECT g FROM Grado g WHERE g.codigo = :codigo")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @JoinColumn(name = "cod_curso", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Curso codCurso;
    @JoinColumn(name = "cod_seccion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Seccion codSeccion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "grado1")
    private Guia guia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grado")
    private List<Matricula> matriculaList;

    public Grado() {
    }

    public Grado(Integer codigo) {
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

    public Seccion getCodSeccion() {
        return codSeccion;
    }

    public void setCodSeccion(Seccion codSeccion) {
        this.codSeccion = codSeccion;
    }

    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Grado[ codigo=" + codigo + " ]";
    }
    
}
