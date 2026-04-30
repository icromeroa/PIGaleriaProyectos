package galeria.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import galeria.model.Facultad;

public class FacultadDAO {

    // 🔹 CREATE (INSERTAR)
    public void insertarFacultad(Facultad f) {
        String sql = "INSERT INTO facultades (nombre_facultad) VALUES (?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getNombreFacultad());

            ps.executeUpdate();
            System.out.println("Facultad guardada ");

        } catch (SQLException e) {
            System.out.println("Error al insertar facultad: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR TODAS)
    public List<Facultad> listar() {
        List<Facultad> lista = new ArrayList<>();
        String sql = "SELECT id_facultad, nombre_facultad FROM facultades";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Facultad f = new Facultad(
                    rs.getInt("id_facultad"),
                    rs.getString("nombre_facultad")
                );
                lista.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar facultades: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarFacultad(Facultad f) {
        String sql = "UPDATE facultades SET nombre_facultad=? WHERE id_facultad=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getNombreFacultad());
            ps.setInt(2, f.getIdFacultad());

            ps.executeUpdate();
            System.out.println("Facultad actualizada ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar facultad: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarFacultad(int idFacultad) {
        String sql = "DELETE FROM facultades WHERE id_facultad = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFacultad);

            ps.executeUpdate();
            System.out.println("Facultad eliminada ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar facultad: " + e.getMessage());
        }
    }
}