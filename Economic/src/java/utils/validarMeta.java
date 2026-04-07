package utils; // Paquete que contiene clases de utilidad del sistema
 
// Importaciones necesarias para la validación de metas de ahorro
import dao.MetaDao;               // Clase para acceder a datos de metas en la base de datos
import java.math.BigDecimal;      // Para manejar valores monetarios con precisión
import java.time.LocalDate;       // Para manejar fechas sin problemas de zona horaria
 
/**
 * Utilidad de validación para la gestión de metas de ahorro.
 * <p>
 * Esta clase se encarga de validar todos los datos relacionados con las
 * metas de ahorro que los usuarios crean y gestionan. Las metas son
 * importantes para la motivación financiera del usuario, por lo que
 * debemos asegurar que los datos sean correctos y realistas.
 * </p>
 * <p>
 * ¿Por qué validar metas aquí? Porque las metas involucran fechas futuras
 * y valores monetarios que deben ser realistas y alcanzables. Una
 * validación adecuada ayuda a los usuarios a establecer metas que puedan
 * realmente cumplir.
 * </p>
 */
public class validarMeta {
 
    /**
     * Valida todos los datos necesarios para crear una nueva meta de ahorro.
     * 
     * Este método verifica:
     * 1. Que el nombre tenga entre 3 y 100 caracteres
     * 2. Que el monto sea un número positivo válido
     * 3. Que la fecha límite sea futura
     * 4. Que no exista una meta duplicada para el mismo usuario
     * 
     * @param nombre    Nombre de la meta que quiere crear el usuario
     * @param montoStr  Monto objetivo como texto (ej: "1000.50")
     * @param fechaStr  Fecha límite en formato "yyyy-MM-dd"
     * @param idUsuario ID del usuario que está creando la meta
     * @return "ok" si todo es válido, o un código de error específico
     */
    public static String validarCreacion(String nombre, String montoStr,
                                          String fechaStr, long idUsuario) {
        
        // Paso 1: Validar el nombre de la meta
        if (nombre == null || nombre.trim().isEmpty()) {
            return "nombre_vacio"; // Retorna error si el nombre está vacío
        }
        if (nombre.trim().length() < 3) {
            return "nombre_muy_corto"; // Retorna error si el nombre es muy corto
        }
        if (nombre.trim().length() > 100) {
            return "nombre_muy_largo"; // Retorna error si el nombre es muy largo
        }
 
        // Paso 2: Validar el monto objetivo
        if (montoStr == null || montoStr.trim().isEmpty()) {
            return "monto_vacio"; // Retorna error si el monto está vacío
        }
        BigDecimal monto; // Variable para guardar el monto convertido
        try {
            monto = new BigDecimal(montoStr.trim()); // Convierte texto a BigDecimal
        } catch (NumberFormatException e) { // Si no puede convertir
            return "monto_invalido"; // Retorna error de formato inválido
        }
        if (monto.compareTo(BigDecimal.ZERO) <= 0) { // Si el monto es cero o negativo
            return "monto_debe_ser_positivo"; // Retorna error de monto no positivo
        }
 
        // Paso 3: Validar la fecha límite
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return "fecha_vacia"; // Retorna error si la fecha está vacía
        }
        LocalDate fechaLimite; // Variable para guardar la fecha convertida
        try {
            fechaLimite = LocalDate.parse(fechaStr.trim()); // Convierte texto a fecha
        } catch (Exception e) { // Si no puede convertir
            return "fecha_invalida"; // Retorna error de formato inválido
        }
        if (!fechaLimite.isAfter(LocalDate.now())) { // Si la fecha no es futura
            return "fecha_debe_ser_futura"; // Retorna error de fecha pasada o actual
        }
 
        // Paso 4: Validar que no exista una meta duplicada
        // Evitamos que el usuario tenga metas iguales con la misma fecha límite
        MetaDao dao = new MetaDao(); // Crea objeto para acceder a datos de metas
        if (dao.existeDuplicado(nombre.trim(), fechaLimite, idUsuario)) {
            return "meta_duplicada"; // Retorna error si ya existe una meta igual
        }
 
        // Si todas las validaciones pasaron, retornamos "ok"
        return "ok";
    }
 
    /**
     * Valida los datos cuando un usuario edita una meta existente.
     * 
     * Similar a la creación, pero excluye la meta actual de la validación
     * de duplicados para permitir editar el nombre o fecha de una meta existente.
     * 
     * @param nombre    Nuevo nombre de la meta
     * @param fechaStr  Nueva fecha límite en formato "yyyy-MM-dd"
     * @param idUsuario ID del usuario que está editando
     * @param idMeta    ID de la meta que se está editando
     * @return "ok" si todo es válido, o un código de error específico
     */
    public static String validarEdicion(String nombre, String fechaStr,
                                         long idUsuario, long idMeta) {
        
        // Paso 1: Validar el nombre (mismas reglas que en creación)
        if (nombre == null || nombre.trim().isEmpty()) {
            return "nombre_vacio";
        }
        if (nombre.trim().length() < 3) {
            return "nombre_muy_corto";
        }
        if (nombre.trim().length() > 100) {
            return "nombre_muy_largo";
        }
 
        // Paso 2: Validar la fecha límite (mismas reglas que en creación)
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return "fecha_vacia";
        }
        LocalDate fechaLimite;
        try {
            fechaLimite = LocalDate.parse(fechaStr.trim());
        } catch (Exception e) {
            return "fecha_invalida";
        }
        // Restricción importante: la fecha debe ser futura
        if (!fechaLimite.isAfter(LocalDate.now())) {
            return "fecha_debe_ser_futura";
        }
 
        // Paso 3: Validar duplicado excluyendo la meta actual
        // Permitimos cambiar nombre/fecha siempre que no choque con OTRA meta
        MetaDao dao = new MetaDao();
        if (dao.existeDuplicadoEdicion(nombre.trim(), fechaLimite, idUsuario, idMeta)) {
            return "meta_duplicada";
        }
 
        // Si todas las validaciones pasaron, retornamos "ok"
        return "ok";
    }
 
    /**
     * Valida el monto cuando un usuario hace un abono a una meta.
     * 
     * Esta validación es más simple porque solo necesitamos verificar
     * que el monto sea un número positivo. El abono puede ser de cualquier
     * cantidad, no hay límite máximo.
     * 
     * @param montoStr Monto del abono como texto (ej: "250.75")
     * @return "ok" si el monto es válido, o un código de error específico
     */
    public static String validarAbono(String montoStr) {
        
        // Validar que el monto no esté vacío
        if (montoStr == null || montoStr.trim().isEmpty()) {
            return "monto_vacio"; // Retorna error si el monto está vacío
        }
        
        try {
            // Convertir el texto a BigDecimal y validar que sea positivo
            BigDecimal monto = new BigDecimal(montoStr.trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) { // Si es cero o negativo
                return "monto_debe_ser_positivo"; // Retorna error de monto no positivo
            }
        } catch (NumberFormatException e) { // Si no puede convertir a número
            return "monto_invalido"; // Retorna error de formato inválido
        }
        
        // Si la validación pasó, retornamos "ok"
        return "ok";
    }
}