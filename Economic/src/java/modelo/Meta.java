package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Modelo que representa una meta de ahorro del usuario.
 * <p>
 * Esta clase encapsula los objetivos financieros de los usuarios y proporciona
 * métodos de utilidad para calcular el progreso. Mapea la tabla {@code Metas_Ahorro}
 * de la base de datos y está diseñada para ser usada directamente en las vistas JSP.
 * </p>
 * <p>
 * ¿Por qué incluir lógica de negocio en el modelo? Porque esta lógica
 * (cálculo de porcentaje, validación de estados) es específica del dominio
 * y se usa frecuentemente en las vistas. Mantenerla aquí evita duplicar
 * código en los controladores y hace el modelo más rico semánticamente.
 * </p>
 * <p>
 * Los estados posibles siguen una máquina de estados simple: Activa → Completada/Cancelada.
 * Una vez completada o cancelada, la meta no puede modificarse.
 * </p>
 */
public class Meta {

    // Identificadores
    private long      idMeta;            // PK de la tabla Metas_Ahorro
    private long      idUsuario;         // FK a tabla Usuario
    
    // Datos básicos de la meta
    private String    nombreMeta;        // Nombre descriptivo de la meta
    private BigDecimal montoObjetivo;     // Meta final a alcanzar
    private BigDecimal montoActual;       // Progreso actual del ahorro
    private LocalDate  fechaCreacion;     // Fecha de creación
    private LocalDate  fechaLimite;       // Fecha objetivo para completar
    private String    estado;             // "Activa" | "Completada" | "Cancelada"

    /**
     * Constructor por defecto que inicializa el monto actual en cero.
     * <p>
     * Inicializar montoActual en ZERO es crucial para evitar NullPointerException
     * al calcular porcentajes de progreso en metas nuevas.
     * </p>
     */
    public Meta() {
        this.montoActual = BigDecimal.ZERO;
    }
 
    // ── Getters y Setters ─────────────────────────────────────────────────────

    public long getIdMeta() { return idMeta; }
    public void setIdMeta(long idMeta) { this.idMeta = idMeta; }

    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreMeta() { return nombreMeta; }
    public void setNombreMeta(String nombreMeta) { this.nombreMeta = nombreMeta; }

    public BigDecimal getMontoObjetivo() { return montoObjetivo; }
    public void setMontoObjetivo(BigDecimal montoObjetivo) { this.montoObjetivo = montoObjetivo; }

    public BigDecimal getMontoActual() { return montoActual; }
    public void setMontoActual(BigDecimal montoActual) { this.montoActual = montoActual; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // ── Métodos de utilidad (usados en JSP) ───────────────────────────────────

    /**
     * Calcula el porcentaje de progreso de la meta (0–100).
     * <p>
     * Este método es crucial para la UI ya que muestra visualmente el progreso
     * de ahorro al usuario. Usa BigDecimal para precisión financiera y
     * RoundingMode.DOWN para ser conservador (no mostrar progreso que no existe).
     * </p>
     * <p>
     * ¿Por qué Math.min(pct, 100)? Para evitar mostrar más del 100% por
     * errores de redondeo que podrían dar una falsa impresión de sobre-ahorro.
     * </p>
     *
     * @return Porcentaje de progreso como entero entre 0 y 100.
     */
    public int getPorcentajeProgreso() {
        // Validación de seguridad: evitar división por cero
        if (montoObjetivo == null || montoObjetivo.compareTo(BigDecimal.ZERO) == 0) return 0;
        
        // Cálculo: (montoActual / montoObjetivo) * 100
        BigDecimal progreso = montoActual
                .multiply(new BigDecimal("100"))
                .divide(montoObjetivo, 0, RoundingMode.DOWN);
        
        int pct = progreso.intValue();
        return Math.min(pct, 100); // Limitar a 100% máximo
    }

    /**
     * Retorna true si la meta está cancelada.
     * <p>
     * NOTA: Este método parece tener un error lógico - debería verificar
     * la fecha límite, no solo el estado. El nombre sugiere que debería
     * verificar si la fecha límite ha pasado, pero actualmente solo
     * verifica si está cancelada.
     * </p>
     *
     * @return true si la meta está cancelada.
     */
    public boolean isVencida() {
        return "Cancelada".equals(estado);
    }

    /**
     * Retorna true si la meta está activa (editable y aboneable).
     * <p>
     * Las metas activas pueden recibir nuevos abonos y sus datos
     * pueden ser modificados. Una vez completada o cancelada,
     * la meta se vuelve inmutable.
     * </p>
     *
     * @return true si la meta está en estado "Activa".
     */
    public boolean isActiva() {
        return "Activa".equals(estado);
    }

    /**
     * Retorna true si la meta fue completada exitosamente.
     * <p>
     * Una meta completada no puede recibir más abonos ni ser modificada.
     * El estado "Completada" se establece cuando el monto actual
     * alcanza o supera el monto objetivo.
     * </p>
     *
     * @return true si la meta está en estado "Completada".
     */
    public boolean isCompletada() {
        return "Completada".equals(estado);
    }
}