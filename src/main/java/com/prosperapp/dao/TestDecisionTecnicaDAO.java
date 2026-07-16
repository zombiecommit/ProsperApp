package com.prosperapp.dao;

import com.prosperapp.model.DecisionTecnica;

import java.util.List;
import java.util.Optional;

public class TestDecisionTecnicaDAO {

    public static void main(String[] args) {

        try {

            DecisionTecnicaDAO dao = new DecisionTecnicaDAO();

            System.out.println("=== Decisiones técnicas ===");

            List<DecisionTecnica> lista = dao.listarTodos();

            lista.forEach(System.out::println);

            System.out.println();

            System.out.println("=== Buscar decisión técnica 1 ===");

            Optional<DecisionTecnica> decision =
                    dao.obtenerPorId(1);

            decision.ifPresent(System.out::println);

        } catch (Exception e) {

            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();

        }

    }

}