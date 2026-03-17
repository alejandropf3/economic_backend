package modelo;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
 
/**
 * Representa un resumen semanal calculado en tiempo real.
 * Contiene las transacciones del período y los totales.
 */
public class ResumenSemanal {
 
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenDiario> diasResumen;
 
    public ResumenSemanal() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }
 
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
 
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
 
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
 
    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }
 
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
 
    public List<ResumenDiario> getDiasResumen() { return diasResumen; }
    public void setDiasResumen(List<ResumenDiario> diasResumen) { this.diasResumen = diasResumen; }
}