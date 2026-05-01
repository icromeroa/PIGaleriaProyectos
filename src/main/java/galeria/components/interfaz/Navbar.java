package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.util.Animations;
import galeria.util.Colors;
import galeria.components.views.Inicio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;

public class Navbar extends HBox {
    private Label activeLabel = null;
    private Rectangle activeLine = null;

    public Navbar() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(12, 30, 12, 30));
        this.setSpacing(40);
        this.getStyleClass().add("glass-nav");

        // ---------- LOGO (Alineado con los links) ----------
        StackPane logoIcon = new StackPane();
        Circle bg = new Circle(16, Color.web("#3F68E4"));
        FontIcon icon = new FontIcon("fas-cube");
        icon.setIconSize(16);
        icon.setIconColor(Color.WHITE);
        logoIcon.getChildren().addAll(bg, icon);

        HBox logoText = new HBox(0);
        logoText.setAlignment(Pos.CENTER_LEFT); // Alineación corregida
        Label uni = new Label("Uni");
        uni.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 22px; -fx-text-fill: #1f2937;");
        Label repo = new Label("Repo");
        repo.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 22px; -fx-text-fill: #f97316;");
        logoText.getChildren().addAll(uni, repo);

        HBox logo = new HBox(10, logoIcon, logoText);
        logo.setAlignment(Pos.CENTER_LEFT);

        // ---------- LINKS ----------
        HBox links = new HBox(30);
        links.setAlignment(Pos.CENTER);

        links.setTranslateY(3);

        // Creamos los links. El primero (Inicio) empieza activo por defecto.
        VBox link1 = createAnimatedLink("Inicio", false);
        VBox link2 = createAnimatedLink("Explorar Catálogo", false);
        VBox link3 = createAnimatedLink("Sobre Nosotras", false);

        links.getChildren().addAll(link1, link2, link3);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ---------- LOGIN ----------
        FontIcon userIcon = new FontIcon("far-user");
        userIcon.setIconColor(Color.WHITE); // Icono blanco como el texto
        userIcon.setIconSize(14);

        MFXButton loginBtn = new MFXButton("Iniciar Sesión", userIcon);
        loginBtn.setGraphicTextGap(10);
        loginBtn.setStyle(
                "-fx-background-color: #3F68E4; -fx-text-fill: white; " +
                        "-fx-font-family: 'Manrope SemiBold'; -fx-background-radius: 25; " +
                        "-fx-padding: 10 25; -fx-cursor: hand;"
        );

        this.getChildren().addAll(logo, links, spacer, loginBtn);
    }

    private VBox createAnimatedLink(String text, boolean isActive) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Manrope Medium'; -fx-font-size: 15px; -fx-cursor: hand;");
        label.setTextFill(isActive ? Color.web("#3F68E4") : Color.web("#4b5563"));

        // La línea de abajo (invisible al inicio si no está activo)
        Rectangle line = new Rectangle(0, 2, Color.web("#3F68E4"));
        line.setArcHeight(2);
        line.setArcWidth(2);

        if (isActive) {
            line.setWidth(40); // Ancho inicial para el activo
            activeLabel = label;
            activeLine = line;
        }

        VBox container = new VBox(2, label, line);
        container.setAlignment(Pos.CENTER);

        // EVENTOS
        container.setOnMouseClicked(e -> {
            // Desactivar anterior
            if (activeLabel != null) {
                activeLabel.setTextFill(Color.web("#4b5563"));
                Animations.lineShrink(activeLine);
            }
            // Activar este
            label.setTextFill(Color.web("#3F68E4"));
            Animations.lineExpand(line, label.getWidth());
            activeLabel = label;
            activeLine = line;

            if (text.equals("Inicio")) MainApp.setView(new Inicio());
        });

        // Hover suave solo si no es el activo
        container.setOnMouseEntered(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#3F68E4"));
        });
        container.setOnMouseExited(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#4b5563"));
        });

        return container;
    }
}