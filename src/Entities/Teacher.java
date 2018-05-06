/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author carlo
 */
public class Teacher {
    
    
    private SimpleStringProperty nombre;
    private SimpleStringProperty apellido;
    private SimpleStringProperty telefono;
    private SimpleStringProperty dui;
    private BooleanProperty activo;
    private Genero genero;
    
    public enum Genero{
        Masculino, Femenino
    }

    
    public Teacher(String identificacion, String nombres, String apellidos, String numero, Boolean activo, Genero genero){
        this.nombre = new SimpleStringProperty(nombres);
        this.apellido = new SimpleStringProperty(apellidos);
        this.dui = new SimpleStringProperty(identificacion);
        this.telefono = new SimpleStringProperty(numero);
        setActivo(activo);
        setGenero(genero);
    }
    
    /**
     * @return the nombres
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * @param nombres the nombres to set
     */
    public void setNombre(String nombres) {
        this.nombre = new SimpleStringProperty(nombres);
    }

    /**
     * @return the apellidos
     */
    public String getApellido() {
        return apellido.get();
    }

    /**
     * @param apellidos the apellidos to set
     */
    public void setApellido(String apellidos) {
        this.apellido = new SimpleStringProperty(apellidos);
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono.get();
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = new SimpleStringProperty(telefono);
    }

    /**
     * @return the dui
     */
    public String getDui() {
        return dui.get();
    }

    /**
     * @param dui the dui to set
     */
    public void setDui(String dui) {
        this.dui = new SimpleStringProperty(dui);
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
    
    public BooleanProperty getActivoProperty(){
        return this.activo;
    }

    /**
     * @return the genero
     */
    public Genero getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    
    
}

