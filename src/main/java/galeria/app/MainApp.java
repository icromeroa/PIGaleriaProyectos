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
import java.util.Stack;

public class MainApp extends Application {

    private static StackPane root;
    private static BorderPane mainLayout;
    private static Navbar navbarInstance;

    // Historial para permitir la navegación hacia atrás
    private static final Stack<Node> historialVistas = new Stack<>();

    public static Navbar getNavbar() {
        return navbarInstance;
    }

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

        // Vista inicial
        setView(new Inicio());

        root.getChildren().add(mainLayout);

        Node menuOverlay = navbarInstance.getMenu();
        root.getChildren().add(menuOverlay);
        StackPane.setAlignment(menuOverlay, Pos.TOP_RIGHT);
        StackPane.setMargin(menuOverlay, new Insets(85, 40, 0, 0));

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

        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Cambia la vista principal y guarda la vista anterior en el historial.
     */
    public static void setView(Node nuevaVista) {
        if (mainLayout != null) {
            // Si ya hay una vista en el centro, la guardamos en el historial
            if (mainLayout.getCenter() != null) {
                historialVistas.push(mainLayout.getCenter());
            }
            mainLayout.setCenter(nuevaVista);
        }
    }

    /**
     * Regresa a la vista anterior si existe en el historial.
     */
    public static void back() {
        if (!historialVistas.isEmpty()) {
            Node vistaAnterior = historialVistas.pop();
            // Usamos el método directo de BorderPane para no volver a pushear al historial
            mainLayout.setCenter(vistaAnterior);
        } else {
            // Si el historial está vacío, podemos decidir ir al Inicio por defecto
            mainLayout.setCenter(new Inicio());
        }
    }

    public static void aplicarEfectoBlur(boolean activar) {
        if (activar) {
            mainLayout.setEffect(new javafx.scene.effect.BoxBlur(10, 10, 3));
        } else {
            mainLayout.setEffect(null);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}