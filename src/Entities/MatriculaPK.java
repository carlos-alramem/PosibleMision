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
public class MatriculaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_alumno")
    private String codAlumno;
    @Basic(optional = false)
    @Column(name = "cod_grado")
    private int codGrado;
    @Basic(optional = false)
    @Column(name = "anio")
    private int anio;

    public MatriculaPK() {
    }

    public MatriculaPK(String codAlumno, int codGrado, int anio) {
        this.codAlumno = codAlumno;
        this.codGrado = codGrado;
        this.anio = anio;
    }

    public String getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(String codAlumno) {
        this.codAlumno = codAlumno;
    }

    public int getCodGrado() {
        return codGrado;
    }

    public void setCodGrado(int codGrado) {
        this.codGrado = codGrado;
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
        hash += (int) codGrado;
        hash += (int) anio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatriculaPK)) {
            return false;
        }
        MatriculaPK other = (MatriculaPK) object;
        if ((this.codAlumno == null && other.codAlumno != null) || (this.codAlumno != null && !this.codAlumno.equals(other.codAlumno))) {
            return false;
        }
        if (this.codGrado != other.codGrado) {
            return false;
        }
        if (this.anio != other.anio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MatriculaPK[ codAlumno=" + codAlumno + ", codGrado=" + codGrado + ", anio=" + anio + " ]";
    }
    
}
