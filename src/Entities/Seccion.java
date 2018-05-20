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
@Table(name = "seccion", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seccion.findAll", query = "SELECT s FROM Seccion s")
    , @NamedQuery(name = "Seccion.findByCodigo", query = "SELECT s FROM Seccion s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Seccion.findByNombre", query = "SELECT s FROM Seccion s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "Seccion.findByTurno", query = "SELECT s FROM Seccion s WHERE s.turno = :turno")})
public class Seccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "turno")
    private Character turno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codSeccion")
    private List<Grado> gradoList;

    public Seccion() {
    }

    public Seccion(Integer codigo) {
        this.codigo = codigo;
    }

    public Seccion(Integer codigo, String nombre, Character turno) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.turno = turno;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getTurno() {
        return turno;
    }

    public void setTurno(Character turno) {
        this.turno = turno;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seccion)) {
            return false;
        }
        Seccion other = (Seccion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Seccion[ codigo=" + codigo + " ]";
    }
    
}
