# 🎮 Controladores del Sistema Economic

## 📋 **Arquitectura MVC**

Los controladores siguen el patrón MVC (Model-View-Controller) y gestionan las peticiones HTTP, coordinando la interacción entre modelos (DAO) y vistas (JSP).

---

## 🔐 **Controladores de Autenticación**

### **1. LoginControlador**
**Ubicación**: `src/java/controlador/LoginControlador.java`
**URL Pattern**: `/LoginControlador`

**Propósito**: Gestiona el inicio de sesión de usuarios con redirección automática por rol.

**Funcionalidades**:
- ✅ Validación de credenciales con hash SHA-256
- ✅ Redirección automática según rol y permisos
- ✅ Manejo de errores específicos (contraseña incorrecta, usuario no encontrado)
- ✅ Logging para auditoría

**Lógica de Redirección**:
```java
// Administrador (ID_rol=1 + permiso administrar_usuarios) → /AdminControlador
// Usuario normal → /MenuControlador
```

**Parámetros Recibidos**:
- `txtCorreo`: Correo electrónico del usuario
- `txtContrasena`: Contraseña del usuario

**Redirecciones**:
- **Éxito admin**: `/AdminControlador`
- **Éxito usuario**: `/MenuControlador`
- **Error**: `/index.jsp?res=invalid_password` o `user_not_found`

---

### **2. UsuarioControlador**
**Ubicación**: `src/java/controlador/UsuarioControlador.java`
**URL Pattern**: `/UsuarioControlador`

**Propósito**: Gestiona el registro de nuevos usuarios con asignación automática de rol.

**Funcionalidades**:
- ✅ Registro de nuevos usuarios
- ✅ Validación de datos de entrada
- ✅ Encriptación automática de contraseña
- ✅ Asignación automática de rol "usuario"
- ✅ Validación de duplicados por correo

**Flujo de Registro**:
1. Validar datos del formulario
2. Verificar que el correo no exista
3. Encriptar contraseña con SHA-256
4. Registrar usuario + email + rol (transacción atómica)

**Parámetros Recibidos**:
- `txtNombre`: Nombre del usuario
- `txtEmail`: Correo electrónico
- `txtContrasena`: Contraseña
- `txtConfirmar`: Confirmación de contraseña

---

### **3. RecuperacionControlador**
**Ubicación**: `src/java/controlador/RecuperacionControlador.java`
**URL Pattern**: `/RecuperacionControlador`

**Propósito**: Gestiona el proceso de recuperación de contraseña.

**Funcionalidades**:
- ✅ Envío de correos de recuperación
- ✅ Generación de tokens únicos
- ✅ Validación de tokens
- ✅ Restablecimiento de contraseñas

---

## 🏠 **Controladores Principales**

### **4. MenuControlador**
**Ubicación**: `src/java/controlador/MenuControlador.java`
**URL Pattern**: `/MenuControlador`

**Propósito**: Muestra el dashboard principal con resúmenes financieros.

**Funcionalidades**:
- ✅ Mostrar últimas transacciones por tipo
- ✅ Calcular balance actual
- ✅ Mostrar últimos resúmenes semanales (límite 3)
- ✅ Validación de sesión activa

**Datos Cargados**:
- `ultimoIngreso`: Última transacción de ingreso
- `ultimoEgreso`: Última transacción de egreso
- `balance`: Balance total (ingresos - egresos)
- `ultimosResumenes`: Lista de resúmenes semanales

**Vista**: `/Public/User/menu_principal.jsp`

---

### **5. AdminControlador**
**Ubicación**: `src/java/controlador/AdminControlador.java`
**URL Pattern**: `/AdminControlador`

**Propósito**: Gestiona la administración de usuarios del sistema.

**Funcionalidades**:
- ✅ Listar todos los usuarios con roles
- ✅ Eliminar usuarios con validaciones de seguridad
- ✅ Verificación de rol de administrador
- ✅ Validación de contraseña de admin
- ✅ Protección contra auto-eliminación

**Validaciones de Eliminación**:
- Usuario no puede eliminarse a sí mismo
- Debe verificar contraseña de administrador
- No puede tener transacciones o categorías asociadas

**Seguridad**: Solo accesible para usuarios con rol de administrador

---

## 💰 **Controladores Financieros**

### **6. TransaccionControlador**
**Ubicación**: `src/java/controlador/TransaccionControlador.java`
**URL Pattern**: `/TransaccionControlador`

**Propósito**: Gestiona el registro y consulta de transacciones financieras.

**Funcionalidades GET**:
- ✅ Cargar categorías del usuario
- ✅ Mostrar formulario de registro

**Funcionalidades POST**:
- ✅ Registrar nuevas transacciones
- ✅ Validar datos financieros
- ✅ Asociar a categorías y metas

**Parámetros POST**:
- `valor`: Monto de la transacción
- `categoria`: ID de categoría
- `descripcion`: Descripción opcional
- `fecha`: Fecha de realización
- `meta`: ID de meta asociada (opcional)

---

### **7. CategoriaControlador**
**Ubicación**: `src/java/controlador/CategoriaControlador.java`
**URL Pattern**: `/CategoriaControlador`

**Propósito**: Gestiona las categorías personalizadas de los usuarios.

