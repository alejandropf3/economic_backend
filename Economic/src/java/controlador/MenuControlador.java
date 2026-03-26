package controlador;
 
import dao.ResumenDao;
import dao.TransaccionDao;
import modelo.ResumenSemanal;
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
 
    // Cantidad de resúmenes semanales a mostrar en el menú
    private static final int RESUMENES_LIMITE = 3;
 
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
 
        TransaccionDao transaccionDao = new TransaccionDao();
 
        // ── Últimas transacciones por tipo ────────────────────────────────────
        Transaccion ultimoIngreso = transaccionDao.obtenerUltimaPorTipo(idUsuario, "Ingreso");
        Transaccion ultimoEgreso  = transaccionDao.obtenerUltimaPorTipo(idUsuario, "Egreso");
 
        // ── Balance actual ────────────────────────────────────────────────────
        java.math.BigDecimal[] totales = transaccionDao.obtenerTotales(idUsuario);
        java.math.BigDecimal balance = totales[0].subtract(totales[1]);
 
        // ── Últimos resúmenes semanales ───────────────────────────────────────
        ResumenDao resumenDao = new ResumenDao();
        List<ResumenSemanal> ultimosResumenes =
                resumenDao.obtenerUltimosSemanales(idUsuario, RESUMENES_LIMITE);
 
        request.setAttribute("ultimoIngreso",    ultimoIngreso);
        request.setAttribute("ultimoEgreso",     ultimoEgreso);
        request.setAttribute("balance",          balance);
        request.setAttribute("ultimosResumenes", ultimosResumenes);
 
        request.getRequestDispatcher("/Public/User/menu_principal.jsp")
               .forward(request, response);
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para el menú principal";
    }
}