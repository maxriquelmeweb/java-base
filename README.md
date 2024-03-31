# Sistema de Gestión de Usuarios y Roles con Spring Boot

Este proyecto es un sistema base construido con Spring Boot que facilita la gestión de usuarios y roles, ideal para aplicaciones que requieren control de acceso y niveles de permisos.

## Configuración Inicial

### Clonar el Proyecto

Después de clonar el proyecto desde el repositorio, sigue estos pasos para configurarlo correctamente:

1. **Verifica y Actualiza Dependencias**:
   Comprueba que todas las dependencias del proyecto estén actualizadas y sean compatibles con tu entorno de desarrollo.

### Creación de la Base de Datos

2. **Configuración de la Base de Datos**:
   Antes de ejecutar la aplicación, ajusta la configuración de la base de datos en `application.properties` para reflejar tu entorno de desarrollo local.

### Creación Automática de Tablas

3. **Crear Tablas desde las Entidades**:
   Para generar automáticamente las tablas de la base de datos a partir de las entidades de JPA, establece las siguientes propiedades en `application.properties`:

   spring.jpa.hibernate.ddl-auto=create
   spring.profiles.active=dev
   spring.sql.init.platform=dev
   spring.sql.init.mode=never

### Inserción de Roles por Defecto (Opcional)
4. **Insertar Roles por Defecto**:

Si prefieres insertar roles por defecto al iniciar la aplicación, cambia la configuración a:

spring.jpa.hibernate.ddl-auto=update
spring.profiles.active=dev
spring.sql.init.platform=dev
spring.sql.init.mode=always

Con esto, data-dev.sql se ejecutarán automáticamente al iniciar la aplicación.

### Inserción del Administrador (Opcional)
5. **Insertar Administrador**:

Para agregar un usuario administrador:

Modifica el archivo de ejemplo data-prod-example.sql con un correo electrónico válido y una contraseña encriptada. Renombra el archivo a data-prod.sql.

6. **Ajusta application.properties para usar el perfil de producción**:

spring.jpa.hibernate.ddl-auto=update
spring.profiles.active=prod
spring.sql.init.platform=prod
spring.sql.init.mode=always

Inicia la aplicación y el usuario administrador se insertará en la base de datos.

### Desactivación de la Autoconfiguración DDL (Opcional)
7. **Desactivar DDL Automática**:

Una vez que hayas completado la configuración inicial, desactiva la autoconfiguración DDL para prevenir cambios automáticos en la estructura de la base de datos en futuras ejecuciones:

spring.jpa.hibernate.ddl-auto=none
spring.profiles.active=dev
spring.sql.init.platform=dev
spring.sql.init.mode=never