package configuracion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para generar hashes criptográficos de texto plano.
 * <p>
 * Se utiliza principalmente para encriptar contraseñas antes de
 * almacenarlas o compararlas en la base de datos.
 * </p>
 */
public class hash {

    /**
     * Genera un hash hexadecimal del texto dado usando el algoritmo especificado.
     *
     * @param txt      Texto a encriptar.
     * @param hashType Nombre del algoritmo de hash (ej. {@code "SHA-256"}, {@code "MD5"}).
     * @return Representación hexadecimal del hash, o {@code null} si el algoritmo no existe.
     */
    public static String getHash(String txt, String hashType) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
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
     *
     * @param txt Texto a encriptar.
     * @return Hash SHA-256 en formato hexadecimal.
     */
    public static String sha256(String txt) {
        return hash.getHash(txt, "SHA-256");
    }
}
