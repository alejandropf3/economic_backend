# 📊 Modelos del Sistema Economic

## 📋 **Arquitectura de Modelos**

Los modelos representan las entidades del dominio y mapean las tablas de la base de datos MySQL. Siguen el patrón POJO con getters, setters y constructores.

---

## 👤 **Modelos de Usuarios**

### **1. Usuario**
**Ubicación**: `src/java/modelo/Usuario.java`

**Propósito**: Representa un usuario del sistema con roles y permisos.

**Atributos Principales**:
```java
private long idUsuario;              // ID único
private String nombre;               // Nombre completo
private String contrasena;           // Contraseña encriptada
private String correo;               // Correo electrónico
private String correoRespaldo;       // Reutilizado para rol en UI
private String urlImagen;            // URL imagen de perfil
private long idRol;                  // ID del rol asignado
private String nombreRol;             // Nombre del rol
private List<String> permisos;        // Lista de permisos
```

**Características**:
- ✅ Soporte para roles y permisos
- ✅ Imagen de perfil opcional
- ✅ Manejo de seguridad (contraseña encriptada)
- ✅ Campo reutilizado para compatibilidad UI

**Constructores**:
```java
public Usuario() {}  // Requerido por DAO
public Usuario(String nombre, String contrasena) {}  // Para registro
```

**Métodos de Negocio**:
```java
// Validación de permisos (delegado a ValidadorPermisos)
public boolean tienePermiso(String permiso);
public boolean esAdministrador();
```

---

## 💰 **Modelos Financieros**

### **2. Transaccion**
**Ubicación**: `src/java/modelo/Transaccion.java`

**Propósito**: Representa una transacción financiera del usuario.

**Atributos Principales**:
```java
private long idTransaccion;         // ID único
private long idUsuario;             // ID del usuario propietario
private int idCategoria;            // ID de la categoría
private String nombreCategoria;      // Nombre (cargado por JOIN)
private String tipoTransaccion;      // "Ingreso" o "Egreso"
private BigDecimal valorTransaccion; // Valor monetario
private String descripcion;         // Descripción opcional
private LocalDate fechaRealizacion;  // Fecha de realización
private long idMeta;                // Meta asociada (opcional)
```

**Características**:
- ✅ Uso de `BigDecimal` para precisión financiera
- ✅ Soporte para descripciones opcionales
- ✅ Asociación opcional con metas de ahorro
- ✅ Tipo de transacción claramente definido

**Validaciones Implícitas**:
- `valorTransaccion`: No puede ser nulo ni negativo
- `tipoTransaccion`: Solo "Ingreso" o "Egreso"
- `fechaRealizacion`: No puede ser futura

---

### **3. Categoria**
**Ubicación**: `src/java/modelo/Categoria.java`

**Propósito**: Representa una categoría personalizada de transacción.

**Atributos Principales**:
```java
private int idCategoria;            // ID único
private String tipoTransaccion;     // "Ingreso" o "Egreso"
private String nombreCategoria;     // Nombre descriptivo
private long idUsuario;             // ID del usuario propietario
```

**Características**:
- ✅ Tipos bien definidos (Ingreso/Egreso)
- ✅ Propiedad específica por usuario
- ✅ Nombres descriptivos personalizables

**Constructores**:
```java
public Categoria() {}  // Requerido por DAO
public Categoria(String tipoTransaccion, String nombreCategoria, long idUsuario) {}
```

**Validaciones**:
- `tipoTransaccion`: Debe ser "Ingreso" o "Egreso"
- `nombreCategoria`: Requerido, único por usuario
- `idUsuario`: Requerido para seguridad

---

### **4. Meta**
**Ubicación**: `src/java/modelo/Meta.java`

**Propósito**: Representa una meta de ahorro del usuario.

**Atributos Principales**:
```java
private long idMeta;                // ID único
private long idUsuario;             // ID del usuario propietario
private String nombreMeta;          // Nombre descriptivo
private BigDecimal montoObjetivo;    // Monto objetivo
private BigDecimal montoActual;     // Monto acumulado
private LocalDate fechaCreacion;     // Fecha de creación
private LocalDate fechaLimite;       // Fecha límite
private String estado;               // Estado de la meta
```

