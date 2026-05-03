package galeria.app;

import galeria.components.interfaz.Navbar;
import galeria.components.views.Inicio;
import javafx.application.Application;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    private static StackPane root; // Raíz para capas (Overlay)
    private static BorderPane mainLayout; // Capa 0: UI Normal
    private static Navbar navbarInstance;

    public static void actualizarNavbar() {
        if (navbarInstance != null) {
            navbarInstance.actualizarBotonSesion();
        }
    }

    @Override
    public void start(Stage stage) {
        root = new StackPane();
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ffffff;");

        navbarInstance = new Navbar();

        StackPane navbarWrapper = new StackPane(navbarInstance);
        navbarWrapper.setPadding(new Insets(20, 40, 10, 40));
        navbarWrapper.setPickOnBounds(false);

        mainLayout.setTop(navbarWrapper);
        mainLayout.setCenter(new Inicio()); // Cargamos inicio por defecto

        root.getChildren().add(mainLayout);

        // El menú se agrega aquí al final para que esté por encima de todo
        root.getChildren().add(navbarInstance.getMenu());
        StackPane.setAlignment(navbarInstance.getMenu(), Pos.TOP_RIGHT);
        StackPane.setMargin(navbarInstance.getMenu(), new Insets(85, 40, 0, 0));

        Scene scene = new Scene(root, 1280, 820);
        try {
            String css = getClass().getResource("/galeria/css/app.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Error: No se encontró el archivo CSS");
        }

        stage.setTitle("UniRepo - Galería de Proyectos");
        stage.setScene(scene);
        stage.show();

        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

    }
    // Añade esto a MainApp.java
    public static void aplicarEfectoBlur(boolean activar) {
        if (activar) {
            root.setEffect(new javafx.scene.effect.BoxBlur(10, 10, 3));
        } else {
            root.setEffect(null);
        }
    }

    public static void setView(Node nuevaVista) {
        mainLayout.setCenter(nuevaVista);
    }

    public static void main(String[] args) {
        launch(args);
    }
}