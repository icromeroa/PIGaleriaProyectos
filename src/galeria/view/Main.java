package galeria.view;

import galeria.controller.*;
import galeria.model.*;
import java.util.*;
import galeria.dao.*;

public class Main {

	 static Scanner sc = new Scanner(System.in);

	    static ControladorGaleria galeria = new ControladorGaleria();
	    static ControladorAdmin admin = new ControladorAdmin();
	    static ControladorLanding landing = new ControladorLanding(galeria);

	    static UsuarioDAO usuarioDAO = new UsuarioDAO();
	    static ProyectoDAO proyectoDAO = new ProyectoDAO();

	    static FacultadDAO facultadDAO = new FacultadDAO();
	    static ProgramaDAO programaDAO = new ProgramaDAO(); 
	    static MateriaDAO materiaDAO = new MateriaDAO();
	    static SemestreDAO semestreDAO = new SemestreDAO();
	    static CategoriaDAO categoriaDAO = new CategoriaDAO();
	    
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

        String claveEncriptada = "ENC_" + clave.hashCode();

        // VALIDAR EN BASE DE DATOS
        Usuario existente = usuarioDAO.buscarPorCorreo(correo);

        if (existente != null) {
            System.out.println("[ERROR] Ya existe un usuario con ese correo.");
            return;
        }

        Usuario nuevo = new Usuario(
            0,
            nombre,
            apellido,
            correo,
            claveEncriptada,
            "",
            false,
            new ArrayList<>(),
            new ArrayList<>()
        );

        usuarioDAO.insertarUsuario(nuevo);
        admin.registrarUsuario(nuevo);

