package dao;
 
import configuracion.ConexionDB;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * DAO — AdminDao.java
 * Operaciones de base de datos para la vista de administración de usuarios.
 */
public class AdminDao {

    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // ── LISTAR TODOS LOS USUARIOS (orden de creación) ─────────────────────────
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT U.ID_usuario, U.Nombre, E.Correo_electronico, " +
                     "COALESCE(R.Nombre_rol, 'Sin rol') AS Nombre_rol, " +
                     "COALESCE(R.ID_rol, 2) AS ID_rol " +
                     "FROM Usuario U " +
                     "JOIN Email E ON U.ID_usuario = E.ID_usuario " +
                     "LEFT JOIN Usuario_Rol UR ON U.ID_usuario = UR.ID_usuario " +
                     "LEFT JOIN Rol R ON UR.ID_rol = R.ID_rol " +
                     "ORDER BY U.ID_usuario ASC";

        List<Usuario> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            rs  = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getLong("ID_usuario"));
                u.setNombre(rs.getString("Nombre"));
                u.setCorreo(rs.getString("Correo_electronico"));
                u.setCorreoRespaldo(rs.getString("Nombre_rol")); // reutilizamos campo temporal para el rol
                u.setIdRol(rs.getLong("ID_rol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.listarUsuarios: " + e.getMessage());
        } finally {
            cerrar();
        }
        return lista;
    }
    
    // ── VERIFICAR SI USUARIO TIENE PERMISO DE ADMINISTRADOR ─────────────────────
    public boolean esAdministrador(long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Usuario_Rol UR " +
                     "JOIN Rol R ON UR.ID_rol = R.ID_rol " +
                     "WHERE UR.ID_usuario = ? AND R.Nombre_rol = 'administrador'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.esAdministrador: " + e.getMessage());
        } finally {
            cerrar();
        }
        return false;
    }

    // ── VERIFICAR CONTRASEÑA DEL ADMINISTRADOR ────────────────────────────────
    public boolean verificarContrasenaAdmin(long idAdmin, String passEncriptada) {
        String sql = "SELECT COUNT(*) FROM Usuario WHERE ID_usuario = ? AND Contraseña = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idAdmin);
            ps.setString(2, passEncriptada);
            rs  = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.verificarContrasenaAdmin: " + e.getMessage());
        } finally {
            cerrar();
        }
        return false;
    }
 
    // ── CONTAR TRANSACCIONES DEL USUARIO ─────────────────────────────────────
    public int contarTransacciones(long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            rs  = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.contarTransacciones: " + e.getMessage());
        } finally {
            cerrar();
        }
        return 0;
    }
 
    // ── CONTAR CATEGORÍAS DEL USUARIO ─────────────────────────────────────────
    public int contarCategorias(long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Usuario_Categoria WHERE ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            rs  = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.contarCategorias: " + e.getMessage());
        } finally {
            cerrar();
        }
        return 0;
    }
 
    // ── ELIMINAR USUARIO (en cascada por FK ON DELETE CASCADE) ───────────────
    // Elimina: Email, Imagenes, Token_recuperacion, Usuario_Rol → Usuario
    public boolean eliminarUsuario(long idUsuario) {
        // Las FK con ON DELETE CASCADE se encargan de Email, Imagenes,
        // Usuario_Rol, Token_recuperacion y Usuario_Categoria automáticamente.
        String sql = "DELETE FROM Usuario WHERE ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error en AdminDao.eliminarUsuario: " + e.getMessage());
            return false;
        } finally {
            cerrar();
        }
    }
 
    // ── CERRAR RECURSOS ───────────────────────────────────────────────────────
    private void cerrar() {
        try {
            if (rs  != null) rs.close();
            if (ps  != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión AdminDao: " + e.getMessage());
        }
    }
}