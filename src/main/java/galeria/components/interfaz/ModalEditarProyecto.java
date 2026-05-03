package galeria.components.interfaz;

import galeria.model.Proyecto;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ModalEditarProyecto extends Stage {

    private Proyecto proyecto;

    // EL ERROR ESTABA AQUÍ: El constructor debe recibir el Proyecto
    public ModalEditarProyecto(Proyecto p) {
        this.proyecto = p;

        // Configuración básica de la ventana modal
        this.setTitle("Editar Proyecto: " + p.getTitulo());
        this.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana anterior

        // Aquí construyes tu interfaz de edición
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Editando: " + p.getTitulo()));

        Scene scene = new Scene(layout, 400, 500);
        this.setScene(scene);
    }
}