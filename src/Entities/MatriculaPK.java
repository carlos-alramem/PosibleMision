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
public class MatriculaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_alumno")
    private String codAlumno;
    @Basic(optional = false)
    @Column(name = "cod_prof_por_curso")
    private int codProfPorCurso;
    @Basic(optional = false)
    @Column(name = "anio")
    private int anio;

    public MatriculaPK() {
    }

    public MatriculaPK(String codAlumno, int codProfPorCurso, int anio) {
        this.codAlumno = codAlumno;
        this.codProfPorCurso = codProfPorCurso;
        this.anio = anio;
    }

    public String getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(String codAlumno) {
        this.codAlumno = codAlumno;
    }

    public int getCodProfPorCurso() {
        return codProfPorCurso;
    }

    public void setCodProfPorCurso(int codProfPorCurso) {
        this.codProfPorCurso = codProfPorCurso;
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
        hash += (int) codProfPorCurso;
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
        if (this.codProfPorCurso != other.codProfPorCurso) {
            return false;
        }
        if (this.anio != other.anio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MatriculaPK[ codAlumno=" + codAlumno + ", codProfPorCurso=" + codProfPorCurso + ", anio=" + anio + " ]";
    }
    
}
