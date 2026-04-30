package galeria.dao;

import galeria.model.Programa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    // 🔹 CREATE (INSERTAR)
    public void insertarPrograma(Programa p) {
        String sql = "INSERT INTO programas (nombre_programa) VALUES (?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombrePrograma());

            ps.executeUpdate();
            System.out.println("Programa guardado ");

        } catch (SQLException e) {
            System.out.println("Error al insertar programa: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR TODOS)
    public List<Programa> listar() {
        List<Programa> lista = new ArrayList<>();
        String sql = "SELECT id_programa, nombre_programa FROM programas";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Programa p = new Programa(
                    rs.getInt("id_programa"),
                    rs.getString("nombre_programa")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar programas: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarPrograma(Programa p) {
        String sql = "UPDATE programas SET nombre_programa=? WHERE id_programa=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombrePrograma());
            ps.setInt(2, p.getIdPrograma());

            ps.executeUpdate();
            System.out.println("Programa actualizado ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar programa: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarPrograma(int idPrograma) {
        String sql = "DELETE FROM programas WHERE id_programa = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrograma);

            ps.executeUpdate();
            System.out.println("Programa eliminado ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar programa: " + e.getMessage());
        }
    }
}