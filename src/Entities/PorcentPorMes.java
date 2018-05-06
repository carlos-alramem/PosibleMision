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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "porcent_por_mes", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PorcentPorMes.findAll", query = "SELECT p FROM PorcentPorMes p")
    , @NamedQuery(name = "PorcentPorMes.findByCodigo", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentPorMesPK.codigo = :codigo")
    , @NamedQuery(name = "PorcentPorMes.findByNivel", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentPorMesPK.nivel = :nivel")
    , @NamedQuery(name = "PorcentPorMes.findByAnioInicio", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentPorMesPK.anioInicio = :anioInicio")
    , @NamedQuery(name = "PorcentPorMes.findByAnioFin", query = "SELECT p FROM PorcentPorMes p WHERE p.anioFin = :anioFin")
    , @NamedQuery(name = "PorcentPorMes.findByPeriodo", query = "SELECT p FROM PorcentPorMes p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PorcentPorMes.findByPorcentaje", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentaje = :porcentaje")
    , @NamedQuery(name = "PorcentPorMes.findByMes", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentPorMesPK.mes = :mes")})
public class PorcentPorMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PorcentPorMesPK porcentPorMesPK;
    @Basic(optional = false)
    @Column(name = "anio_fin")
    private int anioFin;
    @Basic(optional = false)
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @Column(name = "porcentaje")
    private int porcentaje;

    public PorcentPorMes() {
    }

    public PorcentPorMes(PorcentPorMesPK porcentPorMesPK) {
        this.porcentPorMesPK = porcentPorMesPK;
    }

    public PorcentPorMes(PorcentPorMesPK porcentPorMesPK, int anioFin, int periodo, int porcentaje) {
        this.porcentPorMesPK = porcentPorMesPK;
        this.anioFin = anioFin;
        this.periodo = periodo;
        this.porcentaje = porcentaje;
    }

    public PorcentPorMes(int codigo, int nivel, int anioInicio, int mes) {
        this.porcentPorMesPK = new PorcentPorMesPK(codigo, nivel, anioInicio, mes);
    }

    public PorcentPorMesPK getPorcentPorMesPK() {
        return porcentPorMesPK;
    }

    public void setPorcentPorMesPK(PorcentPorMesPK porcentPorMesPK) {
        this.porcentPorMesPK = porcentPorMesPK;
    }

    public int getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(int anioFin) {
        this.anioFin = anioFin;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (porcentPorMesPK != null ? porcentPorMesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PorcentPorMes)) {
            return false;
        }
        PorcentPorMes other = (PorcentPorMes) object;
        if ((this.porcentPorMesPK == null && other.porcentPorMesPK != null) || (this.porcentPorMesPK != null && !this.porcentPorMesPK.equals(other.porcentPorMesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.PorcentPorMes[ porcentPorMesPK=" + porcentPorMesPK + " ]";
    }
    
}
