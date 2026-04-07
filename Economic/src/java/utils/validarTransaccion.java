package utils; // Paquete que contiene clases de utilidad del sistema

/**
 * Utilidad de validación para el registro y edición de transacciones.
 * <p>
 * Esta clase se encarga de validar todos los datos que el usuario ingresa
 * cuando registra una transacción financiera. Es crucial validar aquí
 * para evitar guardar datos incorrectos o inconsistentes en la base
 * de datos, lo que podría afectar los reportes financieros.
 * </p>
 * <p>
 * ¿Por qué validar transacciones aquí? Porque las transacciones son
 * el corazón del sistema financiero. Si guardamos una transacción con
 * datos incorrectos, todos los balances y reportes serán incorrectos.
 * </p>
 */
public class validarTransaccion {

    /**
     * Valida todos los datos de una transacción antes de guardarla.
     * 
     * Este método realiza las siguientes validaciones:
     * 1. Verifica que ningún campo obligatorio esté vacío
     * 2. Confirma que el valor sea un número positivo mayor a cero
     * 3. Verifica que la fecha tenga el formato correcto (yyyy-MM-dd)
     * 
     * @param valor     Valor monetario de la transacción como texto (ej: "150.50")
     * @param categoria ID de la categoría seleccionada como texto
     * @param fecha     Fecha de realización en formato "yyyy-MM-dd"
     * @return "ok" si todo es válido, o un código de error específico:
     *         - "valor_vacio": el campo valor está vacío o nulo
     *         - "categoria_vacia": no se seleccionó ninguna categoría
     *         - "fecha_vacia": el campo fecha está vacío o nulo
     *         - "valor_invalido": el valor no es número o es menor/igual a cero
     *         - "fecha_invalida": la fecha no tiene formato válido
     */
    public static String validar(String valor, String categoria, String fecha) {

        // Paso 1: Validar que todos los campos obligatorios estén presentes
        if (valor == null || valor.trim().isEmpty()) {
            return "valor_vacio"; // Retorna error si el valor está vacío
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            return "categoria_vacia"; // Retorna error si no hay categoría seleccionada
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            return "fecha_vacia"; // Retorna error si la fecha está vacía
        }

        // Paso 2: Validar que el valor sea un número positivo mayor a cero
        try {
            double v = Double.parseDouble(valor); // Convierte el texto a número
            if (v <= 0) { // Verifica que sea mayor que cero
                return "valor_invalido"; // Retorna error si el valor es cero o negativo
            }
        } catch (NumberFormatException e) { // Si no puede convertir a número
            return "valor_invalido"; // Retorna error de formato inválido
        }

        // Paso 3: Validar que la fecha tenga el formato correcto (yyyy-MM-dd)
        try {
            java.time.LocalDate.parse(fecha); // Intenta convertir el texto a fecha
            // Si no lanza excepción, el formato es correcto
        } catch (Exception e) { // Si lanza excepción, el formato es inválido
            return "fecha_invalida"; // Retorna error de formato de fecha
        }

        // Si todas las validaciones pasaron, retornamos "ok"
        return "ok";
    }
}
