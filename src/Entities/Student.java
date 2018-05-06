/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author carlos
 */
public class Student {
    
    private SimpleStringProperty codigo;
    private SimpleStringProperty nie;
    private SimpleStringProperty nombre;
    private SimpleStringProperty apellido1;
    private SimpleStringProperty apellido2;
    private Genero genero;
    private SimpleBooleanProperty activo;
    
    public enum Genero{
        Masculino, Femenino
    }
    
    public Student(){}
    
    public Student(String codigo, String nie, String nombre, String apellido1, String apellido2, Genero genero, Boolean activo){
        setCodigo(codigo);
        setNie(nie);
        setNombre(nombre);
        setApellido1(apellido1);
        setApellido2(apellido2);
        setGenero(genero);
        setActivo(activo);
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo.get();
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = new SimpleStringProperty(codigo);
    }

    /**
     * @return the nie
     */
    public String getNie() {
        return nie.get();
    }

    /**
     * @param nie the nie to set
     */
    public void setNie(String nie) {
        this.nie = new SimpleStringProperty(nie);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }

    /**
     * @return the apellido1
     */
    public String getApellido1() {
        return apellido1.get();
    }

    /**
     * @param apellido1 the apellido1 to set
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = new SimpleStringProperty(apellido1);
    }

    /**
     * @return the apellido2
     */
    public String getApellido2() {
        return apellido2.get();
    }

    /**
     * @param apellido2 the apellido2 to set
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = new SimpleStringProperty(apellido2);
    }

    /**
     * @return the genero
     */
    public Genero getGenero() {
        //return ((char) genero.toString().toCharArray()[0]);
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(Genero genero) {
        //this.genero = new SimpleStringProperty(String.valueOf(genero));
        this.genero = genero;
    }

    /**
     * @return the activo
     */
    public Boolean getActivo() {
        return activo.get();
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(Boolean activo) {
        this.activo = new SimpleBooleanProperty(activo);
    }
    
}
