package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import configuracion.ConexionDB;
import modelo.Categoria;
 
public class CategoriaDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    // ── CREAR ─────────────────────────────────────────────────────────────────
    public boolean crear(Categoria categoria) {
        String sql = "INSERT INTO Categoria (Tipo_transaccion, Nombre_categoria, ID_usuario) VALUES (?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.setLong(3, categoria.getIdUsuario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Crear Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── LISTAR POR USUARIO ────────────────────────────────────────────────────
    public List<Categoria> listarPorUsuario(long idUsuario) {
        String sql = "SELECT ID_categoria, Tipo_transaccion, Nombre_categoria, ID_usuario " +
                     "FROM Categoria WHERE ID_usuario = ?";
        List<Categoria> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("ID_categoria"));
                c.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                c.setNombreCategoria(rs.getString("Nombre_categoria"));
                c.setIdUsuario(rs.getLong("ID_usuario"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error en DAO Listar Categorias: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return lista;
    }
 
    // ── EDITAR ────────────────────────────────────────────────────────────────
    public boolean editar(Categoria categoria) {
        // Incluye ID_usuario en el WHERE para evitar que un usuario edite categorías de otro
        String sql = "UPDATE Categoria SET Tipo_transaccion = ?, Nombre_categoria = ? " +
                     "WHERE ID_categoria = ? AND ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.setInt(3, categoria.getIdCategoria());
            ps.setLong(4, categoria.getIdUsuario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Editar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── ELIMINAR ──────────────────────────────────────────────────────────────
    public boolean eliminar(int idCategoria, long idUsuario) {
        // Incluye ID_usuario en el WHERE para evitar que un usuario elimine categorías de otro
        String sql = "DELETE FROM Categoria WHERE ID_categoria = ? AND ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Eliminar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── VALIDAR NOMBRE DUPLICADO POR USUARIO ──────────────────────────────────
    public boolean existeNombre(String nombreCategoria, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Categoria " +
                     "WHERE Nombre_categoria = ? AND ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ps.setLong(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en validación nombre categoria: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
 
    // ── VALIDAR NOMBRE DUPLICADO EXCLUYENDO ID ACTUAL (para editar) ───────────
    public boolean existeNombreExcluyendoId(String nombreCategoria, int idCategoria, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Categoria " +
                     "WHERE Nombre_categoria = ? AND ID_categoria != ? AND ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ps.setInt(2, idCategoria);
            ps.setLong(3, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en validación nombre (editar): " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
}