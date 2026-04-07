package dao; // Paquete que contiene todas las clases de acceso a datos
 
// Importaciones necesarias para el manejo de transacciones y base de datos
import java.sql.Connection;           // Para establecer conexión con la base de datos
import java.sql.PreparedStatement;    // Para ejecutar consultas SQL seguras
import java.sql.ResultSet;            // Para procesar los resultados de consultas
import java.sql.SQLException;         // Para manejar errores de base de datos
import java.util.ArrayList;           // Para crear listas de transacciones
import java.util.List;                // Para manejar colecciones de objetos
import configuracion.ConexionDB;      // Para obtener conexiones a la base de datos
import modelo.Transaccion;            // Clase que representa una transacción financiera

/**
 * Data Access Object para la entidad Transaccion.
 * <p>
 * Este DAO maneja todas las operaciones relacionadas con las transacciones
 * financieras del sistema. Es uno de los DAOs más importantes porque las
 * transacciones son el corazón del sistema financiero, afectando balances,
 * reportes y metas de ahorro.
 * </p>
 * <p>
 * ¿Por qué usar consultas dinámicas con StringBuilder? Porque necesitamos
 * construir consultas SQL flexibles que puedan filtrar por diferentes
 * criterios (tipo, categoría, fechas) sin tener que escribir múltiples
 * métodos similares.
 * </p>
 */
public class TransaccionDao {
 
    // Componentes de conexión a base de datos
    ConexionDB cn = new ConexionDB(); // Objeto para gestionar conexiones
    Connection con;                  // Conexión activa con la base de datos
    PreparedStatement ps;             // Consulta SQL preparada para ejecutar
 
