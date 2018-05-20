/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
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
@Table(name = "obs_por_alumno", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObsPorAlumno.findAll", query = "SELECT o FROM ObsPorAlumno o")
    , @NamedQuery(name = "ObsPorAlumno.findByCodAlumno", query = "SELECT o FROM ObsPorAlumno o WHERE o.obsPorAlumnoPK.codAlumno = :codAlumno")
    , @NamedQuery(name = "ObsPorAlumno.findByCodObservacion", query = "SELECT o FROM ObsPorAlumno o WHERE o.obsPorAlumnoPK.codObservacion = :codObservacion")
    , @NamedQuery(name = "ObsPorAlumno.findByMes", query = "SELECT o FROM ObsPorAlumno o WHERE o.obsPorAlumnoPK.mes = :mes")
    , @NamedQuery(name = "ObsPorAlumno.findByAnio", query = "SELECT o FROM ObsPorAlumno o WHERE o.anio = :anio")})
public class ObsPorAlumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObsPorAlumnoPK obsPorAlumnoPK;
    @Column(name = "anio")
    private Integer anio;
    @JoinColumn(name = "cod_alumno", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;
    @JoinColumn(name = "cod_observacion", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Observacion observacion;

    public ObsPorAlumno() {
    }

    public ObsPorAlumno(ObsPorAlumnoPK obsPorAlumnoPK) {
        this.obsPorAlumnoPK = obsPorAlumnoPK;
    }

    public ObsPorAlumno(String codAlumno, int codObservacion, int mes) {
        this.obsPorAlumnoPK = new ObsPorAlumnoPK(codAlumno, codObservacion, mes);
    }

    public ObsPorAlumnoPK getObsPorAlumnoPK() {
        return obsPorAlumnoPK;
    }

    public void setObsPorAlumnoPK(ObsPorAlumnoPK obsPorAlumnoPK) {
        this.obsPorAlumnoPK = obsPorAlumnoPK;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Observacion getObservacion() {
        return observacion;
    }

    public void setObservacion(Observacion observacion) {
        this.observacion = observacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (obsPorAlumnoPK != null ? obsPorAlumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObsPorAlumno)) {
            return false;
        }
        ObsPorAlumno other = (ObsPorAlumno) object;
        if ((this.obsPorAlumnoPK == null && other.obsPorAlumnoPK != null) || (this.obsPorAlumnoPK != null && !this.obsPorAlumnoPK.equals(other.obsPorAlumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ObsPorAlumno[ obsPorAlumnoPK=" + obsPorAlumnoPK + " ]";
    }
    
}
