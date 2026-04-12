package galeria.view;

import galeria.controller.*;
import galeria.model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // ════════════════════════════════════════════════
        //  SETUP: instanciar controladores
        // ════════════════════════════════════════════════
        ControladorGaleria galeria  = new ControladorGaleria();
        ControladorAdmin   admin    = new ControladorAdmin();
        ControladorLanding landing  = new ControladorLanding(galeria);

        separador("1. PARAMETROS ACADEMICOS (admin)");

        galeria.agregarFacultad("Ingenieria");
        galeria.agregarFacultad("Ciencias Economicas");
        galeria.agregarPrograma("Ing. de Sistemas", 1);
        galeria.agregarPrograma("Contaduria Publica", 2);
        galeria.agregarMateria("Programacion III", 1);
        galeria.agregarMateria("Finanzas Corporativas", 2);
        galeria.agregarSemestre(2025, 1, 1);
        galeria.agregarSemestre(2025, 2, 1);
        galeria.agregarCategoria("Desarrollo Web", "Apps y sitios web");
        galeria.agregarCategoria("Inteligencia Artificial", "Modelos y automatizacion");
        galeria.agregarCategoria("Finanzas", "Analisis financiero");

        separador("2. REGISTRO DE USUARIOS");

        // Contraseñas se guardan tal como las pasa el constructor (sin encriptar en este flujo)
        Usuario irene = new Usuario(1, "Irene",  "Romero",   "irene@correo.com",   "clave123", "avatar1.png", false, new ArrayList<>(), new ArrayList<>());
        Usuario carlos= new Usuario(2, "Carlos", "Mendez",   "carlos@correo.com",  "clave456", "avatar2.png", false, new ArrayList<>(), new ArrayList<>());
        Usuario adminU= new Usuario(3, "Ana",    "Perez",    "ana@correo.com",     "admin999", "avatar3.png", true,  new ArrayList<>(), new ArrayList<>());

        admin.registrarUsuario(irene);
        admin.registrarUsuario(carlos);
        admin.registrarUsuario(adminU);

        separador("3. LISTA DE USUARIOS (vista admin)");
        admin.mostrarListaUsuarios();

        separador("4. CAMBIAR ESTADO ADMIN");
        admin.cambiarEstadoAdmin(2, true);
        admin.mostrarListaUsuarios();

        separador("5. AGREGAR PROYECTOS (solo admin puede)");

        Facultad fac1 = galeria.listarFacultades().get(0);
        Programa pro1 = galeria.listarProgramas().get(0);
        Materia  mat1 = galeria.listarMaterias().get(0);
        Semestre sem1 = galeria.listarSemestres().get(0);
        Categoria cat1= galeria.listarCategorias().get(0);
        Categoria cat2= galeria.listarCategorias().get(1);

        Proyecto p1 = new Proyecto(101, "Galeria de Proyectos PI",
            "Sistema web para gestionar proyectos integradores",
            "http://archivos.uni.edu/proyecto101.pdf", "http://img/p101.png",
            0, 0, new Date(), new ArrayList<>(), new ArrayList<>(),
            fac1, pro1, mat1, sem1, cat1);

        Proyecto p2 = new Proyecto(102, "Modelo de Prediccion Financiera",
            "Uso de IA para predecir flujos de caja empresariales",
            "http://archivos.uni.edu/proyecto102.pdf", "http://img/p102.png",
            0, 0, new Date(), new ArrayList<>(), new ArrayList<>(),
            fac1, pro1, mat1, sem1, cat2);

        Proyecto p3 = new Proyecto(103, "App de Gestion de Inventarios",
            "Aplicacion movil para control de stock en PYMES",
            "http://archivos.uni.edu/proyecto103.pdf", "http://img/p103.png",
            0, 0, new Date(), new ArrayList<>(), new ArrayList<>(),
            fac1, pro1, mat1, sem1, cat1);

        galeria.agregarProyecto(p1);
        galeria.agregarProyecto(p2);
        galeria.agregarProyecto(p3);

        separador("6. AGREGAR AUTORES Y RECURSOS A UN PROYECTO");

        Autor autor1 = new Autor(1, "Irene Romero",  "irene@correo.com");
        Autor autor2 = new Autor(2, "Carlos Mendez", "carlos@correo.com");
        p1.getListaAutores().add(autor1);
        p1.getListaAutores().add(autor2);

        Recurso rec1 = new Recurso(1, "Video demo", "http://youtube.com/demo", "video", new Date());
        Recurso rec2 = new Recurso(2, "Repositorio", "http://github.com/proyecto", "enlace", new Date());
        p1.getListaRecursos().add(rec1);
        p1.getListaRecursos().add(rec2);
        System.out.println("[OK] Autores y recursos adjuntados al proyecto: " + p1.getTitulo());

        separador("7. CATALOGO COMPLETO");
        listarProyectos(galeria.consultarCatalogo());

        separador("8. FILTRAR PROYECTOS");
        FiltroProyecto filtro = new FiltroProyecto(1, null, null, null, null, null, null);
        List<Proyecto> filtrados = galeria.filtrarProyectos(filtro);
        System.out.println("Proyectos de la facultad 1: " + filtrados.size());
        listarProyectos(filtrados);

        System.out.println("\nBusqueda por texto 'IA':");
        FiltroProyecto filtroBusqueda = new FiltroProyecto(null, null, null, null, null, "IA", null);
        listarProyectos(galeria.filtrarProyectos(filtroBusqueda));

        separador("9. VER DETALLE Y DESCARGAR ARCHIVO");
        p1.visualizarDetalle();
        galeria.solicitarDescarga(p1);

        separador("10. USUARIO: GUARDAR PROYECTOS");
        irene.guardarProyecto(p1);
        irene.guardarProyecto(p2);
        irene.guardarProyecto(p1); // intento duplicado — debe avisar

        separador("11. USUARIO: VALORAR PROYECTOS");
        irene.valorarProyecto(p1, 5);
        irene.valorarProyecto(p2, 4);
        irene.valorarProyecto(p1, 6); // fuera de rango — debe dar error

        separador("12. USUARIO: REGISTRAR VISTAS");
        irene.registrarVista(p1);
        irene.registrarVista(p2);
        irene.registrarVista(p3);
        p1.setCantidadVistas(45);
        p2.setCantidadVistas(120);
        p3.setCantidadVistas(30);

        separador("13. USUARIO: VER PERFIL");
        irene.verPerfil();

        separador("14. USUARIO: VER GUARDADOS");
        System.out.println("Guardados de " + irene.getNombre() + ":");
        for (Guardado g : irene.getListaGuardados())
            System.out.println("  - " + g.getProyecto().getTitulo() +
                               " | Guardado: " + g.getFechaGuardado());

        separador("15. USUARIO: ELIMINAR UN GUARDADO");
        irene.eliminarProyecto(p2.getIdProyecto());
        System.out.println("Guardados restantes: " + irene.getListaGuardados().size());

        separador("16. LANDING PAGE");
        landing.mostrarLanding();

        separador("17. EDITAR PROYECTO (admin)");
        Proyecto p1Editado = new Proyecto(101, "Galeria PI - v2.0",
            "Version mejorada del sistema de gestion",
            "http://archivos.uni.edu/proyecto101v2.pdf", "http://img/p101v2.png",
            p1.getCantidadVistas(), p1.getCantidadGuardados(), new Date(),
            p1.getListaAutores(), p1.getListaRecursos(),
            fac1, pro1, mat1, sem1, cat1);
        galeria.editarProyecto(101, p1Editado);

        separador("18. ELIMINAR PROYECTO (admin)");
        galeria.eliminarProyecto(103);
        System.out.println("Proyectos en catalogo: " + galeria.consultarCatalogo().size());

        separador("19. ELIMINAR USUARIO (admin)");
        admin.eliminarUsuario(2);
        admin.mostrarListaUsuarios();

        separador("20. USUARIO: ELIMINAR MI CUENTA");
        irene.eliminarMiCuenta();

        separador("=== PRUEBA COMPLETA FINALIZADA ===");
    }

    // ── Utilidades de consola ──────────────────────────────
    private static void separador(String titulo) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("  " + titulo);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    private static void listarProyectos(List<Proyecto> lista) {
        if (lista.isEmpty()) {
            System.out.println("  (sin resultados)");
            return;
        }
        for (Proyecto p : lista) {
            System.out.println("  [" + p.getIdProyecto() + "] " + p.getTitulo() +
                               " | Cat: " + (p.getCategoria() != null ? p.getCategoria().getNombreCategoria() : "N/A") +
                               " | Vistas: " + p.getCantidadVistas());
        }
    }
}