    /**
     * Crea una nueva transacción en la base de datos.
     * 
     * Este método inserta una transacción con todos sus datos financieros.
     * Nota importante: el ID_usuario se maneja a través de la relación
     * con Usuario_Categoria, no directamente en la tabla Transacciones.
     * 
     * @param t Objeto Transaccion con todos los datos a guardar
     * @return true si la transacción se creó exitosamente, false si hubo error
     */
    public boolean crear(Transaccion t) {
        // Consulta SQL para insertar una nueva transacción
        String sql = "INSERT INTO Transacciones " +
                     "(ID_categoria, ID_meta, Valor_transaccion, Descripcion, Fecha_realizacion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            // Obtiene una conexión a la base de datos
            con = cn.getConnection();
            // Prepara la consulta SQL para evitar inyección SQL
            ps  = con.prepareStatement(sql);
            
            // Asigna los valores a los parámetros de la consulta
            ps.setInt(1, t.getIdCategoria()); // ID de la categoría (obligatorio)
            // ID_meta puede ser null si la transacción no está asociada a una meta
            ps.setObject(2, t.getIdMeta() != 0 ? t.getIdMeta() : null);
            ps.setBigDecimal(3, t.getValorTransaccion()); // Valor monetario con precisión
            ps.setString(4, t.getDescripcion()); // Descripción opcional de la transacción
            // Convierte LocalDate a sql.Date para la base de datos
            ps.setDate(5, java.sql.Date.valueOf(t.getFechaRealizacion()));
            
            // Ejecuta la consulta de inserción
            ps.executeUpdate();
            return true; // Retorna éxito si todo salió bien
        } catch (SQLException e) {
            // Registra el error para depuración
            System.err.println("Error en DAO Crear Transaccion: " + e.getMessage());
            return false; // Retorna error si hubo problema
        } finally {
            // Siempre cierra la conexión para liberar recursos
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    /**
     * Lista transacciones de un usuario con filtros opcionales.
     * 
     * Este método construye una consulta SQL dinámica que puede filtrar por:
     * - Tipo de transacción (Ingreso/Egreso)
     * - Categoría específica
     * - Rango de fechas
     * 
     * @param idUsuario    ID del usuario dueño de las transacciones
     * @param tipo         Tipo de transacción ("Ingreso", "Egreso" o null para todos)
     * @param idCategoria  ID de categoría específica o null para todas
     * @param fechaDesde   Fecha inicial del rango (formato yyyy-MM-dd) o null
     * @param fechaHasta   Fecha final del rango (formato yyyy-MM-dd) o null
     * @return Lista de transacciones que cumplen con los filtros
     */
    public List<Transaccion> listarPorUsuario(long idUsuario, String tipo,
                                               String idCategoria,
                                               String fechaDesde, String fechaHasta) {
        // Construye la consulta SQL base usando StringBuilder para modificarla dinámicamente
        StringBuilder sql = new StringBuilder(
            "SELECT T.ID_transaccion, UC.ID_usuario, T.ID_categoria, " +
            "C.Nombre_categoria, C.Tipo_transaccion, T.Valor_transaccion, " +
            "T.Descripcion, T.Fecha_realizacion " +
            "FROM Transacciones T " +
            "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
            "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
            "WHERE UC.ID_usuario = ?"
        );
 
        List<Object> params = new ArrayList<>();
        params.add(idUsuario);
 
        if (tipo != null && !tipo.isEmpty() && !tipo.equals("todos")) {
            sql.append(" AND C.Tipo_transaccion = ?");
            params.add(tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase());
        }
        if (idCategoria != null && !idCategoria.isEmpty() && !idCategoria.equals("todas")) {
            sql.append(" AND T.ID_categoria = ?");
            params.add(Integer.parseInt(idCategoria));
        }
        if (fechaDesde != null && !fechaDesde.isEmpty()) {
            sql.append(" AND T.Fecha_realizacion >= ?");
            params.add(java.sql.Date.valueOf(fechaDesde));
        }
        if (fechaHasta != null && !fechaHasta.isEmpty()) {
            sql.append(" AND T.Fecha_realizacion <= ?");
            params.add(java.sql.Date.valueOf(fechaHasta));
        }
 
        sql.append(" ORDER BY T.ID_transaccion DESC");
 
        List<Transaccion> lista = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaccion t = new Transaccion();
                t.setIdTransaccion(rs.getLong("ID_transaccion"));
                t.setIdUsuario(rs.getLong("ID_usuario"));
                t.setIdCategoria(rs.getInt("ID_categoria"));
                t.setNombreCategoria(rs.getString("Nombre_categoria"));
                t.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                t.setValorTransaccion(rs.getBigDecimal("Valor_transaccion"));
                t.setDescripcion(rs.getString("Descripcion"));
                t.setFechaRealizacion(rs.getDate("Fecha_realizacion").toLocalDate());
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en DAO Listar Transacciones: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return lista;
    }
 
    // ── EDITAR ────────────────────────────────────────────────────────────────
    // Verifica propiedad via JOIN con Usuario_Categoria
    public boolean editar(Transaccion t) {
        String sql = "UPDATE Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "SET T.ID_categoria = ?, T.Valor_transaccion = ?, " +
                     "T.Descripcion = ?, T.Fecha_realizacion = ? " +
                     "WHERE T.ID_transaccion = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, t.getIdCategoria());
            ps.setBigDecimal(2, t.getValorTransaccion());
            ps.setString(3, t.getDescripcion());
            ps.setDate(4, java.sql.Date.valueOf(t.getFechaRealizacion()));
            ps.setLong(5, t.getIdTransaccion());
            ps.setLong(6, t.getIdUsuario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Editar Transaccion: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── ELIMINAR ──────────────────────────────────────────────────────────────
    // Verifica propiedad via JOIN con Usuario_Categoria
    public boolean eliminar(long idTransaccion, long idUsuario) {
        String sql = "DELETE T FROM Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE T.ID_transaccion = ? AND UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idTransaccion);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Eliminar Transaccion: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── TOTALES para resumen ──────────────────────────────────────────────────
    // Filtra por usuario via JOIN con Usuario_Categoria
    public java.math.BigDecimal[] obtenerTotales(long idUsuario) {
        String sql = "SELECT " +
                     "SUM(CASE WHEN C.Tipo_transaccion = 'Ingreso' THEN T.Valor_transaccion ELSE 0 END) AS total_ingresos, " +
                     "SUM(CASE WHEN C.Tipo_transaccion = 'Egreso'  THEN T.Valor_transaccion ELSE 0 END) AS total_egresos " +
                     "FROM Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE UC.ID_usuario = ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                java.math.BigDecimal ingresos = rs.getBigDecimal("total_ingresos");
                java.math.BigDecimal egresos  = rs.getBigDecimal("total_egresos");
                if (ingresos == null) ingresos = java.math.BigDecimal.ZERO;
                if (egresos  == null) egresos  = java.math.BigDecimal.ZERO;
                return new java.math.BigDecimal[]{ ingresos, egresos };
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTotales: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return new java.math.BigDecimal[]{ java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO };
    }
 
    // ── OBTENER ÚLTIMA POR TIPO ───────────────────────────────────────────────
    // Filtra por usuario via JOIN con Usuario_Categoria
    public Transaccion obtenerUltimaPorTipo(long idUsuario, String tipo) {
        String sql = "SELECT T.ID_transaccion, UC.ID_usuario, T.ID_categoria, " +
                     "C.Nombre_categoria, C.Tipo_transaccion, T.Valor_transaccion, " +
                     "T.Descripcion, T.Fecha_realizacion " +
                     "FROM Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria " +
                     "WHERE UC.ID_usuario = ? AND C.Tipo_transaccion = ? " +
                     "ORDER BY T.Fecha_realizacion DESC, T.ID_transaccion DESC " +
                     "LIMIT 1";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setString(2, tipo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Transaccion t = new Transaccion();
                t.setIdTransaccion(rs.getLong("ID_transaccion"));
                t.setIdUsuario(rs.getLong("ID_usuario"));
                t.setIdCategoria(rs.getInt("ID_categoria"));
                t.setNombreCategoria(rs.getString("Nombre_categoria"));
                t.setTipoTransaccion(rs.getString("Tipo_transaccion"));
                t.setValorTransaccion(rs.getBigDecimal("Valor_transaccion"));
                t.setDescripcion(rs.getString("Descripcion"));
                t.setFechaRealizacion(rs.getDate("Fecha_realizacion").toLocalDate());
                return t;
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerUltimaPorTipo: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return null;
    }
}