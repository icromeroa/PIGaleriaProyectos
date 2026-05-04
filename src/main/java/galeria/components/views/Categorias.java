package galeria.components.views;

import galeria.components.interfaz.CardProyecto;
import galeria.dao.CategoriaDAO;
import galeria.dao.ProyectoDAO;
import galeria.model.Categoria;
import galeria.model.Proyecto;
import galeria.util.Animations;
import galeria.util.CardStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class Categorias extends BorderPane {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();

    private VBox menuCategorias;
    private VBox contenedorDerecho;
    private Label lblTituloCatalogo;
    private Label lblSubtituloCatalogo; // Lo guardamos para animarlo
    private GridPane gridProyectos;

    // Guardamos la referencia al ítem seleccionado actualmente
    private HBox itemSeleccionado;

    public Categorias() {
        this.setStyle("-fx-background-color: white;");

        configurarBarraLateral();
        configurarContenidoPrincipal();

        // Precarga de la primera categoría (generalmente Ingeniería si es la primera en DB)
        List<Categoria> cats = categoriaDAO.listar();
        if(!cats.isEmpty()) {
            // Buscamos el primer nodo que sea un HBox en el menú para marcarlo
            HBox primerItem = (HBox) menuCategorias.getChildren().get(0);
            seleccionarCategoriaVisual(primerItem, cats.get(0));
        }
    }

    private void configurarBarraLateral() {
        VBox sidebar = new VBox(30);
        sidebar.setPadding(new Insets(40, 20, 40, 30));
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-border-color: #E2E8F0; -fx-border-width: 0 1 0 0;");

        // Cabecera
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        iconBox.setPadding(new Insets(8));
        iconBox.setStyle("-fx-background-color: #3F68E4; -fx-background-radius: 8;");
        FontIcon mainIcon = new FontIcon("fas-th-large");
        mainIcon.setIconColor(Color.WHITE);
        mainIcon.setIconSize(18);
        iconBox.getChildren().add(mainIcon);

        VBox titleBox = new VBox(2);
        Label lblCategorias = new Label("Categorías");
        lblCategorias.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #0F172A;");
        Label lblFiltros = new Label("Filtros académicos");
        lblFiltros.setStyle("-fx-font-size: 11px; -fx-text-fill: #94A3B8;");
        titleBox.getChildren().addAll(lblCategorias, lblFiltros);
        header.getChildren().addAll(iconBox, titleBox);

        menuCategorias = new VBox(8);
        List<Categoria> lista = categoriaDAO.listar();

        for (Categoria cat : lista) {
            menuCategorias.getChildren().add(crearItemMenu(cat));
        }

        sidebar.getChildren().addAll(header, menuCategorias);
        this.setLeft(sidebar);

        Animations.slideUpFadeIn(sidebar, 100);
    }

    private HBox crearItemMenu(Categoria cat) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 15, 12, 15));
        item.setCursor(Cursor.HAND);
        item.setStyle("-fx-background-radius: 10; -fx-background-color: transparent;");

        FontIcon icon = new FontIcon(obtenerIconoPorCategoria(cat.getNombreCategoria()));
        icon.setIconSize(16);
        icon.setIconColor(Color.web("#64748B"));

        Label label = new Label(cat.getNombreCategoria());
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

        item.getChildren().addAll(icon, label);

        // HOVER
        item.setOnMouseEntered(e -> {
            if (item != itemSeleccionado) {
                item.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 10;");
            }
        });

        item.setOnMouseExited(e -> {
            if (item != itemSeleccionado) {
                item.setStyle("-fx-background-color: transparent;");
            }
        });

        // CLICK: Aquí manejamos la exclusividad
        item.setOnMouseClicked(e -> seleccionarCategoriaVisual(item, cat));

        return item;
    }

    private void seleccionarCategoriaVisual(HBox nuevoItem, Categoria cat) {
        // 1. Limpiar el anterior seleccionado
        if (itemSeleccionado != null) {
            itemSeleccionado.setStyle("-fx-background-color: transparent;");
            ((Label) itemSeleccionado.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #64748B;");
            ((FontIcon) itemSeleccionado.getChildren().get(0)).setIconColor(Color.web("#64748B"));
        }

        // 2. Aplicar estilo "Activo" al nuevo
        itemSeleccionado = nuevoItem;
        itemSeleccionado.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 10;");
        ((Label) itemSeleccionado.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #1E40AF;");
        ((FontIcon) itemSeleccionado.getChildren().get(0)).setIconColor(Color.web("#3F68E4"));

        // 3. Cargar la data
        cargarProyectosPorCategoria(cat);
    }

    private void configurarContenidoPrincipal() {
        contenedorDerecho = new VBox(25);
        contenedorDerecho.setPadding(new Insets(40, 50, 40, 50));
        contenedorDerecho.setStyle("-fx-background-color: #F8FAFC;");

        VBox headerText = new VBox(5);
        lblTituloCatalogo = new Label("Catálogo");
        lblTituloCatalogo.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: #0F172A;");

        lblSubtituloCatalogo = new Label("Explora los proyectos de investigación y diseño de nuestra comunidad.");
        lblSubtituloCatalogo.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748B;");

        headerText.getChildren().addAll(lblTituloCatalogo, lblSubtituloCatalogo);

        gridProyectos = new GridPane();
        gridProyectos.setHgap(20);
        gridProyectos.setVgap(20);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            gridProyectos.getColumnConstraints().add(col);
        }

        ScrollPane scroll = new ScrollPane(contenedorDerecho);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        contenedorDerecho.getChildren().addAll(headerText, gridProyectos);
        this.setCenter(scroll);
    }

    private void cargarProyectosPorCategoria(Categoria cat) {
        lblTituloCatalogo.setText("Catálogo de " + cat.getNombreCategoria());

        // Animaciones de entrada para los títulos cada vez que cambian
        Animations.slideUpFadeIn(lblTituloCatalogo, 0);
        Animations.slideUpFadeIn(lblSubtituloCatalogo, 50);

        gridProyectos.getChildren().clear();
        List<Proyecto> proyectos = proyectoDAO.filtrar(null, null, null, cat.getIdCategoria());

        if (proyectos.isEmpty()) {
            mostrarMensajeVacio();
            return;
        }

        renderizarGridBento(proyectos);
    }

    private void renderizarGridBento(List<Proyecto> lista) {
        for (int i = 0; i < lista.size(); i++) {
            Proyecto p = lista.get(i);
            CardProyecto card = new CardProyecto(p, CardStyle.NORMAL);

            int indexEnBloque = i % 8;
            int offsetFila = (i / 8) * 5;

            switch (indexEnBloque) {
                case 0 -> gridProyectos.add(card, 0, offsetFila, 2, 2);
                case 1 -> gridProyectos.add(card, 2, offsetFila, 1, 1);
                case 2 -> gridProyectos.add(card, 2, offsetFila + 1, 1, 1);
                case 3 -> gridProyectos.add(card, 0, offsetFila + 2, 1, 1);
                case 4 -> gridProyectos.add(card, 1, offsetFila + 2, 2, 1);
                case 5 -> gridProyectos.add(card, 0, offsetFila + 3, 1, 2);
                case 6 -> gridProyectos.add(card, 1, offsetFila + 3, 1, 2);
                case 7 -> gridProyectos.add(card, 2, offsetFila + 3, 1, 2);
            }

            Animations.slideUpFadeIn(card, (indexEnBloque * 80) + 150);
        }
    }

    private void mostrarMensajeVacio() {
        VBox empty = new VBox(20);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(100, 0, 0, 0));

        FontIcon icon = new FontIcon("fas-search");
        icon.setIconSize(60);
        icon.setIconColor(Color.web("#CBD5E1"));

        Label msg = new Label("Por ahora no hay proyectos en esta categoría");
        msg.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #94A3B8;");

        empty.getChildren().addAll(icon, msg);
        gridProyectos.add(empty, 0, 0, 3, 1);
        Animations.slideUpFadeIn(empty, 200);
    }

    private String obtenerIconoPorCategoria(String nombre) {
        String n = nombre.toLowerCase();
        if (n.contains("exactas")) return "fas-atom";
        if (n.contains("ingeniería")) return "fas-robot";
        if (n.contains("artes")) return "fas-paint-brush";
        if (n.contains("sociales")) return "fas-atlas";
        if (n.contains("salud")) return "fas-first-aid";
        return "fas-folder";
    }
}