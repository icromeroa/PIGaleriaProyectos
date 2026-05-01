package galeria.components.interfaz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Componente de pie de página para UniRepo.
 * Se ubica en galeria.components.interfaz.
 */
public class Footer extends HBox {

    public Footer() {
        // Configuración del contenedor principal
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30, 80, 30, 80));
        this.setStyle("-fx-background-color: #f8fafc; " + // Un gris muy claro casi blanco
                "-fx-border-color: #e2e8f0 transparent transparent transparent; " +
                "-fx-border-width: 1;");

        // 1. Copyright Text
        Label copyright = new Label("© 2026 UNIREPO - USB BOGOTÁ. ALL RIGHTS RESERVED.");
        copyright.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 11; -fx-text-fill: #64748b; -fx-letter-spacing: 0.5px;");

        // Espaciador elástico para empujar los links a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 2. Contenedor de Enlaces
        HBox linksContainer = new HBox(25);
        linksContainer.setAlignment(Pos.CENTER_RIGHT);

        linksContainer.getChildren().addAll(
                crearFooterLink("PRIVACY POLICY"),
                crearFooterLink("TERMS OF SERVICE"),
                crearFooterLink("INSTITUTIONAL ACCESS"),
                crearFooterLink("API DOCUMENTATION")
        );

        this.getChildren().addAll(copyright, spacer, linksContainer);
    }

    /**
     * Crea un Label estilizado como enlace para el footer.
     */
    private Label crearFooterLink(String text) {
        Label link = new Label(text);
        // Usamos Manrope SemiBold para los links
        link.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 11; -fx-text-fill: #64748b; -fx-cursor: hand;");

        // Efecto hover simple
        link.setOnMouseEntered(e -> link.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 11; -fx-text-fill: #3F68E4; -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-font-family: 'Manrope SemiBold'; -fx-font-size: 11; -fx-text-fill: #64748b; -fx-cursor: hand;"));

        return link;
    }
}