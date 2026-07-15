package com.prosperapp.model;

import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String correo, String contrasena, LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}