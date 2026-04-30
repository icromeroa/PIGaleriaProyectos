package galeria.dao;

import galeria.model.Semestre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemestreDAO {

    // 🔹 CREATE (INSERTAR)
    public void insertarSemestre(Semestre s) {
        String sql = "INSERT INTO semestres (anio, periodo) VALUES (?, ?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getAnio());
            ps.setInt(2, s.getPeriodo());

            ps.executeUpdate();
            System.out.println("Semestre guardado ");

        } catch (SQLException e) {
            System.out.println("Error al insertar semestre: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR TODOS)
    public List<Semestre> listar() {
        List<Semestre> lista = new ArrayList<>();
        String sql = "SELECT id_semestre, anio, periodo FROM semestres";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Semestre s = new Semestre(
                    rs.getInt("id_semestre"),
                    rs.getInt("anio"),
                    rs.getInt("periodo")
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar semestres: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarSemestre(Semestre s) {
        String sql = "UPDATE semestres SET anio=?, periodo=? WHERE id_semestre=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getAnio());
            ps.setInt(2, s.getPeriodo());
            ps.setInt(3, s.getIdSemestre());

            ps.executeUpdate();
            System.out.println("Semestre actualizado ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar semestre: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarSemestre(int idSemestre) {
        String sql = "DELETE FROM semestres WHERE id_semestre = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSemestre);

            ps.executeUpdate();
            System.out.println("Semestre eliminado ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar semestre: " + e.getMessage());
        }
    }
}