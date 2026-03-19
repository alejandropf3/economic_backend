package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import configuracion.ConexionDB;
import modelo.Categoria;
 
public class CategoriaDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    // ── CREAR ─────────────────────────────────────────────────────────────────
    // Inserta en Categoria y luego en Usuario_Categoria (transacción)
    public boolean crear(Categoria categoria) {
        String sqlCategoria = "INSERT INTO Categoria (Tipo_transaccion, Nombre_categoria) VALUES (?, ?)";
        String sqlRelacion  = "INSERT INTO Usuario_Categoria (ID_usuario, ID_categoria) VALUES (?, ?)";
        try {
            con = cn.getConnection();
            con.setAutoCommit(false);
 
            // 1. Insertar en Categoria y obtener ID generado
            ps = con.prepareStatement(sqlCategoria, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoria.getTipoTransaccion());
            ps.setString(2, categoria.getNombreCategoria());
            ps.executeUpdate();
 
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
 
                // 2. Insertar en tabla relacional
                ps = con.prepareStatement(sqlRelacion);
                ps.setLong(1, categoria.getIdUsuario());
                ps.setInt(2, idGenerado);
                ps.executeUpdate();
 
                con.commit();
                return true;
            }
            con.rollback();
            return false;
 
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en DAO Crear Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── LISTAR POR USUARIO ────────────────────────────────────────────────────
    // JOIN con Usuario_Categoria para obtener solo las categorías del usuario
    public List<Categoria> listarPorUsuario(long idUsuario) {
        String sql = "SELECT C.ID_categoria, C.Tipo_transaccion, C.Nombre_categoria " +
                     "FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE UC.ID_usuario = ?";
        List<Categoria> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("ID_categoria"));
                c.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                c.setNombreCategoria(rs.getString("Nombre_categoria"));
                c.setIdUsuario(idUsuario); // se asigna desde el parámetro
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
    // Verifica que la categoría pertenezca al usuario via Usuario_Categoria
    public boolean editar(Categoria categoria) {
        String sql = "UPDATE Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "SET C.Tipo_transaccion = ?, C.Nombre_categoria = ? " +
                     "WHERE C.ID_categoria = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
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
    // Elimina de Usuario_Categoria primero, luego Categoria si ya no tiene usuarios
    public boolean eliminar(int idCategoria, long idUsuario) {
        String sqlRelacion  = "DELETE FROM Usuario_Categoria " +
                              "WHERE ID_categoria = ? AND ID_usuario = ?";
        String sqlCategoria = "DELETE FROM Categoria WHERE ID_categoria = ? " +
                              "AND NOT EXISTS (" +
                              "SELECT 1 FROM Usuario_Categoria WHERE ID_categoria = ?)";
        try {
            con = cn.getConnection();
            con.setAutoCommit(false);
 
            // 1. Eliminar relación usuario-categoría
            ps = con.prepareStatement(sqlRelacion);
            ps.setInt(1, idCategoria);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();
 
            // 2. Eliminar categoría solo si ya no tiene ningún usuario asociado
            ps = con.prepareStatement(sqlCategoria);
            ps.setInt(1, idCategoria);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();
 
            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error en DAO Eliminar Categoria: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── VALIDAR NOMBRE DUPLICADO POR USUARIO ──────────────────────────────────
    public boolean existeNombre(String nombreCategoria, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE C.Nombre_categoria = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
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
        String sql = "SELECT COUNT(*) FROM Categoria C " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE C.Nombre_categoria = ? AND C.ID_categoria != ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
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
    
    // ── VERIFICAR SI LA CATEGORÍA ESTÁ EN USO EN TRANSACCIONES ───────────────────
    public boolean categoriaEnUso(int idCategoria) {
        String sql = "SELECT COUNT(*) FROM Transacciones WHERE ID_categoria = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en categoriaEnUso: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
 
}