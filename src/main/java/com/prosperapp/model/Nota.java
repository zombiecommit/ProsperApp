package com.prosperapp.model;

import java.time.LocalDateTime;

public class Nota {

    private int idNota;
    private int idFuncionalidad;
    private String contenido;
    private LocalDateTime fechaCreacion;

    public Nota(int idNota, int idFuncionalidad, String contenido, LocalDateTime fechaCreacion) {
        this.idNota = idNota;
        this.idFuncionalidad = idFuncionalidad;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Nota() {
    }

    @Override
    public String toString() {
        return "Nota{" +
                "idNota=" + idNota +
                ", idFuncionalidad=" + idFuncionalidad +
                ", contenido='" + contenido + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}