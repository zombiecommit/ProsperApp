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
import com.prosperapp.view.DashboardView;
import java.sql.SQLException;
import java.util.Optional;

public class LoginView {

    private final Stage stage;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene construir() {

        Label titulo = new Label("ProsperApp");
        titulo.getStyleClass().add("titulo-app");

        Label subtitulo = new Label("Inicia sesión para continuar");
        subtitulo.getStyleClass().add("subtitulo");

        TextField campoCorreo = new TextField();
        campoCorreo.setPromptText("Correo electrónico");
        campoCorreo.getStyleClass().add("campo-texto");

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Contraseña");
        campoContrasena.getStyleClass().add("campo-texto");

        Label mensajeError = new Label();
        mensajeError.getStyleClass().add("mensaje-error");
        mensajeError.setVisible(false);

        Button botonLogin = new Button("Iniciar sesión");
        botonLogin.getStyleClass().add("boton-primario");
        botonLogin.setMaxWidth(Double.MAX_VALUE);

        Label enlaceRegistro = new Label("¿No tienes cuenta? Regístrate aquí");
        enlaceRegistro.getStyleClass().add("enlace-secundario");

        botonLogin.setOnAction(e -> {
            String correo = campoCorreo.getText().trim();
            String contrasena = campoContrasena.getText();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                mostrarError(mensajeError, "Por favor completa todos los campos.");
                return;
            }

            try {
                Optional<Usuario> usuario = usuarioDAO.obtenerPorCorreo(correo);

                if (usuario.isPresent() && PasswordUtil.verificar(contrasena, usuario.get().getContrasena())) {
                    mensajeError.setVisible(false);
                    irADashboard(usuario.get());
                } else {
                    mostrarError(mensajeError, "Correo o contraseña incorrectos.");
                }

            } catch (SQLException ex) {
                mostrarError(mensajeError, "Error de conexión con la base de datos.");
                ex.printStackTrace();
            }
        });

        enlaceRegistro.setOnMouseClicked(e -> {
            RegisterView registerView = new RegisterView(stage);
            stage.setScene(registerView.construir());
        });

        VBox formulario = new VBox(15, titulo, subtitulo, campoCorreo, campoContrasena, mensajeError, botonLogin, enlaceRegistro);
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

    private void irADashboard(Usuario usuario) {
        DashboardView dashboardView = new DashboardView(stage, usuario);
        stage.setScene(dashboardView.construir());
    }
}