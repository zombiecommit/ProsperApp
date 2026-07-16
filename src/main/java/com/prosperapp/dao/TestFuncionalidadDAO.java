package com.prosperapp.dao;

import com.prosperapp.model.Funcionalidad;

import java.util.List;
import java.util.Optional;

public class TestFuncionalidadDAO {

    public static void main(String[] args) {

        try {

            FuncionalidadDAO dao = new FuncionalidadDAO();

            System.out.println("=== Funcionalidades ===");

            List<Funcionalidad> lista = dao.listarTodos();

            lista.forEach(System.out::println);

            System.out.println();

            System.out.println("=== Buscar funcionalidad 1 ===");

            Optional<Funcionalidad> funcionalidad =
                    dao.obtenerPorId(1);

            funcionalidad.ifPresent(System.out::println);

        } catch (Exception e) {

            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();

        }

    }

}