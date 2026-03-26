package controlador;
 
import dao.UsuarioDao;
import dao.TokenRecuperacionDao;
import utils.ServicioCorreo;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "RecuperacionControlador", urlPatterns = {"/RecuperacionControlador"})
public class RecuperacionControlador extends HttpServlet {
 
    private static final int MINUTOS_EXPIRACION = 5;
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");
        procesarRecuperacion(correo, request, response);
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
 
        String accion = request.getParameter("accion");
 
        // Enlace "Generar nuevo código" desde codigo_verificacion.jsp
        if ("reenviar".equals(accion)) {
            String correo = request.getParameter("correo");
            procesarRecuperacion(correo, request, response);
        } else {
            // Acceso directo a la URL → formulario
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp");
        }
    }
 
    // ── LÓGICA CENTRAL (compartida entre POST y reenvío GET) ─────────────────
    private void procesarRecuperacion(String correo, HttpServletRequest request,
                                      HttpServletResponse response)
            throws IOException {
 
        // 1. Validación básica
        if (correo == null || correo.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=correo_vacio");
            return;
        }
 
        correo = correo.trim();
 
        // 2. Verificar existencia del correo (igual que LoginControlador)
        UsuarioDao usuarioDao = new UsuarioDao();
        if (!usuarioDao.existeCorreo(correo)) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=correo_no_encontrado");
            return;
        }
 
        // 3. Obtener ID del usuario
        TokenRecuperacionDao tokenDao = new TokenRecuperacionDao();
        long idUsuario = tokenDao.obtenerIdUsuarioPorCorreo(correo);
 
        if (idUsuario == -1) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=error_general");
            return;
        }
 
        // 4. Invalidar tokens anteriores activos
        tokenDao.invalidarTokensAnteriores(idUsuario);
 
        // 5. Generar código de 6 dígitos
        String codigo = generarCodigo();
 
        // 6. Calcular expiración
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION);
 
        // 7. Guardar token en BD
        if (!tokenDao.guardarToken(idUsuario, codigo, expiracion)) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=error_general");
            return;
        }
 
        // 8. Enviar código por correo
        if (!ServicioCorreo.enviarCodigoRecuperacion(correo, codigo)) {
            response.sendRedirect(request.getContextPath()
                    + "/Public/User/recuperacion_contrase%C3%B1a.jsp?res=error_correo");
            return;
        }
 
        // 9. Guardar datos en sesión
        HttpSession session = request.getSession();
        session.setAttribute("correoRecuperacion", correo);
        session.setAttribute("idUsuarioRecuperacion", idUsuario);
 
        // 10. Redirigir a ingreso de código
        response.sendRedirect(request.getContextPath()
                + "/Public/User/codigo_verificacion.jsp");
    }
 
    // ── GENERADOR DE CÓDIGO ───────────────────────────────────────────────────
    private String generarCodigo() {
        int codigo = 100000 + new Random().nextInt(900000);
        return String.valueOf(codigo);
    }
}