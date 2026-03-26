package controlador;

import dao.UsuarioDao;
import modelo.Usuario;
import configuracion.hash;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "UsuarioControlador", urlPatterns = {"/UsuarioControlador"})
public class UsuarioControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        if ("editarPerfil".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            // 1. Capturar datos del formulario
            String nombre = request.getParameter("txtNombre");
            String correo = request.getParameter("txtCorreo");
            
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            long idUsuario = usuarioSesion.getIdUsuario();
            
            // 2. Validaciones básicas
            String resultado = validarDatosEdicion(nombre, correo, idUsuario);
            
            if (!resultado.equals("ok")) {
                String mensajeCodificado = URLEncoder.encode(resultado, StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() 
                        + "/CategoriaControlador?resEdicion=" + mensajeCodificado);
                return;
            }
            
            // 3. Actualizar datos del usuario
            Usuario usuarioActualizado = new Usuario();
            usuarioActualizado.setIdUsuario(idUsuario);
            usuarioActualizado.setNombre(nombre.trim());
            usuarioActualizado.setCorreo(correo.trim());
            
            UsuarioDao dao = new UsuarioDao();
            if (dao.actualizarUsuario(usuarioActualizado)) {
                // 4. Actualizar objeto en sesión
                usuarioSesion.setNombre(nombre.trim());
                usuarioSesion.setCorreo(correo.trim());
                session.setAttribute("usuario", usuarioSesion);
                
                response.sendRedirect(request.getContextPath() 
                        + "/CategoriaControlador?resEdicion=ok");
            } else {
                String mensajeCodificado = URLEncoder.encode("error_general", StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath() 
                        + "/CategoriaControlador?resEdicion=" + mensajeCodificado);
            }
            return;
        }
        
        // 5. Si no es editarPerfil, procesar registro normal
        procesarRegistro(request, response);
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Capturar datos del formulario
        String nombre = request.getParameter("txtNombre");
        String correo = request.getParameter("txtEmail");
        String pass = request.getParameter("txtContrasena");
        String confirmPass = request.getParameter("txtConfirmar");

        UsuarioDao dao = new UsuarioDao();

        // 2. Ejecutar la validación (asegúrate de que este paquete 'utils' exista)
        String resultado = utils.validarUsuarios.validarRegistro(correo, pass, confirmPass, dao);

        // 3. Evaluar el resultado de la validación
        if (!resultado.equals("ok")) {
            response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp?res=" + resultado);
            return;
        }

        // 4. Si todo está ok, encriptar y registrar
        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setCorreo(correo);
        
        // --- ENCRIPTACIÓN AQUÍ ---
        String passEncriptada = hash.sha256(pass);
        user.setContrasena(passEncriptada);

        if (dao.registrar(user)) {
            request.setAttribute("registroExitoso", "exitoso");
            request.getRequestDispatcher("/Public/User/registro_usuario.jsp")
           .forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Public/User/registro_usuario.jsp?res=error");
        }
    }

    private String validarDatosEdicion(String nombre, String correo, long idUsuario) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            return "nombre_vacio";
        }
        if (nombre.trim().length() < 3) {
            return "nombre_muy_corto";
        }
        if (nombre.trim().length() > 50) {
            return "nombre_muy_largo";
        }
        
        // Validar correo
        if (correo == null || correo.trim().isEmpty()) {
            return "correo_vacio";
        }
        
        // Validar formato de correo
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!correo.trim().matches(emailRegex)) {
            return "correo_formato_invalido";
        }
        
        // Validar correo duplicado (excluyendo el usuario actual)
        UsuarioDao dao = new UsuarioDao();
        if (dao.existeCorreoOtroUsuario(correo.trim(), idUsuario)) {
            return "correo_duplicado";
        }
        
        return "ok";
    }

    @Override
    public String getServletInfo() {
        return "Controlador para registro y edición de usuarios";
    }
}