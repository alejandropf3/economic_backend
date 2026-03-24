package modelo;
 
import java.util.List;
 
public class Usuario {
 
    private long idUsuario;
    private String nombre;
    private String contrasena;
    private String correo;
    private String correoRespaldo;
    private String urlImagen;       // foto de perfil
    private long idRol;            // ID del rol asignado
    private String nombreRol;       // Nombre del rol (para mostrar en UI)
    private List<String> permisos;  // Lista de permisos del usuario
 
    public Usuario() {}
 
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
 
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
 
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
 
    public String getCorreoRespaldo() { return correoRespaldo; }
    public void setCorreoRespaldo(String correoRespaldo) { this.correoRespaldo = correoRespaldo; }
 
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public long getIdRol() { return idRol; }
    public void setIdRol(long idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public List<String> getPermisos() { return permisos; }
    public void setPermisos(List<String> permisos) { this.permisos = permisos; }
}