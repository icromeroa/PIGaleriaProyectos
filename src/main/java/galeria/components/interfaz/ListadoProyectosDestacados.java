package galeria.components.interfaz;

import galeria.dao.ProyectoDAO;
import galeria.model.Proyecto;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;

/**
 * Componente que muestra las listas de proyectos recientes y top con iconos vectoriales.
 * Ubicado en galeria.components.interfaz según la estructura UniRepoFX.
 */
public class ListadoProyectosDestacados extends HBox {
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final ScrollPane parentScroll;

    public ListadoProyectosDestacados(ScrollPane scroll) {
        this.parentScroll = scroll;
        setSpacing(50);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);

        // Color crema suave de fondo para resaltar sobre el blanco del Inicio
        setStyle("-fx-background-color: #f3ede4; -fx-background-radius: 30;");

        // Configuración de datos desde el DAO
        List<Proyecto> recientes = proyectoDAO.listarTodos();
        List<Proyecto> topProyectos = proyectoDAO.listarTopPorVistas(3);

        // Columna Izquierda: Subidos Recientemente (Icono: Clock)
        VBox colRecientes = crearColumna("Subidos Recientemente", FontAwesomeSolid.CLOCK,
                recientes.size() > 3 ? recientes.subList(0, 3) : recientes, "#3F68E4");

        // Columna Derecha: Top Proyectos (Icono: Trophy)
        VBox colTop = crearColumna("Top Proyectos", FontAwesomeSolid.TROPHY, topProyectos, "#f97316");

        getChildren().addAll(colRecientes, colTop);
        HBox.setHgrow(colRecientes, Priority.ALWAYS);
        HBox.setHgrow(colTop, Priority.ALWAYS);

        // Activación de la animación mediante scroll en el componente padre
        Animations.animateOnScroll(this, parentScroll, () -> {
            // Animamos primero los títulos
            Animations.slideUpFadeIn(colRecientes.getChildren().get(0), 0);
            Animations.slideUpFadeIn(colTop.getChildren().get(0), 0);

            // Animamos las tarjetas de forma escalonada
            for (int i = 1; i < colRecientes.getChildren().size(); i++) {
                Animations.revealProjectCard(colRecientes.getChildren().get(i), i * 150);
            }
            for (int i = 1; i < colTop.getChildren().size(); i++) {
                Animations.revealProjectCard(colTop.getChildren().get(i), i * 150);
            }
        });
    }

    private VBox crearColumna(String titulo, FontAwesomeSolid icono, List<Proyecto> proyectos, String colorIcono) {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_LEFT);

        // Configuración del Header de la columna con Ikonli
        FontIcon icon = new FontIcon(icono);
        icon.setIconSize(22);
        icon.setIconColor(Color.web(colorIcono));

        Label lblTitulo = new Label(titulo);
        lblTitulo.setGraphic(icon);
        lblTitulo.setGraphicTextGap(12);
        // Uso estricto de Manrope Bold según la corrección de diseño
        lblTitulo.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 20; -fx-text-fill: #1f2937;");

        vbox.getChildren().add(lblTitulo);

        for (Proyecto p : proyectos) {
            vbox.getChildren().add(crearTarjeta(p, colorIcono));
        }
        return vbox;
    }

    private HBox crearTarjeta(Proyecto p, String colorAccent) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 20; " +
                "-fx-border-color: #e5e7eb; -fx-border-radius: 20; -fx-cursor: hand;");

        // Icono representativo para cada proyecto (usando FILE_ALT de FontAwesome)
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(50, 50);
        iconContainer.setStyle("-fx-background-color: " + colorAccent + "22; -fx-background-radius: 12;"); // Color con opacidad

        FontIcon projectIcon = new FontIcon(FontAwesomeSolid.FILE_ALT);
        projectIcon.setIconSize(20);
        projectIcon.setIconColor(Color.web(colorAccent));
        iconContainer.getChildren().add(projectIcon);

        VBox info = new VBox(4);
        Label title = new Label(p.getTitulo());
        title.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 15; -fx-text-fill: #1f2937;");
        title.setWrapText(true);

        Label meta = new Label("Por: Autor • Systems Engineering");
        meta.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 12; -fx-text-fill: #6b7280;");

        info.getChildren().addAll(title, meta);
        card.getChildren().addAll(iconContainer, info);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Reutilización de la animación de hover definida en galeria.util.Animations
        Animations.attachHoverLift(card);

        return card;
    }
}