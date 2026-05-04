package galeria.components.views;

import galeria.app.MainApp;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class Categorizacion extends ScrollPane {

    public Categorizacion() {
        // Configuración del ScrollPane para que sea blanco total
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: white; -fx-background: white; -fx-border-color: white;");

        VBox content = new VBox(30);
        content.setPadding(new Insets(40, 100, 40, 100));
        content.setAlignment(Pos.TOP_LEFT);
        content.setStyle("-fx-background-color: white;");

        // Botón de regreso rápido
        Button btnBack = new Button(" Volver a Subir Proyecto");
        btnBack.setGraphic(new FontIcon("fas-arrow-left"));
        btnBack.setStyle("-fx-background-color: transparent; -fx-text-fill: #1E3A8A; -fx-font-family: 'Manrope'; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnBack.setCursor(Cursor.HAND);
        btnBack.setOnAction(e -> MainApp.back());
        Animations.attachHoverLift(btnBack);

        // Título de la vista
        VBox header = new VBox(10);
        Label title = new Label("Gestión de Categorización");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");

        Label description = new Label("Desde aquí podrás administrar las facultades, materias, programas y semestres del sistema.");
        description.setStyle("-fx-text-fill: #64748B; -fx-font-size: 15px; -fx-font-family: 'Manrope';");

        header.getChildren().addAll(btnBack, title, description);

        // Placeholder para el contenido de gestión
        VBox placeholder = new VBox();
        placeholder.setMinHeight(400);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-border-color: #E2E8F0; -fx-border-style: dashed; -fx-border-radius: 20; -fx-border-width: 2;");

        Label lblInfo = new Label("Módulo de administración en construcción...");
        lblInfo.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 16px; -fx-font-family: 'Manrope';");
        placeholder.getChildren().add(lblInfo);

        content.getChildren().addAll(header, placeholder);

        this.setContent(new StackPane(content));

        // Animación de entrada
        Animations.slideUpFadeIn(content, 100);
    }
}