package controlador;
 
import dao.TransaccionDao;
import modelo.Transaccion;
import modelo.Usuario;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "MenuControlador", urlPatterns = {"/MenuControlador"})
public class MenuControlador extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario();
 
        TransaccionDao dao = new TransaccionDao();
 
        // Obtener última transacción de tipo Ingreso
        Transaccion ultimoIngreso = dao.obtenerUltimaPorTipo(idUsuario, "Ingreso");
 
        // Obtener última transacción de tipo Egreso
        Transaccion ultimoEgreso = dao.obtenerUltimaPorTipo(idUsuario, "Egreso");
 
        // Obtener balance actual del usuario
        java.math.BigDecimal[] totales = dao.obtenerTotales(idUsuario);
        java.math.BigDecimal balance = totales[0].subtract(totales[1]);
 
        request.setAttribute("ultimoIngreso", ultimoIngreso);
        request.setAttribute("ultimoEgreso",  ultimoEgreso);
        request.setAttribute("balance",        balance);
 
        request.getRequestDispatcher("/Public/User/menu_principal.jsp")
               .forward(request, response);
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para el menú principal";
    }
}