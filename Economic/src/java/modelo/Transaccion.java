package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo que representa una transacción financiera del usuario.
 * <p>
 * Mapea la tabla {@code Transacciones} de la base de datos. Cada transacción
 * pertenece a una categoría del usuario y puede estar asociada opcionalmente
 * a una meta de ahorro.
 * </p>
 */
public class Transaccion {

    private long idMeta;
    private long idTransaccion;
    private long idUsuario;
    private int idCategoria;
    private String nombreCategoria;
    private String tipoTransaccion;
    private BigDecimal valorTransaccion;
    private String descripcion;
    private LocalDate fechaRealizacion;

    /** Constructor vacío requerido por el DAO. */
    public Transaccion() {}

    /**
     * @return ID de la meta de ahorro asociada, o {@code 0} si no tiene.
     */
    public long getIdMeta() { return idMeta; }

    /**
     * @param idMeta ID de la meta de ahorro asociada ({@code 0} si no aplica).
     */
    public void setIdMeta(long idMeta) { this.idMeta = idMeta; }

    /**
     * @return ID único de la transacción.
     */
    public long getIdTransaccion() { return idTransaccion; }

    /**
     * @param idTransaccion ID único de la transacción.
     */
    public void setIdTransaccion(long idTransaccion) { this.idTransaccion = idTransaccion; }

    /**
     * @return ID del usuario propietario de la transacción.
     */
    public long getIdUsuario() { return idUsuario; }

    /**
     * @param idUsuario ID del usuario propietario.
     */
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    /**
     * @return ID de la categoría asociada a la transacción.
     */
    public int getIdCategoria() { return idCategoria; }

    /**
     * @param idCategoria ID de la categoría.
     */
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    /**
     * @return Nombre de la categoría (cargado por JOIN en el DAO).
     */
    public String getNombreCategoria() { return nombreCategoria; }

    /**
     * @param nombreCategoria Nombre de la categoría.
     */
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    /**
     * @return Tipo de transacción: {@code "Ingreso"} o {@code "Egreso"}.
     */
    public String getTipoTransaccion() { return tipoTransaccion; }

    /**
     * @param tipoTransaccion Tipo de transacción ({@code "Ingreso"} o {@code "Egreso"}).
     */
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    /**
     * @return Valor monetario de la transacción.
     */
    public BigDecimal getValorTransaccion() { return valorTransaccion; }

    /**
     * @param valorTransaccion Valor monetario de la transacción.
     */
    public void setValorTransaccion(BigDecimal valorTransaccion) { this.valorTransaccion = valorTransaccion; }

    /**
     * @return Descripción opcional de la transacción, puede ser {@code null}.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * @param descripcion Descripción opcional de la transacción.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * @return Fecha en que se realizó la transacción.
     */
    public LocalDate getFechaRealizacion() { return fechaRealizacion; }

    /**
     * @param fechaRealizacion Fecha de realización de la transacción.
     */
    public void setFechaRealizacion(LocalDate fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
}
