CREATE TABLE usuario (
    id_usuario INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE proyecto (
    id_proyecto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_limite DATE,
    estado VARCHAR(30) NOT NULL DEFAULT 'activo',

    CONSTRAINT fk_proyecto_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);

CREATE TABLE seccion (
    id_seccion INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_proyecto INTEGER NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    orden INTEGER NOT NULL,

    CONSTRAINT fk_seccion_proyecto
        FOREIGN KEY (id_proyecto)
        REFERENCES proyecto(id_proyecto)
        ON DELETE CASCADE
);

CREATE TABLE funcionalidad (
    id_funcionalidad INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_seccion INTEGER NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    prioridad VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_limite DATE,

    CONSTRAINT fk_funcionalidad_seccion
        FOREIGN KEY (id_seccion)
        REFERENCES seccion(id_seccion)
        ON DELETE CASCADE
);

CREATE TABLE subtarea (
    id_subtarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_funcionalidad INTEGER NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'pendiente',

    CONSTRAINT fk_subtarea_funcionalidad
        FOREIGN KEY (id_funcionalidad)
        REFERENCES funcionalidad(id_funcionalidad)
        ON DELETE CASCADE
);

CREATE TABLE nota (
    id_nota INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_funcionalidad INTEGER NOT NULL,
    contenido TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_nota_funcionalidad
        FOREIGN KEY (id_funcionalidad)
        REFERENCES funcionalidad(id_funcionalidad)
        ON DELETE CASCADE
);

CREATE TABLE decision_tecnica (
    id_decision      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_funcionalidad INTEGER      NOT NULL,
    titulo           VARCHAR(150) NOT NULL,
    descripcion      TEXT         NOT NULL,
    fecha_creacion   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fragmento_codigo (
    id_fragmento INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_funcionalidad INTEGER NOT NULL,
    lenguaje VARCHAR(30),
    codigo TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fragmento_funcionalidad
        FOREIGN KEY (id_funcionalidad)
        REFERENCES funcionalidad(id_funcionalidad)
        ON DELETE CASCADE
);