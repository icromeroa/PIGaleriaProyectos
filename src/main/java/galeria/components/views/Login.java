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
        this.setStyle("-fx-background-color: #fdf2f8;");

        VBox card = new VBox(25);
        card.setMaxSize(400, 520);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 30; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 20, 0, 0, 10);"
        );

        Label title = new Label("¡Hola de nuevo!");
        title.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 28px; -fx-text-fill: #1f2937;");

        Label subtitle = new Label("Ingresa tus credenciales institucionales");
        subtitle.setStyle("-fx-font-family: 'Manrope Regular'; -fx-font-size: 14px; -fx-text-fill: #6b7280;");

        // Campos
        MFXTextField txtEmail = new MFXTextField();
        txtEmail.setFloatingText("Correo Institucional");
        txtEmail.setPrefWidth(320);

        MFXPasswordField txtPassword = new MFXPasswordField();
        txtPassword.setFloatingText("Contraseña");
        txtPassword.setPrefWidth(320);

        // Label para errores
        Label lblError = new Label("");
        lblError.setStyle("-fx-text-fill: #ef4444; -fx-font-family: 'Manrope Medium'; -fx-font-size: 12px;");
        lblError.setVisible(false);

        MFXButton btnIngresar = new MFXButton("Acceder");
        btnIngresar.setPrefWidth(320);
        btnIngresar.setStyle(
                "-fx-background-color: #3F68E4; -fx-text-fill: white; " +
                        "-fx-font-family: 'Manrope Bold'; -fx-font-size: 15px; " +
                        "-fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"
        );

        // --- LÓGICA DE CONEXIÓN A BASE DE DATOS ---
        btnIngresar.setOnAction(e -> {
            String correo = txtEmail.getText();
            String clave = txtPassword.getText();

            if (correo.isEmpty() || clave.isEmpty()) {
                lblError.setText("Por favor llena todos los campos");
                lblError.setVisible(true);
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario user = dao.login(correo, clave); // Llamada a tu método de la DB

            if (user != null) {
                Sesion.iniciar(user); // Guardamos en memoria
                MainApp.actualizarNavbar(); // Cambiamos el botón a Hamburguesa
                MainApp.setView(new galeria.components.views.Catalogo());
            } else {
                lblError.setText("Correo o contraseña incorrectos");
                lblError.setVisible(true);
            }
        });

        Hyperlink btnVolver = new Hyperlink("Volver al inicio");
        btnVolver.setOnAction(e -> MainApp.setView(new Inicio()));

        card.getChildren().addAll(title, subtitle, txtEmail, txtPassword, lblError, btnIngresar, btnVolver);
        this.getChildren().add(card);
    }
}