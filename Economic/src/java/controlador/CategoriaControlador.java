package controlador; // Paquete que contiene todos los controladores del sistema
 
// Importaciones necesarias para manejo de categorías
import dao.CategoriaDao;             // Clase para acceder a datos de categorías en la BD
import modelo.Categoria;              // Clase que representa una categoría de transacción
import modelo.Usuario;                // Clase que representa un usuario del sistema
import java.io.IOException;            // Para manejar errores de entrada/salida
import java.util.List;                // Para manejar listas de objetos
import jakarta.servlet.ServletException; // Para errores de servlets
import jakarta.servlet.annotation.WebServlet; // Anotación para configurar servlet
import jakarta.servlet.http.HttpServlet;   // Clase base para servlets HTTP
import jakarta.servlet.http.HttpServletRequest;  // Para manejar peticiones web
import jakarta.servlet.http.HttpServletResponse; // Para manejar respuestas web
import jakarta.servlet.http.HttpSession;      // Para manejar sesiones de usuario
 
/**
 * Controlador que maneja la gestión de categorías de transacciones.
 * 
 * Este servlet permite a los usuarios:
 * - Ver todas sus categorías personales (ingresos y egresos)
 * - Crear nuevas categorías para clasificar sus transacciones
 * - Eliminar categorías que ya no necesita
 * - Validar que las categorías no estén en uso antes de eliminarlas
 * - Mostrar mensajes de éxito o error en las operaciones
 */
@WebServlet(name = "CategoriaControlador", urlPatterns = {"/CategoriaControlador"}) // Configura la URL
public class CategoriaControlador extends HttpServlet { // Hereda de HttpServlet para ser un servlet web
 
