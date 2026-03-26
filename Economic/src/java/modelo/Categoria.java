package modelo;

/**
 * Modelo que representa una categoría de transacción del usuario.
 * <p>
 * Mapea la tabla {@code Categoria} junto con la relación {@code Usuario_Categoria}.
 * Cada categoría puede ser de tipo "Ingreso" o "Egreso" y pertenece a un usuario.
 * </p>
 */
public class Categoria {

    private int idCategoria;
    private String tipoTransaccion; // "Ingreso" o "Egreso"
    private String nombreCategoria;
    private long idUsuario;         // relación con Usuario

    /**
     * Constructor vacío requerido por el DAO para crear el objeto con setters.
     */
    public Categoria() {}

    /**
     * Constructor con los campos principales para crear una nueva categoría.
     *
     * @param tipoTransaccion Tipo de transacción: {@code "Ingreso"} o {@code "Egreso"}.
     * @param nombreCategoria Nombre de la categoría.
     * @param idUsuario       ID del usuario propietario.
     */
    public Categoria(String tipoTransaccion, String nombreCategoria, long idUsuario) {
        this.tipoTransaccion = tipoTransaccion;
        this.nombreCategoria = nombreCategoria;
        this.idUsuario = idUsuario;
    }

    /**
     * @return ID único de la categoría en la base de datos.
     */
    public int getIdCategoria() { return idCategoria; }

    /**
     * @param idCategoria ID único de la categoría.
     */
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    /**
     * @return Tipo de transacción asociado: {@code "Ingreso"} o {@code "Egreso"}.
     */
    public String getTipoTransaccion() { return tipoTransaccion; }

    /**
     * @param tipoTransaccion Tipo de transacción ({@code "Ingreso"} o {@code "Egreso"}).
     */
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    /**
     * @return Nombre de la categoría.
     */
    public String getNombreCategoria() { return nombreCategoria; }

    /**
     * @param nombreCategoria Nombre de la categoría.
     */
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    /**
     * @return ID del usuario propietario de la categoría.
     */
    public long getIdUsuario() { return idUsuario; }

    /**
     * @param idUsuario ID del usuario propietario.
     */
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
}
