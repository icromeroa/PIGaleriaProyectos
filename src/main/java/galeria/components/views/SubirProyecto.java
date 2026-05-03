package galeria.components.views;

import galeria.model.Proyecto;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vista para subir o editar proyectos.
 */
public class SubirProyecto extends VBox {

    public SubirProyecto() {
        configurarVista("Subir Nuevo Proyecto");
    }

    // Constructor que recibe el proyecto para editar
    public SubirProyecto(Proyecto p) {
        configurarVista("Editando: " + p.getTitulo());
    }

    private void configurarVista(String mensaje) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: white; -fx-padding: 50;");

        Label lbl = new Label(mensaje);
        lbl.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        this.getChildren().add(lbl);
        System.out.println("LOG: Vista SubirProyecto abierta con éxito. Modo: " + mensaje);
    }
}