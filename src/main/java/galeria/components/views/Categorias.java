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
    private GridPane gridProyectos;

    public Categorias() {
        this.setStyle("-fx-background-color: white;");

        configurarBarraLateral();
        configurarContenidoPrincipal();

        // Sincronización con el nombre correcto del método en CategoriaDAO: listar()
        List<Categoria> cats = categoriaDAO.listar();
        if(!cats.isEmpty()) {
            cargarProyectosPorCategoria(cats.get(0));
        }
    }

    private void configurarBarraLateral() {
        VBox sidebar = new VBox(30);
        sidebar.setPadding(new Insets(40, 20, 40, 30));
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-border-color: #E2E8F0; -fx-border-width: 0 1 0 0;");

        // Cabecera de la barra lateral
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        iconBox.setPadding(new Insets(8));
        iconBox.setStyle("-fx-background-color: #3F68E4; -fx-background-radius: 8;");
        FontIcon mainIcon = new FontIcon("fas-th-large"); // Icono representativo
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

        // Menú dinámico usando listar()
        menuCategorias = new VBox(8);
        List<Categoria> lista = categoriaDAO.listar();

        for (Categoria cat : lista) {
            menuCategorias.getChildren().add(crearItemMenu(cat));
        }

        sidebar.getChildren().addAll(header, menuCategorias);
        this.setLeft(sidebar);

        // Animación de entrada para la barra lateral
        Animations.slideUpFadeIn(sidebar, 100);
    }

    private HBox crearItemMenu(Categoria cat) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 15, 12, 15));
        item.setCursor(Cursor.HAND);
        item.setStyle("-fx-background-radius: 10;");

        FontIcon icon = new FontIcon(obtenerIconoPorCategoria(cat.getNombreCategoria()));
        icon.setIconSize(16);
        icon.setIconColor(Color.web("#64748B"));

        Label label = new Label(cat.getNombreCategoria());
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

        item.getChildren().addAll(icon, label);

        // Efectos Hover (Gris -> Azul clarito con texto azul oscuro)
        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 10;");
            label.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #1E40AF;");
            icon.setIconColor(Color.web("#3F68E4"));
        });

        item.setOnMouseExited(e -> {
            // Se mantiene el estilo si es la categoría activa (basado en el título actual)
            if (!lblTituloCatalogo.getText().contains(cat.getNombreCategoria())) {
                item.setStyle("-fx-background-color: transparent;");
                label.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #64748B;");
                icon.setIconColor(Color.web("#64748B"));
            }
        });

        item.setOnMouseClicked(e -> cargarProyectosPorCategoria(cat));

        return item;
    }

    private void configurarContenidoPrincipal() {
        contenedorDerecho = new VBox(25);
        contenedorDerecho.setPadding(new Insets(40, 50, 40, 50));
        contenedorDerecho.setStyle("-fx-background-color: #F8FAFC;");

        VBox headerText = new VBox(5);
        lblTituloCatalogo = new Label("Catálogo");
        lblTituloCatalogo.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: #0F172A;");

        Label lblSub = new Label("Explora los proyectos de investigación y diseño de nuestra comunidad.");
        lblSub.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748B;");
        headerText.getChildren().addAll(lblTituloCatalogo, lblSub);

        gridProyectos = new GridPane();
        gridProyectos.setHgap(20);
        gridProyectos.setVgap(20);

        // Configuración de 3 columnas iguales
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            gridProyectos.getColumnConstraints().add(col);
        }

        ScrollPane scroll = new ScrollPane(contenedorDerecho);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        contenedorDerecho.getChildren().addAll(headerText, gridProyectos);
        this.setCenter(scroll);
    }

    private void cargarProyectosPorCategoria(Categoria cat) {
        lblTituloCatalogo.setText("Catálogo de " + cat.getNombreCategoria());
        gridProyectos.getChildren().clear();

        // Filtramos por ID de categoría (Asumiendo que el DAO tiene este orden de parámetros)
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

            // Lógica de Grid según especificaciones (Bloques de 8 proyectos / 5 filas)
            int indexEnBloque = i % 8;
            int offsetFila = (i / 8) * 5;

            switch (indexEnBloque) {
                // Fila 1 y 2 (3 proyectos)
                case 0 -> gridProyectos.add(card, 0, offsetFila, 2, 2);     // 2x2 Grande (Izquierda)
                case 1 -> gridProyectos.add(card, 2, offsetFila, 1, 1);     // 1x1 Arriba (Derecha)
                case 2 -> gridProyectos.add(card, 2, offsetFila + 1, 1, 1); // 1x1 Abajo (Derecha)

                // Fila 3 (2 proyectos)
                case 3 -> gridProyectos.add(card, 0, offsetFila + 2, 1, 1); // 1x1 Izquierda
                case 4 -> gridProyectos.add(card, 1, offsetFila + 2, 2, 1); // 2x1 Derecha

                // Fila 4 y 5 (3 proyectos verticales)
                case 5 -> gridProyectos.add(card, 0, offsetFila + 3, 1, 2); // 1x2 Vertical
                case 6 -> gridProyectos.add(card, 1, offsetFila + 3, 1, 2); // 1x2 Vertical
                case 7 -> gridProyectos.add(card, 2, offsetFila + 3, 1, 2); // 1x2 Vertical
            }

            // Animación individual con retraso escalonado usando slideUpFadeIn
            Animations.slideUpFadeIn(card, (indexEnBloque * 80));
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
        // Ocupa todo el ancho del grid
        gridProyectos.add(empty, 0, 0, 3, 1);
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