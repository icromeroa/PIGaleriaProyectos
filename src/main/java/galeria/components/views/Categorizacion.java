package galeria.components.views;

import galeria.app.MainApp;
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
import java.util.Optional;

public class Categorizacion extends ScrollPane {

    private final String COLOR_GRIS_TEXTO = "#64748B";
    private final String COLOR_GRIS_ICONOS = "#94A3B8";
    private final String COLOR_AZUL = "#3F68E4";
    private final String BG_HEADER = "#F8FAFC";

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

        // Estilos globales de la tabla para ocultar bordes internos y ajustar el header
        tabla.getStylesheets().add(getClass().getResource("/galeria/css/tabla_custom.css").toExternalForm());
        // Si no tienes el CSS, estos estilos inline ayudan:
        tabla.setStyle("-fx-background-color: white; -fx-no-border: true; -fx-table-cell-border-color: transparent;");
    }

    private HBox crearHeader() {
        VBox textos = new VBox(5);
        Label titulo = new Label("Gestión de Estructura");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");
        Label sub = new Label("Administra las jerarquías académicas y organizativas de UniRepo.");
        sub.setStyle("-fx-text-fill: #414753; -fx-font-size: 16px; -fx-font-family: 'Manrope';");
        textos.getChildren().addAll(titulo, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnNuevoAtributo = new Button("Nuevo Atributo");
        btnNuevoAtributo.setGraphic(new FontIcon("fas-plus"));
        btnNuevoAtributo.setStyle("-fx-background-color: " + COLOR_AZUL + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12 25; -fx-font-weight: bold; -fx-font-family: 'Manrope';");
        btnNuevoAtributo.setCursor(Cursor.HAND);
        btnNuevoAtributo.setOnAction(e -> manejarNuevoAtributo());
        Animations.attachHoverLift(btnNuevoAtributo);

        HBox hb = new HBox(textos, spacer, btnNuevoAtributo);
        hb.setAlignment(Pos.CENTER_LEFT);
        return hb;
    }

    private HBox crearMenuFiltros() {
        HBox hb = new HBox(15);
        hb.setAlignment(Pos.CENTER_LEFT);
        botonesFiltro.clear(); // Limpiar por si se vuelve a renderizar

        hb.getChildren().addAll(
                crearBotonFiltro("Categorías", "fas-th-large"),
                crearBotonFiltro("Facultades", "fas-university"),
                crearBotonFiltro("Programas", "fas-graduation-cap"),
                crearBotonFiltro("Materias", "fas-book"),
                crearBotonFiltro("Semestres", "fas-calendar-alt")
        );

        // Opcional: Seleccionar el primero por defecto visualmente
        if(!botonesFiltro.isEmpty()) actualizarEstilosBotones(botonesFiltro.get(0));

        return hb;
    }

    // 3. Modifica crearBotonFiltro para manejar la exclusividad
    private Button crearBotonFiltro(String texto, String iconCode) {
        Button btn = new Button(texto);
        FontIcon icon = new FontIcon(iconCode);
        btn.setGraphic(icon);
        btn.setCursor(Cursor.HAND);

        // Guardamos el botón en nuestra lista de control
        botonesFiltro.add(btn);

        // Acción al hacer clic
        btn.setOnAction(e -> {
            filtroActual = texto;
            lblTituloTabla.setText("Listado de " + texto);
            filtrarTabla(texto);
            actualizarEstilosBotones(btn); // <-- Aquí ocurre la magia de selección única
        });

        // Aplicar estilo inicial (Inactivo)
        aplicarEstiloInactivo(btn);

        // Hovers (Solo actúan si el botón no es el seleccionado actualmente)
        btn.setOnMouseEntered(e -> {
            if (!filtroActual.equals(texto)) aplicarEstiloHover(btn);
        });
        btn.setOnMouseExited(e -> {
            if (!filtroActual.equals(texto)) aplicarEstiloInactivo(btn);
        });

        return btn;
    }

    // 4. Agrega estos métodos auxiliares para gestionar los estados visuales
    private void actualizarEstilosBotones(Button botonSeleccionado) {
        for (Button btn : botonesFiltro) {
            if (btn == botonSeleccionado) {
                aplicarEstiloActivo(btn);
            } else {
                aplicarEstiloInactivo(btn);
            }
        }
    }

    private void aplicarEstiloActivo(Button btn) {
        btn.setStyle("-fx-background-color: white; -fx-border-color: " + COLOR_AZUL +
                "; -fx-border-radius: 25; -fx-background-radius: 25; -fx-text-fill: " + COLOR_AZUL +
                "; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 2;");
        ((FontIcon) btn.getGraphic()).setIconColor(Color.web(COLOR_AZUL));
    }

    private void aplicarEstiloInactivo(Button btn) {
        btn.setStyle("-fx-background-color: white; -fx-border-color: #414753; -fx-border-radius: 25; " +
                "-fx-background-radius: 25; -fx-text-fill: #414753; -fx-padding: 8 20; " +
                "-fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 1;");
        ((FontIcon) btn.getGraphic()).setIconColor(Color.web("#414753"));
    }

    private void aplicarEstiloHover(Button btn) {
        btn.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: " + COLOR_AZUL +
                "; -fx-border-radius: 25; -fx-background-radius: 25; -fx-text-fill: " + COLOR_AZUL +
                "; -fx-padding: 8 20; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-border-width: 1;");
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
        tabla.setFixedCellSize(70); // Aumenta el alto para el padding moderno
        tabla.setPrefHeight(450);

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

                    // Contenedor del icono con fondo suave (como en la imagen)
                    StackPane iconHolder = new StackPane();
                    iconHolder.setPadding(new Insets(8));
                    iconHolder.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 8;");
                    FontIcon icon = new FontIcon(row.icono);
                    icon.setIconColor(Color.web(COLOR_AZUL));
                    iconHolder.getChildren().add(icon);
                    iconHolder.getStyleClass().add("icon-container");

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
                FontIcon editIcon = new FontIcon("fas-pencil-alt");
                editIcon.setIconColor(Color.web(COLOR_GRIS_ICONOS));
                btnEdit.setGraphic(editIcon);

                FontIcon trashIcon = new FontIcon("fas-trash-alt");
                trashIcon.setIconColor(Color.web(COLOR_GRIS_ICONOS));
                btnDelete.setGraphic(trashIcon);

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

        // Estilos para los Headers de las columnas
        for (TableColumn<?, ?> col : new TableColumn[] {colNombre, colTipo, colEstado, colAcciones}) {
            Label lblHeader = new Label(col.getText());
            lblHeader.setStyle("-fx-text-fill: " + COLOR_GRIS_TEXTO + "; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-font-size: 12px;");
            col.setGraphic(lblHeader);
            col.setText("");
        }

        tabla.getColumns().addAll(colNombre, colTipo, colEstado, colAcciones);

        // Footer con paginación
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 0, 0, 0));

        Label lblMostrando = new Label("Mostrando 4 de 12 elementos");
        lblMostrando.setStyle("-fx-font-family: 'Manrope'; -fx-text-fill: #94A3B8;");
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);

        HBox paginacion = new HBox(8);
        paginacion.setAlignment(Pos.CENTER);
        Button p1 = new Button("1");
        p1.setStyle("-fx-background-color: " + COLOR_AZUL + "; -fx-text-fill: white; -fx-background-radius: 5;");
        Button p2 = new Button("2");
        p2.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-text-fill: #64748B; -fx-background-radius: 5;");
        paginacion.getChildren().addAll(new Button("<"), p1, p2, new Button(">"));

        footer.getChildren().addAll(lblMostrando, s, paginacion);

        container.getChildren().addAll(lblTituloTabla, tabla, footer);
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
            // Lógica de actualización DAO...
            recargarDatosCompletos();
            Alertas.mostrarMensaje("Éxito", "Actualizado", "fas-check", "#10B981");
        });
    }

    private void manejarEliminacion(ItemEstructura item) {
        // Tu lógica de alerta de confirmación...
    }

    private void manejarNuevoAtributo() { System.out.println("Nuevo atributo para: " + filtroActual); }

    private static class ItemEstructura {
        int id; String nombre, tipo, icono; Object originalObject;
        ItemEstructura(int id, String n, String t, String i, Object o) {
            this.id = id; this.nombre = n; this.tipo = t; this.icono = i; this.originalObject = o;
        }
    }
}