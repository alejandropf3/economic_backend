package modelo;

/**
 * Modelo que encapsula el resultado de una operación de eliminación de usuario.
 * <p>
 * Contiene información sobre el éxito de la operación, la cantidad de registros
 * eliminados en cascada y un mensaje de error en caso de fallo.
 * Usado por {@code AdminDao.eliminarUsuario()}.
 * </p>
 */
public class ResultadoEliminacion {

    private boolean exito;
    private int transaccionesEliminadas;
    private int categoriasEliminadas;
    private String mensajeError;

    /** Constructor vacío. */
    public ResultadoEliminacion() {}

    /**
     * Constructor con los campos principales del resultado.
     *
     * @param exito                  {@code true} si la eliminación fue exitosa.
     * @param transaccionesEliminadas Número de transacciones eliminadas en cascada.
     * @param categoriasEliminadas    Número de categorías eliminadas en cascada.
     */
    public ResultadoEliminacion(boolean exito, int transaccionesEliminadas, int categoriasEliminadas) {
        this.exito = exito;
        this.transaccionesEliminadas = transaccionesEliminadas;
        this.categoriasEliminadas = categoriasEliminadas;
    }

    /**
     * @return {@code true} si la eliminación fue exitosa.
     */
    public boolean isExito() { return exito; }

    /**
     * @param exito {@code true} si la eliminación fue exitosa.
     */
    public void setExito(boolean exito) { this.exito = exito; }

    /**
     * @return Número de transacciones eliminadas en cascada.
     */
    public int getTransaccionesEliminadas() { return transaccionesEliminadas; }

    /**
     * @param transaccionesEliminadas Número de transacciones eliminadas.
     */
    public void setTransaccionesEliminadas(int transaccionesEliminadas) { this.transaccionesEliminadas = transaccionesEliminadas; }

    /**
     * @return Número de categorías eliminadas en cascada.
     */
    public int getCategoriasEliminadas() { return categoriasEliminadas; }

    /**
     * @param categoriasEliminadas Número de categorías eliminadas.
     */
    public void setCategoriasEliminadas(int categoriasEliminadas) { this.categoriasEliminadas = categoriasEliminadas; }

    /**
     * @return Mensaje de error descriptivo en caso de fallo, o {@code null} si fue exitoso.
     */
    public String getMensajeError() { return mensajeError; }

    /**
     * @param mensajeError Mensaje de error descriptivo.
     */
    public void setMensajeError(String mensajeError) { this.mensajeError = mensajeError; }
}
