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
public class GradoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_seccion")
    private int codSeccion;
    @Basic(optional = false)
    @Column(name = "cod_curso")
    private int codCurso;

    public GradoPK() {
    }

    public GradoPK(int codSeccion, int codCurso) {
        this.codSeccion = codSeccion;
        this.codCurso = codCurso;
    }

    public int getCodSeccion() {
        return codSeccion;
    }

    public void setCodSeccion(int codSeccion) {
        this.codSeccion = codSeccion;
    }

    public int getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codSeccion;
        hash += (int) codCurso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GradoPK)) {
            return false;
        }
        GradoPK other = (GradoPK) object;
        if (this.codSeccion != other.codSeccion) {
            return false;
        }
        if (this.codCurso != other.codCurso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.GradoPK[ codSeccion=" + codSeccion + ", codCurso=" + codCurso + " ]";
    }
    
}
