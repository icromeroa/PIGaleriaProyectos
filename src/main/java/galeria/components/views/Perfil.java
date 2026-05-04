package galeria.components.views;

import galeria.app.MainApp;
import galeria.components.interfaz.CardProyecto;
import galeria.dao.UsuarioDAO;
import galeria.dao.GuardadoDAO;
import galeria.dao.ValoracionDAO;
import galeria.dao.VisualizacionDAO;
import galeria.model.Usuario;
import galeria.model.Proyecto;
import galeria.model.Valoracion;
import galeria.util.Animations;
import galeria.util.CardStyle;
import galeria.util.Sesion;
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

public class Perfil extends ScrollPane {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final GuardadoDAO guardadoDAO = new GuardadoDAO();
    private final ValoracionDAO valoracionDAO = new ValoracionDAO();

    // Suponiendo que tienes acceso al usuario actual (ej. desde un Singleton o Sesion)
    private Usuario usuarioActual;

    private boolean modoEdicion = false;
    private TextField txtNombre, txtApellido, txtCorreo;
    private PasswordField txtClave;

    public Perfil(Usuario usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC;");

        VBox root = new VBox(40);
        root.setPadding(new Insets(40, 60, 40, 60));

        // --- CABECERA ---
        VBox header = crearCabecera();

        // --- CUERPO PRINCIPAL (2 COLUMNAS) ---
        HBox body = new HBox(40);

        // LADO IZQUIERDO (40% ancho)
        VBox leftCol = new VBox(30);
        leftCol.prefWidthProperty().bind(body.widthProperty().multiply(0.38));

        VBox statsPanel = crearPanelEstadisticas();
        VBox infoPanel = crearPanelInformacion();
        Button btnEliminar = crearBtnEliminar();

        leftCol.getChildren().addAll(statsPanel, infoPanel, btnEliminar);

        // LADO DERECHO (60% ancho)
        VBox rightCol = new VBox(30);
        rightCol.prefWidthProperty().bind(body.widthProperty().multiply(0.58));

        VBox actividadReciente = crearSeccionActividad();
        VBox proyectosGuardados = crearSeccionGuardados();
        VBox calificaciones = crearSeccionCalificaciones();

        rightCol.getChildren().addAll(actividadReciente, proyectosGuardados, calificaciones);

        body.getChildren().addAll(leftCol, rightCol);
        root.getChildren().addAll(header, body);

        this.setContent(root);

        // Animaciones de entrada
        Platform.runLater(() -> {
            Animations.slideUpFadeIn(header, 100);
            Animations.slideUpFadeIn(statsPanel, 200);
            Animations.slideUpFadeIn(infoPanel, 300);
            Animations.slideUpFadeIn(rightCol, 400);
        });
    }

    private VBox crearCabecera() {
        VBox header = new VBox(20);

        Label title = new Label("Perfil");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: 800; -fx-text-fill: #0F172A;");

        HBox welcomeBox = new HBox(25);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        StackPane avatar = new StackPane();
        Circle circulo = new Circle(45, Color.web("#E2E8F0"));
        String iniciales = (usuarioActual.getNombre().substring(0,1) + usuarioActual.getApellido().substring(0,1)).toUpperCase();
        Label lblInic = new Label(iniciales);
        lblInic.setStyle("-fx-font-size: 24; -fx-font-weight: 800; -fx-text-fill: #64748B;");
        avatar.getChildren().addAll(circulo, lblInic);

        VBox textoWelcome = new VBox(8);
        Label saludo = new Label("¡Hola, " + usuarioActual.getNombre() + " " + usuarioActual.getApellido() + "!");
        saludo.setStyle("-fx-font-size: 22; -fx-font-weight: 700; -fx-text-fill: #1E293B;");

        Label desc = new Label("Este es tu rincón privado. Aquí puedes poner al día tus datos o cerrar tu cuenta si lo necesitas. " +
                "No pierdas el hilo de lo que has estado viendo en tu historial reciente y mantén a mano esos " +
                "proyectos guardados que tanto te gustaron. Además, puedes ver todas las estrellas que has repartido.");
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14; -fx-line-spacing: 5;");
        desc.setMaxWidth(800);

        textoWelcome.getChildren().addAll(saludo, desc);
        welcomeBox.getChildren().addAll(avatar, textoWelcome);

        header.getChildren().addAll(title, welcomeBox);
        return header;
    }

