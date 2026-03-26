package dao;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import modelo.ResumenDiario;
import modelo.ResumenSemanal;
import modelo.ResumenMensual;
import modelo.ResumenAnual; // Asegúrate de tener este modelo
import configuracion.ConexionDB;
 
public class ResumenDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
 
    // --- VISTA SEMANAL ---
    public ResumenSemanal calcularSemanal(long idUsuario, LocalDate fechaReferencia) {
        LocalDate lunes   = fechaReferencia.with(java.time.DayOfWeek.MONDAY);
        LocalDate domingo = lunes.plusDays(6);
 
        ResumenSemanal resumen = new ResumenSemanal();
        resumen.setFechaInicio(lunes);
        resumen.setFechaFin(domingo);
 
        List<ResumenDiario> diasResumen = new ArrayList<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalEgresos  = BigDecimal.ZERO;
 
        String sql = "SELECT Fecha_realizacion, Ingresos, Egresos, Balance " +
                     "FROM Vista_Detalle_Resumen " +
                     "WHERE ID_usuario = ? AND Fecha_realizacion BETWEEN ? AND ? " +
                     "ORDER BY Fecha_realizacion ASC";
 
        // ── Variables locales para evitar conflicto con calcularMensual/Anual ──
        Connection  localCon = null;
        PreparedStatement localPs = null;
        ResultSet   localRs  = null;
 
        try {
            localCon = cn.getConnection();
            localPs  = localCon.prepareStatement(sql);
            localPs.setLong(1, idUsuario);
            localPs.setDate(2, Date.valueOf(lunes));
            localPs.setDate(3, Date.valueOf(domingo));
            localRs  = localPs.executeQuery();
 
            while (localRs.next()) {
                ResumenDiario dia = new ResumenDiario();
                dia.setFecha(localRs.getDate("Fecha_realizacion").toLocalDate());
                BigDecimal ing = localRs.getBigDecimal("Ingresos");
                BigDecimal egr = localRs.getBigDecimal("Egresos");
 
                dia.setTotalIngresos(ing);
                dia.setTotalEgresos(egr);
                dia.setBalanceDia(localRs.getBigDecimal("Balance"));
 
                totalIngresos = totalIngresos.add(ing);
                totalEgresos  = totalEgresos.add(egr);
                diasResumen.add(dia);
            }
        } catch (SQLException e) {
            System.err.println("Error en calcularSemanal: " + e.getMessage());
        } finally {
            try {
                if (localRs  != null) localRs.close();
                if (localPs  != null) localPs.close();
                if (localCon != null) localCon.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
 
        resumen.setDiasResumen(diasResumen);
        resumen.setTotalIngresos(totalIngresos);
        resumen.setTotalEgresos(totalEgresos);
        resumen.setBalance(totalIngresos.subtract(totalEgresos));
        return resumen;
    }
 
    // --- VISTA MENSUAL ---
    public ResumenMensual calcularMensual(long idUsuario, int mes, int anio) {
        ResumenMensual mensual = new ResumenMensual();
        mensual.setMes(mes);
        mensual.setAnio(anio);
 
        String[] nombresMeses = {"","Enero","Febrero","Marzo","Abril","Mayo","Junio",
                                 "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        mensual.setNombreMes(nombresMeses[mes]);
 
        List<ResumenSemanal> semanas = new ArrayList<>();
        String sql = "SELECT DISTINCT Inicio_Semana FROM Vista_Detalle_Resumen " +
                     "WHERE ID_usuario = ? AND MONTH(Fecha_realizacion) = ? AND YEAR(Fecha_realizacion) = ? " +
                     "ORDER BY Inicio_Semana ASC";
 
        // ── Variables locales para no interferir con this.rs al llamar calcularSemanal ──
        Connection        localCon = null;
        PreparedStatement localPs  = null;
        ResultSet         localRs  = null;
 
        try {
            localCon = cn.getConnection();
            localPs  = localCon.prepareStatement(sql);
            localPs.setLong(1, idUsuario);
            localPs.setInt(2, mes);
            localPs.setInt(3, anio);
            localRs  = localPs.executeQuery();
 
            while (localRs.next()) {
                Date inicioDate = localRs.getDate("Inicio_Semana");
                if (inicioDate == null) continue;
                LocalDate inicio = inicioDate.toLocalDate();
                semanas.add(calcularSemanal(idUsuario, inicio));
            }
        } catch (SQLException e) {
            System.err.println("Error en calcularMensual: " + e.getMessage());
        } finally {
            try {
                if (localRs  != null) localRs.close();
                if (localPs  != null) localPs.close();
                if (localCon != null) localCon.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
 
        mensual.setSemanas(semanas);
 
        BigDecimal ingMes = semanas.stream()
                                   .map(ResumenSemanal::getTotalIngresos)
                                   .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal egrMes = semanas.stream()
                                   .map(ResumenSemanal::getTotalEgresos)
                                   .reduce(BigDecimal.ZERO, BigDecimal::add);
 
        mensual.setTotalIngresos(ingMes);
        mensual.setTotalEgresos(egrMes);
        mensual.setBalance(ingMes.subtract(egrMes));
 
        return mensual;
    }
 
    // --- VISTA ANUAL ---
    public ResumenAnual calcularAnual(long idUsuario, int anio) {
        ResumenAnual anual = new ResumenAnual();
        anual.setAnio(anio);
        
        List<ResumenMensual> meses = new ArrayList<>();
        // Generamos el resumen para cada uno de los 12 meses
        for (int i = 1; i <= 12; i++) {
            meses.add(calcularMensual(idUsuario, i, anio));
        }
        anual.setMeses(meses);
 
        // --- AJUSTE: Sumatoria de totales para el año ---
        BigDecimal ingAnual = meses.stream()
                                   .map(ResumenMensual::getTotalIngresos)
                                   .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal egrAnual = meses.stream()
                                   .map(ResumenMensual::getTotalEgresos)
                                   .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        anual.setTotalIngresos(ingAnual);
        anual.setTotalEgresos(egrAnual);
        anual.setBalance(ingAnual.subtract(egrAnual));
 
        return anual;
    }
 
    // --- SELECTOR DE AÑOS ---
    public List<Integer> obtenerAniosConTransacciones(long idUsuario) {
        List<Integer> listaAnios = new ArrayList<>();
        String sql = "SELECT DISTINCT Anio FROM Vista_Detalle_Resumen WHERE ID_usuario = ? ORDER BY Anio DESC";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                listaAnios.add(rs.getInt("Anio"));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerAniosConTransacciones: " + e.getMessage());
        } finally { cerrarConexiones(); }
        return listaAnios;
    }
 
    // ── ÚLTIMOS N RESÚMENES SEMANALES (para menú principal) ───────────────────
    public List<ResumenSemanal> obtenerUltimosSemanales(long idUsuario, int limite) {
        List<ResumenSemanal> resultado = new ArrayList<>();
        String sql = "SELECT DISTINCT Inicio_Semana " +
                     "FROM Vista_Detalle_Resumen " +
                     "WHERE ID_usuario = ? " +
                     "ORDER BY Inicio_Semana DESC " +
                     "LIMIT ?";
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setInt(2, limite);
            rs  = ps.executeQuery();
            while (rs.next()) {
                LocalDate inicioSemana = rs.getDate("Inicio_Semana").toLocalDate();
                resultado.add(calcularSemanal(idUsuario, inicioSemana));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerUltimosSemanales: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }
        return resultado;
    }
 
    private void cerrarConexiones() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}