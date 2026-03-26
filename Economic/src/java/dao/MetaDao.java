package dao;
 
import configuracion.ConexionDB;
import modelo.Meta;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
 
/**
 * DAO — MetaDao.java
 * Operaciones de base de datos para la tabla Metas_Ahorro.
 */
public class MetaDao {
 
    ConexionDB cn = new ConexionDB();
 
    // ── CREAR META ────────────────────────────────────────────────────────────
    public boolean crear(Meta meta) {
        String sql = "INSERT INTO Metas_Ahorro "
                   + "(ID_usuario, Nombre_meta, Monto_objetivo, Monto_actual, "
                   + " Fecha_creacion, Fecha_limite, Estado) "
                   + "VALUES (?, ?, ?, 0.00, ?, ?, 'Activa')";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, meta.getIdUsuario());
            ps.setString(2, meta.getNombreMeta().trim());
            ps.setBigDecimal(3, meta.getMontoObjetivo());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setDate(5, Date.valueOf(meta.getFechaLimite()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.crear: " + e.getMessage());
            return false;
        }
    }
 
    // ── LISTAR POR USUARIO ────────────────────────────────────────────────────
    // Actualiza automáticamente las metas vencidas antes de listar
    public List<Meta> listarPorUsuario(long idUsuario) {
        actualizarVencidas(idUsuario);
 
        String sql = "SELECT * FROM Metas_Ahorro "
                   + "WHERE ID_usuario = ? "
                   + "ORDER BY "
                   + "  CASE Estado WHEN 'Activa' THEN 1 WHEN 'Completada' THEN 2 ELSE 3 END, "
                   + "  Fecha_limite ASC";
        List<Meta> lista = new ArrayList<>();
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.listarPorUsuario: " + e.getMessage());
        }
        return lista;
    }
 
    // ── EDITAR META (solo nombre y fecha límite) ──────────────────────────────
    public boolean editar(Meta meta) {
        String sql = "UPDATE Metas_Ahorro "
                   + "SET Nombre_meta = ?, Fecha_limite = ? "
                   + "WHERE ID_meta = ? AND ID_usuario = ? AND Estado = 'Activa'";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, meta.getNombreMeta().trim());
            ps.setDate(2, Date.valueOf(meta.getFechaLimite()));
            ps.setLong(3, meta.getIdMeta());
            ps.setLong(4, meta.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.editar: " + e.getMessage());
            return false;
        }
    }
 
    // ── ELIMINAR META ─────────────────────────────────────────────────────────
    public boolean eliminar(long idMeta, long idUsuario) {
        String sql = "DELETE FROM Metas_Ahorro "
                   + "WHERE ID_meta = ? AND ID_usuario = ?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idMeta);
            ps.setLong(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.eliminar: " + e.getMessage());
            return false;
        }
    }
 
    // ── ABONAR MONTO A META ───────────────────────────────────────────────────
    // Suma el monto al monto_actual. Si alcanza el objetivo, cambia a "Completada".
    public boolean abonar(long idMeta, long idUsuario, BigDecimal monto) {
        String sql = "UPDATE Metas_Ahorro "
                   + "SET Monto_actual = Monto_actual + ?, "
                   + "    Estado = CASE "
                   + "               WHEN (Monto_actual + ?) >= Monto_objetivo THEN 'Completada' "
                   + "               ELSE Estado "
                   + "             END "
                   + "WHERE ID_meta = ? AND ID_usuario = ? AND Estado = 'Activa'";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, monto);
            ps.setBigDecimal(2, monto);
            ps.setLong(3, idMeta);
            ps.setLong(4, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.abonar: " + e.getMessage());
            return false;
        }
    }
 
    // ── VERIFICAR DUPLICADO (crear) ───────────────────────────────────────────
    // RF13 métrica 3: prohibido duplicar nombre + fecha_límite para el mismo usuario
    public boolean existeDuplicado(String nombre, LocalDate fechaLimite, long idUsuario) {
        String sql = "SELECT COUNT(*) FROM Metas_Ahorro "
                   + "WHERE ID_usuario = ? "
                   + "AND LOWER(TRIM(Nombre_meta)) = LOWER(TRIM(?)) "
                   + "AND Fecha_limite = ?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.setString(2, nombre);
            ps.setDate(3, Date.valueOf(fechaLimite));
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.existeDuplicado: " + e.getMessage());
            return false;
        }
    }
 
    // ── VERIFICAR DUPLICADO (edición, excluye la propia meta) ────────────────
    public boolean existeDuplicadoEdicion(String nombre, LocalDate fechaLimite,
                                           long idUsuario, long idMetaActual) {
        String sql = "SELECT COUNT(*) FROM Metas_Ahorro "
                   + "WHERE ID_usuario = ? "
                   + "AND LOWER(TRIM(Nombre_meta)) = LOWER(TRIM(?)) "
                   + "AND Fecha_limite = ? "
                   + "AND ID_meta != ?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.setString(2, nombre);
            ps.setDate(3, Date.valueOf(fechaLimite));
            ps.setLong(4, idMetaActual);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.existeDuplicadoEdicion: " + e.getMessage());
            return false;
        }
    }
 
    // ── OBTENER MONTO ACTUAL (para validar eliminación con fondos) ────────────
    public BigDecimal obtenerMontoActual(long idMeta, long idUsuario) {
        String sql = "SELECT Monto_actual FROM Metas_Ahorro "
                   + "WHERE ID_meta = ? AND ID_usuario = ?";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idMeta);
            ps.setLong(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal("Monto_actual");
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.obtenerMontoActual: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
 
    // ── ACTUALIZAR METAS VENCIDAS ─────────────────────────────────────────────
    // RF14 restricción 2: si fecha_límite pasó y monto_actual < monto_objetivo → "Cancelada"
    private void actualizarVencidas(long idUsuario) {
        String sql = "UPDATE Metas_Ahorro "
                   + "SET Estado = 'Cancelada' "
                   + "WHERE ID_usuario = ? "
                   + "AND Estado = 'Activa' "
                   + "AND Fecha_limite < CURDATE() "
                   + "AND Monto_actual < Monto_objetivo";
        try (Connection con = cn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en MetaDao.actualizarVencidas: " + e.getMessage());
        }
    }
 
    // ── MAPPER ────────────────────────────────────────────────────────────────
    private Meta mapear(ResultSet rs) throws SQLException {
        Meta m = new Meta();
        m.setIdMeta(rs.getLong("ID_meta"));
        m.setIdUsuario(rs.getLong("ID_usuario"));
        m.setNombreMeta(rs.getString("Nombre_meta"));
        m.setMontoObjetivo(rs.getBigDecimal("Monto_objetivo"));
        m.setMontoActual(rs.getBigDecimal("Monto_actual"));
        m.setFechaCreacion(rs.getDate("Fecha_creacion").toLocalDate());
        m.setFechaLimite(rs.getDate("Fecha_limite").toLocalDate());
        m.setEstado(rs.getString("Estado"));
        return m;
    }
}