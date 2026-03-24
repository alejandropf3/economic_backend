package controlador;
 
import configuracion.hash;
import dao.AdminDao;
import modelo.Usuario;
import modelo.ResultadoEliminacion;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
/**
 * AdminControlador.java
 * Servlet que maneja la vista de administración de usuarios.
 * GET  → lista todos los usuarios
 * POST → elimina un usuario (con validaciones)
 */
@WebServlet(name = "AdminControlador", urlPatterns = {"/AdminControlador"})
public class AdminControlador extends HttpServlet {
 
    // ── GET → Cargar lista de usuarios ────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Validar que el usuario tenga rol de administrador
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        AdminDao dao = new AdminDao();
        if (!dao.esAdministrador(usuarioSesion.getIdUsuario())) {
            response.sendRedirect(request.getContextPath() + "/MenuControlador?error=no_admin");
            return;
        }
 
        AdminDao dao2 = new AdminDao();
        List<Usuario> usuarios = dao2.listarUsuarios();
        request.setAttribute("usuarios", usuarios);
 
        request.getRequestDispatcher("/Public/Admin/administrar_usuarios.jsp")
               .forward(request, response);
    }
 
    // ── POST → Eliminar usuario ───────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        Usuario admin = (Usuario) session.getAttribute("usuario");
        long idAdmin  = admin.getIdUsuario();
 
        String accion = request.getParameter("accion");
 
        if ("eliminar".equals(accion)) {
 
            String idUsuarioStr  = request.getParameter("idUsuario");
            String passAdmin     = request.getParameter("passAdmin");
 
            // ── 1. Validar que llegaron los datos ─────────────────────────────
            if (idUsuarioStr == null || passAdmin == null || passAdmin.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath()
                        + "/AdminControlador?res=datos_incompletos");
                return;
            }
 
            long idUsuarioEliminar = Long.parseLong(idUsuarioStr);
 
            // ── 2. No permitir que el admin se elimine a sí mismo ─────────────
            if (idUsuarioEliminar == idAdmin) {
                response.sendRedirect(request.getContextPath()
                        + "/AdminControlador?res=no_autoeliminacion");
                return;
            }
 
            // ── 3. Verificar contraseña del administrador ─────────────────────
            String passEncriptada = hash.sha256(passAdmin);
            AdminDao dao = new AdminDao();
 
            if (!dao.verificarContrasenaAdmin(idAdmin, passEncriptada)) {
                response.sendRedirect(request.getContextPath()
                        + "/AdminControlador?res=pass_incorrecta&idUsuario=" + idUsuarioEliminar);
                return;
            }
 
            // ── 4. Eliminar usuario (sin validación de asociados) ───────────────
            ResultadoEliminacion resultado = dao.eliminarUsuario(idUsuarioEliminar);
            
            if (resultado.isExito()) {
                StringBuilder resParam = new StringBuilder("eliminado_ok");
                resParam.append("&transaccionesEliminadas=").append(resultado.getTransaccionesEliminadas());
                resParam.append("&categoriasEliminadas=").append(resultado.getCategoriasEliminadas());
                response.sendRedirect(request.getContextPath() + "/AdminControlador?" + resParam);
            } else {
                String mensajeErrorCodificado = URLEncoder.encode(resultado.getMensajeError(), StandardCharsets.UTF_8.toString());
                response.sendRedirect(request.getContextPath()
                        + "/AdminControlador?res=error_eliminar&error=" + mensajeErrorCodificado);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/AdminControlador");
        }
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para administración de usuarios";
    }
}
 