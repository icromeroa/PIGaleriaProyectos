package galeria.components.views;

import galeria.model.Proyecto;
import galeria.model.Usuario;
import galeria.model.Autor;
import galeria.dao.GuardadoDAO;
import galeria.app.MainApp;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista de detalle de proyecto ajustada a la estética moderna.
 * Usa ScrollPane para evitar que el contenido oculte el Navbar.
 */
public class DetalleProyecto extends ScrollPane {
    private Proyecto proyecto;
    private Usuario usuarioActual;
    private GuardadoDAO guardadoDAO = new GuardadoDAO();
    private List<FontIcon> estrellasIcons = new ArrayList<>();

    public DetalleProyecto(Proyecto p, Usuario user) {
        this.proyecto = p;
        this.usuarioActual = user;

        // --- CONFIGURACIÓN DEL SCROLLPANE ---
        this.setFitToWidth(true);
        this.setPannable(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC; -fx-border-color: transparent;");

        // --- CONTENEDOR DE CONTENIDO ---
        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(40, 80, 30, 80));
        mainContainer.setStyle("-fx-background-color: #F8FAFC;");

        // --- COLUMNA IZQUIERDA: PORTADA (Horizontal/Amplia) ---
        StackPane marcoImagen = new StackPane();
        double imgW = 600;
        double imgH = 650;
        marcoImagen.setPrefSize(imgW, imgH);
        marcoImagen.setMinSize(imgW, imgH);
        marcoImagen.setMaxSize(imgW, imgH);

        ImageView portada = new ImageView();
        try {
            String ruta = p.getPortadaURL();
            if (ruta != null && !ruta.isEmpty()) {
                Image img = new Image(getClass().getResourceAsStream(ruta.startsWith("/") ? ruta : "/" + ruta));
                portada.setImage(img);
                portada.setFitWidth(imgW);
                portada.setFitHeight(imgH);
                portada.setPreserveRatio(false); // Ajuste total al contenedor tipo "cover"
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
        }

        Rectangle clip = new Rectangle(imgW, imgH);
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        marcoImagen.setClip(clip);
        marcoImagen.getChildren().add(portada);

        // Sombra suave para la imagen
        marcoImagen.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.1)));

        // --- COLUMNA DERECHA: FICHA DE INFORMACIÓN ---
        VBox fichaInfo = new VBox(25);
        fichaInfo.setMinWidth(480);
        fichaInfo.setMaxWidth(480);
        fichaInfo.setPadding(new Insets(40));
        fichaInfo.setStyle("-fx-background-color: white; -fx-background-radius: 35; -fx-font-family: 'Manrope';");
        fichaInfo.setEffect(new DropShadow(30, Color.rgb(0, 0, 0, 0.06)));

        // 1. Header (Categoría + Editar)
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        String nombreCat = (p.getCategoria() != null) ? p.getCategoria().getNombreCategoria().toUpperCase() : "GENERAL";
        Label badgeCat = new Label("INVESTIGACIÓN: " + nombreCat);
        badgeCat.setStyle("-fx-background-color: #FFF7ED; -fx-text-fill: #C2410C; -fx-font-weight: bold; -fx-font-size: 10; -fx-padding: 6 14; -fx-background-radius: 20; -fx-border-color: #FDBA74; -fx-border-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().add(badgeCat);

        if (usuarioActual != null && usuarioActual.getEsAdmin()) {
            // 1. Creamos el icono
            FontIcon iconoEditar = new FontIcon("fas-pencil-alt");

            // 2. LE CAMBIAMOS EL COLOR A BLANCO AQUÍ
            iconoEditar.setIconColor(Color.WHITE);
            iconoEditar.setIconSize(14);

            // 3. Se lo pasamos al botón
            Button btnEdit = new Button(" Editar", iconoEditar);

            // El estilo del botón ya tiene el fondo naranja (#F97316)
            btnEdit.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 6 16; -fx-cursor: hand;");

            header.getChildren().addAll(spacer, btnEdit);
            Animations.attachHoverLift(btnEdit);
        }

        // 2. Título
        Label lblTitulo = new Label(p.getTitulo());
        lblTitulo.setWrapText(true);
        lblTitulo.setStyle("-fx-font-size: 30; -fx-font-weight: 800; -fx-text-fill: #0F172A; -fx-line-spacing: -1;");

        // 3. Metadata (Autores y Categoría)
        VBox metaData = new VBox(15);

        // Obtener nombres de autores de la lista del modelo
        String nombresAutores = p.getListaAutores().stream()
                .map(Autor::getNombreAutor)
                .collect(Collectors.joining(", "));
        if (nombresAutores.isEmpty()) nombresAutores = "Autor no especificado";

        metaData.getChildren().addAll(
                crearFilaIcono("fas-user-circle", "AUTOR(ES)", nombresAutores),
                crearFilaIcono("fas-tag", "CATEGORÍA", nombreCat)
        );

        // 4. Resumen
        Label lblResumen = new Label(p.getResumen());
        lblResumen.setWrapText(true);
        lblResumen.setStyle("-fx-font-size: 14; -fx-text-fill: #64748B; -fx-line-spacing: 5;");

        // 5. Estadísticas (Vistas y Guardados)
        HBox rowStats = new HBox(60);
        rowStats.setAlignment(Pos.CENTER_LEFT);
        rowStats.getChildren().addAll(
                crearBloqueStat(String.valueOf(p.getCantidadVistas()), "VISTAS"),
                crearBloqueStat(String.valueOf(p.getCantidadGuardados()), "GUARDADOS")
        );

        // 6. Botones de Acción
        VBox vBoxAcciones = new VBox(15);

// 1. Creamos el icono de descarga
        FontIcon iconoDescargar = new FontIcon("fas-download");

// 2. Seteamos el color blanco y un tamaño adecuado
        iconoDescargar.setIconColor(Color.WHITE);
        iconoDescargar.setIconSize(18);

// 3. Configuramos el botón con el icono y el color naranja de fondo
        Button btnDescargar = new Button(" Descargar Archivo Adjunto", iconoDescargar);
        btnDescargar.setMaxWidth(Double.MAX_VALUE);
        btnDescargar.setStyle("-fx-background-color: #F97316; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 16; " +
                "-fx-background-radius: 14; " +
                "-fx-cursor: hand;");

        btnDescargar.setOnAction(e -> manejarDescarga());
        Animations.attachHoverLift(btnDescargar);

        Button btnGuardar = new Button(" Guardar proyecto", new FontIcon("far-bookmark"));
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-padding: 16; -fx-background-radius: 14; -fx-cursor: hand;");
        btnGuardar.setOnAction(e -> manejarFavorito(btnGuardar));
        Animations.attachHoverLift(btnGuardar);

        // 7. Rating Interactivo
        VBox ratingContainer = crearRatingInteractivo();

        vBoxAcciones.getChildren().addAll(btnDescargar, btnGuardar, ratingContainer);

        fichaInfo.getChildren().addAll(header, lblTitulo, metaData, lblResumen, rowStats, vBoxAcciones);

        mainContainer.getChildren().addAll(marcoImagen, fichaInfo);
        this.setContent(mainContainer);

        // Animaciones de entrada
        Animations.slideUpFadeIn(marcoImagen, 200);
        Animations.slideUpFadeIn(fichaInfo, 400);
    }

    // --- MÉTODOS AUXILIARES ---

    private VBox crearRatingInteractivo() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPadding(new Insets(10, 0, 0, 0));

        float promedio = proyecto.valoracionPromedio();
        String txtLabel = (promedio == 0) ? "Aún no ha sido calificado" : String.format("Calificación: %.1f / 5.0", promedio);

        Label lblStatus = new Label(txtLabel);
        lblStatus.setStyle("-fx-font-size: 11; -fx-text-fill: #94A3B8; -fx-font-weight: bold;");

        HBox starsRow = new HBox(5);
        starsRow.setAlignment(Pos.CENTER_RIGHT);

        for (int i = 1; i <= 5; i++) {
            final int index = i;
            FontIcon star = new FontIcon("far-star");
            star.setIconSize(22);
            star.setIconColor(Color.web("#CBD5E1"));
            star.setCursor(javafx.scene.Cursor.HAND);

            // Eventos de Mouse
            star.setOnMouseEntered(e -> iluminarEstrellas(index));
            star.setOnMouseExited(e -> restaurarEstrellas(proyecto.valoracionPromedio()));
            star.setOnMouseClicked(e -> procesarCalificacion(index));

            estrellasIcons.add(star);
            starsRow.getChildren().add(star);
        }

        // Inicializar con el promedio real
        restaurarEstrellas(promedio);

        box.getChildren().addAll(lblStatus, starsRow);
        return box;
    }

    private void iluminarEstrellas(int count) {
        for (int i = 0; i < 5; i++) {
            estrellasIcons.get(i).setIconLiteral(i < count ? "fas-star" : "far-star");
            estrellasIcons.get(i).setIconColor(Color.web("#F97316"));
        }
    }

    private void restaurarEstrellas(float valor) {
        for (int i = 0; i < 5; i++) {
            int nivel = i + 1;
            if (valor >= nivel) {
                estrellasIcons.get(i).setIconLiteral("fas-star");
                estrellasIcons.get(i).setIconColor(Color.web("#F59E0B"));
            } else if (valor >= nivel - 0.5) {
                estrellasIcons.get(i).setIconLiteral("fas-star-half-alt");
                estrellasIcons.get(i).setIconColor(Color.web("#F59E0B"));
            } else {
                estrellasIcons.get(i).setIconLiteral("far-star");
                estrellasIcons.get(i).setIconColor(Color.web("#CBD5E1"));
            }
        }
    }

    private void procesarCalificacion(int puntos) {
        if (usuarioActual == null) {
            Notifications.create()
                    .title("Identificación requerida")
                    .text("Para calificar este proyecto debes iniciar sesión con tu cuenta.")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showWarning();
        } else {
            // Aquí iría tu lógica de DAO para guardar la valoración
            Notifications.create()
                    .title("¡Valoración recibida!")
                    .text("Has calificado este proyecto con " + puntos + " estrellas.")
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
        }
    }

    private HBox crearFilaIcono(String icon, String title, String value) {
        FontIcon i = new FontIcon(icon);
        i.setIconSize(20);
        i.setIconColor(Color.web("#6366F1"));
        VBox v = new VBox(2);
        Label t = new Label(title); t.setStyle("-fx-font-size: 9; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
        Label val = new Label(value); val.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1E293B;");
        v.getChildren().addAll(t, val);
        return new HBox(15, i, v);
    }

    private VBox crearBloqueStat(String num, String desc) {
        Label n = new Label(num); n.setStyle("-fx-font-size: 24; -fx-font-weight: 900; -fx-text-fill: #0F172A;");
        Label d = new Label(desc); d.setStyle("-fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: #94A3B8; -fx-letter-spacing: 1px;");
        VBox b = new VBox(-2, n, d);
        b.setAlignment(Pos.CENTER_LEFT);
        return b;
    }

    private void manejarDescarga() {
        if (usuarioActual == null) {
            Notifications.create().title("Atención").text("Inicia sesión para descargar archivos.").showInformation();
        } else {
            // Lógica de descarga...
        }
    }

    private void manejarFavorito(Button btn) {
        if (usuarioActual == null) {
            Notifications.create().title("Atención").text("Inicia sesión para guardar favoritos.").showWarning();
            return;
        }
        // Lógica de guardado DAO...
    }
}