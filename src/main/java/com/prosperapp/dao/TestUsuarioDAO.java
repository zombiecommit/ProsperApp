package com.prosperapp.dao;

import com.prosperapp.model.Usuario;
import com.prosperapp.util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TestUsuarioDAO {

    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            // 1. Listar todos los usuarios existentes
            System.out.println("=== Usuarios existentes ===");
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                System.out.println(u);
            }

            // 2. Crear un usuario nuevo de prueba, con la contraseña hasheada
            System.out.println("\n=== Creando usuario nuevo ===");
            String contrasenaPlana = "claveDePrueba123";
            String contrasenaHash = PasswordUtil.hashear(contrasenaPlana);

            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario Prueba");
            usuario.setCorreo("prueba@example.com");
            usuario.setContrasena(contrasenaHash);
            Usuario nuevo = usuarioDAO.crear(usuario);

            System.out.println("Usuario creado: " + nuevo);

            // 3. Buscarlo por correo
            System.out.println("\n=== Buscando por correo ===");
            Optional<Usuario> encontrado = usuarioDAO.obtenerPorCorreo("prueba@example.com");
            if (encontrado.isPresent()) {
                System.out.println("Encontrado: " + encontrado.get());
            } else {
                System.out.println("No se encontró el usuario.");
            }

            // 3.5 Simular un login: verificar que la contraseña ingresada coincide con el hash guardado
            System.out.println("\n=== Simulando login ===");
            if (encontrado.isPresent()) {
                boolean loginExitoso = PasswordUtil.verificar(contrasenaPlana, encontrado.get().getContrasena());
                System.out.println("¿Login exitoso con contraseña correcta?: " + loginExitoso);

                boolean loginFallido = PasswordUtil.verificar("contrasenaMala", encontrado.get().getContrasena());
                System.out.println("¿Login exitoso con contraseña incorrecta?: " + loginFallido);
            }

            // 4. Actualizar su nombre
            System.out.println("\n=== Actualizando usuario ===");

            nuevo.setNombre("Usuario Prueba Editado");

            boolean actualizado = usuarioDAO.actualizar(nuevo);

            System.out.println("¿Se actualizó?: " + actualizado);

            // 5. Confirmar el cambio
            Optional<Usuario> actualizadoConfirmado = usuarioDAO.obtenerPorId(nuevo.getIdUsuario());
            System.out.println("Después de actualizar: " + actualizadoConfirmado.orElse(null));

            // 6. Eliminarlo (para dejar la BD limpia después de la prueba)
            System.out.println("\n=== Eliminando usuario de prueba ===");
            boolean eliminado = usuarioDAO.eliminar(nuevo.getIdUsuario());
            System.out.println("¿Se eliminó?: " + eliminado);

        } catch (SQLException e) {
            System.out.println("Error al interactuar con la base de datos.");
            e.printStackTrace();
        }
    }
}