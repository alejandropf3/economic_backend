# 🗄️ DAOs del Sistema Economic

## 📋 **Arquitectura de Acceso a Datos**

Los DAOs (Data Access Objects) implementan el patrón de acceso a datos, proporcionando una capa de abstracción entre la lógica de negocio y la base de datos MySQL.

---

## 👤 **DAOs de Usuarios**

### **1. UsuarioDao**
**Ubicación**: `src/java/dao/UsuarioDao.java`

**Propósito**: Gestiona todas las operaciones CRUD y de autenticación de usuarios.

**Funcionalidades Principales**:

#### **🔐 Registro de Usuario**
```java
public boolean registrar(Usuario user)
```
- Inserta usuario + email + rol en transacción atómica
- Asigna automáticamente rol "usuario" (ID=2)
- Maneja encriptación de contraseña desde el controlador

#### **🔑 Inicio de Sesión**
```java
public Usuario iniciarSesion(String correo, String contrasena)
```
- Carga información completa del usuario
- Incluye roles y permisos asignados
- Usa JOINs para obtener datos relacionados

#### **✅ Validaciones**
```java
public boolean existeCorreo(String correo)
```
- Verifica disponibilidad de correos únicos

**Consultas SQL Principales**:
```sql
-- Registro con transacción atómica
INSERT INTO Usuario (Nombre, Contraseña) VALUES (?, ?)
INSERT INTO Email (ID_usuario, Correo_electronico) VALUES (?, ?)
INSERT INTO Usuario_Rol (ID_usuario, ID_rol) VALUES (?, 2)

-- Login con información completa
SELECT U.*, E.Correo_electronico, I.Url_imagen, 
       COALESCE(R.ID_rol, 2) as ID_rol, 
       COALESCE(R.Nombre_rol, 'usuario') as Nombre_rol
FROM Usuario U
JOIN Email E ON U.ID_usuario = E.ID_usuario
LEFT JOIN Imagenes I ON U.ID_usuario = I.ID_usuario
LEFT JOIN Usuario_Rol UR ON U.ID_usuario = UR.ID_usuario
LEFT JOIN Rol R ON UR.ID_rol = R.ID_rol
WHERE E.Correo_electronico = ? AND U.Contraseña = ?
```

---

### **2. AdminDao**
**Ubicación**: `src/java/dao/AdminDao.java`

**Propósito**: Gestiona operaciones administrativas sobre usuarios.

**Funcionalidades Principales**:

#### **👥 Listar Usuarios**
```java
public List<Usuario> listarUsuarios()
```
- Lista todos los usuarios con sus roles
- Usa LEFT JOIN para incluir usuarios sin rol asignado

#### **🔒 Validación de Administrador**
```java
public boolean esAdministrador(long idUsuario)
```
- Verifica si un usuario tiene rol de administrador

#### **🗑️ Eliminación Segura**
```java
public boolean eliminarUsuario(long idUsuario)
```
- Elimina usuario con validaciones previas
- Las FK con ON DELETE CASCADE limpian datos relacionados

#### **🔍 Validaciones de Eliminación**
```java
public int contarTransacciones(long idUsuario)
public int contarCategorias(long idUsuario)
```
- Verifica que el usuario no tenga datos asociados

#### **🔐 Verificación de Contraseña**
```java
public boolean verificarContrasenaAdmin(long idAdmin, String passEncriptada)
```
- Valida contraseña de administrador para operaciones críticas

---

## 💰 **DAOs Financieros**

### **3. TransaccionDao**
**Ubicación**: `src/java/dao/TransaccionDao.java`

**Propósito**: Gestiona todas las operaciones de transacciones financieras.

**Funcionalidades Principales**:

#### **💳 Crear Transacción**
```java
public boolean crear(Transaccion t)
```
- Inserta transacción con categoría y meta opcional
- Maneja valores BigDecimal para precisión financiera

#### **📊 Listar con Filtros**
```java
public List<Transaccion> listarPorUsuario(long idUsuario, String tipo, 
                                         String idCategoria, String fechaDesde, String fechaHasta)
```
- Filtra por usuario, tipo, categoría y rango de fechas
- Usa JOIN con Usuario_Categoria para seguridad por usuario

#### **🔍 Consultas Especializadas**
```java
public Transaccion obtenerUltimaPorTipo(long idUsuario, String tipo)
public BigDecimal[] obtenerTotales(long idUsuario)
```
- Obtiene última transacción por tipo (Ingreso/Egreso)
- Calcula totales de ingresos y egresos

