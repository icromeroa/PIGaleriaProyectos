package galeria.components.views;

import galeria.dao.ProyectoDAO;
import galeria.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

public class SobreNosotras extends ScrollPane {

    private final String COLOR_AZUL = "#3F68E4";
    private final String COLOR_NARANJA = "#F59E0B";
    private final String COLOR_TEXTO_TITULO = "#0F172A";
    private final String COLOR_TEXTO_DESC = "#64748B";
    private final String FONDO_LIGERO = "#F8FAFC";
    private final String GITHUB_URL = "https://github.com/icromeroa/PIGaleriaProyectos.git";

    public SobreNosotras() {
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: white; -fx-background: white; -fx-border-color: transparent;");

        VBox mainContainer = new VBox(80);
        mainContainer.setPadding(new Insets(40, 80, 100, 80));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: white;");

        // --- SECCIONES ---
        VBox hero = crearHero();
        VBox equipo = crearSeccionEquipo();
        VBox especificaciones = crearSeccionEspecificaciones();

        mainContainer.getChildren().addAll(hero, equipo, especificaciones);
        this.setContent(mainContainer);

        // --- ANIMACIONES DE ENTRADA INMEDIATA ---
        Animations.slideUpFadeIn(hero, 0);

        // Animamos solo el encabezado del equipo (título y descripción) de inmediato
        VBox textosEquipo = (VBox) equipo.getChildren().get(0);
        Animations.slideUpFadeIn(textosEquipo, 200);
    }

    private VBox crearHero() {
        VBox hero = new VBox(25);
        hero.setPadding(new Insets(60));
        hero.setStyle("-fx-background-color: " + FONDO_LIGERO + "; -fx-background-radius: 40;");
        hero.setMaxWidth(1100);

        Label lblTag = new Label("EXCELENCIA ACADÉMICA");
        lblTag.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: " + COLOR_AZUL + "; -fx-padding: 6 15; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 12;");

        Label lblTitulo = new Label("Innovación Académica:\nUniRepo");
        lblTitulo.setStyle("-fx-font-size: 52; -fx-font-weight: 900; -fx-text-fill: " + COLOR_TEXTO_TITULO + "; -fx-line-spacing: -5;");

        Label lblDesc = new Label("UniRepo es una plataforma de vanguardia diseñada para centralizar, preservar y exhibir el talento creativo de nuestra comunidad universitaria.");
        lblDesc.setStyle("-fx-font-size: 18; -fx-text-fill: " + COLOR_TEXTO_DESC + "; -fx-line-spacing: 5;");
        lblDesc.setWrapText(true);

        HBox hbStats = new HBox(50);
        hbStats.setPadding(new Insets(20, 0, 0, 0));

        VBox statProyectos = new VBox(-5);
        Label lblNumP = new Label("0");
        lblNumP.setStyle("-fx-font-size: 36; -fx-font-weight: 900; -fx-text-fill: " + COLOR_AZUL + ";");
        Label lblTxtP = new Label("Proyectos");
        lblTxtP.setStyle("-fx-font-size: 14; -fx-text-fill: " + COLOR_TEXTO_DESC + ";");
        statProyectos.getChildren().addAll(lblNumP, lblTxtP);

        // Animación de conteo al hacer scroll sobre los números
        Animations.animateOnScroll(lblNumP, this, () ->
                Animations.animarConteo(lblNumP, new ProyectoDAO().getEstadisticasGenerales()[1], "-fx-font-size: 36; -fx-font-weight: 900; -fx-text-fill: " + COLOR_AZUL + ";")
        );

        VBox statDigital = crearStatSimple("100%", "Digital", COLOR_NARANJA);
        hbStats.getChildren().addAll(statProyectos, statDigital);

        hero.getChildren().addAll(lblTag, lblTitulo, lblDesc, hbStats);
        return hero;
    }

    private VBox crearSeccionEquipo() {
        VBox container = new VBox(40);
        container.setAlignment(Pos.CENTER);

        // Encabezado (Animación de entrada normal)
        VBox textos = new VBox(10);
        textos.setAlignment(Pos.CENTER);
        Label titulo = new Label("Nuestro Equipo");
        titulo.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_TITULO + ";");
        Label sub = new Label("Estudiantes de cuarto semestre de Ingeniería de Sistemas apasionados por la tecnología.");
        sub.setStyle("-fx-text-fill: " + COLOR_TEXTO_DESC + "; -fx-font-size: 16;");
        textos.getChildren().addAll(titulo, sub);

        // Cards (Animación de Scroll)
        HBox hbCards = new HBox(30);
        hbCards.setAlignment(Pos.CENTER);

        VBox card1 = crearCardIntegrante("IR", "Irene Romero Avendaño");
        VBox card2 = crearCardIntegrante("MG", "Michelle Guzman Angarita");
        VBox card3 = crearCardIntegrante("HL", "Helen Lavao Benitez");

        hbCards.getChildren().addAll(card1, card2, card3);

