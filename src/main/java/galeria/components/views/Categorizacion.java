package galeria.components.views;

import galeria.dao.*;
import galeria.model.*;
import galeria.util.Alertas;
import galeria.util.Animations;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Collections;

public class Categorizacion extends ScrollPane {

    private final String COLOR_GRIS_TEXTO = "#64748B";
    private final String COLOR_GRIS_ICONOS = "#94A3B8";
    private final String COLOR_AZUL = "#3F68E4";

    private TableView<ItemEstructura> tabla;
    private ObservableList<ItemEstructura> masterData = FXCollections.observableArrayList();
    private Label lblTituloTabla;
    private Button btnNuevoAtributo;
    private String filtroActual = "Todos";
    private final java.util.List<Button> botonesFiltro = new java.util.ArrayList<>();

    public Categorizacion() {
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: white; -fx-background: white;");

        VBox root = new VBox(35);
        root.setPadding(new Insets(50, 100, 50, 100));
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(crearHeader(), crearMenuFiltros(), crearSeccionTabla());
        this.setContent(new StackPane(root));

        recargarDatosCompletos();

        // Carga del CSS externo
        try {
            tabla.getStylesheets().add(getClass().getResource("/galeria/css/tabla_custom.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("No se pudo cargar tabla_custom.css");
        }

        tabla.setStyle("-fx-background-color: white; -fx-no-border: true; -fx-table-cell-border-color: transparent;");
    }

    private HBox crearHeader() {
        // Contenedor de textos (Título y Subtítulo)
        VBox textos = new VBox(5);
        Label titulo = new Label("Gestión de Estructura");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");

        Label sub = new Label("Administra las jerarquías académicas y organizativas de UniRepo.");
        sub.setStyle("-fx-text-fill: #414753; -fx-font-size: 16px; -fx-font-family: 'Manrope';");

        textos.getChildren().addAll(titulo, sub);

        // Espaciador elástico para empujar el botón a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Configuración del botón "Nuevo Atributo"
        btnNuevoAtributo = new Button("Nuevo Atributo");

        // Configuración del Icono (Blanco y con espacio)
        FontIcon plusIcon = new FontIcon("fas-plus");
        plusIcon.setIconColor(Color.WHITE); // Icono blanco
        btnNuevoAtributo.setGraphic(plusIcon);
        btnNuevoAtributo.setGraphicTextGap(10); // Espacio entre icono y texto

        // Estilos visuales del botón
        btnNuevoAtributo.setStyle(
                "-fx-background-color: " + COLOR_AZUL + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 12 25; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: 'Manrope';"
        );

        btnNuevoAtributo.setCursor(Cursor.HAND);
        btnNuevoAtributo.setOnAction(e -> manejarNuevoAtributo());

        // Animación de hover
        Animations.attachHoverLift(btnNuevoAtributo);

        // Contenedor principal del Header
        HBox hb = new HBox(textos, spacer, btnNuevoAtributo);
        hb.setAlignment(Pos.CENTER_LEFT);

        return hb;
    }

    private HBox crearMenuFiltros() {
        HBox hb = new HBox(15);
        hb.setAlignment(Pos.CENTER_LEFT);
        botonesFiltro.clear();

        hb.getChildren().addAll(
                crearBotonFiltro("Categorías", "fas-th-large"),
                crearBotonFiltro("Facultades", "fas-university"),
                crearBotonFiltro("Programas", "fas-graduation-cap"),
                crearBotonFiltro("Materias", "fas-book"),
                crearBotonFiltro("Semestres", "fas-calendar-alt")
        );

        return hb;
    }

    private Button crearBotonFiltro(String texto, String iconCode) {
        Button btn = new Button(texto);
        FontIcon icon = new FontIcon(iconCode);
        btn.setGraphic(icon);
        btn.setCursor(Cursor.HAND);
        botonesFiltro.add(btn);

        btn.setOnAction(e -> {
            filtroActual = texto;
            lblTituloTabla.setText("Listado de " + texto);
            filtrarTabla(texto);
            actualizarEstilosBotones(btn);
        });

        aplicarEstiloInactivo(btn);

        btn.setOnMouseEntered(e -> {
            if (!filtroActual.equals(texto)) aplicarEstiloHover(btn);
        });
        btn.setOnMouseExited(e -> {
            if (!filtroActual.equals(texto)) aplicarEstiloInactivo(btn);
        });

        return btn;
    }

    private void actualizarEstilosBotones(Button botonSeleccionado) {
        for (Button btn : botonesFiltro) {
            if (btn == botonSeleccionado) aplicarEstiloActivo(btn);
            else aplicarEstiloInactivo(btn);
        }
    }

    private void aplicarEstiloActivo(Button btn) {
        btn.setStyle("-fx-background-color: white; -fx-border-color: " + COLOR_AZUL + "; -fx-border-radius: 25; -fx-background-radius: 25; -fx-text-fill: " + COLOR_AZUL + "; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 2;");
        ((FontIcon) btn.getGraphic()).setIconColor(Color.web(COLOR_AZUL));
    }

    private void aplicarEstiloInactivo(Button btn) {
        btn.setStyle("-fx-background-color: white; -fx-border-color: #414753; -fx-border-radius: 25; -fx-background-radius: 25; -fx-text-fill: #414753; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 1;");
        ((FontIcon) btn.getGraphic()).setIconColor(Color.web("#414753"));
    }

    private void aplicarEstiloHover(Button btn) {
        btn.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: " + COLOR_AZUL + "; -fx-border-radius: 25; -fx-background-radius: 25; -fx-text-fill: " + COLOR_AZUL + "; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 1;");
        ((FontIcon) btn.getGraphic()).setIconColor(Color.web(COLOR_AZUL));
    }

    private VBox crearSeccionTabla() {
        VBox container = new VBox(25);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1;");

        lblTituloTabla = new Label("Listado de Categorías");
        lblTituloTabla.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");

        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setFixedCellSize(70);
        tabla.setPrefHeight(500); // Aumentado ligeramente ya que no hay footer

        // --- COLUMNA NOMBRE ---
        TableColumn<ItemEstructura, String> colNombre = new TableColumn<>("NOMBRE DEL ELEMENTO");
        colNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().nombre));
        colNombre.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    HBox box = new HBox(15);
                    box.setAlignment(Pos.CENTER_LEFT);
                    box.setPadding(new Insets(0, 0, 0, 10));
                    ItemEstructura row = getTableView().getItems().get(getIndex());

                    StackPane iconHolder = new StackPane();
                    iconHolder.setPrefSize(40, 40); // Cuadrado perfecto
                    iconHolder.getStyleClass().add("icon-container");

                    FontIcon icon = new FontIcon(row.icono);
                    icon.setIconColor(Color.web(COLOR_AZUL));
                    iconHolder.getChildren().add(icon);

                    Label lbl = new Label(item);
                    lbl.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 15px; -fx-text-fill: #0F172A;");
                    box.getChildren().addAll(iconHolder, lbl);
                    setGraphic(box);
                }
            }
        });

        // --- COLUMNA TIPO ---
        TableColumn<ItemEstructura, String> colTipo = new TableColumn<>("TIPO");
        colTipo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().tipo));
        colTipo.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else {
                    setText(item);
                    setStyle("-fx-font-family: 'Manrope Light'; -fx-text-fill: #64748B; -fx-font-size: 14px;");
                }
            }
        });

        // --- COLUMNA ESTADO ---
        TableColumn<ItemEstructura, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Label badge = new Label("ACTIVO");
                    badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-padding: 5 15; -fx-background-radius: 15; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Manrope';");
                    setGraphic(badge);
                }
            }
        });

        // --- COLUMNA ACCIONES ---
        TableColumn<ItemEstructura, String> colAcciones = new TableColumn<>("ACCIONES");
        colAcciones.setCellFactory(column -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            {
                btnEdit.setGraphic(new FontIcon("fas-pencil-alt"));
                btnDelete.setGraphic(new FontIcon("fas-trash-alt"));
                ((FontIcon)btnEdit.getGraphic()).setIconColor(Color.web(COLOR_GRIS_ICONOS));
                ((FontIcon)btnDelete.getGraphic()).setIconColor(Color.web(COLOR_GRIS_ICONOS));

                btnEdit.setStyle("-fx-background-color: transparent;");
                btnDelete.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);
                btnDelete.setCursor(Cursor.HAND);

                btnEdit.setOnAction(e -> manejarEdicion(getTableView().getItems().get(getIndex())));
                btnDelete.setOnAction(e -> manejarEliminacion(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    HBox hb = new HBox(15, btnEdit, btnDelete);
                    hb.setAlignment(Pos.CENTER);
                    setGraphic(hb);
                }
            }
        });

        for (TableColumn<?, ?> col : new TableColumn[] {colNombre, colTipo, colEstado, colAcciones}) {
            Label lblHeader = new Label(col.getText());
            lblHeader.setStyle("-fx-text-fill: " + COLOR_GRIS_TEXTO + "; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-font-size: 12px;");
            col.setGraphic(lblHeader);
            col.setText("");
        }

        tabla.getColumns().addAll(colNombre, colTipo, colEstado, colAcciones);

        // Ahora solo agregamos el título y la tabla al contenedor
        container.getChildren().addAll(lblTituloTabla, tabla);
        return container;
    }

    private void recargarDatosCompletos() {
        masterData.clear();
        new CategoriaDAO().listar().forEach(c -> masterData.add(new ItemEstructura(c.getIdCategoria(), c.getNombreCategoria(), "Categoría", "fas-layer-group", c)));
        new FacultadDAO().listar().forEach(f -> masterData.add(new ItemEstructura(f.getIdFacultad(), f.getNombreFacultad(), "Facultad", "fas-university", f)));
        new MateriaDAO().listar().forEach(m -> masterData.add(new ItemEstructura(m.getIdMateria(), m.getNombreMateria(), "Materia", "fas-book", m)));
        new ProgramaDAO().listar().forEach(p -> masterData.add(new ItemEstructura(p.getIdPrograma(), p.getNombrePrograma(), "Programa", "fas-graduation-cap", p)));
        new SemestreDAO().listar().forEach(s -> masterData.add(new ItemEstructura(s.getIdSemestre(), s.getAnio() + " - " + s.getPeriodo(), "Semestre", "fas-calendar-alt", s)));
        Collections.shuffle(masterData);
        tabla.setItems(masterData);
    }

    private void filtrarTabla(String tipo) {
        String singular;
        // Manejo de plurales irregulares
        if (tipo.equalsIgnoreCase("Categorías")) {
            singular = "Categoría";
        } else if (tipo.equalsIgnoreCase("Facultades")) {
            singular = "Facultad"; // Aquí estaba el error: antes buscaba "Facultade"
        } else {
            singular = tipo.substring(0, tipo.length() - 1);
        }

        ObservableList<ItemEstructura> filtrados = masterData.filtered(i -> i.tipo.equalsIgnoreCase(singular));
        tabla.setItems(filtrados);
        Animations.slideUpFadeIn(tabla, 50);
    }

