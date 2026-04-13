/* =====================================================
   SELECCIONAR BASE DE DATOS
   ===================================================== */

CREATE DATABASE IF NOT EXISTS bbjn4mpo2wssuphikk8t;
USE bbjn4mpo2wssuphikk8t;


/* =====================================================
   ELIMINAR TABLAS (orden correcto por FK)
   ===================================================== */

DROP TABLE IF EXISTS prestamo;
DROP TABLE IF EXISTS libro;
DROP TABLE IF EXISTS socio;


/* =====================================================
   TABLA LIBRO
   ===================================================== */

CREATE TABLE libro (
                       codigo INT AUTO_INCREMENT,
                       isbn VARCHAR(20) NOT NULL UNIQUE,
                       titulo VARCHAR(100) NOT NULL,
                       escritor VARCHAR(100) NOT NULL,
                       anio_publicacion INT NOT NULL,
                       puntuacion DECIMAL(3,1) NOT NULL,

                       PRIMARY KEY (codigo)
) ENGINE=InnoDB;


/* =====================================================
   TABLA SOCIO
   ===================================================== */

CREATE TABLE socio (
                       codigo INT AUTO_INCREMENT,
                       dni VARCHAR(20) NOT NULL UNIQUE,
                       nombre VARCHAR(100) NOT NULL,
                       domicilio VARCHAR(200) NOT NULL,
                       telefono VARCHAR(20) NOT NULL,
                       correo VARCHAR(100) NOT NULL,

                       PRIMARY KEY (codigo)
) ENGINE=InnoDB;


/* =====================================================
   TABLA PRESTAMO
   ===================================================== */

CREATE TABLE prestamo (
                          codigo_libro INT NOT NULL,
                          codigo_socio INT NOT NULL,
                          fecha_inicio DATE NOT NULL,
                          fecha_fin DATE NOT NULL,
                          fecha_devolucion DATE,

                          PRIMARY KEY (codigo_libro, codigo_socio, fecha_inicio),

                          CONSTRAINT fk_prestamo_libro
                              FOREIGN KEY (codigo_libro)
                                  REFERENCES libro(codigo)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE,

                          CONSTRAINT fk_prestamo_socio
                              FOREIGN KEY (codigo_socio)
                                  REFERENCES socio(codigo)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
) ENGINE=InnoDB;


/* =====================================================
   INSERT LIBRO
   ===================================================== */

INSERT INTO libro (isbn, titulo, escritor, anio_publicacion, puntuacion)
VALUES ('9788401023456','El Quijote','Miguel de Cervantes',1605,9.8);


/* =====================================================
   INSERT SOCIO
   ===================================================== */

INSERT INTO socio (dni, nombre, domicilio, telefono, correo)
VALUES ('12345678A','Juan Perez','Calle Mayor 12','600123456','juan@email.com');


/* =====================================================
   INSERT PRESTAMO
   ===================================================== */

INSERT INTO prestamo (codigo_libro, codigo_socio, fecha_inicio, fecha_fin, fecha_devolucion)
VALUES (1,1,'2026-03-10','2026-03-20',NULL);