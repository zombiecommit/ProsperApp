-- FUNCIÓN: Contar la cantidad de funcionalidades de un proyecto
-- Recibe el id de un proyecto y devuelve el número total de funcionalidades asociadas a todas sus secciones.

CREATE OR REPLACE FUNCTION contar_funcionalidades(id_proyecto_param INTEGER)
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
DECLARE
    -- Variable donde se almacenará el total de funcionalidades
    total INTEGER;
BEGIN

    -- Cuenta todas las funcionalidades pertenecientes al proyecto
    SELECT COUNT(*)
    INTO total
    FROM funcionalidad f
    JOIN seccion s
        ON f.id_seccion = s.id_seccion
    WHERE s.id_proyecto = id_proyecto_param;

    -- Devuelve el total encontrado
    RETURN total;

END;
$$;

-- PROCEDIMIENTO: Crear un nuevo proyecto
-- Inserta un proyecto en la base de datos utilizando los datos recibidos como parámetros.

CREATE OR REPLACE PROCEDURE crear_proyecto(
    p_id_usuario INTEGER,
    p_nombre VARCHAR(150),
    p_descripcion TEXT,
    p_fecha_limite DATE,
    p_estado VARCHAR(30)
)
LANGUAGE plpgsql
AS
$$
BEGIN

    -- Inserta un nuevo proyecto
    INSERT INTO proyecto
        (id_usuario, nombre, descripcion, fecha_limite, estado)
    VALUES
        (p_id_usuario, p_nombre, p_descripcion, p_fecha_limite, p_estado);

END;
$$;