**Funcionalidades**:
- ✅ Listar categorías del usuario
- ✅ Crear nuevas categorías
- ✅ Editar categorías existentes
- ✅ Eliminar categorías (con validaciones)

**Tipos de Categoría**:
- `Ingreso`: Para transacciones de ingresos
- `Egreso`: Para transacciones de egresos

---

### **8. MetaControlador**
**Ubicación**: `src/java/controlador/MetaControlador.java`
**URL Pattern**: `/MetaControlador`

**Propósito**: Gestiona las metas de ahorro de los usuarios.

**Funcionalidades**:
- ✅ Crear nuevas metas de ahorro
- ✅ Listar metas del usuario
- ✅ Actualizar progreso de metas
- ✅ Eliminar metas completadas

**Atributos de Meta**:
- Nombre descriptivo
- Monto objetivo
- Monto actual acumulado
- Fecha límite
- Estado (Activa, Completada, Cancelada)

---

## 📊 **Controladores de Reportes**

### **9. HistorialControlador**
**Ubicación**: `src/java/controlador/HistorialControlador.java`
**URL Pattern**: `/HistorialControlador`

**Propósito**: Muestra el historial completo de transacciones.

**Funcionalidades**:
- ✅ Listar transacciones con filtros
- ✅ Paginación de resultados
- ✅ Filtros por fecha, tipo, categoría
- ✅ Exportación de datos

**Filtros Disponibles**:
- Rango de fechas
- Tipo de transacción
- Categoría específica

---

### **10. ResumenControlador**
**Ubicación**: `src/java/controlador/ResumenControlador.java`
**URL Pattern**: `/ResumenControlador`

**Propósito**: Genera resúmenes financieros por diferentes períodos.

**Funcionalidades**:
- ✅ Resúmenes diarios
- ✅ Resúmenes semanales
- ✅ Resúmenes mensuales
- ✅ Resúmenes anuales
- ✅ Comparativos entre períodos

---

## 🖼️ **Controladores Auxiliares**

### **11. ImagenControlador**
**Ubicación**: `src/java/controlador/ImagenControlador.java`
**URL Pattern**: `/ImagenControlador`

**Propósito**: Gestiona las imágenes de perfil de los usuarios.

**Funcionalidades**:
- ✅ Subir imágenes de perfil
- ✅ Validar formatos de imagen
- ✅ Redimensionamiento automático
- ✅ Almacenamiento en base de datos

---

### **12. CambioContrasenaControlador**
**Ubicación**: `src/java/controlador/CambioContrasenaControlador.java`
**URL Pattern**: `/CambioContrasenaControlador`

**Propósito**: Gestiona el cambio de contraseña de usuarios.

**Funcionalidades**:
- ✅ Validar contraseña actual
- ✅ Actualizar a nueva contraseña
- ✅ Encriptación automática
- ✅ Confirmación de cambio

---

### **13. CodigoVerificacionControlador**
**Ubicación**: `src/java/controlador/CodigoVerificacionControlador.java`
**URL Pattern**: `/CodigoVerificacionControlador`

**Propósito**: Gestiona códigos de verificación por correo.

**Funcionalidades**:
- ✅ Generar códigos únicos
- ✅ Enviar códigos por email
- ✅ Validar códigos ingresados
- ✅ Manejo de expiración

---

## 🔒 **Patrones de Seguridad**

### **Validación de Sesión**
Todos los controladores (excepto LoginControlador y UsuarioControlador) implementan:

```java
HttpSession session = request.getSession(false);
if (session == null || session.getAttribute("usuario") == null) {
    response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}
```

### **Encoding UTF-8**
Para manejar caracteres especiales:

```java
request.setCharacterEncoding("UTF-8");
```

### **Validación de Roles**
Controladores administrativos validan permisos específicos usando `ValidadorPermisos`.

---

## 🔄 **Flujo de Navegación**

```
1. Usuario accede a /index.jsp
   ↓
2. LoginControlador valida credenciales
   ↓
3. Redirección según rol:
   - Admin → AdminControlador → /Admin/administrar_usuarios.jsp
   - Usuario → MenuControlador → /User/menu_principal.jsp
   ↓
4. Navegación por funcionalidades:
   - Transacciones → TransaccionControlador
   - Categorías → CategoriaControlador
   - Metas → MetaControlador
   - Historial → HistorialControlador
   - Resúmenes → ResumenControlador
```

---

## 📝 **Mejoras Sugeridas**

### **Seguridad**
- 🔄 Implementar CSRF tokens
- 🔄 Rate limiting para login
- 🔄 Validación de entrada más robusta

### **Rendimiento**
- 🔄 Caching de datos frecuentes
- 🔄 Paginación optimizada
- 🔄 Lazy loading para grandes volúmenes

### **UX**
- 🔄 Manejo mejor de errores
- 🔄 Confirmaciones antes de acciones destructivas
- 🔄 Indicadores de carga

---

## 🎯 **Resumen**

El sistema cuenta con 13 controladores que cubren todas las funcionalidades principales:

- **3 controladores de autenticación** (login, registro, recuperación)
- **2 controladores principales** (menú, administración)
- **3 controladores financieros** (transacciones, categorías, metas)
- **2 controladores de reportes** (historial, resúmenes)
- **3 controladores auxiliares** (imágenes, contraseña, verificación)

**Estado**: Completo y funcional, con buenas prácticas de seguridad implementadas.
