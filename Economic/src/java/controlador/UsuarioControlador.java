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

    @Override
    public String getServletInfo() {
        return "Controlador para el registro de usuarios con seguridad SHA-256";
    }
}