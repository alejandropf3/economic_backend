package controlador; // Paquete que contiene todos los controladores del sistema

// Importaciones necesarias para manejo de usuarios
import dao.UsuarioDao;               // Clase para acceder a datos de usuarios en la BD
import modelo.Usuario;                // Clase que representa un usuario del sistema
import configuracion.hash;            // Clase para encriptar contraseñas
import java.io.IOException;            // Para manejar errores de entrada/salida
import java.net.URLEncoder;           // Para codificar URLs con caracteres especiales
import java.nio.charset.StandardCharsets; // Para manejar codificación UTF-8
import jakarta.servlet.ServletException; // Para errores de servlets
import jakarta.servlet.annotation.WebServlet; // Anotación para configurar servlet
import jakarta.servlet.http.HttpServlet;   // Clase base para servlets HTTP
import jakarta.servlet.http.HttpServletRequest;  // Para manejar peticiones web
import jakarta.servlet.http.HttpServletResponse; // Para manejar respuestas web
import jakarta.servlet.http.HttpSession;      // Para manejar sesiones de usuario

/**
 * Controlador que maneja el registro de nuevos usuarios y la edición de perfiles.
 * 
 * Este servlet permite:
 * - Registrar nuevos usuarios en el sistema
 * - Editar el perfil de usuarios existentes (nombre y correo)
 * - Validar datos de entrada (formato de correo, longitud de nombre, etc.)
 * - Encriptar contraseñas para seguridad
 * - Actualizar la sesión del usuario cuando edita su perfil
 */
@WebServlet(name = "UsuarioControlador", urlPatterns = {"/UsuarioControlador"}) // Configura la URL
public class UsuarioControlador extends HttpServlet { // Hereda de HttpServlet para ser un servlet web

    /**
     * Método que se ejecuta cuando el usuario accede a la página de registro (método GET).
     * 
     * @param request  Objeto que contiene información de la petición
     * @param response Objeto que envía la respuesta al navegador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Lanza excepciones que pueden ocurrir
        // Redirige directamente a la página de registro de usuarios
        response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp");
    }

    /**
     * Método que se ejecuta cuando el usuario envía un formulario (método POST).
     * 
     * @param request  Objeto que contiene los datos del formulario
     * @param response Objeto que envía la respuesta al navegador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Lanza excepciones que pueden ocurrir
        // Configura la codificación para leer correctamente acentos y caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Obtiene el tipo de acción a realizar (editar perfil o registro nuevo)
        String accion = request.getParameter("accion");

        // Si la acción es editar perfil
        if ("editarPerfil".equals(accion)) {
            // Verifica si hay una sesión activa y si el usuario está logueado
            HttpSession session = request.getSession(false); // Obtiene sesión sin crear una nueva
            if (session == null || session.getAttribute("usuario") == null) { // Si no hay sesión o usuario
                response.sendRedirect(request.getContextPath() + "/index.jsp"); // Redirige al login
                return; // Detiene la ejecución
            }
            
            // Paso 1: Capturar los datos del formulario de edición
            String nombre = request.getParameter("txtNombre"); // Lee el nombre del formulario
            String correo = request.getParameter("txtCorreo"); // Lee el correo del formulario
            
            // Obtiene el usuario que está logueado
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            long idUsuario = usuarioSesion.getIdUsuario(); // Obtiene el ID del usuario
            
            // Paso 2: Validar los datos ingresados
            String resultado = validarDatosEdicion(nombre, correo, idUsuario);
            
            // Si la validación falló
            if (!resultado.equals("ok")) {
                // Codifica el mensaje de error para enviarlo por URL
                String mensajeCodificado = URLEncoder.encode(resultado, StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                        + "/CategoriaControlador?resEdicion=" + mensajeCodificado);
                return; // Detiene la ejecución
            }
            
            // Paso 3: Actualizar los datos del usuario en la base de datos
            Usuario usuarioActualizado = new Usuario(); // Crea objeto con los nuevos datos
            usuarioActualizado.setIdUsuario(idUsuario); // Asigna el ID del usuario
            usuarioActualizado.setNombre(nombre.trim()); // Asigna el nombre (sin espacios al inicio/final)
            usuarioActualizado.setCorreo(correo.trim()); // Asigna el correo (sin espacios al inicio/final)
            
            UsuarioDao dao = new UsuarioDao(); // Crea objeto para acceder a la base de datos
            if (dao.actualizarUsuario(usuarioActualizado)) { // Si la actualización fue exitosa
                // Paso 4: Actualizar el objeto usuario que está en la sesión
                usuarioSesion.setNombre(nombre.trim()); // Actualiza el nombre en sesión
                usuarioSesion.setCorreo(correo.trim()); // Actualiza el correo en sesión
                session.setAttribute("usuario", usuarioSesion); // Guarda los cambios en sesión
                
                // Redirige con mensaje de éxito
                response.sendRedirect(request.getContextPath() + "/CategoriaControlador?resEdicion=ok");
            } else { // Si hubo error al actualizar
                // Codifica el mensaje de error
                String mensajeCodificado = URLEncoder.encode("error_general", StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                        + "/CategoriaControlador?resEdicion=" + mensajeCodificado);
            }
            return; // Detiene la ejecución
        }
        
        // Paso 5: Si no es editarPerfil, procesar el registro de un nuevo usuario
        procesarRegistro(request, response);
    }

    /**
     * Método que procesa el registro de un nuevo usuario.
     * 
     * @param request  Objeto que contiene los datos del formulario de registro
     * @param response Objeto que envía la respuesta al navegador
     */
    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Paso 1: Capturar los datos del formulario de registro
        String nombre = request.getParameter("txtNombre");      // Lee el nombre
        String correo = request.getParameter("txtEmail");       // Lee el correo
        String pass = request.getParameter("txtContrasena");   // Lee la contraseña
        String confirmPass = request.getParameter("txtConfirmar"); // Lee la confirmación

