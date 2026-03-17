package modelo;
 
import java.math.BigDecimal;
import java.util.List;
 
/**
 * Representa un resumen anual.
 * Se calcula sumando los totales de los resúmenes mensuales del año.
 */
public class ResumenAnual {
 
    private int anio;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenMensual> meses;
 
    public ResumenAnual() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }
 
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
 
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
 
    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }
 
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
 
    public List<ResumenMensual> getMeses() { return meses; }
    public void setMeses(List<ResumenMensual> meses) { this.meses = meses; }
}