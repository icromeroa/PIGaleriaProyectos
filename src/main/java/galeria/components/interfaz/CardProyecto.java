package galeria.components.interfaz;

import galeria.model.Proyecto;
import galeria.util.Animations;
import galeria.util.CardStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class CardProyecto extends VBox {
    private final Proyecto proyecto;

    public CardProyecto(Proyecto p, CardStyle estilo) {
        this.proyecto = p;

        // Reducimos el padding de 20 a 15 para que la card se sienta "más pequeña"
        // sin forzar medidas fijas que rompan el layout
        this.setPadding(new Insets(15));
        this.setSpacing(6);
        this.setAlignment(Pos.BOTTOM_LEFT);
        this.setCursor(Cursor.HAND);

        // Quitamos los setPrefSize fijos para que el contenedor padre
        // maneje el espacio de forma fluida como antes
        this.setMinHeight(160);

        configurarContenido();
        cargarImagenFondo();
        aplicarClip(20);

        Animations.attachHoverLift(this);
    }

    private void configurarContenido() {
        // 1. Categoría (Badge)
        Label lblCategoria = new Label("SECURITY"); // Aquí iría p.getCategoria().getNombre()
        lblCategoria.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; " +
                "-fx-font-size: 9px; -fx-font-weight: bold; -fx-padding: 4 10; " +
                "-fx-background-radius: 8;");

        // 2. Título
        Label lblTitulo = new Label(proyecto.getTitulo());
        lblTitulo.setWrapText(true);
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        // 3. Resumen
        Label lblResumen = new Label(truncar(proyecto.getResumen(), 80));
        lblResumen.setWrapText(true);
        lblResumen.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b7280;");

        // 4. Fila Inferior (Iconos FontAwesome)
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);

        // Vistas (Izquierda)
        HBox vistasBox = new HBox(5);
        vistasBox.setAlignment(Pos.CENTER_LEFT);
        FontIcon iconVistas = new FontIcon("fas-eye");
        iconVistas.setIconSize(12);
        iconVistas.setIconColor(Color.web("#3F68E4"));
        Label lblVistas = new Label(String.valueOf(proyecto.getCantidadVistas()));
        lblVistas.setStyle("-fx-font-size: 11px; -fx-text-fill: #3F68E4;");
        vistasBox.getChildren().addAll(iconVistas, lblVistas);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Fecha (Derecha)
        HBox fechaBox = new HBox(5);
        fechaBox.setAlignment(Pos.CENTER_LEFT);
        FontIcon iconFecha = new FontIcon("fas-calendar-alt");
        iconFecha.setIconSize(11);
        iconFecha.setIconColor(Color.web("#6b7280"));
        Label lblFecha = new Label("12 May 2024"); // p.getFechaPublicacion()
        lblFecha.setStyle("-fx-font-size: 10px; -fx-text-fill: #6b7280;");
        fechaBox.getChildren().addAll(iconFecha, lblFecha);

        footer.getChildren().addAll(vistasBox, spacer, fechaBox);

        this.getChildren().addAll(lblCategoria, lblTitulo, lblResumen, footer);
    }

    private void cargarImagenFondo() {
        String rutaDirecta = "/galeria/images/PD/p1.jpg";

        try {
            var resource = getClass().getResource(rutaDirecta);
            if (resource != null) {
                Image img = new Image(resource.toExternalForm(), 500, 0, true, true, false);

                // Capa 1: Fondo sólido de respaldo
                BackgroundFill fondoBase = new BackgroundFill(Color.web("#E2E4E9"), new CornerRadii(20), Insets.EMPTY);

                // Capa 2: La Imagen
                BackgroundImage imagenFondo = new BackgroundImage(
                        img,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(1.0, 1.0, true, true, false, true)
                );

                // Capa 3: Filtro de opacidad (Blanco traslúcido)
                // Aumentamos el alfa a 0.7 para que la imagen se vea aún más sutil (más opaca hacia el blanco)
                BackgroundFill filtroClaridad = new BackgroundFill(Color.rgb(226, 228, 233, 0.8), new CornerRadii(20), Insets.EMPTY);

                // Aplicamos las capas: el filtro va al final para que quede "encima" de la imagen
                this.setBackground(new Background(
                        new BackgroundFill[]{fondoBase, filtroClaridad},
                        new BackgroundImage[]{imagenFondo}
                ));

            } else {
                this.setStyle("-fx-background-color: #E2E4E9; -fx-background-radius: 20;");
            }
        } catch (Exception e) {
            this.setStyle("-fx-background-color: #E2E4E9; -fx-background-radius: 20;");
        }
    }

    private void aplicarClip(double radio) {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(radio * 2);
        clip.setArcHeight(radio * 2);
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);
    }

    private String truncar(String t, int n) {
        if (t == null) return "";
        return t.length() > n ? t.substring(0, n) + "..." : t;
    }
}