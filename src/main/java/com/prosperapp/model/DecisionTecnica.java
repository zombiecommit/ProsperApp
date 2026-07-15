package com.prosperapp.model;

import java.time.LocalDateTime;

public class DecisionTecnica {

    private int idDecision;
    private int idFuncionalidad;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;

    public DecisionTecnica(int idDecision, int idFuncionalidad, String titulo, String descripcion, LocalDateTime fechaCreacion) {
        this.idDecision = idDecision;
        this.idFuncionalidad = idFuncionalidad;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public DecisionTecnica() {
    }

    public int getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(int idDecision) {
        this.idDecision = idDecision;
    }

    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "DecisionTecnica{" +
                "idDecision=" + idDecision +
                ", idFuncionalidad=" + idFuncionalidad +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}