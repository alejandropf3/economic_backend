package controlador;
 
import dao.CategoriaDao;
import dao.TransaccionDao;
import modelo.Categoria;
import modelo.Transaccion;
import modelo.Usuario;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "HistorialControlador", urlPatterns = {"/HistorialControlador"})
public class HistorialControlador extends HttpServlet {
 
    // ── GET → cargar historial con filtros ────────────────────────────────────
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
 
        // Leer filtros de la URL
        String tipo        = request.getParameter("tipo");
        String idCategoria = request.getParameter("categoria");
        String fechaDesde  = request.getParameter("fechaDesde");
        String fechaHasta  = request.getParameter("fechaHasta");
 
        TransaccionDao transaccionDao = new TransaccionDao();
        CategoriaDao categoriaDao    = new CategoriaDao();
 
        // Cargar transacciones con filtros
        List<Transaccion> transacciones = transaccionDao.listarPorUsuario(
                idUsuario, tipo, idCategoria, fechaDesde, fechaHasta);
 
        // Cargar categorías del usuario para el filtro y modal editar
        List<Categoria> categorias = categoriaDao.listarPorUsuario(idUsuario);
 
        // Calcular totales generales (sin filtros)
        BigDecimal[] totales = transaccionDao.obtenerTotales(idUsuario);
        BigDecimal totalIngresos = totales[0];
        BigDecimal totalEgresos  = totales[1];
        BigDecimal balance       = totalIngresos.subtract(totalEgresos);
 
        request.setAttribute("transacciones",  transacciones);
        request.setAttribute("categorias",     categorias);
        request.setAttribute("totalIngresos",  totalIngresos);
        request.setAttribute("totalEgresos",   totalEgresos);
        request.setAttribute("balance",        balance);
 
        // Mantener filtros activos en la vista
        request.setAttribute("filtroTipo",       tipo        != null ? tipo        : "");
        request.setAttribute("filtroCategoria",  idCategoria != null ? idCategoria : "");
        request.setAttribute("filtroFechaDesde", fechaDesde  != null ? fechaDesde  : "");
        request.setAttribute("filtroFechaHasta", fechaHasta  != null ? fechaHasta  : "");
 
        request.getRequestDispatcher("/Public/User/historial_transacciones.jsp")
               .forward(request, response);
    }
 
    // ── POST → editar o eliminar transacción ──────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario();
 
        String accion = request.getParameter("accion");
        TransaccionDao dao = new TransaccionDao();
 
        switch (accion != null ? accion : "") {
 
            case "editar": {
                String idStr       = request.getParameter("txtIdTransaccion");
                String categoriaStr = request.getParameter("txtCategoriaTransaccion");
                String valorStr    = request.getParameter("txtValorTransaccion");
                String descripcion = request.getParameter("txtDescripcionTransaccion");
                String fechaStr    = request.getParameter("txtFechaTransaccion");
 
                String resultado = utils.validarTransaccion.validar(valorStr, categoriaStr, fechaStr);
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath()
                            + "/HistorialControlador?res=" + resultado);
                    return;
                }
 
                Transaccion t = new Transaccion();
                t.setIdTransaccion(Long.parseLong(idStr));
                t.setIdUsuario(idUsuario);
                t.setIdCategoria(Integer.parseInt(categoriaStr));
                t.setValorTransaccion(new BigDecimal(valorStr));
                t.setDescripcion(descripcion != null ? descripcion.trim() : null);
                t.setFechaRealizacion(java.time.LocalDate.parse(fechaStr));
 
                if (dao.editar(t)) {
                    response.sendRedirect(request.getContextPath() + "/HistorialControlador?res=ok_editar");
                } else {
                    response.sendRedirect(request.getContextPath() + "/HistorialControlador?res=error");
                }
                break;
            }
 
            case "eliminar": {
                String idStr = request.getParameter("txtIdTransaccion");
                long id = Long.parseLong(idStr);
                if (dao.eliminar(id, idUsuario)) {
                    response.sendRedirect(request.getContextPath() + "/HistorialControlador?res=ok_eliminar");
                } else {
                    response.sendRedirect(request.getContextPath() + "/HistorialControlador?res=error");
                }
                break;
            }
 
            default:
                response.sendRedirect(request.getContextPath() + "/HistorialControlador");
        }
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para historial de transacciones";
    }
}