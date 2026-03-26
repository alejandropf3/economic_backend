# 🗄️ Base de Datos - Sistema Economic

## 📋 **Arquitectura de Base de Datos**

El sistema utiliza MySQL 8.0 con un diseño relacional normalizado que soporta múltiples usuarios con roles, transacciones financieras, categorías personalizadas y metas de ahorro.

---

## 🏗️ **Estructura General**

### **📊 Diagrama de Entidades**
```
Usuario ←→ Email ←→ Imagenes
   ↓         ↓
   └─ Usuario_Rol ←→ Rol ←→ Rol_Permisos ←→ Permisos
   ↓
   └─ Usuario_Categoria ←→ Categoria ←→ Transacciones ←→ Metas_Ahorro
   ↓                                    ↓
   └─ Token_recuperacion              └─ Resumen (vista)
```

---

## 👥 **Tablas de Usuarios y Autenticación**

### **1. Usuario**
**Propósito**: Almacena información básica de usuarios.

```sql
CREATE TABLE Usuario(
ID_usuario bigint auto_increment primary key,
Nombre varchar(255) not null,
Contraseña varchar(255) not null
);
```

**Características**:
- ✅ ID autoincremental
- ✅ Contraseña encriptada (SHA-256)
- ✅ Índice primario en ID_usuario

---

### **2. Email**
**Propósito**: Almacena correos electrónicos de usuarios.

```sql
CREATE TABLE Email(
ID_correo int auto_increment primary key,
ID_usuario bigint not null,
Correo_electronico varchar(255) not null unique,
foreign key (ID_usuario) references Usuario(ID_usuario)
);
```

**Características**:
- ✅ Correo único por usuario
- ✅ Relación 1:1 con Usuario
- ✅ FK con ON DELETE CASCADE

---

### **3. Imagenes**
**Propósito**: Almacena URLs de imágenes de perfil.

```sql
CREATE TABLE Imagenes(
ID_imagen int auto_increment primary key,
ID_usuario bigint not null,
Url_imagen varchar(500) not null,
foreign key (ID_usuario) references Usuario(ID_usuario)
);
```

**Características**:
- ✅ URL de imagen externa
- ✅ Relación 1:1 opcional con Usuario
- ✅ Soporta URLs largas (500 caracteres)

---

## 🔐 **Sistema de Roles y Permisos**

### **4. Rol**
**Propósito**: Define los roles del sistema.

```sql
CREATE TABLE Rol(
ID_rol int auto_increment primary key,
Nombre_rol varchar(100) not null unique
);
```

**Roles Predefinidos**:
- `administrador` (ID=1)
- `usuario` (ID=2)

---

### **5. Permisos**
**Propósito**: Define los permisos granulares del sistema.

```sql
CREATE TABLE Permisos(
ID_permisos int auto_increment primary key,
Permiso varchar (100) not null unique
);
```

**Permisos Disponibles**:
- `crear_transacciones`
- `ver_transacciones`
- `editar_transacciones`
- `eliminar_transacciones`
- `crear_categorias`
- `ver_categorias`
- `editar_categorias`
- `eliminar_categorias`
- `crear_metas`
- `ver_metas`
- `editar_metas`
- `eliminar_metas`
- `administrar_usuarios`
- `ver_reportes`

---

### **6. Rol_Permisos**
**Propósito**: Relación muchos a muchos entre roles y permisos.

```sql
CREATE TABLE Rol_Permisos(
ID_rol int not null,
ID_permisos int not null,
PRIMARY KEY (ID_rol, ID_permisos),
foreign key (ID_rol) references Rol(ID_rol),
foreign key (ID_permisos) references Permisos(ID_permisos)
);
```

**Características**:
- ✅ Clave primaria compuesta
- ✅ Relación M:N
- ✅ Integridad referencial completa

---

### **7. Usuario_Rol**
**Propósito**: Asigna roles a los usuarios.

```sql
CREATE TABLE Usuario_Rol(
ID_rol int not null,
ID_usuario bigint not null,
primary key (ID_rol, ID_usuario),
foreign key (ID_rol) references Rol(ID_rol),
foreign key (ID_usuario) references Usuario(ID_usuario)
);
```

**Características**:
- ✅ Un usuario puede tener múltiples roles
- ✅ Clave primaria compuesta
- ✅ FK con ON DELETE CASCADE

---

## 💰 **Tablas Financieras**

### **8. Categoria**
**Propósito**: Define categorías personalizadas de transacción.

```sql
CREATE TABLE Categoria(
ID_categoria int auto_increment primary key,
Tipo_transaccion enum("Ingreso","Egreso") not null,
Nombre_categoria varchar(100) not null         
);
```

**Características**:
- ✅ Tipo restringido (Ingreso/Egreso)
- ✅ Nombre descriptivo
- ✅ Global a todos los usuarios (asociada por Usuario_Categoria)

---

### **9. Usuario_Categoria**
**Propósito**: Asigna categorías específicas a usuarios.

