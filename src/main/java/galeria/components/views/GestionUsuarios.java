package galeria.components.views;

import galeria.controller.ControladorAdmin;
import galeria.model.Usuario;
import galeria.util.Animations;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.stream.Collectors;

public class GestionUsuarios extends ScrollPane {

    private final ControladorAdmin adminController = new ControladorAdmin();
    private final VBox tableBody = new VBox();
    private final TextField txtBusqueda = new TextField();

    private List<Usuario> usuariosCompletos;
    private List<Usuario> usuariosFiltrados;
    private int paginaActual = 0;
    private final int ITEMS_POR_PAGINA = 4;

    private Label lblValTotalUsuarios, lblValTotalAdmins;

    // --- AJUSTE DE COLUMNAS: Más espacio al nombre, menos al correo y rol ---
    private static final double COL_USUARIO_PCT = 0.40;   // Usuario + Avatar
    private static final double COL_CORREO_PCT = 0.25;    // Correo (más compacto)
    private static final double COL_ROL_PCT = 0.18;       // Combo de Rol
    private static final double COL_ACCIONES_PCT = 0.17;  // Botón eliminar

    public GestionUsuarios() {
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC; -fx-border-color: transparent;");

        VBox container = new VBox(30);
        // Reducido el padding lateral de 60 a 40 para que no se vea tan angosto
        container.setPadding(new Insets(30, 40, 30, 40));

        // --- 1. CABECERA ---
        HBox topBar = crearTopBar();

        // --- 2. TABLA ---
        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 10, 0, 0, 4);");

        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(18, 25, 18, 25)); // Padding interno de la tabla más elegante
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        tableHeader.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 16 16 0 0; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");

        tableHeader.getChildren().addAll(
                crearHeaderLabel("Usuario", COL_USUARIO_PCT),
                crearHeaderLabel("Correo", COL_CORREO_PCT),
                crearHeaderLabel("Rol", COL_ROL_PCT),
                crearHeaderLabel("Acciones", COL_ACCIONES_PCT)
        );

        tableBody.setMinHeight(280);
        tableContainer.getChildren().addAll(tableHeader, tableBody, crearFooterPaginacion());

        // --- 3. ESTADÍSTICAS ---
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                crearCardStat("USUARIOS TOTALES", "fas-users", "#3F68E4", true),
                crearCardStat("ADMINISTRADORES", "fas-shield-alt", "#F59E0B", false)
        );

        container.getChildren().addAll(topBar, tableContainer, statsRow);
        this.setContent(container);

        Platform.runLater(() -> {
            actualizarDatos();
            Animations.slideUpFadeIn(topBar, 100);
            Animations.slideUpFadeIn(tableContainer, 250);
            Animations.slideUpFadeIn(statsRow, 400);
        });
    }

    private HBox crearFilaUsuario(Usuario u) {
        HBox fila = new HBox();
        fila.setPadding(new Insets(12, 25, 12, 25)); // Fila un poco más delgada
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-border-color: #F1F5F9; -fx-border-width: 0 0 1 0;");

        // COLUMNA USUARIO
        HBox colUsuario = new HBox(15);
        colUsuario.setAlignment(Pos.CENTER_LEFT);
        colUsuario.prefWidthProperty().bind(this.widthProperty().multiply(COL_USUARIO_PCT));

        StackPane avatar = crearAvatar(u);
        VBox info = new VBox(1);
        Label nom = new Label(u.getNombre() + " " + u.getApellido());
        nom.setStyle("-fx-font-weight: 700; -fx-text-fill: #1E293B; -fx-font-size: 14;");
        Label rol = new Label(u.getEsAdmin() ? "ADMINISTRADOR" : "ESTUDIANTE");
        rol.setStyle("-fx-font-size: 10; -fx-text-fill: #94A3B8; -fx-font-weight: 800;");
        info.getChildren().addAll(nom, rol);
        colUsuario.getChildren().addAll(avatar, info);

        // COLUMNA CORREO
        Label lblCorreo = new Label(u.getCorreo());
        lblCorreo.setStyle("-fx-text-fill: #64748B; -fx-font-size: 13;");
        lblCorreo.prefWidthProperty().bind(this.widthProperty().multiply(COL_CORREO_PCT));

        // COLUMNA ROL
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("General", "Administrador");
        cb.setValue(u.getEsAdmin() ? "Administrador" : "General");
        cb.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 8; -fx-font-size: 12;");
        cb.setPrefWidth(130);
        cb.setOnAction(e -> {
            adminController.cambiarEstadoAdmin(u.getIdUsuario(), cb.getValue().equals("Administrador"));
            actualizarDatos();
        });
        HBox colRol = new HBox(cb);
        colRol.prefWidthProperty().bind(this.widthProperty().multiply(COL_ROL_PCT));

        // COLUMNA ACCIONES
        HBox colAcc = new HBox(8);
        colAcc.setAlignment(Pos.CENTER_LEFT);
        colAcc.setCursor(Cursor.HAND);
        FontIcon trash = new FontIcon("fas-trash-alt");
        trash.setIconColor(Color.web("#EF4444"));
        Label del = new Label("Eliminar");
        del.setStyle("-fx-text-fill: #EF4444; -fx-font-weight: 700; -fx-font-size: 13;");
        colAcc.getChildren().addAll(trash, del);
        colAcc.setOnMouseClicked(e -> {
            adminController.eliminarUsuario(u.getIdUsuario());
            actualizarDatos();
        });
        colAcc.prefWidthProperty().bind(this.widthProperty().multiply(COL_ACCIONES_PCT));

        fila.getChildren().addAll(colUsuario, lblCorreo, colRol, colAcc);
        return fila;
    }

    private HBox crearTopBar() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        VBox texts = new VBox(5);
        Label title = new Label("Gestión de Usuarios");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: 800; -fx-text-fill: #0F172A;"); // Título un poco más pequeño
        Label sub = new Label("Panel administrativo para el control de roles y seguridad institucional.");
        sub.setStyle("-fx-font-size: 13; -fx-text-fill: #64748B;");
        texts.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox search = new HBox(10);
        search.setAlignment(Pos.CENTER_LEFT);
        search.setPadding(new Insets(8, 12, 8, 12));
        search.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");

        FontIcon ic = new FontIcon("fas-search");
        ic.setIconColor(Color.web("#94A3B8"));
        txtBusqueda.setPromptText("Buscar...");
        txtBusqueda.setStyle("-fx-background-color: transparent; -fx-pref-width: 220;");
        txtBusqueda.setOnKeyReleased(e -> filtrarBusqueda());

        search.getChildren().addAll(ic, txtBusqueda);
        row.getChildren().addAll(texts, spacer, search);
        return row;
    }

    private void renderizarTabla() {
        tableBody.getChildren().clear();
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, usuariosFiltrados.size());

        for (int i = inicio; i < fin; i++) {
            HBox fila = crearFilaUsuario(usuariosFiltrados.get(i));
            tableBody.getChildren().add(fila);
            // Mantenemos la animación de revelación
            Animations.revealProjectCard(fila, (i - inicio) * 120);
        }
    }

    private Label crearHeaderLabel(String text, double pct) {
        Label l = new Label(text.toUpperCase());
        l.setStyle("-fx-text-fill: #94A3B8; -fx-font-weight: 800; -fx-font-size: 10; -fx-letter-spacing: 0.5px;");
        l.prefWidthProperty().bind(this.widthProperty().multiply(pct));
        return l;
    }

    // --- MÉTODOS DE APOYO (Avatar, Stats, etc. se mantienen igual pero con ajustes visuales leves) ---
    private StackPane crearAvatar(Usuario u) {
        Circle c = new Circle(18, Color.web("#E2E8F0"));
        Label l = new Label((u.getNombre().substring(0,1) + u.getApellido().substring(0,1)).toUpperCase());
        l.setStyle("-fx-font-size: 11; -fx-font-weight: 800; -fx-text-fill: #64748B;");
        return new StackPane(c, l);
    }

    private VBox crearCardStat(String title, String icon, String color, boolean isTotal) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 4);");

        HBox h = new HBox(12);
        h.setAlignment(Pos.CENTER_LEFT);
        Circle bg = new Circle(20, Color.web(color + "15"));
        FontIcon ic = new FontIcon(icon);
        ic.setIconColor(Color.web(color));
        ic.setIconSize(18);

        VBox t = new VBox(2);
        Label lt = new Label(title);
        lt.setStyle("-fx-font-size: 10; -fx-text-fill: #94A3B8; -fx-font-weight: 800;");
        Label lv = new Label("0");
        lv.setStyle("-fx-font-size: 24; -fx-font-weight: 800; -fx-text-fill: #1E293B;");

        if(isTotal) lblValTotalUsuarios = lv; else lblValTotalAdmins = lv;

        t.getChildren().addAll(lt, lv);
        h.getChildren().addAll(new StackPane(bg, ic), t);
        card.getChildren().add(h);
        Animations.attachHoverLift(card);
        return card;
    }

    private void actualizarDatos() {
        usuariosCompletos = adminController.getListaUsuarios();
        usuariosFiltrados = usuariosCompletos;
        renderizarTabla();
        Animations.animarConteo(lblValTotalUsuarios, usuariosCompletos.size(), "-fx-font-size: 24; -fx-font-weight: 800;");
        Animations.animarConteo(lblValTotalAdmins, (int)usuariosCompletos.stream().filter(Usuario::getEsAdmin).count(), "-fx-font-size: 24; -fx-font-weight: 800;");
    }

    private HBox crearFooterPaginacion() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(15, 25, 15, 25));
        footer.setAlignment(Pos.CENTER_RIGHT);
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
        Button b1 = new Button(); b1.setGraphic(new FontIcon("fas-chevron-left"));
        Button b2 = new Button(); b2.setGraphic(new FontIcon("fas-chevron-right"));
        b1.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 6;");
        b2.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 6;");
        b1.setCursor(Cursor.HAND); b2.setCursor(Cursor.HAND);
        b1.setOnAction(e -> { if(paginaActual > 0) { paginaActual--; renderizarTabla(); } });
        b2.setOnAction(e -> { if((paginaActual + 1) * ITEMS_POR_PAGINA < usuariosFiltrados.size()) { paginaActual++; renderizarTabla(); } });
        footer.getChildren().addAll(s, b1, b2);
        return footer;
    }

    private void filtrarBusqueda() {
        String query = txtBusqueda.getText().toLowerCase();
        usuariosFiltrados = usuariosCompletos.stream()
                .filter(u -> (u.getNombre()+" "+u.getApellido()).toLowerCase().contains(query) || u.getCorreo().toLowerCase().contains(query))
                .collect(Collectors.toList());
        paginaActual = 0;
        renderizarTabla();
    }
}