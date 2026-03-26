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
        String sqlRol = "INSERT INTO Usuario_Rol (ID_usuario, ID_rol) VALUES (?, 2)"; // ID_rol=2 = "usuario"
        
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

                // 3. Asignar rol automático de "usuario"
                ps = con.prepareStatement(sqlRol);
                ps.setLong(1, idGenerado);
                ps.executeUpdate();

                // Si todos los inserts fueron exitosos, confirmamos la transacción
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
        String sql = "SELECT U.*, E.Correo_electronico, I.Url_imagen, " +
                     "COALESCE(R.ID_rol, 2) as ID_rol, " + // Default a rol 2 si no tiene asignado
                     "COALESCE(R.Nombre_rol, 'usuario') as Nombre_rol " +
                     "FROM Usuario U " +
                     "JOIN Email E ON U.ID_usuario = E.ID_usuario " +
                     "LEFT JOIN Imagenes I ON U.ID_usuario = I.ID_usuario " +
                     "LEFT JOIN Usuario_Rol UR ON U.ID_usuario = UR.ID_usuario " +
                     "LEFT JOIN Rol R ON UR.ID_rol = R.ID_rol " +
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
                user.setIdRol(rs.getLong("ID_rol"));
                user.setNombreRol(rs.getString("Nombre_rol"));
                
                // Cargar permisos del usuario
                user.setPermisos(obtenerPermisosUsuario(rs.getLong("ID_usuario")));
            }
        } catch (SQLException e) {
            System.err.println("Error en Login DAO: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return user;
    }
    
    // Método auxiliar para obtener permisos del usuario
    private java.util.List<String> obtenerPermisosUsuario(long idUsuario) {
        java.util.List<String> permisos = new java.util.ArrayList<>();
        String sql = "SELECT P.Permiso " +
                     "FROM Permisos P " +
                     "JOIN Rol_Permisos RP ON P.ID_permisos = RP.ID_permisos " +
                     "JOIN Usuario_Rol UR ON RP.ID_rol = UR.ID_rol " +
                     "WHERE UR.ID_usuario = ?";
        
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                permisos.add(rs.getString("Permiso"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener permisos: " + e.getMessage());
        }
        
        return permisos;
    }

    // ── ACTUALIZAR USUARIO ─────────────────────────────────────────────────────
    public boolean actualizarUsuario(Usuario user) {
        String sqlUsuario = "UPDATE Usuario SET Nombre = ? WHERE ID_usuario = ?";
        String sqlEmail = "UPDATE Email SET Correo_electronico = ? WHERE ID_usuario = ?";
        
        try {
            con = cn.getConnection();
            // Desactivamos el autocommit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // 1. Actualizar nombre del usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, user.getNombre());
            ps.setLong(2, user.getIdUsuario());
            ps.executeUpdate();

            // 2. Actualizar correo electrónico
            ps = con.prepareStatement(sqlEmail);
            ps.setString(1, user.getCorreo());
            ps.setLong(2, user.getIdUsuario());
            ps.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmamos la transacción
            con.commit();
            return true;
            
        } catch (SQLException e) {
            // Si algo falla, deshacemos cualquier cambio en la base de datos
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error en Rollback: " + ex.getMessage());
            }
            System.err.println("Error en DAO Actualizar Usuario: " + e.getMessage());
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
    }

    // ── VERIFICAR SI CORREO EXISTE (EXCLUYENDO USUARIO ACTUAL) ─────────────
    public boolean existeCorreoOtroUsuario(String correo, long idUsuarioActual) {
        String sql = "SELECT COUNT(*) FROM Email WHERE Correo_electronico = ? AND ID_usuario != ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setLong(2, idUsuarioActual);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en validación de correo duplicado: " + e.getMessage());
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

    // ── OBTENER USUARIO POR ID ───────────────────────────────────────────────────
    public Usuario obtenerUsuarioPorId(long idUsuario) {
        String sql = "SELECT U.*, E.Correo_electronico, I.Url_imagen, " +
                     "COALESCE(R.ID_rol, 2) as ID_rol, " +
                     "COALESCE(R.Nombre_rol, 'usuario') as Nombre_rol " +
                     "FROM Usuario U " +
                     "JOIN Email E ON U.ID_usuario = E.ID_usuario " +
                     "LEFT JOIN Imagenes I ON U.ID_usuario = I.ID_usuario " +
                     "LEFT JOIN Usuario_Rol UR ON U.ID_usuario = UR.ID_usuario " +
                     "LEFT JOIN Rol R ON UR.ID_rol = R.ID_rol " +
                     "WHERE U.ID_usuario = ?";

        Usuario user = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                user.setIdUsuario(rs.getLong("ID_usuario"));
                user.setNombre(rs.getString("Nombre"));
                user.setCorreo(rs.getString("Correo_electronico"));
                user.setUrlImagen(rs.getString("Url_imagen"));
                user.setIdRol(rs.getLong("ID_rol"));
                user.setNombreRol(rs.getString("Nombre_rol"));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerUsuarioPorId: " + e.getMessage());
        } finally {
            try { 
                if (con != null) con.close(); 
            } catch (SQLException e) {}
        }
        return user;
    }
    
    // ── ACTUALIZAR CONTRASEÑA (recuperación) ─────────────────────────────────
    public boolean actualizarContrasena(Usuario user) {
        String sql = "UPDATE Usuario SET Contraseña = ? WHERE ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, user.getContrasena());
            ps.setLong(2, user.getIdUsuario());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao.actualizarContrasena: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}