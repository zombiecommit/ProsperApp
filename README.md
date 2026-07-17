# ProsperApp 🌿

Aplicación de escritorio desarrollada en **JavaFX** para la gestión de proyectos mediante tableros Kanban, permitiendo organizar funcionalidades, subtareas, notas, decisiones técnicas y fragmentos de código.

## Autores

- Daniel Escobar Escobar
- Howard Duvan Sánchez Garcia

---

# Requisitos

Antes de ejecutar la aplicación es necesario tener instalado:

- Java JDK 21 o superior
- Maven
- Docker Desktop
- pgAdmin 4 (opcional, para administrar la base de datos)

---

# Configuración de la Base de Datos

## 1. Crear el contenedor PostgreSQL

Ejecutar:

```bash
docker run --name prosperapp-postgres ^
-e POSTGRES_PASSWORD=prosperapp123 ^
-e POSTGRES_DB=prosperapp_db ^
-p 5432:5432 ^
-d postgres
```

Verificar que el contenedor esté ejecutándose:

```bash
docker ps
```

Si el contenedor ya existe y está detenido:

```bash
docker start prosperapp-postgres
```

---

## 2. Crear las tablas

Abrir **pgAdmin** y conectarse al servidor PostgreSQL usando:

| Parámetro | Valor |
|-----------|-------|
| Host | localhost |
| Puerto | 5432 |
| Usuario | postgres |
| Contraseña | prosperapp123 |

Seleccionar la base de datos:

```
prosperapp_db
```

Ejecutar el script SQL del proyecto para crear todas las tablas.

---

# Configuración de la conexión

La aplicación utiliza la siguiente configuración:

```java
URL:
jdbc:postgresql://localhost:5432/prosperapp_db

Usuario:
postgres

Contraseña:
prosperapp123
```

Si se modifica alguno de estos valores, debe actualizarse el archivo:

```
DatabaseConnection.java
```

---

# Ejecutar la aplicación

Desde el proyecto ejecutar:

```bash
mvn clean javafx:run
```

O ejecutar directamente la clase:

```
Launcher.java
```

La aplicación abrirá la pantalla de inicio de sesión.

---

# Flujo de uso

1. Registrar un nuevo usuario.
2. Iniciar sesión.
3. Crear proyectos.
4. Crear secciones del tablero Kanban.
5. Agregar funcionalidades.
6. Gestionar:
   - Subtareas
   - Notas
   - Decisiones técnicas
   - Fragmentos de código

Toda la información se almacena en PostgreSQL mediante los DAO implementados en el proyecto.

---

# Tecnologías utilizadas

- Java 21
- JavaFX
- PostgreSQL
- JDBC
- Maven
- Docker
- BCrypt (cifrado de contraseñas)
