package com.prosperapp.view;

import com.prosperapp.dao.ProyectoDAO;
import com.prosperapp.dao.SeccionDAO;
import com.prosperapp.model.Proyecto;
import com.prosperapp.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.prosperapp.dao.SeccionDAO;
import com.prosperapp.model.Seccion;
import com.prosperapp.model.Seccion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardView {

    private final Stage stage;
    private final Usuario usuario;
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final SeccionDAO seccionDAO = new SeccionDAO();
    private VBox listaProyectos;

    public DashboardView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public Scene construir() {

        Label titulo = new Label("Mis Proyectos");
        titulo.getStyleClass().add("titulo-app");

        Label bienvenida = new Label("Hola, " + usuario.getNombre() + " 🌿");
        bienvenida.getStyleClass().add("subtitulo");

        Button botonNuevoProyecto = new Button("+ Nuevo Proyecto");
        botonNuevoProyecto.getStyleClass().add("boton-primario");
        botonNuevoProyecto.setOnAction(e -> mostrarDialogoNuevoProyecto());

        HBox encabezado = new HBox(20, bienvenida, botonNuevoProyecto);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(bienvenida, javafx.scene.layout.Priority.ALWAYS);

        listaProyectos = new VBox(12);
        listaProyectos.setPadding(new Insets(10, 0, 0, 0));

        ScrollPane scroll = new ScrollPane(listaProyectos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        VBox contenedor = new VBox(15, titulo, encabezado, scroll);
        contenedor.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setCenter(contenedor);

        cargarProyectos();

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }

    private void cargarProyectos() {
        listaProyectos.getChildren().clear();

        try {
            List<Proyecto> proyectos = proyectoDAO.listarTodos().stream()
                    .filter(p -> p.getIdUsuario() == usuario.getIdUsuario())
                    .collect(Collectors.toList());

            if (proyectos.isEmpty()) {
                Label vacio = new Label("Todavía no tienes proyectos. ¡Crea el primero!");
                vacio.getStyleClass().add("subtitulo");
                listaProyectos.getChildren().add(vacio);
                return;
            }

            for (Proyecto proyecto : proyectos) {
                listaProyectos.getChildren().add(crearTarjetaProyecto(proyecto));
            }

        } catch (SQLException ex) {
            Label error = new Label("Error al cargar los proyectos.");
            error.getStyleClass().add("mensaje-error");
            listaProyectos.getChildren().add(error);
            ex.printStackTrace();
        }
    }

    private VBox crearTarjetaProyecto(Proyecto proyecto) {

        Label nombre = new Label(proyecto.getNombre());
        nombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label descripcion = new Label(proyecto.getDescripcion() == null ? "" : proyecto.getDescripcion());
        descripcion.setStyle("-fx-text-fill: #555;");
        descripcion.setWrapText(true);

        Label estado = new Label("Estado: " + proyecto.getEstado());
        estado.setStyle("-fx-font-size: 12px; -fx-text-fill: #4A4A4A;");

        Label fechaLimite = new Label(
                proyecto.getFechaLimite() != null
                        ? "Fecha límite: " + proyecto.getFechaLimite()
                        : "Sin fecha límite"
        );
        fechaLimite.setStyle("-fx-font-size: 12px; -fx-text-fill: #4A4A4A;");

        VBox tarjeta = new VBox(6, nombre, descripcion, estado, fechaLimite);
        tarjeta.getStyleClass().add("tarjeta-formulario");
        tarjeta.setStyle(tarjeta.getStyle() + "-fx-cursor: hand;");
        tarjeta.setMaxWidth(Double.MAX_VALUE);

        tarjeta.setOnMouseClicked(e -> {
            TableroView tableroView = new TableroView(stage, usuario, proyecto);
            stage.setScene(tableroView.construir());
        });

        return tarjeta;
    }

    private void mostrarDialogoNuevoProyecto() {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Proyecto");

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nombre del proyecto");
        campoNombre.getStyleClass().add("campo-texto");

        TextArea campoDescripcion = new TextArea();
        campoDescripcion.setPromptText("Descripción");
        campoDescripcion.setPrefRowCount(3);

        DatePicker campoFechaLimite = new DatePicker();
        campoFechaLimite.setPromptText("Fecha límite (opcional)");

        Label mensajeError = new Label();
        mensajeError.getStyleClass().add("mensaje-error");
        mensajeError.setVisible(false);

        // --- Configuración de secciones (mínimo 1, máximo 6) ---
        Label labelSecciones = new Label("Secciones del tablero (1 a 6):");
        labelSecciones.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        ObservableList<String> nombresSecciones = FXCollections.observableArrayList(
                "Backlog", "Doing", "Completed"
        );
        ListView<String> listaSecciones = new ListView<>(nombresSecciones);
        listaSecciones.setPrefHeight(100);

        TextField campoNuevaSeccion = new TextField();
        campoNuevaSeccion.setPromptText("Nombre de la sección");
        campoNuevaSeccion.getStyleClass().add("campo-texto");

        Button botonAgregarSeccion = new Button("+ Agregar");
        botonAgregarSeccion.setOnAction(e -> {
            String nombreSeccion = campoNuevaSeccion.getText().trim();
            if (nombreSeccion.isEmpty()) {
                return;
            }
            if (nombresSecciones.size() >= 6) {
                mensajeError.setText("Máximo 6 secciones permitidas.");
                mensajeError.setVisible(true);
                return;
            }
            nombresSecciones.add(nombreSeccion);
            campoNuevaSeccion.clear();
            mensajeError.setVisible(false);
        });

        Button botonQuitarSeccion = new Button("Quitar seleccionada");
        botonQuitarSeccion.setOnAction(e -> {
            String seleccionada = listaSecciones.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                nombresSecciones.remove(seleccionada);
            }
        });

        HBox filaNuevaSeccion = new HBox(8, campoNuevaSeccion, botonAgregarSeccion);
        HBox filaAccionesSeccion = new HBox(8, botonQuitarSeccion);

        VBox contenido = new VBox(10,
                campoNombre, campoDescripcion, campoFechaLimite,
                labelSecciones, listaSecciones, filaNuevaSeccion, filaAccionesSeccion,
                mensajeError
        );
        contenido.setPadding(new Insets(15));
        contenido.setPrefWidth(360);

        dialog.getDialogPane().setContent(contenido);
        ButtonType botonCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonCrear, ButtonType.CANCEL);

        dialog.setResultConverter(botonPresionado -> {
            if (botonPresionado == botonCrear) {

                String nombre = campoNombre.getText().trim();

                if (nombre.isEmpty()) {
                    mensajeError.setText("El nombre es obligatorio.");
                    mensajeError.setVisible(true);
                    return null;
                }

                if (nombresSecciones.isEmpty()) {
                    mensajeError.setText("Debes agregar al menos 1 sección.");
                    mensajeError.setVisible(true);
                    return null;
                }

                try {
                    Proyecto nuevoProyecto = new Proyecto();
                    nuevoProyecto.setIdUsuario(usuario.getIdUsuario());
                    nuevoProyecto.setNombre(nombre);
                    nuevoProyecto.setDescripcion(campoDescripcion.getText());
                    nuevoProyecto.setEstado("activo");
                    nuevoProyecto.setFechaLimite(campoFechaLimite.getValue());

                    Proyecto creado = proyectoDAO.crear(nuevoProyecto);

                    int orden = 1;
                    for (String nombreSeccion : nombresSecciones) {
                        Seccion seccion = new Seccion();
                        seccion.setIdProyecto(creado.getIdProyecto());
                        seccion.setNombre(nombreSeccion);
                        seccion.setOrden(orden);
                        seccionDAO.crear(seccion);
                        orden++;
                    }

                    cargarProyectos();

                } catch (SQLException ex) {
                    mensajeError.setText("Error al crear el proyecto.");
                    mensajeError.setVisible(true);
                    ex.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}