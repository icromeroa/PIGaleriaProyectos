package galeria.components.interfaz;

import galeria.app.MainApp;
import galeria.components.views.Catalogo;
import galeria.components.views.Inicio;
import galeria.util.Animations;
import galeria.util.Sesion;
import galeria.model.Usuario;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class Menu extends VBox {

    private boolean abierto = false;

    public Menu() {
        setPrefWidth(280);
        setMaxWidth(280);
        setStyle(
                "-fx-background-color: #f8faff;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.16),24,0,0,8);"
        );
        setVisible(false);
        setManaged(false);
        setOpacity(0);
        setTranslateX(20);
        // No tiene pickOnBounds cuando está oculto
        setPickOnBounds(false);
    }

    /** Abre si está cerrado, cierra si está abierto. */
    public void toggle() {
        if (abierto) cerrar();
        else         abrir();
    }

    public void abrir() {
        if (abierto) return;
        construirContenido();
        setVisible(true);
        setManaged(true);
        setPickOnBounds(true);
        abierto = true;

        FadeTransition fade = new FadeTransition(Duration.millis(220), this);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(220), this);
        slide.setFromX(20);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fade, slide).play();
    }

    public void cerrar() {
        if (!abierto) return;

        FadeTransition fade = new FadeTransition(Duration.millis(180), this);
        fade.setFromValue(1);
        fade.setToValue(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(180), this);
        slide.setToX(20);
        slide.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition pt = new ParallelTransition(fade, slide);
        pt.setOnFinished(e -> {
            setVisible(false);
            setManaged(false);
            setPickOnBounds(false);
            abierto = false;
        });
        pt.play();
    }

    // ── Construye el contenido según rol ──────────────────
    private void construirContenido() {
        getChildren().clear();
        setPadding(new Insets(20, 16, 20, 16));
        setSpacing(2);
        setMinHeight(400);

        Usuario u      = Sesion.getUsuario();
        boolean esAdmin = u != null && u.getEsAdmin();

        // Items
        getChildren().add(crearItem("fas-home",     "Inicio",    false,
                () -> navegar("Inicio")));
        getChildren().add(crearItem("fas-compass",  "Explorar",  false,
                () -> navegar("Explorar")));

        if (esAdmin) {
            getChildren().add(crearItem("fas-upload",
                    "Agregar Proyecto", false,
                    () -> navegar("AgregarProyecto")));
        }

        getChildren().add(crearItem("fas-th",
                "Categorías", false, () -> navegar("Categorias")));
        getChildren().add(crearItem("fas-bookmark",
                "Guardados",  false, () -> navegar("Guardados")));

        if (esAdmin) {
            getChildren().add(crearItem("fas-users",
                    "Usuarios", false, () -> navegar("Usuarios")));
        }

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        // Separador
        Separator sep = new Separator();
        sep.setPadding(new Insets(8, 0, 8, 0));
        getChildren().add(sep);

        // ── Fila perfil ───────────────────────────────────
        HBox perfilRow = new HBox(12);
        perfilRow.setAlignment(Pos.CENTER_LEFT);
        perfilRow.setPadding(new Insets(8, 10, 8, 10));
        perfilRow.setCursor(Cursor.HAND);
        perfilRow.setStyle("-fx-background-radius: 12;");
        perfilRow.setMaxWidth(Double.MAX_VALUE);

        Circle avatarBg = new Circle(18, Color.web("#3F68E4"));
        String inicial = (u != null && u.getNombre() != null
                && !u.getNombre().isEmpty())
                ? String.valueOf(u.getNombre().charAt(0)).toUpperCase()
                : "?";
        Label avatarLbl = new Label(inicial);
        avatarLbl.setStyle(
                "-fx-font-family: 'Manrope Bold';" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: white;"
        );
        StackPane avatarStack = new StackPane(avatarBg, avatarLbl);
        avatarStack.setPrefSize(36, 36);
        avatarStack.setMaxSize(36, 36);

        Label lblNombre = new Label(
                u != null
                        ? u.getNombre() + " " + u.getApellido()
                        : "Usuario"
        );
        lblNombre.setStyle(
                "-fx-font-family: 'Manrope SemiBold';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #1f2937;"
        );

        perfilRow.getChildren().addAll(avatarStack, lblNombre);
        perfilRow.setOnMouseEntered(e -> perfilRow.setStyle(
                "-fx-background-color: #eef2ff;" +
                        "-fx-background-radius: 12;"
        ));
        perfilRow.setOnMouseExited(e -> perfilRow.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 12;"
        ));
        perfilRow.setOnMouseClicked(e -> {
            cerrar();
            navegar("Perfil");
        });

        // ── Botón cerrar sesión ───────────────────────────
        HBox cerrarRow = new HBox(10);
        cerrarRow.setAlignment(Pos.CENTER_LEFT);
        cerrarRow.setMaxWidth(Double.MAX_VALUE);
        cerrarRow.setPadding(new Insets(11, 18, 11, 18));
        cerrarRow.setCursor(Cursor.HAND);
        cerrarRow.setStyle(
                "-fx-background-color: #3F68E4;" +
                        "-fx-background-radius: 40;"
        );

        FontIcon iconoCerrar = new FontIcon("fas-sign-out-alt");
        iconoCerrar.setIconSize(14);
        iconoCerrar.setIconColor(Color.WHITE);

        Label lblCerrar = new Label("Cerrar Sesión");
        lblCerrar.setStyle(
                "-fx-font-family: 'Manrope SemiBold';" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: white;"
        );
        cerrarRow.getChildren().addAll(iconoCerrar, lblCerrar);

        cerrarRow.setOnMouseEntered(e -> cerrarRow.setStyle(
                "-fx-background-color: #2d55c7;" +
                        "-fx-background-radius: 40;"
        ));
        cerrarRow.setOnMouseExited(e -> cerrarRow.setStyle(
                "-fx-background-color: #3F68E4;" +
                        "-fx-background-radius: 40;"
        ));
        cerrarRow.setOnMouseClicked(e -> {
            Sesion.cerrar();
            cerrar();
            MainApp.actualizarNavbar();
            MainApp.setView(new Inicio());
        });

        getChildren().addAll(perfilRow, cerrarRow);
    }

    // ── Item del menú ─────────────────────────────────────
    private HBox crearItem(String icono, String texto,
                           boolean destacado, Runnable accion) {
        HBox item = new HBox(14);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 14, 12, 14));
        item.setCursor(Cursor.HAND);
        item.setMaxWidth(Double.MAX_VALUE);

        String bgBase  = destacado ? "#3F68E4"   : "transparent";
        String bgHover = destacado ? "#2d55c7"   : "#eef2ff";
        String txtColor = destacado ? "white"    : "#1f2937";
        String iconColor = destacado ? "white"   : "#374151";

        item.setStyle(
                "-fx-background-color: " + bgBase + ";" +
                        "-fx-background-radius: 12;"
        );

        FontIcon iconNode = new FontIcon(icono);
        iconNode.setIconSize(16);
        iconNode.setIconColor(Color.web(iconColor));

        Label lbl = new Label(texto);
        lbl.setStyle(
                "-fx-font-family: 'Manrope SemiBold';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + txtColor + ";"
        );

        item.getChildren().addAll(iconNode, lbl);

        item.setOnMouseEntered(e -> item.setStyle(
                "-fx-background-color: " + bgHover + ";" +
                        "-fx-background-radius: 12;"
        ));
        item.setOnMouseExited(e -> item.setStyle(
                "-fx-background-color: " + bgBase + ";" +
                        "-fx-background-radius: 12;"
        ));
        item.setOnMouseClicked(e -> {
            cerrar();
            accion.run();
        });

        // Animación escalonada de entrada
        int idx = getChildren().size();
        Animations.slideUpFadeIn(item, idx * 35.0);

        return item;
    }

    // ── Navegación ────────────────────────────────────────
    private void navegar(String destino) {
        switch (destino) {
            case "Inicio"          -> MainApp.setView(new Inicio());
            case "Explorar"        -> MainApp.setView(new Catalogo());
            case "AgregarProyecto" -> MainApp.setView(placeholder("Agregar Proyecto"));
            case "Categorias"      -> MainApp.setView(placeholder("Categorías"));
            case "Guardados"       -> MainApp.setView(placeholder("Guardados"));
            case "Usuarios"        -> MainApp.setView(placeholder("Gestión de Usuarios"));
            case "Perfil"          -> MainApp.setView(placeholder("Perfil"));
        }
    }

    private Node placeholder(String nombre) {
        VBox v = new VBox(20);
        v.setAlignment(Pos.CENTER);
        v.setStyle("-fx-background-color: #ffffff;");
        VBox.setVgrow(v, Priority.ALWAYS);

        FontIcon ic = new FontIcon("fas-tools");
        ic.setIconSize(52);
        ic.setIconColor(Color.web("#3F68E4"));

        Label titulo = new Label("Se ha abierto: " + nombre);
        titulo.setStyle(
                "-fx-font-family: 'Manrope Bold';" +
                        "-fx-font-size: 26px;" +
                        "-fx-text-fill: #1f2937;"
        );

        Label sub = new Label("Esta vista estará disponible próximamente.");
        sub.setStyle(
                "-fx-font-family: 'Manrope';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #9ca3af;"
        );

        v.getChildren().addAll(ic, titulo, sub);
        Animations.slideUpFadeIn(titulo, 100);
        Animations.slideUpFadeIn(sub, 200);
        return v;
    }
}