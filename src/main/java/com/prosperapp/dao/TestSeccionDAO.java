package com.prosperapp.dao;

import com.prosperapp.model.Seccion;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TestSeccionDAO {

    public static void main(String[] args) {

        SeccionDAO seccionDAO = new SeccionDAO();

        try {

            System.out.println("=== Secciones existentes ===");

            List<Seccion> secciones = seccionDAO.listarTodos();

            for (Seccion s : secciones) {
                System.out.println(s);
            }

            System.out.println("\n=== Creando sección ===");

            Seccion seccion = new Seccion();

            // IMPORTANTE:
            // Cambia este número por el id de un proyecto que exista.
            seccion.setIdProyecto(1);

            seccion.setNombre("Backlog");
            seccion.setOrden(1);

            Seccion nueva = seccionDAO.crear(seccion);

            System.out.println(nueva);

            System.out.println("\n=== Buscando por ID ===");

            Optional<Seccion> encontrada =
                    seccionDAO.obtenerPorId(nueva.getIdSeccion());

            System.out.println(encontrada.orElse(null));

            System.out.println("\n=== Actualizando sección ===");

            nueva.setNombre("Backlog Editado");

            boolean actualizado =
                    seccionDAO.actualizar(nueva);

            System.out.println(actualizado);

            System.out.println("\n=== Eliminando sección ===");

            boolean eliminado =
                    seccionDAO.eliminar(nueva.getIdSeccion());

            System.out.println(eliminado);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}