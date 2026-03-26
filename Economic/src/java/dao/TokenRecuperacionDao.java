package dao;
 
import configuracion.ConexionDB;
import java.sql.*;
import java.time.LocalDateTime;
 
/**
 * DAO — TokenRecuperacionDao.java
 * Operaciones sobre la tabla Token_recuperacion.
 */
public class TokenRecuperacionDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    // ── GUARDAR NUEVO TOKEN ───────────────────────────────────────────────────
    public boolean guardarToken(long idUsuario, String token, LocalDateTime expiracion) {
        String sql = "INSERT INTO Token_recuperacion "
                   + "(ID_usuario, Token, Fecha_creacion, Fecha_expriracion, Estado) "
                   + "VALUES (?, ?, ?, ?, true)";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setString(2, token);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(4, Timestamp.valueOf(expiracion));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en TokenDao.guardarToken: " + e.getMessage());
            return false;
        } finally {
            cerrar();
        }
    }
 
    // ── INVALIDAR TOKENS ANTERIORES DEL USUARIO ──────────────────────────────
    // Se llama antes de generar uno nuevo para no dejar tokens huérfanos activos
    public void invalidarTokensAnteriores(long idUsuario) {
        String sql = "UPDATE Token_recuperacion SET Estado = false "
                   + "WHERE ID_usuario = ? AND Estado = true";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en TokenDao.invalidarTokensAnteriores: " + e.getMessage());
        } finally {
            cerrar();
        }
    }
 
    // ── VALIDAR TOKEN ─────────────────────────────────────────────────────────
    // Retorna el ID del usuario si el token es válido, activo y no expirado
    // Retorna -1 si el token no es válido
    public long validarToken(long idUsuario, String token) {
        String sql = "SELECT ID_usuario FROM Token_recuperacion "
                   + "WHERE ID_usuario = ? AND Token = ? "
                   + "AND Estado = true "
                   + "AND Fecha_expriracion > NOW()";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setString(2, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("ID_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error en TokenDao.validarToken: " + e.getMessage());
        } finally {
            cerrar();
        }
        return -1;
    }
 
    // ── MARCAR TOKEN COMO USADO ───────────────────────────────────────────────
    // Se llama después de verificar el código correctamente
    public void marcarTokenUsado(long idUsuario, String token) {
        String sql = "UPDATE Token_recuperacion SET Estado = false "
                   + "WHERE ID_usuario = ? AND Token = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setString(2, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en TokenDao.marcarTokenUsado: " + e.getMessage());
        } finally {
            cerrar();
        }
    }
 
    // ── OBTENER ID DE USUARIO POR CORREO ─────────────────────────────────────
    // Necesario para operaciones que solo tienen el correo de sesión
    public long obtenerIdUsuarioPorCorreo(String correo) {
        String sql = "SELECT ID_usuario FROM Email WHERE Correo_electronico = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("ID_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error en TokenDao.obtenerIdUsuarioPorCorreo: " + e.getMessage());
        } finally {
            cerrar();
        }
        return -1;
    }
 
    // ── CERRAR CONEXIÓN ───────────────────────────────────────────────────────
    private void cerrar() {
        try {
            if (ps  != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión TokenDao: " + e.getMessage());
        }
    }
}