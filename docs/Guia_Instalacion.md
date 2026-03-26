# 🚀 Guía de Instalación y Despliegue - Sistema Economic

## 📋 **Requisitos Previos**

### **🔧 Software Requerido**

#### **Desarrollo**:
- **Java**: JDK 11 o superior
- **IDE**: IntelliJ IDEA, Eclipse o VS Code
- **Servidor**: Apache Tomcat 10.0+
- **Base de Datos**: MySQL 8.0+
- **Build Tool**: Maven 3.6+

#### **Producción**:
- **Servidor**: Apache Tomcat 10.0+ (o equivalente)
- **Base de Datos**: MySQL 8.0+ configurada para producción
- **Reverse Proxy**: Nginx o Apache (recomendado)
- **SSL/TLS**: Certificado HTTPS (recomendado)

---

## 🏗️ **Arquitectura del Sistema**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Navegador     │────│  Apache Tomcat  │────│   MySQL 8.0     │
│   (Frontend)    │    │ (Backend Java)  │    │  (Base Datos)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 📦 **Paso 1: Configuración del Entorno**

### **1.1 Instalar Java JDK**
```bash
# Verificar instalación
java -version

# Instalar JDK 11+ (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-11-jdk

# Instalar JDK 11+ (Windows)
# Descargar desde: https://adoptium.net/
```

### **1.2 Instalar Apache Tomcat**
```bash
# Descargar Tomcat 10
wget https://downloads.apache.org/tomcat/tomcat-10/v10.1.15/bin/apache-tomcat-10.1.15.tar.gz

# Extraer
tar -xzf apache-tomcat-10.1.15.tar.gz
sudo mv apache-tomcat-10.1.15 /opt/tomcat

# Configurar permisos
sudo chown -R $USER:$USER /opt/tomcat
chmod +x /opt/tomcat/bin/*.sh
```

### **1.3 Instalar MySQL**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# Windows
# Descargar desde: https://dev.mysql.com/downloads/mysql/

# Configurar seguridad
sudo mysql_secure_installation
```

---

## 🗄️ **Paso 2: Configuración de Base de Datos**

### **2.1 Crear Base de Datos**
```sql
-- Conectar a MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE economic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario para la aplicación
CREATE USER 'economic_user'@'localhost' IDENTIFIED BY 'contraseña_segura';
GRANT ALL PRIVILEGES ON economic.* TO 'economic_user'@'localhost';
FLUSH PRIVILEGES;

-- Salir
EXIT;
```

### **2.2 Ejecutar Script de Base de Datos**
```bash
# Copiar script al servidor
scp economic.sql economic_user@localhost:/tmp/

# Ejecutar script
mysql -u economic_user -p economic < /tmp/economic.sql
```

### **2.3 Script Completo de Base de Datos**
```sql
-- economic.sql
USE economic;

-- Tablas de usuarios
CREATE TABLE Usuario(
    ID_usuario bigint auto_increment primary key,
    Nombre varchar(255) not null,
    Contraseña varchar(255) not null
);

CREATE TABLE Email(
    ID_correo int auto_increment primary key,
    ID_usuario bigint not null,
    Correo_electronico varchar(255) not null unique,
    foreign key (ID_usuario) references Usuario(ID_usuario)
);

-- Sistema de roles y permisos
CREATE TABLE Rol(
    ID_rol int auto_increment primary key,
    Nombre_rol varchar(100) not null unique
);

CREATE TABLE Permisos(
    ID_permisos int auto_increment primary key,
    Permiso varchar (100) not null unique
);

CREATE TABLE Rol_Permisos(
    ID_rol int not null,
    ID_permisos int not null,
    PRIMARY KEY (ID_rol, ID_permisos),
    foreign key (ID_rol) references Rol(ID_rol),
    foreign key (ID_permisos) references Permisos(ID_permisos)
);

CREATE TABLE Usuario_Rol(
    ID_rol int not null,
    ID_usuario bigint not null,
    primary key (ID_rol, ID_usuario),
    foreign key (ID_rol) references Rol(ID_rol),
    foreign key (ID_usuario) references Usuario(ID_usuario)
);

-- Tablas financieras
CREATE TABLE Categoria(
    ID_categoria int auto_increment primary key,
    Tipo_transaccion enum("Ingreso","Egreso") not null,
    Nombre_categoria varchar(100) not null         
);

CREATE TABLE Usuario_Categoria (
    ID_usuario bigint not null,
    ID_categoria int not null,
    PRIMARY KEY (ID_usuario, ID_categoria),
    FOREIGN KEY (ID_usuario)   REFERENCES Usuario(ID_usuario) ON DELETE CASCADE,
    FOREIGN KEY (ID_categoria) REFERENCES Categoria(ID_categoria) ON DELETE CASCADE
);

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

