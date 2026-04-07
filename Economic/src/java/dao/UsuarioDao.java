package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import configuracion.ConexionDB;
import modelo.Usuario;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Data Access Object para la entidad Usuario.
 * <p>
 * Esta clase es el DAO más crítico y complejo del sistema Economic porque
 * maneja la autenticación, autorización y la entidad central del sistema.
 * Combina múltiples tablas (Usuario, Email, Imagenes, Rol, Usuario_Rol, Permisos)
 * y gestiona transacciones complejas para mantener la integridad de datos.
 * </p>
 * <p>
 * ¿Por qué no usar un ORM como Hibernate? Porque este es un sistema de
 * aprendizaje donde queremos mostrar el control explícito del SQL y el
 * manejo manual de transacciones. Además, el SQL optimizado manualmente
 * puede ser más eficiente que el generado automáticamente por ORMs.
 * </p>
 * <p>
 * Todas las operaciones usan PreparedStatement para prevenir SQL Injection
 * y siguen el patrón try-catch-finally para garantizar el cierre de conexiones.
 * </p>
 */
public class UsuarioDao {

    // Componentes de conexión a base de datos
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
    
    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * Este método es crucial porque debe insertar datos en múltiples tablas
     * de forma atómica: Usuario, Email y Usuario_Rol. Si alguna de las
     * inserciones falla, toda la operación debe ser revertida para mantener
     * la integridad de la base de datos.
     * </p>
     * <p>
     * ¿Por qué manejar la transacción manualmente? Porque necesitamos que
     * las tres inserciones (Usuario, Email, Rol) sean una sola unidad
     * atómica. Si el usuario se crea pero el email falla, tendríamos un
     * usuario incompleto que no podría iniciar sesión.
     * </p>
     *
     * @param user Objeto Usuario con datos básicos (nombre, contraseña, correo).
     * @return true si el registro fue exitoso, false si ocurrió algún error.
     */
    public boolean registrar(Usuario user) {
        // SQL statements para cada tabla que debe ser actualizada
        String sqlUsuario = "INSERT INTO Usuario (Nombre, Contraseña) VALUES (?, ?)";
        String sqlEmail = "INSERT INTO Email (ID_usuario, Correo_electronico) VALUES (?, ?)";
        String sqlRol = "INSERT INTO Usuario_Rol (ID_usuario, ID_rol) VALUES (?, 2)"; // ID_rol=2 = "usuario"
        
        try {
            con = cn.getConnection();
            // Desactivamos el autocommit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // 1. Insertar Usuario y obtener el ID generado
            ps = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getContrasena()); // La contraseña ya viene encriptada del controlador
            ps.executeUpdate();

            // Obtener el ID auto-generado para usarlo en las siguientes inserciones
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long idGenerado = rs.getLong(1);

                // 2. Insertar el Correo usando el idGenerado
                ps = con.prepareStatement(sqlEmail);
                ps.setLong(1, idGenerado);
                ps.setString(2, user.getCorreo());
                ps.executeUpdate();

                // 3. Asignar rol automático de "usuario" (ID_rol = 2)
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
            // Siempre cerramos la conexión para liberar recursos
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Verifica si un correo electrónico ya existe en la base de datos.
     * <p>
     * Este método es crucial durante el registro para garantizar la unicidad
     * de correos electrónicos, que actúan como identificadores únicos para
     * el inicio de sesión de usuarios.
     * </p>
     *
     * @param correo Correo electrónico a verificar.
     * @return true si el correo ya existe, false si está disponible.
     */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM Email WHERE Correo_electronico = ?";
        return ejecutarConteo(sql, correo);
    }

    /**
     * Método auxiliar para ejecutar consultas de conteo (COUNT(*)).
     * <p>
     * Este método centraliza la lógica de consultas COUNT(*) que se usan
     * frecuentemente para validaciones de existencia. Evita duplicación
     * de código y maneja consistentemente el cierre de conexiones.
     * </p>
     *
     * @param sql   Consulta SQL con un parámetro placeholder.
     * @param valor Valor a insertar en el placeholder.
     * @return true si el conteo es mayor a cero, false en caso contrario.
     */
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
                System.err.println("Error al cerrar conexión en ejecutarConteo: " + e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Autentica un usuario y carga sus datos completos incluyendo permisos.
     * <p>
     * Este método es el corazón del sistema de autenticación. Realiza un
     * JOIN complejo para obtener todos los datos del usuario en una sola
     * consulta, optimizando el rendimiento al evitar múltiples queries.
     * </p>
     * <p>
     * ¿Por qué usar COALESCE? Para proporcionar valores por defecto
     * si un usuario no tiene rol asignado (caso edge que no debería ocurrir
     * pero que manejamos para robustez). Esto evita NullPointerExceptions.
     * </p>
     * <p>
     * ¿Por qué LEFT JOINs para Imagenes y Rol? Porque estos son opcionales:
     * - Un usuario puede no tener foto de perfil
     * - Un usuario podría no tener rol asignado (caso edge)
     * </p>
     *
     * @param correo    Correo electrónico del usuario.
     * @param contrasena Contraseña ya encriptada con SHA-256.
     * @return Objeto Usuario completo con permisos cargados, o null si no autentica.
     */
    public Usuario iniciarSesion(String correo, String contrasena) {
        // Query complejo que une todas las tablas necesarias para el login
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
                
                // Cargar permisos del usuario para el sistema de autorización
                user.setPermisos(obtenerPermisosUsuario(rs.getLong("ID_usuario")));
            }
        } catch (SQLException e) {
            System.err.println("Error en Login DAO: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return user;
    }
    
    /**
     * Obtiene la lista de permisos asignados a un usuario.
     * <p>
     * Este método carga los permisos del usuario basándose en su rol.
     * Los permisos se usan en ValidadorPermisos para controlar el acceso
     * a diferentes funcionalidades del sistema.
     * </p>
     * <p>
     * ¿Por qué no cargar los permisos en el query principal? Porque
     * haría el query aún más complejo y los permisos son una lista
     * que requiere procesamiento adicional. Es más limpio separar
     * esta lógica en su propio método.
     * </p>
     *
     * @param idUsuario ID del usuario cuyos permisos se van a cargar.
     * @return Lista de strings con los nombres de los permisos.
     */
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
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        
        return permisos;
    }

    // ── ACTUALIZAR USUARIO ─────────────────────────────────────────────────────
    public boolean actualizarUsuario(Usuario user) {
        String sqlUsuario = "UPDATE Usuario SET Nombre = ?, Contraseña = ? WHERE ID_usuario = ?";
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