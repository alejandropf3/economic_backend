package utils;

import modelo.Usuario;
import java.util.List;

/**
 * Sistema centralizado de validación de permisos y control de acceso.
 * <p>
 * Esta clase es el pilar fundamental de la seguridad del sistema Economic.
 * Se crea como una clase estática porque los permisos son una función
 * pura del usuario y no necesitan mantener estado. Centralizar esta
 * lógica asegura que todas las validaciones de acceso sean consistentes
 * y mantenibles en toda la aplicación.
 * </p>
 * <p>
 * ¿Por qué no usar anotaciones de seguridad? Porque este es un sistema
 * de aprendizaje donde queremos que la lógica de seguridad sea explícita
 * y fácil de entender. Además, las anotaciones requerirían frameworks
 * adicionales que complicarían la arquitectura actual.
 * </p>
 * <p>
 * El diseño de permisos granulares permite un control fino sobre qué
 * puede hacer cada usuario, siguiendo el principio de mínimo privilegio.
 * </p>
 */
public class ValidadorPermisos {
    
    // Constantes de permisos definidos - centralizadas para facilitar mantenimiento
    // Usar constantes evita errores de tipeo y facilita refactorización
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
     * Verifica si un usuario tiene un permiso específico.
     * <p>
     * Este método es el núcleo del sistema de control de acceso. Cada vez
     * que un usuario intenta acceder a una funcionalidad sensible, este método
     * determina si la acción está permitida basándose en los permisos
     * asignados al rol del usuario.
     * </p>
     * <p>
     * ¿Por qué la validación de nulos? Porque la seguridad debe ser
     * "fail-safe" - si algo está mal (usuario nulo, permisos nulos),
     * por defecto denegamos el acceso. Esto sigue el principio de
     * "cerrado por defecto" para mayor seguridad.
     * </p>
     *
     * @param usuario Usuario a validar, obtenido generalmente de la sesión.
     * @param permiso Permiso a verificar, debe coincidir con una constante definida.
     * @return true si tiene el permiso, false en caso contrario o si hay errores.
     */
    public static boolean tienePermiso(Usuario usuario, String permiso) {
        // Validación de seguridad: fail-safe - si algo está mal, denegamos acceso
        if (usuario == null || usuario.getPermisos() == null) {
            return false;
        }
        
        List<String> permisos = usuario.getPermisos();
        return permisos.contains(permiso);
    }
    
    /**
     * Verifica si un usuario es administrador.
     * <p>
     * Este método es un atajo para la validación más común del sistema.
     * Los administradores tienen acceso completo, por lo que esta
     * verificación se usa frecuentemente para mostrar/ocultar
     * funcionalidades administrativas en la interfaz.
     * </p>
     * <p>
     * ¿Por qué verificar por nombre de rol y no por permisos?
     * Porque el rol "administrador" es especial y tiene significado
     * semántico en todo el sistema. Es más claro y mantenible
     * verificar el rol directamente que verificar múltiples permisos.
     * </p>
     *
     * @param usuario Usuario a validar.
     * @return true si es administrador, false en caso contrario o si es nulo.
     */
    public static boolean esAdministrador(Usuario usuario) {
        // Fail-safe: si el usuario es nulo, no es administrador
        if (usuario == null) {
            return false;
        }
        
        return "administrador".equals(usuario.getNombreRol());
    }
    
    /**
     * Verifica si un usuario es usuario normal (no administrador).
     * <p>
     * Complemento del método anterior para validaciones específicas
     * del rol de usuario estándar. Se usa para aplicar restricciones
     * que solo aplican a usuarios no administradores.
     * </p>
     *
     * @param usuario Usuario a validar.
     * @return true si es usuario normal, false si es administrador o nulo.
     */
    public static boolean esUsuarioNormal(Usuario usuario) {
        // Fail-safe: si el usuario es nulo, no es usuario normal
        if (usuario == null) {
            return false;
        }
        
        return "usuario".equals(usuario.getNombreRol());
    }
    
    /**
     * Verifica si un usuario puede acceder a funcionalidades de administración.
     * <p>
     * Este método combina la verificación de permisos específicos con
     * el rol de administrador. Esto permite que futuramente podamos
     * tener roles intermedios con permisos administrativos limitados
     * sin modificar este método.
     * </p>
     * <p>
     * ¿Por qué OR lógico en lugar de solo verificar permiso?
     * Porque los administradores tradicionalmente tienen acceso
     * a todo, y esta lógica mantiene esa convención mientras permite
     * flexibilidad futura para nuevos roles.
     * </p>
     *
     * @param usuario Usuario a validar.
     * @return true si puede administrar, false en caso contrario.
     */
    public static boolean puedeAdministrar(Usuario usuario) {
        return tienePermiso(usuario, ADMINISTRAR_USUARIOS) || esAdministrador(usuario);
    }
    
    /**
     * Obtiene mensaje de error para acceso denegado.
     * <p>
     * Centraliza los mensajes de error para consistencia en toda
     * la aplicación. Esto facilita la internacionalización
     * futura y el mantenimiento de mensajes.
     * </p>
     *
     * @param permiso Permiso requerido que fue denegado.
     * @return Mensaje descriptivo del error para mostrar al usuario.
     */
    public static String getMensajeAccesoDenegado(String permiso) {
        return "Acceso denegado. No tienes el permiso requerido: " + permiso;
    }
    
    /**
     * Verifica múltiples permisos (operador AND).
     * <p>
     * Este método es útil para funcionalidades que requieren
     * múltiples permisos simultáneamente. Por ejemplo, editar
     * una transacción podría requerir permisos de ver y editar.
     * </p>
     * <p>
     * ¿Por qué no usar streams? Porque este código debe ser
     * compatible con Java 8+ y la versión actual usa bucles
     * tradicionales que son más claros para desarrolladores
     * junior y tienen mejor rendimiento para arrays pequeños.
     * </p>
     *
     * @param usuario Usuario a validar.
     * @param permisos Array de permisos requeridos (todos deben estar presentes).
     * @return true si tiene todos los permisos, false si falta alguno o hay errores.
     */
    public static boolean tieneTodosLosPermisos(Usuario usuario, String... permisos) {
        // Validaciones de seguridad
        if (usuario == null || permisos == null) {
            return false;
        }
        
        // Verificar cada permiso requerido
        for (String permiso : permisos) {
            if (!tienePermiso(usuario, permiso)) {
                return false; // Falla en el primer permiso faltante (cortocircuito)
            }
        }
        return true;
    }
    
    /**
     * Verifica si tiene al menos uno de los permisos (operador OR).
     * <p>
     * Este método es útil para funcionalidades que pueden ser
     * accedidas por diferentes tipos de usuarios. Por ejemplo,
     * ver reportes podría estar disponible para administradores
     * y usuarios con permiso específico de reportes.
     * </p>
     *
     * @param usuario Usuario a validar.
     * @param permisos Array de permisos alternativos (al menos uno debe estar presente).
     * @return true si tiene al menos un permiso, false si no tiene ninguno o hay errores.
     */
    public static boolean tieneAlgunPermiso(Usuario usuario, String... permisos) {
        // Validaciones de seguridad
        if (usuario == null || permisos == null) {
            return false;
        }
        
        // Buscar cualquier permiso que coincida
        for (String permiso : permisos) {
            if (tienePermiso(usuario, permiso)) {
                return true; // Éxito en el primer permiso encontrado (cortocircuito)
            }
        }
        return false;
    }
}
