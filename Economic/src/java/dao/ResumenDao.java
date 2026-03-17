package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import configuracion.ConexionDB;
import modelo.ResumenAnual;
import modelo.ResumenDiario;
import modelo.ResumenMensual;
import modelo.ResumenSemanal;
 
public class ResumenDao {
 
    ConexionDB cn = new ConexionDB();
    Connection con;
    PreparedStatement ps;
 
    private static final String[] NOMBRES_MESES = {
        "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
 
    // ─────────────────────────────────────────────────────────────────────────
    // RESUMEN SEMANAL — calcula para una semana dada una fecha
    // La semana va de lunes a domingo
    // ─────────────────────────────────────────────────────────────────────────
    public ResumenSemanal calcularSemanal(long idUsuario, LocalDate fechaReferencia) {
 
        // Calcular lunes y domingo de la semana que contiene fechaReferencia
        LocalDate lunes   = fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = lunes.plusDays(6);
 
        String sql = "SELECT DATE(Fecha_realizacion) AS dia, " +
                     "SUM(CASE WHEN C.Tipo_transaccion = 'Ingreso' THEN T.Valor_transaccion ELSE 0 END) AS ingresos, " +
                     "SUM(CASE WHEN C.Tipo_transaccion = 'Egreso'  THEN T.Valor_transaccion ELSE 0 END) AS egresos " +
                     "FROM Transacciones T " +
                     "JOIN Categoria C ON T.ID_categoria = C.ID_categoria " +
                     "WHERE T.ID_usuario = ? " +
                     "AND T.Fecha_realizacion BETWEEN ? AND ? " +
                     "GROUP BY DATE(Fecha_realizacion) " +
                     "ORDER BY DATE(Fecha_realizacion)";
 
        ResumenSemanal resumen = new ResumenSemanal();
        resumen.setFechaInicio(lunes);
        resumen.setFechaFin(domingo);
 
        List<ResumenDiario> dias = new ArrayList<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalEgresos  = BigDecimal.ZERO;
 
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ps.setDate(2, java.sql.Date.valueOf(lunes));
            ps.setDate(3, java.sql.Date.valueOf(domingo));
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                ResumenDiario dia = new ResumenDiario();
                dia.setFecha(rs.getDate("dia").toLocalDate());
 
                BigDecimal ing = rs.getBigDecimal("ingresos");
                BigDecimal egr = rs.getBigDecimal("egresos");
                if (ing == null) ing = BigDecimal.ZERO;
                if (egr == null) egr = BigDecimal.ZERO;
 
                dia.setTotalIngresos(ing);
                dia.setTotalEgresos(egr);
                dia.setBalanceDia(ing.subtract(egr));
 
                totalIngresos = totalIngresos.add(ing);
                totalEgresos  = totalEgresos.add(egr);
                dias.add(dia);
            }
        } catch (SQLException e) {
            System.err.println("Error en calcularSemanal: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
 
        resumen.setDiasResumen(dias);
        resumen.setTotalIngresos(totalIngresos);
        resumen.setTotalEgresos(totalEgresos);
        resumen.setBalance(totalIngresos.subtract(totalEgresos));
        return resumen;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // RESUMEN MENSUAL — divide el mes en semanas y calcula cada una
    // ─────────────────────────────────────────────────────────────────────────
    public ResumenMensual calcularMensual(long idUsuario, int mes, int anio) {
 
        ResumenMensual resumenMensual = new ResumenMensual();
        resumenMensual.setMes(mes);
        resumenMensual.setAnio(anio);
        resumenMensual.setNombreMes(NOMBRES_MESES[mes]);
 
        List<ResumenSemanal> semanas = new ArrayList<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalEgresos  = BigDecimal.ZERO;
 
        // Primer y último día del mes
        LocalDate primerDia = LocalDate.of(anio, mes, 1);
        LocalDate ultimoDia = primerDia.with(TemporalAdjusters.lastDayOfMonth());
 
        // Recorrer semana a semana dentro del mes
        LocalDate cursor = primerDia;
        while (!cursor.isAfter(ultimoDia)) {
            LocalDate lunes   = cursor.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate domingo = lunes.plusDays(6);
 
            // Limitar al mes actual para no solapar semanas de otros meses
            LocalDate inicioPeriodo = lunes.isBefore(primerDia) ? primerDia : lunes;
            LocalDate finPeriodo    = domingo.isAfter(ultimoDia) ? ultimoDia : domingo;
 
            ResumenSemanal semana = calcularSemanal(idUsuario, cursor);
            // Ajustar fechas al rango real del mes
            semana.setFechaInicio(inicioPeriodo);
            semana.setFechaFin(finPeriodo);
 
            semanas.add(semana);
            totalIngresos = totalIngresos.add(semana.getTotalIngresos());
            totalEgresos  = totalEgresos.add(semana.getTotalEgresos());
 
            // Avanzar al siguiente lunes
            cursor = domingo.plusDays(1);
        }
 
        resumenMensual.setSemanas(semanas);
        resumenMensual.setTotalIngresos(totalIngresos);
        resumenMensual.setTotalEgresos(totalEgresos);
        resumenMensual.setBalance(totalIngresos.subtract(totalEgresos));
        return resumenMensual;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // RESUMEN ANUAL — calcula los 12 meses del año
    // Solo incluye meses que tienen transacciones
    // ─────────────────────────────────────────────────────────────────────────
    public ResumenAnual calcularAnual(long idUsuario, int anio) {
 
        ResumenAnual resumenAnual = new ResumenAnual();
        resumenAnual.setAnio(anio);
 
        List<ResumenMensual> meses = new ArrayList<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalEgresos  = BigDecimal.ZERO;
 
        // Solo calcular hasta el mes actual si es el año en curso
        int mesLimite = (anio == LocalDate.now().getYear())
                        ? LocalDate.now().getMonthValue()
                        : 12;
 
        for (int m = 1; m <= mesLimite; m++) {
            ResumenMensual mensual = calcularMensual(idUsuario, m, anio);
            meses.add(mensual);
            totalIngresos = totalIngresos.add(mensual.getTotalIngresos());
            totalEgresos  = totalEgresos.add(mensual.getTotalEgresos());
        }
 
        resumenAnual.setMeses(meses);
        resumenAnual.setTotalIngresos(totalIngresos);
        resumenAnual.setTotalEgresos(totalEgresos);
        resumenAnual.setBalance(totalIngresos.subtract(totalEgresos));
        return resumenAnual;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // OBTENER AÑOS con transacciones del usuario
    // ─────────────────────────────────────────────────────────────────────────
    public List<Integer> obtenerAniosConTransacciones(long idUsuario) {
        String sql = "SELECT DISTINCT YEAR(Fecha_realizacion) AS anio " +
                     "FROM Transacciones " +
                     "WHERE ID_usuario = ? " +
                     "ORDER BY anio DESC";
        List<Integer> anios = new ArrayList<>();
        try {
            con = cn.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) anios.add(rs.getInt("anio"));
        } catch (SQLException e) {
            System.err.println("Error en obtenerAniosConTransacciones: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
        return anios;
    }
}