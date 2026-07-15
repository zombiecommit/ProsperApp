package com.prosperapp.model;

public class Subtarea {

    private int idSubtarea;
    private int idFuncionalidad;
    private String descripcion;
    private String estado;

    public Subtarea(int idSubtarea, int idFuncionalidad, String descripcion, String estado) {
        this.idSubtarea = idSubtarea;
        this.idFuncionalidad = idFuncionalidad;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Subtarea() {
    }

    public int getIdSubtarea() {
        return idSubtarea;
    }

    public void setIdSubtarea(int idSubtarea) {
        this.idSubtarea = idSubtarea;
    }

    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Subtarea{" +
                "idSubtarea=" + idSubtarea +
                ", idFuncionalidad=" + idFuncionalidad +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}