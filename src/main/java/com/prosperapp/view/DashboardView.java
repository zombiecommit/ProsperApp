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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.prosperapp.dao.SeccionDAO;
import com.prosperapp.model.Seccion;
import com.prosperapp.model.Seccion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.prosperapp.util.Toast;

public class DashboardView {

    private final Stage stage;
    private final Usuario usuario;
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final SeccionDAO seccionDAO = new SeccionDAO();
    private VBox listaProyectos;
    private StackPane raizActual;

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
        botonNuevoProyecto.setOnAction(e -> mostrarDialogoProyecto(null));

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

        BorderPane contenidoRoot = new BorderPane();
        contenidoRoot.setCenter(contenedor);

        StackPane root = new StackPane(contenidoRoot);
        this.raizActual = root;

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

        Button btnAbrir = new Button("Abrir");
        Button btnEditar = new Button("Editar");
        btnEditar.setOnAction(e -> mostrarDialogoProyecto(proyecto));
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarProyecto(proyecto));

        btnAbrir.getStyleClass().add("boton-primario");

        HBox botones = new HBox(10, btnAbrir, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox tarjeta = new VBox(6, nombre, descripcion, estado, fechaLimite, botones);
        tarjeta.getStyleClass().add("tarjeta-formulario");
        tarjeta.setStyle(tarjeta.getStyle() + "-fx-cursor: hand;");
        tarjeta.setMaxWidth(Double.MAX_VALUE);

        btnAbrir.setOnAction(e -> {
            TableroView tableroView = new TableroView(stage, usuario, proyecto);
            stage.setScene(tableroView.construir());
        });

        return tarjeta;
    }

    private void mostrarDialogoProyecto(Proyecto proyectoEditar) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(proyectoEditar == null ? "Nuevo Proyecto" : "Editar Proyecto");

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nombre del proyecto");
        campoNombre.getStyleClass().add("campo-texto");

        TextArea campoDescripcion = new TextArea();
        campoDescripcion.setPromptText("Descripción");
        campoDescripcion.setPrefRowCount(3);

        DatePicker campoFechaLimite = new DatePicker();
        campoFechaLimite.setPromptText("Fecha límite (opcional)");

        if (proyectoEditar != null) {
            campoNombre.setText(proyectoEditar.getNombre());
            campoDescripcion.setText(proyectoEditar.getDescripcion());
            campoFechaLimite.setValue(proyectoEditar.getFechaLimite());
        }

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
        ButtonType botonCrear = new ButtonType(
                proyectoEditar == null ? "Crear" : "Guardar",
                ButtonBar.ButtonData.OK_DONE
        );
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

                    if (proyectoEditar == null) {

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

                        Toast.mostrar(raizActual, "Proyecto creado");

                    } else {

                        proyectoEditar.setNombre(nombre);
                        proyectoEditar.setDescripcion(campoDescripcion.getText());
                        proyectoEditar.setFechaLimite(campoFechaLimite.getValue());

                        proyectoDAO.actualizar(proyectoEditar);

                        Toast.mostrar(raizActual, "Proyecto actualizado");
                    }

                    cargarProyectos();

                } catch (SQLException ex) {

                    mensajeError.setText(
                            proyectoEditar == null
                                    ? "Error al crear el proyecto."
                                    : "Error al actualizar el proyecto."
                    );

                    mensajeError.setVisible(true);
                    ex.printStackTrace();
                    }
                }
            return null;
        });

        dialog.showAndWait();
    }

    private void eliminarProyecto(Proyecto proyecto) {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar proyecto");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(
                "¿Deseas eliminar el proyecto \"" +
                        proyecto.getNombre() +
                        "\"?\n\nEsta acción eliminará también todas sus secciones, funcionalidades, notas, subtareas y demás información."
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            try {

                proyectoDAO.eliminar(proyecto.getIdProyecto());

                cargarProyectos();

                Toast.mostrar(raizActual, "Proyecto eliminado correctamente");

            } catch (SQLException ex) {

                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText(null);
                error.setContentText("No fue posible eliminar el proyecto.");
                error.showAndWait();

                ex.printStackTrace();
            }

        }
    }
}