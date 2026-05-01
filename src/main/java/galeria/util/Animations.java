package galeria.util;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** Helpers de animación reutilizables para toda la app. */
public final class Animations {

    private Animations() {}

    /** Aparece desde abajo con fade. */
    public static void slideUpFadeIn(Node node, double delayMs) {
        node.setOpacity(0);
        node.setTranslateY(40);

        FadeTransition fade = new FadeTransition(Duration.millis(700), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(700), node);
        slide.setFromY(40);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.2, 1));

        ParallelTransition pt = new ParallelTransition(fade, slide);
        pt.setDelay(Duration.millis(delayMs));
        pt.play();
    }

    /** Hover: escala ligera + sombra suave. */
    public static void attachHoverLift(Node node) {
        DropShadow shadow = new DropShadow(0, Color.color(0.25, 0.3, 0.7, 0));
        shadow.setOffsetY(0);
        node.setEffect(shadow);

        ScaleTransition stIn = new ScaleTransition(Duration.millis(220), node);
        stIn.setToX(1.035);
        stIn.setToY(1.035);
        stIn.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition stOut = new ScaleTransition(Duration.millis(220), node);
        stOut.setToX(1.0);
        stOut.setToY(1.0);
        stOut.setInterpolator(Interpolator.EASE_OUT);

        Timeline shadowIn = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(shadow.radiusProperty(), 28),
                new KeyValue(shadow.offsetYProperty(), 12),
                new KeyValue(shadow.colorProperty(), Color.color(0.25, 0.3, 0.7, 0.28))));

        Timeline shadowOut = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(shadow.radiusProperty(), 0),
                new KeyValue(shadow.offsetYProperty(), 0),
                new KeyValue(shadow.colorProperty(), Color.color(0.25, 0.3, 0.7, 0))));

        node.setOnMouseEntered(e -> {
            stOut.stop(); shadowOut.stop();
            stIn.playFromStart(); shadowIn.playFromStart();
        });
        node.setOnMouseExited(e -> {
            stIn.stop(); shadowIn.stop();
            stOut.playFromStart(); shadowOut.playFromStart();
        });
    }

    /** Pulso infinito (para puntos de "online", badges, etc.). */
    public static void attachPulse(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(900), node);
        st.setFromX(1); st.setFromY(1);
        st.setToX(1.25); st.setToY(1.25);
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }

    /** Float sutil arriba/abajo. */
    public static void attachFloat(Node node, double amplitude, double durationMs) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), node);
        tt.setFromY(-amplitude / 2);
        tt.setToY(amplitude / 2);
        tt.setAutoReverse(true);
        tt.setCycleCount(Animation.INDEFINITE);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }

    /**
     * Anima el ancho de un rectángulo desde 0 hasta su valor objetivo.
     */
    public static void lineExpand(Rectangle line, double targetWidth) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(line.widthProperty(), targetWidth, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Reduce el ancho de un rectángulo a 0.
     */
    public static void lineShrink(Rectangle line) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(line.widthProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public static void fadeInVideo(Node node, int delayMillis) {
        node.setOpacity(0);
        node.setScaleX(1.1); // Empieza un poco más grande
        node.setScaleY(1.1);

        FadeTransition fade = new FadeTransition(Duration.millis(1500), node);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(2000), node);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setDelay(Duration.millis(delayMillis));
        pt.play();
    }

    /** Anima un Label de 0 al valor objetivo. Llama esto cuando el nodo ya esté visible. */
    public static void animarConteo(Label label, int valorFinal, String estilo) {
        if (valorFinal <= 0) {
            label.setText("0");
            return;
        }
        int pasos = 45; // menos pasos = más rápido
        double duracionTotal = 900; // 0.9 segundos total
        double intervalo = duracionTotal / pasos;

        final int[] paso = {0};

        Timeline tl = new Timeline();
        tl.setCycleCount(pasos);
        tl.getKeyFrames().add(new KeyFrame(
                Duration.millis(intervalo),
                e -> {
                    paso[0]++;
                    double t = (double) paso[0] / pasos;
                    // smoothstep easing
                    double prog = t * t * (3 - 2 * t);
                    int val = (int)(valorFinal * prog);
                    label.setText(String.valueOf(val));
                    label.setStyle(estilo);
                }
        ));
        tl.setOnFinished(e -> {
            label.setText(String.valueOf(valorFinal));
            label.setStyle(estilo);
        });
        tl.play();
    }

    public static void animateOnScroll(Node node, ScrollPane scrollPane, Runnable animation) {
        final boolean[] animated = {false};
        ChangeListener<Number> scrollListener = (obs, oldVal, newVal) -> {
            if (animated[0]) return;

            Bounds boundsInScene = node.localToScene(node.getBoundsInLocal());
            Bounds scrollBounds = scrollPane.localToScene(scrollPane.getBoundsInLocal());

            if (scrollBounds.intersects(boundsInScene)) {
                animated[0] = true;
                animation.run();
            }
        };
        scrollPane.vvalueProperty().addListener(scrollListener);
    }

    /** Animación de entrada escalonada para las listas. */
    public static void revealProjectCard(Node node, double delayMs) {
        node.setOpacity(0);
        node.setTranslateX(-20);

        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.millis(600), node);
        move.setToX(0);

        ParallelTransition pt = new ParallelTransition(fade, move);
        pt.setDelay(Duration.millis(delayMs));
        pt.play();
    }
}
