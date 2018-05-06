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
@Table(name = "promedio", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promedio.findAll", query = "SELECT p FROM Promedio p")
    , @NamedQuery(name = "Promedio.findByCodigo", query = "SELECT p FROM Promedio p WHERE p.promedioPK.codigo = :codigo")
    , @NamedQuery(name = "Promedio.findByNivel", query = "SELECT p FROM Promedio p WHERE p.promedioPK.nivel = :nivel")
    , @NamedQuery(name = "Promedio.findByAnioInicio", query = "SELECT p FROM Promedio p WHERE p.promedioPK.anioInicio = :anioInicio")
    , @NamedQuery(name = "Promedio.findByAnioFin", query = "SELECT p FROM Promedio p WHERE p.promedioPK.anioFin = :anioFin")
    , @NamedQuery(name = "Promedio.findByMes", query = "SELECT p FROM Promedio p WHERE p.promedioPK.mes = :mes")
    , @NamedQuery(name = "Promedio.findByValor", query = "SELECT p FROM Promedio p WHERE p.valor = :valor")})
public class Promedio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PromedioPK promedioPK;
    @Basic(optional = false)
    @Column(name = "valor")
    private int valor;
    @JoinColumn(name = "nivel", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Nivel nivel1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promedio")
    private List<Actividad> actividadList;

    public Promedio() {
    }

    public Promedio(PromedioPK promedioPK) {
        this.promedioPK = promedioPK;
    }

    public Promedio(PromedioPK promedioPK, int valor) {
        this.promedioPK = promedioPK;
        this.valor = valor;
    }

    public Promedio(int codigo, int nivel, int anioInicio, int anioFin, int mes) {
        this.promedioPK = new PromedioPK(codigo, nivel, anioInicio, anioFin, mes);
    }

    public PromedioPK getPromedioPK() {
        return promedioPK;
    }

    public void setPromedioPK(PromedioPK promedioPK) {
        this.promedioPK = promedioPK;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Nivel getNivel1() {
        return nivel1;
    }

    public void setNivel1(Nivel nivel1) {
        this.nivel1 = nivel1;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promedioPK != null ? promedioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Promedio)) {
            return false;
        }
        Promedio other = (Promedio) object;
        if ((this.promedioPK == null && other.promedioPK != null) || (this.promedioPK != null && !this.promedioPK.equals(other.promedioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Promedio[ promedioPK=" + promedioPK + " ]";
    }
    
}
