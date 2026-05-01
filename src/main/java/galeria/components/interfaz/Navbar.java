package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.util.Animations;
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
        // Configuración del contenedor principal (Navbar)
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

        // ---------- LINKS (CON AJUSTE DE ALINEACIÓN) ----------
        HBox links = new HBox(30);
        links.setAlignment(Pos.CENTER);

        // Bajamos los links un poco para que se alineen visualmente con el texto del Logo
        links.setTranslateY(4);

        // Creamos los links. El primero (Inicio) empieza activo por defecto.
        VBox link1 = createAnimatedLink("Inicio", false);
        VBox link2 = createAnimatedLink("Explorar Catálogo", false);
        VBox link3 = createAnimatedLink("Sobre Nosotras", false);

        links.getChildren().addAll(link1, link2, link3);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ---------- LOGIN (CON CORRECCIÓN DE RIPPLE CUADRADO) ----------
        FontIcon userIcon = new FontIcon("far-user");
        userIcon.setIconColor(Color.WHITE); // Icono blanco
        userIcon.setIconSize(14);

        MFXButton loginBtn = new MFXButton("Iniciar Sesión", userIcon);
        loginBtn.setGraphicTextGap(10);

        // Estilo del botón
        loginBtn.setStyle(
                "-fx-background-color: #3F68E4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Manrope SemiBold'; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-padding: 10 25; " +
                        "-fx-cursor: hand;"
        );

        // SOLUCIÓN AL RIPPLE CUADRADO: Aplicamos un Clip al botón
        Rectangle mask = new Rectangle();
        mask.setArcWidth(50); // Valor alto para asegurar redondeo perfecto
        mask.setArcHeight(50);
        mask.widthProperty().bind(loginBtn.widthProperty());
        mask.heightProperty().bind(loginBtn.heightProperty());
        loginBtn.setClip(mask);

        // Configuramos el color de la onda de clic a blanco sutil
        loginBtn.setRippleColor(Color.web("#ffffff", 0.3));

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

        // Si es el link activo inicialmente, calculamos su ancho
        if (isActive) {
            // Un valor aproximado inicial ya que el ancho real se calcula al renderizar
            line.setWidth(45);
            activeLabel = label;
            activeLine = line;
        }

        VBox container = new VBox(2, label, line);
        container.setAlignment(Pos.CENTER);

        // EVENTOS DE CLIC Y ANIMACIÓN
        container.setOnMouseClicked(e -> {
            // 1. Desactivar el link que estaba activo antes
            if (activeLabel != null && activeLabel != label) {
                activeLabel.setTextFill(Color.web("#4b5563"));
                Animations.lineShrink(activeLine);
            }

            // 2. Activar este link
            if (activeLabel != label) {
                label.setTextFill(Color.web("#3F68E4"));
                Animations.lineExpand(line, label.getWidth());
                activeLabel = label;
                activeLine = line;
            }

            // 3. Navegación
            if (text.equals("Inicio")) MainApp.setView(new Inicio());
        });

        // Hover suave (solo cambia el color si no está activo)
        container.setOnMouseEntered(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#3F68E4"));
        });

        container.setOnMouseExited(e -> {
            if (activeLabel != label) label.setTextFill(Color.web("#4b5563"));
        });

        return container;
    }
}