-- Tablas de soporte
CREATE TABLE Imagenes(
    ID_imagen int auto_increment primary key,
    ID_usuario bigint not null,
    Url_imagen varchar(500) not null,
    foreign key (ID_usuario) references Usuario(ID_usuario)
);

CREATE TABLE Token_recuperacion(
    ID_token int auto_increment primary key,
    ID_usuario bigint not null,
    Token varchar(255) not null unique,
    Fecha_creacion datetime not null,
    Fecha_expriracion datetime,
    Estado boolean not null,
    foreign key (ID_usuario) references Usuario(ID_usuario)
);

-- Insertar datos iniciales
INSERT INTO Rol (Nombre_rol) VALUES ('administrador'), ('usuario');

INSERT INTO Permisos (Permiso) VALUES 
('crear_transacciones'), ('ver_transacciones'), ('editar_transacciones'), ('eliminar_transacciones'),
('crear_categorias'), ('ver_categorias'), ('editar_categorias'), ('eliminar_categorias'),
('crear_metas'), ('ver_metas'), ('editar_metas'), ('eliminar_metas'),
('administrar_usuarios'), ('ver_reportes');

-- Asignar todos los permisos al administrador
INSERT INTO Rol_Permisos (ID_rol, ID_permisos) 
SELECT 1, ID_permisos FROM Permisos;

-- Asignar permisos básicos al usuario
INSERT INTO Rol_Permisos (ID_rol, ID_permisos) VALUES 
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12);

-- Crear vistas
CREATE VIEW Vista_perfil_usuario AS
SELECT U.ID_usuario, U.Nombre, E.Correo_electronico, R.nombre_rol
FROM Usuario U
JOIN Email E on U.ID_usuario = E.ID_usuario
LEFT JOIN Usuario_Rol UR on U.ID_usuario = UR.ID_usuario
LEFT JOIN Rol R on UR.ID_rol = R.ID_rol;

-- Crear índices
CREATE INDEX idx_transaccion_fecha ON Transacciones (Fecha_realizacion);
CREATE INDEX idx_meta_usuario_estado ON Metas_Ahorro (ID_usuario, Estado);
CREATE INDEX idx_token_estado ON Token_recuperacion (Token, Estado);
CREATE INDEX idx_categoria_usuario ON Usuario_Categoria (ID_categoria);
CREATE INDEX idx_rolpermisos_permiso_rol ON Rol_Permisos (ID_permisos, ID_rol);
```

---

## 🔧 **Paso 3: Configuración del Proyecto**

### **3.1 Clonar o Importar el Proyecto**
```bash
# Si está en un repositorio Git
git clone <repositorio-url>
cd economic_backend

# Si es un proyecto existente, copiar archivos al servidor
scp -r Economic/ user@server:/opt/tomcat/webapps/
```

### **3.2 Configurar Conexión a Base de Datos**
```bash
# Editar archivo de configuración
nano src/java/configuracion/ConexionDB.java
```

```java
// Cambiar las credenciales
String url    = "jdbc:mysql://localhost:3306/economic";
String user   = "economic_user";     // Usuario creado
String pass   = "contraseña_segura"; // Contraseña segura
String driver = "com.mysql.cj.jdbc.Driver";
```

### **3.3 Configurar Servicio de Correo**
```bash
# Editar servicio de correo
nano src/java/utils/ServicioCorreo.java
```

```java
// Configurar SMTP
private static final String SMTP_HOST     = "smtp.gmail.com";
private static final String SMTP_PORT     = "587";
private static final String SMTP_USUARIO  = "tu_correo@gmail.com";
private static final String SMTP_CONTRASENA = "tu_contraseña_app"; // Contraseña de aplicación
```

---

## 🔨 **Paso 4: Compilación y Despliegue**

### **4.1 Compilar el Proyecto**
```bash
# Usar Maven
mvn clean package

# O si no hay Maven, compilar manualmente
javac -cp "lib/*:." -d build src/java/**/*.java
```

### **4.2 Crear WAR File**
```bash
# Crear estructura de WAR
mkdir -p economic/WEB-INF/classes
mkdir -p economic/WEB-INF/lib

