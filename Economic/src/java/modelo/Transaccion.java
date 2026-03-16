package modelo;
 
import java.math.BigDecimal;
import java.time.LocalDate;
 
public class Transaccion {
 
    private long idTransaccion;
    private long idUsuario;
    private int idCategoria;
    private String nombreCategoria;
    private String tipoTransaccion;
    private BigDecimal valorTransaccion;
    private String descripcion;
    private LocalDate fechaRealizacion;
 
    public Transaccion() {}
 
    public long getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(long idTransaccion) { this.idTransaccion = idTransaccion; }
 
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
 
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
 
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
 
    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }
 
    public BigDecimal getValorTransaccion() { return valorTransaccion; }
    public void setValorTransaccion(BigDecimal valorTransaccion) { this.valorTransaccion = valorTransaccion; }
 
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
    public LocalDate getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDate fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
}