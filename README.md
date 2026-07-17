# ProsperApp

## Integrantes

- Daniel Escobar Escobar 2437924
- Howard Duvan Sanchez Garcia 2438723

# Descripción
ProsperApp es una aplicación de escritorio desarrollada en **Java** utilizando **JavaFX** para la interfaz gráfica y **PostgreSQL** como sistema gestor de base de datos.

La aplicación permite organizar proyectos personales de software mediante un tablero tipo Kanban, donde cada proyecto puede dividirse en secciones y funcionalidades. Además, cada funcionalidad puede administrar subtareas, notas, decisiones técnicas y fragmentos de código.

# Tecnologías utilizadas

- Java 21
- JavaFX
- Maven
- PostgreSQL 15
- pgAdmin 4
- Docker Desktop
- JDBC
- BCrypt

---

# Requisitos

Antes de ejecutar el proyecto es necesario tener instalado:

- Git
- Java JDK 21
- Maven
- Docker Desktop
- IntelliJ IDEA (recomendado)

---

# Clonar el proyecto

```bash
git clone https://github.com/USUARIO/ProsperApp.git
```

Entrar al proyecto

```bash
cd ProsperApp
```

---

# Crear los contenedores Docker

## PostgreSQL

```bash
docker run --name prosperapp-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=prosperapp123 -e POSTGRES_DB=prosperapp_db -p 5432:5432 postgres:15
```

## pgAdmin

```bash
docker run --name prosperapp-pgadmin --link prosperapp-postgres:postgres -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=pg123 -p 5050:80 dpage/pgadmin4
```

---

# Iniciar los contenedores

Si ya fueron creados anteriormente:

```bash
docker start prosperapp-postgres
docker start prosperapp-pgadmin
```

Verificar que estén ejecutándose:

```bash
docker ps
```

Deberán aparecer los contenedores:

- prosperapp-postgres
- prosperapp-pgadmin

---

# Configurar pgAdmin

Abrir en el navegador:

```
http://localhost:5050
```

Iniciar sesión con:

Correo

```
admin@admin.com
```

Contraseña

```
pg123
```

Registrar un nuevo servidor con la siguiente configuración.

## General

Nombre

```
ProsperApp
```

## Connection

Host

```
prosperapp-postgres
```

Puerto

```
5432
```

Maintenance Database

```
prosperapp_db
```

Usuario

```
postgres
```

Contraseña

```
prosperapp123
```

Guardar la configuración.

---

# Crear la base de datos

Abrir el Query Tool de pgAdmin y ejecutar los archivos SQL en el siguiente orden:

1. schema.sql
2. data.sql
3. advanced.sql

Es importante respetar este orden para evitar errores de dependencias entre tablas, datos y objetos de base de datos.

---

# Abrir el proyecto

Abrir la carpeta del proyecto con IntelliJ IDEA.

Esperar a que Maven descargue automáticamente todas las dependencias.

Verificar que el SDK configurado sea:

```
Java 21
```

---

# Configuración de la conexión

Verificar que la clase encargada de la conexión a PostgreSQL tenga la siguiente configuración:

Host

```
localhost
```

Puerto

```
5432
```

Base de datos

```
prosperapp_db
```

Usuario

```
postgres
```

Contraseña

```
prosperapp123
```

---

# Ejecutar la aplicación

Ejecutar la clase principal del proyecto desde IntelliJ IDEA.

Una vez iniciada la aplicación será posible:

- Iniciar sesión.
- Crear proyectos.
- Administrar secciones.
- Crear funcionalidades.
- Gestionar subtareas.
- Gestionar notas.
- Gestionar decisiones técnicas.
- Gestionar fragmentos de código.

---

# Estructura del proyecto

```
src
│
├── database
│   ├── schema.sql
│   ├── data.sql
│   └── advanced.sql
│
├── dao
│
├── model
│
├── util
│
├── view
│
└── resources
```

---

# Arquitectura

El proyecto sigue una arquitectura por capas.

- **database:** Scripts SQL de la base de datos.
- **model:** Entidades del sistema.
- **dao:** Acceso a datos mediante JDBC.
- **util:** Clases reutilizables.
- **view:** Interfaces gráficas desarrolladas con JavaFX.
- **resources:** Recursos gráficos y hojas de estilo.

---

# Funcionalidades principales

- Gestión de proyectos.
- Organización mediante tablero Kanban.
- Administración de funcionalidades.
- Gestión de subtareas.
- Gestión de notas.
- Gestión de decisiones técnicas.
- Gestión de fragmentos de código.
- Persistencia de datos en PostgreSQL.

---

# Autores

Daniel Escobar

Howard Sánchez
