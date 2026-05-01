package galeria.components.interfaz;

import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class Hero extends StackPane {

    public Hero() {
        setPrefHeight(500);
        setMaxHeight(500);

        // 1. Clip redondeado
        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);

        // 2. Contenedor del video
        StackPane videoContainer = new StackPane();
        setupVideoBackground(videoContainer);

        // 3. Overlay (Cambiado a un gradiente para que el texto resalte más)
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.6));");

        // 4. Contenido
        VBox content = new VBox(20);
        content.setAlignment(Pos.BOTTOM_CENTER);
        content.setPadding(new Insets(0, 0, 30, 0));

        // Badge (Píldora)
        HBox badge = new HBox(10);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(8, 20, 8, 20));
        badge.setStyle("-fx-background-color: #3F68E4; -fx-background-radius: 50;");
        badge.setMaxWidth(Region.USE_PREF_SIZE);

        Circle dot = new Circle(4, Color.WHITE);
        Animations.attachPulse(dot);

        Label badgeText = new Label("REPOSITORIO DIGITAL USBBOG");
        badgeText.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-family: 'Manrope Bold';" +
                        "-fx-font-size: 11px;" +
                        "-fx-letter-spacing: 1.5px;"
        );
        badge.getChildren().addAll(dot, badgeText);

        // Subtítulo
        Label subtitle = new Label(
                "Almacene, organice y descubra proyectos de la USB\n" +
                        "fácilmente con nuestro sistema de archivo."
        );
        subtitle.setTextFill(Color.WHITE);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        subtitle.setStyle(
                "-fx-font-family: 'Manrope Regular';" +
                        "-fx-font-size: 19px;" +
                        "-fx-line-spacing: 5px;"
        );

        content.getChildren().addAll(badge, subtitle);

        // Ensamblaje
        getChildren().addAll(videoContainer, overlay, content);

        // Bloquear interacción con el fondo
        overlay.setMouseTransparent(true);
        content.setMouseTransparent(true);

        // --- ANIMACIONES ---
        // El video aparece despacito (1.5 segundos de fade)
        Animations.fadeInVideo(videoContainer, 100);

        // El texto sube después de que el video empieza a verse
        Animations.slideUpFadeIn(badge, 600);
        Animations.slideUpFadeIn(subtitle, 900);
    }

    private void setupVideoBackground(StackPane container) {
        try {
            var resource = getClass().getResource("/galeria/video/landing_video.mp4");
            if (resource == null) throw new Exception("Video no encontrado");

            Media media = new Media(resource.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(this.widthProperty());
            mediaView.fitHeightProperty().bind(this.heightProperty());

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);

            container.getChildren().add(mediaView);

            // Importante: No mostramos el contenedor hasta que el video esté listo
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
            });

        } catch (Throwable t) {
            container.setStyle("-fx-background-color: #1a1b2e;");
        }
    }
}