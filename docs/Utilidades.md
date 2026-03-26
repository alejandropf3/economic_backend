# 🛠️ Utilidades del Sistema Economic

## 📋 **Arquitectura de Utilidades**

Las utilidades son clases helper que proporcionan funcionalidades reutilizables para validación, seguridad, comunicación y operaciones comunes del sistema.

---

## 🔐 **Utilidades de Seguridad y Validación**

### **1. ValidadorPermisos**
**Ubicación**: `src/java/utils/ValidadorPermisos.java`

**Propósito**: Facilita la validación de permisos de usuarios en toda la aplicación.

**Constantes de Permisos**:
```java
public static final String CREAR_TRANSACCIONES = "crear_transacciones";
public static final String VER_TRANSACCIONES = "ver_transacciones";
public static final String EDITAR_TRANSACCIONES = "editar_transacciones";
public static final String ELIMINAR_TRANSACCIONES = "eliminar_transacciones";
public static final String CREAR_CATEGORIAS = "crear_categorias";
public static final String VER_CATEGORIAS = "ver_categorias";
public static final String EDITAR_CATEGORIAS = "editar_categorias";
public static final String ELIMINAR_CATEGORIAS = "eliminar_categorias";
public static final String CREAR_METAS = "crear_metas";
public static final String VER_METAS = "ver_metas";
public static final String EDITAR_METAS = "editar_metas";
public static final String ELIMINAR_METAS = "eliminar_metas";
public static final String ADMINISTRAR_USUARIOS = "administrar_usuarios";
public static final String VER_REPORTES = "ver_reportes";
```

**Métodos Principales**:

#### **🔍 Validación Individual**
```java
public static boolean tienePermiso(Usuario usuario, String permiso)
```
- Verifica si un usuario tiene un permiso específico
- Manejo seguro de nulos

#### **👤 Validación de Roles**
```java
public static boolean esAdministrador(Usuario usuario)
public static boolean esUsuarioNormal(Usuario usuario)
```
- Validación rápida por nombre de rol
- Manejo seguro de usuarios nulos

#### **🎯 Validaciones Compuestas**
```java
public static boolean puedeAdministrar(Usuario usuario)
public static boolean tieneTodosLosPermisos(Usuario usuario, String... permisos)
public static boolean tieneAlgunPermiso(Usuario usuario, String... permisos)
```
- Validaciones lógicas complejas
- Operadores AND/OR

#### **📝 Utilidades de Mensajes**
```java
public static String getMensajeAccesoDenegado(String permiso)
```
- Genera mensajes de error estandarizados

**Uso en Controladores**:
```java
// Validación simple
if (!ValidadorPermisos.tienePermiso(usuario, ValidadorPermisos.ADMINISTRAR_USUARIOS)) {
    response.sendRedirect(request.getContextPath() + "/MenuControlador?error=no_permiso");
    return;
}

// Validación múltiple
if (!ValidadorPermisos.tieneTodosLosPermisos(usuario, 
    ValidadorPermisos.CREAR_TRANSACCIONES, 
    ValidadorPermisos.VER_TRANSACCIONES)) {
    // Mostrar error
}
```

---

### **2. validarLogin**
**Ubicación**: `src/java/utils/validarLogin.java`

**Propósito**: Validación de credenciales de inicio de sesión.

**Métodos Principales**:
```java
public static Usuario validarCredenciales(String correo, String pass, UsuarioDao dao)
```

**Validaciones Realizadas**:
1. ✅ Campos no nulos ni vacíos
2. ✅ Llamada segura al DAO
3. ✅ Manejo de excepciones

**Uso**:
```java
Usuario user = validarLogin.validarCredenciales(correo, passEncriptada, dao);
if (user != null) {
    // Login exitoso
}
```

---

## 📝 **Utilidades de Validación de Datos**

### **3. validarUsuarios**
**Ubicación**: `src/java/utils/validarUsuarios.java`

**Propósito**: Validación completa del formulario de registro de usuarios.

**Método Principal**:
```java
public static String validarRegistro(String correo, String pass, 
                                   String confirmPass, UsuarioDao dao)
```

**Códigos de Retorno**:
- `"ok"`: Validación exitosa
- `"vacio"`: Campos obligatorios vacíos
- `"pass_error"`: Contraseñas no coinciden
- `"cantidad_de_caracteres_error"`: Contraseña < 8 caracteres
- `"correo_duplicado"`: Correo ya registrado

