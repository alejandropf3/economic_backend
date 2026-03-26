package modelo;

import java.util.List;

/**
 * Modelo que representa a un usuario del sistema Economic.
 * <p>
 * Mapea los datos del usuario combinando las tablas {@code Usuario},
 * {@code Email}, {@code Imagenes} y {@code Rol} de la base de datos.
 * El objeto se almacena en sesión tras el inicio de sesión exitoso.
 * </p>
 */
public class Usuario {

    private long idUsuario;
    private String nombre;
    private String contrasena;
    private String correo;
    private String urlImagen;       // foto de perfil
    private long idRol;             // ID del rol asignado
    private String nombreRol;       // Nombre del rol (para mostrar en UI)
    private List<String> permisos;  // Lista de permisos del usuario

    /** Constructor vacío requerido por el DAO. */
    public Usuario() {}

    /**
     * Constructor con nombre y contraseña (ya encriptada).
     *
     * @param nombre     Nombre del usuario.
     * @param contrasena Contraseña encriptada con SHA-256.
     */
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    /**
     * @return ID único del usuario en la base de datos.
     */
    public long getIdUsuario() { return idUsuario; }

    /**
     * @param idUsuario ID único del usuario.
     */
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    /**
     * @return Nombre visible del usuario.
     */
    public String getNombre() { return nombre; }

    /**
     * @param nombre Nombre visible del usuario.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * @return Contraseña encriptada con SHA-256.
     */
    public String getContrasena() { return contrasena; }

    /**
     * @param contrasena Contraseña encriptada con SHA-256.
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /**
     * @return Correo electrónico del usuario.
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
     * @return ID del rol asignado al usuario (1 = administrador, 2 = usuario).
     */
    public long getIdRol() { return idRol; }

    /**
     * @param idRol ID del rol asignado al usuario.
     */
    public void setIdRol(long idRol) { this.idRol = idRol; }

    /**
     * @return Nombre legible del rol (ej. {@code "administrador"}, {@code "usuario"}).
     */
    public String getNombreRol() { return nombreRol; }

    /**
     * @param nombreRol Nombre legible del rol.
     */
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    /**
     * @return Lista de permisos cargados desde {@code Rol_Permisos}.
     */
    public List<String> getPermisos() { return permisos; }

    /**
     * @param permisos Lista de permisos del usuario.
     */
    public void setPermisos(List<String> permisos) { this.permisos = permisos; }
}