**Estados Posibles**:
- `"Activa"`: En progreso
- `"Completada"`: Objetivo alcanzado
- `"Cancelada"`: Cancelada por usuario

**Características**:
- ✅ Seguimiento de progreso
- ✅ Fechas límite definidas
- ✅ Estados de ciclo de vida
- ✅ Precisión decimal con `BigDecimal`

**Métodos de Negocio**:
```java
public BigDecimal getPorcentajeCompletado() {
    if (montoObjetivo.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
    return montoActual.divide(montoObjetivo, 4, RoundingMode.HALF_UP)
                      .multiply(new BigDecimal("100"));
}

public boolean estaCompletada() {
    return montoActual.compareTo(montoObjetivo) >= 0;
}

public boolean estaVencida() {
    return LocalDate.now().isAfter(fechaLimite) && "Activa".equals(estado);
}
```

---

## 📈 **Modelos de Reportes**

### **5. ResumenDiario**
**Ubicación**: `src/java/modelo/ResumenDiario.java`

**Propósito**: Representa un resumen financiero diario.

**Atributos**:
```java
private LocalDate fecha;            // Fecha del resumen
private BigDecimal ingresos;        // Total ingresos del día
private BigDecimal egresos;         // Total egresos del día
private BigDecimal balance;         // Balance del día (ingresos - egresos)
```

---

### **6. ResumenSemanal**
**Ubicación**: `src/java/modelo/ResumenSemanal.java`

**Propósito**: Representa un resumen financiero semanal.

**Atributos**:
```java
private LocalDate fechaInicio;      // Inicio de la semana (lunes)
private LocalDate fechaFin;         // Fin de la semana (domingo)
private BigDecimal totalIngresos;   // Total ingresos semanales
private BigDecimal totalEgresos;    // Total egresos semanales
private BigDecimal balanceSemanal;  // Balance semanal
private List<ResumenDiario> dias;  // Breakdown por día
```

---

### **7. ResumenMensual**
**Ubicación**: `src/java/modelo/ResumenMensual.java`

**Propósito**: Representa un resumen financiero mensual.

**Atributos**:
```java
private int anio;                   // Año del resumen
private int mes;                    // Mes (1-12)
private BigDecimal ingresosMes;      // Total ingresos del mes
private BigDecimal egresosMes;       // Total egresos del mes
private BigDecimal balanceMes;      // Balance mensual
```

---

### **8. ResumenAnual**
**Ubicación**: `src/java/modelo/ResumenAnual.java`

**Propósito**: Representa un resumen financiero anual.

**Atributos**:
```java
private int anio;                   // Año del resumen
private BigDecimal ingresosAnual;    // Total ingresos anuales
private BigDecimal egresosAnual;     // Total egresos anuales
private BigDecimal balanceAnual;     // Balance anual
```

---

## 🔧 **Modelos Auxiliares**

### **9. ResultadoEliminacion**
**Ubicación**: `src/java/modelo/ResultadoEliminacion.java`

**Propósito**: Representa el resultado de una operación de eliminación.

**Atributos**:
```java
private boolean exito;              // Si la operación fue exitosa
private String mensaje;              // Mensaje descriptivo
private String detalle;              // Detalles adicionales
```

---

## 🎨 **Patrones de Diseño**

### **📦 POJO (Plain Old Java Object)**
- Todos los modelos son POJOs simples
- Constructores vacíos requeridos por DAOs
- Getters y setters para todos los atributos

### **🔐 Inmutabilidad Selectiva**
- IDs son inmutables después de la creación
- Fechas de creación son inmutables
- Valores financieros usan `BigDecimal` (inmutable)

### **🎯 Validación en Modelo**
```java
public void setValorTransaccion(BigDecimal valor) {
    if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("El valor debe ser positivo");
    }
    this.valorTransaccion = valor;
}
```

### **🔄 Cálculos Derivados**
```java
// En Meta.java
public BigDecimal getPorcentajeCompletado() {
    // Cálculo del porcentaje completado
}

// En ResumenSemanal.java
public BigDecimal getBalanceSemanal() {
    return totalIngresos.subtract(totalEgresos);
}
```