        System.out.println("[OK] Usuario registrado correctamente ");
        bitacora.add("Usuario registrado: " + nombre + " " + apellido);
    }
    
    // ════════════════════════════════════════════════
    //  INICIO DE SESION
    // ════════════════════════════════════════════════
    static void iniciarSesion() {
        System.out.println("\n-- INICIAR SESION --");

        System.out.print("Correo: ");
        String correo = sc.nextLine().trim();

        System.out.print("Clave : ");
        String clave  = sc.nextLine().trim();

        String claveEncriptada = "ENC_" + clave.hashCode();

        Usuario u = usuarioDAO.login(correo, claveEncriptada);

        if (u == null) {
            System.out.println("[ERROR] Credenciales incorrectas.");
            return;
        }

        System.out.println("[OK] Bienvenido " + u.getNombre() );

        bitacora.add("Sesion iniciada: " + u.getNombre() + " (admin=" + u.getEsAdmin() + ")");

     // 🔹 Lista de correos admin
        List<String> admins = Arrays.asList(
            "michelle@gmail.com",
            "irene@gmail.com",
            "helen@gmail.com"
        );

        // 🔹 Validación
        if (u.getEsAdmin() || admins.contains(u.getCorreo().toLowerCase())) {
            System.out.println("[SISTEMA] Acceso como ADMIN");
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
                case 1:
                    landing.mostrarLanding();
                    bitacora.add(u.getNombre() + " vio el landing.");
                    break;

                case 2:
                    mostrarCatalogo();
                    bitacora.add(u.getNombre() + " vio el catalogo.");
                    break;

                case 3:
                    filtrarProyectos(u);
                    break;

                case 4:
                    verDetalleProyecto(u);
                    break;

                case 5:
                    guardarProyecto(u);
                    break;

                case 6:
                    verMisGuardados(u);
                    bitacora.add(u.getNombre() + " vio sus guardados.");
                    break;

                case 7:
                    eliminarGuardado(u);
                    break;

                case 8:
                    valorarProyecto(u);
                    break;

                case 9:
                    u.verPerfil();
                    bitacora.add(u.getNombre() + " vio su perfil.");
                    break;

                case 10:
                    editarCuenta(u);
                    break;

                case 11:
                    u.eliminarMiCuenta();
                    usuarioDAO.eliminarUsuario(u.getIdUsuario());

                    System.out.println("[OK] Cuenta eliminada correctamente");
                    bitacora.add(u.getNombre() + " elimino su cuenta.");
                    activo = false;
                    break;

                case 0:
                    System.out.println("[OK] Sesion cerrada.");
                    bitacora.add(u.getNombre() + " cerro sesion.");
                    activo = false;
                    break;

                default:
                    System.out.println("[ERROR] Opcion invalida.");
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

            System.out.println("  -- Usuarios (BD) --");
            System.out.println("  [10] Ver usuarios");
            System.out.println("  [11] Cambiar estado admin");
            System.out.println("  [12] Eliminar usuario");

            System.out.println("  -- Landing --");
            System.out.println("  [13] Ver landing");
            System.out.println("  [0]  Cerrar sesion");

            System.out.print("  Opcion: ");
            int op = leerInt();

            switch (op) {

                case 1: agregarProyecto(); break;
                case 2: editarProyecto(); break;
                case 3: eliminarProyecto(); break;
                case 4: mostrarCatalogo(); break;

                case 5: menuFacultades(); break;
                case 6: menuProgramas(); break;
                case 7: menuMaterias(); break;
                case 8: menuSemestres(); break;
                case 9: menuCategorias(); break;

                case 10: verUsuariosBD(); break;
                case 11: cambiarAdminBD(); break;
                case 12: eliminarUsuarioBD(); break;

                case 13: landing.mostrarLanding(); break;

                case 0:
                    System.out.println("[OK] Sesion cerrada.");
                    activo = false;
                    break;

                default:
                    System.out.println("[ERROR] Opcion invalida.");
            }
        }
    }

    /* =========================
       METODOS BD
       ========================= */

    static void verUsuariosBD() {
        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {
            System.out.println("[" + u.getIdUsuario() + "] "
                    + u.getNombre()
                    + " | " + u.getCorreo()
                    + " | Admin: " + u.getEsAdmin());
        }
    }

    static void cambiarAdminBD() {
        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {
            System.out.println("[" + u.getIdUsuario() + "] "
                    + u.getNombre()
                    + " | Admin: " + u.getEsAdmin());
        }

        System.out.print("ID del usuario: ");
        int id = leerInt();

        for (Usuario u : lista) {
            if (u.getIdUsuario() == id) {
                u.setEsAdmin(!u.getEsAdmin());
                usuarioDAO.actualizarUsuario(u);

                System.out.println("[OK] Estado admin actualizado.");
                return;
            }
        }

        System.out.println("[ERROR] Usuario no encontrado.");
    }

    static void eliminarUsuarioBD() {
        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {
            System.out.println("[" + u.getIdUsuario() + "] " + u.getNombre());
        }

        System.out.print("ID del usuario a eliminar: ");
        int id = leerInt();

        usuarioDAO.eliminarUsuario(id);

        System.out.println("[OK] Usuario eliminado.");
    }
    
    
    // ════════════════════════════════════════════════
    //  ACCIONES DE USUARIO
    // ════════════════════════════════════════════════

   static void mostrarCatalogo() {
       List<Proyecto> lista = galeria.consultarCatalogo();

       if (lista == null || lista.isEmpty()) {
           System.out.println("  (sin proyectos)");
           return;
       }

       System.out.println("\n-- CATALOGO --");

       for (Proyecto p : lista) {
           System.out.println("  [" + p.getIdProyecto() + "] " + p.getTitulo()
                   + " | Vistas: " + p.getCantidadVistas()
                   + " | Rating: " + String.format("%.1f", p.valoracionPromedio())
                   + " | Destacado: " + (p.esDestacado() ? "Si" : "No"));
       }
   }

   static void filtrarProyectos(Usuario u) {
       System.out.println("\n-- FILTRAR PROYECTOS --");
       System.out.println("Deja en blanco para ignorar un filtro.");

       System.out.print("ID Facultad  : "); Integer idFac = leerIntOpcional();
       System.out.print("ID Programa  : "); Integer idPro = leerIntOpcional();
       System.out.print("ID Materia   : "); Integer idMat = leerIntOpcional();
       System.out.print("ID Semestre  : "); Integer idSem = leerIntOpcional();
       System.out.print("ID Categoria : "); Integer idCat = leerIntOpcional();

       System.out.print("Texto buscar : "); String texto = sc.nextLine();

       FiltroProyecto filtro = new FiltroProyecto(
               idFac, idPro, idMat, idSem, idCat,
               (texto.isEmpty() ? null : texto),
               null
       );

       List<Proyecto> resultado = galeria.filtrarProyectos(filtro);

       System.out.println("Resultados: " + (resultado == null ? 0 : resultado.size()));

       if (resultado != null) {
           for (Proyecto p : resultado) {
               System.out.println("  [" + p.getIdProyecto() + "] " + p.getTitulo());
           }
       }

       bitacora.add(u.getNombre() + " filtro proyectos. Resultados: " +
               (resultado == null ? 0 : resultado.size()));
   }

   static void verDetalleProyecto(Usuario u) {
       mostrarCatalogo();

       System.out.print("ID del proyecto a ver: ");
       int id = leerInt();

       Proyecto p = galeria.buscarProyectoPorId(id);

       if (p == null) {
           System.out.println("[ERROR] Proyecto no encontrado.");
           return;
       }

       p.visualizarDetalle();
       u.registrarVista(p);

       bitacora.add(u.getNombre() + " vio detalle de: " + p.getTitulo());
   }

   static void guardarProyecto(Usuario u) {
       mostrarCatalogo();

       System.out.print("ID del proyecto a guardar: ");
       int id = leerInt();

       Proyecto p = galeria.buscarProyectoPorId(id);

       if (p == null) {
           System.out.println("[ERROR] Proyecto no encontrado.");
           return;
       }

       u.guardarProyecto(p);

       
       // guardadoDAO.insertar(u.getIdUsuario(), p.getIdProyecto());

       bitacora.add(u.getNombre() + " guardo: " + p.getTitulo());
   }

   static void verMisGuardados(Usuario u) {
       List<Guardado> lista = u.getListaGuardados();

       if (lista == null || lista.isEmpty()) {
           System.out.println("  No tienes proyectos guardados.");
           return;
       }

       System.out.println("\n-- MIS GUARDADOS --");

       for (Guardado g : lista) {
           System.out.println("  [" + g.getIdGuardado() + "] "
                   + g.getProyecto().getTitulo()
                   + " | " + g.getFechaGuardado());
       }
   }

   static void eliminarGuardado(Usuario u) {
       verMisGuardados(u);

       if (u.getListaGuardados() == null || u.getListaGuardados().isEmpty()) {
           return;
       }

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

       if (p == null) {
           System.out.println("[ERROR] Proyecto no encontrado.");
           return;
       }

       System.out.print("Puntuacion (1-5): ");
       int puntuacion = leerInt();

       u.valorarProyecto(p, puntuacion);

       
       bitacora.add(u.getNombre() + " valoro '" + p.getTitulo() + "' con " + puntuacion + "/5");
   }

   static void editarCuenta(Usuario u) {
       System.out.println("\n-- EDITAR MI CUENTA --");

       System.out.print("Nuevo nombre   (Enter para no cambiar): ");
       String nombre = sc.nextLine();

       System.out.print("Nuevo apellido (Enter para no cambiar): ");
       String apellido = sc.nextLine();

       System.out.print("Nuevo correo   (Enter para no cambiar): ");
       String correo = sc.nextLine();

       System.out.print("Nueva clave    (Enter para no cambiar): ");
       String clave = sc.nextLine();

       if (!nombre.isEmpty()) u.setNombre(nombre);
       if (!apellido.isEmpty()) u.setApellido(apellido);
       if (!correo.isEmpty()) u.setCorreo(correo);

       if (!clave.isEmpty()) {
           String claveEnc = "ENC_" + clave.hashCode();
           u.setClave(claveEnc);
       }

       // BD UPDATE 
       usuarioDAO.actualizarUsuario(u);

       System.out.println("[OK] Cuenta actualizada en base de datos 😎");

       bitacora.add(u.getNombre() + " edito su cuenta.");
   }
   
