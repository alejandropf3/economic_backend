package configuracion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad criptográfica para generar hashes seguros de texto plano.
 * <p>
 * Esta clase es fundamental para la seguridad del sistema Economic porque
 * asegura que ninguna contraseña se almacene en texto plano en la base de datos.
 * Se crea como una clase estática porque no necesita mantener estado y
 * queremos que esté disponible en todo el sistema sin instanciación.
 * </p>
 * <p>
 * ¿Por qué SHA-256 y no MD5? Porque MD5 tiene vulnerabilidades conocidas
 * de colisión que podrían ser explotadas por atacantes. SHA-256 es el estándar
 * actual para hashing de contraseñas y proporciona seguridad suficiente
 * para este tipo de aplicación.
 * </p>
 */
public class hash {

    /**
     * Genera un hash hexadecimal del texto dado usando el algoritmo especificado.
     * <p>
     * Este método es el núcleo de la criptografía del sistema. Convierte
     * cualquier texto (como contraseñas) en un hash irreversible que puede
     * ser almacenado de forma segura en la base de datos.
     * </p>
     * <p>
     * ¿Por qué este algoritmo de conversión a hexadecimal? Porque los hashes
     * son arrays de bytes, y las bases de datos necesitan texto para almacenarlos.
     * La conversión a hexadecimal asegura que cada byte se represente con
     * exactamente 2 caracteres, haciendo el hash consistente y comparable.
     * </p>
     * <p>
     * El truco {@code (array[i] & 0xFF) | 0x100} es crucial: maneja bytes
     * negativos correctamente y asegura que siempre tengamos 3 caracteres
     * antes de extraer los últimos 2, garantizando representación de 2 dígitos.
     * </p>
     *
     * @param txt      Texto a encriptar (ej. contraseña en texto plano).
     * @param hashType Nombre del algoritmo de hash (ej. {@code "SHA-256"}, {@code "MD5"}).
     * @return Representación hexadecimal del hash de 64 caracteres (SHA-256) o 32 (MD5), 
     *         o {@code null} si el algoritmo no existe.
     */
    public static String getHash(String txt, String hashType) {
        try {
            // Obtiene instancia del algoritmo de hash especificado
            MessageDigest md = MessageDigest.getInstance(hashType);
            // Genera el hash como array de bytes
            byte[] array = md.digest(txt.getBytes());
            StringBuilder sb = new StringBuilder();
            // Convierte cada byte a su representación hexadecimal
            for (int i = 0; i < array.length; ++i) {
                // & 0xFF: convierte byte signed (0-255) a unsigned
                // | 0x100: asegura que tengamos al menos 0x100 (256) para 3 dígitos
                // substring(1,3): extrae los últimos 2 dígitos hexadecimales
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al generar hash: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método de conveniencia para generar un hash SHA-256.
     * <p>
     * ¿Por qué este método? Porque SHA-256 es el algoritmo estándar que
     * usamos en todo el sistema. Este método evita que los desarrolladores
     * tengan que recordar el nombre exacto del algoritmo y reduce errores.
     * </p>
     *
     * @param txt Texto a encriptar (generalmente una contraseña).
     * @return Hash SHA-256 de 64 caracteres en formato hexadecimal.
     */
    public static String sha256(String txt) {
        return hash.getHash(txt, "SHA-256");
    }
}
