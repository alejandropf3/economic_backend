package controlador;
 
import dao.ResumenDao;
import modelo.ResumenAnual;
import modelo.ResumenMensual;
import modelo.ResumenSemanal;
import modelo.Usuario;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "ResumenControlador", urlPatterns = {"/ResumenControlador"})
public class ResumenControlador extends HttpServlet {
 
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
 
        ResumenDao dao = new ResumenDao();
 
        // ── Parámetros de navegación ──────────────────────────────────────────
        String vistaParam  = request.getParameter("vista");   // "semanal","mensual","anual"
        String anioParam   = request.getParameter("anio");
        String mesParam    = request.getParameter("mes");
        String fechaParam  = request.getParameter("fecha");   // para vista semanal
 
        String vista = (vistaParam != null) ? vistaParam : "semanal";
        int anioActual = LocalDate.now().getYear();
        int anio = (anioParam != null && !anioParam.isEmpty())
                   ? Integer.parseInt(anioParam) : anioActual;
 
        // ── Años con transacciones (para el selector) ─────────────────────────
        List<Integer> aniosDisponibles = dao.obtenerAniosConTransacciones(idUsuario);
        if (!aniosDisponibles.contains(anioActual)) aniosDisponibles.add(0, anioActual);
 
        request.setAttribute("vista",            vista);
        request.setAttribute("anio",             anio);
        request.setAttribute("aniosDisponibles", aniosDisponibles);
 
        switch (vista) {
 
            case "semanal": {
                LocalDate fecha = (fechaParam != null && !fechaParam.isEmpty())
                                  ? LocalDate.parse(fechaParam) : LocalDate.now();
                ResumenSemanal resumenSemanal = dao.calcularSemanal(idUsuario, fecha);
                request.setAttribute("resumenSemanal", resumenSemanal);
                request.setAttribute("fechaSeleccionada", fecha.toString());
                break;
            }
 
            case "mensual": {
                int mes = (mesParam != null && !mesParam.isEmpty())
                          ? Integer.parseInt(mesParam) : LocalDate.now().getMonthValue();
                ResumenMensual resumenMensual = dao.calcularMensual(idUsuario, mes, anio);
                request.setAttribute("resumenMensual", resumenMensual);
                request.setAttribute("mesSeleccionado", mes);
                break;
            }
 
            case "anual": {
                ResumenAnual resumenAnual = dao.calcularAnual(idUsuario, anio);
                request.setAttribute("resumenAnual", resumenAnual);
                break;
            }
 
            default: {
                LocalDate fecha = LocalDate.now();
                ResumenSemanal resumenSemanal = dao.calcularSemanal(idUsuario, fecha);
                request.setAttribute("resumenSemanal", resumenSemanal);
                request.setAttribute("fechaSeleccionada", fecha.toString());
                request.setAttribute("vista", "semanal");
            }
        }
 
        request.getRequestDispatcher("/Public/User/historial_resumenes.jsp")
               .forward(request, response);
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para historial de resúmenes";
    }
}