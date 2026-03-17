package controlador;
 
import dao.ImagenDao;
import modelo.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
 
@WebServlet(name = "ImagenControlador", urlPatterns = {"/ImagenControlador"})
@MultipartConfig(
    maxFileSize    = 2 * 1024 * 1024,   // 2 MB máximo por archivo
    maxRequestSize = 3 * 1024 * 1024    // 3 MB máximo por request
)
public class ImagenControlador extends HttpServlet {
 
    // Extensiones permitidas
    private static final String[] EXTENSIONES_PERMITIDAS = {"jpg", "jpeg", "png", "webp"};
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("UTF-8");
 
        // 1. Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
 
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        long idUsuario = usuarioSesion.getIdUsuario();
 
        // 2. Obtener el archivo del input
        Part archivo = request.getPart("foto_usuario");
 
        if (archivo == null || archivo.getSize() == 0) {
            response.sendRedirect(request.getContextPath()
                    + "/CategoriaControlador?resImagen=vacio");
            return;
        }
 
        // 3. Validar extensión
        String nombreOriginal = Paths.get(archivo.getSubmittedFileName()).getFileName().toString();
        String extension = obtenerExtension(nombreOriginal);
 
        if (!extensionPermitida(extension)) {
            response.sendRedirect(request.getContextPath()
                    + "/CategoriaControlador?resImagen=formato_invalido");
            return;
        }
 
        // 4. Generar nombre único para evitar colisiones
        String nombreArchivo = "perfil_" + idUsuario + "_" + UUID.randomUUID() + "." + extension;
 
        // 5. Carpeta de destino dentro del proyecto
        String carpetaImagenes = getServletContext().getRealPath("/Assets/Imagenes/perfiles/");
        File directorio = new File(carpetaImagenes);
        if (!directorio.exists()) directorio.mkdirs();
 
        // 6. Guardar archivo en el servidor
        String rutaCompleta = carpetaImagenes + File.separator + nombreArchivo;
        try (InputStream input = archivo.getInputStream()) {
            Files.copy(input, Paths.get(rutaCompleta), StandardCopyOption.REPLACE_EXISTING);
        }
 
        // 7. Guardar URL relativa en la BD
        String urlRelativa = request.getContextPath() + "/Assets/Imagenes/perfiles/" + nombreArchivo;
        ImagenDao dao = new ImagenDao();
        boolean guardado = dao.guardarImagen(idUsuario, urlRelativa);
 
        if (guardado) {
            // 8. Actualizar objeto en sesión con la nueva URL
            usuarioSesion.setUrlImagen(urlRelativa);
            session.setAttribute("usuario", usuarioSesion);
            response.sendRedirect(request.getContextPath()
                    + "/CategoriaControlador?resImagen=ok");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/CategoriaControlador?resImagen=error");
        }
    }
 
    private String obtenerExtension(String nombreArchivo) {
        int punto = nombreArchivo.lastIndexOf('.');
        if (punto == -1) return "";
        return nombreArchivo.substring(punto + 1).toLowerCase();
    }
 
    private boolean extensionPermitida(String extension) {
        for (String ext : EXTENSIONES_PERMITIDAS) {
            if (ext.equals(extension)) return true;
        }
        return false;
    }
 
    @Override
    public String getServletInfo() {
        return "Controlador para subida de imagen de perfil";
    }
}