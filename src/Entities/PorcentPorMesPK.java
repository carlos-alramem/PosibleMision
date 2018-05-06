/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author carlos
 */
@Embeddable
public class PorcentPorMesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "nivel")
    private int nivel;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;

    public PorcentPorMesPK() {
    }

    public PorcentPorMesPK(int codigo, int nivel, int anioInicio, int mes) {
        this.codigo = codigo;
        this.nivel = nivel;
        this.anioInicio = anioInicio;
        this.mes = mes;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigo;
        hash += (int) nivel;
        hash += (int) anioInicio;
        hash += (int) mes;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PorcentPorMesPK)) {
            return false;
        }
        PorcentPorMesPK other = (PorcentPorMesPK) object;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (this.nivel != other.nivel) {
            return false;
        }
        if (this.anioInicio != other.anioInicio) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.PorcentPorMesPK[ codigo=" + codigo + ", nivel=" + nivel + ", anioInicio=" + anioInicio + ", mes=" + mes + " ]";
    }
    
}
