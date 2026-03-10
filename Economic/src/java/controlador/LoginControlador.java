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
            response.sendRedirect(request.getContextPath() + "/Public/User/menu_principal.jsp");
        } else {
            // Verificar si el correo existe para dar mensaje específico
            boolean correoExiste = dao.existeCorreo(correo);
            String mensajeError = correoExiste ? "invalid_password" : "user_not_found";
            response.sendRedirect(request.getContextPath() + "/index.jsp?res=" + mensajeError);
        }
    }
}