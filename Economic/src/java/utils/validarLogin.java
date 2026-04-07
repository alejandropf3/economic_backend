package utils; // Paquete que contiene clases de utilidad del sistema

// Importaciones necesarias para la validación de login
import dao.UsuarioDao; // Clase para acceder a datos de usuarios en la base de datos
import modelo.Usuario;  // Clase que representa un usuario del sistema

/**
 * Utilidad de validación para el proceso de inicio de sesión.
 * <p>
 * Esta clase actúa como una capa intermedia entre el controlador y el DAO
 * para validar que las credenciales del usuario sean correctas antes de
 * consultar la base de datos. Es importante validar los datos aquí para
 * evitar consultas innecesarias a la base de datos.
 * </p>
 * <p>
 * ¿Por qué no validar directamente en el controlador? Porque separar la
 * lógica de validación en una clase dedicada hace el código más reusable
 * y fácil de probar. Además, centraliza todas las validaciones de login
 * en un solo lugar.
 * </p>
 */
public class validarLogin {

    /**
     * Valida las credenciales del usuario y las consulta en la base de datos.
     * 
     * Este método realiza dos pasos principales:
     * 1. Verifica que el correo y la contraseña no estén vacíos
     * 2. Si son válidos, consulta al DAO para verificar si existen en la BD
     * 
     * @param correo Correo electrónico ingresado por el usuario (sin encriptar)
     * @param pass   Contraseña ya encriptada con SHA-256 desde el controlador
     * @param dao    Instancia del UsuarioDao para consultar la base de datos
     * @return El objeto Usuario si las credenciales son correctas, o null si hay error
     */
    public static Usuario validarCredenciales(String correo, String pass, UsuarioDao dao) {

        // Paso 1: Validar que los campos no estén vacíos o nulos
        if (correo == null || correo.trim().isEmpty() || // Si el correo está vacío
            pass == null || pass.trim().isEmpty()) {     // O si la contraseña está vacía
            return null; // Retorna null para indicar que las credenciales son inválidas
        }

        // Paso 2: Si los campos son válidos, consultar al DAO
        // El DAO se encargará de verificar si el correo y contraseña existen en la BD
        return dao.iniciarSesion(correo, pass); // Retorna el usuario si existe, o null si no
    }
}
