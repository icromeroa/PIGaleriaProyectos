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

    private static StackPane root; // Raíz para capas (Overlay: UI + Menús flotantes)
    private static BorderPane mainLayout; // Estructura base (Top: Navbar, Center: Vistas)
    private static Navbar navbarInstance;

    /**
     * Devuelve la instancia única y persistente del Navbar.
     * Esto evita que la animación de la línea azul se resetee al cambiar de vista.
     */
    public static Navbar getNavbar() {
        return navbarInstance;
    }

    /**
     * Actualiza solo los botones de sesión (Login/Hamburguesa)
     * sin reconstruir el objeto Navbar.
     */
    public static void actualizarNavbar() {
        if (navbarInstance != null) {
            navbarInstance.actualizarBotonSesion();
        }
    }

    @Override
    public void start(Stage stage) {
        // 1. Inicializar contenedores principales
        root = new StackPane();
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ffffff;");

        // 2. Crear la Navbar UNA SOLA VEZ
        navbarInstance = new Navbar();

        // 3. Configurar el Wrapper de la Navbar (Capa superior fija)
        StackPane navbarWrapper = new StackPane(navbarInstance);
        navbarWrapper.setPadding(new Insets(20, 40, 10, 40));
        navbarWrapper.setPickOnBounds(false); // Permite clics a través de las áreas transparentes

        mainLayout.setTop(navbarWrapper);
        mainLayout.setCenter(new Inicio()); // Vista inicial por defecto

        // 4. Montar la jerarquía en el StackPane 'root'
        root.getChildren().add(mainLayout);

        // 5. Agregar el Menú Overlay (Debe estar en root para flotar sobre el Center)
        // Usamos la instancia del menú que ya vive dentro de navbarInstance
        Node menuOverlay = navbarInstance.getMenu();
        root.getChildren().add(menuOverlay);
        StackPane.setAlignment(menuOverlay, Pos.TOP_RIGHT);
        StackPane.setMargin(menuOverlay, new Insets(85, 40, 0, 0));

        // 6. Configuración de Escena y CSS
        Scene scene = new Scene(root, 1280, 820);
        try {
            String css = getClass().getResource("/galeria/css/app.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo cargar el archivo CSS.");
        }

        stage.setTitle("UniRepo - Galería de Proyectos");
        stage.setScene(scene);
        stage.show();

        // 7. Animación de entrada de la aplicación
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Aplica desenfoque al fondo cuando el menú lateral o un modal se activa.
     */
    public static void aplicarEfectoBlur(boolean activar) {
        if (activar) {
            mainLayout.setEffect(new javafx.scene.effect.BoxBlur(10, 10, 3));
        } else {
            mainLayout.setEffect(null);
        }
    }

    /**
     * Cambia la vista principal sin afectar la Navbar superior.
     * Esto mantiene la fluidez de la línea animada de navegación.
     */
    public static void setView(Node nuevaVista) {
        if (mainLayout != null) {
            mainLayout.setCenter(nuevaVista);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}