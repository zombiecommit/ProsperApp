package com.prosperapp.dao;

import com.prosperapp.model.Proyecto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TestProyectoDAO {

    public static void main(String[] args) {

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            System.out.println("=== Proyectos existentes ===");

            List<Proyecto> proyectos = proyectoDAO.listarTodos();

            for (Proyecto p : proyectos) {
                System.out.println(p);
            }

            System.out.println("\n=== Creando proyecto ===");

            Proyecto proyecto = new Proyecto();

            proyecto.setIdUsuario(1);
            proyecto.setNombre("Proyecto de Prueba");
            proyecto.setDescripcion("Proyecto creado desde Java");
            proyecto.setFechaLimite(LocalDate.of(2026, 12, 31));
            proyecto.setEstado("activo");

            Proyecto nuevo = proyectoDAO.crear(proyecto);

            System.out.println(nuevo);

            System.out.println("\n=== Buscando por ID ===");

            Optional<Proyecto> encontrado =
                    proyectoDAO.obtenerPorId(nuevo.getIdProyecto());

            System.out.println(encontrado.orElse(null));

            System.out.println("\n=== Actualizando proyecto ===");

            nuevo.setNombre("Proyecto Editado");

            boolean actualizado =
                    proyectoDAO.actualizar(nuevo);

            System.out.println(actualizado);

            System.out.println("\n=== Eliminando proyecto ===");

            boolean eliminado =
                    proyectoDAO.eliminar(nuevo.getIdProyecto());

            System.out.println(eliminado);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}