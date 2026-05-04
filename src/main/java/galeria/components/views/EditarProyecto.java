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
        // 'proyectoRecibido' es la información que venía del detalle
        this.proyectoOriginal = proyectoRecibido;
        this.autoresEditados = new ArrayList<>(proyectoOriginal.getListaAutores());

        this.getStyleClass().add("scroll-pane");
        this.setFitToWidth(true);

        // Importante: Cargar el CSS
        this.getStylesheets().add(getClass().getResource("/galeria/css/app.css").toExternalForm());

        VBox content = new VBox(35);
        content.setPadding(new Insets(40, 80, 40, 80));
        content.setMaxWidth(1000);
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 30, 0, 0, 15);");

        // --- CONSTRUCCIÓN DE LA VISTA ---
        content.getChildren().addAll(
                crearHeader(),
                crearSeccionGeneral(),
                crearSeccionCategorizacion(),
                crearSeccionMultimedia(),
                crearFooter()
        );

        this.setContent(new StackPane(content));
        this.setPadding(new Insets(20));

        // Rellenar ComboBoxes y seleccionar los valores actuales del proyecto
        cargarDatosYPreseleccionar();

        // Animación de entrada
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
        lblT.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        txtTitulo = new TextField(proyectoOriginal.getTitulo());
        txtTitulo.getStyleClass().add("input-moderno");

        Label lblR = new Label("Resumen");
        lblR.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        txtResumen = new TextArea(proyectoOriginal.getResumen());
        txtResumen.getStyleClass().add("area-moderna");
        txtResumen.setPrefHeight(120);
        txtResumen.setWrapText(true);

        Label lblA = new Label("Autores vinculados");
        lblA.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
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
        l.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        cb.getStyleClass().add("combo-box-moderno");
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setPrefHeight(45);
        box.getChildren().addAll(l, cb);
        grid.add(box, col, row);
        GridPane.setHgrow(box, Priority.ALWAYS);
    }

    private void cargarDatosYPreseleccionar() {
        // Llenar listas
        cbCategoria.getItems().addAll(new CategoriaDAO().listar());
        cbFacultad.getItems().addAll(new FacultadDAO().listar());
        cbPrograma.getItems().addAll(new ProgramaDAO().listar());
        cbMateria.getItems().addAll(new MateriaDAO().listar());
        cbSemestre.getItems().addAll(new SemestreDAO().listar());

        // Preseleccionar lo que ya tiene el proyecto (si no es null)
        if(proyectoOriginal.getCategoria() != null) cbCategoria.setValue(proyectoOriginal.getCategoria());
        if(proyectoOriginal.getFacultad() != null) cbFacultad.setValue(proyectoOriginal.getFacultad());
        if(proyectoOriginal.getPrograma() != null) cbPrograma.setValue(proyectoOriginal.getPrograma());
        if(proyectoOriginal.getMateria() != null) cbMateria.setValue(proyectoOriginal.getMateria());
        if(proyectoOriginal.getSemestre() != null) cbSemestre.setValue(proyectoOriginal.getSemestre());
    }

    private VBox crearSeccionMultimedia() {
        VBox sec = new VBox(20);
        Label header = crearBadgeSeccion("03", "Archivos y Multimedia");

        HBox layout = new HBox(40);

        VBox portBox = new VBox(10);
        Label lblP = new Label("Imagen de Portada");
        lblP.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");

        // Previsualización (Usa portadaURL del modelo)
        ImageView preview = new ImageView();
        try {
            preview.setImage(new Image(proyectoOriginal.getPortadaURL()));
        } catch (Exception e) {
            preview.setImage(new Image(getClass().getResourceAsStream("/galeria/resources/placeholder.png")));
        }
        preview.setFitWidth(180); preview.setFitHeight(180);
        preview.setPreserveRatio(true);
        portBox.getChildren().addAll(lblP, preview);

        VBox fileBox = new VBox(15);
        HBox.setHgrow(fileBox, Priority.ALWAYS);

        VBox dropZone = new VBox(10);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.setPrefHeight(120);
        dropZone.setStyle("-fx-border-color: #CBD5E1; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-radius: 15;");
        dropZone.getChildren().addAll(new FontIcon("fas-cloud-upload-alt"), new Label("Arrastra tus archivos aquí (No habilitado)"));

        // CORRECCIÓN AQUÍ: getArchivoURL() con mayúsculas
        txtEnlaceExterno = new TextField(proyectoOriginal.getArchivoURL());
        txtEnlaceExterno.getStyleClass().add("input-moderno");

        fileBox.getChildren().addAll(new Label("Archivos del Proyecto"), dropZone, new Label("Enlace Externo (Repo/Web)"), txtEnlaceExterno);

        layout.getChildren().addAll(portBox, fileBox);
        sec.getChildren().addAll(header, layout);
        return sec;
    }

    private HBox crearFooter() {
        Button btnDelete = new Button("Eliminar Proyecto");
        btnDelete.setGraphic(new FontIcon("fas-trash-alt"));
        btnDelete.getStyleClass().add("boton-eliminar");
        btnDelete.setOnAction(e -> accionEliminar());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnCancel = new Button("Cancelar");
        btnCancel.getStyleClass().add("boton-cancelar");
        btnCancel.setOnAction(e -> MainApp.setView(new DetalleProyecto(proyectoOriginal, null)));

        Button btnSave = new Button("Guardar Cambios");
        btnSave.getStyleClass().add("boton-guardar");
        btnSave.setPrefWidth(180);
        btnSave.setPrefHeight(45);
        btnSave.setOnAction(e -> accionGuardar());

        HBox h = new HBox(25, btnDelete, spacer, btnCancel, btnSave);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(20, 0, 0, 0));
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