        // Disparamos la animación de las cards solo cuando el contenedor de las cards sea visible
        Animations.animateOnScroll(hbCards, this, () -> {
            Animations.slideUpFadeIn(card1, 0);
            Animations.slideUpFadeIn(card2, 200);
            Animations.slideUpFadeIn(card3, 400);
        });

        container.getChildren().addAll(textos, hbCards);
        return container;
    }

    private VBox crearCardIntegrante(String iniciales, String nombre) {
        VBox card = new VBox(15);
        card.setOpacity(0); // Oculta inicialmente para el scroll reveal
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(40, 30, 40, 30));
        card.setPrefSize(320, 350);

        card.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 30; " +
                "-fx-border-radius: 30; " +
                "-fx-border-color: #F1F5F9; " +
                "-fx-border-width: 2;");

        StackPane avatar = new StackPane();
        Circle circulo = new Circle(35, Color.web("#F1F5F9"));
        Label lblIniciales = new Label(iniciales);
        lblIniciales.setStyle("-fx-font-weight: 800; -fx-font-size: 22; -fx-text-fill: " + COLOR_TEXTO_TITULO + ";");
        avatar.getChildren().addAll(circulo, lblIniciales);
        avatar.setAlignment(Pos.CENTER);

        HBox avatarWrapper = new HBox(avatar);
        avatarWrapper.setAlignment(Pos.CENTER_LEFT);

        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_TITULO + ";");

        Label lblCarrera = new Label("Ingeniería de Sistemas");
        lblCarrera.setStyle("-fx-font-size: 14; -fx-text-fill: " + COLOR_TEXTO_DESC + ";");

        HBox redes = new HBox(15);
        FontIcon iconLink = new FontIcon("fas-link");
        FontIcon iconMail = new FontIcon("fas-envelope");

        for(FontIcon i : new FontIcon[]{iconLink, iconMail}) {
            i.setIconColor(Color.web(COLOR_TEXTO_DESC));
            i.setIconSize(18);
            i.setCursor(Cursor.HAND);
        }

        redes.getChildren().addAll(iconLink, iconMail);
        card.getChildren().addAll(avatarWrapper, lblNombre, lblCarrera, redes);

        Animations.attachHoverLift(card);
        return card;
    }

    private VBox crearSeccionEspecificaciones() {
        VBox container = new VBox(25);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: #D9E0F9; -fx-background-radius: 30;");
        container.setMaxWidth(1100);

        Label titulo = new Label("Especificaciones Académicas");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: " + COLOR_AZUL + ";");

        Region line = new Region();
        line.setPrefSize(80, 3);
        line.setStyle("-fx-background-color: " + COLOR_NARANJA + "; -fx-background-radius: 5;");
        line.setMaxWidth(80);

        HBox hbItems = new HBox(20);
        hbItems.setPadding(new Insets(20, 0, 0, 0));

        VBox item1 = crearItemEspec("fas-code", "MATERIA", "Programación Orientada a Objetos");
        VBox item2 = crearItemEspec("fas-calendar-alt", "SEMESTRE", "2026-1");
        VBox item3 = crearItemEspec("fas-map-marker-alt", "UBICACIÓN", "Bogotá, Colombia");

        hbItems.getChildren().addAll(item1, item2, item3);

        // Animación de scroll escalonada para esta sección
        Animations.animateOnScroll(container, this, () -> {
            Animations.slideUpFadeIn(titulo, 0);
            Animations.slideUpFadeIn(line, 100);
            Animations.slideUpFadeIn(item1, 200);
            Animations.slideUpFadeIn(item2, 400);
            Animations.slideUpFadeIn(item3, 600);
        });

        container.getChildren().addAll(titulo, line, hbItems);
        return container;
    }

    private VBox crearItemEspec(String iconCode, String top, String bottom) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        HBox.setHgrow(box, Priority.ALWAYS);

        FontIcon icon = new FontIcon(iconCode);
        icon.setIconSize(22);
        icon.setIconColor(Color.web(COLOR_AZUL));

        VBox textos = new VBox(2);
        Label lblTop = new Label(top);
        lblTop.setStyle("-fx-font-size: 10; -fx-text-fill: " + COLOR_TEXTO_DESC + "; -fx-font-weight: bold;");
        Label lblBottom = new Label(bottom);
        lblBottom.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_TITULO + ";");

        textos.getChildren().addAll(lblTop, lblBottom);
        box.getChildren().addAll(icon, textos);

        VBox wrapper = new VBox(box);
        wrapper.setOpacity(0);
        HBox.setHgrow(wrapper, Priority.ALWAYS);
        return wrapper;
    }

    private VBox crearStatSimple(String num, String label, String color) {
        Label lblNum = new Label(num);
        lblNum.setStyle("-fx-font-size: 36; -fx-font-weight: 900; -fx-text-fill: " + color + ";");
        Label lblTxt = new Label(label);
        lblTxt.setStyle("-fx-font-size: 14; -fx-text-fill: " + COLOR_TEXTO_DESC + ";");
        return new VBox(-5, lblNum, lblTxt);
    }
}