package utils; // Paquete que contiene clases de utilidad del sistema

import dao.UsuarioDao; // Clase para acceder a datos de usuarios en la base de datos

/**
 * Utilidad de validación para el registro de nuevos usuarios.
 * <p>
 * Esta clase se encarga de validar todos los datos que el usuario ingresa
 * en el formulario de registro antes de guardarlos en la base de datos.
 * Es importante validar aquí para evitar guardar datos incorrectos o
 * incompletos en la base de datos.
 * </p>
 * <p>
 * ¿Por qué tener una clase separada para validación? Porque centraliza
 * todas las reglas de validación en un solo lugar, haciendo el código
 * más fácil de mantener y modificar. Si necesitamos cambiar una regla,
 * solo tenemos que hacerlo aquí.
 * </p>
 */
public class validarUsuarios {

    /**
     * Valida todos los datos del formulario de registro de usuario.
     * 
     * Este método realiza las siguientes validaciones en orden:
     * 1. Verifica que ningún campo esté vacío
     * 2. Confirma que las contraseñas coincidan exactamente
     * 3. Verifica que la contraseña tenga al menos 8 caracteres
     * 4. Comprueba que el correo electrónico no esté ya registrado
     * 
     * @param correo      Correo electrónico ingresado por el usuario
     * @param pass        Contraseña ingresada (en texto plano, sin encriptar)
     * @param confirmPass Confirmación de la contraseña ingresada
     * @param dao         Instancia del UsuarioDao para verificar si el correo ya existe
     * @return "ok" si todo es válido, o un código de error específico:
     *         - "vacio": algún campo obligatorio está vacío
     *         - "pass_error": las contraseñas no coinciden
     *         - "cantidad_de_caracteres_error": contraseña tiene menos de 8 caracteres
     *         - "correo_duplicado": el correo ya está registrado en el sistema
     */
    public static String validarRegistro(String correo, String pass, String confirmPass, UsuarioDao dao) {

        // Paso 1: Validar que ningún campo esté vacío o sea solo espacios
        if (correo == null || correo.trim().isEmpty() || // Si el correo está vacío
            pass == null || pass.trim().isEmpty() ||       // O si la contraseña está vacía
            confirmPass == null || confirmPass.trim().isEmpty()) { // O si la confirmación está vacía
            return "vacio"; // Retorna error de campo vacío
        }

        // Paso 2: Validar que la contraseña y la confirmación sean exactamente iguales
        // Usamos equals() para comparar strings, no == que compara referencias
        if (!pass.equals(confirmPass)) {
            return "pass_error"; // Retorna error de contraseñas diferentes
        }

        // Paso 3: Validar que la contraseña tenga al menos 8 caracteres
        // Esta es una medida de seguridad básica para contraseñas robustas
        if (pass.length() < 8) {
            return "cantidad_de_caracteres_error"; // Retorna error de contraseña muy corta
        }

        // Paso 4: Validar que el correo electrónico no esté ya registrado
        // Evitamos tener usuarios duplicados con el mismo correo
        if (dao.existeCorreo(correo)) {
            return "correo_duplicado"; // Retorna error de correo ya existente
        }

        // Si todas las validaciones pasaron, retornamos "ok"
        return "ok";
    }
}
