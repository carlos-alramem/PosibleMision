/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "porcent_por_mes", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PorcentPorMes.findAll", query = "SELECT p FROM PorcentPorMes p")
    , @NamedQuery(name = "PorcentPorMes.findByCodigo", query = "SELECT p FROM PorcentPorMes p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "PorcentPorMes.findByAnioInicio", query = "SELECT p FROM PorcentPorMes p WHERE p.anioInicio = :anioInicio")
    , @NamedQuery(name = "PorcentPorMes.findByAnioFin", query = "SELECT p FROM PorcentPorMes p WHERE p.anioFin = :anioFin")
    , @NamedQuery(name = "PorcentPorMes.findByPeriodo", query = "SELECT p FROM PorcentPorMes p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PorcentPorMes.findByPorcentaje", query = "SELECT p FROM PorcentPorMes p WHERE p.porcentaje = :porcentaje")
    , @NamedQuery(name = "PorcentPorMes.findByMes", query = "SELECT p FROM PorcentPorMes p WHERE p.mes = :mes")})
public class PorcentPorMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;
    @Basic(optional = false)
    @Column(name = "anio_fin")
    private int anioFin;
    @Basic(optional = false)
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @Column(name = "porcentaje")
    private int porcentaje;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;
    @JoinColumn(name = "nivel", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Nivel nivel;

    public PorcentPorMes() {
    }

    public PorcentPorMes(Integer codigo) {
        this.codigo = codigo;
    }

    public PorcentPorMes(Integer codigo, int anioInicio, int anioFin, int periodo, int porcentaje, int mes) {
        this.codigo = codigo;
        this.anioInicio = anioInicio;
        this.anioFin = anioFin;
        this.periodo = periodo;
        this.porcentaje = porcentaje;
        this.mes = mes;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
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

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
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
        if (!(object instanceof PorcentPorMes)) {
            return false;
        }
        PorcentPorMes other = (PorcentPorMes) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.PorcentPorMes[ codigo=" + codigo + " ]";
    }
    
}
