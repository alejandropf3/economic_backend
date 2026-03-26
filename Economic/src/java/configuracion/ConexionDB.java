package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos MySQL del sistema Economic.
 * <p>
 * Carga el driver JDBC y establece la conexión usando las credenciales
 * configuradas como atributos de instancia.
 * </p>
 *
 * @author Alejandro
 */
public class ConexionDB {
    private Connection con;

    String url    = "jdbc:mysql://localhost:3306/economic";
    String user   = "root";
    String pass   = "#Aprendiz2024";
    //String pass = "Orion31Heart";
    String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * Carga el driver MySQL y abre una nueva conexión a la base de datos.
     *
     * @return {@link Connection} activa, o {@code null} si ocurre un error.
     */
    public Connection getConnection() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexion exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexión: " + e);
        }
        return con;
    }
}