**Validaciones Realizadas**:
1. ✅ Campos no vacíos
2. ✅ Coincidencia de contraseñas
3. ✅ Longitud mínima (8 caracteres)
4. ✅ Unicidad de correo electrónico

**Uso en UsuarioControlador**:
```java
String resultado = utils.validarUsuarios.validarRegistro(correo, pass, confirmPass, dao);
if (!resultado.equals("ok")) {
    response.sendRedirect(request.getContextPath() + "/registro.jsp?res=" + resultado);
    return;
}
```

---

### **4. validarTransaccion**
**Ubicación**: `src/java/utils/validarTransaccion.java`

**Propósito**: Validación de datos de transacciones financieras.

**Validaciones Principales**:
- ✅ Valor positivo y no nulo
- ✅ Categoría válida para el usuario
- ✅ Fecha no futura
- ✅ Descripción longitud razonable

---

### **5. validarCategorias**
**Ubicación**: `src/java/utils/validarCategorias.java`

**Propósito**: Validación de nombres y tipos de categorías.

**Validaciones**:
- ✅ Tipo válido ("Ingreso" o "Egreso")
- ✅ Nombre no vacío
- ✅ Longitud apropiada
- ✅ Sin caracteres especiales peligrosos

---

### **6. validarMeta**
**Ubicación**: `src/java/utils/validarMeta.java`

**Propósito**: Validación de metas de ahorro.

**Validaciones**:
- ✅ Monto objetivo positivo
- ✅ Fecha límite futura
- ✅ Nombre descriptivo
- ✅ Monto actual no mayor al objetivo

---

## 📧 **Utilidades de Comunicación**

### **7. ServicioCorreo**
**Ubicación**: `src/java/utils/ServicioCorreo.java`

**Propósito**: Servicio centralizado para envío de correos vía SMTP.

**Configuración SMTP**:
```java
private static final String SMTP_HOST     = "smtp.gmail.com";
private static final String SMTP_PORT     = "587";
private static final String SMTP_USUARIO  = "alejandropefa31@gmail.com";
private static final String SMTP_CONTRASENA = "whcq detk muxu zlzr";
private static final String NOMBRE_REMITENTE = "Economic App";
```

**Métodos Principales**:

#### **🔑 Recuperación de Contraseña**
```java
public static boolean enviarCodigoRecuperacion(String destinatario, String codigo)
```
- Envía código de 6 dígitos
- Plantilla HTML profesional
- Manejo de errores

**Plantilla de Correo**:
```java
String htmlContent = "<html><body>"
    + "<h2>Código de Recuperación</h2>"
    + "<p>Tu código para recuperar tu contraseña es:</p>"
    + "<h1 style='color: #002349; font-size: 32px; text-align: center;'>" + codigo + "</h1>"
    + "<p>Este código expirará en 15 minutos.</p>"
    + "<hr>"
    + "<p style='color: #666; font-size: 12px;'>Si no solicitaste este código, ignora este correo.</p>"
    + "</body></html>";
```

#### **📧 Correo de Bienvenida**
```java
public static boolean enviarBienvenida(String destinatario, String nombreUsuario)
```
- Correo de bienvenida personalizado
- Información básica de la aplicación

#### **🔔 Notificaciones del Sistema**
```java
public static boolean enviarNotificacion(String destinatario, String asunto, String mensaje)
```
- Envío genérico de notificaciones
- Soporte para HTML y texto plano

**Configuración de Producción**:
```java
// Para producción, usar variables de entorno
private static final String SMTP_HOST = System.getenv("SMTP_HOST");
private static final String SMTP_USUARIO = System.getenv("SMTP_USER");
private static final String SMTP_CONTRASENA = System.getenv("SMTP_PASSWORD");
```

**Dependencias Maven**:
```xml
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.0.1</version>
</dependency>
```

---

## 🔧 **Utilidades del Sistema**

### **8. hash**
**Ubicación**: `src/java/configuracion/hash.java`

**Propósito**: Utilidades de hash para seguridad de contraseñas.

**Métodos Principales**:
```java
public static String sha256(String input)
public static boolean verificarHash(String texto, String hash)
```

**Características**:
- ✅ Implementación SHA-256
- ✅ Salting básico
- ✅ Manejo de excepciones

**Uso**:
```java
// Encriptar contraseña
String passwordHash = hash.sha256(passwordUsuario);

// Verificar contraseña
boolean coincide = hash.verificarHash(passwordIngresado, hashAlmacenado);
```

