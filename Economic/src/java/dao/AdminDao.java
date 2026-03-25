package dao;
 
import configuracion.ConexionDB;
import modelo.Usuario;
import modelo.ResultadoEliminacion;
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
                u.setNombreRol(rs.getString("Nombre_rol")); // Usar el campo correcto para el rol
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
 
    // ── ELIMINAR USUARIO (eliminación manual sin cascada) ───────────────────────
    // Elimina manualmente: Transacciones→Categorías→Usuario_Categoria→Email→Imagenes→Token_recuperacion→Usuario_Rol→Usuario
    public ResultadoEliminacion eliminarUsuario(long idUsuario) {
        ResultadoEliminacion resultado = new ResultadoEliminacion();
        int transaccionesEliminadas = 0;
        int categoriasEliminadas = 0;
        
        try {
            con = cn.getConnection();
            con.setAutoCommit(false); // Iniciar transacción
            
            // 1. Eliminar transacciones del usuario y contar
            String sqlTransacciones = "DELETE T FROM Transacciones T " +
                                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                                     "WHERE UC.ID_usuario = ?";
            ps = con.prepareStatement(sqlTransacciones);
            ps.setLong(1, idUsuario);
            transaccionesEliminadas = ps.executeUpdate();
            ps.close();
            
            // 2. Guardar IDs de categorías del usuario antes de eliminar relaciones
            String sqlObtenerCategorias = "SELECT DISTINCT C.ID_categoria FROM Categoria C " +
                                         "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                                         "WHERE UC.ID_usuario = ?";
            ps = con.prepareStatement(sqlObtenerCategorias);
            ps.setLong(1, idUsuario);
            rs = ps.executeQuery();
            
            StringBuilder categoriasIds = new StringBuilder();
            while (rs.next()) {
                if (categoriasIds.length() > 0) categoriasIds.append(",");
                categoriasIds.append(rs.getLong("ID_categoria"));
            }
            rs.close();
            ps.close();
            
            // 3. Eliminar relaciones usuario_categoría
            String sqlUsuarioCategoria = "DELETE FROM Usuario_Categoria WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlUsuarioCategoria);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            
            // 4. Eliminar categorías huérfanas y contar
            if (categoriasIds.length() > 0) {
                String sqlCategorias = "DELETE FROM Categoria WHERE ID_categoria IN (" + categoriasIds.toString() + ")";
                ps = con.prepareStatement(sqlCategorias);
                categoriasEliminadas = ps.executeUpdate();
                ps.close();
            }
            
            // 5. Eliminar email del usuario
            String sqlEmail = "DELETE FROM Email WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlEmail);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            
            // 5. Eliminar imágenes del usuario
            String sqlImagenes = "DELETE FROM Imagenes WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlImagenes);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            
            // 6. Eliminar tokens de recuperación del usuario
            String sqlTokens = "DELETE FROM Token_recuperacion WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlTokens);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            
            // 7. Eliminar roles del usuario
            String sqlUsuarioRol = "DELETE FROM Usuario_Rol WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlUsuarioRol);
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            
            // 8. Finalmente eliminar al usuario
            String sqlUsuario = "DELETE FROM Usuario WHERE ID_usuario = ?";
            ps = con.prepareStatement(sqlUsuario);
            ps.setLong(1, idUsuario);
            int filas = ps.executeUpdate();
            
            con.commit(); // Confirmar transacción
            
            // Configurar resultado
            resultado.setExito(filas > 0);
            resultado.setTransaccionesEliminadas(transaccionesEliminadas);
            resultado.setCategoriasEliminadas(categoriasEliminadas);
            
            return resultado;
            
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback(); // Revertir en caso de error
            } catch (SQLException rollbackEx) {
                System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error en AdminDao.eliminarUsuario: " + e.getMessage());
            resultado.setExito(false);
            resultado.setMensajeError("Error al eliminar usuario: " + e.getMessage());
            return resultado;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autoCommit: " + e.getMessage());
            }
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