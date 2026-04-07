/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package configuracion;

/**
 * Clase de prueba para verificar la configuración de conexión a la base de datos.
 * <p>
 * Esta clase se crea como una herramienta de diagnóstico rápido para asegurar
 * que la configuración de la base de datos es correcta antes de integrarla
 * con el resto del sistema. Es especialmente útil durante el desarrollo y
 * despliegue inicial.
 * </p>
 * <p>
 * ¿Por qué no usar un framework de testing? Porque esta es una verificación
 * simple de infraestructura que no requiere assertions complejas. Solo necesitamos
 * saber si la conexión se establece o no, y los mensajes de consola son suficientes.
 * </p>
 * 
 * @author usuario
 */
public class prueva {

    /**
     * Método principal que prueba la conexión a la base de datos.
     * <p>
     * Este método es un punto de entrada rápido para verificar que la
     * configuración en ConexionDB.java es correcta. Si vemos "Conexion exitosa"
     * en la consola, sabemos que la base de datos está accesible y las
     * credenciales son válidas.
     * </p>
     * <p>
     * ¿Por qué no manejar excepciones aquí? Porque queremos que el programa
     * termine si la conexión falla, indicando claramente que hay un problema
     * de configuración que debe resolverse antes de continuar.
     * </p>
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Creamos instancia del gestor de conexión
        ConexionDB hola = new ConexionDB();
        
        // Realizamos prueba del funcionamiento de la conexión con la base de datos
        // Este llamado debe imprimir "Conexion exitosa" o mostrar el error específico
        hola.getConnection(); 
    }
    
}
