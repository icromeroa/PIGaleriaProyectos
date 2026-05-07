package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.components.views.DetalleProyecto;
import galeria.model.Proyecto;
import galeria.model.Usuario;
import galeria.util.Animations;
import galeria.util.CardStyle;
import galeria.util.Sesion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import java.text.SimpleDateFormat;

public class CardProyecto extends VBox {
    private final Proyecto proyecto;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

    public CardProyecto(Proyecto p, CardStyle estilo) {
        this.proyecto = p;

        this.setPadding(new Insets(15));
        this.setSpacing(8);
        this.setAlignment(Pos.BOTTOM_LEFT);
        this.setCursor(Cursor.HAND);
        this.setMinHeight(180); // Un poco más de altura para el contenido real

        configurarContenido();
        cargarImagenFondo();
        aplicarClip(20);

        Animations.attachHoverLift(this);

        this.setOnMouseClicked(e -> {
            MainApp.setView(new DetalleProyecto(this.proyecto, Sesion.getUsuario()));
        });
    }

    private void configurarContenido() {
        // 1. Categoría Real (Badge)
        String nombreCat = (proyecto.getCategoria() != null) ? proyecto.getCategoria().getNombreCategoria().toUpperCase() : "GENERAL";
        Label lblCategoria = new Label(nombreCat);
        lblCategoria.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; " +
                "-fx-font-size: 9px; -fx-font-weight: bold; -fx-padding: 4 10; " +
                "-fx-background-radius: 8; -fx-font-family: 'Manrope';");

        // 2. Título
        Label lblTitulo = new Label(proyecto.getTitulo());
        lblTitulo.setWrapText(true);
        lblTitulo.setMaxHeight(50);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1f2937; -fx-font-family: 'Manrope';");

        // 3. Resumen
        Label lblResumen = new Label(truncar(proyecto.getResumen(), 75));
        lblResumen.setWrapText(true);
        lblResumen.setStyle("-fx-font-size: 11px; -fx-text-fill: #4b5563; -fx-font-family: 'Manrope';");

        // 4. Fila Inferior
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);

        // Vistas
        HBox vistasBox = new HBox(5);
        vistasBox.setAlignment(Pos.CENTER_LEFT);
        FontIcon iconVistas = new FontIcon("fas-eye");
        iconVistas.setIconSize(12);
        iconVistas.setIconColor(Color.web("#3F68E4"));
        Label lblVistas = new Label(String.valueOf(proyecto.getCantidadVistas()));
        lblVistas.setStyle("-fx-font-size: 11px; -fx-text-fill: #3F68E4; -fx-font-weight: bold;");
        vistasBox.getChildren().addAll(iconVistas, lblVistas);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Fecha Real
        HBox fechaBox = new HBox(5);
        fechaBox.setAlignment(Pos.CENTER_LEFT);
        FontIcon iconFecha = new FontIcon("fas-calendar-alt");
        iconFecha.setIconSize(11);
        iconFecha.setIconColor(Color.web("#6b7280"));

        String fechaStr = (proyecto.getFechaSubida() != null) ? dateFormat.format(proyecto.getFechaSubida()) : "Reciente";
        Label lblFecha = new Label(fechaStr);
        lblFecha.setStyle("-fx-font-size: 10px; -fx-text-fill: #6b7280; -fx-font-family: 'Manrope';");
        fechaBox.getChildren().addAll(iconFecha, lblFecha);

        footer.getChildren().addAll(vistasBox, spacer, fechaBox);

        this.getChildren().addAll(lblCategoria, lblTitulo, lblResumen, footer);
    }

    private void cargarImagenFondo() {
        String urlPortada = proyecto.getPortadaURL();

        // 1. Verificar si es una URL de Cloudinary (HTTP/HTTPS)
        if (urlPortada != null && urlPortada.startsWith("http")) {
            try {
                // El parámetro 'true' indica que la imagen se cargará en segundo plano (no congela la app)
                Image img = new Image(urlPortada, 400, 0, true, true, false);

                // Listener para aplicar el fondo una vez que la imagen termine de descargar
                img.progressProperty().addListener((obs, old, progress) -> {
                    if (progress.doubleValue() == 1.0) {
                        aplicarImagenFondo(img);
                    }
                });

                // Mientras carga, ponemos un color base para que no se vea vacío
                aplicarFondoColor();
                return;
            } catch (Exception e) {
                System.err.println("Error cargando imagen remota: " + e.getMessage());
            }
        }

        // 2. Fallback: Si no es URL, intentar cargar desde recursos locales
        if (urlPortada != null && !urlPortada.isEmpty()) {
            try {
                String ruta = urlPortada.startsWith("/") ? urlPortada : "/" + urlPortada;
                var resource = getClass().getResource(ruta);
                if (resource != null) {
                    Image img = new Image(resource.toExternalForm(), 400, 0, true, true, false);
                    aplicarImagenFondo(img);
                    return;
                }
            } catch (Exception ignored) {}
        }

        // 3. Si todo falla, usar color plano
        aplicarFondoColor();
    }

    private void aplicarImagenFondo(Image img) {
        // Fondo gris muy claro de base
        BackgroundFill fondoBase = new BackgroundFill(Color.web("#F8FAFC"), new CornerRadii(20), Insets.EMPTY);

        BackgroundImage imagenFondo = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );

        // Filtro blanco semi-transparente para que el texto sea legible sobre la imagen
        BackgroundFill filtroLegibilidad = new BackgroundFill(
                Color.rgb(255, 255, 255, 0.82),
                new CornerRadii(20),
                Insets.EMPTY
        );

        this.setBackground(new Background(
                new BackgroundFill[]{fondoBase, filtroLegibilidad},
                new BackgroundImage[]{imagenFondo}
        ));
    }

    private void aplicarFondoColor() {
        String[] colores = {"#FFF7ED", "#F0F9FF", "#FFEDD5", "#E0F2FE", "#FEF3C7", "#EFF6FF"};
        String color = colores[Math.abs(proyecto.getIdProyecto()) % colores.length];
        this.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(0,0,0,0.05);" +
                        "-fx-border-radius: 20;"
        );
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