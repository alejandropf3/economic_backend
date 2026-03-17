package modelo;
 
import java.math.BigDecimal;
import java.util.List;
 
/**
 * Representa un resumen mensual.
 * Se calcula sumando los totales de los resúmenes semanales del mes.
 */
public class ResumenMensual {
 
    private int mes;       // 1-12
    private int anio;
    private String nombreMes;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenSemanal> semanas;
 
    public ResumenMensual() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }
 
    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }
 
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
 
    public String getNombreMes() { return nombreMes; }
    public void setNombreMes(String nombreMes) { this.nombreMes = nombreMes; }
 
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
 
    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }
 
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
 
    public List<ResumenSemanal> getSemanas() { return semanas; }
    public void setSemanas(List<ResumenSemanal> semanas) { this.semanas = semanas; }
}