package controlador; // Paquete que contiene todos los controladores del sistema
 
// Importaciones necesarias para manejar transacciones financieras
import dao.CategoriaDao;             // Clase para acceder a datos de categorías
import dao.TransaccionDao;           // Clase para acceder a datos de transacciones
import modelo.Categoria;              // Clase que representa una categoría de gasto/ingreso
import modelo.Transaccion;            // Clase que representa una transacción financiera
import modelo.Usuario;                // Clase que representa un usuario del sistema
import java.io.IOException;            // Para manejar errores de entrada/salida
import java.math.BigDecimal;          // Para manejar dinero con precisión exacta
import java.time.LocalDate;           // Para manejar fechas sin problemas de zona horaria
import java.util.List;                // Para manejar listas de objetos
import jakarta.servlet.ServletException; // Para errores de servlets
import jakarta.servlet.annotation.WebServlet; // Anotación para configurar servlet
import jakarta.servlet.http.HttpServlet;   // Clase base para servlets HTTP
import jakarta.servlet.http.HttpServletRequest;  // Para manejar peticiones web
import jakarta.servlet.http.HttpServletResponse; // Para manejar respuestas web
import jakarta.servlet.http.HttpSession;      // Para manejar sesiones de usuario
 
/**
 * Controlador que maneja el registro de transacciones financieras.
 * 
 * Este servlet permite a los usuarios:
 * - Ver el formulario para registrar nuevas transacciones
 * - Validar los datos ingresados (monto, categoría, fecha)
 * - Guardar transacciones de ingresos y egresos
 * - Mostrar mensajes de éxito o error
 * - Cargar las categorías personales del usuario
 */
@WebServlet(name = "TransaccionControlador", urlPatterns = {"/TransaccionControlador"}) // Configura la URL
public class TransaccionControlador extends HttpServlet { // Hereda de HttpServlet para ser un servlet web
 
    /**
     * Método que se ejecuta cuando el usuario quiere ver el formulario de registro (método GET).
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
 
        // Carga las categorías personales del usuario
        CategoriaDao categoriaDao = new CategoriaDao(); // Crea objeto para acceder a categorías
        List<Categoria> categorias = categoriaDao.listarPorUsuario(usuarioSesion.getIdUsuario()); // Obtiene categorías del usuario
 
        // Envía las categorías a la página JSP para mostrarlas en el formulario
        request.setAttribute("categorias", categorias); // Guarda las categorías como atributo de la petición
        
        // Redirige a la página del formulario de registro de transacciones
        request.getRequestDispatcher("/Public/User/realizar_registro.jsp") // Obtiene la página JSP
               .forward(request, response); // Reenvía la petición a la página JSP
    }
 
    /**
     * Método que se ejecuta cuando el usuario envía el formulario de transacción (método POST).
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
 
        // Lee los datos del formulario de transacción
        String valorStr     = request.getParameter("valor");        // Monto de la transacción
        String categoriaStr = request.getParameter("categoria");    // Categoría seleccionada
        String descripcion  = request.getParameter("descripcion"); // Descripción opcional
        String fechaStr     = request.getParameter("fecha");        // Fecha de la transacción
 
        // Valida que los datos ingresados sean correctos
        String resultado = utils.validarTransaccion.validar(valorStr, categoriaStr, fechaStr);
        if (!resultado.equals("ok")) { // Si la validación falló
            response.sendRedirect(request.getContextPath() // Redirige al formulario con mensaje de error
                    + "/TransaccionControlador?res=" + resultado);
            return; // Detiene la ejecución
        }
 
        // Crea un objeto Transaccion con los datos validados
        Transaccion t = new Transaccion(); // Crea nueva transacción
        t.setIdUsuario(usuarioSesion.getIdUsuario()); // Asigna el ID del usuario logueado
        t.setIdCategoria(Integer.parseInt(categoriaStr)); // Asigna la categoría seleccionada
        t.setValorTransaccion(new BigDecimal(valorStr)); // Asigna el monto (con precisión decimal)
        t.setDescripcion(descripcion != null ? descripcion.trim() : null); // Asigna descripción si existe
        t.setFechaRealizacion(LocalDate.parse(fechaStr)); // Asigna la fecha (convierte texto a fecha)
 
        // Guarda la transacción en la base de datos
        TransaccionDao dao = new TransaccionDao(); // Crea objeto para acceder a datos de transacciones
        if (dao.crear(t)) { // Si la transacción se guardó exitosamente
            // Vuelve a cargar las categorías para mostrarlas en la página de éxito
            CategoriaDao categoriaDao = new CategoriaDao(); // Crea objeto para categorías
            List<Categoria> categorias = categoriaDao.listarPorUsuario(usuarioSesion.getIdUsuario()); // Obtiene categorías
            
            // Prepara los datos para mostrar en la página de éxito
            request.setAttribute("categorias",          categorias); // Envía las categorías
            request.setAttribute("transaccionExitosa",  "exitosa"); // Marca que la transacción fue exitosa
            
            // Muestra la página de éxito
            request.getRequestDispatcher("/Public/User/realizar_registro.jsp") // Obtiene la página
                   .forward(request, response); // Reenvía a la página con mensaje de éxito
        } else { // Si hubo error al guardar la transacción
            // Redirige al formulario con mensaje de error
            response.sendRedirect(request.getContextPath() + "/TransaccionControlador?res=error");
        }
    }
 
    /**
     * Método que devuelve información sobre este servlet.
     * 
     * @return Descripción del propósito del controlador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    public String getServletInfo() {
        return "Controlador para registro de transacciones"; // Describe lo que hace este servlet
    }
}