// ════════════════════════════════════════════════
// ACCIONES DE ADMIN — PROYECTOS
//════════════════════════════════════════════════

static void agregarProyecto() {

   System.out.println("\n-- AGREGAR PROYECTO --");

   System.out.print("Titulo  : ");
   String titulo = sc.nextLine();

   System.out.print("Resumen : ");
   String resumen = sc.nextLine();

   System.out.print("URL archivo: ");
   String url = sc.nextLine();

   mostrarLista("Facultades", galeria.listarFacultades(),
           f -> "[" + f.getIdFacultad() + "] " + f.getNombreFacultad());
   System.out.print("ID Facultad : ");
   int idFac = leerInt();

   mostrarLista("Programas", galeria.listarProgramas(),
           p -> "[" + p.getIdPrograma() + "] " + p.getNombrePrograma());
   System.out.print("ID Programa : ");
   int idPro = leerInt();

   mostrarLista("Materias", galeria.listarMaterias(),
           m -> "[" + m.getIdMateria() + "] " + m.getNombreMateria());
   System.out.print("ID Materia  : ");
   int idMat = leerInt();

   mostrarLista("Semestres", galeria.listarSemestres(),
           s -> "[" + s.getIdSemestre() + "] " + s.getAnio() + "-" + s.getPeriodo());
   System.out.print("ID Semestre : ");
   int idSem = leerInt();

   mostrarLista("Categorias", galeria.listarCategorias(),
           c -> "[" + c.getIdCategoria() + "] " + c.getNombreCategoria());
   System.out.print("ID Categoria: ");
   int idCat = leerInt();

   Facultad fac = buscarEnLista(galeria.listarFacultades(), f -> f.getIdFacultad() == idFac);
   Programa pro = buscarEnLista(galeria.listarProgramas(), p -> p.getIdPrograma() == idPro);
   Materia mat = buscarEnLista(galeria.listarMaterias(), m -> m.getIdMateria() == idMat);
   Semestre sem = buscarEnLista(galeria.listarSemestres(), s -> s.getIdSemestre() == idSem);
   Categoria cat = buscarEnLista(galeria.listarCategorias(), c -> c.getIdCategoria() == idCat);

   if (fac == null || pro == null || mat == null || sem == null || cat == null) {
       System.out.println("[ERROR] Debes seleccionar opciones válidas");
       return;
   }

   int nuevoId = proyectoDAO.generarId();

   Proyecto nuevo = new Proyecto(
           nuevoId, titulo, resumen, url, "",
           0, 0, new Date(),
           new ArrayList<>(), new ArrayList<>(),
           fac, pro, mat, sem, cat, new ArrayList<>()
   );

   proyectoDAO.insertarProyecto(nuevo);
   galeria.agregarProyecto(nuevo);

   System.out.println("[OK] Proyecto agregado correctamente ");
}


