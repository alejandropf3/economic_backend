package utils;
 
import dao.CategoriaDao;
 
public class validarCategorias {
 
    public static String validarCrear(String nombre, String tipo, long idUsuario, CategoriaDao dao) {
 
        if (nombre == null || nombre.trim().isEmpty() ||
            tipo   == null || tipo.trim().isEmpty()) {
            return "vacio";
        }
        if (nombre.trim().length() < 4)  return "nombre_muy_corto";
        if (nombre.trim().length() > 30) return "nombre_muy_largo";
        if (!tipo.equals("Ingreso") && !tipo.equals("Egreso")) return "tipo_invalido";
 
        // Nombre duplicado solo dentro del mismo usuario
        if (dao.existeNombre(nombre.trim(), idUsuario)) return "nombre_duplicado";
 
        return "ok";
    }
 
    public static String validarEditar(String nombre, String tipo,
                                        int idCategoria, long idUsuario, CategoriaDao dao) {
 
        if (nombre == null || nombre.trim().isEmpty() ||
            tipo   == null || tipo.trim().isEmpty()) {
            return "vacio";
        }
        if (nombre.trim().length() < 4)  return "nombre_muy_corto";
        if (nombre.trim().length() > 30) return "nombre_muy_largo";
        if (!tipo.equals("Ingreso") && !tipo.equals("Egreso")) return "tipo_invalido";
 
        if (dao.existeNombreExcluyendoId(nombre.trim(), idCategoria, idUsuario)) {
            return "nombre_duplicado";
        }
 
        return "ok";
    }
}