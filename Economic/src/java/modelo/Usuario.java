package modelo;
 
public class Usuario {
 
    private long idUsuario;
    private String nombre;
    private String contrasena;
    private String correo;
    private String correoRespaldo;
    private String urlImagen;       // foto de perfil
 
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
}