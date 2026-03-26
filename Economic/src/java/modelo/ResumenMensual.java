package modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelo que representa un resumen mensual de transacciones.
 * <p>
 * Agrupa los resúmenes semanales ({@link ResumenSemanal}) del mes y calcula
 * los totales consolidados. El nombre del mes se asigna en español.
 * Es calculado por {@code ResumenDao.calcularMensual()}.
 * </p>
 */
public class ResumenMensual {

    private int mes;          // 1-12
    private int anio;
    private String nombreMes;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenSemanal> semanas;

    /**
     * Constructor que inicializa todos los montos en cero.
     */
    public ResumenMensual() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }

    /**
     * @return Número del mes (1 = enero, 12 = diciembre).
     */
    public int getMes() { return mes; }

    /**
     * @param mes Número del mes (1-12).
     */
    public void setMes(int mes) { this.mes = mes; }

    /**
     * @return Año del resumen mensual.
     */
    public int getAnio() { return anio; }

    /**
     * @param anio Año del resumen mensual.
     */
    public void setAnio(int anio) { this.anio = anio; }

    /**
     * @return Nombre del mes en español (ej. {@code "Marzo"}).
     */
    public String getNombreMes() { return nombreMes; }

    /**
     * @param nombreMes Nombre del mes en español.
     */
    public void setNombreMes(String nombreMes) { this.nombreMes = nombreMes; }

    /**
     * @return Total de ingresos del mes.
     */
    public BigDecimal getTotalIngresos() { return totalIngresos; }

    /**
     * @param totalIngresos Total de ingresos del mes.
     */
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    /**
     * @return Total de egresos del mes.
     */
    public BigDecimal getTotalEgresos() { return totalEgresos; }

    /**
     * @param totalEgresos Total de egresos del mes.
     */
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    /**
     * @return Balance neto del mes (ingresos - egresos).
     */
    public BigDecimal getBalance() { return balance; }

    /**
     * @param balance Balance neto del mes.
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    /**
     * @return Lista de resúmenes semanales que componen el mes.
     */
    public List<ResumenSemanal> getSemanas() { return semanas; }

    /**
     * @param semanas Lista de resúmenes semanales del mes.
     */
    public void setSemanas(List<ResumenSemanal> semanas) { this.semanas = semanas; }
}
