package galeria.components.views;

import galeria.components.interfaz.Navbar;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainLayout extends BorderPane {

    public MainLayout() {
        // 1. Instanciamos la Navbar global
        Navbar navbar = new Navbar();

        // 2. Para que "flote" como en la imagen 2, la envolvemos en un StackPane con Padding
        StackPane headerWrapper = new StackPane(navbar);
        headerWrapper.setPadding(new Insets(20, 40, 10, 40)); // Mucho aire arriba y a los lados

        // 3. La fijamos en la parte superior
        this.setTop(headerWrapper);

        // 4. Por defecto, mostramos la pantalla de Inicio
        // setCenter(new InicioView());
    }

    // Este método lo usarás para cambiar de página
    public void cargarPagina(Node nuevaPagina) {
        this.setCenter(nuevaPagina);
    }
}