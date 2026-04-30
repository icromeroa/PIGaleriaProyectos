package galeria.dao;

import galeria.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // 🔹 LOGIN
    public Usuario login(String correo, String clave) {
        Usuario user = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND clave = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("clave"),
                        rs.getString("avatar_url"),
                        rs.getBoolean("es_admin"),
                        new ArrayList<>(),
                        new ArrayList<>()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en el login: " + e.getMessage());
        }
        return user;
    }
    
 // 🔹 BUSCAR POR CORREO (PARA VALIDAR REGISTRO)
    public Usuario buscarPorCorreo(String correo) {
        Usuario user = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("clave"),
                        rs.getString("avatar_url"),
                        rs.getBoolean("es_admin"),
                        new ArrayList<>(),
                        new ArrayList<>()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por correo: " + e.getMessage());
        }

        return user;
    }

    // 🔹 INSERTAR (CREATE)
    public void insertarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, correo, clave, es_admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getClave());
            ps.setBoolean(5, u.getEsAdmin());

            ps.executeUpdate();
            System.out.println("Usuario guardado en MySQL ");

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    // 🔹 LISTAR (READ TODOS)
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("clave"),
                    rs.getString("avatar_url"),
                    rs.getBoolean("es_admin"),
                    new ArrayList<>(),
                    new ArrayList<>()
                );

                lista.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 ACTUALIZAR (UPDATE)
    public void actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, correo=?, clave=?, es_admin=? WHERE id_usuario=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getClave());
            ps.setBoolean(5, u.getEsAdmin());
            ps.setInt(6, u.getIdUsuario());

            ps.executeUpdate();
            System.out.println("Usuario actualizado ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    // 🔹 ELIMINAR (DELETE)
    public void eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ps.executeUpdate();
            System.out.println("Usuario eliminado ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
}