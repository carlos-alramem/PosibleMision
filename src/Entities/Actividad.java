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
 * @author carlos
 */
@Entity
@Table(name = "actividad", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a")
    , @NamedQuery(name = "Actividad.findByCodigo", query = "SELECT a FROM Actividad a WHERE a.actividadPK.codigo = :codigo")
    , @NamedQuery(name = "Actividad.findByCodPromedio", query = "SELECT a FROM Actividad a WHERE a.actividadPK.codPromedio = :codPromedio")
    , @NamedQuery(name = "Actividad.findByCodNomActividad", query = "SELECT a FROM Actividad a WHERE a.codNomActividad = :codNomActividad")
    , @NamedQuery(name = "Actividad.findByActiva", query = "SELECT a FROM Actividad a WHERE a.activa = :activa")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActividadPK actividadPK;
    @Basic(optional = false)
    @Column(name = "cod_nom_actividad")
    private int codNomActividad;
    @Basic(optional = false)
    @Column(name = "activa")
    private boolean activa;
    @JoinColumn(name = "cod_mat_curso", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private MatPorCurso codMatCurso;
    @JoinColumn(name = "codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private NomActividad nomActividad;
    @JoinColumn(name = "cod_promedio", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Promedio promedio;

    public Actividad() {
    }

    public Actividad(ActividadPK actividadPK) {
        this.actividadPK = actividadPK;
    }

    public Actividad(ActividadPK actividadPK, int codNomActividad, boolean activa) {
        this.actividadPK = actividadPK;
        this.codNomActividad = codNomActividad;
        this.activa = activa;
    }

    public Actividad(int codigo, int codPromedio) {
        this.actividadPK = new ActividadPK(codigo, codPromedio);
    }

    public ActividadPK getActividadPK() {
        return actividadPK;
    }

    public void setActividadPK(ActividadPK actividadPK) {
        this.actividadPK = actividadPK;
    }

    public int getCodNomActividad() {
        return codNomActividad;
    }

    public void setCodNomActividad(int codNomActividad) {
        this.codNomActividad = codNomActividad;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public MatPorCurso getCodMatCurso() {
        return codMatCurso;
    }

    public void setCodMatCurso(MatPorCurso codMatCurso) {
        this.codMatCurso = codMatCurso;
    }

    public NomActividad getNomActividad() {
        return nomActividad;
    }

    public void setNomActividad(NomActividad nomActividad) {
        this.nomActividad = nomActividad;
    }

    public Promedio getPromedio() {
        return promedio;
    }

    public void setPromedio(Promedio promedio) {
        this.promedio = promedio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actividadPK != null ? actividadPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.actividadPK == null && other.actividadPK != null) || (this.actividadPK != null && !this.actividadPK.equals(other.actividadPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Actividad[ actividadPK=" + actividadPK + " ]";
    }
    
}
