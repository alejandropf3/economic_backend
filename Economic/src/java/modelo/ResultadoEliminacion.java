package modelo;

public class ResultadoEliminacion {
    private boolean exito;
    private int transaccionesEliminadas;
    private int categoriasEliminadas;
    private String mensajeError;
    
    public ResultadoEliminacion() {}
    
    public ResultadoEliminacion(boolean exito, int transaccionesEliminadas, int categoriasEliminadas) {
        this.exito = exito;
        this.transaccionesEliminadas = transaccionesEliminadas;
        this.categoriasEliminadas = categoriasEliminadas;
    }
    
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    
    public int getTransaccionesEliminadas() { return transaccionesEliminadas; }
    public void setTransaccionesEliminadas(int transaccionesEliminadas) { this.transaccionesEliminadas = transaccionesEliminadas; }
    
    public int getCategoriasEliminadas() { return categoriasEliminadas; }
    public void setCategoriasEliminadas(int categoriasEliminadas) { this.categoriasEliminadas = categoriasEliminadas; }
    
    public String getMensajeError() { return mensajeError; }
    public void setMensajeError(String mensajeError) { this.mensajeError = mensajeError; }
}
