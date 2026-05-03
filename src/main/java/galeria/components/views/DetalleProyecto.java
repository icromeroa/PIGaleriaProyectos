package galeria.components.views; // <--- Ubicación corregida

import galeria.model.Proyecto;
import galeria.model.Usuario;
import galeria.dao.GuardadoDAO;
import galeria.app.MainApp;
import galeria.components.interfaz.ModalEditarProyecto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class DetalleProyecto extends HBox {
    private Proyecto proyecto;
    private Usuario usuarioActual;
    private GuardadoDAO guardadoDAO = new GuardadoDAO();

    public DetalleProyecto(Proyecto p, Usuario user) {
        this.proyecto = p;
        this.usuarioActual = user;

        // Configuración del contenedor principal (HBox)
        this.setSpacing(50);
        this.setPadding(new Insets(60));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #ffffff;");

        // --- COLUMNA IZQUIERDA: PORTADA ---
        VBox colIzquierda = new VBox();
        colIzquierda.setMinWidth(400);
        colIzquierda.setMaxWidth(400);

        ImageView portada = new ImageView();
        try {
            // Ajuste de ruta de imagen similar a tu CardProyecto
            String ruta = p.getPortadaURL();
            if (ruta != null && !ruta.isEmpty()) {
                portada.setImage(new Image(getClass().getResourceAsStream(ruta.startsWith("/") ? ruta : "/" + ruta)));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de detalle.");
        }

        portada.setFitWidth(400);
        portada.setPreserveRatio(true);

        // Bordes redondeados para la portada
        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        clip.widthProperty().bind(portada.fitWidthProperty());
        clip.heightProperty().bind(portada.layoutBoundsProperty().map(bounds -> bounds.getHeight()));
        portada.setClip(clip);

        colIzquierda.getChildren().add(portada);

        // --- COLUMNA DERECHA: INFORMACIÓN ---
        VBox colDerecha = new VBox(30);
        colDerecha.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(colDerecha, Priority.ALWAYS);

        // Header: Badge + Botón Editar
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label badge = new Label("PROYECTO ACADÉMICO");
        badge.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: #4338CA; -fx-font-weight: bold; -fx-padding: 6 15; -fx-background-radius: 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().add(badge);

        // Lógica corregida para el Administrador según tu modelo Usuario
        if (usuarioActual != null && usuarioActual.getEsAdmin()) { // <--- AQUÍ ESTABA EL ERROR (getEsAdmin)
            Button btnEditar = new Button(" Editar Proyecto", new FontIcon("fas-edit"));
            btnEditar.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-background-radius: 25; -fx-padding: 10 25; -fx-cursor: hand;");
            btnEditar.setOnAction(e -> abrirModalEdicion());
            header.getChildren().addAll(spacer, btnEditar);
        }

        // Título y Descripción
        Label titulo = new Label(p.getTitulo());
        titulo.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");
        titulo.setWrapText(true);

        Label desc = new Label(p.getResumen());
        desc.setStyle("-fx-font-size: 18px; -fx-text-fill: #64748B; -fx-line-spacing: 6;");
        desc.setWrapText(true);
        desc.setMaxWidth(800);

        // Botones de Acción
        HBox acciones = new HBox(20);

// --- DENTRO DEL CONSTRUCTOR DE DetalleProyecto ---

// Botón Descargar con validación de sesión
        Button btnDescargar = new Button(" Descargar PDF", new FontIcon("fas-file-download"));
        btnDescargar.setStyle("-fx-background-color: #0F172A; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 15 35; -fx-font-weight: bold; -fx-cursor: hand;");

        btnDescargar.setOnAction(e -> {
            if (usuarioActual == null) {
                // Si no está logueado, lanzamos alerta
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Acceso Restringido");
                alert.setHeaderText("Debes iniciar sesión");
                alert.setContentText("La descarga de archivos está reservada para usuarios registrados.");
                alert.showAndWait();

                // Opcional: Redirigir al login después de cerrar la alerta
                // MainApp.setView(new Login());
            } else {
                // Si hay usuario, llamamos al método que causaba el error
                descargarArchivo(proyecto.getArchivoURL());
            }
        });

// Botón Favoritos (Ya lo tienes condicionado para desactivarse o cambiar texto)
        Button btnFav = new Button();
// ... (tu lógica de estilo actual)

        if (usuarioActual == null) {
            btnFav.setText(" Inicia sesión para guardar");
            btnFav.setDisable(true); // Opcional: puedes dejarlo habilitado y que mande al Login también
        } else {
            actualizarIconoFavorito(btnFav);
            btnFav.setOnAction(ev -> manejarFavorito(btnFav));
        }

        acciones.getChildren().addAll(btnDescargar, btnFav);

        colDerecha.getChildren().addAll(header, titulo, desc, acciones);

        this.getChildren().addAll(colIzquierda, colDerecha);
    }

    private void manejarFavorito(Button btn) {
        // Usamos getIdUsuario() e getIdProyecto() que ya tienes en tus modelos
        if (guardadoDAO.esFavorito(usuarioActual.getIdUsuario(), proyecto.getIdProyecto())) {
            guardadoDAO.eliminarFavorito(usuarioActual.getIdUsuario(), proyecto.getIdProyecto());
        } else {
            guardadoDAO.guardarFavorito(usuarioActual.getIdUsuario(), proyecto.getIdProyecto());
        }
        actualizarIconoFavorito(btn);
    }

    private void actualizarIconoFavorito(Button btn) {
        boolean esFav = guardadoDAO.esFavorito(usuarioActual.getIdUsuario(), proyecto.getIdProyecto());
        FontIcon icono = new FontIcon(esFav ? "fas-heart" : "far-heart");
        if (esFav) icono.setIconColor(Color.RED);
        btn.setGraphic(icono);
        btn.setText(esFav ? " Guardado" : " Guardar");
    }

    private void abrirModalEdicion() {
        // Usamos el método de MainApp que desenfoca toda la interfaz
        MainApp.aplicarEfectoBlur(true);

        // Se asume que ModalEditarProyecto es un Stage o Dialog
        ModalEditarProyecto modal = new ModalEditarProyecto(this.proyecto);
        modal.showAndWait(); // Al ser modal, detiene la ejecución aquí hasta cerrarse

        MainApp.aplicarEfectoBlur(false); // Quitamos el desenfoque al volver
    }

    private void descargarArchivo(String url) {
        if (url == null || url.isEmpty()) {
            System.out.println("El proyecto no tiene una URL de archivo válida.");
            return;
        }

        try {
            // Opción nativa de Java para abrir archivos o URLs en el sistema operativo
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

                // Si la URL es un link web (http)
                if (url.startsWith("http")) {
                    desktop.browse(new java.net.URI(url));
                } else {
                    // Si la URL es una ruta de archivo local
                    java.io.File archivo = new java.io.File(url);
                    if (archivo.exists()) {
                        desktop.open(archivo);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al intentar abrir el archivo: " + e.getMessage());
        }
    }
}