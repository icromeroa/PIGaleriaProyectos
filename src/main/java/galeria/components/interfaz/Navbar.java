package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.util.Animations;
import galeria.util.Sesion;
import galeria.components.views.Inicio;
import galeria.components.views.Login;
import galeria.components.views.Catalogo;
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
    private final HBox sessionContainer = new HBox(); // Contenedor para Login o Menú
    private final Menu menu = new Menu(); // Instancia del menú overlay

    public Navbar() {
        // Configuración del contenedor principal (Navbar)
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(12, 30, 12, 30));
        this.setSpacing(40);
        this.getStyleClass().add("glass-nav");

        // ---------- LOGO (TU DISEÑO ORIGINAL) ----------
        StackPane logoIcon = new StackPane();
        Circle bg = new Circle(16, Color.web("#3F68E4"));
        FontIcon icon = new FontIcon("fas-cube");
        icon.setIconSize(16);
        icon.setIconColor(Color.WHITE);
        logoIcon.getChildren().addAll(bg, icon);

        HBox logoText = new HBox(0);
        logoText.setAlignment(Pos.CENTER_LEFT);
        Label uni = new Label("Uni");
        uni.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 22px; -fx-text-fill: #1f2937;");
        Label repo = new Label("Repo");
        repo.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 22px; -fx-text-fill: #f97316;");
        logoText.getChildren().addAll(uni, repo);

        HBox logo = new HBox(10, logoIcon, logoText);
        logo.setAlignment(Pos.CENTER_LEFT);

        // ---------- LINKS (TU DISEÑO ORIGINAL) ----------
        HBox links = new HBox(30);
        links.setAlignment(Pos.CENTER);
        links.setTranslateY(4);

        VBox link1 = createAnimatedLink("Inicio", true);
        VBox link2 = createAnimatedLink("Explorar Catálogo", false);
        VBox link3 = createAnimatedLink("Sobre Nosotras", false);

        links.getChildren().addAll(link1, link2, link3);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ---------- CONTENEDOR DE SESIÓN DINÁMICO ----------
        sessionContainer.setAlignment(Pos.CENTER_RIGHT);
        actualizarBotonSesion();

        this.getChildren().addAll(logo, links, spacer, sessionContainer);
    }

    /**
     * Devuelve la instancia del menú para que MainApp pueda ponerlo en el StackPane superior
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Cambia entre el botón de "Iniciar Sesión" y el botón de "Menú/Perfil"
     */
    public void actualizarBotonSesion() {
        sessionContainer.getChildren().clear();

        if (Sesion.estaLogueado()) {
            sessionContainer.getChildren().add(crearBotonHamburguesaPill());
        } else {
            sessionContainer.getChildren().add(crearBotonLoginOriginal());
        }
    }

    private StackPane crearBotonHamburguesaPill() {
        StackPane pill = new StackPane();
        pill.setPrefSize(85, 45);
        pill.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        Circle circuloAzul = new Circle(18, Color.web("#3F68E4"));
        FontIcon iconBars = new FontIcon("fas-bars");
        iconBars.setIconSize(15);
        iconBars.setIconColor(Color.WHITE);

        StackPane innerCircle = new StackPane(circuloAzul, iconBars);
        StackPane.setAlignment(innerCircle, Pos.CENTER_RIGHT);
        StackPane.setMargin(innerCircle, new Insets(0, 5, 0, 0));

        pill.getChildren().add(innerCircle);

        // Al hacer clic, abre/cierra el menú flotante
        pill.setOnMouseClicked(e -> menu.toggle());

        return pill;
    }

    private MFXButton crearBotonLoginOriginal() {
        FontIcon userIcon = new FontIcon("far-user");
        userIcon.setIconColor(Color.WHITE);
        userIcon.setIconSize(14);

        MFXButton loginBtn = new MFXButton("Iniciar Sesión", userIcon);
        loginBtn.setGraphicTextGap(10);
        loginBtn.setStyle(
                "-fx-background-color: #3F68E4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Manrope SemiBold'; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-padding: 10 25; " +
                        "-fx-cursor: hand;"
        );

        // Tu máscara para el ripple cuadrado
        Rectangle mask = new Rectangle();
        mask.setArcWidth(50);
        mask.setArcHeight(50);
        mask.widthProperty().bind(loginBtn.widthProperty());
        mask.heightProperty().bind(loginBtn.heightProperty());
        loginBtn.setClip(mask);
        loginBtn.setRippleColor(Color.web("#ffffff", 0.3));

        loginBtn.setOnAction(e -> MainApp.setView(new Login()));

        return loginBtn;
    }

    private VBox createAnimatedLink(String text, boolean isActive) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Manrope Medium'; -fx-font-size: 15px; -fx-cursor: hand;");
        label.setTextFill(isActive ? Color.web("#3F68E4") : Color.web("#4b5563"));

        Rectangle line = new Rectangle(0, 2, Color.web("#3F68E4"));
        line.setArcHeight(2);
        line.setArcWidth(2);

        if (isActive) {
            line.setWidth(45);
            activeLabel = label;
            activeLine = line;
        }

        VBox container = new VBox(2, label, line);
        container.setAlignment(Pos.CENTER);

        container.setOnMouseClicked(e -> {
            if (activeLabel != null && activeLabel != label) {
                activeLabel.setTextFill(Color.web("#4b5563"));
                Animations.lineShrink(activeLine);
            }

            if (activeLabel != label) {
                label.setTextFill(Color.web("#3F68E4"));
                Animations.lineExpand(line, label.getWidth());
                activeLabel = label;
                activeLine = line;
            }

            // Lógica de Navegación
            switch (text) {
                case "Inicio":
                    MainApp.setView(new Inicio());
                    break;
                case "Explorar Catálogo":
                    MainApp.setView(new Catalogo());
                    break;
                case "Sobre Nosotras":
                    System.out.println("Navegando a Sobre Nosotras...");
                    break;
            }
        });

        container.setOnMouseEntered(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#3F68E4"));
        });

        container.setOnMouseExited(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#4b5563"));
        });

        return container;
    }
}