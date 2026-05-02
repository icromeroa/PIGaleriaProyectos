package galeria.components.interfaz;

import galeria.dao.ProyectoDAO;
import galeria.model.Proyecto;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class ProyectosDestacados extends VBox {

    // Se agrega el parámetro onExplorar para la navegación
    public ProyectosDestacados(Runnable onExplorar) {
        setSpacing(24);
        setAlignment(Pos.TOP_LEFT);

        // ── Título ─────────────────────────────────────
        HBox titulo = new HBox(10);
        titulo.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(6, Color.web("#3F68E4"));

        Label lblTitulo = new Label("Proyectos Destacados");
        lblTitulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3F68E4;"
        );

        titulo.getChildren().addAll(dot, lblTitulo);

        // ── Datos (máximo 6) ───────────────────────────
        ProyectoDAO dao = new ProyectoDAO();
        List<Proyecto> proyectos = dao.listarTopPorVistas(6);

        if (proyectos.isEmpty()) {
            Label vacio = new Label("No hay proyectos disponibles aún.");
            vacio.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 14px;");
            getChildren().addAll(titulo, vacio);
            return;
        }

        // ── GRID 2x3 PERFECTO ──────────────────────────
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMaxWidth(Double.MAX_VALUE);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.3333);
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        for (int i = 0; i < proyectos.size() && i < 6; i++) {
            VBox card = crearCard(proyectos.get(i));
            int col = i % 3;
            int row = i / 3;
            grid.add(card, col, row);
            Animations.slideUpFadeIn(card, 100L * i);
        }

        // ── Botón ──────────────────────────────────────
        HBox btnRow = new HBox();
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(8, 0, 0, 0));

        Button btnExplorar = new Button("Explorar catálogo de Proyectos");
        estilizarBoton(btnExplorar, false);

        btnExplorar.setOnMouseEntered(e -> estilizarBoton(btnExplorar, true));
        btnExplorar.setOnMouseExited(e -> estilizarBoton(btnExplorar, false));

        // Acción de redirección
        btnExplorar.setOnAction(e -> {
            if (onExplorar != null) onExplorar.run();
        });

        btnRow.getChildren().add(btnExplorar);
        getChildren().addAll(titulo, grid, btnRow);
    }

    private VBox crearCard(Proyecto p) {
        VBox card = new VBox();
        card.setPrefHeight(180);
        card.setMinHeight(180);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setCursor(Cursor.HAND);
        card.setStyle("-fx-background-color: linear-gradient(to bottom, #f97316, #2563eb); -fx-background-radius: 18;");
        aplicarClip(card, 18);

        if (p.getPortadaURL() != null && !p.getPortadaURL().isEmpty()) {
            aplicarImagenFondo(card, p.getPortadaURL());
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox info = new VBox(6);
        info.setPadding(new Insets(14));

        Label badge = new Label("PROYECTO");
        badge.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 3 8; -fx-background-radius: 4;");

        Label titulo = new Label(p.getTitulo());
        titulo.setWrapText(true);
        titulo.setStyle("-fx-font-family: medium ; -fx-font-size: 14px; -fx-text-fill: white;");

        Label resumen = new Label(truncar(p.getResumen(), 60));
        resumen.setWrapText(true);
        resumen.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.7);");

        HBox meta = new HBox(6);
        FontIcon icon = new FontIcon("fas-eye");
        icon.setIconSize(11);
        icon.setIconColor(Color.WHITE);
        Label vistas = new Label(p.getCantidadVistas() + " vistas");
        vistas.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");

        meta.getChildren().addAll(icon, vistas);
        info.getChildren().addAll(badge, titulo, resumen, meta);
        card.getChildren().addAll(spacer, info);
        Animations.attachHoverLift(card);
        return card;
    }

    private void aplicarImagenFondo(VBox card, String rutaRelativa) {
        try {
            var url = getClass().getResource("/" + rutaRelativa);
            if (url != null) {
                Image img = new Image(url.toExternalForm(), true);
                BackgroundImage bgImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true));
                card.setBackground(new Background(bgImg));
            }
        } catch (Exception e) {
            System.out.println("[IMAGEN] No encontrada: " + rutaRelativa);
        }
    }

    private void aplicarClip(VBox card, double radio) {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(radio * 2);
        clip.setArcHeight(radio * 2);
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        card.setClip(clip);
    }

    private void estilizarBoton(Button btn, boolean hover) {
        btn.setStyle("-fx-background-color: " + (hover ? "#2d55c7" : "#3F68E4") + "; -fx-text-fill: white; -fx-font-family: 'Manrope-SemiBold'; -fx-font-size: 14px; -fx-background-radius: 40; -fx-padding: 13 32;");
    }

    private String truncar(String texto, int max) {
        if (texto == null) return "";
        return texto.length() > max ? texto.substring(0, max) + "..." : texto;
    }
}