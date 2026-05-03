package galeria.components.views;

import galeria.app.MainApp;
import galeria.dao.UsuarioDAO;
import galeria.model.Usuario;
import galeria.util.Sesion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;

public class Login extends StackPane {

    public Login() {
        // Fondo suave acorde a la paleta de colores
        this.setStyle("-fx-background-color: #fdf2f8;");

        // Tarjeta contenedora blanca
        VBox card = new VBox(25);
        card.setMaxSize(400, 520);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 30; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 20, 0, 0, 10);"
        );

        // Encabezados
        Label title = new Label("¡Hola de nuevo!");
        title.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 28px; -fx-text-fill: #1f2937;");

        Label subtitle = new Label("Ingresa tus credenciales institucionales");
        subtitle.setStyle("-fx-font-family: 'Manrope Regular'; -fx-font-size: 14px; -fx-text-fill: #6b7280;");

        // Campos de entrada (MaterialFX)
        MFXTextField txtEmail = new MFXTextField();
        txtEmail.setFloatingText("Correo Institucional");
        txtEmail.setPrefWidth(320);

        MFXPasswordField txtPassword = new MFXPasswordField();
        txtPassword.setFloatingText("Contraseña");
        txtPassword.setPrefWidth(320);

        // Label para gestión de errores
        Label lblError = new Label("");
        lblError.setStyle("-fx-text-fill: #ef4444; -fx-font-family: 'Manrope Medium'; -fx-font-size: 12px;");
        lblError.setVisible(false);

        // Botón principal de acceso
        MFXButton btnIngresar = new MFXButton("Acceder");
        btnIngresar.setPrefWidth(320);
        btnIngresar.setStyle(
                "-fx-background-color: #3F68E4; -fx-text-fill: white; " +
                        "-fx-font-family: 'Manrope Bold'; -fx-font-size: 15px; " +
                        "-fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"
        );

        // --- LÓGICA DE LOGIN CORREGIDA ---
        btnIngresar.setOnAction(e -> {
            String correo = txtEmail.getText();
            String clave = txtPassword.getText();

            // Validación básica de campos vacíos
            if (correo.isEmpty() || clave.isEmpty()) {
                lblError.setText("Por favor llena todos los campos");
                lblError.setVisible(true);
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario user = dao.login(correo, clave);

            if (user != null) {
                // 1. Guardar sesión
                Sesion.iniciar(user);

                // 2. Actualizar botones del Navbar (Login -> Hamburguesa)
                MainApp.actualizarNavbar();

                // 3. Cambiar el indicador visual del Navbar a 'Explorar Catálogo'
                if (MainApp.getNavbar() != null) {
                    MainApp.getNavbar().setLinkActivo("Explorar Catálogo");
                }

                // 4. Cambiar a la vista del catálogo
                MainApp.setView(new Catalogo());

            } else {
                lblError.setText("Correo o contraseña incorrectos");
                lblError.setVisible(true);
            }
        });

        // Botón para retroceder
        Hyperlink btnVolver = new Hyperlink("Volver al inicio");
        btnVolver.setStyle("-fx-font-family: 'Manrope Medium'; -fx-text-fill: #6b7280;");
        btnVolver.setOnAction(e -> {
            MainApp.setView(new Inicio());
            if (MainApp.getNavbar() != null) {
                MainApp.getNavbar().setLinkActivo("Inicio");
            }
        });

        card.getChildren().addAll(title, subtitle, txtEmail, txtPassword, lblError, btnIngresar, btnVolver);
        this.getChildren().add(card);
    }
}