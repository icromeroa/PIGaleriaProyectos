package galeria.dao;

import galeria.model.Guardado;
import galeria.model.Proyecto;

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

    public List<Proyecto> listarProyectosGuardados(int idUsuario) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM proyectos p " +
                "JOIN guardados g ON p.id_proyecto = g.id_proyecto " +
                "WHERE g.id_usuario = ?";

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proyecto p = new Proyecto(); // Ya no dará error si añadiste el constructor vacío
                p.setIdProyecto(rs.getInt("id_proyecto"));
                p.setTitulo(rs.getString("titulo"));
                p.setResumen(rs.getString("resumen"));
                p.setPortadaURL(rs.getString("portada_url"));
                p.setCantidadVistas(rs.getInt("cantidad_vistas"));
                p.setCantidadGuardados(rs.getInt("cantidad_guardados"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}