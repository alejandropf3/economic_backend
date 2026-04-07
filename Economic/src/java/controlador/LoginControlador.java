package controlador; // Paquete que contiene todos los controladores del sistema

// Importaciones necesarias para el funcionamiento del login
import dao.UsuarioDao;              // Clase para acceder a datos de usuarios en la BD
import modelo.Usuario;               // Clase que representa un usuario del sistema
import configuracion.hash;           // Clase para encriptar contraseñas
import java.io.IOException;           // Para manejar errores de entrada/salida
import jakarta.servlet.ServletException; // Para errores de servlets
import jakarta.servlet.annotation.WebServlet; // Anotación para configurar servlet
import jakarta.servlet.http.HttpServlet;   // Clase base para servlets HTTP
import jakarta.servlet.http.HttpServletRequest;  // Para manejar peticiones web
import jakarta.servlet.http.HttpServletResponse; // Para manejar respuestas web
import jakarta.servlet.http.HttpSession;      // Para manejar sesiones de usuario
import utils.validarLogin;          // Clase para validar credenciales de login
import utils.ValidadorPermisos;     // Clase para verificar permisos de usuario

/**
 * Controlador que maneja el proceso de inicio de sesión de usuarios.
 * 
 * Este servlet se encarga de:
 * - Recibir el correo y contraseña del formulario de login
 * - Encriptar la contraseña para compararla con la guardada
 * - Validar las credenciales en la base de datos
 * - Crear la sesión del usuario si las credenciales son correctas
 * - Redirigir al usuario según su rol (administrador o usuario normal)
 * - Mostrar mensajes de error específicos si falla el login
 */
@WebServlet(name = "LoginControlador", urlPatterns = {"/LoginControlador"}) // Configura la URL para este servlet
public class LoginControlador extends HttpServlet { // Hereda de HttpServlet para ser un servlet web

    /**
     * Método que se ejecuta cuando el usuario envía el formulario de login (método POST).
     * 
     * @param request  Objeto que contiene la información del formulario
     * @param response Objeto que envía la respuesta al navegador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException { // Lanza excepciones que pueden ocurrir
        
        // Configura la codificación de caracteres para leer correctamente los acentos y ñ
        request.setCharacterEncoding("UTF-8");

        // Obtiene los datos del formulario de login
        String correo = request.getParameter("txtCorreo");    // Lee el correo electrónico
        String pass = request.getParameter("txtContrasena");   // Lee la contraseña

        // Encripta la contraseña con SHA-256 para compararla con la guardada en la BD
        String passEncriptada = hash.sha256(pass);

        // Crea un objeto para acceder a la base de datos de usuarios
        UsuarioDao dao = new UsuarioDao();

        // Valida las credenciales usando la contraseña ya encriptada
        Usuario user = validarLogin.validarCredenciales(correo, passEncriptada, dao);

        // Verifica si las credenciales son correctas
        if (user != null) { // Si user no es nulo, el login fue exitoso
            // Crea una sesión para mantener al usuario logueado
            HttpSession session = request.getSession();
            session.setAttribute("usuario", user); // Guarda el objeto usuario en la sesión
            
            // Verifica el rol del usuario para decidir a dónde redirigirlo
            boolean esAdminPorRol = (user.getIdRol() == 1); // 1 = administrador, 2 = usuario normal
            boolean tienePermisoAdmin = ValidadorPermisos.tienePermiso(user, ValidadorPermisos.ADMINISTRAR_USUARIOS);
            
            // Redirige según el rol del usuario
            if (esAdminPorRol && tienePermisoAdmin) { // Si es administrador
                // Muestra mensaje en consola para depuración
                System.out.println("Administrador detectado: " + user.getNombre() + " (ID: " + user.getIdUsuario() + ")");
                System.out.println("Redirigiendo a /AdminControlador");
                // Redirige al panel de administración
                response.sendRedirect(request.getContextPath() + "/AdminControlador");
            } else { // Si es usuario normal
                // Muestra mensaje en consola para depuración
                System.out.println("Usuario normal detectado: " + user.getNombre() + " (Rol: " + user.getNombreRol() + ")");
                System.out.println("Redirigiendo a /MenuControlador");
                // Redirige al menú principal de usuario
                response.sendRedirect(request.getContextPath() + "/MenuControlador");
            }
        } else { // Si las credenciales son incorrectas
            // Verifica si el correo existe para dar un mensaje de error específico
            boolean correoExiste = dao.existeCorreo(correo);
            String mensajeError = correoExiste ? "invalid_password" : "user_not_found";
            // "invalid_password" = correo existe pero contraseña incorrecta
            // "user_not_found" = el correo no está registrado
            // Redirige a la página de login con el mensaje de error
            response.sendRedirect(request.getContextPath() + "/index.jsp?res=" + mensajeError);
        }
    }
}