package galeria.components.interfaz;

import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;

/**
 * Componente Navbar construido totalmente en Java.
 * Gestiona el logo, los links de navegación y el botón de acceso.
 */
public class Navbar extends HBox {

    public Navbar() {
        // 1. Configuración de la barra principal
        this.getStyleClass().add("navbar");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(15, 30, 15, 30));
        this.setSpacing(35);
        this.setStyle("-fx-background-color: #ffffff; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");

        // ---------- SECCIÓN LOGO ----------
        StackPane logoContainer = new StackPane();

        Circle fondoLogo = new Circle(18);
        fondoLogo.setFill(Color.web("#f97316")); // Naranja vibrante

// Esta es la forma infalible si el símbolo no resuelve:
        FontIcon iconoCubo = new FontIcon("fas-cube");
        iconoCubo.setIconSize(20);
        iconoCubo.setIconColor(Color.WHITE);

        logoContainer.getChildren().addAll(fondoLogo, iconoCubo);

        // Animación constante de pulso para el logo
        Animations.attachPulse(logoContainer);

        Label textoUni = new Label("Uni");
        textoUni.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        Label textoRepo = new Label("Repo");
        textoRepo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f97316;");

        HBox marca = new HBox(8, logoContainer, new HBox(0, textoUni, textoRepo));
        marca.setAlignment(Pos.CENTER_LEFT);

        // ---------- LINKS DE NAVEGACIÓN ----------
        HBox menuLinks = new HBox(30);
        menuLinks.setAlignment(Pos.CENTER);
        menuLinks.getChildren().addAll(
                crearEnlace("Inicio", true),
                crearEnlace("Explorar", false),
                crearEnlace("Sobre Nosotros", false)
        );

        // ---------- ESPACIADOR ----------
        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        // ---------- BOTÓN DE ACCIÓN ----------
        Button botonLogin = new Button("Iniciar Sesión");
        botonLogin.setStyle(
                "-fx-background-color: #1f2937; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 8;"
        );

        // Aplicar animación de levantamiento y sombra al botón
        Animations.attachHoverLift(botonLogin);

        // 2. Ensamblar todos los elementos en la Navbar
        this.getChildren().addAll(marca, menuLinks, espaciador, botonLogin);
    }

    /**
     * Crea un Label que funciona como link con efectos de hover.
     */
    private Label crearEnlace(String titulo, boolean esActivo) {
        Label link = new Label(titulo);

        String estiloBase = "-fx-font-size: 15px; -fx-font-weight: 500; -fx-cursor: hand; ";
        String colorNormal = esActivo ? "-fx-text-fill: #f97316;" : "-fx-text-fill: #6b7280;";

        link.setStyle(estiloBase + colorNormal);

        if (!esActivo) {
            link.setOnMouseEntered(e -> {
                link.setStyle(estiloBase + "-fx-text-fill: #f97316;");
                // Podrías añadir una pequeña animación aquí
            });
            link.setOnMouseExited(e -> {
                link.setStyle(estiloBase + colorNormal);
            });
        }

        return link;
    }
}