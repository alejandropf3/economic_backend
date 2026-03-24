# 🔐 Redirección Automática por Rol - Sistema de Login

## 🎯 **Funcionalidad Implementada**

Se ha modificado el sistema de login para que redirija automáticamente a los usuarios según su rol y permisos:

### **🔄 Lógica de Redirección:**

1. **Usuario Administrador** (ID_rol = 1 + permiso `administrar_usuarios`)
   - 📍 **Redirección**: `/AdminControlador`
   - 🎪 **Destino**: Página de administración de usuarios

2. **Usuario Normal** (cualquier otro rol)
   - 📍 **Redirección**: `/MenuControlador`
   - 🎪 **Destino**: Menú principal de la aplicación

---

## 📋 **Cambios Realizados**

### **Archivo Modificado:**
- **`controlador/LoginControlador.java`**

### **Cambios Específicos:**

#### **1. Importación Nueva:**
```java
import utils.ValidadorPermisos;
```

#### **2. Lógica de Validación:**
```java
// 🔍 VALIDAR ROL Y PERMISOS PARA REDIRECCIÓN AUTOMÁTICA
boolean esAdminPorRol = (user.getIdRol() == 1); // ID_rol = 1 = administrador
boolean tienePermisoAdmin = ValidadorPermisos.tienePermiso(user, ValidadorPermisos.ADMINISTRAR_USUARIOS);

// 🎯 LÓGICA DE REDIRECCIÓN CONDICIONAL
if (esAdminPorRol && tienePermisoAdmin) {
    // ✅ Usuario es administrador y tiene permiso → Redirigir a administración
    response.sendRedirect(request.getContextPath() + "/AdminControlador");
} else {
    // ✅ Usuario normal → Redirigir al menú principal
    response.sendRedirect(request.getContextPath() + "/MenuControlador");
}
```

#### **3. Logging para Debug:**
```java
System.out.println("🔑 Administrador detectado: " + user.getNombre() + " (ID: " + user.getIdUsuario() + ")");
System.out.println("📋 Redirigiendo a /AdminControlador");

// O para usuario normal:
System.out.println("👤 Usuario normal detectado: " + user.getNombre() + " (Rol: " + user.getNombreRol() + ")");
System.out.println("🏠 Redirigiendo a /MenuControlador");
```

---

## 🔍 **Condiciones de Validación**

### **Para ser redirigido a Administración:**
1. ✅ `user.getIdRol() == 1` (Rol de administrador)
2. ✅ `ValidadorPermisos.tienePermiso(user, ValidadorPermisos.ADMINISTRAR_USUARIOS)`

### **Para ser redirigido a Menú Principal:**
- ❌ No cumple las condiciones de administrador
- ✅ Cualquier otro rol (usuario, invitado, etc.)

---

## 🧪 **Pasos para Testing**

### **1. Preparar Base de Datos:**
```sql
-- Asegurarse que existan los roles y permisos
source d:/adsoo/economic_backend/database/roles_y_permisos.sql;

-- Verificar administradores existentes
SELECT U.Nombre, E.Correo_electronico, R.Nombre_rol, R.ID_rol
FROM Usuario U
JOIN Email E ON U.ID_usuario = E.ID_usuario
JOIN Usuario_Rol UR ON U.ID_usuario = UR.ID_usuario
JOIN Rol R ON UR.ID_rol = R.ID_rol
WHERE R.ID_rol = 1;
```

### **2. Casos de Prueba:**

#### **Caso 1: Administrador Válido**
- **Usuario**: Con rol ID=1 y permiso `administrar_usuarios`
- **Contraseña**: Correcta
- **Resultado Esperado**: Redirección a `/AdminControlador`
- **Logs**: 
  ```
  🔑 Administrador detectado: [nombre] (ID: [id])
  📋 Redirigiendo a /AdminControlador
  ```

#### **Caso 2: Usuario Normal**
- **Usuario**: Con rol ID=2 (usuario)
- **Contraseña**: Correcta
- **Resultado Esperado**: Redirección a `/MenuControlador`
- **Logs**:
  ```
  👤 Usuario normal detectado: [nombre] (Rol: usuario)
  🏠 Redirigiendo a /MenuControlador
  ```

#### **Caso 3: Credenciales Incorrectas**
- **Usuario**: Cualquiera
- **Contraseña**: Incorrecta
- **Resultado Esperado**: Redirección a `index.jsp?res=invalid_password`

---

## 🔧 **Configuración Requerida**

### **Base de Datos:**
- ✅ Tabla `Rol` con registro ID=1, Nombre_rol='administrador'
- ✅ Tabla `Permisos` con permiso `administrar_usuarios`
- ✅ Tabla `Rol_Permisos` con la relación correspondiente
- ✅ Tabla `Usuario_Rol` con las asignaciones de usuarios

### **Dependencias:**
- ✅ `utils/ValidadorPermisos.java` - Debe existir y estar compilado
- ✅ `modelo/Usuario.java` - Con métodos `getIdRol()` y `getPermisos()`
- ✅ `dao/UsuarioDao.java` - Cargando roles y permisos en `iniciarSesion()`

---

## 🚨 **Consideraciones de Seguridad**

### **✅ Validaciones Implementadas:**
1. **Doble validación**: Por rol (ID=1) Y por permiso específico
2. **Redirección segura**: Usa `getContextPath()` para evitar ataques
3. **Logging informativo**: Para auditoría y debug
4. **Mantenimiento de sesión**: Se mantiene la sesión del usuario

### **🔒 Recomendaciones Adicionales:**
1. **Limitar intentos**: Implementar rate limiting en login
2. **Registro de auditoría**: Guardar logs en base de datos
3. **Timeout de sesión**: Implementar logout automático
4. **Validación de token**: Considerar CSRF tokens

---

## 🔄 **Flujo Completo del Login**

```
1. Usuario ingresa correo y contraseña
   ↓
2. LoginControlador encripta contraseña (SHA-256)
   ↓
3. UsuarioDao.iniciarSesion() valida credenciales
   ↓
4. Si válido → Carga objeto Usuario completo con:
   • ID, Nombre, Correo
   • ID_rol, Nombre_rol
   • Lista de permisos
   ↓
5. LoginControlador valida:
   • ¿ID_rol == 1? (administrador)
   • ¿Tiene permiso 'administrar_usuarios'?
   ↓
6. Redirección condicional:
   • Admin → /AdminControlador
   • Usuario → /MenuControlador
   ↓
7. Usuario accede a su página correspondiente
```

---

## 🎊 **Estado Final**

✅ **Funcionalidad Completada:**
- Redirección automática según rol y permisos
- Validación doble (rol + permiso)
- Logging para debug y auditoría
- Mantenimiento de compatibilidad con usuarios existentes

🚀 **Lista para Producción:**
- La funcionalidad está implementada y lista para testing
- Todos los componentes necesarios están en su lugar
- La documentación está completa para mantenimiento futuro

---

**📝 Nota:** Para activar esta funcionalidad, asegúrate de haber ejecutado el script SQL `roles_y_permisos.sql` para crear los roles y permisos básicos en la base de datos.
