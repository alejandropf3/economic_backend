package controlador;
 
import dao.MetaDao;
import modelo.Meta;
import modelo.Usuario;
import utils.validarMeta;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "MetaControlador", urlPatterns = {"/MetaControlador"})
public class MetaControlador extends HttpServlet {
 
    // ── GET → cargar lista de metas ───────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        cargarMetas(request, response);
    }
 
    // ── POST → crear / editar / eliminar / abonar ─────────────────────────────
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
 
        switch (accion != null ? accion : "") {
            case "crear":    procesarCrear(request, response, idUsuario);   break;
            case "editar":   procesarEditar(request, response, idUsuario);  break;
            case "eliminar": procesarEliminar(request, response, idUsuario); break;
            case "abonar":   procesarAbonar(request, response, idUsuario);  break;
            default:
                response.sendRedirect(request.getContextPath() + "/MetaControlador");
        }
    }
 
    // ── CREAR ─────────────────────────────────────────────────────────────────
    private void procesarCrear(HttpServletRequest request, HttpServletResponse response,
                                long idUsuario) throws IOException, ServletException {
 
        String nombre   = request.getParameter("nombre");
        String montoStr = request.getParameter("montoObjetivo");
        String fechaStr = request.getParameter("fechaLimite");
 
        String resultado = validarMeta.validarCreacion(nombre, montoStr, fechaStr, idUsuario);
 
        if (!resultado.equals("ok")) {
            request.setAttribute("errorCrear", resultado);
            cargarMetas(request, response);
            return;
        }
 
        Meta meta = new Meta();
        meta.setIdUsuario(idUsuario);
        meta.setNombreMeta(nombre.trim());
        meta.setMontoObjetivo(new BigDecimal(montoStr.trim()));
        meta.setFechaLimite(LocalDate.parse(fechaStr.trim()));
 
        MetaDao dao = new MetaDao();
        if (dao.crear(meta)) {
            request.setAttribute("resCrear", "ok");
        } else {
            request.setAttribute("errorCrear", "error_general");
        }
        cargarMetas(request, response);
    }
 
    // ── EDITAR ────────────────────────────────────────────────────────────────
    private void procesarEditar(HttpServletRequest request, HttpServletResponse response,
                                 long idUsuario) throws IOException, ServletException {
 
        String idStr    = request.getParameter("idMeta");
        String nombre   = request.getParameter("nombre");
        String fechaStr = request.getParameter("fechaLimite");
 
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MetaControlador");
            return;
        }
 
        long idMeta = Long.parseLong(idStr.trim());
        String resultado = validarMeta.validarEdicion(nombre, fechaStr, idUsuario, idMeta);
 
        if (!resultado.equals("ok")) {
            request.setAttribute("errorEditar", resultado);
            request.setAttribute("idMetaError", idMeta);
            cargarMetas(request, response);
            return;
        }
 
        Meta meta = new Meta();
        meta.setIdMeta(idMeta);
        meta.setIdUsuario(idUsuario);
        meta.setNombreMeta(nombre.trim());
        meta.setFechaLimite(LocalDate.parse(fechaStr.trim()));
 
        MetaDao dao = new MetaDao();
        if (dao.editar(meta)) {
            request.setAttribute("resEditar", "ok");
        } else {
            request.setAttribute("errorEditar", "error_general");
        }
        cargarMetas(request, response);
    }
 
    // ── ELIMINAR ──────────────────────────────────────────────────────────────
    private void procesarEliminar(HttpServletRequest request, HttpServletResponse response,
                                   long idUsuario) throws IOException, ServletException {
 
        String idStr         = request.getParameter("idMeta");
        String confirmacionStr = request.getParameter("confirmado"); // "true" si el usuario confirmó
 
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MetaControlador");
            return;
        }
 
        long idMeta = Long.parseLong(idStr.trim());
        MetaDao dao = new MetaDao();
        BigDecimal montoActual = dao.obtenerMontoActual(idMeta, idUsuario);
 
        // RF14 restricción 1: si tiene fondos y no fue confirmado → error
        if (montoActual.compareTo(BigDecimal.ZERO) > 0
                && !"true".equals(confirmacionStr)) {
            request.setAttribute("errorEliminar", "requiere_confirmacion");
            request.setAttribute("idMetaEliminar", idMeta);
            request.setAttribute("montoMetaEliminar", montoActual);
            cargarMetas(request, response);
            return;
        }
 
        if (dao.eliminar(idMeta, idUsuario)) {
            request.setAttribute("resEliminar", "ok");
        } else {
            request.setAttribute("errorEliminar", "error_general");
        }
        cargarMetas(request, response);
    }
 
    // ── ABONAR ────────────────────────────────────────────────────────────────
    private void procesarAbonar(HttpServletRequest request, HttpServletResponse response,
                                 long idUsuario) throws IOException, ServletException {
 
        String idStr    = request.getParameter("idMeta");
        String montoStr = request.getParameter("montoAbono");
 
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MetaControlador");
            return;
        }
 
        long idMeta = Long.parseLong(idStr.trim());
        String resultado = validarMeta.validarAbono(montoStr);
 
        if (!resultado.equals("ok")) {
            request.setAttribute("errorAbonar", resultado);
            request.setAttribute("idMetaAbonar", idMeta);
            cargarMetas(request, response);
            return;
        }
 
        MetaDao dao = new MetaDao();
        if (dao.abonar(idMeta, idUsuario, new BigDecimal(montoStr.trim()))) {
            request.setAttribute("resAbonar", "ok");
        } else {
            request.setAttribute("errorAbonar", "error_general");
        }
        cargarMetas(request, response);
    }
 
    // ── HELPER: cargar lista y hacer forward al JSP ───────────────────────────
    private void cargarMetas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        Usuario usuarioSesion = (Usuario) ((HttpSession) request.getSession(false))
                                            .getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario();
 
        MetaDao dao = new MetaDao();
        List<Meta> metas = dao.listarPorUsuario(idUsuario);
        request.setAttribute("metas", metas);
 
        request.getRequestDispatcher("/Public/User/metas_ahorro.jsp")
               .forward(request, response);
    }
}