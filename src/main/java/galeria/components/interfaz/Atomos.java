package galeria.components.interfaz;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.effects.DepthLevel;
import galeria.util.Animations;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * Clase centralizada para componentes reutilizables (Átomos)
 */
public class Atomos {

    /**
     * Crea un botón estilo Material con el diseño de UniRepo
     */
    public static MFXButton crearBotonPrincipal(String texto, FontAwesomeSolid icono) {
        MFXButton btn = new MFXButton(texto);

        // 1. Configuración del Icono (si existe)
        if (icono != null) {
            FontIcon iconView = new FontIcon(icono);
            iconView.setIconColor(Color.WHITE);
            iconView.setIconSize(18);
            btn.setGraphic(iconView);
            btn.setGraphicTextGap(10);
        }

        // 2. Comportamiento Material
        btn.setButtonType(ButtonType.RAISED);
        btn.setDepthLevel(DepthLevel.LEVEL1);

        // 3. Estilo vinculado al CSS (Asegúrate de tener esta clase en style.css)
        btn.getStyleClass().add("btn-principal-custom");

        // 4. Animación de utilidad
        Animations.attachHoverLift(btn);

        return btn;
    }

    // Aquí puedes ir agregando más átomos: crearInput(), crearLabelTitulo(), etc.
}
