package galeria.dao;

import galeria.model.Guardado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuardadoDAO {

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/galeria_db", "root", "s7jeriKo8");
    }

    public boolean guardarFavorito(int idUsuario, int idProyecto) {
        String sql = "INSERT INTO guardados (id_usuario, id_proyecto) VALUES (?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProyecto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean eliminarFavorito(int idUsuario, int idProyecto) {
        String sql = "DELETE FROM guardados WHERE id_usuario = ? AND id_proyecto = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProyecto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean esFavorito(int idUsuario, int idProyecto) {
        String sql = "SELECT 1 FROM guardados WHERE id_usuario = ? AND id_proyecto = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProyecto);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}