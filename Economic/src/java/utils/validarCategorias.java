package utils;
 
import dao.CategoriaDao;
 
public class validarCategorias {
 
    /**
     * Validación para CREAR una categoría nueva.
     */
    public static String validarCrear(String nombre, String tipo, CategoriaDao dao) {
 
        // 1. Campos vacíos
        if (nombre == null || nombre.trim().isEmpty() ||
            tipo   == null || tipo.trim().isEmpty()) {
            return "vacio";
        }
 
        // 2. Longitud mínima (8 caracteres)
        if (nombre.trim().length() < 4) {
            return "nombre_muy_corto";
        }
 
        // 3. Longitud máxima (15 caracteres)
        if (nombre.trim().length() > 30) {
            return "nombre_muy_largo";
        }
 
        // 4. Tipo válido
        if (!tipo.equals("Ingreso") && !tipo.equals("Egreso")) {
            return "tipo_invalido";
        }
 
        // 5. Nombre duplicado
        if (dao.existeNombre(nombre.trim())) {
            return "nombre_duplicado";
        }
 
        return "ok";
    }
 
    /**
     * Validación para EDITAR una categoría existente.
     */
    public static String validarEditar(String nombre, String tipo, int idCategoria, CategoriaDao dao) {
 
        // 1. Campos vacíos
        if (nombre == null || nombre.trim().isEmpty() ||
            tipo   == null || tipo.trim().isEmpty()) {
            return "vacio";
        }
 
        // 2. Longitud mínima
        if (nombre.trim().length() < 4) {
            return "nombre_muy_corto";
        }
 
        // 3. Longitud máxima
        if (nombre.trim().length() > 30) {
            return "nombre_muy_largo";
        }
 
        // 4. Tipo válido
        if (!tipo.equals("Ingreso") && !tipo.equals("Egreso")) {
            return "tipo_invalido";
        }
 
        // 5. Nombre duplicado excluyendo el registro actual
        if (dao.existeNombreExcluyendoId(nombre.trim(), idCategoria)) {
            return "nombre_duplicado";
        }
 
        return "ok";
    }
}