/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos
 */
@Entity
@Table(name = "alumno", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a")
    , @NamedQuery(name = "Alumno.findByApellido1", query = "SELECT a FROM Alumno a WHERE a.apellido1 = :apellido1")
    , @NamedQuery(name = "Alumno.findByApellido2", query = "SELECT a FROM Alumno a WHERE a.apellido2 = :apellido2")
    , @NamedQuery(name = "Alumno.findByCodigo", query = "SELECT a FROM Alumno a WHERE a.alumnoPK.codigo = :codigo")
    , @NamedQuery(name = "Alumno.findByNie", query = "SELECT a FROM Alumno a WHERE a.alumnoPK.nie = :nie")
    , @NamedQuery(name = "Alumno.findByNombres", query = "SELECT a FROM Alumno a WHERE a.nombres = :nombres")
    , @NamedQuery(name = "Alumno.findByGenero", query = "SELECT a FROM Alumno a WHERE a.genero = :genero")
    , @NamedQuery(name = "Alumno.findByActivo", query = "SELECT a FROM Alumno a WHERE a.activo = :activo")})
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlumnoPK alumnoPK;
    @Basic(optional = false)
    @Column(name = "apellido1")
    private String apellido1;
    @Basic(optional = false)
    @Column(name = "apellido2")
    private String apellido2;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "genero")
    private Character genero;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumno")
    private List<Matricula> matriculaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumno")
    private List<Nota> notaList;

    public Alumno() {
    }

    public Alumno(AlumnoPK alumnoPK) {
        this.alumnoPK = alumnoPK;
    }

    public Alumno(AlumnoPK alumnoPK, String apellido1, String apellido2, String nombres, Character genero, boolean activo) {
        this.alumnoPK = alumnoPK;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombres = nombres;
        this.genero = genero;
        this.activo = activo;
    }

    public Alumno(String codigo, String nie) {
        this.alumnoPK = new AlumnoPK(codigo, nie);
    }

    public AlumnoPK getAlumnoPK() {
        return alumnoPK;
    }

    public void setAlumnoPK(AlumnoPK alumnoPK) {
        this.alumnoPK = alumnoPK;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alumnoPK != null ? alumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.alumnoPK == null && other.alumnoPK != null) || (this.alumnoPK != null && !this.alumnoPK.equals(other.alumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Alumno[ alumnoPK=" + alumnoPK + " ]";
    }
    
}
