package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Alejandro
 */
public class ConexionDB {
    private Connection con;

    String url = "jdbc:mysql://localhost:3306/economic";
    String user = "root";
    //String pass = "#Aprendiz2024";
    String pass = "Orion31Heart";
    String driver = "com.mysql.cj.jdbc.Driver";
    
    public Connection getConnection() 
    {
        try {
            Class.forName(driver); //Cargamos el driver
            con = DriverManager.getConnection(url, user, pass); //Realizamos el intento de conexion
            System.out.println("Conexion exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexión: " + e);
        }
        return con;
    }
}
