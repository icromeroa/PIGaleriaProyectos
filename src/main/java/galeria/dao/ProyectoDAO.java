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
	    String pass = "s7jeriKo8";

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

    public List<Proyecto> listarTopPorVistas(int limite) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = """
        SELECT p.*, c.nombre_categoria, f.nombre_facultad
        FROM proyectos p
        LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
        LEFT JOIN facultades f ON p.id_facultad = f.id_facultad
        ORDER BY p.cantidad_vistas DESC
        LIMIT ?
        """;
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Proyecto p = new Proyecto(
                            rs.getInt("id_proyecto"),
                            rs.getString("titulo"),
                            rs.getString("resumen"),
                            rs.getString("archivo_url") != null ? rs.getString("archivo_url") : "",
                            rs.getString("portada_url") != null ? rs.getString("portada_url") : "",
                            rs.getInt("cantidad_vistas"),
                            rs.getInt("cantidad_guardados"),
                            rs.getDate("fecha_subida"),
                            new ArrayList<>(), new ArrayList<>(),
                            null, null, null, null,
                            null, new ArrayList<>()
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR TOP VISTAS] " + e.getMessage());
        }
        return lista;
    }

    public int[] getEstadisticasGenerales() {
        // [totalVistas, totalProyectos, totalGuardados, totalUsuarios]
        String sql = """
        SELECT 
            (SELECT COALESCE(SUM(cantidad_vistas), 0) FROM proyectos),
            (SELECT COUNT(*) FROM proyectos),
            (SELECT COALESCE(SUM(cantidad_guardados), 0) FROM proyectos),
            (SELECT COUNT(*) FROM usuarios)
        """;
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new int[]{
                        rs.getInt(1), // vistas
                        rs.getInt(2), // proyectos
                        rs.getInt(3), // guardados
                        rs.getInt(4)  // usuarios
                };
            }
        } catch (SQLException e) {
            System.out.println("[ERROR STATS] " + e.getMessage());
        }
        return new int[]{0, 0, 0, 0};
    }

    // Busqueda predictiva por titulo
    public List<Proyecto> buscarPorTitulo(String texto) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = """
        SELECT p.*, a.nombre_autor
        FROM proyectos p
        LEFT JOIN proyecto_autores pa ON p.id_proyecto = pa.id_proyecto
        LEFT JOIN autores a ON pa.id_autor = a.id_autor
        WHERE p.titulo LIKE ?
        ORDER BY p.cantidad_vistas DESC
        """;
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProyecto(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR BUSCAR TITULO] " + e.getMessage());
        }
        return lista;
    }

    // Filtrar por programa, materia, semestre, categoria (cualquier combinacion)
    public List<Proyecto> filtrar(Integer idPrograma, Integer idMateria,
                                  Integer idSemestre, Integer idCategoria) {
        List<Proyecto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT p.*, a.nombre_autor
        FROM proyectos p
        LEFT JOIN proyecto_autores pa ON p.id_proyecto = pa.id_proyecto
        LEFT JOIN autores a ON pa.id_autor = a.id_autor
        WHERE 1=1
        """);
        if (idPrograma  != null) sql.append(" AND p.id_programa = ").append(idPrograma);
        if (idMateria   != null) sql.append(" AND p.id_materia = ").append(idMateria);
        if (idSemestre  != null) sql.append(" AND p.id_semestre = ").append(idSemestre);
        if (idCategoria != null) sql.append(" AND p.id_categoria = ").append(idCategoria);
        sql.append(" ORDER BY p.cantidad_vistas DESC");

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearProyecto(rs));
        } catch (SQLException e) {
            System.out.println("[ERROR FILTRAR] " + e.getMessage());
        }
        return lista;
    }

    // Listar todos con autor incluido
    public List<Proyecto> listarTodosConAutor() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = """
        SELECT p.*, a.nombre_autor
        FROM proyectos p
        LEFT JOIN proyecto_autores pa ON p.id_proyecto = pa.id_proyecto
        LEFT JOIN autores a ON pa.id_autor = a.id_autor
        ORDER BY p.fecha_subida DESC
        """;
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearProyecto(rs));
        } catch (SQLException e) {
            System.out.println("[ERROR LISTAR CON AUTOR] " + e.getMessage());
        }
        return lista;
    }

    // Helper privado para no repetir el mapeo
    private Proyecto mapearProyecto(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto(
                rs.getInt("id_proyecto"),
                rs.getString("titulo"),
                rs.getString("resumen"),
                rs.getString("archivo_url") != null ? rs.getString("archivo_url") : "",
                rs.getString("portada_url") != null ? rs.getString("portada_url") : "",
                rs.getInt("cantidad_vistas"),
                rs.getInt("cantidad_guardados"),
                rs.getDate("fecha_subida"),
                new ArrayList<>(), new ArrayList<>(),
                null, null, null, null, null, new ArrayList<>()
        );
        // Agregar autor si viene en el ResultSet
        try {
            String nombreAutor = rs.getString("nombre_autor");
            if (nombreAutor != null) {
                p.getListaAutores().add(
                        new galeria.model.Autor(0, nombreAutor, "")
                );
            }
        } catch (SQLException ignored) {}
        return p;
    }

}