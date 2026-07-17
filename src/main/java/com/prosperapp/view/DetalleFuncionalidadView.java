package com.prosperapp.view;

import com.prosperapp.dao.*;
import com.prosperapp.model.*;
import com.prosperapp.util.Toast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class DetalleFuncionalidadView {

    private final Stage stage;
    private final Usuario usuario;
    private final Proyecto proyecto;
    private final Funcionalidad funcionalidad;
    private StackPane raizActual;
    private final SubtareaDAO subtareaDAO = new SubtareaDAO();
    private final NotaDAO notaDAO = new NotaDAO();
    private final DecisionTecnicaDAO decisionDAO = new DecisionTecnicaDAO();
    private final FragmentoCodigoDAO fragmentoDAO = new FragmentoCodigoDAO();

    public DetalleFuncionalidadView(Stage stage, Usuario usuario, Proyecto proyecto, Funcionalidad funcionalidad) {
        this.stage = stage;
        this.usuario = usuario;
        this.proyecto = proyecto;
        this.funcionalidad = funcionalidad;
    }

    public Scene construir() {

        Button botonVolver = new Button("← Volver al tablero");
        botonVolver.getStyleClass().add("enlace-secundario");
        botonVolver.setOnAction(e -> {
            TableroView tableroView = new TableroView(stage, usuario, proyecto);
            stage.setScene(tableroView.construir());
        });

        Label titulo = new Label(funcionalidad.getTitulo());
        titulo.getStyleClass().add("titulo-app");

        Label descripcion = new Label(
                funcionalidad.getDescripcion() == null || funcionalidad.getDescripcion().isEmpty()
                        ? "Sin descripción"
                        : funcionalidad.getDescripcion()
        );
        descripcion.setWrapText(true);
        descripcion.setStyle("-fx-text-fill: #444;");

        Label meta = new Label(
                "Prioridad: " + funcionalidad.getPrioridad() +
                        "   |   " +
                        (funcionalidad.getFechaLimite() != null
                                ? "Vence: " + funcionalidad.getFechaLimite()
                                : "Sin fecha límite")
        );
        meta.setStyle("-fx-font-size: 12px; -fx-text-fill: #4A4A4A;");

        VBox encabezado = new VBox(6, botonVolver, titulo, meta, descripcion);
        encabezado.setPadding(new Insets(0, 0, 15, 0));

        TabPane pestañas = new TabPane();
        pestañas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabSubtareas = new Tab("Subtareas", construirTabSubtareas());
        Tab tabNotas = new Tab("Notas", construirTabNotas());
        Tab tabDecisiones = new Tab("Decisiones Técnicas", construirTabDecisiones());
        Tab tabFragmentos = new Tab("Fragmentos de Código", construirTabFragmentos());

        pestañas.getTabs().addAll(tabSubtareas, tabNotas, tabDecisiones, tabFragmentos);

        VBox contenedor = new VBox(10, encabezado, pestañas);
        contenedor.setPadding(new Insets(30));

        StackPane raiz = new StackPane(contenedor);
        Scene scene = new Scene(raiz, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        this.raizActual = raiz;

        return scene;
    }

    // ================= SUBTAREAS (checklist) =================

    private VBox construirTabSubtareas() {

        VBox lista = new VBox(8);
        lista.setPadding(new Insets(15));

        try {
            List<Subtarea> subtareas = subtareaDAO.listarPorFuncionalidad(funcionalidad.getIdFuncionalidad());

            for (Subtarea s : subtareas) {

                CheckBox check = new CheckBox(s.getDescripcion());
                check.setSelected("completada".equalsIgnoreCase(s.getEstado()));

                check.setOnAction(e -> {
                    try {
                        s.setEstado(check.isSelected() ? "completada" : "pendiente");
                        subtareaDAO.actualizar(s);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

                Button btnEditar = new Button("Editar");
                Button btnEliminar = new Button("Eliminar");



                btnEditar.setOnAction(e -> {

                    TextInputDialog dialog = new TextInputDialog(s.getDescripcion());
                    dialog.setTitle("Editar subtarea");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Descripción:");

                    dialog.showAndWait().ifPresent(texto -> {

                        if (texto.trim().isEmpty()) {
                            return;
                        }

                        try {

                            s.setDescripcion(texto.trim());

                            subtareaDAO.actualizar(s);

                            stage.setScene(construir());

                            Toast.mostrar(raizActual, "Subtarea actualizada");

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });

                });

                btnEliminar.setOnAction(e -> {

                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Eliminar subtarea");
                    confirmacion.setHeaderText(null);
                    confirmacion.setContentText("¿Eliminar esta subtarea?");

                    if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                        try {

                            subtareaDAO.eliminar(s.getIdSubtarea());

                            stage.setScene(construir());

                            Toast.mostrar(raizActual, "Subtarea eliminada");

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }

                });

                HBox fila = new HBox(8, check, btnEditar, btnEliminar);
                fila.setAlignment(Pos.CENTER_LEFT);

                lista.getChildren().add(fila);
            }

        } catch (SQLException ex) {
            lista.getChildren().add(new Label("Error al cargar subtareas."));
            ex.printStackTrace();
        }

        TextField campoNueva = new TextField();
        campoNueva.setPromptText("Nueva subtarea");
        campoNueva.getStyleClass().add("campo-texto");

        Button botonAgregar = new Button("+ Agregar");
        botonAgregar.getStyleClass().add("boton-primario");
        botonAgregar.setOnAction(e -> {
            String texto = campoNueva.getText().trim();
            if (texto.isEmpty()) return;

            try {
                Subtarea nueva = new Subtarea();
                nueva.setIdFuncionalidad(funcionalidad.getIdFuncionalidad());
                nueva.setDescripcion(texto);
                nueva.setEstado("pendiente");
                subtareaDAO.crear(nueva);

                stage.setScene(construir());
                Toast.mostrar(raizActual, "Subtarea agregada");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        HBox filaAgregar = new HBox(8, campoNueva, botonAgregar);
        filaAgregar.setAlignment(Pos.CENTER_LEFT);
        filaAgregar.setPadding(new Insets(10, 15, 15, 15));

        VBox contenedorTab = new VBox(lista, filaAgregar);
        return contenedorTab;
    }

    // ================= NOTAS =================

    private VBox construirTabNotas() {

        VBox lista = new VBox(10);
        lista.setPadding(new Insets(15));

        try {
            List<Nota> notas = notaDAO.listarPorFuncionalidad(funcionalidad.getIdFuncionalidad());

            for (Nota n : notas) {

                Label contenido = new Label(n.getContenido());
                contenido.setWrapText(true);

                Label fecha = new Label(n.getFechaCreacion().toLocalDate().toString());
                fecha.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

                Button btnEditar = new Button("Editar");
                Button btnEliminar = new Button("Eliminar");


                btnEditar.setOnAction(e -> {

                    TextInputDialog dialog = new TextInputDialog(n.getContenido());
                    dialog.setTitle("Editar nota");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Contenido:");

                    dialog.showAndWait().ifPresent(texto -> {

                        if (texto.trim().isEmpty()) {
                            return;
                        }

                        try {
                            n.setContenido(texto.trim());
                            notaDAO.actualizar(n);

                            stage.setScene(construir());
                            Toast.mostrar(raizActual, "Nota actualizada");

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });

                });

                btnEliminar.setOnAction(e -> {

                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Eliminar nota");
                    confirmacion.setHeaderText(null);
                    confirmacion.setContentText("¿Eliminar esta nota?");

                    if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                        try {

                            notaDAO.eliminar(n.getIdNota());

                            stage.setScene(construir());

                            Toast.mostrar(raizActual, "Nota eliminada");

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                });

                HBox botones = new HBox(8, btnEditar, btnEliminar);

                VBox item = new VBox(3, contenido, fecha, botones);
                item.getStyleClass().add("tarjeta-formulario");
                item.setStyle(item.getStyle() + "-fx-padding: 10px;");

                lista.getChildren().add(item);
            }

        } catch (SQLException ex) {
            lista.getChildren().add(new Label("Error al cargar notas."));
            ex.printStackTrace();
        }

        TextArea campoNueva = new TextArea();
        campoNueva.setPromptText("Nueva nota");
        campoNueva.setPrefRowCount(2);

        Button botonAgregar = new Button("+ Agregar nota");
        botonAgregar.getStyleClass().add("boton-primario");
        botonAgregar.setOnAction(e -> {
            String texto = campoNueva.getText().trim();
            if (texto.isEmpty()) return;

            try {
                Nota nueva = new Nota();
                nueva.setIdFuncionalidad(funcionalidad.getIdFuncionalidad());
                nueva.setContenido(texto);
                notaDAO.crear(nueva);

                stage.setScene(construir());
                Toast.mostrar(raizActual, "Nota agregada");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox filaAgregar = new VBox(6, campoNueva, botonAgregar);
        filaAgregar.setPadding(new Insets(10, 15, 15, 15));

        return new VBox(lista, filaAgregar);
    }

    // ================= DECISIONES TÉCNICAS =================

    private VBox construirTabDecisiones() {

        VBox lista = new VBox(10);
        lista.setPadding(new Insets(15));

        try {
            List<DecisionTecnica> decisiones = decisionDAO.listarPorFuncionalidad(funcionalidad.getIdFuncionalidad());

            for (DecisionTecnica d : decisiones) {
                Label titulo = new Label(d.getTitulo());
                titulo.setStyle("-fx-font-weight: bold;");

                Label descripcion = new Label(d.getDescripcion());
                descripcion.setWrapText(true);

                Button btnEditar = new Button("Editar");
                Button btnEliminar = new Button("Eliminar");

                btnEditar.setOnAction(e -> {

                    TextInputDialog dialogTitulo = new TextInputDialog(d.getTitulo());
                    dialogTitulo.setTitle("Editar decisión técnica");
                    dialogTitulo.setHeaderText(null);
                    dialogTitulo.setContentText("Título:");

                    dialogTitulo.showAndWait().ifPresent(tituloNuevo -> {

                        if (tituloNuevo.trim().isEmpty()) {
                            return;
                        }


                        TextInputDialog dialogDescripcion = new TextInputDialog(d.getDescripcion());
                        dialogDescripcion.setTitle("Editar decisión técnica");
                        dialogDescripcion.setHeaderText(null);
                        dialogDescripcion.setContentText("Descripción:");

                        dialogDescripcion.showAndWait().ifPresent(descripcionNueva -> {

                            if (descripcionNueva.trim().isEmpty()) {
                                return;
                            }

                            try {

                                d.setTitulo(tituloNuevo.trim());
                                d.setDescripcion(descripcionNueva.trim());

                                decisionDAO.actualizar(d);

                                stage.setScene(construir());

                                Toast.mostrar(raizActual, "Decisión técnica actualizada");

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                        });

                    });

                });

                btnEliminar.setOnAction(e -> {

                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Eliminar decisión técnica");
                    confirmacion.setHeaderText(null);
                    confirmacion.setContentText("¿Eliminar esta decisión técnica?");

                    if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                        try {

                            decisionDAO.eliminar(d.getIdDecision());

                            stage.setScene(construir());

                            Toast.mostrar(raizActual, "Decisión técnica eliminada");

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }

                });

                HBox botones = new HBox(8, btnEditar, btnEliminar);

                VBox item = new VBox(3, titulo, descripcion, botones);
                item.getStyleClass().add("tarjeta-formulario");
                item.setStyle(item.getStyle() + "-fx-padding: 10px;");

                lista.getChildren().add(item);
            }

        } catch (SQLException ex) {
            lista.getChildren().add(new Label("Error al cargar decisiones técnicas."));
            ex.printStackTrace();
        }

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título de la decisión");
        campoTitulo.getStyleClass().add("campo-texto");

        TextArea campoDescripcion = new TextArea();
        campoDescripcion.setPromptText("Descripción de la decisión");
        campoDescripcion.setPrefRowCount(2);

        Button botonAgregar = new Button("+ Agregar decisión técnica");
        botonAgregar.getStyleClass().add("boton-primario");
        botonAgregar.setOnAction(e -> {
            String titulo = campoTitulo.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            if (titulo.isEmpty() || descripcion.isEmpty()) return;

            try {
                DecisionTecnica nueva = new DecisionTecnica();
                nueva.setIdFuncionalidad(funcionalidad.getIdFuncionalidad());
                nueva.setTitulo(titulo);
                nueva.setDescripcion(descripcion);
                decisionDAO.crear(nueva);

                stage.setScene(construir());
                Toast.mostrar(raizActual, "Decisión técnica agregada");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox filaAgregar = new VBox(6, campoTitulo, campoDescripcion, botonAgregar);
        filaAgregar.setPadding(new Insets(10, 15, 15, 15));

        return new VBox(lista, filaAgregar);
    }

    // ================= FRAGMENTOS DE CÓDIGO =================

    private VBox construirTabFragmentos() {

        VBox lista = new VBox(10);
        lista.setPadding(new Insets(15));

        try {
            List<FragmentoCodigo> fragmentos = fragmentoDAO.listarPorFuncionalidad(funcionalidad.getIdFuncionalidad());

            for (FragmentoCodigo f : fragmentos) {
                Label lenguaje = new Label(f.getLenguaje() == null ? "Sin lenguaje especificado" : f.getLenguaje());
                lenguaje.setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");

                Label codigo = new Label(f.getCodigo());
                codigo.setStyle("-fx-font-family: 'Consolas', monospace; -fx-background-color: #F1F8F4; -fx-padding: 8px;");
                codigo.setWrapText(true);

                VBox item = new VBox(3, lenguaje, codigo);
                item.getStyleClass().add("tarjeta-formulario");
                item.setStyle(item.getStyle() + "-fx-padding: 10px;");

                lista.getChildren().add(item);
            }

        } catch (SQLException ex) {
            lista.getChildren().add(new Label("Error al cargar fragmentos de código."));
            ex.printStackTrace();
        }

        TextField campoLenguaje = new TextField();
        campoLenguaje.setPromptText("Lenguaje (ej: SQL, Java)");
        campoLenguaje.getStyleClass().add("campo-texto");

        TextArea campoCodigo = new TextArea();
        campoCodigo.setPromptText("Código");
        campoCodigo.setPrefRowCount(3);
        campoCodigo.setStyle("-fx-font-family: 'Consolas', monospace;");

        Button botonAgregar = new Button("+ Agregar fragmento");
        botonAgregar.getStyleClass().add("boton-primario");
        botonAgregar.setOnAction(e -> {
            String codigo = campoCodigo.getText().trim();
            if (codigo.isEmpty()) return;

            try {
                FragmentoCodigo nuevo = new FragmentoCodigo();
                nuevo.setIdFuncionalidad(funcionalidad.getIdFuncionalidad());
                nuevo.setLenguaje(campoLenguaje.getText().trim());
                nuevo.setCodigo(codigo);
                fragmentoDAO.crear(nuevo);

                stage.setScene(construir());
                Toast.mostrar(raizActual, "Fragmento agregado");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox filaAgregar = new VBox(6, campoLenguaje, campoCodigo, botonAgregar);
        filaAgregar.setPadding(new Insets(10, 15, 15, 15));

        return new VBox(lista, filaAgregar);
    }
}