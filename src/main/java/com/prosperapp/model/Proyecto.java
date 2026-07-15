package com.prosperapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Proyecto {

    private int idProyecto;
    private int idUsuario;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaLimite;
    private String estado;

    public Proyecto() {
    }

    public Proyecto(int idProyecto, int idUsuario, String nombre, String descripcion,
                    LocalDateTime fechaCreacion, LocalDate fechaLimite, String estado) {
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "idProyecto=" + idProyecto +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaLimite=" + fechaLimite +
                ", estado='" + estado + '\'' +
                '}';
    }
}