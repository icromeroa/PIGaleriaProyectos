package galeria.components.interfaz;

import galeria.util.Animations;
import galeria.util.Colors; // Importamos nuevas variables
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class Navbar extends HBox {

    public Navbar() {
        // 1. Estilo de la Barra (Forma de Píldora)
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setSpacing(30);

        // El fondo azul clarito y redondeado de la imagen
        // Para que sea dinámico según tu clase Colors:
        String fondo = "#" + Colors.FONDO_NAV.toString().substring(2, 8);
        this.setStyle("-fx-background-color: " + fondo + "; " +
                "-fx-background-radius: 50; " +
                "-fx-margin: 10 20 10 20;");

        // ---------- LOGO ----------
        StackPane logoBox = new StackPane();
        Circle logoBg = new Circle(12, Colors.PRINCIPAL); // Usamos variable

        FontIcon cubeIcon = new FontIcon("fas-cube");
        cubeIcon.setIconSize(12);
        cubeIcon.setIconColor(Colors.BLANCO);
        logoBox.getChildren().addAll(logoBg, cubeIcon);

        logoBox.setTranslateY(-5);

        Label uni = new Label("Uni");
        uni.setStyle("-fx-font-size: 20px; -fx-font-weight: 900;");
        Label repo = new Label("Repo");
        repo.setTextFill(Colors.ACCENTO);
        repo.setStyle("-fx-font-size: 20px; -fx-font-weight: 900;");

        HBox logo = new HBox(8, logoBox, new HBox(0, uni, repo));
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setTranslateY(2);

        // ---------- LINKS ----------
        HBox links = new HBox(25);
        links.setAlignment(Pos.CENTER);
        links.getChildren().addAll(
                navLink("Inicio", true),
                navLink("Explorar Catálogo", false),
                navLink("Sobre Nosotros", false)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ---------- BOTÓN LOGIN (AZUL Y REDONDEADO) ----------
        FontIcon userIcon = new FontIcon(FontAwesomeSolid.USER_CIRCLE);
        userIcon.setIconColor(Colors.BLANCO);
        userIcon.setIconSize(18);

        Button loginBtn = new Button("Iniciar Sesión", userIcon);
        loginBtn.setGraphicTextGap(10);
        loginBtn.setStyle(
                "-fx-background-color: #3F68E4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: medium; " +
                        "-fx-background-radius: 30; " +
                        "-fx-padding: 8 22;"
        );

        Animations.attachHoverLift(loginBtn);

        this.getChildren().addAll(logo, links, spacer, loginBtn);
    }

    private Label navLink(String text, boolean active) {
        Label l = new Label(text);
        l.setTextFill(active ? Colors.ACCENTO : Colors.TEXTO_OSCURO);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: medium; -fx-cursor: hand;");

        if(!active) {
            l.setOnMouseEntered(e -> l.setTextFill(Colors.ACCENTO));
            l.setOnMouseExited(e -> l.setTextFill(Colors.TEXTO_OSCURO));
        }
        return l;
    }
}