package galeria.components.views;

import galeria.components.interfaz.CardProyecto;
import galeria.dao.*;
import galeria.model.*;
import galeria.util.Animations;
import galeria.util.CardStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalogo extends ScrollPane {
    private int limiteActual = 8;
    private static final int INCREMENTO = 8;
    private List<Proyecto> listaFiltrada = new ArrayList<>();

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final ProgramaDAO programaDAO = new ProgramaDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final SemestreDAO semestreDAO = new SemestreDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private Integer filtroPrograma = null, filtroMateria = null, filtroSemestre = null, filtroCategoria = null;
    private String textoBusqueda = null;

    private final TilePane gridCards = new TilePane();
    private Button btnVerMas;

    public Catalogo() {
        VBox content = new VBox(32);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 60, 60, 60));
        content.setStyle("-fx-background-color: #ffffff;");

        // ── Encabezado (Manteniendo tu estilo original) ─────────────────
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

        Label lblTitulo = new Label("Explorar el Catálogo");
        lblTitulo.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 42px; -fx-text-fill: #001b48;");

        Label lblSubtitulo = new Label("Descubre investigaciones, conjuntos de datos y proyectos de gran impacto de\n" +
                "la comunidad académica de la Universidad de San Buenaventura.");
        lblSubtitulo.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 16px; -fx-text-fill: #4b5563; -fx-text-alignment: center;");
        lblSubtitulo.setWrapText(true);

        header.getChildren().addAll(lblTitulo, lblSubtitulo);

        // ── Buscador ───────────────────────────
        HBox searchBox = new HBox(12);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(800);
        searchBox.setStyle("-fx-background-color: #F2F4FF; -fx-background-radius: 20; -fx-padding: 15 20;");

        TextField campoSearch = new TextField();
        campoSearch.setPromptText("Busca proyectos en la USB...");
        campoSearch.setStyle("-fx-background-color: transparent; -fx-font-family: 'Manrope'; -fx-font-size: 15px;");
        HBox.setHgrow(campoSearch, Priority.ALWAYS);

        FontIcon searchIcon = new FontIcon("fas-search");
        searchIcon.setIconColor(javafx.scene.paint.Color.web("#3F68E4"));
        searchBox.getChildren().addAll(searchIcon, campoSearch);

        // ── Filtros ──────────────────────────────
        HBox filtrosRow = new HBox(15);
        filtrosRow.setAlignment(Pos.CENTER);

        Button btnProg = crearBotonFiltro("Programa", programaDAO.listar().stream().map(p -> new FilterItem(p.getIdPrograma(), p.getNombrePrograma())).collect(Collectors.toList()), "programa");
        Button btnMat = crearBotonFiltro("Materia", materiaDAO.listar().stream().map(m -> new FilterItem(m.getIdMateria(), m.getNombreMateria())).collect(Collectors.toList()), "materia");
        Button btnSem = crearBotonFiltro("Semestre", semestreDAO.listar().stream().map(s -> new FilterItem(s.getIdSemestre(), s.getAnio() + "-" + s.getPeriodo())).collect(Collectors.toList()), "semestre");
        Button btnCat = crearBotonFiltro("Categoría", categoriaDAO.listar().stream().map(c -> new FilterItem(c.getIdCategoria(), c.getNombreCategoria())).collect(Collectors.toList()), "categoria");

        filtrosRow.getChildren().addAll(btnProg, btnMat, btnSem, btnCat);

        // ── Grid Configurado para CardProyecto ──────────────────────────
        gridCards.setHgap(25);
        gridCards.setVgap(30);
        gridCards.setPrefColumns(4); // 4 Columnas
        gridCards.setAlignment(Pos.TOP_CENTER);

        // Ajuste dinámico del ancho de las tiles para que CardProyecto quepa bien
        gridCards.prefTileWidthProperty().bind(gridCards.widthProperty().subtract(150).divide(4));

        // ── Botón Ver Más ─────────────────────────────────
        btnVerMas = new Button("Ver Más Proyectos  ↓");
        btnVerMas.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; -fx-font-family: 'Manrope Bold'; -fx-background-radius: 40; -fx-padding: 12 35; -fx-cursor: hand;");
        btnVerMas.setOnAction(e -> {
            limiteActual += INCREMENTO;
            renderizarCards();
        });

        // Listeners y Animaciones
        campoSearch.textProperty().addListener((obs, old, val) -> {
            textoBusqueda = val;
            limiteActual = 8;
            actualizarDatos();
        });

        Animations.slideUpFadeIn(lblTitulo, 100);
        Animations.slideUpFadeIn(lblSubtitulo, 300);
        Animations.slideUpFadeIn(searchBox, 500);

        content.getChildren().addAll(header, searchBox, filtrosRow, gridCards, btnVerMas);

        this.setContent(content);
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: white;");

        actualizarDatos();
    }

    private void actualizarDatos() {
        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            listaFiltrada = proyectoDAO.buscarPorTitulo(textoBusqueda);
        } else if (filtroPrograma != null || filtroMateria != null || filtroSemestre != null || filtroCategoria != null) {
            listaFiltrada = proyectoDAO.filtrar(filtroPrograma, filtroMateria, filtroSemestre, filtroCategoria);
        } else {
            listaFiltrada = proyectoDAO.listarTodosConAutor();
        }
        renderizarCards();
    }

    /**
     * MÉTODO CORREGIDO: Ahora usa CardProyecto
     */
    private void renderizarCards() {
        gridCards.getChildren().clear();
        int fin = Math.min(limiteActual, listaFiltrada.size());

        for (int i = 0; i < fin; i++) {
            Proyecto p = listaFiltrada.get(i);

            // Instanciamos el nuevo CardProyecto reutilizable
            // Nota: CardStyle es un enum que definiste, aquí usamos uno estándar (ej: LIGHT o SOLID)
            CardProyecto card = new CardProyecto(p, CardStyle.NORMAL);

            // Forzamos un alto mínimo para que el grid se vea uniforme
            card.setMinHeight(280);

            gridCards.getChildren().add(card);

            // Animación de entrada
            Animations.revealProjectCard(card, i * 50L);
        }
        btnVerMas.setVisible(limiteActual < listaFiltrada.size());
    }

    private Button crearBotonFiltro(String tituloOriginal, List<FilterItem> items, String tipo) {
        Button btn = new Button(tituloOriginal);
        btn.setStyle(estiloFiltro(false));

        ContextMenu menu = new ContextMenu();
        MenuItem limpiar = new MenuItem("Todos");
        limpiar.setOnAction(e -> {
            setFiltro(tipo, null);
            btn.setText(tituloOriginal);
            btn.setStyle(estiloFiltro(false));
            actualizarDatos();
        });
        menu.getItems().add(limpiar);

        for (FilterItem item : items) {
            MenuItem mi = new MenuItem(item.label);
            mi.setOnAction(e -> {
                setFiltro(tipo, item.id);
                btn.setText(item.label);
                btn.setStyle(estiloFiltro(true));
                actualizarDatos();
            });
            menu.getItems().add(mi);
        }

        btn.setOnAction(e -> menu.show(btn, javafx.geometry.Side.BOTTOM, 0, 5));
        return btn;
    }

    private void setFiltro(String tipo, Integer id) {
        switch (tipo) {
            case "programa" -> filtroPrograma = id;
            case "materia" -> filtroMateria = id;
            case "semestre" -> filtroSemestre = id;
            case "categoria" -> filtroCategoria = id;
        }
        limiteActual = 8;
    }

    private String estiloFiltro(boolean activo) {
        return String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; " +
                        "-fx-border-radius: 40; -fx-background-radius: 40; -fx-padding: 8 20; -fx-cursor: hand; -fx-font-family: 'Manrope SemiBold';",
                activo ? "#F97316" : "white", activo ? "white" : "#4b5563", activo ? "#F97316" : "#e5e7eb");
    }

    private record FilterItem(Integer id, String label) {}
}