package utils;
 
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
 
/**
 * Servicio centralizado para envío de correos electrónicos vía SMTP.
 * <p>
 * Esta clase es fundamental para la experiencia de usuario y seguridad
 * del sistema Economic, ya que permite la recuperación de contraseñas
 * y comunicación con los usuarios. Se crea como estática porque el
 * envío de correos es una operación stateless que no requiere mantener
 * estado entre llamadas.
 * </p>
 * <p>
 * ¿Por qué no usar un servicio de email como SendGrid? Porque este
 * es un sistema de aprendizaje donde queremos mostrar la implementación
 * completa del protocolo SMTP. Además, usar Gmail nos permite tener
 * un servicio gratuito y robusto para desarrollo y producción inicial.
 * </p>
 * <p>
 * La configuración SMTP está hardcodeada por simplicidad, pero en
 * producción debería estar en variables de entorno o archivo de
 * configuración para mayor seguridad y flexibilidad.
 * </p>
 */
public class ServicioCorreo {
 
    // ─── CONFIGURACIÓN SMTP ───────────────────────────────────────────────────
    // NOTA: En producción, estas credenciales deben estar en variables de entorno
    // para no exponerlas en el código fuente. Esta configuración es para desarrollo.
    
    // Reemplaza estos valores con los de tu proveedor SMTP
    private static final String SMTP_HOST     = "smtp.gmail.com";   // ← tu host SMTP
    private static final String SMTP_PORT     = "587";              // 587 = TLS, 465 = SSL
    private static final String SMTP_USUARIO  = "alejandropefa31@gmail.com"; // ← tu correo
    private static final String SMTP_CONTRASENA = "whcq detk muxu zlzr"; // ← contraseña de app
    private static final String NOMBRE_REMITENTE = "Economic App";
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Envía el código de recuperación al correo del usuario.
     * <p>
     * Este método es el corazón del sistema de recuperación de contraseñas.
     * Genera y envía un correo electrónico con un código de verificación
     * que el usuario debe ingresar para confirmar su identidad antes de
     * poder cambiar su contraseña.
     * </p>
     * <p>
     * ¿Por qué usar TLS y no SSL? Porque TLS (puerto 587) es el estándar
     * moderno y más seguro. SSL (puerto 465) está obsoleto y muchos
     * proveedores ya no lo soportan. TLS proporciona encriptación
     * punto a punto durante toda la transmisión.
     * </p>
     * <p>
     * El correo se envía en formato HTML para mejor presentación visual
     * y experiencia de usuario profesional. El código se muestra en
     * un formato destacado para facilitar su lectura.
     * </p>
     *
     * @param destinatario correo electrónico del usuario que solicitó recuperación.
     * @param codigo       código de 6 dígitos generado aleatoriamente para verificación.
     * @return true si el envío fue exitoso, false si ocurrió algún error.
     */
    public static boolean enviarCodigoRecuperacion(String destinatario, String codigo) {
        // Configurar propiedades SMTP para conexión segura
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                    // Requiere autenticación
        props.put("mail.smtp.starttls.enable", "true");          // Habilita TLS para encriptación
        props.put("mail.smtp.host", SMTP_HOST);                 // Servidor SMTP
        props.put("mail.smtp.port", SMTP_PORT);                 // Puerto SMTP
        props.put("mail.smtp.ssl.trust", SMTP_HOST);             // Confía en el certificado del host

        // Crear sesión con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Autenticación con credenciales SMTP
                return new PasswordAuthentication(SMTP_USUARIO, SMTP_CONTRASENA);
            }
        });
 
        try {
            // Crear mensaje MIME
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(SMTP_USUARIO, NOMBRE_REMITENTE));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mensaje.setSubject("Código de recuperación - Economic");
 
            // ── Cuerpo del correo (HTML) ──────────────────────────────────────
            // Usamos HTML para mejor presentación y experiencia de usuario
            String cuerpo = "<div style='font-family:Arial,sans-serif; max-width:480px; margin:auto;'>"
                    + "<h2 style='color:#2c3e50;'>Recuperación de contraseña</h2>"
                    + "<p>Recibiste este correo porque solicitaste recuperar tu contraseña en <b>Economic</b>.</p>"
                    + "<p>Tu código de verificación es:</p>"
                    + "<div style='font-size:32px; font-weight:bold; letter-spacing:8px; "
                    +      "color:#2c3e50; padding:16px; background:#f4f4f4; "
                    +      "border-radius:8px; text-align:center;'>"
                    + codigo
                    + "</div>"
                    + "<p style='color:#888; font-size:13px; margin-top:16px;'>"
                    + "Este código expira en <b>5 minutos</b>. "
                    + "Si no realizaste esta solicitud, ignora este correo.</p>"
                    + "</div>";
 
            // Establecer contenido HTML con encoding UTF-8 para soporte de caracteres especiales
            mensaje.setContent(cuerpo, "text/html; charset=UTF-8");
            
            // Enviar correo
            Transport.send(mensaje);
            System.out.println("Correo de recuperación enviado a: " + destinatario);
            return true;
 
        } catch (Exception e) {
            // Registrar error para diagnóstico
            System.err.println("Error al enviar correo SMTP: " + e.getMessage());
            return false;
        }
    }
}