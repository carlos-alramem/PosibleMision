/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "nota", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nota.findAll", query = "SELECT n FROM Nota n")
    , @NamedQuery(name = "Nota.findByCodAlumno", query = "SELECT n FROM Nota n WHERE n.notaPK.codAlumno = :codAlumno")
    , @NamedQuery(name = "Nota.findByCodActividad", query = "SELECT n FROM Nota n WHERE n.notaPK.codActividad = :codActividad")
    , @NamedQuery(name = "Nota.findByMes", query = "SELECT n FROM Nota n WHERE n.notaPK.mes = :mes")
    , @NamedQuery(name = "Nota.findByAnio", query = "SELECT n FROM Nota n WHERE n.notaPK.anio = :anio")
    , @NamedQuery(name = "Nota.findByNota", query = "SELECT n FROM Nota n WHERE n.nota = :nota")})
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotaPK notaPK;
    @Basic(optional = false)
    @Column(name = "nota")
    private double nota;
    @JoinColumn(name = "cod_actividad", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Actividad actividad;
    @JoinColumn(name = "cod_alumno", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;

    public Nota() {
    }

    public Nota(NotaPK notaPK) {
        this.notaPK = notaPK;
    }

    public Nota(NotaPK notaPK, double nota) {
        this.notaPK = notaPK;
        this.nota = nota;
    }

    public Nota(String codAlumno, int codActividad, int mes, int anio) {
        this.notaPK = new NotaPK(codAlumno, codActividad, mes, anio);
    }

    public NotaPK getNotaPK() {
        return notaPK;
    }

    public void setNotaPK(NotaPK notaPK) {
        this.notaPK = notaPK;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notaPK != null ? notaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nota)) {
            return false;
        }
        Nota other = (Nota) object;
        if ((this.notaPK == null && other.notaPK != null) || (this.notaPK != null && !this.notaPK.equals(other.notaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Nota[ notaPK=" + notaPK + " ]";
    }
    
}
