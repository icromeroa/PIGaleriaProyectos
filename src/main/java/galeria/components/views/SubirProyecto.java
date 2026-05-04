package galeria.components.views;

import galeria.app.MainApp;
import galeria.dao.*;
import galeria.model.*;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class SubirProyecto extends ScrollPane {

    // Estilos constantes según requerimiento
    private final String RELLENO_INPUT = "-fx-background-color: #F1F3FC;";
    private final String BORDE_INPUT = "-fx-border-color: #C1C6D5; -fx-border-radius: 10; -fx-background-radius: 10;";
    private final String TEXTO_PLACEHOLDER = "-fx-prompt-text-fill: #6B7280; -fx-font-family: 'Manrope';";
    private final String TITULO_SEC = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F172A;";
    private final String ESTILO_CONTAINER = "-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-width: 1;";

    // Campos de datos
    private TextField txtTitulo, txtLink;
    private TextArea txtResumen;
    private ComboBox<Categoria> cbCategoria;
    private ComboBox<Facultad> cbFacultad;
    private ComboBox<Programa> cbPrograma;
    private ComboBox<Materia> cbMateria;
    private ComboBox<Semestre> cbSemestre;

    private FlowPane contenedorAutores;
    private List<Autor> listaAutores = new ArrayList<>();

    public SubirProyecto() {
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: white; -fx-background: white;");
        this.getStylesheets().add(getClass().getResource("/galeria/css/app.css").toExternalForm());

        VBox root = new VBox(30);
        root.setPadding(new Insets(40, 100, 40, 100));
        root.setMaxWidth(1100);
        root.setAlignment(Pos.TOP_CENTER);

        // --- HEADER ---
        VBox header = new VBox(10);
        Label lblMainTitle = new Label("Subir Nuevo Proyecto");
        lblMainTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");
        Label lblSub = new Label("Utiliza este formulario para registrar una nueva investigación o proyecto académico en el repositorio institucional. Asegúrate de completar todos los campos obligatorios para facilitar la indexación.");
        lblSub.setWrapText(true);
        lblSub.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px; -fx-font-family: 'Manrope';");
        header.getChildren().addAll(lblMainTitle, lblSub);

        // --- SECCIONES ---
        root.getChildren().addAll(
                header,
                crearSeccionInformacion(),
                crearSeccionAutores(),
                crearSeccionCategorizacion(),
                crearSeccionRecursos(),
                crearFooter()
        );

        this.setContent(new StackPane(root));
        Animations.slideUpFadeIn(root, 100);
    }

    private VBox crearSeccionInformacion() {
        VBox cont = crearContainerBase();
        HBox tituloSec = crearTituloSeccion("fas-info-circle", "#3F68E4", "Información del Proyecto");

        VBox campos = new VBox(15);

        Label lblT = new Label("Título del Proyecto");
        lblT.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");
        txtTitulo = new TextField();
        txtTitulo.setPromptText("Ej: Implementación de Redes Neuronales para Detección de Plagas en Cultivos de Café");
        txtTitulo.setStyle(RELLENO_INPUT + BORDE_INPUT + TEXTO_PLACEHOLDER + "-fx-padding: 12;");

        Label lblR = new Label("Resumen / Descripción");
        lblR.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");
        txtResumen = new TextArea();
        txtResumen.setPromptText("Describe brevemente los objetivos, metodología y resultados principales del proyecto...");
        txtResumen.setStyle(RELLENO_INPUT + BORDE_INPUT + TEXTO_PLACEHOLDER +
                "-fx-padding: 12; " +
                "-fx-control-inner-background: #F1F3FC; " +
                "-fx-faint-focus-color: transparent; " + // Quita el resplandor azul claro
                "-fx-focus-color: transparent;");       // Quita el borde azul sólido
        txtResumen.setPrefHeight(120);
        txtResumen.setWrapText(true);

        campos.getChildren().addAll(lblT, txtTitulo, lblR, txtResumen);
        cont.getChildren().addAll(tituloSec, campos);
        return cont;
    }

    private VBox crearSeccionAutores() {
        VBox cont = crearContainerBase();

        HBox header = new HBox();
        HBox tituloSec = crearTituloSeccion("fas-user-friends", "#3F68E4", "Autores");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = new Button("Agregar autor");
        // CORRECCIÓN: Icono blanco
        FontIcon iconPlus = new FontIcon("fas-plus");
        iconPlus.setIconColor(Color.WHITE);
        btnAdd.setGraphic(iconPlus);
        btnAdd.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-padding: 8 15; -fx-font-family: 'Manrope';");
        btnAdd.setCursor(Cursor.HAND);
        Animations.attachHoverLift(btnAdd);
        btnAdd.setOnAction(e -> mostrarModalAutor());

        header.getChildren().addAll(tituloSec, spacer, btnAdd);

        contenedorAutores = new FlowPane(10, 10);
        actualizarPildorasAutores();

        cont.getChildren().addAll(header, contenedorAutores);
        return cont;
    }

    private void actualizarPildorasAutores() {
        contenedorAutores.getChildren().clear();
        if(listaAutores.isEmpty()){
            Label empty = new Label("Sin autores asignados");
            empty.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic; -fx-border-color: #CBD5E1; -fx-border-style: dashed; -fx-border-radius: 10; -fx-padding: 10 20; -fx-font-family: 'Manrope';");
            contenedorAutores.getChildren().add(empty);
            return;
        }

        for (Autor a : listaAutores) {
            HBox pildora = new HBox(8);
            pildora.setAlignment(Pos.CENTER_LEFT);
            pildora.setStyle("-fx-background-color: #F1F5F9; -fx-padding: 8 12; -fx-background-radius: 20;");

            Label name = new Label(a.getNombreAutor());
            name.setStyle("-fx-font-family: 'Manrope';");
            Button btnX = new Button();
            btnX.setGraphic(new FontIcon("fas-times"));
            btnX.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-padding: 0;");
            btnX.setCursor(Cursor.HAND);
            btnX.setOnAction(e -> {
                listaAutores.remove(a);
                actualizarPildorasAutores();
            });

            pildora.getChildren().addAll(name, btnX);
            contenedorAutores.getChildren().add(pildora);
        }
    }

    private VBox crearSeccionCategorizacion() {
        VBox cont = crearContainerBase();
        HBox top = new HBox();
        HBox tituloSec = crearTituloSeccion("fas-th-large", "#3F68E4", "Categorización Académica");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnGest = new Button("Gestionar");
        // CORRECCIÓN: Tipo de letra Manrope
        btnGest.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold;");
        btnGest.setCursor(Cursor.HAND);
        Animations.attachHoverLift(btnGest);
        btnGest.setOnAction(e -> MainApp.setView(new Categorizacion()));

        top.getChildren().addAll(tituloSec, spacer, btnGest);

        GridPane grid = new GridPane();
        grid.setHgap(20); grid.setVgap(20);

        cbCategoria = new ComboBox<>(); cbCategoria.setPromptText("Seleccione Categoría");
        cbFacultad = new ComboBox<>(); cbFacultad.setPromptText("Seleccione Facultad");
        cbPrograma = new ComboBox<>(); cbPrograma.setPromptText("Seleccione Programa");
        cbMateria = new ComboBox<>(); cbMateria.setPromptText("Seleccione Materia");
        cbSemestre = new ComboBox<>(); cbSemestre.setPromptText("Seleccione Semestre");

        configCombo(cbCategoria, "Categoría", grid, 0, 0);
        configCombo(cbFacultad, "Facultad", grid, 1, 0);
        configCombo(cbPrograma, "Programa", grid, 0, 1);
        configCombo(cbMateria, "Materia", grid, 1, 1);
        configCombo(cbSemestre, "Semestre", grid, 0, 2);

        // SOLUCIÓN COMBOBOX: Traer nombres reales
        cbCategoria.getItems().setAll(new CategoriaDAO().listar());
        cbCategoria.setConverter(new StringConverter<Categoria>() {
            @Override public String toString(Categoria obj) { return (obj == null) ? "" : obj.getNombreCategoria(); }
            @Override public Categoria fromString(String s) { return null; }
        });

        cbFacultad.getItems().setAll(new FacultadDAO().listar());
        cbFacultad.setConverter(new StringConverter<Facultad>() {
            @Override public String toString(Facultad obj) { return (obj == null) ? "" : obj.getNombreFacultad(); }
            @Override public Facultad fromString(String s) { return null; }
        });

        cbPrograma.getItems().setAll(new ProgramaDAO().listar());
        cbPrograma.setConverter(new StringConverter<Programa>() {
            @Override public String toString(Programa obj) { return (obj == null) ? "" : obj.getNombrePrograma(); }
            @Override public Programa fromString(String s) { return null; }
        });

        cbMateria.getItems().setAll(new MateriaDAO().listar());
        cbMateria.setConverter(new StringConverter<Materia>() {
            @Override public String toString(Materia obj) { return (obj == null) ? "" : obj.getNombreMateria(); }
            @Override public Materia fromString(String s) { return null; }
        });

        cbSemestre.getItems().setAll(new SemestreDAO().listar());
        cbSemestre.setConverter(new StringConverter<Semestre>() {
            @Override public String toString(Semestre obj) { return (obj == null) ? "" : obj.getAnio() + " - " + obj.getPeriodo(); }
            @Override public Semestre fromString(String s) { return null; }
        });

        cont.getChildren().addAll(top, grid);
        return cont;
    }

    private VBox crearSeccionRecursos() {
        VBox cont = crearContainerBase();
        HBox tituloSec = crearTituloSeccion("fas-folder-open", "#3F68E4", "Recursos del Proyecto");

        HBox multimedia = new HBox(30);

        // --- PORTADA (RESTAURADO) ---
        VBox colPortada = new VBox(10);
        Label lblP = new Label("Portada del Proyecto");
        lblP.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");

        VBox previsualizacion = new VBox(10);
        previsualizacion.setAlignment(Pos.CENTER);
        previsualizacion.setPrefSize(220, 180);
        previsualizacion.setStyle("-fx-background-color: #94A3B8; -fx-background-radius: 15;");

        FontIcon iconImg = new FontIcon("fas-image");
        iconImg.setIconSize(40); iconImg.setIconColor(Color.WHITE);
        Label lblClick = new Label("Clic para cambiar imagen");
        lblClick.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Manrope';");
        Label lblInfo = new Label("JPG o PNG (máx. 2MB)");
        lblInfo.setStyle("-fx-text-fill: #334155; -fx-font-size: 11px; -fx-font-family: 'Manrope';");

        previsualizacion.getChildren().addAll(iconImg, lblClick, lblInfo);
        colPortada.getChildren().addAll(lblP, previsualizacion);

        // --- ARCHIVOS (RESTAURADO) ---
        VBox colArchivos = new VBox(10);
        HBox.setHgrow(colArchivos, Priority.ALWAYS);
        Label lblA = new Label("Archivos (PDF, DOCX, ZIP)");
        lblA.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");

        VBox dropZone = new VBox(10);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.setPrefHeight(180);
        dropZone.setStyle("-fx-background-color: #F1F3FC; " +
                "-fx-background-radius: 15; " +    // <--- ESTO REDONDEA EL FONDO
                "-fx-border-color: #C1C6D5; " +
                "-fx-border-style: dashed; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 15;");         // <--- ESTO REDONDEA EL BORDE

        FontIcon iconCloud = new FontIcon("fas-cloud-upload-alt");
        iconCloud.setIconSize(30); iconCloud.setIconColor(Color.web("#3B82F6"));
        Label lblDropT = new Label("Arrastra y suelta tus archivos aquí");
        lblDropT.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");
        Label lblDropS = new Label("o haz clic para explorar en tu equipo");
        lblDropS.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px; -fx-font-family: 'Manrope';");

        dropZone.getChildren().addAll(iconCloud, lblDropT, lblDropS);
        colArchivos.getChildren().addAll(lblA, dropZone);

        multimedia.getChildren().addAll(colPortada, colArchivos);

        // --- LINK EXTERNO (RESTAURADO) ---
        VBox linkBox = new VBox(10);
        linkBox.setPadding(new Insets(20, 0, 0, 0));
        Separator sep = new Separator();

        Label lblLinkTitle = new Label("Link del proyecto");
        lblLinkTitle.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");

        HBox linkInputCont = new HBox(10);
        linkInputCont.setAlignment(Pos.CENTER_LEFT);
        linkInputCont.setPadding(new Insets(0, 15, 0, 15));
        linkInputCont.setStyle(RELLENO_INPUT + BORDE_INPUT);

        FontIcon linkIcon = new FontIcon("fas-link");
        linkIcon.setIconColor(Color.web("#64748B"));
        txtLink = new TextField();
        txtLink.setPromptText("https://github.com/usuario/proyecto");
        txtLink.setStyle("-fx-background-color: transparent; -fx-padding: 12; -fx-font-family: 'Manrope';");
        HBox.setHgrow(txtLink, Priority.ALWAYS);
        linkInputCont.getChildren().addAll(linkIcon, txtLink);

        Label lblLinkDesc = new Label("Si tu proyecto está alojado en una plataforma externa, pega aquí el enlace. Puedes incluir repositorios de GitHub, despliegues en vivo o carpetas compartidas.");
        lblLinkDesc.setWrapText(true);
        lblLinkDesc.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px; -fx-font-family: 'Manrope';");

        linkBox.getChildren().addAll(sep, lblLinkTitle, linkInputCont, lblLinkDesc);

        cont.getChildren().addAll(tituloSec, multimedia, linkBox);
        return cont;
    }

    private HBox crearFooter() {
        HBox h = new HBox(20);
        h.setAlignment(Pos.CENTER_RIGHT);
        h.setPadding(new Insets(20, 0, 40, 0));

        Button btnCancel = new Button("Cancelar");
        btnCancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748B; -fx-font-weight: bold; -fx-font-family: 'Manrope';");
        btnCancel.setCursor(Cursor.HAND);
        btnCancel.setPrefHeight(48);
        btnCancel.setPrefWidth(120);
        Animations.attachHoverLift(btnCancel);
        btnCancel.setOnAction(e -> MainApp.back());

        Button btnUpload = new Button("Subir Proyecto");
        // CORRECCIÓN: Icono cohete blanco
        FontIcon iconRocket = new FontIcon("fas-rocket");
        iconRocket.setIconColor(Color.WHITE);
        btnUpload.setGraphic(iconRocket);
        btnUpload.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-background-radius: 30; -fx-font-weight: bold; -fx-font-family: 'Manrope'; -fx-font-size: 15px;");
        btnUpload.setCursor(Cursor.HAND);
        btnUpload.setPrefHeight(50);
        btnUpload.setPrefWidth(220);
        Animations.attachHoverLift(btnUpload);
        btnUpload.setOnAction(e -> guardarProyecto());

        h.getChildren().addAll(btnCancel, btnUpload);
        return h;
    }

    // --- HELPERS ---

    private VBox crearContainerBase() {
        VBox v = new VBox(25);
        v.setPadding(new Insets(30));
        v.setStyle(ESTILO_CONTAINER);
        return v;
    }

    private HBox crearTituloSeccion(String iconCode, String color, String texto) {
        HBox h = new HBox(12);
        h.setAlignment(Pos.CENTER_LEFT);
        FontIcon icon = new FontIcon(iconCode);
        icon.setIconSize(20);
        icon.setIconColor(Color.web(color));
        Label l = new Label(texto);
        l.setStyle(TITULO_SEC + "-fx-font-family: 'Manrope';");
        h.getChildren().addAll(icon, l);
        return h;
    }

    private void configCombo(ComboBox<?> cb, String label, GridPane grid, int col, int row) {
        VBox v = new VBox(8);
        Label l = new Label(label);
        l.setStyle("-fx-font-weight: bold; -fx-font-family: 'Manrope';");
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setStyle(RELLENO_INPUT + BORDE_INPUT + "-fx-padding: 5; -fx-font-family: 'Manrope';");
        v.getChildren().addAll(l, cb);
        grid.add(v, col, row);
        GridPane.setHgrow(v, Priority.ALWAYS);
    }

    private void mostrarModalAutor() {
        Dialog<Autor> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Autor");
        ButtonType btnOk = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10); g.setVgap(10); g.setPadding(new Insets(20));
        TextField n = new TextField(); n.setPromptText("Nombre y Apellido");
        TextField c = new TextField(); c.setPromptText("correo@ejemplo.com");
        g.add(new Label("Nombre:"), 0, 0); g.add(n, 1, 0);
        g.add(new Label("Correo:"), 0, 1); g.add(c, 1, 1);

        dialog.getDialogPane().setContent(g);
        dialog.setResultConverter(b -> b == btnOk ? new Autor(0, n.getText(), c.getText()) : null);
        dialog.showAndWait().ifPresent(a -> {
            listaAutores.add(a);
            actualizarPildorasAutores();
        });
    }

    private void guardarProyecto() {
        Proyecto p = new Proyecto();
        p.setTitulo(txtTitulo.getText());
        p.setResumen(txtResumen.getText());
        p.setArchivoURL(txtLink.getText());
        p.setCategoria(cbCategoria.getValue());
        p.setFacultad(cbFacultad.getValue());
        p.setPrograma(cbPrograma.getValue());
        p.setMateria(cbMateria.getValue());
        p.setSemestre(cbSemestre.getValue());
        p.setListaAutores(new ArrayList<>(listaAutores));
        p.setFechaSubida(new java.util.Date());
        p.setPortadaURL("/galeria/images/p1.jpg");

        new ProyectoDAO().insertarProyecto(p);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proyecto registrado con éxito.");
        alert.showAndWait();
        MainApp.back();
    }
}