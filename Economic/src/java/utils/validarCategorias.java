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
 
        if (dao.existeNombre(nombre.trim(), idUsuario)) return "nombre_duplicado";
 
        return "ok";
    }
 
    // ── VALIDAR ELIMINAR — verifica que la categoría no esté en uso ───────────
    public static String validarEliminar(int idCategoria, long idUsuario, CategoriaDao dao) {
 
        if (dao.categoriaEnUso(idCategoria)) return "categoria_en_uso";
 
        return "ok";
    }
}
 