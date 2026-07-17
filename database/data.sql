INSERT INTO usuario (nombre, correo, contrasena)
VALUES
    ('Daniel Perez', 'daniel@example.com', 'hash_contrasena_1'),
    ('Howard Castillo', 'howard@example.com', 'hash_contrasena_2'),
    ('Carlos Ramirez', 'carlos@example.com', 'hash_contrasena_3');

INSERT INTO proyecto (id_usuario, nombre, descripcion, fecha_limite, estado)
VALUES
    (1, 'Sistema de Inventario',
     'Aplicacion para gestionar productos y existencias.',
     '2026-08-30', 'activo'),

    (1, 'Aplicacion de Finanzas',
     'Aplicacion para controlar ingresos y gastos personales.',
     '2026-10-15', 'activo'),

    (2, 'Portafolio Web',
     'Sitio web para presentar proyectos profesionales.',
     '2026-09-20', 'activo');

INSERT INTO seccion (id_proyecto, nombre, orden)
VALUES
    (1, 'Pendientes', 1),
    (1, 'En proceso', 2),
    (1, 'Completado', 3),
    (1, 'Lanzado', 4),

    (2, 'Pendientes', 1),
    (2, 'En proceso', 2),
    (2, 'Finalizado', 3),

    (3, 'Pendientes', 1),
    (3, 'En proceso', 2),
    (3, 'Completado', 3);

INSERT INTO funcionalidad
    (id_seccion, titulo, descripcion, prioridad, fecha_limite)
VALUES
    (1, 'Registrar productos',
     'Permitir registrar nuevos productos en el inventario.',
     'alta', '2026-08-05'),

    (1, 'Controlar existencias',
     'Permitir consultar la cantidad disponible de cada producto.',
     'alta', '2026-08-15'),

    (2, 'Generar reportes',
     'Generar reportes sobre los productos almacenados.',
     'media', '2026-08-25'),

    (5, 'Registrar ingresos',
     'Permitir registrar los ingresos personales del usuario.',
     'alta', '2026-09-20'),

    (6, 'Mostrar balance',
     'Calcular y mostrar el balance financiero del usuario.',
     'media', '2026-10-05'),

    (8, 'Mostrar proyectos',
     'Presentar los proyectos realizados en el portafolio.',
     'alta', '2026-09-01');

INSERT INTO subtarea (id_funcionalidad, descripcion, estado)
VALUES
    (1, 'Diseñar formulario de registro de productos', 'completada'),
    (1, 'Validar los datos ingresados', 'pendiente'),
    (2, 'Crear consulta de existencias', 'pendiente'),
    (3, 'Diseñar formato de los reportes', 'pendiente'),
    (4, 'Crear formulario de ingresos', 'completada'),
    (5, 'Implementar cálculo del balance', 'pendiente'),
    (6, 'Diseñar sección de proyectos', 'pendiente');


INSERT INTO nota (id_funcionalidad, contenido)
VALUES
    (1, 'Validar que el código de cada producto sea único.'),
    (2, 'Mostrar una alerta cuando las existencias sean bajas.'),
    (3, 'Los reportes deben permitir visualizar la información de forma clara.'),
    (4, 'Permitir el registro de diferentes fuentes de ingreso.'),
    (6, 'Agregar una descripción y las tecnologías utilizadas en cada proyecto.');


INSERT INTO decision_tecnica
    (id_funcionalidad, titulo, descripcion)
VALUES
    (1, 'Uso de PostgreSQL',
     'Se utilizará PostgreSQL como sistema gestor de base de datos.'),

    (2, 'Consulta de existencias',
     'Las existencias se consultarán directamente desde la base de datos.'),

    (3, 'Formato de reportes',
     'Los reportes se generarán utilizando los datos almacenados en PostgreSQL.'),

    (5, 'Cálculo del balance',
     'El balance se calculará a partir de los ingresos y gastos registrados.'),

    (6, 'Diseño del portafolio',
     'Los proyectos se presentarán mediante tarjetas organizadas visualmente.');


INSERT INTO fragmento_codigo (id_funcionalidad, lenguaje, codigo)
VALUES
    (1, 'SQL', 'SELECT * FROM producto WHERE codigo = ?;'),
    (4, 'Java', 'public double calcularBalance() { return ingresos - gastos; }');