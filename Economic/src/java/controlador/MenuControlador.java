package controlador; // Paquete que contiene todos los controladores del sistema
 
// Importaciones necesarias para el menú principal del usuario
import dao.ResumenDao;               // Clase para acceder a datos de resúmenes financieros
import dao.TransaccionDao;           // Clase para acceder a datos de transacciones
import modelo.ResumenSemanal;         // Clase que representa un resumen semanal de finanzas
import modelo.Transaccion;            // Clase que representa una transacción financiera
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
 * Controlador que maneja el menú principal del usuario.
 * 
 * Este servlet muestra el dashboard del usuario con:
 * - Últimas transacciones de ingresos y egresos
 * - Balance financiero actual (ingresos - egresos)
 * - Resúmenes semanales recientes
 * - Información financiera importante en el panel principal
 */
@WebServlet(name = "MenuControlador", urlPatterns = {"/MenuControlador"}) // Configura la URL
public class MenuControlador extends HttpServlet { // Hereda de HttpServlet para ser un servlet web
 
    // Constante que define cuántos resúmenes semanales mostrar en el menú
    private static final int RESUMENES_LIMITE = 3; // Muestra los últimos 3 resúmenes
 
    /**
     * Método que se ejecuta cuando el usuario accede al menú principal (método GET).
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
 
        // Crea objeto para acceder a los datos de transacciones
        TransaccionDao transaccionDao = new TransaccionDao();
 
        // Obtiene la última transacción de ingreso del usuario
        Transaccion ultimoIngreso = transaccionDao.obtenerUltimaPorTipo(idUsuario, "Ingreso");
        // Obtiene la última transacción de egreso del usuario
        Transaccion ultimoEgreso  = transaccionDao.obtenerUltimaPorTipo(idUsuario, "Egreso");
 
        // Calcula el balance actual del usuario (ingresos - egresos)
        java.math.BigDecimal[] totales = transaccionDao.obtenerTotales(idUsuario); // Obtiene [ingresos, egresos]
        java.math.BigDecimal balance = totales[0].subtract(totales[1]); // Resta egresos a los ingresos
 
        // Obtiene los últimos resúmenes semanales del usuario
        ResumenDao resumenDao = new ResumenDao(); // Crea objeto para acceder a resúmenes
        List<ResumenSemanal> ultimosResumenes = // Lista para guardar los resúmenes
                resumenDao.obtenerUltimosSemanales(idUsuario, RESUMENES_LIMITE); // Obtiene los últimos 3 resúmenes
 
        // Prepara todos los datos para enviarlos a la página JSP
        request.setAttribute("ultimoIngreso",    ultimoIngreso);    // Envía el último ingreso
        request.setAttribute("ultimoEgreso",     ultimoEgreso);     // Envía el último egreso
        request.setAttribute("balance",          balance);          // Envía el balance calculado
        request.setAttribute("ultimosResumenes", ultimosResumenes); // Envía los resúmenes semanales
 
        // Redirige a la página del menú principal con todos los datos
        request.getRequestDispatcher("/Public/User/menu_principal.jsp") // Obtiene la página JSP
               .forward(request, response); // Reenvía la petición a la página JSP
    }
 
    /**
     * Método que devuelve información sobre este servlet.
     * 
     * @return Descripción del propósito del controlador
     */
    @Override // Indica que sobreescribe un método de la clase padre
    public String getServletInfo() {
        return "Controlador para el menú principal"; // Describe lo que hace este servlet
    }
}