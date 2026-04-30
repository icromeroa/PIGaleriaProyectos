package galeria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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
}
