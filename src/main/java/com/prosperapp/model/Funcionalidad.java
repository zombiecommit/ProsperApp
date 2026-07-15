package com.prosperapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Funcionalidad {

    private int idFuncionalidad;
    private int idSeccion;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaLimite;

    public Funcionalidad() {
    }

    public Funcionalidad(int idFuncionalidad, int idSeccion, String titulo,
                         String descripcion, String prioridad,
                         LocalDateTime fechaCreacion, LocalDate fechaLimite) {

        this.idFuncionalidad = idFuncionalidad;
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimite = fechaLimite;
    }

    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
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

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Override
    public String toString() {
        return "Funcionalidad{" +
                "idFuncionalidad=" + idFuncionalidad +
                ", idSeccion=" + idSeccion +
                ", titulo='" + titulo + '\'' +
                ", prioridad='" + prioridad + '\'' +
                '}';
    }
}