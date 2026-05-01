package galeria.components.views;

import galeria.components.interfaz.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Vista principal de la aplicación UniRepo.
 * Organiza los componentes de la landing page con soporte para scroll y animaciones.
 */
public class Inicio extends ScrollPane {

    public Inicio() {
        // Contenedor principal sin padding horizontal para permitir que el Footer se expanda
        VBox content = new VBox(50);
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #ffffff;");

        // 1. Instanciación de componentes
        Hero hero = new Hero();
        SectoresPrincipales sectores = new SectoresPrincipales();
        ProyectosDestacados destacados = new ProyectosDestacados();
        Estadisticas estadisticas = new Estadisticas();

        // Componente nuevo con iconos Ikonli y lógica de scroll
        ListadoProyectosDestacados listadoEspecial = new ListadoProyectosDestacados(this);

        // Footer que se expandirá al 100% del ancho
        Footer footer = new Footer();

        // 2. Configuración de anchos y márgenes (80px de aire a los lados para el contenido)
        double margenLateral = 80.0;

        configurarComponente(hero, content, margenLateral);
        configurarComponente(sectores, content, margenLateral);
        configurarComponente(destacados, content, margenLateral);
        configurarComponente(estadisticas, content, margenLateral);
        configurarComponente(listadoEspecial, content, margenLateral);

        // 3. El Footer no lleva margen lateral para que su borde/fondo toque los extremos
        footer.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(footer, new Insets(40, 0, 0, 0)); // Solo margen superior para separarlo

        // 4. Agregar todos los elementos al VBox en orden lógico
        content.getChildren().addAll(
                hero,
                sectores,
                destacados,
                estadisticas,
                listadoEspecial,
                footer
        );

        // 5. Configuración del ScrollPane
        this.setContent(content);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Estilo para eliminar bordes por defecto del ScrollPane y asegurar fondo blanco
        this.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background: #ffffff;" +
                        "-fx-border-color: transparent;"
        );
    }

    /**
     * Método auxiliar para estandarizar el comportamiento de los componentes hijos
     * y aplicar los márgenes laterales requeridos por el diseño de UniRepo.
     */
    private void configurarComponente(javafx.scene.layout.Region nodo, VBox parent, double margin) {
        nodo.setMaxWidth(Double.MAX_VALUE);
        // El ancho preferido se adapta al contenedor menos el margen doble (izq + der)
        nodo.prefWidthProperty().bind(parent.widthProperty().subtract(margin * 2));
        VBox.setMargin(nodo, new Insets(0, margin, 0, margin));
    }
}