package com.prosperapp.dao;

import com.prosperapp.model.FragmentoCodigo;

import java.util.List;
import java.util.Optional;

public class TestFragmentoCodigoDAO {

    public static void main(String[] args) {

        try {

            FragmentoCodigoDAO dao = new FragmentoCodigoDAO();

            System.out.println("=== Fragmentos de código ===");

            List<FragmentoCodigo> lista = dao.listarTodos();

            lista.forEach(System.out::println);

            System.out.println();

            System.out.println("=== Buscar fragmento 1 ===");

            Optional<FragmentoCodigo> fragmento =
                    dao.obtenerPorId(1);

            fragmento.ifPresent(System.out::println);

        } catch (Exception e) {

            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();

        }

    }

}