---

## 🎨 **Patrones de Diseño**

### **🔐 Clases Utilitarias Estáticas**
- Todos los métodos son estáticos
- Constructores privados para prevenir instanciación
- Sin estado mutable

### **🛡️ Validación en Capa**
- Validaciones separadas de la lógica de negocio
- Códigos de error estandarizados
- Reutilización across controladores

### **📧 Servicio Centralizado**
- Configuración SMTP centralizada
- Plantillas reutilizables
- Manejo unificado de errores

---

## 🔒 **Consideraciones de Seguridad**

### **🚨 Advertencia de Seguridad**
```java
// En ServicioCorreo.java - ⚠️ NO HACER ESTO EN PRODUCCIÓN
private static final String SMTP_CONTRASENA = "whcq detk muxu zlzr"; // Hardcodeada
```

### **🔐 Mejoras de Seguridad Sugeridas**:

#### **Variables de Entorno**:
```java
private static final String SMTP_CONTRASENA = System.getenv("SMTP_PASSWORD");
```

#### **Hash Mejorado**:
```java
public static String sha256ConSalt(String input, String salt) {
    return DigestUtils.sha256Hex(input + salt);
}
```

#### **Validación de Entrada**:
```java
public static boolean esCorreoSeguro(String correo) {
    // Validar formato y prevenir inyección
    return correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
}
```

---

## 📊 **Métricas y Monitoreo**

### **📈 Logging Sugerido**:
```java
private static final Logger logger = LoggerFactory.getLogger(ServicioCorreo.class);

public static boolean enviarCodigoRecuperacion(String destinatario, String codigo) {
    try {
        // ... lógica de envío
        logger.info("Código enviado exitosamente a: {}", destinatario);
        return true;
    } catch (MessagingException e) {
        logger.error("Error enviando correo a {}: {}", destinatario, e.getMessage());
        return false;
    }
}
```

### **🎯 Métricas de Rendimiento**:
- Tiempo de envío de correos
- Tasa de éxito/fracaso
- Validaciones fallidas por tipo

---

## 🔄 **Extensibilidad**

### **🔧 Agregar Nuevas Validaciones**:
```java
public class ValidadorPersonalizado {
    public static boolean validarIBAN(String iban) {
        // Lógica de validación de IBAN
        return iban.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$");
    }
}
```

### **📧 Nuevas Plantillas de Correo**:
```java
public static boolean enviarReporteMensual(String destinatario, Reporte reporte) {
    String plantilla = cargarPlantilla("reporte_mensual.html");
    String contenido = plantilla.replace("{{nombre}}", reporte.getNombreUsuario())
                              .replace("{{balance}}", reporte.getBalance());
    return enviarCorreo(destinatario, "Reporte Mensual", contenido);
}
```

---

## 🛠️ **Mejoras Futuras**

### **🔄 Validación con Anotaciones**:
```java
public class UsuarioDTO {
    @NotBlank(message = "El correo es requerido")
    @Email(message = "Formato de correo inválido")
    private String correo;
    
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;
}
```

### **📧 Servicio de Colas**:
```java
public class ServicioColasCorreo {
    public static void encolarCorreo(CorreoMessage mensaje) {
        // Encolar para procesamiento asíncrono
    }
}
```

### **🔐 Validación Avanzada**:
```java
public class ValidadorSeguridad {
    public static boolean esContraseñaSegura(String pass) {
        return pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$");
    }
    
    public static boolean detectarSQLInjection(String input) {
        return !input.toLowerCase().matches(".*(select|insert|update|delete|drop|union|script).*");
    }
}
```

---

## 🎯 **Resumen**

El sistema cuenta con 8 utilidades principales:

- **2 utilidades de seguridad** (ValidadorPermisos, validarLogin)
- **4 utilidades de validación** (validarUsuarios, validarTransaccion, validarCategorias, validarMeta)
- **1 utilidad de comunicación** (ServicioCorreo)
- **1 utilidad de sistema** (hash)

**Características Destacadas**:
- ✅ Validaciones reutilizables y consistentes
- ✅ Sistema de permisos centralizado
- ✅ Servicio de correo profesional
- ✅ Seguridad con hash SHA-256
- ✅ Códigos de error estandarizados
- ✅ Manejo robusto de excepciones

**Estado**: Completo y funcional, requiere ajustes de seguridad para producción (credenciales hardcodeadas).
