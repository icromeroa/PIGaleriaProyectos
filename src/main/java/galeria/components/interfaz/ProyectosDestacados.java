package galeria.components.interfaz;

import galeria.dao.ProyectoDAO;
import galeria.model.Proyecto;
import galeria.util.Animations; // Importamos tus animaciones
import galeria.util.CardStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;

public class ProyectosDestacados extends VBox {

    public ProyectosDestacados(Runnable onExplorar) {
        setSpacing(25);
        setPadding(new Insets(30, 0, 30, 0));

        // Título
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(6, Color.web("#3F68E4"));
        Label lblSeccion = new Label("Proyectos Destacados");
        lblSeccion.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #3F68E4;");
        header.getChildren().addAll(dot, lblSeccion);

        // GRID BENTO CONFIGURACIÓN
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            grid.getRowConstraints().add(row);
        }

        ProyectoDAO dao = new ProyectoDAO();
        List<Proyecto> proyectos = dao.listarTopPorVistas(7);

        if (proyectos.size() >= 7) {
            grid.add(new CardProyecto(proyectos.get(0), CardStyle.DESTACADO), 0, 0, 2, 3);
            grid.add(new CardProyecto(proyectos.get(1), CardStyle.DESTACADO), 2, 0, 1, 3);
            grid.add(new CardProyecto(proyectos.get(2), CardStyle.DESTACADO), 3, 0, 1, 2);
            grid.add(new CardProyecto(proyectos.get(3), CardStyle.DESTACADO), 3, 2, 1, 1);
            grid.add(new CardProyecto(proyectos.get(4), CardStyle.DESTACADO), 0, 3, 1, 2);
            grid.add(new CardProyecto(proyectos.get(5), CardStyle.DESTACADO), 1, 3, 2, 2);
            grid.add(new CardProyecto(proyectos.get(6), CardStyle.DESTACADO), 3, 3, 1, 2);
        }

        // --- BOTÓN EXPLORAR CON TUS ANIMACIONES ---
        Button btnCat = new Button("Explorar catálogo de Proyectos");
        btnCat.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; " +
                "-fx-background-radius: 30; -fx-padding: 14 40; " +
                "-fx-font-family: 'Manrope Bold'; -fx-font-size: 15px; -fx-cursor: hand;");

        btnCat.setOnAction(e -> onExplorar.run());

        // 1. Aplicamos el efecto de elevación al pasar el mouse (Hover Lift)
        Animations.attachHoverLift(btnCat);

        HBox btnWrapper = new HBox(btnCat);
        btnWrapper.setAlignment(Pos.CENTER);
        btnWrapper.setPadding(new Insets(20, 0, 0, 0));

        // 2. Aplicamos la entrada suave (Slide Up) al cargar la vista
        // Le damos un delay de 800ms para que aparezca después de que el grid empiece a verse
        Animations.slideUpFadeIn(btnWrapper, 800);

        this.getChildren().addAll(header, grid, btnWrapper);
    }
}