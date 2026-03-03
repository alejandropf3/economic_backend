package utils;

import dao.UsuarioDao;

public class validarUsuarios {

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
        if (pass.length() < 8){
            return "cantidad_de_caracteres_error";
        }

        // 5. Validar Correo Único
        if (dao.existeCorreo(correo)) {
            return "correo_duplicado";
        }

        return "ok"; // Siempre retorna "ok" si pasa los puntos 1, 2, 3 y 5
    }
    
}