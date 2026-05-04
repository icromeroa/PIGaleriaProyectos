package galeria.components.views;

import galeria.app.MainApp;
import galeria.components.interfaz.CardProyecto;
import galeria.dao.*;
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
    private final VisualizacionDAO visualizacionDAO = new VisualizacionDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();

    private Usuario usuarioActual;
    private boolean modoEdicion = false;
    private TextField txtNombre, txtApellido, txtCorreo;
    private PasswordField txtClave;

    // Variables para paginación de calificaciones
    private int paginaActualCalificaciones = 0;
    private final int TAMANO_PAGINA = 2;
    private VBox contenedorListaCalificaciones;

    public Perfil(Usuario usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: #F8FAFC;");

        VBox root = new VBox(40);
        root.setPadding(new Insets(40, 60, 40, 60));

        VBox header = crearCabecera();
        HBox body = new HBox(40);

        // LADO IZQUIERDO
        VBox leftCol = new VBox(30);
        leftCol.prefWidthProperty().bind(body.widthProperty().multiply(0.38));
        VBox statsPanel = crearPanelEstadisticas();
        VBox infoPanel = crearPanelInformacion();
        Button btnEliminar = crearBtnEliminar();
        leftCol.getChildren().addAll(statsPanel, infoPanel, btnEliminar);

        // LADO DERECHO
        VBox rightCol = new VBox(30);
        rightCol.prefWidthProperty().bind(body.widthProperty().multiply(0.58));
        VBox actividadReciente = crearSeccionActividad();
        VBox proyectosGuardados = crearSeccionGuardados();
        VBox calificaciones = crearSeccionCalificaciones();
        rightCol.getChildren().addAll(actividadReciente, proyectosGuardados, calificaciones);

        body.getChildren().addAll(leftCol, rightCol);
        root.getChildren().addAll(header, body);
        this.setContent(root);

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

        StackPane avatar = new StackPane();
        Circle circulo = new Circle(45, Color.web("#E2E8F0"));
        String iniciales = (usuarioActual.getNombre().substring(0,1) + usuarioActual.getApellido().substring(0,1)).toUpperCase();
        Label lblInic = new Label(iniciales);
        lblInic.setStyle("-fx-font-size: 24; -fx-font-weight: 800; -fx-text-fill: #64748B;");
        avatar.getChildren().addAll(circulo, lblInic);

        VBox textoWelcome = new VBox(8);
        Label saludo = new Label("¡Hola, " + usuarioActual.getNombre() + " " + usuarioActual.getApellido() + "!");
        saludo.setStyle("-fx-font-size: 22; -fx-font-weight: 700; -fx-text-fill: #1E293B;");

        Label desc = new Label("Este es tu rincón privado. Mantén tus datos al día, revisa tu historial y gestiona tus calificaciones.");
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14;");
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
        grid.setHgap(15); grid.setVgap(15);

        int id = usuarioActual.getIdUsuario();
        int numPublicaciones = proyectoDAO.contarPublicacionesPorCorreo(usuarioActual.getCorreo());
        int numGuardados = guardadoDAO.listarProyectosGuardados(id).size();
        int numCalificados = valoracionDAO.contarValoracionesPorUsuario(id);
        int numVistas = visualizacionDAO.contarTotalVistasUsuario(id);

        grid.add(crearItemStat("Publicaciones", String.valueOf(numPublicaciones), "#3F68E4"), 0, 0);
        grid.add(crearItemStat("Guardados", String.valueOf(numGuardados), "#3F68E4"), 1, 0);
        grid.add(crearItemStat("Calificados", String.valueOf(numCalificados), "#3F68E4"), 0, 1);
        grid.add(crearItemStat("Vistas", String.valueOf(numVistas), "#3F68E4"), 1, 1);

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
        if (modoEdicion) {
            icon.setIconCode(org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.SAVE);
            txtNombre.setEditable(true); txtApellido.setEditable(true);
            txtCorreo.setEditable(true); txtClave.setEditable(true);
            txtNombre.setStyle("-fx-background-color: #F1F5F9;");
        } else {
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setApellido(txtApellido.getText());
            usuarioActual.setCorreo(txtCorreo.getText());
            usuarioActual.setClave(txtClave.getText());
            usuarioDAO.actualizarUsuario(usuarioActual);

            // RECARGA DE PÁGINA PARA ACTUALIZAR INFO
            MainApp.setView(new Perfil(usuarioActual));
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
        btn.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 12; -fx-background-radius: 12;");
        btn.setCursor(Cursor.HAND);
        btn.setOnAction(e -> {
            usuarioDAO.eliminarUsuario(usuarioActual.getIdUsuario());
            Sesion.cerrar();
            // Redirigir al login según tu lógica de MainApp
        });
        return btn;
    }

    private VBox crearSeccionActividad() {
        VBox section = new VBox(15);
        Label t = new Label("Actividad Reciente");
        t.setStyle("-fx-font-size: 18; -fx-font-weight: 700; -fx-text-fill: #1E293B;");
        FlowPane list = new FlowPane(15, 15);
        List<Proyecto> recientes = visualizacionDAO.listarHistorialReciente(usuarioActual.getIdUsuario(), 3);
        if (recientes.isEmpty()) {
            Label vacio = new Label("No has visitado proyectos.");
            vacio.setStyle("-fx-text-fill: #94A3B8;");
            list.getChildren().add(vacio);
        } else {
            for (Proyecto p : recientes) {
                CardProyecto card = new CardProyecto(p, CardStyle.MINI);
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
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);

        Button btnVerTodos = new Button("Ver todos");
        btnVerTodos.setStyle("-fx-background-color: transparent; -fx-text-fill: #3F68E4; -fx-font-weight: 700;");
        btnVerTodos.setOnAction(e -> MainApp.setView(new Guardados()));

        header.getChildren().addAll(t, s, btnVerTodos);
        FlowPane list = new FlowPane(15, 15);
        List<Proyecto> guardados = guardadoDAO.listarProyectosGuardados(usuarioActual.getIdUsuario());
        int limite = Math.min(3, guardados.size());
        for (int i = 0; i < limite; i++) {
            CardProyecto card = new CardProyecto(guardados.get(i), CardStyle.MINI);
            card.setPrefWidth(200);
            list.getChildren().add(card);
        }
        section.getChildren().addAll(header, list);
        return section;
    }

    private VBox crearSeccionCalificaciones() {
        VBox section = new VBox(15);

        HBox header = new HBox();
        Label t = new Label("Calificaciones dadas");
        t.setStyle("-fx-font-size: 18; -fx-font-weight: 700; -fx-text-fill: #1E293B;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Flechas de navegación
        HBox nav = new HBox(10);
        Button btnPrev = new Button();
        btnPrev.setGraphic(new FontIcon("fas-chevron-left"));
        Button btnNext = new Button();
        btnNext.setGraphic(new FontIcon("fas-chevron-right"));

        String btnStyle = "-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);";
        btnPrev.setStyle(btnStyle); btnNext.setStyle(btnStyle);
        btnPrev.setCursor(Cursor.HAND); btnNext.setCursor(Cursor.HAND);

        nav.getChildren().addAll(btnPrev, btnNext);
        header.getChildren().addAll(t, spacer, nav);

        contenedorListaCalificaciones = new VBox(10);
        actualizarListaCalificaciones();

        btnPrev.setOnAction(e -> {
            if (paginaActualCalificaciones > 0) {
                paginaActualCalificaciones--;
                actualizarListaCalificaciones();
            }
        });

        btnNext.setOnAction(e -> {
            List<Valoracion> todas = valoracionDAO.listarValoracionesPorUsuario(usuarioActual.getIdUsuario());
            if ((paginaActualCalificaciones + 1) * TAMANO_PAGINA < todas.size()) {
                paginaActualCalificaciones++;
                actualizarListaCalificaciones();
            }
        });

        section.getChildren().addAll(header, contenedorListaCalificaciones);
        return section;
    }

    private void actualizarListaCalificaciones() {
        contenedorListaCalificaciones.getChildren().clear();
        List<Valoracion> todas = valoracionDAO.listarValoracionesPorUsuario(usuarioActual.getIdUsuario());

        if (todas.isEmpty()) {
            Label vacio = new Label("Aún no has calificado ningún proyecto.");
            vacio.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic;");
            contenedorListaCalificaciones.getChildren().add(vacio);
            return;
        }

        int inicio = paginaActualCalificaciones * TAMANO_PAGINA;
        int fin = Math.min(inicio + TAMANO_PAGINA, todas.size());

        for (int i = inicio; i < fin; i++) {
            Valoracion v = todas.get(i);
            Proyecto p = proyectoDAO.buscarPorId(v.getProyecto().getIdProyecto());

            VBox filaAnimada = crearFilaCalificacionInteractiva(p, v);
            contenedorListaCalificaciones.getChildren().add(filaAnimada);

            // Animación individual de entrada para cada fila (escalonada)
            Animations.slideUpFadeIn(filaAnimada, (i - inicio) * 100);
        }
    }

    private VBox crearFilaCalificacionInteractiva(Proyecto p, Valoracion v) {
        VBox contenedorFila = new VBox(5);
        contenedorFila.setOpacity(0); // Para la animación de entrada

        HBox fila = new HBox(15);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(12, 20, 12, 20));
        fila.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 5, 0, 0, 2);");

        Label n = new Label(p != null ? p.getTitulo() : "Proyecto desconocido");
        n.setStyle("-fx-font-weight: 700; -fx-text-fill: #3F68E4;");
        n.setCursor(Cursor.HAND);
        n.setOnMouseClicked(e -> MainApp.setView(new DetalleProyecto(p, usuarioActual)));

        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);

        HBox estrellas = new HBox(2);
        for(int i=0; i<5; i++) {
            FontIcon star = new FontIcon("fas-star");
            star.setIconColor(i < v.getPuntuacion() ? Color.web("#F59E0B") : Color.web("#E2E8F0"));
            estrellas.getChildren().add(star);
        }

        fila.getChildren().addAll(n, s, estrellas);

        // Botón Eliminar
        Button btnEliminarVal = new Button("Eliminar calificación");
        btnEliminarVal.setGraphic(new FontIcon("fas-trash-alt"));
        btnEliminarVal.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 11; -fx-font-weight: 600;");
        btnEliminarVal.setCursor(Cursor.HAND);

        btnEliminarVal.setOnAction(e -> {
            // 1. ELIMINAR REALMENTE DE LA BASE DE DATOS
            valoracionDAO.eliminarValoracion(v.getIdValoracion());

            // 2. LOGICA DE RECALCULO DE PAGINACION
            // Si eliminamos el último elemento de una página, retrocedemos una
            List<Valoracion> restantes = valoracionDAO.listarValoracionesPorUsuario(usuarioActual.getIdUsuario());
            if (paginaActualCalificaciones > 0 && (paginaActualCalificaciones * TAMANO_PAGINA) >= restantes.size()) {
                paginaActualCalificaciones--;
            }

            // 3. REFRESCAR TODA LA VISTA (Para actualizar contadores de estadísticas y lista)
            MainApp.setView(new Perfil(usuarioActual));
        });

        // Efecto hover simple para el botón eliminar
        btnEliminarVal.setOnMouseEntered(e -> btnEliminarVal.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-font-size: 11; -fx-font-weight: 600; -fx-background-radius: 5;"));
        btnEliminarVal.setOnMouseExited(e -> btnEliminarVal.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 11; -fx-font-weight: 600;"));

        contenedorFila.getChildren().addAll(fila, btnEliminarVal);
        return contenedorFila;
    }

}