package galeria.view;

import galeria.controller.*;
import galeria.model.*;
import java.util.*;

public class Main {

    static Scanner sc       = new Scanner(System.in);
    static ControladorGaleria galeria = new ControladorGaleria();
    static ControladorAdmin   admin   = new ControladorAdmin();
    static ControladorLanding landing = new ControladorLanding(galeria);

    // Registro de acciones para el resumen final
    static List<String> bitacora = new ArrayList<>();

    public static void main(String[] args) {

        cargarDatosDePrueba();

        boolean ejecutando = true;
        while (ejecutando) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("   REPOSITORIO DE PROYECTOS INTEGRADORES");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("  [1] Registrarse");
            System.out.println("  [2] Iniciar sesion");
            System.out.println("  [3] Salir");
            System.out.print("  Opcion: ");
            int op = leerInt();

            switch (op) {
                case 1: registrarse();        break;
                case 2: iniciarSesion();      break;
                case 3: ejecutando = false;   break;
                default: System.out.println("[ERROR] Opcion invalida.");
            }
        }

        mostrarResumenFinal();
        sc.close();
    }

    // ════════════════════════════════════════════════
    //  REGISTRO
    // ════════════════════════════════════════════════
    static void registrarse() {
        System.out.println("\n-- REGISTRO DE NUEVO USUARIO --");
        System.out.print("Nombre    : "); String nombre   = sc.nextLine();
        System.out.print("Apellido  : "); String apellido = sc.nextLine();
        System.out.print("Correo    : "); String correo   = sc.nextLine();
        System.out.print("Clave     : "); String clave    = sc.nextLine();

        if (admin.buscarUsuarioPorCorreo(correo) != null) {
            System.out.println("[ERROR] Ya existe un usuario con ese correo.");
            return;
        }

        int nuevoId = admin.getListaUsuarios().size() + 1;
        Usuario nuevo = new Usuario(nuevoId, nombre, apellido, correo, clave,
                                    "", false, new ArrayList<>(), new ArrayList<>());
        admin.registrarUsuario(nuevo);
        bitacora.add("Usuario registrado: " + nombre + " " + apellido);
    }

    // ════════════════════════════════════════════════
    //  INICIO DE SESION
    // ════════════════════════════════════════════════
    static void iniciarSesion() {
        System.out.println("\n-- INICIAR SESION --");
        System.out.print("Correo: "); String correo = sc.nextLine();
        System.out.print("Clave : "); String clave  = sc.nextLine();

        Usuario u = admin.iniciarSesion(correo, clave);
        if (u == null) return;

        bitacora.add("Sesion iniciada: " + u.getNombre() + " (admin=" + u.getEsAdmin() + ")");

        if (u.getEsAdmin()) {
            menuAdmin(u);
        } else {
            menuUsuario(u);
        }
    }

    // ════════════════════════════════════════════════
    //  MENU USUARIO NORMAL
    // ════════════════════════════════════════════════
    static void menuUsuario(Usuario u) {
        boolean activo = true;
        while (activo) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("   Hola, " + u.getNombre() + " | MENU USUARIO");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("  [1] Ver landing (proyectos destacados)");
            System.out.println("  [2] Ver catalogo completo");
            System.out.println("  [3] Filtrar proyectos");
            System.out.println("  [4] Ver detalle de un proyecto");
            System.out.println("  [5] Guardar proyecto");
            System.out.println("  [6] Ver mis guardados");
            System.out.println("  [7] Eliminar un guardado");
            System.out.println("  [8] Valorar un proyecto");
            System.out.println("  [9] Ver mi perfil");
            System.out.println("  [10] Editar mi cuenta");
            System.out.println("  [11] Eliminar mi cuenta");
            System.out.println("  [0] Cerrar sesion");
            System.out.print("  Opcion: ");
            int op = leerInt();

            switch (op) {
                case 1:  landing.mostrarLanding();                       bitacora.add(u.getNombre() + " vio el landing.");           break;
                case 2:  mostrarCatalogo();                              bitacora.add(u.getNombre() + " vio el catalogo.");          break;
                case 3:  filtrarProyectos(u);                                                                                        break;
                case 4:  verDetalleProyecto(u);                                                                                      break;
                case 5:  guardarProyecto(u);                                                                                         break;
                case 6:  verMisGuardados(u);                             bitacora.add(u.getNombre() + " vio sus guardados.");        break;
                case 7:  eliminarGuardado(u);                                                                                        break;
                case 8:  valorarProyecto(u);                                                                                         break;
                case 9:  u.verPerfil();                                  bitacora.add(u.getNombre() + " vio su perfil.");            break;
                case 10: editarCuenta(u);                                                                                            break;
                case 11:
                    u.eliminarMiCuenta();
                    bitacora.add(u.getNombre() + " elimino su cuenta.");
                    activo = false;
                    break;
                case 0:
                    System.out.println("[OK] Sesion cerrada.");
                    bitacora.add(u.getNombre() + " cerro sesion.");
                    activo = false;
                    break;
                default: System.out.println("[ERROR] Opcion invalida.");
            }
        }
    }

    // ════════════════════════════════════════════════
    //  MENU ADMINISTRADOR
    // ════════════════════════════════════════════════
    static void menuAdmin(Usuario u) {
        boolean activo = true;
        while (activo) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("   MENU ADMINISTRADOR | " + u.getNombre());
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("  -- Proyectos --");
            System.out.println("  [1] Agregar proyecto");
            System.out.println("  [2] Editar proyecto");
            System.out.println("  [3] Eliminar proyecto");
            System.out.println("  [4] Ver catalogo");
            System.out.println("  -- Parametros academicos --");
            System.out.println("  [5] Gestionar facultades");
            System.out.println("  [6] Gestionar programas");
            System.out.println("  [7] Gestionar materias");
            System.out.println("  [8] Gestionar semestres");
            System.out.println("  [9] Gestionar categorias");
            System.out.println("  -- Usuarios --");
            System.out.println("  [10] Ver lista de usuarios");
            System.out.println("  [11] Cambiar estado admin");
            System.out.println("  [12] Eliminar usuario");
            System.out.println("  -- Landing --");
            System.out.println("  [13] Ver landing");
            System.out.println("  [0]  Cerrar sesion");
            System.out.print("  Opcion: ");
            int op = leerInt();

            switch (op) {
                case 1:  agregarProyecto();                                              bitacora.add("Admin agrego un proyecto.");          break;
                case 2:  editarProyecto();                                               bitacora.add("Admin edito un proyecto.");           break;
                case 3:  eliminarProyecto();                                             bitacora.add("Admin elimino un proyecto.");         break;
                case 4:  mostrarCatalogo();                                                                                                  break;
                case 5:  menuFacultades();                                                                                                   break;
                case 6:  menuProgramas();                                                                                                    break;
                case 7:  menuMaterias();                                                                                                     break;
                case 8:  menuSemestres();                                                                                                    break;
                case 9:  menuCategorias();                                                                                                   break;
                case 10: admin.mostrarListaUsuarios();                                   bitacora.add("Admin vio lista de usuarios.");       break;
                case 11: cambiarAdmin();                                                 bitacora.add("Admin cambio estado de admin.");      break;
                case 12: eliminarUsuario();                                              bitacora.add("Admin elimino un usuario.");          break;
                case 13: landing.mostrarLanding();                                       bitacora.add("Admin vio el landing.");              break;
                case 0:
                    System.out.println("[OK] Sesion cerrada.");
                    bitacora.add("Admin " + u.getNombre() + " cerro sesion.");
                    activo = false;
                    break;
                default: System.out.println("[ERROR] Opcion invalida.");
            }
        }
    }

    // ════════════════════════════════════════════════
    //  ACCIONES DE USUARIO
    // ════════════════════════════════════════════════
    static void mostrarCatalogo() {
        List<Proyecto> lista = galeria.consultarCatalogo();
        if (lista.isEmpty()) { System.out.println("  (sin proyectos)"); return; }
        System.out.println("\n-- CATALOGO --");
        for (Proyecto p : lista)
            System.out.println("  [" + p.getIdProyecto() + "] " + p.getTitulo()
                + " | Vistas: " + p.getCantidadVistas()
                + " | Rating: " + String.format("%.1f", p.valoracionPromedio())
                + " | Destacado: " + (p.esDestacado() ? "Si" : "No"));
    }

    static void filtrarProyectos(Usuario u) {
        System.out.println("\n-- FILTRAR PROYECTOS --");
        System.out.println("Deja en blanco para ignorar un filtro.");
        System.out.print("ID Facultad  : "); Integer idFac = leerIntOpcional();
        System.out.print("ID Programa  : "); Integer idPro = leerIntOpcional();
        System.out.print("ID Materia   : "); Integer idMat = leerIntOpcional();
        System.out.print("ID Semestre  : "); Integer idSem = leerIntOpcional();
        System.out.print("ID Categoria : "); Integer idCat = leerIntOpcional();
        System.out.print("Texto buscar : "); String texto  = sc.nextLine();

        FiltroProyecto filtro = new FiltroProyecto(idFac, idPro, idMat, idSem, idCat,
                                                    texto.isEmpty() ? null : texto, null);
        List<Proyecto> resultado = galeria.filtrarProyectos(filtro);
        System.out.println("Resultados: " + resultado.size());
        for (Proyecto p : resultado)
            System.out.println("  [" + p.getIdProyecto() + "] " + p.getTitulo());
        bitacora.add(u.getNombre() + " filtro proyectos. Resultados: " + resultado.size());
    }

    static void verDetalleProyecto(Usuario u) {
        mostrarCatalogo();
        System.out.print("ID del proyecto a ver: ");
        int id = leerInt();
        Proyecto p = galeria.buscarProyectoPorId(id);
        if (p == null) { System.out.println("[ERROR] Proyecto no encontrado."); return; }
        p.visualizarDetalle();
        u.registrarVista(p);
        bitacora.add(u.getNombre() + " vio detalle de: " + p.getTitulo());
    }

    static void guardarProyecto(Usuario u) {
        mostrarCatalogo();
        System.out.print("ID del proyecto a guardar: ");
        int id = leerInt();
        Proyecto p = galeria.buscarProyectoPorId(id);
        if (p == null) { System.out.println("[ERROR] Proyecto no encontrado."); return; }
        u.guardarProyecto(p);
        bitacora.add(u.getNombre() + " guardo: " + p.getTitulo());
    }

    static void verMisGuardados(Usuario u) {
        List<Guardado> lista = u.getListaGuardados();
        if (lista.isEmpty()) { System.out.println("  No tienes proyectos guardados."); return; }
        System.out.println("\n-- MIS GUARDADOS --");
        for (Guardado g : lista)
            System.out.println("  [" + g.getIdGuardado() + "] " + g.getProyecto().getTitulo()
                               + " | " + g.getFechaGuardado());
    }

    static void eliminarGuardado(Usuario u) {
        verMisGuardados(u);
        if (u.getListaGuardados().isEmpty()) return;
        System.out.print("ID del proyecto a eliminar de guardados: ");
        int id = leerInt();
        u.eliminarProyecto(id);
        bitacora.add(u.getNombre() + " elimino guardado del proyecto ID: " + id);
    }

    static void valorarProyecto(Usuario u) {
        mostrarCatalogo();
        System.out.print("ID del proyecto a valorar: ");
        int id = leerInt();
        Proyecto p = galeria.buscarProyectoPorId(id);
        if (p == null) { System.out.println("[ERROR] Proyecto no encontrado."); return; }
        System.out.print("Puntuacion (1-5): ");
        int puntuacion = leerInt();
        u.valorarProyecto(p, puntuacion);
        bitacora.add(u.getNombre() + " valoro '" + p.getTitulo() + "' con " + puntuacion + "/5");
    }

    static void editarCuenta(Usuario u) {
        System.out.println("\n-- EDITAR MI CUENTA --");
        System.out.print("Nuevo nombre   (Enter para no cambiar): "); String nombre   = sc.nextLine();
        System.out.print("Nuevo apellido (Enter para no cambiar): "); String apellido = sc.nextLine();
        System.out.print("Nuevo correo   (Enter para no cambiar): "); String correo   = sc.nextLine();
        System.out.print("Nueva clave    (Enter para no cambiar): "); String clave    = sc.nextLine();
        if (!nombre.isEmpty())   u.setNombre(nombre);
        if (!apellido.isEmpty()) u.setApellido(apellido);
        if (!correo.isEmpty())   u.setCorreo(correo);
        if (!clave.isEmpty())    u.setClave(clave);
        System.out.println("[OK] Cuenta actualizada.");
        bitacora.add(u.getNombre() + " edito su cuenta.");
    }

    // ════════════════════════════════════════════════
    //  ACCIONES DE ADMIN — PROYECTOS
    // ════════════════════════════════════════════════
    static void agregarProyecto() {
        System.out.println("\n-- AGREGAR PROYECTO --");
        System.out.print("Titulo  : "); String titulo  = sc.nextLine();
        System.out.print("Resumen : "); String resumen = sc.nextLine();
        System.out.print("URL archivo: "); String url  = sc.nextLine();

        mostrarLista("Facultades",  galeria.listarFacultades(),  f -> "[" + f.getIdFacultad()  + "] " + f.getNombreFacultad());
        System.out.print("ID Facultad : "); int idFac = leerInt();

        mostrarLista("Programas",   galeria.listarProgramas(),   p -> "[" + p.getIdPrograma()  + "] " + p.getNombrePrograma());
        System.out.print("ID Programa : "); int idPro = leerInt();

        mostrarLista("Materias",    galeria.listarMaterias(),    m -> "[" + m.getIdMateria()   + "] " + m.getNombreMateria());
        System.out.print("ID Materia  : "); int idMat = leerInt();

        mostrarLista("Semestres",   galeria.listarSemestres(),   s -> "[" + s.getIdSemestre()  + "] " + s.getAnio() + "-" + s.getPeriodo());
        System.out.print("ID Semestre : "); int idSem = leerInt();

        mostrarLista("Categorias",  galeria.listarCategorias(),  c -> "[" + c.getIdCategoria() + "] " + c.getNombreCategoria());
        System.out.print("ID Categoria: "); int idCat = leerInt();

        Facultad  fac = buscarEnLista(galeria.listarFacultades(),  f -> f.getIdFacultad()  == idFac);
        Programa  pro = buscarEnLista(galeria.listarProgramas(),   p -> p.getIdPrograma()  == idPro);
        Materia   mat = buscarEnLista(galeria.listarMaterias(),    m -> m.getIdMateria()   == idMat);
        Semestre  sem = buscarEnLista(galeria.listarSemestres(),   s -> s.getIdSemestre()  == idSem);
        Categoria cat = buscarEnLista(galeria.listarCategorias(),  c -> c.getIdCategoria() == idCat);

        int nuevoId = galeria.consultarCatalogo().size() + 101;
        Proyecto nuevo = new Proyecto(nuevoId, titulo, resumen, url, "",
                                       0, 0, new Date(),
                                       new ArrayList<>(), new ArrayList<>(),
                                       fac, pro, mat, sem, cat, new ArrayList<>());
        galeria.agregarProyecto(nuevo);
    }

    static void editarProyecto() {
        mostrarCatalogo();
        System.out.print("ID del proyecto a editar: ");
        int id = leerInt();
        Proyecto p = galeria.buscarProyectoPorId(id);
        if (p == null) { System.out.println("[ERROR] No encontrado."); return; }
        System.out.print("Nuevo titulo  (Enter para no cambiar): "); String titulo  = sc.nextLine();
        System.out.print("Nuevo resumen (Enter para no cambiar): "); String resumen = sc.nextLine();
        System.out.print("Nueva URL     (Enter para no cambiar): "); String url     = sc.nextLine();
        if (!titulo.isEmpty())  p.setTitulo(titulo);
        if (!resumen.isEmpty()) p.setResumen(resumen);
        if (!url.isEmpty())     p.setArchivoURL(url);
        System.out.println("[OK] Proyecto actualizado.");
    }

    static void eliminarProyecto() {
        mostrarCatalogo();
        System.out.print("ID del proyecto a eliminar: ");
        int id = leerInt();
        galeria.eliminarProyecto(id);
    }

    // ════════════════════════════════════════════════
    //  ACCIONES DE ADMIN — PARAMETROS
    // ════════════════════════════════════════════════
    static void menuFacultades() {
        System.out.println("\n-- FACULTADES --");
        System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
        System.out.print("  Opcion: ");
        switch (leerInt()) {
            case 1: System.out.print("Nombre: "); galeria.agregarFacultad(sc.nextLine());                                                 break;
            case 2: System.out.print("ID: "); int id=leerInt(); System.out.print("Nuevo nombre: "); galeria.editarFacultad(id, sc.nextLine()); break;
            case 3: System.out.print("ID a eliminar: "); galeria.eliminarFacultad(leerInt());                                             break;
            case 4: galeria.listarFacultades().forEach(f -> System.out.println("  " + f.getIdFacultad() + " - " + f.getNombreFacultad())); break;
        }
    }

    static void menuProgramas() {
        System.out.println("\n-- PROGRAMAS --");
        System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
        System.out.print("  Opcion: ");
        switch (leerInt()) {
            case 1: System.out.print("Nombre: "); String n=sc.nextLine(); System.out.print("ID Facultad: "); galeria.agregarPrograma(n, leerInt()); break;
            case 2: System.out.print("ID: "); int id=leerInt(); System.out.print("Nuevo nombre: "); galeria.editarPrograma(id, sc.nextLine());       break;
            case 3: System.out.print("ID a eliminar: "); galeria.eliminarPrograma(leerInt());                                                         break;
            case 4: galeria.listarProgramas().forEach(p -> System.out.println("  " + p.getIdPrograma() + " - " + p.getNombrePrograma()));            break;
        }
    }

    static void menuMaterias() {
        System.out.println("\n-- MATERIAS --");
        System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
        System.out.print("  Opcion: ");
        switch (leerInt()) {
            case 1: System.out.print("Nombre: "); String n=sc.nextLine(); System.out.print("ID Programa: "); galeria.agregarMateria(n, leerInt()); break;
            case 2: System.out.print("ID: "); int id=leerInt(); System.out.print("Nuevo nombre: "); galeria.editarMateria(id, sc.nextLine());      break;
            case 3: System.out.print("ID a eliminar: "); galeria.eliminarMateria(leerInt());                                                        break;
            case 4: galeria.listarMaterias().forEach(m -> System.out.println("  " + m.getIdMateria() + " - " + m.getNombreMateria()));             break;
        }
    }

    static void menuSemestres() {
        System.out.println("\n-- SEMESTRES --");
        System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
        System.out.print("  Opcion: ");
        switch (leerInt()) {
            case 1: System.out.print("Anio: "); int a=leerInt(); System.out.print("Periodo (1 o 2): "); int per=leerInt(); System.out.print("ID Materia: "); galeria.agregarSemestre(a, per, leerInt()); break;
            case 2: System.out.print("ID: "); int id=leerInt(); System.out.print("Nuevo anio: "); int na=leerInt(); System.out.print("Nuevo periodo: "); galeria.editarSemestre(id, na, leerInt());        break;
            case 3: System.out.print("ID a eliminar: "); galeria.eliminarSemestre(leerInt());                                                                                                                break;
            case 4: galeria.listarSemestres().forEach(s -> System.out.println("  " + s.getIdSemestre() + " - " + s.getAnio() + "-" + s.getPeriodo()));                                                      break;
        }
    }

    static void menuCategorias() {
        System.out.println("\n-- CATEGORIAS --");
        System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
        System.out.print("  Opcion: ");
        switch (leerInt()) {
            case 1: System.out.print("Nombre: "); String n=sc.nextLine(); System.out.print("Descripcion: "); galeria.agregarCategoria(n, sc.nextLine()); break;
            case 2: System.out.print("ID: "); int id=leerInt(); System.out.print("Nuevo nombre: "); galeria.editarCategoria(id, sc.nextLine());          break;
            case 3: System.out.print("ID a eliminar: "); galeria.eliminarCategoria(leerInt());                                                            break;
            case 4: galeria.listarCategorias().forEach(c -> System.out.println("  " + c.getIdCategoria() + " - " + c.getNombreCategoria()));             break;
        }
    }

    // ════════════════════════════════════════════════
    //  ACCIONES DE ADMIN — USUARIOS
    // ════════════════════════════════════════════════
    static void cambiarAdmin() {
        admin.mostrarListaUsuarios();
        System.out.print("ID del usuario: ");  int id  = leerInt();
        System.out.print("Es admin (s/n): ");  String r = sc.nextLine();
        admin.cambiarEstadoAdmin(id, r.equalsIgnoreCase("s"));
    }

    static void eliminarUsuario() {
        admin.mostrarListaUsuarios();
        System.out.print("ID del usuario a eliminar: ");
        admin.eliminarUsuario(leerInt());
    }

    // ════════════════════════════════════════════════
    //  RESUMEN FINAL
    // ════════════════════════════════════════════════
    static void mostrarResumenFinal() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("   RESUMEN DE LA SESION");
        System.out.println("╚══════════════════════════════════════════════╝");
        if (bitacora.isEmpty()) {
            System.out.println("  No se realizaron acciones.");
        } else {
            for (int i = 0; i < bitacora.size(); i++)
                System.out.println("  " + (i + 1) + ". " + bitacora.get(i));
        }
        System.out.println("\n  Estado final del catalogo: " +
                           galeria.consultarCatalogo().size() + " proyectos");
        System.out.println("  Usuarios registrados     : " +
                           admin.getListaUsuarios().size());
        System.out.println("════════════════════════════════════════════════");
    }

    // ════════════════════════════════════════════════
    //  DATOS DE PRUEBA PRECARGADOS
    // ════════════════════════════════════════════════
    static void cargarDatosDePrueba() {
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

        Facultad  fac = galeria.listarFacultades().get(0);
        Programa  pro = galeria.listarProgramas().get(0);
        Materia   mat = galeria.listarMaterias().get(0);
        Semestre  sem = galeria.listarSemestres().get(0);
        Categoria cat = galeria.listarCategorias().get(0);
        Categoria cat2= galeria.listarCategorias().get(1);

        Proyecto p1 = new Proyecto(101, "Galeria de Proyectos PI",
            "Sistema web para gestionar proyectos integradores universitarios",
            "http://archivos.uni.edu/p101.pdf", "", 75, 12, new Date(),
            new ArrayList<>(), new ArrayList<>(), fac, pro, mat, sem, cat, new ArrayList<>());

        Proyecto p2 = new Proyecto(102, "Modelo de Prediccion con IA",
            "Uso de machine learning para predecir flujos financieros",
            "http://archivos.uni.edu/p102.pdf", "", 30, 5, new Date(),
            new ArrayList<>(), new ArrayList<>(), fac, pro, mat, sem, cat2, new ArrayList<>());

        galeria.agregarProyecto(p1);
        galeria.agregarProyecto(p2);

        // Usuario normal con clave en texto plano (autenticar compara con encriptarClave)
        // Para que funcione el login de prueba, la clave se guarda sin encriptar aquí
        // y autenticar() la encripta al comparar. Entonces al crear el usuario
        // pasamos la clave ya "encriptada" igual a como la encripta el metodo:
        String claveIrene = "ENC_" + "clave123".hashCode();
        String claveAdmin = "ENC_" + "admin999".hashCode();

        Usuario irene = new Usuario(1, "Irene",  "Romero", "irene@correo.com", claveIrene, "", false, new ArrayList<>(), new ArrayList<>());
        Usuario ana   = new Usuario(2, "Ana",    "Perez",  "ana@correo.com",   claveAdmin, "", true,  new ArrayList<>(), new ArrayList<>());

        admin.registrarUsuario(irene);
        admin.registrarUsuario(ana);

        System.out.println("[SISTEMA] Datos de prueba cargados.");
        System.out.println("[SISTEMA] Usuario normal : irene@correo.com / clave123");
        System.out.println("[SISTEMA] Administrador  : ana@correo.com   / admin999");
    }

    // ════════════════════════════════════════════════
    //  UTILIDADES
    // ════════════════════════════════════════════════
    static int leerInt() {
        try {
            int n = Integer.parseInt(sc.nextLine().trim());
            return n;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static Integer leerIntOpcional() {
        String linea = sc.nextLine().trim();
        if (linea.isEmpty()) return null;
        try { return Integer.parseInt(linea); }
        catch (NumberFormatException e) { return null; }
    }

    interface Etiquetador<T> { String etiqueta(T item); }
    interface Condicion<T>   { boolean cumple(T item); }

    static <T> void mostrarLista(String titulo, List<T> lista, Etiquetador<T> fn) {
        System.out.println("  -- " + titulo + " --");
        for (T item : lista) System.out.println("  " + fn.etiqueta(item));
    }

    static <T> T buscarEnLista(List<T> lista, Condicion<T> condicion) {
        for (T item : lista) if (condicion.cumple(item)) return item;
        return null;
    }
}