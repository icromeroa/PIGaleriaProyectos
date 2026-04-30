package galeria.dao;

import galeria.model.Proyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    // ════════════════════════════════════════════════
    // CONEXIÓN
    // ════════════════════════════════════════════════
	private Connection conectar() throws SQLException {
	    String url = "jdbc:mysql://localhost:3306/galeria_db?useSSL=false&serverTimezone=UTC";
	    String user = "root";
	    String pass = "1234";

	    return DriverManager.getConnection(url, user, pass);
	}

    // ════════════════════════════════════════════════
    // CREATE
    // ════════════════════════════════════════════════
    public void insertarProyecto(Proyecto p) {

        String sql = "INSERT INTO proyectos (id_proyecto, titulo, resumen, fecha_subida) VALUES (?, ?, ?, ?)";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdProyecto());
            ps.setString(2, p.getTitulo());
            ps.setString(3, p.getResumen());
            ps.setDate(4, new java.sql.Date(p.getFechaSubida().getTime()));

            ps.executeUpdate();
            System.out.println("[BD] Proyecto guardado correctamente");

        } catch (SQLException e) {
            System.out.println("[ERROR INSERT] " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════
    // READ (LISTAR)
    // ════════════════════════════════════════════════
    public List<Proyecto> listarTodos() {

        List<Proyecto> lista = new ArrayList<>();

        String sql = "SELECT * FROM proyectos";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Proyecto p = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("titulo"),
                        rs.getString("resumen"),
                        "N/A",
                        "N/A",
                        0,
                        0,
                        rs.getDate("fecha_subida"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        new ArrayList<>()
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR LISTAR] " + e.getMessage());
        }

        return lista;
    }

    // ════════════════════════════════════════════════
    // READ (BUSCAR POR ID)
    // ════════════════════════════════════════════════
    public Proyecto buscarPorId(int id) {

        String sql = "SELECT * FROM proyectos WHERE id_proyecto = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("titulo"),
                        rs.getString("resumen"),
                        "N/A",
                        "N/A",
                        0,
                        0,
                        rs.getDate("fecha_subida"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        new ArrayList<>()
                );
            }

        } catch (SQLException e) {
            System.out.println("[ERROR BUSCAR] " + e.getMessage());
        }

        return null;
    }

    // ════════════════════════════════════════════════
    // UPDATE
    // ════════════════════════════════════════════════
    public void actualizarProyecto(Proyecto p) {

        String sql = "UPDATE proyectos SET titulo=?, resumen=?, fecha_subida=? WHERE id_proyecto=?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getResumen());
            ps.setDate(3, new java.sql.Date(p.getFechaSubida().getTime()));
            ps.setInt(4, p.getIdProyecto());

            ps.executeUpdate();
            System.out.println("[BD] Proyecto actualizado");

        } catch (SQLException e) {
            System.out.println("[ERROR UPDATE] " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════
    // DELETE
    // ════════════════════════════════════════════════
    public void eliminarProyecto(int id) {

        String sql = "DELETE FROM proyectos WHERE id_proyecto = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("[BD] Proyecto eliminado");

        } catch (SQLException e) {
            System.out.println("[ERROR DELETE] " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════
    // GENERAR ID AUTOMÁTICO
    // ════════════════════════════════════════════════
    public int generarId() {

        String sql = "SELECT MAX(id_proyecto) FROM proyectos";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }

        } catch (SQLException e) {
            System.out.println("[ERROR ID] " + e.getMessage());
        }

        return 1;
    }
}