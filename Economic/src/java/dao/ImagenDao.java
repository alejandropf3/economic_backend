package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import configuracion.ConexionDB;
 
public class ImagenDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    // ── Guardar o actualizar URL de imagen ────────────────────────────────────
    public boolean guardarImagen(long idUsuario, String urlImagen) {
        // Si ya tiene imagen la actualizamos, si no la insertamos
        if (existeImagen(idUsuario)) {
            return actualizarImagen(idUsuario, urlImagen);
        } else {
            return insertarImagen(idUsuario, urlImagen);
        }
    }
 
    private boolean insertarImagen(long idUsuario, String urlImagen) {
        String sql = "INSERT INTO Imagenes (ID_usuario, Url_imagen) VALUES (?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setString(2, urlImagen);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en insertarImagen: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    private boolean actualizarImagen(long idUsuario, String urlImagen) {
        String sql = "UPDATE Imagenes SET Url_imagen = ? WHERE ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, urlImagen);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en actualizarImagen: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── Obtener URL de imagen del usuario ─────────────────────────────────────
    public String obtenerUrlImagen(long idUsuario) {
        String sql = "SELECT Url_imagen FROM Imagenes WHERE ID_usuario = ? LIMIT 1";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("Url_imagen");
        } catch (SQLException e) {
            System.err.println("Error en obtenerUrlImagen: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return null;
    }
 
    // ── Verificar si el usuario ya tiene imagen ───────────────────────────────
    private boolean existeImagen(long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Imagenes WHERE ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en existeImagen: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return false;
    }
}