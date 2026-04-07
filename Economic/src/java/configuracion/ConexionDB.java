package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos MySQL del sistema Economic.
 * <p>
 * Esta clase es el pilar fundamental de la persistencia de datos del sistema.
 * Se crea como un singleton implícito porque necesitamos un punto centralizado
 * para gestionar todas las conexiones a la base de datos, evitando así múltiples
 * conexiones simultáneas que podrían agotar los recursos del servidor.
 * </p>
 * <p>
 * ¿Por qué no usamos un pool de conexiones? Porque este es un sistema de 
 * aprendizaje donde la simplicidad es más importante que la optimización
 * extrema. Cada petición HTTP creará su propia conexión y la cerrará
 * al finalizar, lo que es suficiente para la carga esperada.
 * </p>
 *
 * @author Alejandro
 */
public class ConexionDB {
    private Connection con;

    // Configuración de conexión a MySQL
    // NOTA: En producción, estas credenciales deberían estar en variables de entorno
    // o en un archivo de configuración externo para mayor seguridad.
    String url    = "jdbc:mysql://localhost:3306/economic";
    String user   = "root";
    //String pass   = "#Aprendiz2024";  // Contraseña anterior (comentada para referencia)
    String pass = "Orion31Heart";       // Contraseña actual de desarrollo
    String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * Carga el driver MySQL y abre una nueva conexión a la base de datos.
     * <p>
     * Este método es el corazón del sistema de persistencia. Cada vez que
     * un DAO necesita interactuar con la base de datos, llama a este método.
     * </p>
     * <p>
     * ¿Por qué no mantenemos la conexión abierta? Porque las conexiones
     * son recursos limitados y costosos. Es mejor abrirla, usarla y cerrarla
     * en el menor tiempo posible para permitir que más usuarios concurrentes
     * puedan acceder al sistema.
     * </p>
     *
     * @return {@link Connection} activa, o {@code null} si ocurre un error.
     */
    public Connection getConnection() {
        try {
            // Carga dinámica del driver JDBC - permite cambiar de BD sin recompilar
            Class.forName(driver);
            // Establece conexión con parámetros configurados
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexion exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            // Registramos error específico para diagnóstico rápido
            System.err.println("Error de conexión: " + e);
        }
        return con;
    }
}
