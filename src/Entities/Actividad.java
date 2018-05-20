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
@Table(name = "actividad", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a")
    , @NamedQuery(name = "Actividad.findByCodigo", query = "SELECT a FROM Actividad a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "Actividad.findByActiva", query = "SELECT a FROM Actividad a WHERE a.activa = :activa")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "activa")
    private boolean activa;
    @JoinColumn(name = "cod_mat_curso", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private MatPorCurso codMatCurso;
    @JoinColumn(name = "cod_nom_actividad", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private NomActividad codNomActividad;
    @JoinColumn(name = "cod_promedio", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Promedio codPromedio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividad")
    private List<Nota> notaList;

    public Actividad() {
    }

    public Actividad(Integer codigo) {
        this.codigo = codigo;
    }

    public Actividad(Integer codigo, boolean activa) {
        this.codigo = codigo;
        this.activa = activa;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public MatPorCurso getCodMatCurso() {
        return codMatCurso;
    }

    public void setCodMatCurso(MatPorCurso codMatCurso) {
        this.codMatCurso = codMatCurso;
    }

    public NomActividad getCodNomActividad() {
        return codNomActividad;
    }

    public void setCodNomActividad(NomActividad codNomActividad) {
        this.codNomActividad = codNomActividad;
    }

    public Promedio getCodPromedio() {
        return codPromedio;
    }

    public void setCodPromedio(Promedio codPromedio) {
        this.codPromedio = codPromedio;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
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
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Actividad[ codigo=" + codigo + " ]";
    }
    
}
