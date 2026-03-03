package utils;

import dao.UsuarioDao;
import modelo.Usuario;

public class validarLogin {

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