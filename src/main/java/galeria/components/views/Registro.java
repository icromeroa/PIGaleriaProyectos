package galeria.components.views;

import galeria.app.MainApp;
import galeria.util.Animations;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class Registro extends ScrollPane {

    // =========================================================
    // CAMPOS GLOBALES
    // =========================================================
    private TextField nombre;
    private TextField apellido;
    private TextField correo;

    private PasswordField pass;
    private PasswordField confirm;

    private CheckBox terms;

    private Label mensaje;

    public Registro() {

        // =====================================================
        // SCROLL
        // =====================================================
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.setStyle("""
            -fx-background: #F9FAFB;
            -fx-background-color: #F9FAFB;
        """);

        // =====================================================
        // CONTENEDOR PRINCIPAL
        // =====================================================
        HBox contenedorPrincipal = new HBox(80);

        contenedorPrincipal.setAlignment(Pos.CENTER);

        contenedorPrincipal.setPadding(
                new Insets(60, 80, 80, 80)
        );

        contenedorPrincipal.setMaxWidth(1200);

        VBox izquierda = crearColumnaIzquierda();
        VBox derecha = crearFormulario();

        HBox.setHgrow(izquierda, Priority.ALWAYS);

        contenedorPrincipal.getChildren().addAll(
                izquierda,
                derecha
        );

        VBox wrapper = new VBox(contenedorPrincipal);

        wrapper.setAlignment(Pos.TOP_CENTER);

        this.setContent(wrapper);

        // =====================================================
        // ANIMACIONES
        // =====================================================
        Animations.slideUpFadeIn(izquierda, 100);
        Animations.slideUpFadeIn(derecha, 250);
    }

    // =========================================================
    // COLUMNA IZQUIERDA
    // =========================================================
    private VBox crearColumnaIzquierda() {

        VBox box = new VBox(25);

        box.setAlignment(Pos.CENTER_LEFT);

        box.setMaxWidth(500);

        // BADGE
        Label badge = new Label("PORTAL DE INVESTIGADORES");

        badge.setStyle("""
            -fx-background-color: #E0E7FF;
            -fx-text-fill: #3F68E4;
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 11px;
            -fx-padding: 6 14 6 14;
            -fx-background-radius: 20;
        """);

        // TITULO
        Text t1 = new Text("Impulsa tu\n");

        t1.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 40px;
            -fx-fill: #111827;
        """);

        Text t2 = new Text("Legado Académico.");

        t2.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 40px;
            -fx-fill: #3F68E4;
        """);

        TextFlow titulo = new TextFlow(t1, t2);

        // SUBTITULO
        Label subtitulo = new Label(
                "Únete a la red de innovación científica más grande de la región. " +
                        "Repositorio Académico conecta tu talento con el mundo."
        );

        subtitulo.setWrapText(true);

        subtitulo.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 16px;
            -fx-text-fill: #4B5563;
            -fx-line-spacing: 5px;
        """);

        // TARJETAS
        HBox tarjetas = new HBox(20);

        VBox card1 = crearCard(
                "fas-chart-bar",
                "Métricas",
                "Sigue el impacto de tus publicaciones en tiempo real."
        );

        VBox card2 = crearCard(
                "fas-project-diagram",
                "Red Global",
                "Colabora con investigadores de más de 500 instituciones."
        );

        tarjetas.getChildren().addAll(card1, card2);

        // TESTIMONIO
        VBox quote = new VBox(15);

        quote.setStyle("""
            -fx-background-color: #0F172A;
            -fx-background-radius: 18;
            -fx-padding: 30;
        """);

        Label texto = new Label(
                "\"UniRepo ha transformado la forma en que visibilizamos nuestra investigación institucional.\""
        );

        texto.setWrapText(true);

        texto.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 15px;
            -fx-text-fill: white;
            -fx-font-style: italic;
        """);

        Label autor = new Label(
                "— Sergio Cristancho, Director de Ingeniería de sistemas"
        );

        autor.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 13px;
            -fx-text-fill: #94A3B8;
        """);

        quote.getChildren().addAll(texto, autor);

        Animations.attachHoverLift(quote);

        box.getChildren().addAll(
                badge,
                titulo,
                subtitulo,
                tarjetas,
                quote
        );

        return box;
    }

    // =========================================================
    // TARJETAS
    // =========================================================
    private VBox crearCard(
            String iconoTexto,
            String tituloTexto,
            String descripcionTexto
    ) {

        VBox card = new VBox(10);

        card.setStyle("""
            -fx-background-color: #F8FAFC;
            -fx-background-radius: 18;
            -fx-padding: 20;
        """);

        card.setEffect(
                new DropShadow(
                        12,
                        Color.color(0,0,0,0.05)
                )
        );

        FontIcon icono = new FontIcon(iconoTexto);

        icono.setIconSize(20);

        icono.setIconColor(
                Color.web("#3F68E4")
        );

        Label titulo = new Label(tituloTexto);

        titulo.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 16px;
            -fx-text-fill: #111827;
        """);

        Label desc = new Label(descripcionTexto);

        desc.setWrapText(true);

        desc.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 13px;
            -fx-text-fill: #64748B;
        """);

        card.getChildren().addAll(
                icono,
                titulo,
                desc
        );

        Animations.attachHoverLift(card);

        return card;
    }

    // =========================================================
    // FORMULARIO
    // =========================================================
    private VBox crearFormulario() {

        VBox form = new VBox(20);

        form.setPrefWidth(450);

        form.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 25;
            -fx-padding: 40;
        """);

        DropShadow shadow = new DropShadow(
                25,
                Color.color(0,0,0,0.08)
        );

        shadow.setOffsetY(10);

        form.setEffect(shadow);

        // =====================================================
        // HEADER
        // =====================================================
        Label titulo = new Label("Crear cuenta");

        titulo.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 24px;
            -fx-text-fill: #111827;
        """);

        Label subtitulo = new Label(
                "Comienza tu viaje académico hoy mismo."
        );

        subtitulo.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 14px;
            -fx-text-fill: #64748B;
        """);

        VBox header = new VBox(5);

        header.getChildren().addAll(
                titulo,
                subtitulo
        );

        // =====================================================
        // INPUTS
        // =====================================================
        nombre = crearTextField("Ej. Ana");
        apellido = crearTextField("Ej. Rossi");

        HBox filaNombre = new HBox(15);

        VBox nombreBox = crearInput("Nombre", nombre);
        VBox apellidoBox = crearInput("Apellido", apellido);

        HBox.setHgrow(nombreBox, Priority.ALWAYS);
        HBox.setHgrow(apellidoBox, Priority.ALWAYS);

        filaNombre.getChildren().addAll(
                nombreBox,
                apellidoBox
        );

        correo = crearTextField(
                "usuario@academia.usbbog.edu.co"
        );

        pass = crearPassword("••••••••");

        confirm = crearPassword("••••••••");

        VBox correoBox = crearInput(
                "Correo Institucional",
                correo
        );

        VBox passBox = crearInput(
                "Contraseña",
                pass
        );

        VBox confirmBox = crearInput(
                "Confirmar Contraseña",
                confirm
        );

        // =====================================================
        // CHECKBOX
        // =====================================================
        terms = new CheckBox(
                "Acepto los Términos de Servicio y Política de Privacidad"
        );

        terms.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 12px;
            -fx-text-fill: #64748B;
        """);

        // =====================================================
        // MENSAJE
        // =====================================================
        mensaje = new Label();

        mensaje.setVisible(false);

        mensaje.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 13px;
        """);

        // =====================================================
        // BOTON
        // =====================================================
        Button btn = new Button(
                "Publicar Investigación"
        );

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle("""
            -fx-background-color: #F97316;
            -fx-text-fill: white;
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 15px;
            -fx-background-radius: 25;
            -fx-padding: 13;
            -fx-cursor: hand;
        """);

        Animations.attachHoverLift(btn);

        // =====================================================
        // EVENTO BOTON
        // =====================================================
        btn.setOnAction(e -> {

            limpiarErrores(
                    nombre,
                    apellido,
                    correo,
                    pass,
                    confirm
            );

            mensaje.setVisible(false);

            boolean valido = true;

            if (nombre.getText().trim().isEmpty()) {

                marcarError(
                        nombre,
                        "Ingresa tu nombre"
                );

                valido = false;
            }

            if (apellido.getText().trim().isEmpty()) {

                marcarError(
                        apellido,
                        "Ingresa tu apellido"
                );

                valido = false;
            }

            if (correo.getText().trim().isEmpty()) {

                marcarError(
                        correo,
                        "Ingresa tu correo"
                );

                valido = false;
            }

            if (pass.getText().isEmpty()) {

                marcarError(
                        pass,
                        "Ingresa una contraseña"
                );

                valido = false;
            }

            if (!pass.getText().equals(confirm.getText())) {

                marcarError(
                        confirm,
                        "Las contraseñas no coinciden"
                );

                valido = false;
            }

            if (!terms.isSelected()) {

                mensaje.setVisible(true);

                mensaje.setTextFill(
                        Color.web("#EF4444")
                );

                mensaje.setText(
                        "❌ Debes aceptar los términos"
                );

                return;
            }

            if (!valido) return;

            // =================================================
            // EXITO
            // =================================================
            mensaje.setVisible(true);

            mensaje.setTextFill(
                    Color.web("#10B981")
            );

            mensaje.setText(
                    "✅ Registro exitoso. Redirigiendo al login..."
            );

            PauseTransition delay =
                    new PauseTransition(
                            Duration.seconds(1.5)
                    );

            delay.setOnFinished(ev -> {
                MainApp.setView(new Login());
            });

            delay.play();
        });

        // =====================================================
        // LOGIN LINK
        // =====================================================
        HBox loginBox = new HBox(5);

        loginBox.setAlignment(Pos.CENTER);

        Label l1 = new Label(
                "¿Ya tienes una cuenta?"
        );

        l1.setStyle("""
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 13px;
            -fx-text-fill: #64748B;
        """);

        Hyperlink loginLink = new Hyperlink(
                "Iniciar Sesión"
        );

        loginLink.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 13px;
            -fx-text-fill: #3F68E4;
        """);

        loginLink.setOnAction(e -> {
            MainApp.setView(new Login());
        });

        loginBox.getChildren().addAll(
                l1,
                loginLink
        );

        // =====================================================
        // ADD
        // =====================================================
        form.getChildren().addAll(
                header,
                filaNombre,
                correoBox,
                passBox,
                confirmBox,
                terms,
                mensaje,
                btn,
                loginBox
        );

        return form;
    }

    // =========================================================
    // INPUT WRAPPER
    // =========================================================
    private VBox crearInput(
            String texto,
            Control input
    ) {

        VBox box = new VBox(8);

        Label label = new Label(texto);

        label.setStyle("""
            -fx-font-family: 'Manrope Bold';
            -fx-font-size: 13px;
            -fx-text-fill: #374151;
        """);

        input.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #E2E8F0;
            -fx-border-radius: 15;
            -fx-background-radius: 15;
            -fx-padding: 12;
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 14px;
        """);

        input.setPrefHeight(45);

        box.getChildren().addAll(
                label,
                input
        );

        return box;
    }

    // =========================================================
    // TEXTFIELD
    // =========================================================
    private TextField crearTextField(String prompt) {

        TextField tf = new TextField();

        tf.setPromptText(prompt);

        return tf;
    }

    // =========================================================
    // PASSWORD
    // =========================================================
    private PasswordField crearPassword(String prompt) {

        PasswordField pf = new PasswordField();

        pf.setPromptText(prompt);

        return pf;
    }

    // =========================================================
    // ERROR INPUT
    // =========================================================
    private void marcarError(
            Control campo,
            String mensaje
    ) {

        campo.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #EF4444;
            -fx-border-radius: 15;
            -fx-background-radius: 15;
            -fx-padding: 12;
            -fx-font-family: 'Manrope Medium';
            -fx-font-size: 14px;
        """);

        campo.setTooltip(
                new Tooltip(mensaje)
        );
    }

    // =========================================================
    // LIMPIAR ERRORES
    // =========================================================
    private void limpiarErrores(Control... campos) {

        for (Control campo : campos) {

            campo.setTooltip(null);

            campo.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E2E8F0;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-padding: 12;
                -fx-font-family: 'Manrope Medium';
                -fx-font-size: 14px;
            """);
        }
    }
}