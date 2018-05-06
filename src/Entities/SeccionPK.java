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
public class SeccionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "turno")
    private Character turno;

    public SeccionPK() {
    }

    public SeccionPK(int codigo, Character turno) {
        this.codigo = codigo;
        this.turno = turno;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Character getTurno() {
        return turno;
    }

    public void setTurno(Character turno) {
        this.turno = turno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigo;
        hash += (turno != null ? turno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeccionPK)) {
            return false;
        }
        SeccionPK other = (SeccionPK) object;
        if (this.codigo != other.codigo) {
            return false;
        }
        if ((this.turno == null && other.turno != null) || (this.turno != null && !this.turno.equals(other.turno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.SeccionPK[ codigo=" + codigo + ", turno=" + turno + " ]";
    }
    
}