**Consultas SQL Principales**:
```sql
-- Listar transacciones con filtros
SELECT T.ID_transaccion, UC.ID_usuario, T.ID_categoria, 
       C.Nombre_categoria, C.Tipo_transaccion, T.Valor_transaccion, 
       T.Descripcion, T.Fecha_realizacion
FROM Transacciones T
JOIN Categoria C ON T.ID_categoria = C.ID_categoria
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
WHERE UC.ID_usuario = ? AND ...

-- Obtener totales
SELECT 
    SUM(CASE WHEN C.Tipo_transaccion = 'Ingreso' THEN T.Valor_transaccion ELSE 0 END) as totalIngresos,
    SUM(CASE WHEN C.Tipo_transaccion = 'Egreso' THEN T.Valor_transaccion ELSE 0 END) as totalEgresos
FROM Transacciones T
JOIN Categoria C ON T.ID_categoria = C.ID_categoria
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
WHERE UC.ID_usuario = ?
```

---

### **4. CategoriaDao**
**Ubicación**: `src/java/dao/CategoriaDao.java`

**Propósito**: Gestiona las categorías personalizadas de los usuarios.

**Funcionalidades Principales**:

#### **📁 Crear Categoría**
```java
public boolean crear(Categoria categoria)
```
- Inserta categoría y la asocia al usuario en transacción
- Soporta tipos: "Ingreso" y "Egreso"

#### **📋 Listar por Usuario**
```java
public List<Categoria> listarPorUsuario(long idUsuario)
```
- Obtiene categorías específicas del usuario
- Usa tabla intermedia Usuario_Categoria

#### **✏️ Actualizar y Eliminar**
```java
public boolean actualizar(Categoria categoria)
public boolean eliminar(int idCategoria, long idUsuario)
```
- Actualiza nombre de categoría
- Elimina con validación de usuario propietario

**Consultas SQL Principales**:
```sql
-- Crear categoría con relación a usuario
INSERT INTO Categoria (Tipo_transaccion, Nombre_categoria) VALUES (?, ?)
INSERT INTO Usuario_Categoria (ID_usuario, ID_categoria) VALUES (?, ?)

-- Listar categorías de usuario
SELECT C.ID_categoria, C.Nombre_categoria, C.Tipo_transaccion
FROM Categoria C
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
WHERE UC.ID_usuario = ?
ORDER BY C.Tipo_transaccion, C.Nombre_categoria
```

---

### **5. MetaDao**
**Ubicación**: `src/java/dao/MetaDao.java`

**Propósito**: Gestiona las metas de ahorro de los usuarios.

**Funcionalidades Principales**:

#### **🎯 Crear Meta**
```java
public boolean crear(Meta meta)
```
- Inserta nueva meta de ahorro
- Valida fechas y montos

#### **📊 Listar y Actualizar**
```java
public List<Meta> listarPorUsuario(long idUsuario)
public boolean actualizarProgreso(long idMeta, BigDecimal nuevoMonto)
```
- Lista metas con estado actual
- Actualiza monto acumulado

#### **🔔 Validaciones**
```java
public boolean estaCompletada(long idMeta)
public boolean perteneceUsuario(long idMeta, long idUsuario)
```
- Verifica estado de meta
- Valida propiedad del usuario

---

## 📈 **DAOs de Reportes**

### **6. ResumenDao**
**Ubicación**: `src/java/dao/ResumenDao.java`

**Propósito**: Genera resúmenes financieros por diferentes períodos.

**Funcionalidades Principales**:

#### **📅 Resúmenes por Período**
```java
public ResumenSemanal calcularSemanal(long idUsuario, LocalDate fechaReferencia)
public ResumenMensual calcularMensual(long idUsuario, int anio, int mes)
public ResumenAnual calcularAnual(long idUsuario, int anio)
```
- Calcula resúmenes con breakdown diario
- Usa vista predefinida `Vista_Detalle_Resumen`

#### **📊 Consultas de Resumen**
```java
public List<ResumenSemanal> obtenerUltimosSemanales(long idUsuario, int limite)
public List<ResumenDiario> obtenerDiarios(long idUsuario, LocalDate desde, LocalDate hasta)
```
- Obtiene resúmenes históricos
- Soporta paginación

