package utils;
 
import dao.MetaDao;
import java.math.BigDecimal;
import java.time.LocalDate;
 
/**
 * Utilidad — validarMeta.java
 * Validaciones del servidor para la creación y edición de metas de ahorro.
 */
public class validarMeta {
 
    // ── VALIDAR CREACIÓN ──────────────────────────────────────────────────────
    public static String validarCreacion(String nombre, String montoStr,
                                          String fechaStr, long idUsuario) {
        // 1. Nombre
        if (nombre == null || nombre.trim().isEmpty())
            return "nombre_vacio";
        if (nombre.trim().length() < 3)
            return "nombre_muy_corto";
        if (nombre.trim().length() > 100)
            return "nombre_muy_largo";
 
        // 2. Monto objetivo
        if (montoStr == null || montoStr.trim().isEmpty())
            return "monto_vacio";
        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr.trim());
        } catch (NumberFormatException e) {
            return "monto_invalido";
        }
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            return "monto_debe_ser_positivo";
 
        // 3. Fecha límite
        if (fechaStr == null || fechaStr.trim().isEmpty())
            return "fecha_vacia";
        LocalDate fechaLimite;
        try {
            fechaLimite = LocalDate.parse(fechaStr.trim());
        } catch (Exception e) {
            return "fecha_invalida";
        }
        if (!fechaLimite.isAfter(LocalDate.now()))
            return "fecha_debe_ser_futura";
 
        // 4. Duplicado (nombre + fecha_límite para el mismo usuario)
        MetaDao dao = new MetaDao();
        if (dao.existeDuplicado(nombre.trim(), fechaLimite, idUsuario))
            return "meta_duplicada";
 
        return "ok";
    }
 
    // ── VALIDAR EDICIÓN ───────────────────────────────────────────────────────
    public static String validarEdicion(String nombre, String fechaStr,
                                         long idUsuario, long idMeta) {
        // 1. Nombre
        if (nombre == null || nombre.trim().isEmpty())
            return "nombre_vacio";
        if (nombre.trim().length() < 3)
            return "nombre_muy_corto";
        if (nombre.trim().length() > 100)
            return "nombre_muy_largo";
 
        // 2. Fecha límite
        if (fechaStr == null || fechaStr.trim().isEmpty())
            return "fecha_vacia";
        LocalDate fechaLimite;
        try {
            fechaLimite = LocalDate.parse(fechaStr.trim());
        } catch (Exception e) {
            return "fecha_invalida";
        }
        // RF14 restricción 3: no puede ser menor a la fecha actual
        if (!fechaLimite.isAfter(LocalDate.now()))
            return "fecha_debe_ser_futura";
 
        // 3. Duplicado excluyendo la meta actual
        MetaDao dao = new MetaDao();
        if (dao.existeDuplicadoEdicion(nombre.trim(), fechaLimite, idUsuario, idMeta))
            return "meta_duplicada";
 
        return "ok";
    }
 
    // ── VALIDAR ABONO ─────────────────────────────────────────────────────────
    public static String validarAbono(String montoStr) {
        if (montoStr == null || montoStr.trim().isEmpty())
            return "monto_vacio";
        try {
            BigDecimal monto = new BigDecimal(montoStr.trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0)
                return "monto_debe_ser_positivo";
        } catch (NumberFormatException e) {
            return "monto_invalido";
        }
        return "ok";
    }
}