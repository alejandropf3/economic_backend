package controlador;

import dao.UsuarioDao;
import modelo.Usuario;
import configuracion.hash;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.validarLogin;
import utils.ValidadorPermisos;

@WebServlet(name = "LoginControlador", urlPatterns = {"/LoginControlador"})
public class LoginControlador extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("txtCorreo");
        String pass = request.getParameter("txtContrasena");

        // 2. ENCRIPTA LA CONTRASEÑA ANTES DE VALIDAR
        String passEncriptada = hash.sha256(pass);

        UsuarioDao dao = new UsuarioDao();

        // 3. USA LA CONTRASEÑA ENCRIPTADA PARA VALIDAR
        Usuario user = validarLogin.validarCredenciales(correo, passEncriptada, dao);
        // ---------------------

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", user);
            
            // 🔍 VALIDAR ROL Y PERMISOS PARA REDIRECCIÓN AUTOMÁTICA
            boolean esAdminPorRol = (user.getIdRol() == 1); // ID_rol = 1 = administrador
            boolean tienePermisoAdmin = ValidadorPermisos.tienePermiso(user, ValidadorPermisos.ADMINISTRAR_USUARIOS);
            
            // 🎯 LÓGICA DE REDIRECCIÓN CONDICIONAL
            if (esAdminPorRol && tienePermisoAdmin) {
                // ✅ Usuario es administrador y tiene permiso → Redirigir a administración
                System.out.println("Administrador detectado: " + user.getNombre() + " (ID: " + user.getIdUsuario() + ")");
                System.out.println("Redirigiendo a /AdminControlador");
                response.sendRedirect(request.getContextPath() + "/AdminControlador");
            } else {
                // ✅ Usuario normal → Redirigir al menú principal
                System.out.println("Usuario normal detectado: " + user.getNombre() + " (Rol: " + user.getNombreRol() + ")");
                System.out.println("Redirigiendo a /MenuControlador");
                response.sendRedirect(request.getContextPath() + "/MenuControlador");
            }
        } else {
            // Verificar si el correo existe para dar mensaje específico
            boolean correoExiste = dao.existeCorreo(correo);
            String mensajeError = correoExiste ? "invalid_password" : "user_not_found";
            response.sendRedirect(request.getContextPath() + "/index.jsp?res=" + mensajeError);
        }
    }
}