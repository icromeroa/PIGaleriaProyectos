package galeria.dao;

import galeria.model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // 🔹 CREATE (INSERTAR)
    public void insertarCategoria(Categoria c) {
        String sql = "INSERT INTO categorias (nombre_categoria, descripcion, icono) VALUES (?, ?, ?)";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setString(3, c.getIcono());

            ps.executeUpdate();
            System.out.println("Categoria guardada ");

        } catch (SQLException e) {
            System.out.println("Error al insertar categoria: " + e.getMessage());
        }
    }

    // 🔹 READ (LISTAR TODAS)
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria, descripcion, icono FROM categorias";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre_categoria"),
                    rs.getString("descripcion"),
                    rs.getString("icono")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorias: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 UPDATE
    public void actualizarCategoria(Categoria c) {
        String sql = "UPDATE categorias SET nombre_categoria=?, descripcion=?, icono=? WHERE id_categoria=?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setString(3, c.getIcono());
            ps.setInt(4, c.getIdCategoria());

            ps.executeUpdate();
            System.out.println("Categoria actualizada ");

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
        }
    }

    // 🔹 DELETE
    public void eliminarCategoria(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            ps.executeUpdate();
            System.out.println("Categoria eliminada ");

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoria: " + e.getMessage());
        }
    }

    public List<Object[]> listarTopCategoriasPorProyectos(int limite) {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
        SELECT c.id_categoria, c.nombre_categoria, c.descripcion, c.icono,
               COUNT(p.id_proyecto) as total_proyectos
        FROM categorias c
        LEFT JOIN proyectos p ON p.id_categoria = c.id_categoria
        GROUP BY c.id_categoria
        ORDER BY total_proyectos DESC
        LIMIT ?
        """;
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                            rs.getInt("id_categoria"),
                            rs.getString("nombre_categoria"),
                            rs.getString("descripcion"),
                            rs.getInt("total_proyectos")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listarTopCategorias: " + e.getMessage());
        }
        return lista;
    }
}