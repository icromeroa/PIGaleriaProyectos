package galeria.components.views;

import galeria.app.MainApp;
import galeria.dao.*;
import galeria.model.*;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class EditarProyecto extends ScrollPane {

    private final String ESTILO_INPUTS = "-fx-font-family: 'Manrope'; -fx-padding: 10 15; -fx-font-size: 14px;";

    private final Proyecto proyectoOriginal;
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final AutorDAO autorDAO = new AutorDAO();

    private TextField txtTitulo;
    private TextArea txtResumen;
    private FlowPane contenedorAutores;
    private List<Autor> autoresEditados;

    private ComboBox<Categoria> cbCategoria;
    private ComboBox<Facultad> cbFacultad;
    private ComboBox<Programa> cbPrograma;
    private ComboBox<Materia> cbMateria;
    private ComboBox<Semestre> cbSemestre;

    private TextField txtEnlaceExterno;

    public EditarProyecto(Proyecto proyectoRecibido) {
        System.out.println("[DEBUG] Iniciando EditarProyecto...");
        if (proyectoRecibido == null) {
            System.out.println("[ERROR] El proyectoRecibido es NULO. Por eso no se ve nada.");
        } else {
            System.out.println("[DEBUG] Recibido Proyecto ID: " + proyectoRecibido.getIdProyecto());
            System.out.println("[DEBUG] Título: " + proyectoRecibido.getTitulo());
        }

        this.proyectoOriginal = proyectoRecibido;
        this.autoresEditados = (proyectoOriginal != null) ? new ArrayList<>(proyectoOriginal.getListaAutores()) : new ArrayList<>();

        this.getStyleClass().add("scroll-pane");
        this.setFitToWidth(true);
        this.getStylesheets().add(getClass().getResource("/galeria/css/app.css").toExternalForm());

        VBox content = new VBox(35);
        content.setPadding(new Insets(40, 80, 40, 80));
        content.setMaxWidth(1000);
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 25;");

        if (proyectoOriginal != null) {
            content.getChildren().addAll(
                    crearHeader(),
                    crearSeccionGeneral(),
                    crearSeccionCategorizacion(),
                    crearSeccionMultimedia(),
                    crearFooter()
            );
        } else {
            content.getChildren().add(new Label("Error: No se pudo cargar la información del proyecto."));
        }

        this.setContent(new StackPane(content));
        this.setPadding(new Insets(0));

        System.out.println("[DEBUG] Llamando a cargarDatosYPreseleccionar()...");
        cargarDatosYPreseleccionar();

        Animations.slideUpFadeIn(content, 100);
        System.out.println("[DEBUG] Vista EditarProyecto construida.");
    }

    private HBox crearHeader() {
        VBox info = new VBox(5);
        Label tituloLabel = new Label("Editar Proyecto");
        tituloLabel.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label idLab = new Label("ID del Registro: #UR-" + proyectoOriginal.getIdProyecto());
        idLab.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13;");

        info.getChildren().addAll(tituloLabel, idLab);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnClose = new Button();
        btnClose.setGraphic(new FontIcon("fas-times"));
        btnClose.setStyle("-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: #64748B;");
        btnClose.setCursor(Cursor.HAND);
        btnClose.setOnAction(e -> {
            System.out.println("[DEBUG] Cerrar edición. Volviendo a Detalle.");
            MainApp.setView(new DetalleProyecto(proyectoOriginal, null));
        });

        HBox h = new HBox(info, spacer, btnClose);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    private VBox crearSeccionGeneral() {
        VBox sec = new VBox(20);
        Label header = crearBadgeSeccion("01", "Información General");

        VBox campos = new VBox(15);

        Label lblT = new Label("Título del proyecto");
        lblT.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");
        txtTitulo = new TextField(proyectoOriginal.getTitulo());
        txtTitulo.getStyleClass().add("input-moderno");
        txtTitulo.setStyle(ESTILO_INPUTS);

        Label lblR = new Label("Resumen");
        lblR.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");
        txtResumen = new TextArea(proyectoOriginal.getResumen());
        txtResumen.getStyleClass().add("area-moderna");
        txtResumen.setPrefHeight(120);
        txtResumen.setWrapText(true);
        txtResumen.setStyle(ESTILO_INPUTS);

        Label lblA = new Label("Autores vinculados");
        lblA.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");
        contenedorAutores = new FlowPane(12, 12);
        actualizarPildorasAutores();

        campos.getChildren().addAll(lblT, txtTitulo, lblR, txtResumen, lblA, contenedorAutores);
        sec.getChildren().addAll(header, campos);
        return sec;
    }

    private void actualizarPildorasAutores() {
        contenedorAutores.getChildren().clear();
        for (Autor a : autoresEditados) {
            HBox pildora = new HBox(10);
            pildora.setAlignment(Pos.CENTER_LEFT);
            pildora.setPadding(new Insets(6, 12, 6, 12));
            pildora.getStyleClass().add("pildora-autor");

            Label name = new Label(a.getNombreAutor());

            Button btnX = new Button();
            btnX.setGraphic(new FontIcon("fas-times"));
            btnX.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-text-fill: #94A3B8;");
            btnX.setCursor(Cursor.HAND);
            btnX.setOnAction(e -> {
                System.out.println("[DEBUG] Removiendo autor: " + a.getNombreAutor());
                autoresEditados.remove(a);
                actualizarPildorasAutores();
            });

            pildora.getChildren().addAll(new FontIcon("fas-user-circle"), name, btnX);
            contenedorAutores.getChildren().add(pildora);
        }

        Button btnAdd = new Button("Agregar autor");
        btnAdd.setGraphic(new FontIcon("fas-plus"));
        btnAdd.setStyle("-fx-background-color: #FFF7ED; -fx-text-fill: #F97316; -fx-border-color: #FFEDD5; -fx-border-radius: 20; -fx-background-radius: 20; -fx-font-weight: bold;");
        btnAdd.setCursor(Cursor.HAND);
        btnAdd.setOnAction(e -> mostrarModalNuevoAutor());
        contenedorAutores.getChildren().add(btnAdd);
    }

    private void mostrarModalNuevoAutor() {
        System.out.println("[DEBUG] Abriendo modal de nuevo autor");
        Dialog<Autor> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Autor");

        ButtonType btnOk = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField n = new TextField(); n.setPromptText("Nombre completo");
        TextField c = new TextField(); c.setPromptText("correo@ejemplo.com");

        grid.add(new Label("Nombre:"), 0, 0); grid.add(n, 1, 0);
        grid.add(new Label("Correo:"), 0, 1); grid.add(c, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(b -> b == btnOk ? new Autor(0, n.getText(), c.getText()) : null);

        dialog.showAndWait().ifPresent(autor -> {
            System.out.println("[DEBUG] Autor agregado: " + autor.getNombreAutor());
            autoresEditados.add(autor);
            actualizarPildorasAutores();
        });
    }

    private VBox crearSeccionCategorizacion() {
        VBox sec = new VBox(20);
        Label header = crearBadgeSeccion("02", "Categorización");

        GridPane grid = new GridPane();
        grid.setHgap(30); grid.setVgap(20);

        cbCategoria = new ComboBox<>();
        cbFacultad = new ComboBox<>();
        cbPrograma = new ComboBox<>();
        cbMateria = new ComboBox<>();
        cbSemestre = new ComboBox<>();

        cbCategoria.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreCategoria());
            }
        });
        cbCategoria.setButtonCell(cbCategoria.getCellFactory().call(null));

        cbFacultad.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Facultad item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreFacultad());
            }
        });
        cbFacultad.setButtonCell(cbFacultad.getCellFactory().call(null));

        cbPrograma.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Programa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombrePrograma());
            }
        });
        cbPrograma.setButtonCell(cbPrograma.getCellFactory().call(null));

        cbMateria.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Materia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreMateria());
            }
        });
        cbMateria.setButtonCell(cbMateria.getCellFactory().call(null));

        cbSemestre.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Semestre item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getAnio() + " - " + item.getPeriodo());
                }
            }
        });
        cbSemestre.setButtonCell(cbSemestre.getCellFactory().call(null));

        configCombo(cbCategoria, "Categoría", grid, 0, 0);
        configCombo(cbFacultad, "Facultad", grid, 1, 0);
        configCombo(cbPrograma, "Programa", grid, 0, 1);
        configCombo(cbMateria, "Materia", grid, 1, 1);
        configCombo(cbSemestre, "Semestre", grid, 0, 2);

        sec.getChildren().addAll(header, grid);
        return sec;
    }

    private void configCombo(ComboBox<?> cb, String label, GridPane grid, int col, int row) {
        VBox box = new VBox(8);
        Label l = new Label(label);
        l.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");
        cb.getStyleClass().add("combo-box-moderno");
        cb.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14px; -fx-padding: 2;");
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setPrefHeight(45);
        box.getChildren().addAll(l, cb);
        grid.add(box, col, row);
        GridPane.setHgrow(box, Priority.ALWAYS);
    }

    private void cargarDatosYPreseleccionar() {
        System.out.println("[DEBUG] Iniciando carga de datos en Combos...");

        try {
            cbCategoria.getItems().setAll(new CategoriaDAO().listar());
            cbFacultad.getItems().setAll(new FacultadDAO().listar());
            cbPrograma.getItems().setAll(new ProgramaDAO().listar());
            cbMateria.getItems().setAll(new MateriaDAO().listar());
            cbSemestre.getItems().setAll(new SemestreDAO().listar());
            System.out.println("[DEBUG] Listas de DAOs cargadas correctamente.");
        } catch (Exception e) {
            System.out.println("[ERROR] Falló la carga de datos desde la DB: " + e.getMessage());
        }

        if (proyectoOriginal.getCategoria() != null) {
            System.out.println("[DEBUG] Preseleccionando Categoría ID: " + proyectoOriginal.getCategoria().getIdCategoria());
            cbCategoria.getItems().stream()
                    .filter(c -> c.getIdCategoria() == proyectoOriginal.getCategoria().getIdCategoria())
                    .findFirst()
                    .ifPresent(seleccion -> cbCategoria.getSelectionModel().select(seleccion));
        }

        if (proyectoOriginal.getFacultad() != null) {
            cbFacultad.getItems().stream()
                    .filter(f -> f.getIdFacultad() == proyectoOriginal.getFacultad().getIdFacultad())
                    .findFirst()
                    .ifPresent(seleccion -> cbFacultad.getSelectionModel().select(seleccion));
        }

        if (proyectoOriginal.getPrograma() != null) {
            cbPrograma.getItems().stream()
                    .filter(p -> p.getIdPrograma() == proyectoOriginal.getPrograma().getIdPrograma())
                    .findFirst()
                    .ifPresent(seleccion -> cbPrograma.getSelectionModel().select(seleccion));
        }

        if (proyectoOriginal.getMateria() != null) {
            cbMateria.getItems().stream()
                    .filter(m -> m.getIdMateria() == proyectoOriginal.getMateria().getIdMateria())
                    .findFirst()
                    .ifPresent(seleccion -> cbMateria.getSelectionModel().select(seleccion));
        }

        if (proyectoOriginal.getSemestre() != null) {
            cbSemestre.getItems().stream()
                    .filter(s -> s.getIdSemestre() == proyectoOriginal.getSemestre().getIdSemestre())
                    .findFirst()
                    .ifPresent(seleccion -> cbSemestre.getSelectionModel().select(seleccion));
        }
        System.out.println("[DEBUG] Fin de preselección.");
    }

    private VBox crearSeccionMultimedia() {
        System.out.println("[DEBUG] Creando sección multimedia...");
        VBox sec = new VBox(20);
        Label header = crearBadgeSeccion("03", "Archivos y Multimedia");
        HBox layout = new HBox(40);

        VBox portBox = new VBox(10);
        Label lblP = new Label("Imagen de Portada");
        lblP.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");

        ImageView preview = new ImageView();

        // --- LOGICA DE CARGA SEGURA ---
// REEMPLAZA todo el bloque try/catch de la imagen por esto:
        try {
            String url = proyectoOriginal.getPortadaURL();
            System.out.println("[DEBUG] URL de portada: " + url);

            if (url != null && !url.trim().isEmpty()) {
                String ruta = url.startsWith("/") ? url : "/" + url;

                // Intento 1: classpath
                var resource = getClass().getResource(ruta);
                if (resource != null) {
                    preview.setImage(new Image(resource.toExternalForm()));
                } else {
                    // Intento 2: filesystem directo
                    java.io.File archivo = new java.io.File("src/main/resources" + ruta);
                    if (archivo.exists()) {
                        preview.setImage(new Image(archivo.toURI().toString()));
                    } else {
                        // Fallback: imagen por defecto
                        cargarImagenPorDefecto(preview);
                    }
                }
            } else {
                // Sin URL: imagen por defecto
                cargarImagenPorDefecto(preview);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo cargar la imagen: " + e.getMessage());
            cargarImagenPorDefecto(preview);
        }
        // ------------------------------

        preview.setFitWidth(180);
        preview.setFitHeight(180);
        preview.setPreserveRatio(true);
        portBox.getChildren().addAll(lblP, preview);

        VBox fileBox = new VBox(15);
        HBox.setHgrow(fileBox, Priority.ALWAYS);

        VBox dropZone = new VBox(10);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.setPrefHeight(120);
        dropZone.setStyle("-fx-border-color: #CBD5E1; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-radius: 15;");

        Label lblDrop = new Label("Arrastra tus archivos aquí (No habilitado)");
        lblDrop.setStyle("-fx-font-family: 'Manrope'; -fx-text-fill: #94A3B8;");
        dropZone.getChildren().addAll(new FontIcon("fas-cloud-upload-alt"), lblDrop);

        Label lblEnlace = new Label("Enlace Externo (Repo/Web)");
        lblEnlace.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");

        txtEnlaceExterno = new TextField(proyectoOriginal.getArchivoURL());
        txtEnlaceExterno.getStyleClass().add("input-moderno");
        txtEnlaceExterno.setStyle("-fx-font-family: 'Manrope'; -fx-padding: 10 15; -fx-font-size: 14px;");

        fileBox.getChildren().addAll(new Label("Archivos del Proyecto"), dropZone, lblEnlace, txtEnlaceExterno);
        layout.getChildren().addAll(portBox, fileBox);
        sec.getChildren().addAll(header, layout);

        return sec;
    }

    private HBox crearFooter() {
        Button btnDelete = new Button("Eliminar Proyecto");
        FontIcon trashIcon = new FontIcon("fas-trash-alt");
        trashIcon.setIconColor(Color.web("#EF4444"));

        btnDelete.setGraphic(trashIcon);
        btnDelete.setStyle("-fx-text-fill: #EF4444; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-background-color: transparent;");
        btnDelete.setCursor(Cursor.HAND);
        btnDelete.setOnAction(e -> {
            System.out.println("[DEBUG] Botón eliminar presionado.");
            accionEliminar();
        });

        Animations.attachHoverLift(btnDelete);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String estiloBaseBotones = "-fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-background-radius: 30; -fx-border-radius: 30; -fx-font-size: 14px;";

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefHeight(48);
        btnCancel.setPrefWidth(120);
        btnCancel.setStyle(estiloBaseBotones + "-fx-background-color: #F1F5F9; -fx-text-fill: #64748B; -fx-border-color: #E2E8F0; -fx-border-width: 1;");
        btnCancel.setCursor(Cursor.HAND);
        btnCancel.setOnAction(e -> {
            System.out.println("[DEBUG] Cancelar presionado.");
            MainApp.setView(new DetalleProyecto(proyectoOriginal, null));
        });

        Animations.attachHoverLift(btnCancel);

        Button btnSave = new Button("Guardar Cambios");
        btnSave.setPrefHeight(48);
        btnSave.setPrefWidth(200);
        btnSave.setStyle(estiloBaseBotones + "-fx-background-color: #F97316; -fx-text-fill: white;");
        btnSave.setCursor(Cursor.HAND);
        btnSave.setOnAction(e -> {
            System.out.println("[DEBUG] Guardar Cambios presionado.");
            accionGuardar();
        });

        Animations.attachHoverLift(btnSave);

        HBox h = new HBox(15, btnDelete, spacer, btnCancel, btnSave);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(30, 0, 0, 0));

        return h;
    }

    private Label crearBadgeSeccion(String num, String text) {
        HBox h = new HBox(12);
        Label n = new Label(num);
        n.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #2563EB; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 8;");
        Label t = new Label(text);
        t.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1E293B;");
        h.getChildren().addAll(n, t);
        h.setAlignment(Pos.CENTER_LEFT);
        Label wrapper = new Label();
        wrapper.setGraphic(h);
        return wrapper;
    }

    private void accionGuardar() {
        System.out.println("[DEBUG] Iniciando accionGuardar()...");
        proyectoOriginal.setTitulo(txtTitulo.getText());
        proyectoOriginal.setResumen(txtResumen.getText());
        proyectoOriginal.setArchivoURL(txtEnlaceExterno.getText());

        proyectoOriginal.setCategoria(cbCategoria.getValue());
        proyectoOriginal.setFacultad(cbFacultad.getValue());
        proyectoOriginal.setPrograma(cbPrograma.getValue());
        proyectoOriginal.setMateria(cbMateria.getValue());
        proyectoOriginal.setSemestre(cbSemestre.getValue());
        proyectoOriginal.setListaAutores(autoresEditados);

        System.out.println("[DEBUG] Ejecutando UPDATE en base de datos para ID: " + proyectoOriginal.getIdProyecto());
        proyectoDAO.actualizarProyecto(proyectoOriginal);

        System.out.println("[DEBUG] Proyecto actualizado con éxito.");
        MainApp.setView(new DetalleProyecto(proyectoOriginal, null));
    }

    private void accionEliminar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar este proyecto?");
        alert.setContentText("Esta acción no se puede deshacer y eliminará autores y valoraciones asociados.");

        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK || res == ButtonType.YES) {
                try {
                    // Llamamos al método que acabamos de arreglar
                    proyectoDAO.eliminarProyecto(proyectoOriginal.getIdProyecto());

                    // IMPORTANTE: Volver al catálogo después de borrar
                    MainApp.setView(new Catalogo());

                } catch (Exception e) {
                    System.out.println("[DEBUG] Error al navegar tras eliminar: " + e.getMessage());
                }
            }
        });
    }

    private void cargarImagenPorDefecto(javafx.scene.image.ImageView iv) {
        // Rutas posibles del placeholder
        String[] rutas = {
                "/galeria/images.PD/p1.jpg",
                "/galeria/images/PD/p1.jpg",
                "/galeria/images.PD/p1.png",
                "/galeria/images/PD/p1.png"
        };
        for (String ruta : rutas) {
            try {
                var res = getClass().getResource(ruta);
                if (res != null) {
                    iv.setImage(new javafx.scene.image.Image(res.toExternalForm()));
                    System.out.println("[DEFAULT IMG] Cargada: " + ruta);
                    return;
                }
                java.io.File f = new java.io.File("src/main/resources" + ruta);
                if (f.exists()) {
                    iv.setImage(new javafx.scene.image.Image(f.toURI().toString()));
                    System.out.println("[DEFAULT IMG] Cargada desde filesystem: " + ruta);
                    return;
                }
            } catch (Exception ignored) {}
        }
        // Si ninguna funciona, fondo gris — sin imagen
        System.out.println("[DEFAULT IMG] No se encontró ningún placeholder, usando fondo gris.");
    }
}