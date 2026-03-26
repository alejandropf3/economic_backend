package utils;

import dao.UsuarioDao;

/**
 * Utilidad de validación para el registro de nuevos usuarios.
 * <p>
 * Verifica que los datos del formulario de registro sean correctos antes de
 * persistirlos: campos vacíos, coincidencia de contraseñas, longitud mínima
 * y unicidad del correo electrónico.
 * </p>
 */
public class validarUsuarios {

    /**
     * Valida los datos del formulario de registro de usuario.
     *
     * @param correo      Correo electrónico ingresado.
     * @param pass        Contraseña ingresada (en texto plano).
     * @param confirmPass Confirmación de la contraseña.
     * @param dao         Instancia del {@link UsuarioDao} para verificar unicidad de correo.
     * @return {@code "ok"} si todos los datos son válidos, o uno de los siguientes
     *         códigos de error:
     *         <ul>
     *           <li>{@code "vacio"} — algún campo obligatorio está vacío.</li>
     *           <li>{@code "pass_error"} — las contraseñas no coinciden.</li>
     *           <li>{@code "cantidad_de_caracteres_error"} — contraseña menor a 8 caracteres.</li>
     *           <li>{@code "correo_duplicado"} — el correo ya está registrado.</li>
     *         </ul>
     */
    public static String validarRegistro(String correo, String pass, String confirmPass, UsuarioDao dao) {

        // 1. Validar campos vacíos
        if (correo == null || correo.trim().isEmpty() ||
            pass == null || pass.trim().isEmpty() ||
            confirmPass == null || confirmPass.trim().isEmpty()) {
            return "vacio";
        }

        // 2. Validar coincidencia de contraseña
        if (!pass.equals(confirmPass)) {
            return "pass_error";
        }

        // 3. Validar longitud mínima (8 caracteres)
        if (pass.length() < 8) {
            return "cantidad_de_caracteres_error";
        }

        // 4. Validar correo único
        if (dao.existeCorreo(correo)) {
            return "correo_duplicado";
        }

        return "ok";
    }
}
