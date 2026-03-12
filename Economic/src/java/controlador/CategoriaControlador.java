package controlador;
 
import dao.CategoriaDao;
import modelo.Categoria;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet(name = "CategoriaControlador", urlPatterns = {"/CategoriaControlador"})
public class CategoriaControlador extends HttpServlet {
 
    /**
     * GET → Carga la lista de categorías y redirige a la vista.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        CategoriaDao dao = new CategoriaDao();
        List<Categoria> categorias = dao.listar();
 
        // Enviamos la lista a la vista como atributo del request
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/Public/User/configuracion_usuario.jsp")
               .forward(request, response);
    }
 
    /**
     * POST → Gestiona crear, editar y eliminar según el parámetro "accion".
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        String accion = request.getParameter("accion");
        CategoriaDao dao = new CategoriaDao();
 
        switch (accion != null ? accion : "") {
 
            // ── CREAR ──────────────────────────────────────────────────────────
            case "crear": {
                String nombre = request.getParameter("txtNombreCategoria");
                String tipo   = request.getParameter("txtTipoCategoria");
 
                String resultado = utils.validarCategorias.validarCrear(nombre, tipo, dao);
 
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=" + resultado + "&form=crear");
                    return;
                }
 
                Categoria categoria = new Categoria();
                categoria.setNombreCategoria(nombre.trim());
                categoria.setTipoTransaccion(tipo);
 
                if (dao.crear(categoria)) {
                    response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=error&form=crear");
                }
                break;
            }
 
            // ── EDITAR ─────────────────────────────────────────────────────────
            case "editar": {
                String idStr  = request.getParameter("txtIdCategoria");
                String nombre = request.getParameter("txtNombreCategoria");
                String tipo   = request.getParameter("txtTipoCategoria");
 
                int id = Integer.parseInt(idStr);
                String resultado = utils.validarCategorias.validarEditar(nombre, tipo, id, dao);
 
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=" + resultado + "&form=editar&id=" + id);
                    return;
                }
 
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(id);
                categoria.setNombreCategoria(nombre.trim());
                categoria.setTipoTransaccion(tipo);
 
                if (dao.editar(categoria)) {
                    response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=error&form=editar&id=" + id);
                }
                break;
            }
 
            // ── ELIMINAR ───────────────────────────────────────────────────────
            case "eliminar": {
                String idStr = request.getParameter("txtIdCategoria");
                int id = Integer.parseInt(idStr);
 
                if (dao.eliminar(id)) {
                    response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=error_eliminar");
                }
                break;
            }
 
            default:
                response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
        }
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para gestión de categorías (crear, editar, eliminar)";
    }
}