// --- Métodos de Acción Actualizados ---

    private void manejarEdicion(ItemEstructura item) {
        if (item.tipo.equalsIgnoreCase("Semestre")) {
            manejarEdicionSemestre(item);
            return;
        }

        // Diálogo estándar para nombres simples (Categoría, Facultad, etc.)
        TextInputDialog dialog = new TextInputDialog(item.nombre);
        dialog.setTitle("Editar " + item.tipo);
        dialog.setHeaderText("Actualizar nombre");
        dialog.setContentText("Nuevo nombre:");

        dialog.showAndWait().ifPresent(nuevoNombre -> {
            if (nuevoNombre.trim().isEmpty()) return;

            try {
                switch (item.tipo) {
                    case "Categoría" -> {
                        Categoria c = (Categoria) item.originalObject;
                        c.setNombreCategoria(nuevoNombre);
                        new CategoriaDAO().actualizarCategoria(c);
                    }
                    case "Facultad" -> {
                        Facultad f = (Facultad) item.originalObject;
                        f.setNombreFacultad(nuevoNombre);
                        new FacultadDAO().actualizarFacultad(f);
                    }
                    case "Programa" -> {
                        Programa p = (Programa) item.originalObject;
                        p.setNombrePrograma(nuevoNombre);
                        new ProgramaDAO().actualizarPrograma(p);
                    }
                    case "Materia" -> {
                        Materia m = (Materia) item.originalObject;
                        m.setNombreMateria(nuevoNombre);
                        new MateriaDAO().actualizarMateria(m);
                    }
                }
                recargarYNotificar("Elemento actualizado correctamente");
            } catch (Exception e) {
                Alertas.mostrarMensaje("Error", "No se pudo actualizar", "fas-exclamation-triangle", "#EF4444");
            }
        });
    }

    private void manejarEdicionSemestre(ItemEstructura item) {
        Semestre s = (Semestre) item.originalObject;

        // Crear un diálogo personalizado para Semestre (Año y Periodo)
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Semestre");
        dialog.setHeaderText("Actualice los datos del semestre");

        TextField txtAnio = new TextField(String.valueOf(s.getAnio()));
        ComboBox<Integer> cbPeriodo = new ComboBox<>(FXCollections.observableArrayList(1, 2));
        cbPeriodo.setValue(s.getPeriodo());

        VBox content = new VBox(10, new Label("Año:"), txtAnio, new Label("Periodo:"), cbPeriodo);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    s.setAnio(Integer.parseInt(txtAnio.getText()));
                    s.setPeriodo(cbPeriodo.getValue());
                    new SemestreDAO().actualizarSemestre(s);
                    recargarYNotificar("Semestre actualizado");
                } catch (NumberFormatException e) {
                    Alertas.mostrarMensaje("Error", "El año debe ser un número", "fas-times", "#EF4444");
                }
            }
        });
    }

    private void manejarEliminacion(ItemEstructura item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar este elemento?");
        alert.setContentText(item.tipo + ": " + item.nombre);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    switch (item.tipo) {
                        case "Categoría" -> new CategoriaDAO().eliminarCategoria(item.id);
                        case "Facultad" -> new FacultadDAO().eliminarFacultad(item.id);
                        case "Programa" -> new ProgramaDAO().eliminarPrograma(item.id);
                        case "Materia" -> new MateriaDAO().eliminarMateria(item.id);
                        case "Semestre" -> new SemestreDAO().eliminarSemestre(item.id);
                    }
                    recargarYNotificar("Elemento eliminado");
                } catch (Exception e) {
                    Alertas.mostrarMensaje("Error", "El elemento está siendo usado", "fas-lock", "#EF4444");
                }
            }
        });
    }

    // Método auxiliar para evitar repetir código de refresco
    private void recargarYNotificar(String mensaje) {
        recargarDatosCompletos();
        // Forzar el filtrado actual para que no se pierda la vista en la que estaba el usuario
        if (!filtroActual.equals("Todos")) {
            filtrarTabla(filtroActual);
        }
        Alertas.mostrarMensaje("Éxito", mensaje, "fas-check", "#10B981");
    }

    private void manejarNuevoAtributo() {
        // Si no se ha seleccionado un filtro específico (caso inicial "Todos")
        if (filtroActual.equals("Todos")) {
            Alertas.mostrarMensaje("Selección requerida", "Por favor, elige una categoría en el menú de filtros antes de agregar.", "fas-info-circle", "#3F68E4");
            return;
        }

        if (filtroActual.equalsIgnoreCase("Semestres")) {
            crearNuevoSemestre();
        } else {
            crearNuevoElementoSimple();
        }
    }

    private void crearNuevoElementoSimple() {
        String singular;
        // Aplicamos la misma lógica de corrección de nombres
        if (filtroActual.equalsIgnoreCase("Categorías")) {
            singular = "Categoría";
        } else if (filtroActual.equalsIgnoreCase("Facultades")) {
            singular = "Facultad";
        } else {
            singular = filtroActual.substring(0, filtroActual.length() - 1);
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo " + singular);
        dialog.setHeaderText("Añadir un nuevo registro a " + filtroActual);
        dialog.setContentText("Nombre:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (nombre.trim().isEmpty()) return;

            try {
                switch (filtroActual) {
                    case "Categorías" -> {
                        // Categoria requiere nombre, desc e icono según tu DAO
                        Categoria c = new Categoria(0, nombre, "Sin descripción", "fas-layer-group");
                        new CategoriaDAO().insertarCategoria(c);
                    }
                    case "Facultades" -> {
                        Facultad f = new Facultad(0, nombre);
                        new FacultadDAO().insertarFacultad(f);
                    }
                    case "Programas" -> {
                        Programa p = new Programa(0, nombre);
                        new ProgramaDAO().insertarPrograma(p);
                    }
                    case "Materias" -> {
                        Materia m = new Materia(0, nombre);
                        new MateriaDAO().insertarMateria(m);
                    }
                }
                recargarYNotificar(singular + " agregado con éxito");
            } catch (Exception e) {
                Alertas.mostrarMensaje("Error", "No se pudo guardar en la base de datos", "fas-exclamation-triangle", "#EF4444");
            }
        });
    }

    private void crearNuevoSemestre() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Semestre");
        dialog.setHeaderText("Ingrese los datos del nuevo semestre");

        TextField txtAnio = new TextField();
        txtAnio.setPromptText("Ej: 2024");
        ComboBox<Integer> cbPeriodo = new ComboBox<>(FXCollections.observableArrayList(1, 2));
        cbPeriodo.getSelectionModel().selectFirst();

        VBox content = new VBox(10,
                new Label("Año:"), txtAnio,
                new Label("Periodo:"), cbPeriodo
        );
        content.setPadding(new Insets(20));
        content.setPrefWidth(300);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    int anio = Integer.parseInt(txtAnio.getText());
                    int periodo = cbPeriodo.getValue();

                    Semestre s = new Semestre(0, anio, periodo);
                    new SemestreDAO().insertarSemestre(s);

                    recargarYNotificar("Semestre " + anio + "-" + periodo + " creado");
                } catch (NumberFormatException e) {
                    Alertas.mostrarMensaje("Dato inválido", "El año debe ser un número entero.", "fas-times", "#EF4444");
                } catch (Exception e) {
                    Alertas.mostrarMensaje("Error", "No se pudo guardar el semestre.", "fas-exclamation-triangle", "#EF4444");
                }
            }
        });
    }

    private static class ItemEstructura {
        int id; String nombre, tipo, icono; Object originalObject;
        ItemEstructura(int id, String n, String t, String i, Object o) {
            this.id = id; this.nombre = n; this.tipo = t; this.icono = i; this.originalObject = o;
        }
    }
}