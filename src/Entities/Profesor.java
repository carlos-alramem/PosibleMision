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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "profesor", catalog = "notas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profesor.findAll", query = "SELECT p FROM Profesor p")
    , @NamedQuery(name = "Profesor.findByDui", query = "SELECT p FROM Profesor p WHERE p.dui = :dui")
    , @NamedQuery(name = "Profesor.findByApellidos", query = "SELECT p FROM Profesor p WHERE p.apellidos = :apellidos")
    , @NamedQuery(name = "Profesor.findByNombres", query = "SELECT p FROM Profesor p WHERE p.nombres = :nombres")
    , @NamedQuery(name = "Profesor.findByActivo", query = "SELECT p FROM Profesor p WHERE p.activo = :activo")})
public class Profesor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "dui")
    private String dui;
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "activo")
    private Boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profesor")
    private List<ProfPorCurso> profPorCursoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profesor")
    private List<ProfPorMateria> profPorMateriaList;

    public Profesor() {
    }

    public Profesor(String dui) {
        this.dui = dui;
    }

    public Profesor(String dui, String nombres) {
        this.dui = dui;
        this.nombres = nombres;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<ProfPorCurso> getProfPorCursoList() {
        return profPorCursoList;
    }

    public void setProfPorCursoList(List<ProfPorCurso> profPorCursoList) {
        this.profPorCursoList = profPorCursoList;
    }

    @XmlTransient
    public List<ProfPorMateria> getProfPorMateriaList() {
        return profPorMateriaList;
    }

    public void setProfPorMateriaList(List<ProfPorMateria> profPorMateriaList) {
        this.profPorMateriaList = profPorMateriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dui != null ? dui.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profesor)) {
            return false;
        }
        Profesor other = (Profesor) object;
        if ((this.dui == null && other.dui != null) || (this.dui != null && !this.dui.equals(other.dui))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Profesor[ dui=" + dui + " ]";
    }
    
}
