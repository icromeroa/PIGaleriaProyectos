package galeria.util;

import galeria.model.Usuario;

/**
 * Guarda en memoria el usuario que inició sesión.
 * No toca la base de datos — solo es un "portador" del usuario activo.
 */
public class Sesion {

    // El usuario logueado actualmente. null = nadie ha iniciado sesión.
    private static Usuario usuarioActual = null;

    // Privado para que nadie cree instancias — todo es estático
    private Sesion() {}

    /** Llama esto cuando el login es exitoso. */
    public static void iniciar(Usuario u) {
        usuarioActual = u;
    }

    /** Llama esto cuando el usuario cierra sesión. */
    public static void cerrar() {
        usuarioActual = null;
    }

    /** Retorna el usuario logueado, o null si no hay nadie. */
    public static Usuario getUsuario() {
        return usuarioActual;
    }

    /** true si hay alguien logueado. */
    public static boolean estaLogueado() {
        return usuarioActual != null;
    }

    /** true si el usuario logueado es administrador. */
    public static boolean esAdmin() {
        return usuarioActual != null && usuarioActual.getEsAdmin();
    }
}