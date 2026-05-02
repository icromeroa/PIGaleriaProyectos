package galeria.components.views;

import galeria.components.interfaz.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Inicio extends ScrollPane {

    public Inicio() {
        VBox content = new VBox(40);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(16, 40, 48, 40));
        content.setStyle("-fx-background-color: #ffffff;");

        // Hero
        Hero hero = new Hero();
        hero.setMaxWidth(Double.MAX_VALUE);
        hero.prefWidthProperty().bind(content.widthProperty().subtract(80));

        // Sectores
        SectoresPrincipales sectores = new SectoresPrincipales();
        sectores.setMaxWidth(Double.MAX_VALUE);
        sectores.prefWidthProperty().bind(content.widthProperty().subtract(80));

        // Proyectos destacados
        ProyectosDestacados destacados = new ProyectosDestacados();
        destacados.setMaxWidth(Double.MAX_VALUE);
        destacados.prefWidthProperty().bind(content.widthProperty().subtract(80));

        // Estadísticas
        Estadisticas estadisticas = new Estadisticas();
        estadisticas.setMaxWidth(Double.MAX_VALUE);
        estadisticas.prefWidthProperty().bind(content.widthProperty().subtract(80));

        content.getChildren().addAll(hero, sectores, destacados, estadisticas);

        this.setContent(content);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background: #ffffff;" +
                        "-fx-border-color: transparent;"
        );

        ListadoProyectosDestacados listadoEspecial = new ListadoProyectosDestacados(this);
        listadoEspecial.setMaxWidth(Double.MAX_VALUE);
        listadoEspecial.prefWidthProperty().bind(content.widthProperty().subtract(80));

        content.getChildren().add(listadoEspecial);



    }
}