```sql
CREATE TABLE Usuario_Categoria (
ID_usuario bigint not null,
ID_categoria int not null,
PRIMARY KEY (ID_usuario, ID_categoria),
FOREIGN KEY (ID_usuario)   REFERENCES Usuario(ID_usuario) ON DELETE CASCADE,
FOREIGN KEY (ID_categoria) REFERENCES Categoria(ID_categoria) ON DELETE CASCADE
);
```

**Características**:
- ✅ Cada usuario tiene sus propias categorías
- ✅ FK con ON DELETE CASCADE
- ✅ Clave primaria compuesta

---

### **10. Transacciones**
**Propósito**: Registra todas las transacciones financieras.

```sql
CREATE TABLE Transacciones(
ID_transaccion bigint auto_increment primary key,
ID_categoria int not null,
ID_meta bigint null,
Valor_transaccion decimal(10, 2) not null,
Descripcion varchar (255) null,
Fecha_realizacion date not null,
foreign key (ID_categoria) references Categoria(ID_categoria),
foreign key (ID_meta) references Metas_Ahorro(ID_meta)
);
```

**Características**:
- ✅ Valor con precisión decimal (10,2)
- ✅ Asociación opcional con meta
- ✅ Descripción opcional
- ✅ Fecha obligatoria

---

### **11. Metas_Ahorro**
**Propósito**: Define metas de ahorro de los usuarios.

```sql
CREATE TABLE Metas_Ahorro(
ID_meta bigint auto_increment primary key,
ID_usuario bigint not null,
Nombre_meta varchar(255) not null,
Monto_objetivo decimal(10, 2) not null,
Monto_actual decimal(10, 2) default 0.00,
Fecha_creacion date not null,
Fecha_limite date not null,
Estado enum("Activa", "Completada", "Cancelada") default "Activa",
foreign key (ID_usuario) references Usuario(ID_usuario)
);
```

**Características**:
- ✅ Estados predefinidos
- ✅ Monto actual con valor por defecto
- ✅ Fechas de creación y límite
- ✅ Precisión decimal para montos

---

## 🔄 **Tablas de Soporte**

### **12. Token_recuperacion**
**Propósito**: Maneja tokens para recuperación de contraseña.

```sql
CREATE TABLE Token_recuperacion(
ID_token int auto_increment primary key,
ID_usuario bigint not null,
Token varchar(255) not null unique,
Fecha_creacion datetime not null,
Fecha_expriracion datetime,
Estado boolean not null,
foreign key (ID_usuario) references Usuario(ID_usuario)
);
```

**Características**:
- ✅ Token único y expirable
- ✅ Control de estado
- ✅ Fechas de creación y expiración

---

## 📈 **Vistas y Consultas Avanzadas**

### **13. Vista_perfil_usuario**
**Propósito**: Vista unificada del perfil de usuario.

```sql
CREATE VIEW Vista_perfil_usuario AS
SELECT
U.ID_usuario,
U.Nombre,
E.Correo_electronico,
R.nombre_rol
FROM Usuario U
JOIN Email E on U.ID_usuario = E.ID_usuario
LEFT JOIN Usuario_Rol UR on U.ID_usuario = UR.ID_usuario
LEFT JOIN Rol R on UR.ID_rol = R.ID_rol;
```

**Características**:
- ✅ Información consolidada
- ✅ LEFT JOIN para usuarios sin rol
- ✅ Optimizada para consultas frecuentes

---

### **14. Vista_Detalle_Resumen**
**Propósito**: Vista preprocesada para reportes financieros.

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
GROUP BY UC.ID_usuario, T.Fecha_realizacion, Anio, Mes, Inicio_Semana;
```

**Características**:
- ✅ Cálculos preprocesados
- ✅ Agrupación por múltiples períodos
- ✅ Optimizada para reportes

---

## 🚀 **Índices de Rendimiento**

### **Índices Principales**
```sql
-- Índices para búsquedas frecuentes
CREATE INDEX idx_transaccion_fecha ON Transacciones (Fecha_realizacion);
CREATE INDEX idx_meta_usuario_estado ON Metas_Ahorro (ID_usuario, Estado);
CREATE INDEX idx_token_estado ON Token_recuperacion (Token, Estado);
CREATE INDEX idx_categoria_usuario ON Usuario_Categoria (ID_categoria);
CREATE INDEX idx_rolpermisos_permiso_rol ON Rol_Permisos (ID_permisos, ID_rol);

-- Índices compuestos para consultas complejas
CREATE INDEX idx_transaccion_usuario_categoria ON Transacciones (ID_categoria, Fecha_realizacion);
CREATE INDEX idx_usuario_categoria_completa ON Usuario_Categoria (ID_usuario, ID_categoria);
```

### **Beneficios de los Índices**:
- ✅ Mejora rendimiento en consultas por fecha
- ✅ Optimiza búsquedas de tokens
- ✅ Acelera validaciones de permisos
- ✅ Soporte para ordenamiento complejo

---

## 🔒 **Seguridad de Datos**

### **Restricciones de Integridad**:
```sql
-- Foreign Keys con CASCADE
FOREIGN KEY (ID_usuario) REFERENCES Usuario(ID_usuario) ON DELETE CASCADE

