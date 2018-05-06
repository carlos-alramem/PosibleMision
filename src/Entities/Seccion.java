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
@Table(name = "seccion", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seccion.findAll", query = "SELECT s FROM Seccion s")
    , @NamedQuery(name = "Seccion.findByCodigo", query = "SELECT s FROM Seccion s WHERE s.seccionPK.codigo = :codigo")
    , @NamedQuery(name = "Seccion.findByNombre", query = "SELECT s FROM Seccion s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "Seccion.findByTurno", query = "SELECT s FROM Seccion s WHERE s.seccionPK.turno = :turno")})
public class Seccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SeccionPK seccionPK;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccion")
    private List<Grado> gradoList;

    public Seccion() {
    }

    public Seccion(SeccionPK seccionPK) {
        this.seccionPK = seccionPK;
    }

    public Seccion(SeccionPK seccionPK, String nombre) {
        this.seccionPK = seccionPK;
        this.nombre = nombre;
    }

    public Seccion(int codigo, Character turno) {
        this.seccionPK = new SeccionPK(codigo, turno);
    }

    public SeccionPK getSeccionPK() {
        return seccionPK;
    }

    public void setSeccionPK(SeccionPK seccionPK) {
        this.seccionPK = seccionPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Grado> getGradoList() {
        return gradoList;
    }

    public void setGradoList(List<Grado> gradoList) {
        this.gradoList = gradoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seccionPK != null ? seccionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seccion)) {
            return false;
        }
        Seccion other = (Seccion) object;
        if ((this.seccionPK == null && other.seccionPK != null) || (this.seccionPK != null && !this.seccionPK.equals(other.seccionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Seccion[ seccionPK=" + seccionPK + " ]";
    }
    
}