    /**
     * Método que se ejecuta cuando el usuario accede a la página de categorías (método GET).
     * 
     * @param request  Objeto que contiene información de la petición
     * @param response Objeto que envía la respuesta al navegador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Lanza excepciones que pueden ocurrir
 
        // Verifica si hay una sesión activa y si el usuario está logueado
        HttpSession session = request.getSession(false); // Obtiene sesión sin crear una nueva
        if (session == null || session.getAttribute("usuario") == null) { // Si no hay sesión o usuario
            response.sendRedirect(request.getContextPath() + "/index.jsp"); // Redirige al login
            return; // Detiene la ejecución
        }
 
        // Obtiene el usuario que está logueado
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario(); // Obtiene el ID del usuario
 
        // Carga todas las categorías personales del usuario
        CategoriaDao categoriaDao = new CategoriaDao(); // Crea objeto para acceder a categorías
        List<Categoria> categorias = categoriaDao.listarPorUsuario(idUsuario); // Obtiene categorías del usuario
 
        // Envía las categorías a la página JSP para mostrarlas
        request.setAttribute("categorias", categorias); // Guarda las categorías como atributo
        
        // Redirige a la página de configuración de usuario donde se muestran las categorías
        request.getRequestDispatcher("/Public/User/configuracion_usuario.jsp") // Obtiene la página JSP
               .forward(request, response); // Reenvía la petición a la página JSP
    }
 
    /**
     * Método que se ejecuta cuando el usuario envía un formulario de categorías (método POST).
     * 
     * @param request  Objeto que contiene los datos del formulario
     * @param response Objeto que envía la respuesta al navegador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Lanza excepciones que pueden ocurrir
 
        // Configura la codificación para leer correctamente acentos y caracteres especiales
        request.setCharacterEncoding("UTF-8");
 
        // Verifica si hay una sesión activa y si el usuario está logueado
        HttpSession session = request.getSession(false); // Obtiene sesión sin crear una nueva
        if (session == null || session.getAttribute("usuario") == null) { // Si no hay sesión o usuario
            response.sendRedirect(request.getContextPath() + "/index.jsp"); // Redirige al login
            return; // Detiene la ejecución
        }
 
        // Obtiene el usuario que está logueado
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario(); // Obtiene el ID del usuario
 
        // Obtiene la acción a realizar (crear o eliminar categoría)
        String accion = request.getParameter("accion");
        CategoriaDao dao = new CategoriaDao(); // Crea objeto para acceder a la base de datos
 
        // Usa un switch para manejar diferentes acciones
        switch (accion != null ? accion : "") { // Si accion es null, usa cadena vacía
 
            // Caso para crear una nueva categoría
            case "crear": {
                // Lee los datos del formulario de crear categoría
                String nombre = request.getParameter("txtNombreCategoria"); // Lee el nombre de la categoría
                String tipo   = request.getParameter("txtTipoCategoria"); // Lee el tipo (Ingreso/Egreso)
 
                // Valida los datos antes de crear la categoría
                String resultado = utils.validarCategorias.validarCrear(nombre, tipo, idUsuario, dao);
 
                // Si la validación falló
                if (!resultado.equals("ok")) {
                    response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                            + "/CategoriaControlador?res=" + resultado + "&form=crear");
                    return; // Detiene la ejecución
                }
 
                // Crea un objeto Categoria con los datos validados
                Categoria categoria = new Categoria(); // Crea nueva categoría
                categoria.setNombreCategoria(nombre.trim()); // Asigna el nombre (sin espacios extra)
                categoria.setTipoTransaccion(tipo); // Asigna el tipo (Ingreso o Egreso)
                categoria.setIdUsuario(idUsuario); // Asigna el ID del usuario dueño
 
                // Intenta guardar la categoría en la base de datos
                if (dao.crear(categoria)) { // Si la categoría se creó exitosamente
                    // Vuelve a cargar todas las categorías para mostrarlas actualizadas
                    List<Categoria> categorias = dao.listarPorUsuario(idUsuario); // Obtiene categorías actualizadas
                    
                    // Prepara los datos para mostrar en la página de éxito
                    request.setAttribute("categorias",       categorias); // Envía las categorías actualizadas
                    request.setAttribute("categoriaExitosa", "exitosa"); // Marca que la creación fue exitosa
                    
                    // Muestra la página con mensaje de éxito
                    request.getRequestDispatcher("/Public/User/configuracion_usuario.jsp") // Obtiene la página
                           .forward(request, response); // Reenvía a la página con mensaje de éxito
                } else { // Si hubo error al crear la categoría
                    response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                            + "/CategoriaControlador?res=error&form=crear");
                }
                break; // Sale del switch
            }
 
            // Caso para eliminar una categoría existente
            case "eliminar": {
                // Lee el ID de la categoría a eliminar
                String idStr = request.getParameter("txtIdCategoria"); // Lee el ID como texto
                int id = Integer.parseInt(idStr); // Convierte el texto a número entero
 
                // Valida que la categoría no esté siendo usada por transacciones
                String resultado = utils.validarCategorias.validarEliminar(id, idUsuario, dao);
                if (!resultado.equals("ok")) { // Si la categoría está en uso
                    response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                            + "/CategoriaControlador?res=" + resultado);
                    return; // Detiene la ejecución
                }
 
                // Intenta eliminar la categoría de la base de datos
                if (dao.eliminar(id, idUsuario)) { // Si la eliminación fue exitosa
                    response.sendRedirect(request.getContextPath() + "/CategoriaControlador"); // Recarga la página
                } else { // Si hubo error al eliminar
                    response.sendRedirect(request.getContextPath() // Redirige con mensaje de error
                            + "/CategoriaControlador?res=error_eliminar");
                }
                break; // Sale del switch
            }
 
            // Caso por defecto si no se reconoce la acción
            default:
                response.sendRedirect(request.getContextPath() + "/CategoriaControlador"); // Recarga la página
        }
    }
 
    /**
     * Método que devuelve información sobre este servlet.
     * 
     * @return Descripción del propósito del controlador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    public String getServletInfo() {
        return "Controlador para gestión de categorías por usuario"; // Describe lo que hace este servlet
    }
}