package utils;
 
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
 
/**
 * ServicioCorreo.java
 * Servicio centralizado para envío de correos vía SMTP.
 * Coloca aquí tus credenciales SMTP.
 */
public class ServicioCorreo {
 
    // ─── CONFIGURACIÓN SMTP ───────────────────────────────────────────────────
    // Reemplaza estos valores con los de tu proveedor SMTP
    private static final String SMTP_HOST     = "smtp.gmail.com";   // ← tu host SMTP
    private static final String SMTP_PORT     = "587";              // 587 = TLS, 465 = SSL
    private static final String SMTP_USUARIO  = "alejandropefa31@gmail.com"; // ← tu correo
    private static final String SMTP_CONTRASENA = "whcq detk muxu zlzr"; // ← contraseña de app
    private static final String NOMBRE_REMITENTE = "Economic App";
    // ─────────────────────────────────────────────────────────────────────────
 
    /**
     * Envía el código de recuperación al correo del usuario.
     *
     * @param destinatario correo del usuario
     * @param codigo       código de 6 dígitos generado
     * @return true si el envío fue exitoso
     */
    public static boolean enviarCodigoRecuperacion(String destinatario, String codigo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);
        props.put("mail.smtp.ssl.trust",       SMTP_HOST);
 
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USUARIO, SMTP_CONTRASENA);
            }
        });
 
        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(SMTP_USUARIO, NOMBRE_REMITENTE));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mensaje.setSubject("Código de recuperación - Economic");
 
            // ── Cuerpo del correo (HTML) ──────────────────────────────────────
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
 
            mensaje.setContent(cuerpo, "text/html; charset=UTF-8");
            Transport.send(mensaje);
            System.out.println("Correo de recuperación enviado a: " + destinatario);
            return true;
 
        } catch (Exception e) {
            System.err.println("Error al enviar correo SMTP: " + e.getMessage());
            return false;
        }
    }
}