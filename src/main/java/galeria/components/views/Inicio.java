package galeria.components.views;

import galeria.components.interfaz.Hero;
import galeria.components.interfaz.SectoresPrincipales;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Inicio extends ScrollPane {

    public Inicio() {
        VBox content = new VBox(32);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(16, 40, 48, 40));
        content.setStyle("-fx-background-color: #ffffff;");

        Hero hero = new Hero();

        // Esto es lo que controla el ancho del hero:
        // le decimos que su ancho máximo sea el ancho del content
        // menos 80px (40px de padding por cada lado)
        hero.setMaxWidth(Double.MAX_VALUE);
        hero.prefWidthProperty().bind(
                content.widthProperty().subtract(80)
        );

        content.getChildren().add(hero);

        this.setContent(content);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background: #ffffff;" +
                        "-fx-border-color: transparent;"
        );

        // En Inicio.java, después de agregar el hero:
        SectoresPrincipales sectores = new SectoresPrincipales();
        sectores.setMaxWidth(Double.MAX_VALUE);
        sectores.prefWidthProperty().bind(content.widthProperty().subtract(80));
        content.getChildren().add(sectores);

    }

}