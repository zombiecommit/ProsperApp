package com.prosperapp.dao;

import com.prosperapp.model.Subtarea;

import java.util.List;
import java.util.Optional;

public class TestSubtareaDAO {

    public static void main(String[] args) {

        try {

            SubtareaDAO dao = new SubtareaDAO();

            System.out.println("=== Subtareas ===");

            List<Subtarea> lista = dao.listarTodos();

            lista.forEach(System.out::println);

            System.out.println();

            System.out.println("=== Buscar subtarea 1 ===");

            Optional<Subtarea> subtarea =
                    dao.obtenerPorId(1);

            subtarea.ifPresent(System.out::println);

        } catch (Exception e) {

            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();

        }

    }

}