---

## 🛡️ **Seguridad en Modelos**

### **🔒 Encapsulamiento**
- Todos los atributos son privados
- Acceso controlado mediante getters/setters
- Validaciones en setters

### **🔐 Datos Sensibles**
- Contraseña solo accesible via setter
- IDs de usuario validados en operaciones
- No hay exposición directa de contraseñas

### **✅ Validaciones Integradas**
```java
// Validación de tipo de transacción
public void setTipoTransaccion(String tipo) {
    if (!"Ingreso".equals(tipo) && !"Egreso".equals(tipo)) {
        throw new IllegalArgumentException("Tipo debe ser Ingreso o Egreso");
    }
    this.tipoTransaccion = tipo;
}
```

---

## 🔄 **Serialización**

### **📊 Formato JSON**
Los modelos son fácilmente serializables a JSON:

```json
// Usuario
{
    "idUsuario": 1,
    "nombre": "Juan Pérez",
    "correo": "juan@example.com",
    "nombreRol": "usuario",
    "permisos": ["crear_transacciones", "ver_transacciones"]
}

// Transaccion
{
    "idTransaccion": 123,
    "valorTransaccion": 1500.50,
    "tipoTransaccion": "Ingreso",
    "nombreCategoria": "Salario",
    "fechaRealizacion": "2024-03-26"
}
```

---

## 📏 **Convenciones de Nomenclatura**

### **🏷️ Nombres de Atributos**
- `id` + `Entidad`: `idUsuario`, `idTransaccion`
- `nombre` + `Entidad`: `nombreCategoria`, `nombreMeta`
- `tipo` + `Concepto`: `tipoTransaccion`
- Valores compuestos: `montoObjetivo`, `fechaLimite`

### **📋 Tipos de Datos**
- **IDs**: `long` para entidades principales, `int` para secundarias
- **Valores monetarios**: `BigDecimal` siempre
- **Fechas**: `LocalDate` (no `Date`)
- **Estados**: `String` con valores predefinidos
- **Listas**: `List<Tipo>` genérico

---

## 🚀 **Extensibilidad**

### **🔧 Fácil Adición de Campos**
```java
// Ejemplo: Agregar moneda a Transaccion
private String moneda;  // "USD", "EUR", etc.

public void setMoneda(String moneda) {
    this.moneda = moneda != null ? moneda : "USD";
}
```

### **📊 Soporte para Herencia**
```java
// Base para todos los modelos con ID
public abstract class ModeloBase {
    protected long id;
    // getters/setters comunes
}

public class Transaccion extends ModeloBase {
    // atributos específicos
}
```

---

## 🎯 **Mejoras Sugeridas**

### **🔍 Validaciones Avanzadas**
- Anotaciones de validación (`@NotNull`, `@Positive`)
- Validaciones cruzadas entre campos
- Validaciones de negocio complejas

### **📊 Métricas Integradas**
```java
public class Transaccion {
    // Campos existentes...
    
    public BigDecimal getValorImpuesto(BigDecimal tasa) {
        return valorTransaccion.multiply(tasa);
    }
    
    public boolean esGrande(BigDecimal umbral) {
        return valorTransaccion.compareTo(umbral) > 0;
    }
}
```

### **🔄 Estado Inmutable**
```java
public final class Transaccion {
    private final long idTransaccion;
    private final BigDecimal valor;
    
    // Constructor con todos los campos
    // Solo getters, no setters
}
```

---

## 📝 **Resumen**

El sistema cuenta con 9 modelos principales:

- **1 modelo de usuarios** (Usuario)
- **3 modelos financieros** (Transaccion, Categoria, Meta)
- **4 modelos de reportes** (ResumenDiario, ResumenSemanal, ResumenMensual, ResumenAnual)
- **1 modelo auxiliar** (ResultadoEliminacion)

**Características Destacadas**:
- ✅ Diseño POJO limpio
- ✅ Precisión financiera con BigDecimal
- ✅ Validaciones integradas
- ✅ Soporte para roles y permisos
- ✅ Cálculos derivados
- ✅ Fácil serialización

**Estado**: Completo y bien estructurado, listo para producción.