static void editarProyecto() {

   mostrarCatalogo();

   System.out.print("ID del proyecto a editar: ");
   int id = leerInt();

   Proyecto p = proyectoDAO.buscarPorId(id);

   if (p == null) {
       System.out.println("[ERROR] No encontrado en BD");
       return;
   }

   System.out.print("Nuevo titulo (Enter para no cambiar): ");
   String titulo = sc.nextLine();

   System.out.print("Nuevo resumen (Enter para no cambiar): ");
   String resumen = sc.nextLine();

   System.out.print("Nueva URL (Enter para no cambiar): ");
   String url = sc.nextLine();

   if (!titulo.isEmpty()) p.setTitulo(titulo);
   if (!resumen.isEmpty()) p.setResumen(resumen);
   if (!url.isEmpty()) p.setArchivoURL(url);

   proyectoDAO.actualizarProyecto(p);

   //  sincronizar memoria si la usas
   Proyecto pMem = galeria.buscarProyectoPorId(id);
   if (pMem != null) {
       pMem.setTitulo(p.getTitulo());
       pMem.setResumen(p.getResumen());
       pMem.setArchivoURL(p.getArchivoURL());
   }

   System.out.println("[OK] Proyecto actualizado en BD ");
}


static void eliminarProyecto() {

   mostrarCatalogo();

   System.out.print("ID del proyecto a eliminar: ");
   int id = leerInt();

   proyectoDAO.eliminarProyecto(id);
   galeria.eliminarProyecto(id);

   System.out.println("[OK] Proyecto eliminado ");
}
    

// ════════════════════════════════════════════════
    //  ACCIONES DE ADMIN — PARAMETROS
    // ════════════════════════════════════════════════
