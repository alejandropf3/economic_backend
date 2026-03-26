package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo que representa el balance neto de un día específico.
 * <p>
 * Contiene los totales de ingresos, egresos y el balance resultante
 * (ingresos - egresos) para una fecha dada. Es la unidad mínima de
 * agregación utilizada por {@link ResumenSemanal}.
 * </p>
 */
public class ResumenDiario {

    private LocalDate fecha;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balanceDia;

    /**
     * Constructor que inicializa todos los montos en cero.
     */
    public ResumenDiario() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balanceDia    = BigDecimal.ZERO;
    }

    /**
     * @return Fecha del resumen diario.
     */
    public LocalDate getFecha() { return fecha; }

    /**
     * @param fecha Fecha del resumen diario.
     */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /**
     * @return Total de ingresos del día.
     */
    public BigDecimal getTotalIngresos() { return totalIngresos; }

    /**
     * @param totalIngresos Total de ingresos del día.
     */
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    /**
     * @return Total de egresos del día.
     */
    public BigDecimal getTotalEgresos() { return totalEgresos; }

    /**
     * @param totalEgresos Total de egresos del día.
     */
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    /**
     * @return Balance neto del día (ingresos - egresos).
     */
    public BigDecimal getBalanceDia() { return balanceDia; }

    /**
     * @param balanceDia Balance neto del día.
     */
    public void setBalanceDia(BigDecimal balanceDia) { this.balanceDia = balanceDia; }
}
