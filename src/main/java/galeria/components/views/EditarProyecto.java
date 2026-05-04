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

    // El objeto que recibimos del "DetalleProyecto"
    private final Proyecto proyectoOriginal;

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final AutorDAO autorDAO = new AutorDAO();

    // Inputs de información general
    private TextField txtTitulo;
    private TextArea txtResumen;
    private FlowPane contenedorAutores;
    private List<Autor> autoresEditados;

    // Inputs de categorización
    private ComboBox<Categoria> cbCategoria;
    private ComboBox<Facultad> cbFacultad;
    private ComboBox<Programa> cbPrograma;
    private ComboBox<Materia> cbMateria;
    private ComboBox<Semestre> cbSemestre;

    // Multimedia
    private TextField txtEnlaceExterno;

    public EditarProyecto(Proyecto proyectoRecibido) {
        this.proyectoOriginal = proyectoRecibido;
        this.autoresEditados = new ArrayList<>(proyectoOriginal.getListaAutores());

        this.getStyleClass().add("scroll-pane");
        this.setFitToWidth(true);

        this.getStylesheets().add(getClass().getResource("/galeria/css/app.css").toExternalForm());

        VBox content = new VBox(35);
        content.setPadding(new Insets(40, 80, 40, 80));
        content.setMaxWidth(1000);
        content.setAlignment(Pos.TOP_CENTER);
        // Quitamos el sombreado como pediste anteriormente
        content.setStyle("-fx-background-color: white; -fx-background-radius: 25;");

        content.getChildren().addAll(
                crearHeader(),
                crearSeccionGeneral(),
                crearSeccionCategorizacion(),
                crearSeccionMultimedia(),
                crearFooter()
        );

        this.setContent(new StackPane(content));
        this.setPadding(new Insets(0));

        // Rellenar ComboBoxes y seleccionar los valores actuales
        cargarDatosYPreseleccionar();

        Animations.slideUpFadeIn(content, 100);
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
        // Volver atrás sin guardar
        btnClose.setOnAction(e -> MainApp.setView(new DetalleProyecto(proyectoOriginal, null)));

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
        txtTitulo.setStyle(ESTILO_INPUTS); // Aplicando Manrope y Padding

        Label lblR = new Label("Resumen");
        lblR.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");
        txtResumen = new TextArea(proyectoOriginal.getResumen());
        txtResumen.getStyleClass().add("area-moderna");
        txtResumen.setPrefHeight(120);
        txtResumen.setWrapText(true);
        txtResumen.setStyle(ESTILO_INPUTS); // Aplicando Manrope y Padding

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

        // --- CONFIGURACIÓN DE VISUALIZACIÓN DE TEXTO ---

        // Categoría -> nombreCategoria
        cbCategoria.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreCategoria());
            }
        });
        cbCategoria.setButtonCell(cbCategoria.getCellFactory().call(null));

        // Facultad -> nombreFacultad
        cbFacultad.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Facultad item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreFacultad());
            }
        });
        cbFacultad.setButtonCell(cbFacultad.getCellFactory().call(null));

        // Programa -> nombrePrograma
        cbPrograma.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Programa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombrePrograma());
            }
        });
        cbPrograma.setButtonCell(cbPrograma.getCellFactory().call(null));

        // Materia -> nombreMateria
        cbMateria.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Materia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombreMateria());
            }
        });
        cbMateria.setButtonCell(cbMateria.getCellFactory().call(null));