        UsuarioDao dao = new UsuarioDao(); // Crea objeto para acceder a la base de datos

        // Paso 2: Validar los datos ingresados usando la clase de validación
        String resultado = utils.validarUsuarios.validarRegistro(correo, pass, confirmPass, dao);

        // Paso 3: Evaluar el resultado de la validación
        if (!resultado.equals("ok")) { // Si la validación falló
            // Redirige a la página de registro con el mensaje de error
            response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp?res=" + resultado);
            return; // Detiene la ejecución
        }

        // Paso 4: Si todo está correcto, encriptar la contraseña y registrar el usuario
        Usuario user = new Usuario(); // Crea objeto para el nuevo usuario
        user.setNombre(nombre); // Asigna el nombre
        user.setCorreo(correo); // Asigna el correo
        
        // Encripta la contraseña con SHA-256 para guardarla de forma segura
        String passEncriptada = hash.sha256(pass);
        user.setContrasena(passEncriptada); // Asigna la contraseña ya encriptada

        // Intenta registrar el usuario en la base de datos
        if (dao.registrar(user)) { // Si el registro fue exitoso
            // Muestra la página de registro con mensaje de éxito
            request.setAttribute("registroExitoso", "exitoso"); // Marca que el registro fue exitoso
            request.getRequestDispatcher("/Public/User/registro_usuario.jsp") // Obtiene la página
           .forward(request, response); // Reenvía a la página con mensaje de éxito
        } else { // Si hubo error al registrar
            // Redirige a la página de registro con mensaje de error
            response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp?res=error");
        }
    }

    /**
     * Método que valida los datos cuando un usuario edita su perfil.
     * 
     * @param nombre    Nombre ingresado por el usuario
     * @param correo    Correo ingresado por el usuario
     * @param idUsuario ID del usuario que está editando (para evitar duplicados)
     * @return "ok" si todo es válido, o un código de error si hay problemas
     */
    private String validarDatosEdicion(String nombre, String correo, long idUsuario) {
        // Validar que el nombre no esté vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            return "nombre_vacio"; // Error: nombre vacío
        }
        // Validar que el nombre tenga al menos 3 caracteres
        if (nombre.trim().length() < 3) {
            return "nombre_muy_corto"; // Error: nombre muy corto
        }
        // Validar que el nombre no tenga más de 50 caracteres
        if (nombre.trim().length() > 50) {
            return "nombre_muy_largo"; // Error: nombre muy largo
        }
        
        // Validar que el correo no esté vacío
        if (correo == null || correo.trim().isEmpty()) {
            return "correo_vacio"; // Error: correo vacío
        }
        
        // Validar el formato del correo electrónico usando expresión regular
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"; // Patrón de email válido
        if (!correo.trim().matches(emailRegex)) {
            return "correo_formato_invalido"; // Error: formato de correo inválido
        }
        
        // Validar que el correo no esté duplicado (excluyendo al usuario actual)
        UsuarioDao dao = new UsuarioDao(); // Crea objeto para acceder a la base de datos
        if (dao.existeCorreoOtroUsuario(correo.trim(), idUsuario)) { // Si otro usuario ya tiene este correo
            return "correo_duplicado"; // Error: correo ya existe
        }
        
        // Si todas las validaciones pasaron, retorna "ok"
        return "ok";
    }

    /**
     * Método que devuelve información sobre este servlet.
     * 
     * @return Descripción del propósito del controlador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    public String getServletInfo() {
        return "Controlador para registro y edición de usuarios"; // Describe lo que hace este servlet
    }
}