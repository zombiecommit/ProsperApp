package com.prosperapp.dao;

import com.prosperapp.model.Nota;

import java.util.List;
import java.util.Optional;

public class TestNotaDAO {

    public static void main(String[] args) {

        try {

            NotaDAO dao = new NotaDAO();

            System.out.println("=== Notas ===");

            List<Nota> lista = dao.listarTodos();

            lista.forEach(System.out::println);

            System.out.println();

            System.out.println("=== Buscar nota 1 ===");

            Optional<Nota> nota =
                    dao.obtenerPorId(1);

            nota.ifPresent(System.out::println);

        } catch (Exception e) {

            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();

        }

    }

}