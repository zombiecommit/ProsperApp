package com.prosperapp.model;

import java.time.LocalDateTime;

public class FragmentoCodigo {

    private int idFragmento;
    private int idFuncionalidad;
    private String lenguaje;
    private String codigo;
    private LocalDateTime fechaCreacion;

    public FragmentoCodigo() {
    }

    public FragmentoCodigo(int idFragmento, int idFuncionalidad,
                           String lenguaje, String codigo,
                           LocalDateTime fechaCreacion) {

        this.idFragmento = idFragmento;
        this.idFuncionalidad = idFuncionalidad;
        this.lenguaje = lenguaje;
        this.codigo = codigo;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdFragmento() {
        return idFragmento;
    }

    public void setIdFragmento(int idFragmento) {
        this.idFragmento = idFragmento;
    }

    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "FragmentoCodigo{" +
                "idFragmento=" + idFragmento +
                ", idFuncionalidad=" + idFuncionalidad +
                ", lenguaje='" + lenguaje + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}