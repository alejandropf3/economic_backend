package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Modelo que representa un resumen semanal de transacciones.
 * <p>
 * Contiene el rango de fechas de la semana (lunes a domingo), los totales
 * de ingresos y egresos, el balance neto y la lista de resúmenes diarios
 * que lo componen. Es calculado en tiempo real por {@code ResumenDao}.
 * </p>
 */
public class ResumenSemanal {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenDiario> diasResumen;

    /**
     * Constructor que inicializa todos los montos en cero.
     */
    public ResumenSemanal() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }

    /**
     * @return Fecha de inicio de la semana (lunes).
     */
    public LocalDate getFechaInicio() { return fechaInicio; }

    /**
     * @param fechaInicio Fecha de inicio de la semana (lunes).
     */
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    /**
     * @return Fecha de fin de la semana (domingo).
     */
    public LocalDate getFechaFin() { return fechaFin; }

    /**
     * @param fechaFin Fecha de fin de la semana (domingo).
     */
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    /**
     * @return Total de ingresos de la semana.
     */
    public BigDecimal getTotalIngresos() { return totalIngresos; }

    /**
     * @param totalIngresos Total de ingresos de la semana.
     */
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    /**
     * @return Total de egresos de la semana.
     */
    public BigDecimal getTotalEgresos() { return totalEgresos; }

    /**
     * @param totalEgresos Total de egresos de la semana.
     */
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    /**
     * @return Balance neto de la semana (ingresos - egresos).
     */
    public BigDecimal getBalance() { return balance; }

    /**
     * @param balance Balance neto de la semana.
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    /**
     * @return Lista de resúmenes diarios que componen la semana.
     */
    public List<ResumenDiario> getDiasResumen() { return diasResumen; }

    /**
     * @param diasResumen Lista de resúmenes diarios de la semana.
     */
    public void setDiasResumen(List<ResumenDiario> diasResumen) { this.diasResumen = diasResumen; }
}
