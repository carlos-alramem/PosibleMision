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
 * @author carlo
 */
@Embeddable
public class ObsPorAlumnoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_alumno")
    private String codAlumno;
    @Basic(optional = false)
    @Column(name = "cod_observacion")
    private int codObservacion;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;

    public ObsPorAlumnoPK() {
    }

    public ObsPorAlumnoPK(String codAlumno, int codObservacion, int mes) {
        this.codAlumno = codAlumno;
        this.codObservacion = codObservacion;
        this.mes = mes;
    }

    public String getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(String codAlumno) {
        this.codAlumno = codAlumno;
    }

    public int getCodObservacion() {
        return codObservacion;
    }

    public void setCodObservacion(int codObservacion) {
        this.codObservacion = codObservacion;
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
        hash += (codAlumno != null ? codAlumno.hashCode() : 0);
        hash += (int) codObservacion;
        hash += (int) mes;
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
        if (this.codObservacion != other.codObservacion) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ObsPorAlumnoPK[ codAlumno=" + codAlumno + ", codObservacion=" + codObservacion + ", mes=" + mes + " ]";
    }
    
}
