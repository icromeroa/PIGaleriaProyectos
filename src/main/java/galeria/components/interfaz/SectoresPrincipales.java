package galeria.components.interfaz;

import galeria.dao.CategoriaDAO;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

// --- IMPORTS DE ANIMACIÓN (Los que te faltan) ---
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

// --- IMPORTS DE EFECTOS Y COLORES ---
import javafx.scene.effect.DropShadow;

// --- IMPORTS DE ESTRUCTURA (Ya deberías tenerlos, pero verifica) ---
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;


import java.util.List;
import java.util.Map;

public class SectoresPrincipales extends VBox {

    // Mapa de icono por nombre de categoría
    private static final Map<String, String> ICONOS = Map.of(
            "Ingenieria",         "fas-cogs",
            "Diseño y multimedia","fas-palette",
            "Programacion",       "fas-laptop-code",
            "Investigación",      "fas-flask",
            "Ciencias",           "fas-atom",
            "Arte",               "fas-paint-brush"
    );

    // Colores de fondo del ícono por índice
    private static final String[] ICON_COLORS = {
            "#dce6ff", "#ffe4cc", "#dce6ff", "#ffe4cc"
    };
    private static final String[] ICON_TINT = {
            "#3F68E4", "#F97316", "#3F68E4", "#F97316"
    };

    public SectoresPrincipales() {
        setSpacing(20);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(0));

