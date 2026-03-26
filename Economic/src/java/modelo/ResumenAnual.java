package modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelo que representa un resumen anual de transacciones.
 * <p>
 * Agrupa los 12 resúmenes mensuales ({@link ResumenMensual}) del año y calcula
 * los totales consolidados anuales. Es calculado por {@code ResumenDao.calcularAnual()}.
 * </p>
 */
public class ResumenAnual {

    private int anio;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<ResumenMensual> meses;

    /**
     * Constructor que inicializa todos los montos en cero.
     */
    public ResumenAnual() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalEgresos  = BigDecimal.ZERO;
        this.balance       = BigDecimal.ZERO;
    }

    /**
     * @return Año del resumen anual.
     */
    public int getAnio() { return anio; }

    /**
     * @param anio Año del resumen anual.
     */
    public void setAnio(int anio) { this.anio = anio; }

    /**
     * @return Total de ingresos del año.
     */
    public BigDecimal getTotalIngresos() { return totalIngresos; }

    /**
     * @param totalIngresos Total de ingresos del año.
     */
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    /**
     * @return Total de egresos del año.
     */
    public BigDecimal getTotalEgresos() { return totalEgresos; }

    /**
     * @param totalEgresos Total de egresos del año.
     */
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    /**
     * @return Balance neto del año (ingresos - egresos).
     */
    public BigDecimal getBalance() { return balance; }

    /**
     * @param balance Balance neto del año.
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    /**
     * @return Lista de los 12 resúmenes mensuales del año.
     */
    public List<ResumenMensual> getMeses() { return meses; }

    /**
     * @param meses Lista de resúmenes mensuales del año.
     */
    public void setMeses(List<ResumenMensual> meses) { this.meses = meses; }
}
