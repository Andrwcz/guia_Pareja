CREATE TABLE empleado (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    cargo VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    salario NUMERIC(10,2) NOT NULL CHECK (salario >= 0),
    fecha_contratacion DATE NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('Activo', 'Inactivo'))
);


INSERT INTO empleado
(nombres, apellidos, cedula, correo, telefono, cargo, departamento, salario, fecha_contratacion, estado)
VALUES
('Andrés', 'Centeno', '001-010101-0001A', 'andres@gmail.com', '88881111', 'Desarrollador', 'Tecnología', 18000.00, '2026-01-15', 'Activo'),

('Carlos', 'López', '001-020202-0002B', 'carlos@gmail.com', '88882222', 'Contador', 'Finanzas', 16000.00, '2025-11-10', 'Activo'),

('María', 'Gómez', '001-030303-0003C', 'maria@gmail.com', '88883333', 'Recursos Humanos', 'Recursos Humanos', 15000.00, '2025-09-20', 'Activo'),

('José', 'Martínez', '001-040404-0004D', 'jose@gmail.com', '88884444', 'Soporte Técnico', 'Tecnología', 14000.00, '2026-02-05', 'Activo'),

('Laura', 'Rodríguez', '001-050505-0005E', 'laura@gmail.com', '88885555', 'Secretaria', 'Administración', 12000.00, '2025-08-12', 'Inactivo');


SELECT * FROM empleado;

SELECT nombres, apellidos, cargo
FROM empleado;

SELECT *
FROM empleado
WHERE departamento = 'Tecnología';

SELECT *
FROM empleado
WHERE salario > 15000;

SELECT *
FROM empleado
ORDER BY salario DESC;

SELECT COUNT(*) AS total_empleados
FROM empleado;

SELECT AVG(salario) AS salario_promedio
FROM empleado;

SELECT SUM(salario) AS total_salarios
FROM empleado;

SELECT *
FROM empleado
WHERE estado = 'Activo';

SELECT departamento, COUNT(*) AS cantidad
FROM empleado
GROUP BY departamento;