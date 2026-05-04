package galeria.components.views;

import galeria.components.interfaz.CardProyecto;
import galeria.dao.GuardadoDAO;
import galeria.model.Proyecto;
import galeria.util.Animations; // Importamos las animaciones
import galeria.util.CardStyle;
import galeria.util.Sesion;
import javafx.application.Platform; // Necesario para disparar la animación al cargar
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.List;

public class Guardados extends ScrollPane {
    private GuardadoDAO guardadoDAO = new GuardadoDAO();

    public Guardados() {
        // Configuración estética del ScrollPane
        this.setFitToWidth(true);
        this.setPannable(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC; -fx-border-color: transparent;");

        VBox root = new VBox(30);
        root.setPadding(new Insets(40, 60, 40, 60));
        root.setStyle("-fx-background-color: #F8FAFC;");

        // Cabecera de la sección
        VBox header = new VBox(10);
        Label lblTitulo = new Label("Mis Proyectos Guardados");
        lblTitulo.setStyle("-fx-font-size: 32; -fx-font-weight: 800; -fx-text-fill: #0F172A; -fx-font-family: 'Manrope';");

        Label lblSubtitulo = new Label("Administra y revisa las investigaciones y proyectos académicos que has marcado como\n" +
                "favoritos para tu referencia futura.");
        lblSubtitulo.setStyle("-fx-font-size: 14; -fx-text-fill: #64748B; -fx-font-family: 'Manrope';");

        header.getChildren().addAll(lblTitulo, lblSubtitulo);

        // Contenedor fluído para las tarjetas
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(25);
        flowPane.setVgap(25);
        flowPane.setAlignment(Pos.TOP_LEFT);

        // Lógica de carga
        if (Sesion.getUsuario() != null) {
            List<Proyecto> guardados = guardadoDAO.listarProyectosGuardados(Sesion.getUsuario().getIdUsuario());

            if (guardados.isEmpty()) {
                Label lblVacio = new Label("No has guardado ningún proyecto todavía.");
                lblVacio.setStyle("-fx-font-size: 16; -fx-text-fill: #94A3B8; -fx-font-style: italic;");
                flowPane.getChildren().add(lblVacio);
            } else {
                double delay = 200; // Retraso inicial para las tarjetas
                for (Proyecto p : guardados) {
                    CardProyecto card = new CardProyecto(p, CardStyle.NORMAL);
                    card.setPrefWidth(300);
                    flowPane.getChildren().add(card);

                    // Aplicamos animación individual escalonada a cada tarjeta
                    double finalDelay = delay;
                    Platform.runLater(() -> Animations.revealProjectCard(card, finalDelay));
                    delay += 100; // Incrementamos el delay para la siguiente tarjeta
                }
            }
        } else {
            flowPane.getChildren().add(new Label("Inicia sesión para ver tus proyectos guardados."));
        }

        root.getChildren().addAll(header, flowPane);
        this.setContent(root);

        // ANIMACIÓN DE ENTRADA PARA LA CABECERA
        Platform.runLater(() -> {
            Animations.slideUpFadeIn(header, 100);
        });
    }
}