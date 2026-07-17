package com.prosperapp.model;

public class Seccion {

    private int idSeccion;
    private int idProyecto;
    private String nombre;
    private int orden;

    public Seccion() {
    }

    public Seccion(int idSeccion, int idProyecto, String nombre, int orden) {
        this.idSeccion = idSeccion;
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.orden = orden;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return nombre;
    }
}