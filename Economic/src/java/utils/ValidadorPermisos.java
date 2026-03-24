package utils;

import modelo.Usuario;
import java.util.List;

/**
 * Utilidad para validar permisos de usuarios en la aplicación
 * Facilita la verificación de accesos a diferentes funcionalidades
 */
public class ValidadorPermisos {
    
    // Constantes de permisos definidos
    public static final String CREAR_TRANSACCIONES = "crear_transacciones";
    public static final String VER_TRANSACCIONES = "ver_transacciones";
    public static final String EDITAR_TRANSACCIONES = "editar_transacciones";
    public static final String ELIMINAR_TRANSACCIONES = "eliminar_transacciones";
    
    public static final String CREAR_CATEGORIAS = "crear_categorias";
    public static final String VER_CATEGORIAS = "ver_categorias";
    public static final String EDITAR_CATEGORIAS = "editar_categorias";
    public static final String ELIMINAR_CATEGORIAS = "eliminar_categorias";
    
    public static final String CREAR_METAS = "crear_metas";
    public static final String VER_METAS = "ver_metas";
    public static final String EDITAR_METAS = "editar_metas";
    public static final String ELIMINAR_METAS = "eliminar_metas";
    
    public static final String ADMINISTRAR_USUARIOS = "administrar_usuarios";
    public static final String VER_REPORTES = "ver_reportes";
    
    /**
     * Verifica si un usuario tiene un permiso específico
     * @param usuario Usuario a validar
     * @param permiso Permiso a verificar
     * @return true si tiene el permiso, false en caso contrario
     */
    public static boolean tienePermiso(Usuario usuario, String permiso) {
        if (usuario == null || usuario.getPermisos() == null) {
            return false;
        }
        
        List<String> permisos = usuario.getPermisos();
        return permisos.contains(permiso);
    }
    
    /**
     * Verifica si un usuario es administrador
     * @param usuario Usuario a validar
     * @return true si es administrador, false en caso contrario
     */
    public static boolean esAdministrador(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        
        return "administrador".equals(usuario.getNombreRol());
    }
    
    /**
     * Verifica si un usuario es usuario normal (no administrador)
     * @param usuario Usuario a validar
     * @return true si es usuario normal, false en caso contrario
     */
    public static boolean esUsuarioNormal(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        
        return "usuario".equals(usuario.getNombreRol());
    }
    
    /**
     * Verifica si un usuario puede acceder a funcionalidades de administración
     * @param usuario Usuario a validar
     * @return true si puede administrar, false en caso contrario
     */
    public static boolean puedeAdministrar(Usuario usuario) {
        return tienePermiso(usuario, ADMINISTRAR_USUARIOS) || esAdministrador(usuario);
    }
    
    /**
     * Obtiene mensaje de error para acceso denegado
     * @param permiso Permiso requerido
     * @return Mensaje descriptivo del error
     */
    public static String getMensajeAccesoDenegado(String permiso) {
        return "Acceso denegado. No tienes el permiso requerido: " + permiso;
    }
    
    /**
     * Verifica múltiples permisos (operador AND)
     * @param usuario Usuario a validar
     * @param permisos Array de permisos requeridos
     * @return true si tiene todos los permisos, false en caso contrario
     */
    public static boolean tieneTodosLosPermisos(Usuario usuario, String... permisos) {
        if (usuario == null || permisos == null) {
            return false;
        }
        
        for (String permiso : permisos) {
            if (!tienePermiso(usuario, permiso)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica si tiene al menos uno de los permisos (operador OR)
     * @param usuario Usuario a validar
     * @param permisos Array de permisos alternativos
     * @return true si tiene al menos un permiso, false en caso contrario
     */
    public static boolean tieneAlgunPermiso(Usuario usuario, String... permisos) {
        if (usuario == null || permisos == null) {
            return false;
        }
        
        for (String permiso : permisos) {
            if (tienePermiso(usuario, permiso)) {
                return true;
            }
        }
        return false;
    }
}
