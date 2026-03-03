package configuracion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hash {

    /**
     * Retorna un hash a partir de un tipo y un texto
     * @param txt Texto a encriptar
     * @param hashType Algoritmo (SHA-256, MD5, etc)
     * @return String encriptado
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

    /* Método directo para usar SHA-256 */
    public static String sha256(String txt) {
        return hash.getHash(txt, "SHA-256");
    }
}