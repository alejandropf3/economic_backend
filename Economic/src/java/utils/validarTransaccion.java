package utils;

/**
 * Utilidad de validación para el registro y edición de transacciones.
 * <p>
 * Verifica que los campos obligatorios estén presentes, que el valor sea
 * un número positivo y que la fecha tenga el formato correcto ({@code yyyy-MM-dd}).
 * </p>
 */
public class validarTransaccion {

    /**
     * Valida los datos de una transacción antes de persistirla.
     *
     * @param valor     Valor de la transacción como cadena (debe ser positivo).
     * @param categoria ID de la categoría seleccionada como cadena.
     * @param fecha     Fecha de realización en formato {@code yyyy-MM-dd}.
     * @return {@code "ok"} si todos los datos son válidos, o uno de los siguientes
     *         códigos de error:
     *         <ul>
     *           <li>{@code "valor_vacio"} — el campo valor está vacío.</li>
     *           <li>{@code "categoria_vacia"} — no se seleccionó categoría.</li>
     *           <li>{@code "fecha_vacia"} — el campo fecha está vacío.</li>
     *           <li>{@code "valor_invalido"} — el valor no es numérico o es menor/igual a cero.</li>
     *           <li>{@code "fecha_invalida"} — la fecha no tiene formato válido.</li>
     *         </ul>
     */
    public static String validar(String valor, String categoria, String fecha) {

        // 1. Campos obligatorios
        if (valor == null || valor.trim().isEmpty()) return "valor_vacio";
        if (categoria == null || categoria.trim().isEmpty()) return "categoria_vacia";
        if (fecha == null || fecha.trim().isEmpty()) return "fecha_vacia";

        // 2. Valor numérico y positivo
        try {
            double v = Double.parseDouble(valor);
            if (v <= 0) return "valor_invalido";
        } catch (NumberFormatException e) {
            return "valor_invalido";
        }

        // 3. Fecha con formato correcto (yyyy-MM-dd)
        try {
            java.time.LocalDate.parse(fecha);
        } catch (Exception e) {
            return "fecha_invalida";
        }

        return "ok";
    }
}
