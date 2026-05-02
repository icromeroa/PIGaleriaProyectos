package galeria.components.interfaz;

import galeria.model.Proyecto;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class CardProyectoCatalogo extends VBox {

    private static final Background[] FONDOS_SOLIDOS = {
            crearFondo("#1e293b", "#334155"),
            crearFondo("#1e1b4b", "#4c1d95"),
            crearFondo("#0f172a", "#1e3a5f"),
            crearFondo("#f97316", "#ef4444"),
            crearFondo("#0d9488", "#0891b2"),
            crearFondo("#6366f1", "#8b5cf6"),
            crearFondo("#be185d", "#9d174d")
    };

    public CardProyectoCatalogo(Proyecto p, int indice, Runnable alHacerClic) {
        this.setSpacing(0);

        // --- SOMBREADO SUAVE (Garantizado) ---
        // Color negro con muy baja opacidad (0.08), radio de 15 y desplazamiento en Y
        DropShadow sombraSuave = new DropShadow();
        sombraSuave.setRadius(15);
        sombraSuave.setOffsetX(0);
        sombraSuave.setOffsetY(4);
        sombraSuave.setColor(Color.rgb(0, 0, 0, 0.8));

        this.setEffect(sombraSuave);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 16;");
        this.setCursor(Cursor.HAND);

        // Clip para redondear bordes
        Rectangle clip = new Rectangle();
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);

        // ── PORTADA ──
        Region portada = new Region();
        portada.setPrefHeight(160);
        portada.setMinHeight(160);
        portada.setBackground(FONDOS_SOLIDOS[indice % FONDOS_SOLIDOS.length]);

        // ── INFO ──
        VBox info = new VBox(12);
        info.setPadding(new Insets(16));
        info.setAlignment(Pos.TOP_LEFT);

        Label lblTitulo = new Label(p.getTitulo() != null ? p.getTitulo() : "Proyecto UniRepo");
        lblTitulo.setWrapText(true);
        lblTitulo.setMinHeight(45);
        lblTitulo.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 15px; -fx-text-fill: #1f2937;");

        HBox autorRow = crearFilaAutor(p);
        HBox metaRow = new HBox(15);
        metaRow.getChildren().addAll(
                crearMeta("fas-eye", formatear(p.getCantidadVistas())),
                crearMeta("fas-download", formatear(p.getCantidadGuardados()))
        );

        info.getChildren().addAll(lblTitulo, autorRow, metaRow);
        this.getChildren().addAll(portada, info);

        // Animaciones de hover
        Animations.attachHoverLift(this);
        this.setOnMouseClicked(e -> alHacerClic.run());
    }

    private static Background crearFondo(String color1, String color2) {
        LinearGradient gradiente = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(color1)),
                new Stop(1, Color.web(color2))
        );
        return new Background(new BackgroundFill(gradiente, CornerRadii.EMPTY, Insets.EMPTY));
    }

    private HBox crearFilaAutor(Proyecto p) {
        String nombre = (p.getListaAutores() != null && !p.getListaAutores().isEmpty())
                ? p.getListaAutores().get(0).getNombreAutor()
                : "Autor USB";

        Circle avatarCircle = new Circle(12, Color.web("#F1F5F9"));
        Label inicial = new Label(String.valueOf(nombre.charAt(0)).toUpperCase());
        inicial.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 10px; -fx-text-fill: #3F68E4;");

        StackPane avatar = new StackPane(avatarCircle, inicial);
        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 12px; -fx-text-fill: #64748b;");

        HBox row = new HBox(8, avatar, lblNombre);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox crearMeta(String icono, String valor) {
        FontIcon icon = new FontIcon(icono);
        icon.setIconSize(12);
        icon.setIconColor(Color.web("#94a3b8"));
        Label lbl = new Label(valor);
        lbl.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        return new HBox(5, icon, lbl);
    }

    private String formatear(int n) {
        if (n >= 1000) return String.format("%.1fk", n / 1000.0);
        return String.valueOf(n);
    }
}