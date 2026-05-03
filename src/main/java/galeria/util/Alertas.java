package galeria.util;

import galeria.app.MainApp;
import galeria.components.views.Login;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

public class Alertas {

    public static void mostrarModalLoginRequerido() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #3F68E4; -fx-border-radius: 25; -fx-border-width: 2;");
        root.setEffect(new DropShadow(25, Color.rgb(0, 0, 0, 0.3)));

        FontIcon icon = new FontIcon("fas-user-lock");
        icon.setIconSize(45);
        icon.setIconColor(Color.web("#3F68E4"));

        Label title = new Label("Identificación Requerida");
        title.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 20; -fx-text-fill: #1E293B;");

        Label msg = new Label("Para calificar, guardar o descargar recursos, necesitas ingresar a tu cuenta.");
        msg.setWrapText(true);
        msg.setAlignment(Pos.CENTER);
        msg.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14; -fx-text-fill: #64748B;");

        Button btnLogin = new Button("Iniciar Sesión ahora");
        btnLogin.setStyle("-fx-background-color: #3F68E4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 10 25; -fx-cursor: hand;");
        btnLogin.setOnAction(e -> {
            stage.close();
            MainApp.setView(new Login());
        });

        Button btnCerrar = new Button("Quizás luego");
        btnCerrar.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-cursor: hand;");
        btnCerrar.setOnAction(e -> stage.close());

        root.getChildren().addAll(icon, title, msg, btnLogin, btnCerrar);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void mostrarMensaje(String titulo, String mensaje, String iconoKey, String colorHex) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        root.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.2)));

        FontIcon icon = new FontIcon(iconoKey);
        icon.setIconSize(30);
        icon.setIconColor(Color.web(colorHex));

        Label t = new Label(titulo);
        t.setStyle("-fx-font-family: 'Manrope Bold'; -fx-font-size: 16;");

        Label m = new Label(mensaje);
        m.setStyle("-fx-font-family: 'Manrope'; -fx-text-fill: #475569;");

        Button btnOk = new Button("Entendido");
        btnOk.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        btnOk.setOnAction(e -> stage.close());

        root.getChildren().addAll(icon, t, m, btnOk);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}