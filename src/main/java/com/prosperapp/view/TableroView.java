package com.prosperapp.view;

import com.prosperapp.dao.FuncionalidadDAO;
import com.prosperapp.dao.SeccionDAO;
import com.prosperapp.model.Funcionalidad;
import com.prosperapp.model.Proyecto;
import com.prosperapp.model.Seccion;
import com.prosperapp.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import com.prosperapp.util.Toast;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TableroView {

    private final Stage stage;
    private final Usuario usuario;
    private final Proyecto proyecto;
    private final SeccionDAO seccionDAO = new SeccionDAO();
    private final FuncionalidadDAO funcionalidadDAO = new FuncionalidadDAO();
    private StackPane raizActual;

    public TableroView(Stage stage, Usuario usuario, Proyecto proyecto) {
        this.stage = stage;
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    public Scene construir() {

        Button botonVolver = new Button("← Volver");
        botonVolver.getStyleClass().add("enlace-secundario");
        botonVolver.setOnAction(e -> {
            DashboardView dashboardView = new DashboardView(stage, usuario);
            stage.setScene(dashboardView.construir());
        });

        Label titulo = new Label(proyecto.getNombre());
        titulo.getStyleClass().add("titulo-app");

        VBox encabezado = new VBox(5, botonVolver, titulo);

        HBox columnas = new HBox(20);
        columnas.setPadding(new Insets(20, 0, 0, 0));

        try {
            List<Seccion> secciones = seccionDAO.listarPorProyecto(proyecto.getIdProyecto());

            for (Seccion seccion : secciones) {
                columnas.getChildren().add(crearColumnaSeccion(seccion));
            }

        } catch (SQLException ex) {
            Label error = new Label("Error al cargar las secciones.");
            error.getStyleClass().add("mensaje-error");
            columnas.getChildren().add(error);
            ex.printStackTrace();
        }

        ScrollPane scrollHorizontal = new ScrollPane(columnas);
        scrollHorizontal.setFitToHeight(true);
        scrollHorizontal.setStyle("-fx-background-color: transparent;");

        VBox contenedor = new VBox(10, encabezado, scrollHorizontal);
        contenedor.setPadding(new Insets(30));

        StackPane root = new StackPane(contenedor);
        this.raizActual = root;

        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }

    private VBox crearColumnaSeccion(Seccion seccion) {

        Label nombreSeccion = new Label(seccion.getNombre());
        nombreSeccion.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        VBox tarjetasFuncionalidad = new VBox(10);

        try {
            List<Funcionalidad> funcionalidades = funcionalidadDAO.listarPorSeccion(seccion.getIdSeccion());

            for (Funcionalidad f : funcionalidades) {
                tarjetasFuncionalidad.getChildren().add(crearTarjetaFuncionalidad(f, seccion));
            }

        } catch (SQLException ex) {
            Label error = new Label("Error al cargar.");
            error.getStyleClass().add("mensaje-error");
            tarjetasFuncionalidad.getChildren().add(error);
            ex.printStackTrace();
        }

        Button botonAgregar = new Button("+ Agregar funcionalidad");
        botonAgregar.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #2E7D32; " +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-underline: true; -fx-padding: 5 0 0 0;"
        );
        botonAgregar.setOnAction(e -> mostrarDialogoFuncionalidad(seccion, null));

        VBox columna = new VBox(10, nombreSeccion, tarjetasFuncionalidad, botonAgregar);
        columna.setPrefWidth(240);
        columna.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-padding: 15px;");

        return columna;
    }

    private VBox crearTarjetaFuncionalidad(Funcionalidad funcionalidad, Seccion seccion) {

        Label titulo = new Label(funcionalidad.getTitulo());
        titulo.setStyle("-fx-font-weight: bold;");
        titulo.setWrapText(true);

        Label prioridad = new Label("Prioridad: " + funcionalidad.getPrioridad());
        prioridad.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");

        Label fechaLimite = new Label(
                funcionalidad.getFechaLimite() != null
                        ? "Vence: " + funcionalidad.getFechaLimite()
                        : "Sin fecha límite"
        );
        fechaLimite.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");

        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");

        btnEditar.setOnAction(e ->
                mostrarDialogoFuncionalidad(seccion, funcionalidad)
        );

        btnEliminar.setOnAction(e -> {

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar funcionalidad");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText(
                    "¿Deseas eliminar la funcionalidad \"" +
                            funcionalidad.getTitulo() + "\"?"
            );

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                try {

                    funcionalidadDAO.eliminar(funcionalidad.getIdFuncionalidad());

                    stage.setScene(construir());

                    Toast.mostrar(raizActual, "Funcionalidad eliminada");

                } catch (SQLException ex) {

                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setHeaderText(null);
                    error.setContentText("No fue posible eliminar la funcionalidad.");
                    error.showAndWait();

                    ex.printStackTrace();
                }
            }
        });

        HBox botones = new HBox(8, btnEditar, btnEliminar);

        VBox tarjeta = new VBox(4, titulo, prioridad, fechaLimite, botones);

        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);");

        tarjeta.setOnMouseClicked(e -> {
            if (e.getTarget() instanceof Button) {
                return;
            }

            DetalleFuncionalidadView detalleView =
                    new DetalleFuncionalidadView(stage, usuario, proyecto, funcionalidad);

            stage.setScene(detalleView.construir());
        });

        return tarjeta;
    }

    private void mostrarDialogoFuncionalidad(Seccion seccion, Funcionalidad funcionalidadEditar) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(
                funcionalidadEditar == null
                        ? "Nueva Funcionalidad en \"" + seccion.getNombre() + "\""
                        : "Editar Funcionalidad"
        );

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");
        campoTitulo.getStyleClass().add("campo-texto");

        TextArea campoDescripcion = new TextArea();
        campoDescripcion.setPromptText("Descripción");
        campoDescripcion.setPrefRowCount(3);

        ComboBox<String> campoPrioridad = new ComboBox<>();
        campoPrioridad.getItems().addAll("alta", "media", "baja");
        campoPrioridad.setValue("media");
        campoPrioridad.setMaxWidth(Double.MAX_VALUE);

        DatePicker campoFechaLimite = new DatePicker();
        campoFechaLimite.setPromptText("Fecha límite (opcional)");
        campoFechaLimite.setPromptText("Fecha límite");

        if (funcionalidadEditar != null) {
            campoTitulo.setText(funcionalidadEditar.getTitulo());
            campoDescripcion.setText(funcionalidadEditar.getDescripcion());
            campoPrioridad.setValue(funcionalidadEditar.getPrioridad());
            campoFechaLimite.setValue(funcionalidadEditar.getFechaLimite());
        }

        Label mensajeError = new Label();
        mensajeError.getStyleClass().add("mensaje-error");
        mensajeError.setVisible(false);

        VBox contenido = new VBox(10, campoTitulo, campoDescripcion, campoPrioridad, campoFechaLimite, mensajeError);
        contenido.setPadding(new Insets(15));
        contenido.setPrefWidth(320);

        dialog.getDialogPane().setContent(contenido);
        ButtonType botonCrear = new ButtonType(
                funcionalidadEditar == null ? "Crear" : "Guardar",
                ButtonBar.ButtonData.OK_DONE
        );
        dialog.getDialogPane().getButtonTypes().addAll(botonCrear, ButtonType.CANCEL);

        dialog.setResultConverter(botonPresionado -> {
            if (botonPresionado == botonCrear) {

                String titulo = campoTitulo.getText().trim();
                LocalDate fecha = campoFechaLimite.getValue();

                if (titulo.isEmpty()) {
                    mensajeError.setText("El título es obligatorio.");
                    mensajeError.setVisible(true);
                    return null;
                }

                try {

                    if (funcionalidadEditar == null) {

                        Funcionalidad nueva = new Funcionalidad();
                        nueva.setIdSeccion(seccion.getIdSeccion());
                        nueva.setTitulo(titulo);
                        nueva.setDescripcion(campoDescripcion.getText());
                        nueva.setPrioridad(campoPrioridad.getValue());
                        nueva.setFechaLimite(fecha);

                        funcionalidadDAO.crear(nueva);

                        Toast.mostrar(raizActual, "Funcionalidad creada");

                    } else {

                        funcionalidadEditar.setTitulo(titulo);
                        funcionalidadEditar.setDescripcion(campoDescripcion.getText());
                        funcionalidadEditar.setPrioridad(campoPrioridad.getValue());
                        funcionalidadEditar.setFechaLimite(fecha);

                        funcionalidadDAO.actualizar(funcionalidadEditar);

                        Toast.mostrar(raizActual, "Funcionalidad actualizada");
                    }

                    stage.setScene(construir());

                } catch (SQLException ex) {

                    mensajeError.setText(
                            funcionalidadEditar == null
                                    ? "Error al crear la funcionalidad."
                                    : "Error al actualizar la funcionalidad."
                    );

                    mensajeError.setVisible(true);
                    ex.printStackTrace();
                }
            }

            return null;
        });

        dialog.showAndWait();
    }
}