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

        if(!botonesFiltro.isEmpty()) actualizarEstilosBotones(botonesFiltro.get(0));

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
        String singular = tipo.equalsIgnoreCase("Categorías") ? "Categoría" : tipo.substring(0, tipo.length() - 1);
        ObservableList<ItemEstructura> filtrados = masterData.filtered(i -> i.tipo.equalsIgnoreCase(singular));
        tabla.setItems(filtrados);
        Animations.slideUpFadeIn(tabla, 50);
    }

    private void manejarEdicion(ItemEstructura item) {
        TextInputDialog dialog = new TextInputDialog(item.nombre);
        dialog.setTitle("Editar Elemento");
        dialog.setHeaderText("Actualizar nombre de " + item.tipo);
        dialog.showAndWait().ifPresent(nuevo -> {
            recargarDatosCompletos();
            Alertas.mostrarMensaje("Éxito", "Actualizado", "fas-check", "#10B981");
        });
    }

    private void manejarEliminacion(ItemEstructura item) { /* Lógica de eliminación */ }

    private void manejarNuevoAtributo() { System.out.println("Nuevo atributo para: " + filtroActual); }

    private static class ItemEstructura {
        int id; String nombre, tipo, icono; Object originalObject;
        ItemEstructura(int id, String n, String t, String i, Object o) {
            this.id = id; this.nombre = n; this.tipo = t; this.icono = i; this.originalObject = o;
        }
    }
}