# Copiar archivos compilados
cp -r build/* economic/WEB-INF/classes/

# Copiar dependencias
cp lib/*.jar economic/WEB-INF/lib/

# Crear web.xml si es necesario
cat > economic/WEB-INF/web.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee 
         https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">
    <display-name>Economic App</display-name>
</web-app>
EOF

# Crear WAR
cd economic
jar -cvf ../economic.war *
cd ..
```

### **4.3 Desplegar en Tomcat**
```bash
# Detener Tomcat
/opt/tomcat/bin/shutdown.sh

# Copiar WAR file
cp economic.war /opt/tomcat/webapps/

# Iniciar Tomcat
/opt/tomcat/bin/startup.sh

# Verificar logs
tail -f /opt/tomcat/logs/catalina.out
```

---

## 🔐 **Paso 5: Configuración de Seguridad**

### **5.1 Configurar HTTPS con SSL**
```bash
# Generar keystore (development)
keytool -genkey -alias tomcat -keyalg RSA -keystore /opt/tomcat/keystore/tomcat.keystore

# Configurar server.xml
nano /opt/tomcat/conf/server.xml
```

```xml
<!-- Agregar conector HTTPS -->
<Connector port="8443"
           protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150"
           SSLEnabled="true"
           scheme="https"
           secure="true"
           keystoreFile="/opt/tomcat/keystore/tomcat.keystore"
           keystorePass="changeit"
           clientAuth="false"
           sslProtocol="TLS" />
```

### **5.2 Configurar Nginx como Reverse Proxy**
```bash
# Instalar Nginx
sudo apt install nginx

# Crear configuración
sudo nano /etc/nginx/sites-available/economic
```

```nginx
server {
    listen 80;
    server_name tu-dominio.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name tu-dominio.com;

    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

```bash
# Activar configuración
sudo ln -s /etc/nginx/sites-available/economic /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

---

## 🔍 **Paso 6: Verificación y Testing**

### **6.1 Verificar Despliegue**
```bash
# Verificar que la aplicación está corriendo
curl -I http://localhost:8080/economic/

# Verificar logs de errores
tail -f /opt/tomcat/logs/catalina.out | grep ERROR

# Verificar conexión a base de datos
mysql -u economic_user -p -e "USE economic; SHOW TABLES;"
```

### **6.2 Testing Funcional**
```bash
# Test de conexión a base de datos
curl -X POST http://localhost:8080/economic/LoginControlador \
     -d "txtCorreo=test@test.com&txtContrasena=test123"

# Test de registro
curl -X POST http://localhost:8080/economic/UsuarioControlador \
     -d "txtNombre=Test User&txtEmail=test@test.com&txtContrasena=password123&txtConfirmar=password123"

# Verificar en base de datos
mysql -u economic_user -p -e "SELECT * FROM economic.Usuario;"
```

---

## 🔧 **Paso 7: Configuración de Producción**

### **7.1 Variables de Entorno**
```bash
# Crear archivo de entorno
sudo nano /etc/environment
```

```bash
# Variables de aplicación
DB_URL=jdbc:mysql://localhost:3306/economic
DB_USER=economic_user
DB_PASSWORD=contraseña_segura
SMTP_HOST=smtp.gmail.com
SMTP_USER=tu_correo@gmail.com
SMTP_PASSWORD=tu_contraseña_app
```

### **7.2 Configuración de Tomcat para Producción**
```bash
# Editar server.xml para producción
nano /opt/tomcat/conf/server.xml
```

```xml
<!-- Configuración de producción -->
<Connector port="8080"
           protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxThreads="200"
           minSpareThreads="10"
           enableLookups="false"
           acceptCount="100"
           compression="on"
           compressionMinSize="2048"
           noCompressionUserAgents="gozilla, traviata"
           compressableMimeType="text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json" />
```

### **7.3 Configuración de JVM**
```bash
# Editar catalina.sh
nano /opt/tomcat/bin/catalina.sh
```

```bash
# Agregar al inicio
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Duser.timezone=UTC"
```

---

## 📊 **Paso 8: Monitoreo y Mantenimiento**

### **8.1 Configurar Logs**
```bash
# Configurar logging en Tomcat
nano /opt/tomcat/conf/logging.properties
```

```properties
# Configuración de logs
handlers = 1catalina.org.apache.juli.AsyncFileHandler, 2localhost.org.apache.juli.AsyncFileHandler, 3manager.org.apache.juli.AsyncFileHandler, 4host-manager.org.apache.juli.AsyncFileHandler, java.util.logging.ConsoleHandler

.level = INFO

1catalina.org.apache.juli.AsyncFileHandler.level = FINE
1catalina.org.apache.juli.AsyncFileHandler.directory = ${catalina.base}/logs
1catalina.org.apache.juli.AsyncFileHandler.prefix = catalina.
1catalina.org.apache.juli.AsyncFileHandler.maxDays = 90
1catalina.org.apache.juli.AsyncFileHandler.limit = 10000000
```

### **8.2 Configurar Backups Automáticos**
```bash
# Crear script de backup
sudo nano /usr/local/bin/backup_economic.sh
```

```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backups/economic"
DB_USER="economic_user"
DB_PASS="contraseña_segura"
DB_NAME="economic"

# Crear directorio
mkdir -p $BACKUP_DIR

# Backup de base de datos
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME > $BACKUP_DIR/db_$DATE.sql

# Backup de aplicación
tar -czf $BACKUP_DIR/app_$DATE.tar.gz /opt/tomcat/webapps/economic

# Limpiar backups antiguos (30 días)
find $BACKUP_DIR -name "*.sql" -mtime +30 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +30 -delete

echo "Backup completado: $DATE"
```

```bash
# Hacer ejecutable y configurar cron
sudo chmod +x /usr/local/bin/backup_economic.sh
sudo crontab -e
```

```bash
# Agregar a cron (diario a las 2 AM)
0 2 * * * /usr/local/bin/backup_economic.sh
```

---

## 🚨 **Paso 9: Solución de Problemas Comunes**

### **9.1 Problemas Frecuentes**

#### **Error: "Connection refused"**
```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql

# Verificar puerto
netstat -tlnp | grep :3306

# Reiniciar MySQL
sudo systemctl restart mysql
```

#### **Error: "Out of Memory"**
```bash
# Aumentar memoria JVM
export JAVA_OPTS="-Xms1g -Xmx4g"

# Verificar uso de memoria
free -h
```

#### **Error: "Database connection failed"**
```bash
# Verificar credenciales
mysql -u economic_user -p -e "SELECT 1;"

# Verificar firewall
sudo ufw status
sudo ufw allow 3306
```

### **9.2 Comandos Útiles**
```bash
# Ver logs de Tomcat
tail -f /opt/tomcat/logs/catalina.out

# Ver procesos Java
ps aux | grep java

# Reiniciar servicios
sudo systemctl restart mysql
sudo systemctl restart nginx
/opt/tomcat/bin/restart.sh

# Verificar puertos
netstat -tlnp | grep -E ':(80|443|8080|8443|3306)'
```

---

## 📈 **Paso 10: Optimización de Rendimiento**

### **10.1 Optimización de Base de Datos**
```sql
-- Configurar MySQL para producción
SET GLOBAL innodb_buffer_pool_size = 1073741824; -- 1GB
SET GLOBAL innodb_log_file_size = 268435456;      -- 256MB
SET GLOBAL max_connections = 200;
SET GLOBAL query_cache_size = 67108864;           -- 64MB
```

### **10.2 Optimización de Tomcat**
```xml
<!-- En server.xml -->
<Executor name="tomcatThreadPool" namePrefix="tomcat-http-"
           maxThreads="200" minSpareThreads="10" maxIdleTime="60000"/>

<Connector executor="tomcatThreadPool"
           port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

---

## 🎯 **Checklist de Producción**

### **✅ Antes del Lanzamiento:**
- [ ] Base de datos configurada y con datos iniciales
- [ ] Credenciales hardcodeadas reemplazadas
- [ ] HTTPS configurado
- [ ] Logs configurados
- [ ] Backups automáticos configurados
- [ ] Monitoreo básico configurado
- [ ] Testing funcional completado

### **✅ Seguridad:**
- [ ] Contraseñas seguras
- [ ] Firewall configurado
- [ ] SSL/TLS implementado
- [ ] Actualizaciones de seguridad aplicadas
- [ ] Logs de acceso configurados

### **✅ Rendimiento:**
- [ ] JVM optimizada
- [ ] Base de datos optimizada
- [ ] Caching implementado
- [ ] Compresión habilitada
- [ ] Monitoreo de recursos

---

## 🎉 **¡Instalación Completada!**

El sistema Economic debería estar ahora funcionando en:

- **URL principal**: `http://localhost:8080/economic/`
- **URL segura**: `https://tu-dominio.com/` (si configuraste HTTPS)
- **Panel de administración**: `http://localhost:8080/economic/AdminControlador`

### **Usuarios de Prueba:**
- **Administrador**: Crear manualmente en base de datos con rol ID=1
- **Usuario**: Registrarse normalmente (recibirá rol ID=2 automáticamente)

---

## 📞 **Soporte y Mantenimiento**

### **Recursos Útiles:**
- **Documentación completa**: `/docs/`
- **Logs de aplicación**: `/opt/tomcat/logs/`
- **Logs de Nginx**: `/var/log/nginx/`
- **Logs de MySQL**: `/var/log/mysql/`

### **Contacto de Soporte:**
- **Issues técnicos**: Revisar logs primero
- **Problemas de base de datos**: Verificar conexión y permisos
- **Problemas de red**: Verificar firewall y puertos

---

**🚀 El sistema está listo para producción!**
