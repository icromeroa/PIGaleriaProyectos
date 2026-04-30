package galeria.dao;

import galeria.model.Materia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    // 🔹 CREATE (INSERTAR)
    public void insertarMateria(Materia m) {
        String sql = "INSERT INTO materias (nombre_materia) VALUES (?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombreMateria());

            ps.executeUpdate();
            System.out.println("Materia guardada ");

        } catch (SQLException e) {
            System.out.println("Error al insertar materia: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR TODAS)
    public List<Materia> listar() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia, nombre_materia FROM materias";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Materia m = new Materia(
                    rs.getInt("id_materia"),
                    rs.getString("nombre_materia")
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar materias: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarMateria(Materia m) {
        String sql = "UPDATE materias SET nombre_materia=? WHERE id_materia=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombreMateria());
            ps.setInt(2, m.getIdMateria());

            ps.executeUpdate();
            System.out.println("Materia actualizada ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar materia: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarMateria(int idMateria) {
        String sql = "DELETE FROM materias WHERE id_materia = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMateria);

            ps.executeUpdate();
            System.out.println("Materia eliminada ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar materia: " + e.getMessage());
        }
    }
}