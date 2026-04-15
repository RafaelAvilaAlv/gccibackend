INSERT INTO roles (nombre, descripcion)
VALUES ('ADMIN', 'Administrador')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

INSERT INTO roles (nombre, descripcion)
VALUES ('EMPLEADO', 'Empleado del negocio')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

INSERT INTO roles (nombre, descripcion)
VALUES ('USER', 'Usuario normal')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);