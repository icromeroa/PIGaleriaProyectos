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

    // Hacemos el root estático o accesible si necesitas cambiar de vista desde otros controladores
    private static BorderPane root = new BorderPane();

    @Override
    public void start(Stage stage) {
        // 1. Configuración de la Navbar Global
        Navbar navbar = new Navbar();

        // Contenedor para darle el aire "flotante" de la imagen 2
        StackPane navbarWrapper = new StackPane(navbar);
        navbarWrapper.setPadding(new Insets(20, 40, 10, 40));

        // 2. Integración en el "Marco" (BorderPane)
        root.setTop(navbarWrapper);

        // Aquí es donde irán tus vistas (Inicio, Catálogo, etc.)
        // Por ahora lo dejamos vacío o con un placeholder
        // root.setCenter(new InicioView());

        // 3. Configuración de la Escena
        Scene scene = new Scene(root, 1280, 820);

        // Carga de estilos globales (Fonts y CSS)
        try {
            String css = getClass().getResource("/galeria/css/app.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo CSS. Verifica la ruta en resources.");
        }

        // 4. Configuración del Stage
        stage.setTitle("UniRepo - Galería de Proyectos");
        stage.setScene(scene);
        stage.show();

        // 5. Animación de entrada (Fade In)
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Método útil para cambiar el contenido central desde cualquier parte de la app.
     * @param nuevaVista El componente (Node) que quieres mostrar.
     */
    public static void setView(javafx.scene.Node nuevaVista) {
        root.setCenter(nuevaVista);
    }

    public static void main(String[] args) {
        launch(args);
    }
}