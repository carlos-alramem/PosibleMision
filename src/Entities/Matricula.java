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
@Table(name = "matricula", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m")
    , @NamedQuery(name = "Matricula.findByCodigo", query = "SELECT m FROM Matricula m WHERE m.codigo = :codigo")
    , @NamedQuery(name = "Matricula.findByCodAlumno", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.codAlumno = :codAlumno")
    , @NamedQuery(name = "Matricula.findByCodGrado", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.codGrado = :codGrado")
    , @NamedQuery(name = "Matricula.findByNumLista", query = "SELECT m FROM Matricula m WHERE m.numLista = :numLista")
    , @NamedQuery(name = "Matricula.findByAnio", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.anio = :anio")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatriculaPK matriculaPK;
    @Basic(optional = false)
    @Column(name = "codigo")
    private long codigo;
    @Basic(optional = false)
    @Column(name = "num_lista")
    private int numLista;
    @JoinColumn(name = "cod_alumno", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;
    @JoinColumn(name = "cod_grado", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grado grado;

    public Matricula() {
    }

    public Matricula(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public Matricula(MatriculaPK matriculaPK, long codigo, int numLista) {
        this.matriculaPK = matriculaPK;
        this.codigo = codigo;
        this.numLista = numLista;
    }

    public Matricula(String codAlumno, int codGrado, int anio) {
        this.matriculaPK = new MatriculaPK(codAlumno, codGrado, anio);
    }

    public MatriculaPK getMatriculaPK() {
        return matriculaPK;
    }

    public void setMatriculaPK(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public int getNumLista() {
        return numLista;
    }

    public void setNumLista(int numLista) {
        this.numLista = numLista;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaPK != null ? matriculaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.matriculaPK == null && other.matriculaPK != null) || (this.matriculaPK != null && !this.matriculaPK.equals(other.matriculaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Matricula[ matriculaPK=" + matriculaPK + " ]";
    }
    
}
