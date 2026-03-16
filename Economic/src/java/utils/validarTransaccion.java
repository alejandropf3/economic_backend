package utils;
 
public class validarTransaccion {
 
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