package galeria.util;

import galeria.model.Usuario;

public class Sesion {

    private static Usuario usuarioActual = null;

    public static void iniciar(Usuario u) {
        usuarioActual = u;
    }

    public static void cerrar() {
        usuarioActual = null;
    }

    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static boolean estaLogueado() {
        return usuarioActual != null;
    }

    public static boolean esAdmin() {
        return usuarioActual != null && usuarioActual.getEsAdmin();
    }
}