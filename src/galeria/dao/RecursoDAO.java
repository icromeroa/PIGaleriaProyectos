package galeria.dao;

import galeria.model.Recurso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {

    // 🔹 CREATE (INSERTAR)
	public void insertarRecurso(Recurso r, int idProyecto) {
	    String sql = "INSERT INTO recursos (nombre_recurso, url, tipo, fecha_subida, id_proyecto) VALUES (?, ?, ?, ?, ?)";

	    try (Connection con = conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, r.getNombreRecurso());
	        ps.setString(2, r.getUrl());
	        ps.setString(3, r.getTipo());
	        ps.setDate(4, new java.sql.Date(r.getFechaSubida().getTime()));
	        ps.setInt(5, idProyecto);

	        ps.executeUpdate();
	        System.out.println("Recurso guardado ");

	    } catch (SQLException e) {
	        System.out.println("Error al insertar recurso: " + e.getMessage());
	    }
	}

    // 🔹 READ (LISTAR POR PROYECTO)
    public List<Recurso> listarPorProyecto(int idProyecto) {
        List<Recurso> lista = new ArrayList<>();
        String sql = "SELECT * FROM recursos WHERE id_proyecto = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProyecto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Recurso r = new Recurso(
                        rs.getInt("id_recurso"),
                        rs.getString("nombre_recurso"),
                        rs.getString("url"),
                        rs.getString("tipo"),
                        rs.getDate("fecha_subida")
                    );
                    lista.add(r);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar recursos: " + e.getMessage());
        }

        return lista;
    }

 // 🔹 UPDATE
    public void actualizarRecurso(Recurso r) {
        String sql = "UPDATE recursos SET nombre_recurso=?, url=?, tipo=?, fecha_subida=? WHERE id_recurso=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getNombreRecurso()); // ✅ corregido
            ps.setString(2, r.getUrl());
            ps.setString(3, r.getTipo());
            ps.setDate(4, new java.sql.Date(r.getFechaSubida().getTime())); // ✅ corregido
            ps.setInt(5, r.getIdRecurso());

            ps.executeUpdate();
            System.out.println("Recurso actualizado ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar recurso: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarRecurso(int idRecurso) {
        String sql = "DELETE FROM recursos WHERE id_recurso = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRecurso);

            ps.executeUpdate();
            System.out.println("Recurso eliminado ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar recurso: " + e.getMessage());
        }
    }
}