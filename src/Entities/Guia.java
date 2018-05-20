/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "guia", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Guia.findAll", query = "SELECT g FROM Guia g")
    , @NamedQuery(name = "Guia.findByCodigo", query = "SELECT g FROM Guia g WHERE g.codigo = :codigo")
    , @NamedQuery(name = "Guia.findByGrado", query = "SELECT g FROM Guia g WHERE g.grado = :grado")
    , @NamedQuery(name = "Guia.findByAnioInicio", query = "SELECT g FROM Guia g WHERE g.anioInicio = :anioInicio")
    , @NamedQuery(name = "Guia.findByAnioFin", query = "SELECT g FROM Guia g WHERE g.anioFin = :anioFin")})
public class Guia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @Column(name = "anio_inicio")
    private int anioInicio;
    @Basic(optional = false)
    @Column(name = "anio_fin")
    private int anioFin;
    @JoinColumn(name = "codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Grado grado1;
    @JoinColumn(name = "dui_profesor", referencedColumnName = "dui")
    @ManyToOne(optional = false)
    private Profesor duiProfesor;

    public Guia() {
    }

    public Guia(Integer codigo) {
        this.codigo = codigo;
    }

    public Guia(Integer codigo, int grado, int anioInicio, int anioFin) {
        this.codigo = codigo;
        this.grado = grado;
        this.anioInicio = anioInicio;
        this.anioFin = anioFin;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public int getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(int anioFin) {
        this.anioFin = anioFin;
    }

    public Grado getGrado1() {
        return grado1;
    }

    public void setGrado1(Grado grado1) {
        this.grado1 = grado1;
    }

    public Profesor getDuiProfesor() {
        return duiProfesor;
    }

    public void setDuiProfesor(Profesor duiProfesor) {
        this.duiProfesor = duiProfesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Guia)) {
            return false;
        }
        Guia other = (Guia) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Guia[ codigo=" + codigo + " ]";
    }
    
}
