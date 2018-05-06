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
public class ProfPorCursoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dui_profesor")
    private String duiProfesor;
    @Basic(optional = false)
    @Column(name = "cod_sec_por_curso")
    private int codSecPorCurso;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;

    public ProfPorCursoPK() {
    }

    public ProfPorCursoPK(String duiProfesor, int codSecPorCurso, int anioInicio) {
        this.duiProfesor = duiProfesor;
        this.codSecPorCurso = codSecPorCurso;
        this.anioInicio = anioInicio;
    }

    public String getDuiProfesor() {
        return duiProfesor;
    }

    public void setDuiProfesor(String duiProfesor) {
        this.duiProfesor = duiProfesor;
    }

    public int getCodSecPorCurso() {
        return codSecPorCurso;
    }

    public void setCodSecPorCurso(int codSecPorCurso) {
        this.codSecPorCurso = codSecPorCurso;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (duiProfesor != null ? duiProfesor.hashCode() : 0);
        hash += (int) codSecPorCurso;
        hash += (int) anioInicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfPorCursoPK)) {
            return false;
        }
        ProfPorCursoPK other = (ProfPorCursoPK) object;
        if ((this.duiProfesor == null && other.duiProfesor != null) || (this.duiProfesor != null && !this.duiProfesor.equals(other.duiProfesor))) {
            return false;
        }
        if (this.codSecPorCurso != other.codSecPorCurso) {
            return false;
        }
        if (this.anioInicio != other.anioInicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ProfPorCursoPK[ duiProfesor=" + duiProfesor + ", codSecPorCurso=" + codSecPorCurso + ", anioInicio=" + anioInicio + " ]";
    }
    
}