//════════════════════════════════════════════════
//FACULTADES (BD)
//════════════════════════════════════════════════
static void menuFacultades() {
System.out.println("\n-- FACULTADES --");
System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
System.out.print("  Opcion: ");
int op = leerInt();
sc.nextLine();

switch (op) {

    case 1:
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        Facultad f = new Facultad(0, nombre);
        facultadDAO.insertarFacultad(f);
        break;

    case 2:
        System.out.print("ID: ");
        int id = leerInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nuevo = sc.nextLine();

        Facultad f2 = new Facultad(id, nuevo);
        facultadDAO.actualizarFacultad(f2);
        break;

    case 3:
        System.out.print("ID a eliminar: ");
        facultadDAO.eliminarFacultad(leerInt());
        sc.nextLine();
        break;

    case 4:
        facultadDAO.listar()
            .forEach(x -> System.out.println(
                "  " + x.getIdFacultad() + " - " + x.getNombreFacultad()
            ));
        break;
}
}
//════════════════════════════════════════════════
//PROGRAMAS (BD)
//════════════════════════════════════════════════
static void menuProgramas() {
    System.out.println("\n-- PROGRAMAS --");
    System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
    System.out.print("  Opcion: ");
    int op = leerInt();
    sc.nextLine();

    switch (op) {

        case 1:
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            Programa p = new Programa(0, nombre);
            programaDAO.insertarPrograma(p);
            break;

        case 2:
            System.out.print("ID: ");
            int id = leerInt();
            sc.nextLine();

            System.out.print("Nuevo nombre: ");
            String nuevo = sc.nextLine();

            Programa p2 = new Programa(id, nuevo);
            programaDAO.actualizarPrograma(p2);
            break;

        case 3:
            System.out.print("ID a eliminar: ");
            programaDAO.eliminarPrograma(leerInt());
            sc.nextLine();
            break;

        case 4:
            programaDAO.listar()
                .forEach(x -> System.out.println(
                    "  " + x.getIdPrograma() + " - " + x.getNombrePrograma()
                ));
            break;
    }
}
//════════════════════════════════════════════════
//MATERIAS (BD)
//════════════════════════════════════════════════
static void menuMaterias() {
    System.out.println("\n-- MATERIAS --");
    System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
    System.out.print("  Opcion: ");
    int op = leerInt();
    sc.nextLine();

    switch (op) {

        case 1:
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            Materia m = new Materia(0, nombre);
            materiaDAO.insertarMateria(m);
            break;

        case 2:
            System.out.print("ID: ");
            int id = leerInt();
            sc.nextLine();

            System.out.print("Nuevo nombre: ");
            String nuevo = sc.nextLine();

            Materia m2 = new Materia(id, nuevo);
            materiaDAO.actualizarMateria(m2);
            break;

        case 3:
            System.out.print("ID a eliminar: ");
            materiaDAO.eliminarMateria(leerInt());
            sc.nextLine();
            break;

        case 4:
            materiaDAO.listar()
                .forEach(x -> System.out.println(
                    "  " + x.getIdMateria() + " - " + x.getNombreMateria()
                ));
            break;
    }
}
//════════════════════════════════════════════════
//SEMESTRES (BD)
//════════════════════════════════════════════════
static void menuSemestres() {
System.out.println("\n-- SEMESTRES --");
System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
System.out.print("  Opcion: ");
int op = leerInt();
sc.nextLine();

switch (op) {

    case 1:
        System.out.print("Año: ");
        int anio = leerInt();

        System.out.print("Periodo: ");
        int periodo = leerInt();
        sc.nextLine();

        Semestre s = new Semestre(0, anio, periodo);
        semestreDAO.insertarSemestre(s);
        break;

    case 2:
        System.out.print("ID: ");
        int id = leerInt();

        System.out.print("Nuevo año: ");
        int nuevoAnio = leerInt();

        System.out.print("Nuevo periodo: ");
        int nuevoPeriodo = leerInt();
        sc.nextLine();

        Semestre s2 = new Semestre(id, nuevoAnio, nuevoPeriodo);
        semestreDAO.actualizarSemestre(s2);
        break;

    case 3:
        System.out.print("ID a eliminar: ");
        semestreDAO.eliminarSemestre(leerInt());
        sc.nextLine();
        break;

    case 4:
        semestreDAO.listar()
                .forEach(x -> System.out.println("  " + x.getIdSemestre() + " - " + x.getAnio() + "-" + x.getPeriodo()));
        break;
}
}
//════════════════════════════════════════════════
//CATEGORIAS (BD)
//════════════════════════════════════════════════
static void menuCategorias() {
System.out.println("\n-- CATEGORIAS --");
System.out.println("  [1] Agregar  [2] Editar  [3] Eliminar  [4] Listar");
System.out.print("  Opcion: ");
int op = leerInt();
sc.nextLine();

switch (op) {

    case 1:
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Descripcion: ");
        String desc = sc.nextLine();

        Categoria c = new Categoria(0, nombre, desc, "");
        categoriaDAO.insertarCategoria(c);
        break;

    case 2:
        System.out.print("ID: ");
        int id = leerInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nuevo = sc.nextLine();

        Categoria c2 = new Categoria(id, nuevo, "", "");
        categoriaDAO.actualizarCategoria(c2);
        break;

    case 3:
        System.out.print("ID a eliminar: ");
        categoriaDAO.eliminarCategoria(leerInt());
        sc.nextLine();
        break;

    case 4:
        categoriaDAO.listar()
                .forEach(x -> System.out.println("  " + x.getIdCategoria() + " - " + x.getNombreCategoria()));
        break;
}
}
    // ════════════════════════════════════════════════
    //  ACCIONES DE ADMIN — USUARIOS
    // ════════════════════════════════════════════════
