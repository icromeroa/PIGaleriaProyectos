package galeria.dao;

import galeria.model.Proyecto;
import galeria.model.Valoracion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValoracionDAO {
	
    // 🔹 CREATE (INSERTAR)
    public void insertarValoracion(Valoracion v) {
        String sql = "INSERT INTO valoraciones (id_usuario, id_proyecto, puntuacion, fecha_valoracion) VALUES (?, ?, ?, ?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, v.getUsuario().getIdUsuario());
            ps.setInt(2, v.getProyecto().getIdProyecto());
            ps.setInt(3, v.getPuntuacion());
            ps.setDate(4, new java.sql.Date(v.getFechaValoracion().getTime()));

            ps.executeUpdate();
            System.out.println("Valoracion guardada ");

        } catch (SQLException e) {
            System.out.println("Error al insertar valoracion: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR POR PROYECTO)
    public List<Valoracion> listarPorProyecto(int idProyecto) {
        List<Valoracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM valoraciones WHERE id_proyecto = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProyecto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Valoracion v = new Valoracion(
                        rs.getInt("id_valoracion"),
                        null, // usuario (puedes mejorarlo luego)
                        null, // proyecto
                        rs.getInt("puntuacion"),
                        rs.getDate("fecha_valoracion")
                    );

                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar valoraciones: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 READ (LISTAR TODAS)
    public List<Valoracion> listarTodas() {
        List<Valoracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM valoraciones";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Valoracion v = new Valoracion(
                    rs.getInt("id_valoracion"),
                    null,
                    null,
                    rs.getInt("puntuacion"),
                    rs.getDate("fecha_valoracion")
                );

                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar valoraciones: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarValoracion(Valoracion v) {
        String sql = "UPDATE valoraciones SET puntuacion=? WHERE id_valoracion=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, v.getPuntuacion());
            ps.setInt(2, v.getIdValoracion());

            ps.executeUpdate();
            System.out.println("Valoracion actualizada ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar valoracion: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarValoracion(int idValoracion) {
        String sql = "DELETE FROM valoraciones WHERE id_valoracion = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idValoracion);

            ps.executeUpdate();
            System.out.println("Valoracion eliminada ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar valoracion: " + e.getMessage());
        }
    }

    public int contarValoracionesPorUsuario(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM valoraciones WHERE id_usuario = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Valoracion> listarValoracionesPorUsuario(int idUsuario) {
        List<Valoracion> lista = new ArrayList<>();
        // Ajustamos la consulta para traer el ID del proyecto directamente
        String sql = "SELECT id_valoracion, id_proyecto, puntuacion, fecha_valoracion FROM valoraciones WHERE id_usuario = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Creamos un objeto Proyecto vacío solo con el ID para cumplir con el modelo
                    Proyecto pSimple = new Proyecto();
                    pSimple.setIdProyecto(rs.getInt("id_proyecto"));

                    Valoracion v = new Valoracion(
                            rs.getInt("id_valoracion"),
                            null, // El usuario ya lo conocemos (es el actual)
                            pSimple,
                            rs.getInt("puntuacion"),
                            rs.getDate("fecha_valoracion")
                    );
                    lista.add(v);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar valoraciones por usuario: " + e.getMessage());
        }
        return lista;
    }
}