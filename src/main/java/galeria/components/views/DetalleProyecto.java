package galeria.components.views;

import galeria.model.*;
import galeria.dao.*;
import galeria.app.MainApp;
import galeria.components.interfaz.CardProyecto;
import galeria.util.Animations;
import galeria.util.Sesion;
import galeria.util.Alertas;
import galeria.util.CardStyle;
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

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DetalleProyecto extends ScrollPane {
    private Proyecto proyecto;
    private GuardadoDAO guardadoDAO = new GuardadoDAO();
    private ValoracionDAO valoracionDAO = new ValoracionDAO();
    private RecursoDAO recursoDAO = new RecursoDAO();
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private List<FontIcon> estrellasIcons = new ArrayList<>();

    private Button btnGuardar;
    private Label lblNumGuardados, lblNumDescargas, lblCalificacionMedia;

    public DetalleProyecto(Proyecto p, Usuario userIgnored) {
        this.proyecto = p;

        this.setFitToWidth(true);
        this.setPannable(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC; -fx-border-color: transparent;");

        // --- CONTENEDOR RAIZ VERTICAL ---
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #F8FAFC;");
        root.setAlignment(Pos.TOP_CENTER);

        // --- SECCIÓN SUPERIOR: INFO PRINCIPAL ---
        HBox mainInfoContainer = new HBox(40);
        mainInfoContainer.setAlignment(Pos.TOP_CENTER);
        mainInfoContainer.setPadding(new Insets(60, 40, 20, 40));

        StackPane marcoImagen = crearSeccionPortada(p);
        VBox fichaInfo = crearFichaDetalle(p);

        mainInfoContainer.getChildren().addAll(marcoImagen, fichaInfo);

        // --- SECCIÓN INFERIOR: RECOMENDADOS ---
        VBox recomendadosContainer = crearSeccionRecomendados();

        root.getChildren().addAll(mainInfoContainer, recomendadosContainer);
        this.setContent(root);

        // Animaciones de entrada
        Animations.slideUpFadeIn(marcoImagen, 200);
        Animations.slideUpFadeIn(fichaInfo, 400);
        Animations.slideUpFadeIn(recomendadosContainer, 600);
    }

    private VBox crearFichaDetalle(Proyecto p) {
        VBox fichaInfo = new VBox(25);
        fichaInfo.setMinWidth(500); fichaInfo.setMaxWidth(500);
        fichaInfo.setPadding(new Insets(40));
        fichaInfo.setStyle("-fx-background-color: white; -fx-background-radius: 35; -fx-font-family: 'Manrope';");
        fichaInfo.setEffect(new DropShadow(30, Color.rgb(0, 0, 0, 0.06)));

        // Cabecera (Badge + Editar)
        HBox cabeceraFicha = new HBox();
        cabeceraFicha.setAlignment(Pos.CENTER_LEFT);
        Label badgeCat = new Label("INVESTIGACIÓN VERIFICADA");
        badgeCat.setStyle("-fx-background-color: #FFEDD5; -fx-text-fill: #9A3412; -fx-font-weight: bold; -fx-font-size: 11; -fx-padding: 8 16; -fx-background-radius: 20;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        cabeceraFicha.getChildren().addAll(badgeCat, spacer);

        if (Sesion.estaLogueado() && Sesion.esAdmin()) {
            FontIcon iconEdit = new FontIcon("fas-pen");
            iconEdit.setIconColor(Color.WHITE);
            iconEdit.setIconSize(14);
            Button btnEditar = new Button(" Editar", iconEdit);
            btnEditar.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 15; -fx-cursor: hand; -fx-font-size: 13;");
            btnEditar.setOnAction(e -> MainApp.setView(new SubirProyecto(proyecto)));
            Animations.attachHoverLift(btnEditar);
            cabeceraFicha.getChildren().add(btnEditar);
        }

        Label lblTitulo = new Label(p.getTitulo());
        lblTitulo.setWrapText(true);
        lblTitulo.setStyle("-fx-font-size: 32; -fx-font-weight: 800; -fx-text-fill: #0F172A; -fx-line-spacing: -2;");

        VBox metaData = new VBox(15);
        String nombresAutores = p.getListaAutores().stream().map(Autor::getNombreAutor).collect(Collectors.joining(", "));
        String nombreCat = (p.getCategoria() != null) ? p.getCategoria().getNombreCategoria().toUpperCase() : "GENERAL";
        metaData.getChildren().addAll(
                crearFilaInfoVisual("fas-user-circle", "AUTOR", nombresAutores.isEmpty() ? "No especificado" : nombresAutores),
                crearFilaInfoVisual("fas-shapes", "CATEGORÍA", nombreCat)
        );

        Label lblResumen = new Label(p.getResumen());
        lblResumen.setWrapText(true);
        lblResumen.setStyle("-fx-font-size: 15; -fx-text-fill: #475569; -fx-line-spacing: 5;");

        HBox rowStats = crearSeccionEstadisticas();

        VBox vBoxAcciones = new VBox(15);
        FontIcon iconDesc = new FontIcon("fas-download");
        iconDesc.setIconColor(Color.WHITE);
        Button btnDescargar = new Button(" Descargar Archivo Adjunto", iconDesc);
        btnDescargar.setMaxWidth(Double.MAX_VALUE);
        btnDescargar.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: 800; -fx-padding: 18; -fx-background-radius: 16; -fx-cursor: hand; -fx-font-size: 15;");
        btnDescargar.setOnAction(e -> manejarRecurso());
        Animations.attachHoverLift(btnDescargar);

        btnGuardar = new Button();
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        actualizarEstiloBotonGuardar();
        btnGuardar.setOnAction(e -> manejarFavorito());
        Animations.attachHoverLift(btnGuardar);

        VBox ratingContainer = crearRatingInteractivo();
        vBoxAcciones.getChildren().addAll(btnDescargar, btnGuardar, ratingContainer);

        fichaInfo.getChildren().addAll(cabeceraFicha, lblTitulo, metaData, lblResumen, rowStats, vBoxAcciones);
        return fichaInfo;
    }

    private VBox crearSeccionRecomendados() {
        VBox container = new VBox(30);
        container.setPadding(new Insets(20, 80, 60, 80));
        container.setAlignment(Pos.CENTER_LEFT);

        Label lblSubtitulo = new Label("Tal vez te interese");
        lblSubtitulo.setStyle("-fx-font-size: 24; -fx-font-weight: 800; -fx-text-fill: #0F172A;");

        // USO DE TU MÉTODO: listarTodosConAutor()
        List<Proyecto> todos = proyectoDAO.listarTodosConAutor();

        List<Proyecto> filtrados = todos.stream()
                .filter(p -> p.getIdProyecto() != proyecto.getIdProyecto())
                .limit(4)
                .collect(Collectors.toList());

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < filtrados.size(); i++) {
            CardProyecto card = new CardProyecto(filtrados.get(i), CardStyle.MINI);
            card.setPrefWidth(300);
            grid.add(card, i, 0);
        }

        container.getChildren().addAll(lblSubtitulo, grid);
        return container;
    }

    private StackPane crearSeccionPortada(Proyecto p) {
        StackPane marco = new StackPane();
        double imgW = 600; double imgH = 700;
        marco.setPrefSize(imgW, imgH);

        ImageView portada = new ImageView();
        String rutaBD = p.getPortadaURL();

        if (rutaBD != null && !rutaBD.isEmpty()) {
            try {
                String ruta = rutaBD.startsWith("/") ? rutaBD : "/" + rutaBD;
                // Carga robusta mediante Stream para evitar imágenes vacías en navegación interna
                Image img = new Image(getClass().getResourceAsStream(ruta), imgW, imgH, true, true);

                if (img.isError()) {
                    img = new Image(getClass().getResource(ruta).toExternalForm(), imgW, imgH, true, true);
                }

                portada.setImage(img);
                portada.setFitWidth(imgW);
                portada.setFitHeight(imgH);
            } catch (Exception e) {
                System.out.println("[ERROR IMAGEN DETALLE] " + e.getMessage());
                marco.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 50;");
            }
        }

        Rectangle clip = new Rectangle(imgW, imgH);
        clip.setArcWidth(50); clip.setArcHeight(50);
        marco.setClip(clip);
        marco.getChildren().add(portada);
        marco.setEffect(new DropShadow(40, Color.rgb(0, 0, 0, 0.12)));
        return marco;
    }

    private HBox crearSeccionEstadisticas() {
        HBox box = new HBox(40);
        box.setAlignment(Pos.CENTER);
        lblNumGuardados = new Label("0");
        lblNumDescargas = new Label("0");
        lblCalificacionMedia = new Label("0.0");
        String estiloNum = "-fx-font-size: 24; -fx-font-weight: 900; -fx-text-fill: #0F172A;";
        box.getChildren().addAll(
                crearBloqueStat(lblNumGuardados, "GUARDADOS", estiloNum),
                crearBloqueStat(lblNumDescargas, "DESCARGAS", estiloNum),
                crearBloqueStat(lblCalificacionMedia, "CALIFICACIÓN", estiloNum)
        );
        Animations.animarConteo(lblNumGuardados, proyecto.getCantidadGuardados(), estiloNum);
        Animations.animarConteo(lblNumDescargas, 4200, estiloNum);
        lblCalificacionMedia.setText(String.format("%.1f", proyecto.valoracionPromedio()));
        return box;
    }

    private VBox crearBloqueStat(Label lbl, String texto, String estilo) {
        VBox v = new VBox(-5); v.setAlignment(Pos.CENTER);
        lbl.setStyle(estilo);
        Label desc = new Label(texto);
        desc.setStyle("-fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
        v.getChildren().addAll(lbl, desc);
        return v;
    }

    private void manejarFavorito() {
        if (!Sesion.estaLogueado()) { Alertas.mostrarModalLoginRequerido(); return; }
        int idUser = Sesion.getUsuario().getIdUsuario();
        int idProy = proyecto.getIdProyecto();
        String estiloNum = "-fx-font-size: 24; -fx-font-weight: 900; -fx-text-fill: #0F172A;";

        if (guardadoDAO.esFavorito(idUser, idProy)) {
            if(guardadoDAO.eliminarFavorito(idUser, idProy)) {
                proyecto.setCantidadGuardados(proyecto.getCantidadGuardados() - 1);
                Animations.animarConteo(lblNumGuardados, proyecto.getCantidadGuardados(), estiloNum);
            }
        } else {
            if(guardadoDAO.guardarFavorito(idUser, idProy)) {
                proyecto.setCantidadGuardados(proyecto.getCantidadGuardados() + 1);
                Animations.animarConteo(lblNumGuardados, proyecto.getCantidadGuardados(), estiloNum);
            }
        }
        actualizarEstiloBotonGuardar();
    }

    private void actualizarEstiloBotonGuardar() {
        FontIcon icon = new FontIcon();
        icon.setIconColor(Color.WHITE);
        if (Sesion.estaLogueado() && guardadoDAO.esFavorito(Sesion.getUsuario().getIdUsuario(), proyecto.getIdProyecto())) {
            icon.setIconLiteral("fas-bookmark");
            btnGuardar.setText(" Proyecto Guardado");
            btnGuardar.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; -fx-font-weight: 800; -fx-padding: 18; -fx-background-radius: 16; -fx-font-size: 15;");
        } else {
            icon.setIconLiteral("far-bookmark");
            btnGuardar.setText(" Guardar proyecto");
            btnGuardar.setStyle("-fx-background-color: #CBD5E1; -fx-text-fill: white; -fx-font-weight: 800; -fx-padding: 18; -fx-background-radius: 16; -fx-font-size: 15;");
        }
        btnGuardar.setGraphic(icon);
    }

    private void manejarRecurso() {
        if (!Sesion.estaLogueado()) { Alertas.mostrarModalLoginRequerido(); return; }
        List<Recurso> recursos = recursoDAO.listarPorProyecto(proyecto.getIdProyecto());
        if (recursos == null || recursos.isEmpty()) {
            Alertas.mostrarMensaje("Sin Archivos", "No hay recursos adjuntos.", "fas-exclamation-circle", "#F97316");
        } else {
            try { Desktop.getDesktop().browse(new URI(recursos.get(0).getUrl())); } catch (Exception e) {}
        }
    }

    private void procesarCalificacion(int puntos) {
        if (!Sesion.estaLogueado()) { Alertas.mostrarModalLoginRequerido(); return; }
        Valoracion v = new Valoracion(0, Sesion.getUsuario(), proyecto, puntos, new Date());
        valoracionDAO.insertarValoracion(v);
        proyecto.getListaValoraciones().add(v);
        lblCalificacionMedia.setText(String.format("%.1f", proyecto.valoracionPromedio()));
        restaurarEstrellas(proyecto.valoracionPromedio());
        Alertas.mostrarMensaje("¡Gracias!", "Calificación guardada.", "fas-check-circle", "#10B981");
    }

    private VBox crearRatingInteractivo() {
        VBox box = new VBox(10); box.setAlignment(Pos.CENTER_RIGHT);
        Label lblHeader = new Label("Calificar Proyecto");
        lblHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E293B; -fx-font-size: 14;");
        HBox starsRow = new HBox(8); starsRow.setAlignment(Pos.CENTER_RIGHT);
        for (int i = 1; i <= 5; i++) {
            final int index = i;
            FontIcon star = new FontIcon("far-star"); star.setIconSize(28); star.setIconColor(Color.web("#CBD5E1")); star.setCursor(javafx.scene.Cursor.HAND);
            star.setOnMouseEntered(e -> iluminarEstrellas(index));
            star.setOnMouseExited(e -> restaurarEstrellas(proyecto.valoracionPromedio()));
            star.setOnMouseClicked(e -> procesarCalificacion(index));
            estrellasIcons.add(star); starsRow.getChildren().add(star);
        }
        restaurarEstrellas(proyecto.valoracionPromedio());
        box.getChildren().addAll(lblHeader, starsRow);
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

    private HBox crearFilaInfoVisual(String iconKey, String t, String val) {
        FontIcon icon = new FontIcon(iconKey); icon.setIconSize(22); icon.setIconColor(Color.web("#6366F1"));
        VBox v = new VBox(2);
        Label title = new Label(t); title.setStyle("-fx-font-size: 10; -fx-font-weight: 800; -fx-text-fill: #94A3B8;");
        Label value = new Label(val); value.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #1E293B;");
        v.getChildren().addAll(title, value);
        HBox row = new HBox(15, icon, v); row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}