        // Título de la sección
        HBox titulo = new HBox(10);
        titulo.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(6, Color.web("#F97316"));
        Label lblTitulo = new Label("Sectores Principales");
        lblTitulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #F97316;"
        );
        titulo.getChildren().addAll(dot, lblTitulo);

        // Grid de tarjetas
        HBox grid = new HBox(16);
        grid.setAlignment(Pos.CENTER_LEFT);

        // Carga desde BD
        CategoriaDAO dao = new CategoriaDAO();
        List<Object[]> datos = dao.listarTopCategoriasPorProyectos(4);

        // Si la BD aún no tiene datos suficientes, muestra placeholders
        if (datos.isEmpty()) {
            datos = List.of(
                    new Object[]{1, "Ingenieria",         "proyectos de ingenieria", 0},
                    new Object[]{2, "Diseño y multimedia","proyectos de diseño",     0},
                    new Object[]{3, "Programacion",       "proyectos de código",     0},
                    new Object[]{4, "Investigación",      "proyectos académicos",    0}
            );
        }

        for (int i = 0; i < datos.size(); i++) {
            Object[] fila = datos.get(i);
            String nombre = (String) fila[1];
            int total    = (int)   fila[3];

            VBox card = crearCard(nombre, total, i);
            HBox.setHgrow(card, Priority.ALWAYS);
            grid.getChildren().add(card);

            // Animación de entrada escalonada
            Animations.slideUpFadeIn(card, 200L * i);
        }

        getChildren().addAll(titulo, grid);
    }

    private VBox crearCard(String nombre, int totalProyectos, int indice) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.setAlignment(Pos.TOP_LEFT);
        card.setCursor(Cursor.HAND);
        card.setPickOnBounds(true);

        // --- 1. CONFIGURACIÓN DE COLORES ---
        boolean esAzul = indice % 2 == 0;
        String bgBase = esAzul ? "#eef2ff" : "#fff4ec";
        String iconBgBase = esAzul ? "#dce6ff" : "#ffe4cc";
        String tintBase = esAzul ? "#3F68E4" : "#F97316";
        // Colores Hover (Fuertes)
        Color colorFuerte = Color.web(esAzul ? "#3F68E4" : "#F97316");
        String bgHover = esAzul ? "linear-gradient(to bottom right, #5b7ef7, #3F68E4)"
                : "linear-gradient(to bottom right, #f97316, #f59e0b)";

        card.setStyle("-fx-background-color: " + bgBase + "; -fx-background-radius: 18;");

        // --- 2. ELEMENTOS INTERNOS ---
        StackPane iconBox = new StackPane();
        iconBox.setPrefSize(48, 48);
        iconBox.setMaxSize(48, 48);
        iconBox.setStyle("-fx-background-color: " + iconBgBase + "; -fx-background-radius: 12;");

        FontIcon icono = new FontIcon(ICONOS.getOrDefault(nombre, "fas-folder"));
        icono.setIconSize(22);
        icono.setIconColor(Color.web(tintBase));
        iconBox.getChildren().add(icono);

        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 15px; -fx-text-fill: #1f2937;");

        Label lblCount = new Label(totalProyectos + " Proyectos");
        lblCount.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 13px; -fx-text-fill: #6b7280;");

        card.getChildren().addAll(iconBox, lblNombre, lblCount);

        // --- 3. ANIMACIONES PROFESIONALES (Lógica fluida) ---

        // Efecto de sombra (Shadow)
        DropShadow shadow = new DropShadow(0, Color.color(colorFuerte.getRed(), colorFuerte.getGreen(), colorFuerte.getBlue(), 0));
        card.setEffect(shadow);

        // Transiciones de Escala
        ScaleTransition stIn = new ScaleTransition(Duration.millis(220), card);
        stIn.setToX(1.035); stIn.setToY(1.035);
        stIn.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition stOut = new ScaleTransition(Duration.millis(220), card);
        stOut.setToX(1.0); stOut.setToY(1.0);
        stOut.setInterpolator(Interpolator.EASE_OUT);

        // Timeline para Sombra y posición Y (Subida fluida)
        Timeline hoverIn = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(shadow.radiusProperty(), 25),
                new KeyValue(shadow.offsetYProperty(), 10),
                new KeyValue(shadow.colorProperty(), Color.color(colorFuerte.getRed(), colorFuerte.getGreen(), colorFuerte.getBlue(), 0.3)),
                new KeyValue(card.translateYProperty(), -8) // Sube un poco
        ));

        Timeline hoverOut = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(shadow.radiusProperty(), 0),
                new KeyValue(shadow.offsetYProperty(), 0),
                new KeyValue(shadow.colorProperty(), Color.color(colorFuerte.getRed(), colorFuerte.getGreen(), colorFuerte.getBlue(), 0)),
                new KeyValue(card.translateYProperty(), 0) // Baja
        ));

        // --- 4. ASIGNACIÓN DE EVENTOS ---
        card.setOnMouseEntered(e -> {
            System.out.println(">>> Hover In: " + nombre);
            stOut.stop(); hoverOut.stop();

            // Cambio de colores inmediato (o podrías usar un FillTransition si quieres ser mas pro)
            card.setStyle("-fx-background-color: " + bgHover + "; -fx-background-radius: 18;");
            lblNombre.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 15px; -fx-text-fill: white;");
            lblCount.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.8);");
            icono.setIconColor(Color.WHITE);
            iconBox.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 12;");

            stIn.playFromStart();
            hoverIn.playFromStart();
        });

        card.setOnMouseExited(e -> {
            System.out.println("<<< Hover Out: " + nombre);
            stIn.stop(); hoverIn.stop();

            // Restaurar colores
            card.setStyle("-fx-background-color: " + bgBase + "; -fx-background-radius: 18;");
            lblNombre.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 15px; -fx-text-fill: #1f2937;");
            lblCount.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 13px; -fx-text-fill: #6b7280;");
            icono.setIconColor(Color.web(tintBase));
            iconBox.setStyle("-fx-background-color: " + iconBgBase + "; -fx-background-radius: 12;");

            stOut.playFromStart();
            hoverOut.playFromStart();
        });

        card.setOnMouseClicked(e -> {
            System.out.println("ACTION: Clic detectado en " + nombre);
            // Feedback de clic rápido
            card.setScaleX(0.98);
            card.setScaleY(0.98);
        });

        return card;
    }
    private String cardStyle() {
        return "-fx-background-color: #eef2ff;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;";
    }
}