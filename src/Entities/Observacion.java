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
@Table(name = "observacion", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacion.findAll", query = "SELECT o FROM Observacion o")
    , @NamedQuery(name = "Observacion.findByCodigo", query = "SELECT o FROM Observacion o WHERE o.codigo = :codigo")
    , @NamedQuery(name = "Observacion.findByDescripcion", query = "SELECT o FROM Observacion o WHERE o.descripcion = :descripcion")
    , @NamedQuery(name = "Observacion.findByGravedad", query = "SELECT o FROM Observacion o WHERE o.gravedad = :gravedad")})
public class Observacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "gravedad")
    private int gravedad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "observacion")
    private List<ObsPorAlumno> obsPorAlumnoList;

    public Observacion() {
    }

    public Observacion(Integer codigo) {
        this.codigo = codigo;
    }

    public Observacion(Integer codigo, String descripcion, int gravedad) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.gravedad = gravedad;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getGravedad() {
        return gravedad;
    }

    public void setGravedad(int gravedad) {
        this.gravedad = gravedad;
    }

    @XmlTransient
    public List<ObsPorAlumno> getObsPorAlumnoList() {
        return obsPorAlumnoList;
    }

    public void setObsPorAlumnoList(List<ObsPorAlumno> obsPorAlumnoList) {
        this.obsPorAlumnoList = obsPorAlumnoList;
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
        if (!(object instanceof Observacion)) {
            return false;
        }
        Observacion other = (Observacion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Observacion[ codigo=" + codigo + " ]";
    }
    
}
