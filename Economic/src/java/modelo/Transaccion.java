package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo que representa una transacción financiera del usuario.
 * <p>
 * Esta clase es el corazón funcional del sistema Economic, ya que las
 * transacciones son la razón de ser de la aplicación. Mapea la tabla
 * {@code Transacciones} de la base de datos e incluye datos denormalizados
 * de categorías para optimizar el rendimiento en consultas frecuentes.
 * </p>
 * <p>
 * ¿Por qué usar BigDecimal para valores monetarios? Porque los tipos de
 * punto flotante (float, double) tienen problemas de precisión con
 * cálculos financieros que pueden resultar en pérdidas de centavos.
 * BigDecimal garantiza precisión exacta en operaciones monetarias.
 * </p>
 * <p>
 * ¿Por qué LocalDate en lugar de Date? Porque LocalDate es la API moderna
 * de Java para fechas sin zona horaria, evitando los problemas clásicos
 * de Date que es mutable y tiene complicaciones con zonas horarias.
 * </p>
 */
public class Transaccion {

    // Relaciones con otras entidades
    private long idMeta;               // FK opcional a Metas_Ahorro
    private long idTransaccion;        // PK de la tabla Transacciones
    private long idUsuario;            // FK a tabla Usuario
    private int idCategoria;           // FK a tabla Categoria
    
    // Datos denormalizados para optimizar consultas
    private String nombreCategoria;     // Nombre de categoría (evita JOIN)
    
    // Datos básicos de la transacción
    private String tipoTransaccion;    // "Ingreso" o "Egreso"
    private BigDecimal valorTransaccion; // Valor monetario exacto
    private String descripcion;        // Descripción opcional
    private LocalDate fechaRealizacion; // Fecha de la transacción

    /**
     * Constructor vacío requerido por el DAO para instanciación con Reflection.
     * <p>
     * Los DAOs típicamente crean el objeto vacío y luego usan los setters
     * para poblar los valores desde el ResultSet de la consulta SQL.
     * </p>
     */
    public Transaccion() {}

    // ── Getters y Setters ─────────────────────────────────────────────────────

    /**
     * @return ID de la meta de ahorro asociada, o {@code 0} si no tiene.
     *         <p>
     *         Esta relación es opcional. Una transacción puede estar
     *         asociada a una meta de ahorro para seguimiento de progreso.
     *         </p>
     */
    public long getIdMeta() { return idMeta; }

    /**
     * @param idMeta ID de la meta de ahorro asociada ({@code 0} si no aplica).
     */
    public void setIdMeta(long idMeta) { this.idMeta = idMeta; }

    /**
     * @return ID único de la transacción en la base de datos (PK).
     */
    public long getIdTransaccion() { return idTransaccion; }

    /**
     * @param idTransaccion ID único de la transacción.
     */
    public void setIdTransaccion(long idTransaccion) { this.idTransaccion = idTransaccion; }

    /**
     * @return ID del usuario propietario de la transacción.
     *         <p>
     *         Este campo es crucial para el aislamiento de datos entre usuarios.
     *         Todas las consultas deben filtrar por este campo.
     *         </p>
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
     *         <p>
     *         Este campo está denormalizado para optimizar las consultas
     *         que muestran listados de transacciones, evitando JOINs adicionales.
     *         </p>
     */
    public String getNombreCategoria() { return nombreCategoria; }

    /**
     * @param nombreCategoria Nombre de la categoría.
     */
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    /**
     * @return Tipo de transacción: {@code "Ingreso"} o {@code "Egreso"}.
     *         <p>
     *         Este campo determina cómo afecta la transacción al balance
     *         del usuario. Los ingresos suman, los egresos restan.
     *         </p>
     */
    public String getTipoTransaccion() { return tipoTransaccion; }

    /**
     * @param tipoTransaccion Tipo de transacción ({@code "Ingreso"} o {@code "Egreso"}).
     */
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    /**
     * @return Valor monetario exacto de la transacción.
     *         <p>
     *         Usar BigDecimal garantiza precisión financiera sin errores
     *         de redondeo que podrían ocurrir con double o float.
     *         </p>
     */
    public BigDecimal getValorTransaccion() { return valorTransaccion; }

    /**
     * @param valorTransaccion Valor monetario exacto de la transacción.
     */
    public void setValorTransaccion(BigDecimal valorTransaccion) { this.valorTransaccion = valorTransaccion; }

    /**
     * @return Descripción opcional de la transacción, puede ser {@code null}.
     *         <p>
     *         La descripción ayuda al usuario a recordar el contexto
     *         de la transacción, especialmente para gastos importantes.
     *         </p>
     */
    public String getDescripcion() { return descripcion; }

    /**
     * @param descripcion Descripción opcional de la transacción.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * @return Fecha en que se realizó la transacción.
     *         <p>
     *         LocalDate es inmutable y no tiene problemas de zona horaria,
     *         haciendo ideal para registrar fechas financieras.
     *         </p>
     */
    public LocalDate getFechaRealizacion() { return fechaRealizacion; }

    /**
     * @param fechaRealizacion Fecha de realización de la transacción.
     */
    public void setFechaRealizacion(LocalDate fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
}
