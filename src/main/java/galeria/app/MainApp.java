package galeria.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // Etiqueta de prueba
        Label label = new Label("¡JavaFX funciona! 🎉");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Contenedor
        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: #0a0b14;");

        // Escena
        Scene scene = new Scene(root, 600, 400);

        // Ventana
        stage.setTitle("UniRepo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}