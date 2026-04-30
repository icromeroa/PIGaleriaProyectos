package galeria.app;

import galeria.components.interfaz.Navbar;
import javafx.application.Application;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // Usamos un BorderPane para poner la Navbar arriba (Top)
        BorderPane root = new BorderPane();

        // Instanciamos tu Navbar
        Navbar navbar = new Navbar();
        // Creamos un contenedor "falso" para darle márgenes externos
        StackPane navbarWrapper = new StackPane(navbar);
        navbarWrapper.setPadding(new Insets(20, 40, 0, 40)); // Arriba, Derecha, Abajo, Izquierda

        root.setTop(navbarWrapper);

        // Creamos la escena
        Scene scene = new Scene(root, 1280, 820);

        // IMPORTANTE: Cargar el CSS (asegúrate de que el archivo exista en resources)
        String css = getClass().getResource("/galeria/css/app.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("UniRepo - Galería de Proyectos");
        stage.setScene(scene);
        stage.show();

        // Efecto de Fade-in inicial como el del proyecto de la derecha
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(700), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}