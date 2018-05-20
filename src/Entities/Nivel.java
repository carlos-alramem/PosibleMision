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
@Table(name = "nivel", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nivel.findAll", query = "SELECT n FROM Nivel n")
    , @NamedQuery(name = "Nivel.findByCodigo", query = "SELECT n FROM Nivel n WHERE n.codigo = :codigo")
    , @NamedQuery(name = "Nivel.findByNombre", query = "SELECT n FROM Nivel n WHERE n.nombre = :nombre")})
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel")
    private List<Promedio> promedioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel")
    private List<PorcentPorMes> porcentPorMesList;

    public Nivel() {
    }

    public Nivel(Integer codigo) {
        this.codigo = codigo;
    }

    public Nivel(Integer codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
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

    @XmlTransient
    public List<Promedio> getPromedioList() {
        return promedioList;
    }

    public void setPromedioList(List<Promedio> promedioList) {
        this.promedioList = promedioList;
    }

    @XmlTransient
    public List<PorcentPorMes> getPorcentPorMesList() {
        return porcentPorMesList;
    }

    public void setPorcentPorMesList(List<PorcentPorMes> porcentPorMesList) {
        this.porcentPorMesList = porcentPorMesList;
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
        if (!(object instanceof Nivel)) {
            return false;
        }
        Nivel other = (Nivel) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Nivel[ codigo=" + codigo + " ]";
    }
    
}