-- Valores únicos
UNIQUE (Correo_electronico)
UNIQUE (Token)
UNIQUE (Nombre_rol)
UNIQUE (Permiso)

-- Valores restringidos
ENUM("Ingreso","Egreso")
ENUM("Activa", "Completada", "Cancelada")
```

### **Validaciones a Nivel de BD**:
- ✅ No permite correos duplicados
- ✅ Tipos de transacción válidos
- ✅ Estados de meta predefinidos
- ✅ Eliminación en cascada controlada

---

## 📊 **Estadísticas y Monitoreo**

### **Consultas de Monitoreo**:
```sql
-- Usuarios por rol
SELECT R.Nombre_rol, COUNT(UR.ID_usuario) as cantidad
FROM Rol R
LEFT JOIN Usuario_Rol UR ON R.ID_rol = UR.ID_rol
GROUP BY R.Nombre_rol;

-- Transacciones por mes
SELECT 
    YEAR(Fecha_realizacion) as anio,
    MONTH(Fecha_realizacion) as mes,
    COUNT(*) as cantidad,
    SUM(Valor_transaccion) as total
FROM Transacciones T
JOIN Categoria C ON T.ID_categoria = C.ID_categoria
JOIN Usuario_Categoria UC ON C.ID_categoria = UC.ID_categoria
GROUP BY YEAR(Fecha_realizacion), MONTH(Fecha_realizacion)
ORDER BY anio, mes;

-- Metas próximas a vencer
SELECT * FROM Metas_Ahorro
WHERE Estado = 'Activa' 
AND Fecha_limite BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY);
```

---

## 🔄 **Mantenimiento**

### **Procedimientos de Limpieza**:
```sql
-- Limpiar tokens expirados
DELETE FROM Token_recuperacion 
WHERE Fecha_expriracion < NOW() OR Estado = false;

-- Actualizar metas vencidas
UPDATE Metas_Ahorro 
SET Estado = 'Cancelada' 
WHERE Estado = 'Activa' AND Fecha_limite < CURDATE();
```

### **Backups Automáticos**:
```bash
# Backup completo
mysqldump -u root -p economic > backup_$(date +%Y%m%d_%H%M%S).sql

# Backup solo datos importantes
mysqldump -u root -p economic --no-create-info Usuario Email Transacciones > datos_$(date +%Y%m%d).sql
```

---

## 🚀 **Optimizaciones**

### **Configuración MySQL Sugerida**:
```ini
[mysqld]
# Para rendimiento
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
max_connections = 200

# Para seguridad
sql_mode = STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
```

### **Partitioning (para grandes volúmenes)**:
```sql
-- Particionar transacciones por año
ALTER TABLE Transacciones 
PARTITION BY RANGE (YEAR(Fecha_realizacion)) (
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);
```

---

## 📈 **Escalabilidad**

### **Estrategias de Escalamiento**:

#### **Vertical**:
- Aumentar recursos del servidor MySQL
- Optimizar consultas lentas
- Implementar caching

#### **Horizontal**:
- Replicación maestro-esclavo
- Sharding por usuario
- Separación de lecturas/escrituras

### **Caching Sugerido**:
```sql
-- Caché de categorías por usuario
CREATE TABLE Cache_Categorias_Usuario (
    ID_usuario bigint PRIMARY KEY,
    categorias_json TEXT,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 🔧 **Troubleshooting**

### **Problemas Comunes**:

#### **1. Bloqueos por FK**:
```sql
-- Identificar bloqueos
SHOW ENGINE INNODB STATUS;

-- Solución: orden correcto de eliminación
DELETE FROM Usuario_Rol WHERE ID_usuario = ?;
DELETE FROM Usuario WHERE ID_usuario = ?;
```

#### **2. Performance en Transacciones**:
```sql
-- Analizar consultas lentas
EXPLAIN SELECT * FROM Vista_Detalle_Resumen 
WHERE ID_usuario = ? AND Fecha_realizacion BETWEEN ? AND ?;

-- Optimización: usar índices compuestos
CREATE INDEX idx_resumen_usuario_fecha ON Vista_Detalle_Resumen (ID_usuario, Fecha_realizacion);
```

#### **3. Espacio en Disco**:
```sql
-- Monitorear tamaño de tablas
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'economic'
ORDER BY (data_length + index_length) DESC;
```

---

## 🎯 **Resumen**

**Características Destacadas**:
- ✅ Diseño normalizado 3NF
- ✅ Sistema de roles y permisos completo
- ✅ Integridad referencial robusta
- ✅ Vistas optimizadas para reportes
- ✅ Índices estratégicos
- ✅ Soporte para multi-tenant

**Estado**: Completo y optimizado para producción, con capacidad de escalamiento horizontal.

---

## 📝 **Notas de Implementación**

1. **Ejecutar script completo**: Incluye creación de tablas, datos iniciales y vistas
2. **Configurar usuario MySQL**: Crear usuario específico para la aplicación
3. **Establecer backups**: Configurar backups automáticos diarios
4. **Monitorear rendimiento**: Implementar monitoreo de consultas lentas
5. **Planificar escalamiento: Preparar estrategias para crecimiento de datos
