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
public class MatPorCursoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "cod_curso")
    private int codCurso;
    @Basic(optional = false)
    @Column(name = "cod_materia")
    private int codMateria;

    public MatPorCursoPK() {
    }

    public MatPorCursoPK(int codigo, int codCurso, int codMateria) {
        this.codigo = codigo;
        this.codCurso = codCurso;
        this.codMateria = codMateria;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }

    public int getCodMateria() {
        return codMateria;
    }

    public void setCodMateria(int codMateria) {
        this.codMateria = codMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigo;
        hash += (int) codCurso;
        hash += (int) codMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatPorCursoPK)) {
            return false;
        }
        MatPorCursoPK other = (MatPorCursoPK) object;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (this.codCurso != other.codCurso) {
            return false;
        }
        if (this.codMateria != other.codMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.MatPorCursoPK[ codigo=" + codigo + ", codCurso=" + codCurso + ", codMateria=" + codMateria + " ]";
    }
    
}
