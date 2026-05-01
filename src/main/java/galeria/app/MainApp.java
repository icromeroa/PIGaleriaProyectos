package galeria.app;

import galeria.components.interfaz.Navbar;
import galeria.components.views.Inicio; // Importamos tu vista de Inicio
import javafx.application.Application;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    // El contenedor principal es estático para que setView funcione desde fuera
    private static BorderPane root = new BorderPane();

    @Override
    public void start(Stage stage) {
        root.setStyle("-fx-background-color: #ffffff;");
        // 1. Instanciamos la Navbar (que está en galeria.components.interfaz)
        Navbar navbar = new Navbar();

        // Contenedor para el margen superior del Navbar
        StackPane navbarWrapper = new StackPane(navbar);
        navbarWrapper.setPadding(new Insets(20, 40, 10, 40));
        navbarWrapper.setStyle("-fx-background-color: transparent;");
        navbarWrapper.setPickOnBounds(false);
        // 2. Colocamos la Navbar fija arriba
        root.setTop(navbarWrapper);

        // 3. NO cargamos Inicio por defecto aquí para que esté vacío al abrir,
        // o puedes poner root.setCenter(new TuVistaDeLogin()); mas adelante.

        // 4. Configuración de la Escena
        Scene scene = new Scene(root, 1280, 820);

        try {
            String css = getClass().getResource("/galeria/css/app.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Error: No se encontró el archivo CSS en resources/galeria/css/app.css");
        }

        stage.setTitle("UniRepo - Galería de Proyectos");
        stage.setScene(scene);
        stage.show();

        // Animación de entrada suave
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Este método es el que usarás en el Navbar para cambiar la vista.
     */
    public static void setView(Node nuevaVista) {
        root.setCenter(nuevaVista);
        root.requestLayout(); // <--- Esto obliga a la app a refrescarse visualmente
    }

    public static void main(String[] args) {
        launch(args);
    }
}