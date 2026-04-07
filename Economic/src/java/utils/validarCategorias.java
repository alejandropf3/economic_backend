package utils; // Paquete que contiene clases de utilidad del sistema

import dao.CategoriaDao; // Clase para acceder a datos de categorías en la base de datos

/**
 * Utilidad de validación para la gestión de categorías de transacción.
 * <p>
 * Esta clase se encarga de validar todas las reglas de negocio relacionadas
 * con las categorías que los usuarios crean y eliminan. Es importante
 * validar aquí para mantener la integridad de los datos y evitar
 * problemas en el sistema financiero.
 * </p>
 * <p>
 * ¿Por qué tener validaciones separadas? Porque las categorías son
 * fundamentales para el correcto funcionamiento del sistema. Si una
 * categoría se elimina incorrectamente, podrían perderse datos
 * importantes de transacciones.
 * </p>
 */
public class validarCategorias {

    /**
     * Valida todos los datos necesarios para crear una nueva categoría.
     * 
     * Este método verifica que:
     * 1. Ni el nombre ni el tipo estén vacíos
     * 2. El nombre tenga entre 4 y 30 caracteres
     * 3. El tipo sea exactamente "Ingreso" o "Egreso"
     * 4. El nombre no esté duplicado para este usuario
     * 
     * @param nombre    Nombre de la nueva categoría que quiere crear el usuario
     * @param tipo      Tipo de transacción: debe ser "Ingreso" o "Egreso"
     * @param idUsuario ID del usuario que está creando la categoría
     * @param dao       Instancia del CategoriaDao para verificar duplicados
     * @return "ok" si todo es válido, o un código de error específico:
     *         - "vacio": nombre o tipo están vacíos
     *         - "nombre_muy_corto": nombre tiene menos de 4 caracteres
     *         - "nombre_muy_largo": nombre tiene más de 30 caracteres
     *         - "tipo_invalido": tipo no es "Ingreso" ni "Egreso"
     *         - "nombre_duplicado": el usuario ya tiene una categoría con ese nombre
     */
    public static String validarCrear(String nombre, String tipo, long idUsuario, CategoriaDao dao) {

        // Paso 1: Validar que ni el nombre ni el tipo estén vacíos
        if (nombre == null || nombre.trim().isEmpty() || // Si el nombre está vacío
            tipo   == null || tipo.trim().isEmpty()) {   // O si el tipo está vacío
            return "vacio"; // Retorna error de campo vacío
        }
        
        // Paso 2: Validar longitud mínima del nombre (4 caracteres)
        if (nombre.trim().length() < 4) {
            return "nombre_muy_corto"; // Retorna error de nombre muy corto
        }
        
        // Paso 3: Validar longitud máxima del nombre (30 caracteres)
        if (nombre.trim().length() > 30) {
            return "nombre_muy_largo"; // Retorna error de nombre muy largo
        }
        
        // Paso 4: Validar que el tipo sea uno de los valores permitidos
        if (!tipo.equals("Ingreso") && !tipo.equals("Egreso")) {
            return "tipo_invalido"; // Retorna error de tipo no válido
        }

        // Paso 5: Validar que el nombre no esté duplicado para este usuario
        // Cada usuario debe tener nombres únicos para sus categorías
        if (dao.existeNombre(nombre.trim(), idUsuario)) {
            return "nombre_duplicado"; // Retorna error de nombre duplicado
        }

        // Si todas las validaciones pasaron, retornamos "ok"
        return "ok";
    }

    /**
     * Valida que una categoría pueda ser eliminada de forma segura.
     * 
     * La validación más importante es verificar que la categoría no esté
     * siendo usada por ninguna transacción. Si eliminamos una categoría
     * que está en uso, perderíamos información financiera importante.
     * 
     * @param idCategoria ID de la categoría que se quiere eliminar
     * @param idUsuario   ID del usuario propietario (reservado para validaciones futuras)
     * @param dao         Instancia del CategoriaDao para verificar uso en transacciones
     * @return "ok" si la categoría puede eliminarse, o "categoria_en_uso" si tiene transacciones
     */
    public static String validarEliminar(int idCategoria, long idUsuario, CategoriaDao dao) {

        // Validación principal: verificar que la categoría no tenga transacciones asociadas
        if (dao.categoriaEnUso(idCategoria)) {
            return "categoria_en_uso"; // Retorna error si la categoría está siendo usada
        }

        // Si la categoría no está en uso, puede eliminarse
        return "ok";
    }
}
