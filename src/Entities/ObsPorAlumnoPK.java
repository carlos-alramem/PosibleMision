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
public class ObsPorAlumnoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_alumno")
    private String codAlumno;
    @Basic(optional = false)
    @Column(name = "cod_obs")
    private int codObs;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;
    @Basic(optional = false)
    @Column(name = "anio")
    private int anio;

    public ObsPorAlumnoPK() {
    }

    public ObsPorAlumnoPK(String codAlumno, int codObs, int mes, int anio) {
        this.codAlumno = codAlumno;
        this.codObs = codObs;
        this.mes = mes;
        this.anio = anio;
    }

    public String getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(String codAlumno) {
        this.codAlumno = codAlumno;
    }

    public int getCodObs() {
        return codObs;
    }

    public void setCodObs(int codObs) {
        this.codObs = codObs;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAlumno != null ? codAlumno.hashCode() : 0);
        hash += (int) codObs;
        hash += (int) mes;
        hash += (int) anio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObsPorAlumnoPK)) {
            return false;
        }
        ObsPorAlumnoPK other = (ObsPorAlumnoPK) object;
        if ((this.codAlumno == null && other.codAlumno != null) || (this.codAlumno != null && !this.codAlumno.equals(other.codAlumno))) {
            return false;
        }
        if (this.codObs != other.codObs) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.anio != other.anio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ObsPorAlumnoPK[ codAlumno=" + codAlumno + ", codObs=" + codObs + ", mes=" + mes + ", anio=" + anio + " ]";
    }
    
}
