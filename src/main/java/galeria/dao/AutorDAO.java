package galeria.dao;

import galeria.model.Autor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    // 🔹 INSERTAR 
    public void insertarAutor(Autor a, int idProyecto) {
        String sql = "INSERT INTO autores (nombre_autor, correo) VALUES (?, ?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombreAutor());
            ps.setString(2, a.getCorreo());

            ps.executeUpdate();
            System.out.println("Autor guardado ");

        } catch (SQLException e) {
            System.out.println("Error al insertar autor: " + e.getMessage());
        }
    }

    // 🔹 LISTAR 
    public List<Autor> listarPorProyecto(int idProyecto) {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autores";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Autor a = new Autor(
                    rs.getInt("id_autor"),
                    rs.getString("nombre_autor"),
                    rs.getString("correo")
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarAutor(Autor a) {
        String sql = "UPDATE autores SET nombre_autor=?, correo=? WHERE id_autor=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombreAutor());
            ps.setString(2, a.getCorreo());
            ps.setInt(3, a.getIdAutor());

            ps.executeUpdate();
            System.out.println("Autor actualizado ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar autor: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarAutor(int idAutor) {
        String sql = "DELETE FROM autores WHERE id_autor = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);

            ps.executeUpdate();
            System.out.println("Autor eliminado ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar autor: " + e.getMessage());
        }
    }
}