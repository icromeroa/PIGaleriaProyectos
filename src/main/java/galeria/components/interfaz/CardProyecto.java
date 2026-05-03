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

        // Dentro del constructor de CardProyecto
        this.setOnMouseClicked(e -> {
            // 1. Obtenemos el usuario logueado (asumiendo que usas tu clase Sesion en util)
            Usuario usuarioLogueado = Sesion.getUsuario();

            // 2. Creamos la vista de detalle pasándole el proyecto de esta Card
            DetalleProyecto vistaDetalle = new DetalleProyecto(this.proyecto, usuarioLogueado);

            // 3. Usamos el método estático de tu MainApp para cambiar la vista
            MainApp.setView(new DetalleProyecto(this.proyecto, Sesion.getUsuario()));
        });

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
        String rutaBD = proyecto.getPortadaURL();

        if (rutaBD == null || rutaBD.isEmpty()) {
            aplicarFondoColor();
            return;
        }

        // Asegura que empiece con /
        String ruta = rutaBD.startsWith("/") ? rutaBD : "/" + rutaBD;

        try {
        // Método 1: getResource (classpath)
            var resource = getClass().getResource(ruta);

            if (resource == null) {
        // Método 2: buscar directamente en src/main/resources
        // útil cuando Maven no copió el archivo al target todavía
                java.io.File archivo = new java.io.File(
                        "src/main/resources" + ruta
                );

                if (archivo.exists()) {
                    Image img = new Image(
                            archivo.toURI().toString(),
                            500, 0, true, true, false
                    );
                    aplicarImagenFondo(img);
                    System.out.println("[IMAGEN] Cargada desde filesystem: " + archivo.getAbsolutePath());
                } else {
                    System.out.println("[IMAGEN] No encontrada en classpath ni filesystem: " + ruta);
                    System.out.println("[IMAGEN] Ruta absoluta buscada: " + archivo.getAbsolutePath());
                    aplicarFondoColor();
                }
                return;
            }

            Image img = new Image(resource.toExternalForm(), 500, 0, true, true, false);
            aplicarImagenFondo(img);
            System.out.println("[IMAGEN] Cargada desde classpath: " + ruta);

        } catch (Exception e) {
            System.out.println("[IMAGEN] Error: " + e.getMessage());
            aplicarFondoColor();
        }
    }

    private void aplicarImagenFondo(Image img) {
        BackgroundFill fondoBase = new BackgroundFill(
                Color.web("#E2E4E9"),
                new CornerRadii(20),
                Insets.EMPTY
        );
        BackgroundImage imagenFondo = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );
        BackgroundFill filtro = new BackgroundFill(
                Color.rgb(226, 228, 233, 0.75),
                new CornerRadii(20),
                Insets.EMPTY
        );
        this.setBackground(new Background(
                new BackgroundFill[]{fondoBase, filtro},
                new BackgroundImage[]{imagenFondo}
        ));
    }

    private void aplicarFondoColor() {
        String[] colores = {
                "#dce6ff", "#ffe4cc", "#d1fae5",
                "#fce7f3", "#e0e7ff", "#fef3c7"
        };
        String color = colores[proyecto.getIdProyecto() % colores.length];
        this.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 20;"
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