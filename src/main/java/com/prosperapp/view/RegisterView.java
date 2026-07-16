package com.prosperapp.view;

import com.prosperapp.dao.UsuarioDAO;
import com.prosperapp.model.Usuario;
import com.prosperapp.util.PasswordUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

public class RegisterView {

    private final Stage stage;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public RegisterView(Stage stage) {
        this.stage = stage;
    }

    public Scene construir() {

        Label titulo = new Label("Crear cuenta");
        titulo.getStyleClass().add("titulo-app");

        Label subtitulo = new Label("Únete a ProsperApp");
        subtitulo.getStyleClass().add("subtitulo");

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nombre completo");
        campoNombre.getStyleClass().add("campo-texto");

        TextField campoCorreo = new TextField();
        campoCorreo.setPromptText("Correo electrónico");
        campoCorreo.getStyleClass().add("campo-texto");

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Contraseña");
        campoContrasena.getStyleClass().add("campo-texto");

        PasswordField campoConfirmar = new PasswordField();
        campoConfirmar.setPromptText("Confirmar contraseña");
        campoConfirmar.getStyleClass().add("campo-texto");

        Label mensajeError = new Label();
        mensajeError.getStyleClass().add("mensaje-error");
        mensajeError.setVisible(false);

        Button botonRegistrar = new Button("Registrarme");
        botonRegistrar.getStyleClass().add("boton-primario");
        botonRegistrar.setMaxWidth(Double.MAX_VALUE);

        Label enlaceLogin = new Label("¿Ya tienes cuenta? Inicia sesión");
        enlaceLogin.getStyleClass().add("enlace-secundario");

        botonRegistrar.setOnAction(e -> {
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String contrasena = campoContrasena.getText();
            String confirmar = campoConfirmar.getText();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                mostrarError(mensajeError, "Por favor completa todos los campos.");
                return;
            }

            if (!contrasena.equals(confirmar)) {
                mostrarError(mensajeError, "Las contraseñas no coinciden.");
                return;
            }

            try {
                Optional<Usuario> existente = usuarioDAO.obtenerPorCorreo(correo);
                if (existente.isPresent()) {
                    mostrarError(mensajeError, "Ya existe una cuenta con ese correo.");
                    return;
                }

                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setCorreo(correo);
                nuevoUsuario.setContrasena(PasswordUtil.hashear(contrasena));

                usuarioDAO.crear(nuevoUsuario);

                LoginView loginView = new LoginView(stage);
                stage.setScene(loginView.construir());

            } catch (SQLException ex) {
                mostrarError(mensajeError, "Error de conexión con la base de datos.");
                ex.printStackTrace();
            }
        });

        enlaceLogin.setOnMouseClicked(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.construir());
        });

        VBox formulario = new VBox(12, titulo, subtitulo, campoNombre, campoCorreo, campoContrasena, campoConfirmar, mensajeError, botonRegistrar, enlaceLogin);
        formulario.setAlignment(Pos.CENTER);
        formulario.setMaxWidth(340);
        formulario.getStyleClass().add("tarjeta-formulario");

        StackPane root = new StackPane(formulario);
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }

    private void mostrarError(Label label, String texto) {
        label.setText(texto);
        label.setVisible(true);
    }
}