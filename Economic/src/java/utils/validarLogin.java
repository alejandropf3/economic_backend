package utils;

import dao.UsuarioDao;
import modelo.Usuario;

/**
 * Utilidad de validación para el proceso de inicio de sesión.
 * <p>
 * Valida que las credenciales ingresadas no estén vacías antes de
 * consultarlas contra la base de datos. La contraseña debe llegar
 * ya encriptada con SHA-256.
 * </p>
 */
public class validarLogin {

    /**
     * Valida las credenciales del usuario y, si son válidas, consulta al DAO.
     *
     * @param correo Correo electrónico ingresado por el usuario.
     * @param pass   Contraseña ya encriptada con SHA-256.
     * @param dao    Instancia del {@link UsuarioDao} para consultar la base de datos.
     * @return El objeto {@link Usuario} si las credenciales son correctas,
     *         o {@code null} si están vacías o no coinciden.
     */
    public static Usuario validarCredenciales(String correo, String pass, UsuarioDao dao) {

        // 1. Validar que los campos no estén vacíos
        if (correo == null || correo.trim().isEmpty() ||
            pass == null || pass.trim().isEmpty()) {
            return null;
        }

        // 2. Llamar al DAO
        return dao.iniciarSesion(correo, pass);
    }
}