**Vista SQL Utilizada**:
```sql
CREATE OR REPLACE VIEW Vista_Detalle_Resumen AS
SELECT 
    UC.ID_usuario,
    T.Fecha_realizacion,
    YEAR(T.Fecha_realizacion) AS Anio,
    MONTH(T.Fecha_realizacion) AS Mes,
    STR_TO_DATE(CONCAT(YEARWEEK(T.Fecha_realizacion, 1), ' Monday'), '%X%V %W') AS Inicio_Semana,
    SUM(CASE WHEN C.Tipo_transaccion = 'Ingreso' THEN T.Valor_transaccion ELSE 0 END) AS Ingresos,
    SUM(CASE WHEN C.Tipo_transaccion = 'Egreso' THEN T.Valor_transaccion ELSE 0 END) AS Egresos,
    (SUM(CASE WHEN C.Tipo_transaccion = 'Ingreso' THEN T.Valor_transaccion ELSE 0 END) - 
     SUM(CASE WHEN C.Tipo_transaccion = 'Egreso' THEN T.Valor_transaccion ELSE 0 END)) AS Balance
FROM Transacciones T
JOIN Categoria C ON T.ID_categoria = C.ID_categoria
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
GROUP BY UC.ID_usuario, T.Fecha_realizacion, Anio, Mes, Inicio_Semana
```

---

## 🖼️ **DAOs Auxiliares**

### **7. ImagenDao**
**Ubicación**: `src/java/dao/ImagenDao.java`

**Propósito**: Gestiona las imágenes de perfil de usuarios.

**Funcionalidades**:
- Subir imágenes de perfil
- Obtener URL de imagen por usuario
- Eliminar imágenes

### **8. TokenRecuperacionDao**
**Ubicación**: `src/java/dao/TokenRecuperacionDao.java`

**Propósito**: Gestiona tokens de recuperación de contraseña.

**Funcionalidades**:
- Generar y almacenar tokens
- Validar tokens no expirados
- Limpiar tokens expirados

---

## 🔧 **Patrones y Buenas Prácticas**

### **🔄 Manejo de Transacciones**
```java
con.setAutoCommit(false);
try {
    // Operaciones múltiples
    con.commit();
    return true;
} catch (SQLException e) {
    con.rollback();
    return false;
} finally {
    con.close();
}
```

### **🛡️ Seguridad por Usuario**
Todos los DAOs filtran por ID de usuario usando `Usuario_Categoria`:
```sql
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
WHERE UC.ID_usuario = ?
```

### **♻️ Manejo de Recursos**
```java
finally {
    try { if (con != null) con.close(); } catch (SQLException e) {}
}
```

### **📊 Uso de Vistas**
- `Vista_Detalle_Resumen`: Preprocesa datos para reportes
- Mejora rendimiento en consultas complejas

---

## 🚀 **Optimizaciones Implementadas**

### **🎯 Índices de Base de Datos**
```sql
CREATE INDEX idx_transaccion_fecha ON Transacciones (Fecha_realizacion);
CREATE INDEX idx_meta_usuario_estado ON Metas_Ahorro (ID_usuario, Estado);
CREATE INDEX idx_categoria_usuario ON Usuario_Categoria (ID_categoria);
```

### **🔄 Conexiones Eficientes**
- Cada DAO maneja sus propias conexiones
- Cierre automático de recursos
- Manejo de excepciones específico

### **📈 Consultas Optimizadas**
- Uso de JOINs en lugar de subconsultas
- Vistas predefinidas para reportes complejos
- Filtros en base de datos en lugar de aplicación

---

## 🔍 **Métricas y Monitoreo**

### **📊 Logging Implementado**
```java
System.err.println("Error en DAO Crear Transaccion: " + e.getMessage());
System.out.println("Conexion exitosa");
```

### **🎯 Métricas Sugeridas**
- Tiempo de ejecución por consulta
- Número de conexiones activas
- Tasa de errores por operación

---

## 🛠️ **Mejoras Futuras**

### **🔄 Connection Pooling**
- Implementar HikariCP
- Reducir overhead de conexiones

### **📊 Caching**
- Caché de categorías por usuario
- Caché de resúmenes frecuentes

### **🔍 Consultas Dinámicas**
- Builder para consultas complejas
- Paginación dinámica

---

## 🎯 **Resumen**

El sistema cuenta con 8 DAOs especializados:

- **2 DAOs de usuarios** (UsuarioDao, AdminDao)
- **3 DAOs financieros** (TransaccionDao, CategoriaDao, MetaDao)
- **1 DAO de reportes** (ResumenDao)
- **2 DAOs auxiliares** (ImagenDao, TokenRecuperacionDao)

**Características Destacadas**:
- ✅ Transacciones atómicas
- ✅ Seguridad por usuario
- ✅ Manejo eficiente de recursos
- ✅ Optimización de consultas
- ✅ Vistas predefinidas

**Estado**: Completo y optimizado para producción.
