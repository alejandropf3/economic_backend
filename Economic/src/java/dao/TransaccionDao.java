package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import configuracion.ConexionDB;
import modelo.Transaccion;
 
public class TransaccionDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    // ── CREAR ─────────────────────────────────────────────────────────────────
    // ID_usuario ya no existe en Transacciones — se omite del INSERT
    public boolean crear(Transaccion t) {
        String sql = "INSERT INTO Transacciones " +
                     "(ID_categoria, ID_meta, Valor_transaccion, Descripcion, Fecha_realizacion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, t.getIdCategoria());
            ps.setObject(2, t.getIdMeta() != 0 ? t.getIdMeta() : null);
            ps.setBigDecimal(3, t.getValorTransaccion());
            ps.setString(4, t.getDescripcion());
            ps.setDate(5, java.sql.Date.valueOf(t.getFechaRealizacion()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en DAO Crear Transaccion: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
 
    // ── LISTAR CON FILTROS ────────────────────────────────────────────────────
    // Filtra por usuario via JOIN con Usuario_Categoria
    public List<Transaccion> listarPorUsuario(long idUsuario, String tipo,
                                               String idCategoria,
                                               String fechaDesde, String fechaHasta) {
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