// Semestre -> Mostrar como "2024 - 1"
        cbSemestre.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Semestre item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    // Usamos tus métodos reales: getAnio() y getPeriodo()
                    setText(item.getAnio() + " - " + item.getPeriodo());
                }
            }
        });
        cbSemestre.setButtonCell(cbSemestre.getCellFactory().call(null));
        // --- FIN CONFIGURACIÓN ---

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
        // Esto asegura que el texto seleccionado (el "placeholder" con datos) tenga Manrope y padding
        cb.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14px; -fx-padding: 2;");

        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setPrefHeight(45);
        box.getChildren().addAll(l, cb);
        grid.add(box, col, row);
        GridPane.setHgrow(box, Priority.ALWAYS);
    }

    private void cargarDatosYPreseleccionar() {
        // 1. Cargamos las listas desde la DB
        cbCategoria.getItems().setAll(new CategoriaDAO().listar());
        cbFacultad.getItems().setAll(new FacultadDAO().listar());
        cbPrograma.getItems().setAll(new ProgramaDAO().listar());
        cbMateria.getItems().setAll(new MateriaDAO().listar());
        cbSemestre.getItems().setAll(new SemestreDAO().listar());

        // 2. Preseleccionar comparando por ID
        // Es CRUCIAL que el objeto seleccionado sea uno de los que acabamos de cargar en 'getItems()'

        if (proyectoOriginal.getCategoria() != null) {
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
    }

    private VBox crearSeccionMultimedia() {
        // 1. DECLARACIÓN DE LA VARIABLE 'sec' (Esto es lo que falta)
        VBox sec = new VBox(20);
        Label header = crearBadgeSeccion("03", "Archivos y Multimedia");

        HBox layout = new HBox(40);

        // Bloque de Portada
        VBox portBox = new VBox(10);
        Label lblP = new Label("Imagen de Portada");
        lblP.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-family: 'Manrope';");

        ImageView preview = new ImageView();
        try {
            // Carga segura de la imagen actual
            if (proyectoOriginal.getPortadaURL() != null && !proyectoOriginal.getPortadaURL().isEmpty()) {
                preview.setImage(new Image(proyectoOriginal.getPortadaURL(), true));
            } else {
                throw new Exception("Sin URL");
            }
        } catch (Exception e) {
            preview.setImage(new Image(getClass().getResourceAsStream("/galeria/resources/placeholder.png")));
        }
        preview.setFitWidth(180);
        preview.setFitHeight(180);
        preview.setPreserveRatio(true);
        portBox.getChildren().addAll(lblP, preview);

        // Bloque de Archivos
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
        // Aplicando Manrope y Padding solicitado
        txtEnlaceExterno.setStyle("-fx-font-family: 'Manrope'; -fx-padding: 10 15; -fx-font-size: 14px;");

        fileBox.getChildren().addAll(new Label("Archivos del Proyecto"), dropZone, lblEnlace, txtEnlaceExterno);

        layout.getChildren().addAll(portBox, fileBox);

        // 2. AHORA 'sec' EXISTE Y PUEDES AGREGARLE HIJOS
        sec.getChildren().addAll(header, layout);

        return sec;
    }

    private HBox crearFooter() {
        // --- BOTÓN ELIMINAR ---
        Button btnDelete = new Button("Eliminar Proyecto");
        FontIcon trashIcon = new FontIcon("fas-trash-alt");
        trashIcon.setIconColor(Color.web("#EF4444")); // Rojo para el icono

        btnDelete.setGraphic(trashIcon);
        // Letras rojas, sin fondo y fuente Manrope
        btnDelete.setStyle("-fx-text-fill: #EF4444; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-background-color: transparent;");
        btnDelete.setCursor(Cursor.HAND);
        btnDelete.setOnAction(e -> accionEliminar());

        // Integración de animación Hover
        Animations.attachHoverLift(btnDelete);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // --- CONFIGURACIÓN DE ESTILO COMÚN PARA BOTONES DE ACCIÓN ---
        // Radio de 30 para efecto "pill" (completamente redondeado)
        String estiloBaseBotones = "-fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-background-radius: 30; -fx-border-radius: 30; -fx-font-size: 14px;";

        // --- BOTÓN CANCELAR ---
        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefHeight(48);
        btnCancel.setPrefWidth(120); // Más delgado
        btnCancel.setStyle(estiloBaseBotones + "-fx-background-color: #F1F5F9; -fx-text-fill: #64748B; -fx-border-color: #E2E8F0; -fx-border-width: 1;");
        btnCancel.setCursor(Cursor.HAND);
        btnCancel.setOnAction(e -> MainApp.setView(new DetalleProyecto(proyectoOriginal, null)));

        // Integración de animación Hover
        Animations.attachHoverLift(btnCancel);

        // --- BOTÓN GUARDAR CAMBIOS ---
        Button btnSave = new Button("Guardar Cambios");
        btnSave.setPrefHeight(48);
        btnSave.setPrefWidth(200); // Más ancho
        btnSave.setStyle(estiloBaseBotones + "-fx-background-color: #F97316; -fx-text-fill: white;");
        btnSave.setCursor(Cursor.HAND);
        btnSave.setOnAction(e -> accionGuardar());

        // Integración de animación Hover
        Animations.attachHoverLift(btnSave);

        // --- LAYOUT FINAL ---
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
        // Actualizar el objeto con lo que hay en los inputs
        proyectoOriginal.setTitulo(txtTitulo.getText());
        proyectoOriginal.setResumen(txtResumen.getText());
        proyectoOriginal.setArchivoURL(txtEnlaceExterno.getText()); // CORREGIDO

        proyectoOriginal.setCategoria(cbCategoria.getValue());
        proyectoOriginal.setFacultad(cbFacultad.getValue());
        proyectoOriginal.setPrograma(cbPrograma.getValue());
        proyectoOriginal.setMateria(cbMateria.getValue());
        proyectoOriginal.setSemestre(cbSemestre.getValue());

        proyectoOriginal.setListaAutores(autoresEditados);

        // Llamar al DAO para persistir en DB
        proyectoDAO.actualizarProyecto(proyectoOriginal);

        // Feedback y navegación
        System.out.println("Proyecto actualizado con éxito.");
        MainApp.setView(new DetalleProyecto(proyectoOriginal, null));
    }

    private void accionEliminar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de eliminar este proyecto?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                proyectoDAO.eliminarProyecto(proyectoOriginal.getIdProyecto());
                // Redirigir a una vista general (ejemplo Categorías)
                MainApp.setView(new Categorias());
            }
        });
    }
}