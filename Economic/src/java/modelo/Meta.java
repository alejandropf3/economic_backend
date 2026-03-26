package modelo;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
 
/**
 * Modelo — Meta.java
 * Representa una meta de ahorro del usuario.
 * Mapea la tabla Metas_Ahorro de la base de datos.
 */
public class Meta {
 
    private long      idMeta;
    private long      idUsuario;
    private String    nombreMeta;
    private BigDecimal montoObjetivo;
    private BigDecimal montoActual;
    private LocalDate  fechaCreacion;
    private LocalDate  fechaLimite;
    private String    estado;   // "Activa" | "Completada" | "Cancelada"
 
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
     */
    public int getPorcentajeProgreso() {
        if (montoObjetivo == null || montoObjetivo.compareTo(BigDecimal.ZERO) == 0) return 0;
        BigDecimal progreso = montoActual
                .multiply(new BigDecimal("100"))
                .divide(montoObjetivo, 0, RoundingMode.DOWN);
        int pct = progreso.intValue();
        return Math.min(pct, 100);
    }
 
    /**
     * Retorna true si la meta está vencida (fecha límite pasó y no está completada).
     */
    public boolean isVencida() {
        return "Cancelada".equals(estado);
    }
 
    /**
     * Retorna true si la meta está activa (editable y aboneable).
     */
    public boolean isActiva() {
        return "Activa".equals(estado);
    }
 
    /**
     * Retorna true si la meta fue completada.
     */
    public boolean isCompletada() {
        return "Completada".equals(estado);
    }
}