static void cambiarAdmin() {
    admin.mostrarListaUsuarios();

    System.out.print("ID del usuario: ");
    int id = leerInt();
    sc.nextLine(); 

    System.out.print("Es admin (s/n): ");
    String r = sc.nextLine();

    admin.cambiarEstadoAdmin(id, r.equalsIgnoreCase("s"));
}

static void eliminarUsuario() {
    admin.mostrarListaUsuarios();

    System.out.print("ID del usuario a eliminar: ");
    int id = leerInt();

    admin.eliminarUsuario(id);
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
        for (int i = 0; i < bitacora.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + bitacora.get(i));
        }
    }

    int totalProyectos = (galeria.consultarCatalogo() != null)
            ? galeria.consultarCatalogo().size()
            : 0;

    int totalUsuarios = (admin.getListaUsuarios() != null)
            ? admin.getListaUsuarios().size()
            : 0;

    System.out.println("\n  Estado final del catalogo: " + totalProyectos + " proyectos");
    System.out.println("  Usuarios registrados     : " + totalUsuarios);

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

    // 🔴 VALIDACIÓN IMPORTANTE (EVITA QUE SE CAIGA TODO)
    if (galeria.listarFacultades().isEmpty() ||
        galeria.listarProgramas().isEmpty() ||
        galeria.listarMaterias().isEmpty() ||
        galeria.listarSemestres().isEmpty() ||
        galeria.listarCategorias().size() < 2) {

        System.out.println("❌ ERROR: No se cargaron correctamente los datos base");
        return;
    }

    Facultad fac = galeria.listarFacultades().get(0);
    Programa pro = galeria.listarProgramas().get(0);
    Materia mat = galeria.listarMaterias().get(0);
    Semestre sem = galeria.listarSemestres().get(0);
    Categoria cat = galeria.listarCategorias().get(0);
    Categoria cat2 = galeria.listarCategorias().get(1);

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

    String claveIrene = "ENC_" + "clave123".hashCode();
    String claveAdmin = "ENC_" + "admin999".hashCode();

    Usuario irene = new Usuario(1, "Irene", "Romero", "irene@correo.com",
            claveIrene, "", false, new ArrayList<>(), new ArrayList<>());

    Usuario ana = new Usuario(2, "Ana", "Perez", "ana@correo.com",
            claveAdmin, "", true, new ArrayList<>(), new ArrayList<>());

    admin.registrarUsuario(irene);
    admin.registrarUsuario(ana);

    System.out.println("[SISTEMA] Datos de prueba cargados.");
    System.out.println("[SISTEMA] Usuario normal : irene@correo.com / clave123");
    System.out.println("[SISTEMA] Administrador  : ana@correo.com   / admin999");}
    // ════════════════════════════════════════════════
    //  UTILIDADES
    // ════════════════════════════════════════════════

static int leerInt() {
    try {
        String entrada = sc.nextLine().trim();
        if (entrada.isEmpty()) return -1;
        return Integer.parseInt(entrada);
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Numero invalido");
        return -1;
    }
}

static Integer leerIntOpcional() {
    String linea = sc.nextLine().trim();
    if (linea.isEmpty()) return null;

    try {
        return Integer.parseInt(linea);
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Numero invalido");
        return null;
    }
}

// ════════════════════════════════════════════════
//  INTERFACES GENERICAS
// ════════════════════════════════════════════════

interface Etiquetador<T> {
    String etiqueta(T item);
}

interface Condicion<T> {
    boolean cumple(T item);
}

// ════════════════════════════════════════════════
//  MOSTRAR LISTAS GENERICAS
// ════════════════════════════════════════════════

static <T> void mostrarLista(String titulo, List<T> lista, Etiquetador<T> fn) {
    System.out.println("\n  -- " + titulo + " --");

    if (lista == null || lista.isEmpty()) {
        System.out.println("  (sin datos)");
        return;
    }

    for (T item : lista) {
        System.out.println("  " + fn.etiqueta(item));
    }
}

// ════════════════════════════════════════════════
//  BUSQUEDA GENERICA
// ════════════════════════════════════════════════

static <T> T buscarEnLista(List<T> lista, Condicion<T> condicion) {
    if (lista == null) return null;

    for (T item : lista) {
        if (condicion.cumple(item)) return item;
    }
    return null;
}
}