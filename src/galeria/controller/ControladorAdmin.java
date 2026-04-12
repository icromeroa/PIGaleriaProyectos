package galeria.controller;

import galeria.model.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorAdmin {

    private List<Usuario> listaUsuarios = new ArrayList<>();

    public void registrarUsuario(Usuario u) {
        listaUsuarios.add(u);
        System.out.println("[OK] Usuario registrado: " + u.getNombre() + " " + u.getApellido());
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void mostrarListaUsuarios() {
        System.out.println("================================================");
        System.out.println("  LISTA DE USUARIOS REGISTRADOS");
        System.out.println("================================================");
        System.out.printf("  %-4s %-12s %-12s %-25s %-6s%n",
                          "ID", "Nombre", "Apellido", "Correo", "Admin");
        System.out.println("  ------------------------------------------------");
        for (Usuario u : listaUsuarios) {
            System.out.printf("  %-4d %-12s %-12s %-25s %-6s%n",
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                u.getCorreo(),
                u.getEsAdmin() ? "Si" : "No");
        }
        System.out.println("================================================");
    }

    public void cambiarEstadoAdmin(int idUsuario, boolean esAdmin) {
        Usuario u = buscarUsuario(idUsuario);
        if (u != null) {
            u.setEsAdmin(esAdmin);
            System.out.println("[OK] Usuario " + u.getNombre() +
                               " ahora es admin: " + esAdmin);
        } else {
            System.out.println("[ERROR] Usuario no encontrado: " + idUsuario);
        }
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario u : listaUsuarios)
            if (u.getCorreo().equals(correo)) return u;
        return null;
    }

    public void eliminarUsuario(int idUsuario) {
        boolean ok = listaUsuarios.removeIf(u -> u.getIdUsuario() == idUsuario);
        System.out.println(ok
            ? "[OK] Usuario eliminado con ID: " + idUsuario
            : "[ERROR] Usuario no encontrado: " + idUsuario);
    }

    private Usuario buscarUsuario(int idUsuario) {
        for (Usuario u : listaUsuarios)
            if (u.getIdUsuario() == idUsuario) return u;
        return null;
    }

    // Autenticación: retorna el usuario si las credenciales son correctas
    public Usuario iniciarSesion(String correo, String clave) {
        for (Usuario u : listaUsuarios) {
            if (u.autenticar(correo, clave)) {
                System.out.println("[OK] Sesion iniciada: " + u.getNombre() + " " + u.getApellido());
                return u;
            }
        }
        System.out.println("[ERROR] Credenciales incorrectas.");
        return null;
    }
}