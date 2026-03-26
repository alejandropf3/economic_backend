package utils;

import dao.CategoriaDao;

/**
 * Utilidad de validación para la gestión de categorías de transacción.
 * <p>
 * Verifica las reglas de negocio para crear y eliminar categorías:
 * nombre no vacío, longitud dentro de rango, tipo válido, ausencia de
 * duplicados y que la categoría no esté en uso antes de eliminarla.
 * </p>
 */
public class validarCategorias {

    /**
     * Valida los datos para crear una nueva categoría.
     *
     * @param nombre    Nombre de la nueva categoría.
     * @param tipo      Tipo de transacción: {@code "Ingreso"} o {@code "Egreso"}.
     * @param idUsuario ID del usuario propietario.
     * @param dao       Instancia del {@link CategoriaDao} para verificar duplicados.
     * @return {@code "ok"} si los datos son válidos, o uno de los siguientes
     *         códigos de error:
     *         <ul>
     *           <li>{@code "vacio"} — nombre o tipo vacíos.</li>
     *           <li>{@code "nombre_muy_corto"} — nombre menor a 4 caracteres.</li>
     *           <li>{@code "nombre_muy_largo"} — nombre mayor a 30 caracteres.</li>
     *           <li>{@code "tipo_invalido"} — tipo distinto de "Ingreso" o "Egreso".</li>
     *           <li>{@code "nombre_duplicado"} — el usuario ya tiene una categoría con ese nombre.</li>
     *         </ul>
     */
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

    /**
     * Valida que una categoría pueda ser eliminada (que no esté en uso).
     *
     * @param idCategoria ID de la categoría a eliminar.
     * @param idUsuario   ID del usuario propietario (reservado para validaciones futuras).
     * @param dao         Instancia del {@link CategoriaDao} para verificar uso en transacciones.
     * @return {@code "ok"} si la categoría puede eliminarse, o:
     *         <ul>
     *           <li>{@code "categoria_en_uso"} — la categoría tiene transacciones asociadas.</li>
     *         </ul>
     */
    public static String validarEliminar(int idCategoria, long idUsuario, CategoriaDao dao) {

        if (dao.categoriaEnUso(idCategoria)) return "categoria_en_uso";

        return "ok";
    }
}
