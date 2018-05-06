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
public class ActividadPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "cod_promedio")
    private int codPromedio;

    public ActividadPK() {
    }

    public ActividadPK(int codigo, int codPromedio) {
        this.codigo = codigo;
        this.codPromedio = codPromedio;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodPromedio() {
        return codPromedio;
    }

    public void setCodPromedio(int codPromedio) {
        this.codPromedio = codPromedio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigo;
        hash += (int) codPromedio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadPK)) {
            return false;
        }
        ActividadPK other = (ActividadPK) object;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (this.codPromedio != other.codPromedio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ActividadPK[ codigo=" + codigo + ", codPromedio=" + codPromedio + " ]";
    }
    
}
