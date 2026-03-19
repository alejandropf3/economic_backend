package controlador;
 
import dao.CategoriaDao;
import dao.ImagenDao;
import modelo.Categoria;
import modelo.Usuario;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
@WebServlet(name = "CategoriaControlador", urlPatterns = {"/CategoriaControlador"})
public class CategoriaControlador extends HttpServlet {
 
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
 
        CategoriaDao categoriaDao = new CategoriaDao();
        List<Categoria> categorias = categoriaDao.listarPorUsuario(idUsuario);
 
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/Public/User/configuracion_usuario.jsp")
               .forward(request, response);
    }
 
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
        CategoriaDao dao = new CategoriaDao();
 
        switch (accion != null ? accion : "") {
 
            case "crear": {
                String nombre = request.getParameter("txtNombreCategoria");
                String tipo   = request.getParameter("txtTipoCategoria");
 
                String resultado = utils.validarCategorias.validarCrear(nombre, tipo, idUsuario, dao);
 
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=" + resultado + "&form=crear");
                    return;
                }
 
                Categoria categoria = new Categoria();
                categoria.setNombreCategoria(nombre.trim());
                categoria.setTipoTransaccion(tipo);
                categoria.setIdUsuario(idUsuario);
 
                if (dao.crear(categoria)) {
                    // Éxito — forward con flag para mostrar ventana de confirmación
                    List<Categoria> categorias = dao.listarPorUsuario(idUsuario);
                    request.setAttribute("categorias",        categorias);
                    request.setAttribute("categoriaExitosa",  "exitosa");
                    request.getRequestDispatcher("/Public/User/configuracion_usuario.jsp")
                           .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=error&form=crear");
                }
                break;
            }
 
            case "editar": {
                String idStr  = request.getParameter("txtIdCategoria");
                String nombre = request.getParameter("txtNombreCategoria");
                String tipo   = request.getParameter("txtTipoCategoria");
                int id = Integer.parseInt(idStr);
 
                String resultado = utils.validarCategorias.validarEditar(
                        nombre, tipo, id, idUsuario, dao);
 
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=" + resultado + "&form=editar&id=" + id);
                    return;
                }
 
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(id);
                categoria.setNombreCategoria(nombre.trim());
                categoria.setTipoTransaccion(tipo);
                categoria.setIdUsuario(idUsuario);
 
                if (dao.editar(categoria)) {
                    response.sendRedirect(request.getContextPath() + "/CategoriaControlador");
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/CategoriaControlador?res=error&form=editar&id=" + id);
                }
                break;
            }
 
            case "eliminar": {
                String idStr = request.getParameter("txtIdCategoria");
                int id = Integer.parseInt(idStr);
 
                if (dao.eliminar(id, idUsuario)) {
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
        return "Controlador para gestión de categorías por usuario";
    }
}