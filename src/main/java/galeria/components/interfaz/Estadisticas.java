package galeria.components.interfaz;

import galeria.dao.ProyectoDAO;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class Estadisticas extends HBox {

    public Estadisticas() {
        setSpacing(0);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(48, 0, 48, 0));
        setStyle(
                "-fx-background-color: #f8faff;" +
                        "-fx-background-radius: 20;"
        );

        ProyectoDAO dao = new ProyectoDAO();
        int[] stats = dao.getEstadisticasGenerales();
        // stats: [vistas, proyectos, guardados, usuarios]

        Object[][] datos = {
                {stats[0], "VISTAS",     "#3F68E4"},
                {stats[1], "PROYECTOS",  "#1f2937"},
                {stats[2], "GUARDADOS",  "#F97316"},
                {stats[3], "USUARIOS",   "#1f2937"}
        };

        for (int i = 0; i < datos.length; i++) {
            int    valor    = (int)    datos[i][0];
            String etiqueta = (String) datos[i][1];
            String color    = (String) datos[i][2];

            VBox stat = crearStat(valor, etiqueta, color);
            HBox.setHgrow(stat, Priority.ALWAYS);
            getChildren().add(stat);

            if (i < datos.length - 1) {
                Region sep = new Region();
                sep.setPrefWidth(1);
                sep.setPrefHeight(48);
                sep.setMaxHeight(48);
                sep.setStyle("-fx-background-color: #e5e7eb;");
                getChildren().add(sep);
            }
        }
    }

    private VBox crearStat(int valorFinal, String etiqueta, String color) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 24, 0, 24));

        String estiloNumero =
                "-fx-font-family: 'Manrope Bold';" +
                        "-fx-font-size: 38px;" +
                        "-fx-text-fill: " + color + ";";

        Label lblNumero = new Label("0");
        lblNumero.setStyle(estiloNumero);

        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.setStyle(
                "-fx-font-family: 'Manrope SemiBold';" +
                        "-fx-font-size: 11px;" +
                        "-fx-text-fill: #9ca3af;" +
                        "-fx-letter-spacing: 1.5px;"
        );

        box.getChildren().addAll(lblNumero, lblEtiqueta);

        // Animación reutilizada desde Animations
        Animations.animarConteo(lblNumero, valorFinal, estiloNumero);

        return box;
    }
}