    private VBox crearPanelEstadisticas() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 10, 0, 0, 4);");

        Label t = new Label("Estadísticas");
        t.setStyle("-fx-font-weight: 800; -fx-text-fill: #94A3B8; -fx-font-size: 13;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Aquí deberías tener métodos en tus DAOs para obtener estos conteos
        grid.add(crearItemStat("Publicaciones", "0", "#3F68E4"), 0, 0);
        grid.add(crearItemStat("Guardados", String.valueOf(guardadoDAO.listarProyectosGuardados(usuarioActual.getIdUsuario()).size()), "#3F68E4"), 1, 0);
        grid.add(crearItemStat("Calificados", "0", "#3F68E4"), 0, 1);
        grid.add(crearItemStat("Vistas", String.valueOf(usuarioActual.getHistorialVistas().size()), "#3F68E4"), 1, 1);

        card.getChildren().addAll(t, grid);
        return card;
    }

    private VBox crearItemStat(String label, String valor, String color) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(15));
        item.setPrefWidth(140);
        item.setStyle("-fx-background-color: " + color + "10; -fx-background-radius: 12;");

        Label v = new Label("0");
        v.setStyle("-fx-font-size: 20; -fx-font-weight: 800; -fx-text-fill: " + color + ";");
        Label l = new Label(label.toUpperCase());
        l.setStyle("-fx-font-size: 9; -fx-font-weight: 800; -fx-text-fill: #94A3B8;");

        item.getChildren().addAll(v, l);
        Platform.runLater(() -> Animations.animarConteo(v, Integer.parseInt(valor), v.getStyle()));
        return item;
    }

    private VBox crearPanelInformacion() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 10, 0, 0, 4);");

        HBox head = new HBox();
        Label t = new Label("Información");
        t.setStyle("-fx-font-weight: 800; -fx-text-fill: #94A3B8; -fx-font-size: 13;");
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);

        FontIcon editIcon = new FontIcon("fas-pen");
        editIcon.setIconColor(Color.web("#3F68E4"));
        editIcon.setCursor(Cursor.HAND);

        head.getChildren().addAll(t, s, editIcon);

        VBox fields = new VBox(15);
        txtNombre = crearField("Nombre", usuarioActual.getNombre());
        txtApellido = crearField("Apellidos", usuarioActual.getApellido());
        txtCorreo = crearField("Email", usuarioActual.getCorreo());
        txtClave = new PasswordField();
        txtClave.setText(usuarioActual.getClave());
        txtClave.setEditable(false);
        txtClave.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: #E2E8F0;");

        fields.getChildren().addAll(new Label("Contraseña"), txtClave);

        editIcon.setOnMouseClicked(e -> {
            modoEdicion = !modoEdicion;
            toggleEdicion(editIcon);
        });

        card.getChildren().addAll(head, txtNombre.getParent(), txtApellido.getParent(), txtCorreo.getParent(), fields);
        return card;
    }

    private void toggleEdicion(FontIcon icon) {
        boolean editable = modoEdicion;
        txtNombre.setEditable(editable);
        txtApellido.setEditable(editable);
        txtCorreo.setEditable(editable);
        txtClave.setEditable(editable);

        if (editable) {
            icon.setIconCode(org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.SAVE);
            txtNombre.setStyle("-fx-background-color: #F1F5F9;");
        } else {
            icon.setIconCode(org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.PEN);
            txtNombre.setStyle("-fx-background-color: transparent;");
            // Guardar en DB
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setApellido(txtApellido.getText());
            usuarioActual.setCorreo(txtCorreo.getText());
            usuarioActual.setClave(txtClave.getText());
            usuarioDAO.actualizarUsuario(usuarioActual);
        }
    }

    private TextField crearField(String label, String value) {
        VBox b = new VBox(5);
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12;");
        TextField tf = new TextField(value);
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: #E2E8F0; -fx-font-weight: 600;");
        b.getChildren().addAll(l, tf);
        return tf;
    }

    private Button crearBtnEliminar() {
        Button btn = new Button("Eliminar cuenta");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-font-weight: 700; -fx-padding: 12; -fx-background-radius: 12;");
        btn.setCursor(Cursor.HAND);
        btn.setOnAction(e -> {
            usuarioDAO.eliminarUsuario(usuarioActual.getIdUsuario());
            // Aquí deberías redirigir al Login
        });
        return btn;
    }

    // --- SECCIONES DERECHAS ---
    private final VisualizacionDAO visualizacionDAO = new VisualizacionDAO();

    private VBox crearSeccionActividad() {
        VBox section = new VBox(15);
        Label t = new Label("Actividad Reciente");
        t.setStyle("-fx-font-size: 18; -fx-font-weight: 700; -fx-text-fill: #1E293B;");

        // FlowPane permite que las cards se envuelvan si no hay espacio
        FlowPane list = new FlowPane();
        list.setHgap(15);
        list.setVgap(15);

        // Obtenemos los últimos 3 proyectos visitados desde la DB
        List<Proyecto> recientes = visualizacionDAO.listarHistorialReciente(usuarioActual.getIdUsuario(), 3);

        if (recientes.isEmpty()) {
            Label vacio = new Label("No has visitado proyectos recientemente.");
            vacio.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic;");
            list.getChildren().add(vacio);
        } else {
            for (Proyecto p : recientes) {
                CardProyecto card = new CardProyecto(p, CardStyle.MINI);
                // Definimos un ancho fijo para que se vean como "miniaturas" en el perfil
                card.setPrefWidth(200);
                list.getChildren().add(card);
            }
        }

        section.getChildren().addAll(t, list);
        return section;
    }

    private VBox crearSeccionGuardados() {
        VBox section = new VBox(15);
        HBox header = new HBox();
        Label t = new Label("Proyectos Guardados");
        t.setStyle("-fx-font-size: 18; -fx-font-weight: 700; -fx-text-fill: #1E293B;");

        Region s = new Region();
        HBox.setHgrow(s, Priority.ALWAYS);

        // Estilos base
        String colorNormal = "#3F68E4";
        String colorHover = "#1D4ED8"; // Un azul más oscuro para el hover

        Button btnVerTodos = new Button("Ver todos");
        btnVerTodos.setStyle("-fx-background-color: transparent; -fx-text-fill: #3F68E4; -fx-font-weight: 700;");
        btnVerTodos.setCursor(Cursor.HAND);

        // --- ANIMACIÓN DE CAMBIO DE COLOR ---
        btnVerTodos.setOnMouseEntered(e -> {
            btnVerTodos.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: " + colorHover + "; -fx-font-weight: 700; -fx-background-radius: 8;");
        });

        btnVerTodos.setOnMouseExited(e -> {
            btnVerTodos.setStyle("-fx-background-color: transparent; -fx-text-fill: " + colorNormal + "; -fx-font-weight: 700;");
        });

        btnVerTodos.setOnAction(e -> {
            if (Sesion.estaLogueado()) {
                // Llamada sin argumentos para que coincida con lo que Java espera
                MainApp.setView(new Guardados());
            }
        });

        header.getChildren().addAll(t, s, btnVerTodos);

        FlowPane list = new FlowPane();
        list.setHgap(15);
        list.setVgap(15);

        // Listar proyectos guardados desde el DAO de guardados
        List<Proyecto> guardados = guardadoDAO.listarProyectosGuardados(usuarioActual.getIdUsuario());

        if (guardados.isEmpty()) {
            Label vacio = new Label("No tienes proyectos guardados.");
            vacio.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic;");
            list.getChildren().add(vacio);
        } else {
            // Mostramos máximo 3 para mantener la estética
            int limite = Math.min(3, guardados.size());
            for (int i = 0; i < limite; i++) {
                CardProyecto card = new CardProyecto(guardados.get(i), CardStyle.MINI);
                card.setPrefWidth(200);
                list.getChildren().add(card);
            }
        }

        section.getChildren().addAll(header, list);
        return section;
    }

    private VBox crearSeccionCalificaciones() {
        VBox section = new VBox(15);
        Label t = new Label("Calificaciones dadas");
        t.setStyle("-fx-font-size: 18; -fx-font-weight: 700;");

        VBox list = new VBox(10);
        // Ejemplo estático
        list.getChildren().addAll(crearFilaCalificacion("Sistemas Distribuidos", 5), crearFilaCalificacion("Algoritmos", 4));

        section.getChildren().addAll(t, list);
        return section;
    }

    private HBox crearFilaProyecto(Proyecto p) {
        HBox fila = new HBox(15);
        fila.setPadding(new Insets(15));
        fila.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        Label t = new Label(p.getTitulo());
        t.setStyle("-fx-font-weight: 600;");
        fila.getChildren().add(t);
        return fila;
    }

    private HBox crearFilaCalificacion(String nombre, int stars) {
        HBox fila = new HBox(15);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 20, 10, 20));
        fila.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        Label n = new Label(nombre);
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
        HBox estrellas = new HBox(2);
        for(int i=0; i<5; i++) {
            FontIcon star = new FontIcon("fas-star");
            star.setIconColor(i < stars ? Color.web("#F59E0B") : Color.web("#E2E8F0"));
            estrellas.getChildren().add(star);
        }
        fila.getChildren().addAll(n, s, estrellas);
        return fila;
    }

    private VBox crearMiniCard(String titulo, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(180, 100);
        card.setStyle("-fx-background-color: "+color+"; -fx-background-radius: 15;");
        card.setAlignment(Pos.CENTER);
        Label l = new Label(titulo);
        l.setStyle("-fx-text-fill: white; -fx-font-weight: 700;");
        card.getChildren().add(l);
        Animations.attachHoverLift(card);
        return card;
    }
}