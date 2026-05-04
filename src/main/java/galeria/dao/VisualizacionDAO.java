package galeria.dao;

import galeria.model.Proyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisualizacionDAO {

    public List<Proyecto> listarHistorialReciente(int idUsuario, int limite) {
        List<Proyecto> historial = new ArrayList<>();
        // Consulta que une visualizaciones con proyectos usando los nombres de columna de tu DB
        String sql = "SELECT p.* FROM registro_visualizaciones rv " +
                "JOIN proyectos p ON rv.id_proyecto = p.id_proyecto " +
                "WHERE rv.id_usuario = ? " +
                "ORDER BY rv.fecha_vista DESC LIMIT ?";

        // Cambiado a conexion.conectar() según tu código proporcionado
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, limite);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt("id_proyecto"));
                p.setTitulo(rs.getString("titulo"));
                p.setResumen(rs.getString("resumen"));
                p.setPortadaURL(rs.getString("portada_url"));
                p.setCantidadVistas(rs.getInt("cantidad_vistas"));
                historial.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error en VisualizacionDAO: " + e.getMessage());
        }
        return historial;
    }

    // Agrega este método a tu clase VisualizacionDAO.java

    public void registrarVisualizacion(int idUsuario, int idProyecto) {
        // Usamos ON DUPLICATE KEY UPDATE para que si el usuario vuelve a ver el proyecto,
        // simplemente se actualice la fecha al momento actual y suba al inicio de su historial.
        String sql = "INSERT INTO registro_visualizaciones (id_usuario, id_proyecto, fecha_vista) " +
                "VALUES (?, ?, NOW()) " +
                "ON DUPLICATE KEY UPDATE fecha_vista = NOW()";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idProyecto);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al registrar visualización: " + e.getMessage());
        }
    }
}