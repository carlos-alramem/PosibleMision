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
public class ProfPorMateriaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dui_profesor")
    private String duiProfesor;
    @Basic(optional = false)
    @Column(name = "cod_mat_por_curso")
    private int codMatPorCurso;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;

    public ProfPorMateriaPK() {
    }

    public ProfPorMateriaPK(String duiProfesor, int codMatPorCurso, int anioInicio) {
        this.duiProfesor = duiProfesor;
        this.codMatPorCurso = codMatPorCurso;
        this.anioInicio = anioInicio;
    }

    public String getDuiProfesor() {
        return duiProfesor;
    }

    public void setDuiProfesor(String duiProfesor) {
        this.duiProfesor = duiProfesor;
    }

    public int getCodMatPorCurso() {
        return codMatPorCurso;
    }

    public void setCodMatPorCurso(int codMatPorCurso) {
        this.codMatPorCurso = codMatPorCurso;
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
        hash += (int) codMatPorCurso;
        hash += (int) anioInicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfPorMateriaPK)) {
            return false;
        }
        ProfPorMateriaPK other = (ProfPorMateriaPK) object;
        if ((this.duiProfesor == null && other.duiProfesor != null) || (this.duiProfesor != null && !this.duiProfesor.equals(other.duiProfesor))) {
            return false;
        }
        if (this.codMatPorCurso != other.codMatPorCurso) {
            return false;
        }
        if (this.anioInicio != other.anioInicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ProfPorMateriaPK[ duiProfesor=" + duiProfesor + ", codMatPorCurso=" + codMatPorCurso + ", anioInicio=" + anioInicio + " ]";
    }
    
}
