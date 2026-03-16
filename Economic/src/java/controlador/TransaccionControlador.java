package controlador;
 
import dao.CategoriaDao;
import dao.TransaccionDao;
import modelo.Categoria;
import modelo.Transaccion;
import modelo.Usuario;
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
 
@WebServlet(name = "TransaccionControlador", urlPatterns = {"/TransaccionControlador"})
public class TransaccionControlador extends HttpServlet {
 
    // GET → carga categorías del usuario y redirige a la vista
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
 
        // Cargar categorías del usuario para el select
        CategoriaDao categoriaDao = new CategoriaDao();
        List<Categoria> categorias = categoriaDao.listarPorUsuario(usuarioSesion.getIdUsuario());
 
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/Public/User/realizar_registro.jsp")
               .forward(request, response);
    }
 
    // POST → guardar la transacción
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
 
        String valorStr     = request.getParameter("valor");
        String categoriaStr = request.getParameter("categoria");
        String descripcion  = request.getParameter("descripcion");
        String fechaStr     = request.getParameter("fecha");
 
        // Validar campos
        String resultado = utils.validarTransaccion.validar(valorStr, categoriaStr, fechaStr);
        if (!resultado.equals("ok")) {
            response.sendRedirect(request.getContextPath()
                    + "/TransaccionControlador?res=" + resultado);
            return;
        }
 
        Transaccion t = new Transaccion();
        t.setIdUsuario(usuarioSesion.getIdUsuario());
        t.setIdCategoria(Integer.parseInt(categoriaStr));
        t.setValorTransaccion(new BigDecimal(valorStr));
        t.setDescripcion(descripcion != null ? descripcion.trim() : null);
        t.setFechaRealizacion(LocalDate.parse(fechaStr));
 
        TransaccionDao dao = new TransaccionDao();
        if (dao.crear(t)) {
            response.sendRedirect(request.getContextPath()
                    + "/TransaccionControlador?res=ok");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/TransaccionControlador?res=error");
        }
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para registro de transacciones";
    }
}