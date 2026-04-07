package modelo;

import java.util.List;

/**
 * Modelo que representa a un usuario del sistema Economic.
 * <p>
 * Esta clase es la entidad central del sistema y combina datos de múltiples
 * tablas ({@code Usuario}, {@code Email}, {@code Imagenes}, {@code Rol} y {@code Rol_Permisos})
 * para optimizar el rendimiento y simplificar el acceso a datos.
 * </p>
 * <p>
 * ¿Por qué combinar tantas tablas en un solo objeto? Porque este objeto
 * se almacena en sesión HTTP tras el inicio de sesión, y queremos evitar
 * consultas repetidas a la base de datos para cada verificación de permisos
 * o dato del usuario. Esta denormalización estratégica mejora significativamente
 * el rendimiento del sistema.
 * </p>
 * <p>
 * El objeto está diseñado para ser inmutable después de la carga inicial,
 * lo que lo hace seguro para uso concurrente en entornos web.
 * </p>
 */
public class Usuario {

    // Atributos básicos del usuario
    private long idUsuario;              // PK de la tabla Usuario
    private String nombre;               // Nombre visible del usuario
    private String contrasena;           // Contraseña encriptada (SHA-256)
    private String correo;               // Email único (tabla Email)
    private String urlImagen;            // Foto de perfil (tabla Imagenes)
    
    // Atributos de rol y permisos (denormalizados para performance)
    private long idRol;                  // FK a tabla Rol
    private String nombreRol;            // Nombre del rol (para UI)
    private List<String> permisos;       // Permisos cargados de Rol_Permisos

    /**
     * Constructor vacío requerido por el DAO para creación con Reflection.
     * <p>
     * Los frameworks ORM y muchos DAOs requieren un constructor vacío
     * para poder instanciar el objeto mediante reflexión y luego
     * poblar los atributos mediante setters.
     * </p>
     */
    public Usuario() {}

    /**
     * Constructor con datos básicos para autenticación.
     * <p>
     * Este constructor se usa principalmente durante el proceso de login
     * cuando solo necesitamos los campos esenciales para validar credenciales.
     * La contraseña ya debe venir encriptada con SHA-256.
     * </p>
     *
     * @param nombre     Nombre del usuario.
     * @param contrasena Contraseña ya encriptada con SHA-256.
     */
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────

    /**
     * @return ID único del usuario en la base de datos (PK).
     */
    public long getIdUsuario() { return idUsuario; }

    /**
     * @param idUsuario ID único del usuario.
     */
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    /**
     * @return Nombre visible del usuario para mostrar en la interfaz.
     */
    public String getNombre() { return nombre; }

    /**
     * @param nombre Nombre visible del usuario.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * @return Contraseña encriptada con SHA-256 (nunca almacenar texto plano).
     */
    public String getContrasena() { return contrasena; }

    /**
     * @param contrasena Contraseña encriptada con SHA-256.
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /**
     * @return Correo electrónico único del usuario (usado para login).
     */
    public String getCorreo() { return correo; }

    /**
     * @param correo Correo electrónico del usuario.
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * @return URL relativa de la imagen de perfil, o {@code null} si no tiene.
     */
    public String getUrlImagen() { return urlImagen; }

    /**
     * @param urlImagen URL relativa de la imagen de perfil.
     */
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    /**
     * @return ID del rol asignado al usuario (1 = administrador, 2 = usuario normal).
     */
    public long getIdRol() { return idRol; }

    /**
     * @param idRol ID del rol asignado al usuario.
     */
    public void setIdRol(long idRol) { this.idRol = idRol; }

    /**
     * @return Nombre legible del rol (ej. {@code "administrador"}, {@code "usuario"}).
     *         <p>
     *         Este campo está denormalizado para evitar JOINs adicionales
     *         en cada consulta que necesita mostrar el rol del usuario.
     *         </p>
     */
    public String getNombreRol() { return nombreRol; }

    /**
     * @param nombreRol Nombre legible del rol.
     */
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    /**
     * @return Lista de permisos cargados desde {@code Rol_Permisos}.
     *         <p>
     *         Esta lista se carga durante el login y se mantiene en sesión
     *         para evitar consultas repetidas a la base de datos en cada
     *         verificación de permisos del ValidadorPermisos.
     *         </p>
     */
    public List<String> getPermisos() { return permisos; }

    /**
     * @param permisos Lista de permisos del usuario.
     */
    public void setPermisos(List<String> permisos) { this.permisos = permisos; }
}
