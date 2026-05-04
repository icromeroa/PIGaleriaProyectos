package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.components.views.SobreNosotras;
import galeria.util.Animations;
import galeria.util.Sesion;
import galeria.components.views.Inicio;
import galeria.components.views.Login;
import galeria.components.views.Catalogo;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import io.github.palexdev.materialfx.controls.MFXButton;

public class Navbar extends HBox {
    private Label activeLabel = null;
    private Rectangle activeLine = null;
    private final HBox sessionContainer = new HBox();
    private final Menu menu = new Menu();

    public Navbar() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(12, 30, 12, 30));
        this.setSpacing(40);
        this.getStyleClass().add("glass-nav");

        // ---------- LOGO ----------
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
        logo.setCursor(javafx.scene.Cursor.HAND);
        // Al dar clic al logo, volvemos a Inicio con animación
        logo.setOnMouseClicked(e -> {
            VBox inicioLink = findLink("Inicio");
            if (inicioLink != null) navegar("Inicio", new Inicio(), inicioLink);
        });

        // ---------- LINKS ----------
        HBox links = new HBox(30);
        links.setAlignment(Pos.CENTER);
        links.setTranslateY(4);

        links.getChildren().addAll(
                createAnimatedLink("Inicio", true),
                createAnimatedLink("Explorar Catálogo", false),
                createAnimatedLink("Sobre Nosotras", false)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        sessionContainer.setAlignment(Pos.CENTER_RIGHT);
        actualizarBotonSesion();

        this.getChildren().addAll(logo, links, spacer, sessionContainer);
    }

    private VBox createAnimatedLink(String text, boolean isActive) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Manrope Medium'; -fx-font-size: 15px; -fx-cursor: hand;");
        label.setTextFill(isActive ? Color.web("#3F68E4") : Color.web("#4b5563"));

        Rectangle line = new Rectangle(0, 2, Color.web("#3F68E4"));
        line.setArcHeight(2);
        line.setArcWidth(2);

        VBox container = new VBox(2, label, line);
        container.setAlignment(Pos.CENTER);

        if (isActive) {
            activeLabel = label;
            activeLine = line;
            Platform.runLater(() -> Animations.lineExpand(line, label.getWidth()));
        }

        container.setOnMouseClicked(e -> {
            // Definimos la vista según el texto
            Node vista = switch (text) {
                case "Inicio" -> new Inicio();
                case "Explorar Catálogo" -> new Catalogo();
                case "Sobre Nosotras" -> new SobreNosotras();
                default -> null; // Sobre Nosotras u otros
            };
            // Llamamos a navegar (se ejecutará la animación aunque vista sea null)
            navegar(text, vista, container);
        });

        container.setOnMouseEntered(e -> { if (activeLabel != label) label.setTextFill(Color.web("#3F68E4")); });
        container.setOnMouseExited(e -> { if (activeLabel != label) label.setTextFill(Color.web("#4b5563")); });

        return container;
    }

    private void navegar(String texto, Node nuevaVista, VBox container) {
        Label proximoLabel = (Label) container.getChildren().get(0);
        Rectangle proximaLinea = (Rectangle) container.getChildren().get(1);

        if (activeLabel == proximoLabel) return;

        // 1. CAPTURAR ESTADO ANTERIOR PARA LA ANIMACIÓN
        Label labelViejo = activeLabel;
        Rectangle lineaVieja = activeLine;

        // 2. ACTUALIZAR ESTADO ACTUAL
        activeLabel = proximoLabel;
        activeLine = proximaLinea;

        // 3. DISPARAR ANIMACIONES DE NAVBAR (Hilo de UI - Instantáneo)
        if (labelViejo != null) {
            labelViejo.setTextFill(Color.web("#4b5563"));
            Animations.lineShrink(lineaVieja);
        }
        proximoLabel.setTextFill(Color.web("#3F68E4"));
        Animations.lineExpand(proximaLinea, proximoLabel.getWidth());

        // 4. CARGAR VISTA CON UN PEQUEÑO DELAY (Para no congelar la línea)
        if (nuevaVista != null) {
            PauseTransition delay = new PauseTransition(Duration.millis(80));
            delay.setOnFinished(e -> {
                MainApp.setView(nuevaVista);

                // Si la nueva vista es un ScrollPane (como Inicio, Catalogo, etc.)
                if (nuevaVista instanceof javafx.scene.control.ScrollPane sp) {
                    Platform.runLater(() -> {
                        sp.setVvalue(0); // Forzar scroll al inicio
                        sp.requestFocus(); // Quitar el foco de cualquier botón interno
                    });
                }

                nuevaVista.setOpacity(0);
                FadeTransition ft = new FadeTransition(Duration.millis(300), nuevaVista);
                ft.setToValue(1.0);
                ft.play();
            });
            delay.play();
        }
    }

    private VBox findLink(String text) {
        HBox linksContainer = (HBox) this.getChildren().get(1);
        for (Node n : linksContainer.getChildren()) {
            if (n instanceof VBox vb) {
                Label lbl = (Label) vb.getChildren().get(0);
                if (lbl.getText().equals(text)) return vb;
            }
        }
        return null;
    }

    public void setLinkActivo(String textoLink) {
        VBox target = findLink(textoLink);
        if (target != null) {
            navegar(textoLink, null, target);
        }
    }

    public Menu getMenu() { return menu; }

    public void actualizarBotonSesion() {
        sessionContainer.getChildren().clear();
        if (Sesion.estaLogueado()) sessionContainer.getChildren().add(crearBotonHamburguesaPill());
        else sessionContainer.getChildren().add(crearBotonLoginOriginal());
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
        pill.setOnMouseClicked(e -> menu.toggle());
        return pill;
    }

    private MFXButton crearBotonLoginOriginal() {
        FontIcon userIcon = new FontIcon("far-user");
        userIcon.setIconColor(Color.WHITE);
        userIcon.setIconSize(14);
        MFXButton loginBtn = new MFXButton("Iniciar Sesión", userIcon);
        loginBtn.setGraphicTextGap(10);
        loginBtn.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; -fx-font-family: 'Manrope SemiBold'; -fx-background-radius: 25; -fx-padding: 10 25; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> MainApp.setView(new Login()));
        return loginBtn;
    }
}