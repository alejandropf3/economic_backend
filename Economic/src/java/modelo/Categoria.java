package modelo;
 
/**
 * MODELO - Categoria.java
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Categoria {
 
    // --- Datos de tabla Categoria ---
    private int idCategoria;
    private String tipoTransaccion; // "Ingreso" o "Egreso"
    private String nombreCategoria;
 
    // --- Constructores ---
    /** Constructor vacío: el DAO lo usa para crear el objeto y llenarlo con setters */
    public Categoria() {}
 
    public Categoria(String tipoTransaccion, String nombreCategoria) {
        this.tipoTransaccion = tipoTransaccion;
        this.nombreCategoria = nombreCategoria;
    }
 
    // --- Getters y Setters ---
 
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
 
    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }
 
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}