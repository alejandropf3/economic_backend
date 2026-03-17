package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import configuracion.ConexionDB;
import modelo.Usuario;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioDao {

    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
    
    // Metodo para realizar registro de usuario

    public boolean registrar(Usuario user) {
        String sqlUsuario = "INSERT INTO Usuario (Nombre, Contraseña) VALUES (?, ?)";
        String sqlEmail = "INSERT INTO Email (ID_usuario, Correo_electronico) VALUES (?, ?)";
        
        try {
            con = cn.getConnection();
            // Desactivamos el autocommit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // 1. Insertar Usuario
            ps = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getContrasena()); // La contraseña ya viene encriptada del controlador
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long idGenerado = rs.getLong(1);

                // 2. Insertar el Correo usando el idGenerado
                ps = con.prepareStatement(sqlEmail);
                ps.setLong(1, idGenerado);
                ps.setString(2, user.getCorreo());
                ps.executeUpdate();

                // Si ambos inserts fueron exitosos, confirmamos la transacción
                con.commit();
                return true;
            }
        } catch (SQLException e) {
            // Si algo falla, deshacemos cualquier cambio en la base de datos
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error en Rollback: " + ex.getMessage());
            }
            System.err.println("Error en DAO Registrar: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM Email WHERE Correo_electronico = ?";
        return ejecutarConteo(sql, correo);
    }

    private boolean ejecutarConteo(String sql, String valor) {
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en validación: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
    
    public Usuario iniciarSesion(String correo, String contrasena) {
        String sql = "SELECT U.*, E.Correo_electronico, I.Url_imagen " +
                     "FROM Usuario U " +
                     "JOIN Email E ON U.ID_usuario = E.ID_usuario " +
                     "LEFT JOIN Imagenes I ON U.ID_usuario = I.ID_usuario " +
                     "WHERE E.Correo_electronico = ? AND U.Contraseña = ?";

        Usuario user = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                user.setIdUsuario(rs.getLong("ID_usuario"));
                user.setNombre(rs.getString("Nombre"));
                user.setContrasena(rs.getString("Contraseña"));
                user.setCorreo(rs.getString("Correo_electronico"));
                user.setUrlImagen(rs.getString("Url_imagen")); // puede ser null si no tiene imagen
            }
        } catch (SQLException e) {
            System.err.println("Error en Login DAO: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return user;
    }
}