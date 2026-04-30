package galeria.controller;

import galeria.dao.UsuarioDAO;
import galeria.model.Usuario;
import galeria.util.Sesion;

import java.util.List;

public class ControladorAdmin {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ── LOGIN ──────────────────────────────────────────────
    public Usuario iniciarSesion(String correo, String clave) {
        Usuario u = usuarioDAO.login(correo, clave);
        if (u != null) {
            Sesion.iniciar(u);
            System.out.println("[OK] Sesion iniciada: " + u.getNombre());
        } else {
            System.out.println("[ERROR] Credenciales incorrectas.");
        }
        return u;
    }

    public void cerrarSesion() {
        Sesion.cerrar();
        System.out.println("[OK] Sesion cerrada.");
    }

    // ── REGISTRO ───────────────────────────────────────────
    public boolean registrarUsuario(Usuario u) {
        if (usuarioDAO.buscarPorCorreo(u.getCorreo()) != null) {
            System.out.println("[ERROR] Ya existe un usuario con ese correo.");
            return false;
        }
        usuarioDAO.insertarUsuario(u);
        return true;
    }

    // ── USUARIOS ───────────────────────────────────────────
    public List<Usuario> getListaUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        return usuarioDAO.buscarPorCorreo(correo);
    }

    public void cambiarEstadoAdmin(int idUsuario, boolean esAdmin) {
        List<Usuario> lista = usuarioDAO.listarUsuarios();
        for (Usuario u : lista) {
            if (u.getIdUsuario() == idUsuario) {
                u.setEsAdmin(esAdmin);
                usuarioDAO.actualizarUsuario(u);
                System.out.println("[OK] Estado admin actualizado.");
                return;
            }
        }
        System.out.println("[ERROR] Usuario no encontrado.");
    }

    public void eliminarUsuario(int idUsuario) {
        usuarioDAO.eliminarUsuario(idUsuario);
    }

    public void editarUsuario(Usuario u) {
        usuarioDAO.actualizarUsuario(u);
    }
}