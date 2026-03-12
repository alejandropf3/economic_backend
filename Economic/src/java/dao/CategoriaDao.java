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
 
    // ─── CREAR ────────────────────────────────────────────────────────────────
    public boolean crear(Categoria categoria) {
        String sql = "INSERT INTO Categoria (Tipo_transaccion, Nombre_categoria) VALUES (?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Crear Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ─── LISTAR TODAS ─────────────────────────────────────────────────────────
    public List<Categoria> listar() {
        String sql = "SELECT ID_categoria, Tipo_transaccion, Nombre_categoria FROM Categoria";
        List<Categoria> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("ID_categoria"));
                c.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                c.setNombreCategoria(rs.getString("Nombre_categoria"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error en DAO Listar Categorias: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return lista;
    }
 
    // ─── EDITAR ───────────────────────────────────────────────────────────────
    public boolean editar(Categoria categoria) {
        String sql = "UPDATE Categoria SET Tipo_transaccion = ?, Nombre_categoria = ? WHERE ID_categoria = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.setInt(3, categoria.getIdCategoria());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Editar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ─── ELIMINAR ─────────────────────────────────────────────────────────────
    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM Categoria WHERE ID_categoria = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Eliminar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ─── VALIDAR NOMBRE DUPLICADO ─────────────────────────────────────────────
    public boolean existeNombre(String nombreCategoria) {
        String sql = "SELECT COUNT(*) FROM Categoria WHERE Nombre_categoria = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en validación nombre categoria: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
 
    // ─── VALIDAR NOMBRE DUPLICADO EXCLUYENDO ID ACTUAL (para editar) ──────────
    public boolean existeNombreExcluyendoId(String nombreCategoria, int idCategoria) {
        String sql = "SELECT COUNT(*) FROM Categoria WHERE Nombre_categoria = ? AND ID_categoria != ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCategoria);
            ps.setInt(2, idCategoria);
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