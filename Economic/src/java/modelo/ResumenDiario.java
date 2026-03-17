package modelo;
 
import java.math.BigDecimal;
import java.time.LocalDate;
 
/**
 * Representa el balance neto de un día específico.
 * Ingresos - Egresos del día.
 */
public class ResumenDiario {
 
    private LocalDate fecha;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balanceDia;
 
    public ResumenDiario() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balanceDia    = BigDecimal.ZERO;
    }
 
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
 
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
 
    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }
 
    public BigDecimal getBalanceDia() { return balanceDia; }
    public void setBalanceDia(BigDecimal balanceDia) { this.balanceDia = balanceDia; }
}