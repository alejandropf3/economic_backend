package controlador;
 
import dao.TokenRecuperacionDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "CodigoVerificacionControlador", urlPatterns = {"/CodigoVerificacionControlador"})
public class CodigoVerificacionControlador extends HttpServlet {
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        // 1. Verificar que la sesión de recuperación existe
        HttpSession session = request.getSession(false);
        if (session == null
                || session.getAttribute("correoRecuperacion") == null
                || session.getAttribute("idUsuarioRecuperacion") == null) {
            // Sesión expirada o acceso directo — volver al inicio
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=sesion_expirada");
            return;
        }
 
        String codigo = request.getParameter("codigo");
        long idUsuario = (long) session.getAttribute("idUsuarioRecuperacion");
 
        // 2. Validación básica del campo
        if (codigo == null || codigo.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/codigo_verificacion.jsp?res=codigo_vacio");
            return;
        }
 
        codigo = codigo.trim();
 
        // 3. Validar el token contra la BD (activo + no expirado)
        TokenRecuperacionDao tokenDao = new TokenRecuperacionDao();
        long idValidado = tokenDao.validarToken(idUsuario, codigo);
 
        if (idValidado == -1) {
            // Código incorrecto o expirado
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/codigo_verificacion.jsp?res=codigo_invalido");
            return;
        }
 
        // 4. Marcar el token como usado para que no pueda reutilizarse
        tokenDao.marcarTokenUsado(idUsuario, codigo);
 
        // 5. Marcar en sesión que el código fue verificado correctamente
        session.setAttribute("tokenVerificado", true);
 
        // 6. Redirigir al formulario de cambio de contraseña
        response.sendRedirect(request.getContextPath()
                + "/Public/User/cambio_contrase%C3%B1a.jsp");
    }
 
    // GET: si alguien intenta acceder directo, redirige a verificación
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath()
                + "/Public/User/codigo_verificacion.jsp");
    }
}