/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * MODELO - Usuario.java
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Usuario {
    
    //--- Datos de tabla Usuario ----
    private long idUsuario;
    private String nombre;
    private String contrasena;
    private String correo;
    
    // --- Constructores ---
    /** Constructor vacío: el DAO lo usa para crear el objeto y llenarlo con setters */
    public Usuario() {}
    
    public Usuario (String nombre, String contrasena){
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
    
    // --- Getters y Setters ---
    
    public long getIdUsuario() { return idUsuario;}
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    
    public String getNombre() { return nombre; }
    public void setNombre (String nombre) { this.nombre = nombre; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena( String contrasena ){ this.contrasena = contrasena; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
