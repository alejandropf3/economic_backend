package controlador;
 
import configuracion.hash;
import dao.UsuarioDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Usuario;
 
@WebServlet(name = "CambioContrasenaControlador", urlPatterns = {"/CambioContrasenaControlador"})
public class CambioContrasenaControlador extends HttpServlet {
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        // 1. Verificar que el token fue verificado en el paso anterior
        HttpSession session = request.getSession(false);
        if (session == null
                || session.getAttribute("tokenVerificado") == null
                || !(boolean) session.getAttribute("tokenVerificado")
                || session.getAttribute("idUsuarioRecuperacion") == null) {
            // Acceso directo sin haber verificado el código
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=acceso_denegado");
            return;
        }
 
        String nuevaContrasena  = request.getParameter("nuevaContrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");
        long idUsuario = (long) session.getAttribute("idUsuarioRecuperacion");
 
        // 2. Validaciones del servidor (el JS ya las hace en el cliente)
        String resultado = validarContrasenas(nuevaContrasena, confirmarContrasena);
        if (!resultado.equals("ok")) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/cambio_contrase%C3%B1a.jsp?res=" + resultado);
            return;
        }
 
        // 3. Encriptar la nueva contraseña con SHA-256 (igual que en UsuarioControlador)
        String contrasenaEncriptada = hash.sha256(nuevaContrasena.trim());
 
        // 4. Actualizar en la BD
        UsuarioDao dao = new UsuarioDao();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setContrasena(contrasenaEncriptada);
 
        boolean actualizado = dao.actualizarContrasena(usuario);
 
        if (!actualizado) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/cambio_contrase%C3%B1a.jsp?res=error_general");
            return;
        }
 
        // 5. Limpiar todos los atributos de recuperación de la sesión
        session.removeAttribute("correoRecuperacion");
        session.removeAttribute("idUsuarioRecuperacion");
        session.removeAttribute("tokenVerificado");
 
        // 6. Redirigir al login con mensaje de éxito
        response.sendRedirect(request.getContextPath()
                + "/index.jsp?res=contrasena_actualizada");
    }
 
    // GET: redirige al formulario
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()
                + "/Public/User/cambio_contrase%C3%B1a.jsp");
    }
 
    // ── VALIDACIONES ─────────────────────────────────────────────────────────
    private String validarContrasenas(String pass, String confirmar) {
        if (pass == null || pass.trim().isEmpty()) {
            return "contrasena_vacia";
        }
        if (confirmar == null || confirmar.trim().isEmpty()) {
            return "confirmacion_vacia";
        }
        if (pass.trim().length() < 8) {
            return "contrasena_muy_corta";
        }
        if (pass.trim().length() > 15) {
            return "contrasena_muy_larga";
        }
        if (!pass.trim().equals(confirmar.trim())) {
            return "contrasenas_no_coinciden";
        }
        return "ok";
    }
}