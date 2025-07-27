-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-07-2025 a las 07:56:57
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bibliotecadb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comentarios`
--

CREATE TABLE `comentarios` (
  `id` int(11) NOT NULL,
  `id_usuario` varchar(10) DEFAULT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `comentario` text NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `comentarios`
--

INSERT INTO `comentarios` (`id`, `id_usuario`, `isbn`, `comentario`, `fecha`) VALUES
(1, 'user1', '9780140449136', 'Una lectura desafiante pero muy rica en contenido.', '2025-06-20 10:00:00'),
(2, 'user2', '9780141439600', 'Me encantó el desarrollo de los personajes.', '2025-06-22 10:00:00'),
(3, 'user3', '9780307474278', 'Un clásico distópico que sigue siendo relevante.', '2025-06-23 10:00:00'),
(4, 'user4', '9780439023481', 'Muy emocionante, no pude dejar de leerlo.', '2025-06-25 10:00:00'),
(5, 'user5', '9780307949486', 'Realismo mágico en su máxima expresión.', '2025-06-26 10:00:00'),
(6, 'user6', '9780061120084', 'Una historia poderosa sobre justicia y prejuicio.', '2025-06-27 10:00:00'),
(7, 'user7', '9788491050654', 'Un poco largo, pero muy entretenido.', '2025-06-28 10:00:00'),
(8, 'user8', '9788401337635', 'Intrigante y lleno de giros inesperados.', '2025-06-29 10:00:00'),
(9, 'user9', '9780553380163', 'Muy interesante para los amantes de la ciencia.', '2025-06-30 10:00:00'),
(10, 'user10', '9788423348463', 'Inspirador y fácil de leer.', '2025-07-01 10:00:00'),
(11, 'ADMIN', '9780140449136', 'Uy, que linda la trama, me encanta', '2025-07-12 10:00:00'),
(12, 'admin', '9780140449136', 'Una lectura desafiante y muy entretenida.', '2025-08-05 10:00:00'),
(13, 'admin', '9780061120084', 'Muy bueno', '2025-07-12 10:00:00'),
(14, 'Ejuan', '9780307949486', 'Me encanto este libro, es muy bueno', '2025-07-20 10:00:00'),
(15, 'user1', '9780141439600', 'Me gusto demasiado', '2025-07-21 10:00:00'),
(16, 'Ehonorio', '9780307474278', 'muy buen libro, excelente', '2025-07-26 05:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `isbn` varchar(20) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `autor` varchar(100) NOT NULL,
  `anio_publicacion` varchar(10) NOT NULL,
  `estado_reserva` varchar(20) DEFAULT 'Disponible'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`isbn`, `titulo`, `autor`, `anio_publicacion`, `estado_reserva`) VALUES
('9780061120084', 'Matar a un ruiseñor', 'Harper Lee', '2006', 'Reservado'),
('9780140449136', 'La Odisea', 'Homero', '1996', 'Reservado'),
('9780141439600', 'Orgullo y Prejuicio', 'Jane Austen', '2002', 'Disponible'),
('9780307474278', '1984', 'George Orwell', '2013', 'Reservado'),
('9780307949486', 'Cien Años de Soledad', 'Gabriel García Márquez', '2014', 'Disponible'),
('9780439023481', 'Los juegos del hambre', 'Suzanne Collins', '2008', 'Disponible'),
('9780553380163', 'Una breve historia del tiempo', 'Stephen Hawking', '1998', 'Disponible'),
('9786124970306', 'Historia de la literatura peruana', 'Cesar Toro Montalvo', '2024', 'Disponible'),
('9786129909301', 'Olvidarte sería conocer el olvido', 'Juan Gonzalo Rose', '2025', 'Reservado'),
('9788401337635', 'El código Da Vinci', 'Dan Brown', '2003', 'Disponible'),
('9788423348463', 'El Alquimista', 'Paulo Coelho', '1997', 'Disponible'),
('9788491050654', 'Don Quijote de la Mancha', 'Miguel de Cervantes', '2015', 'Disponible');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `multas`
--

CREATE TABLE `multas` (
  `id` int(11) NOT NULL,
  `id_usuario` varchar(10) DEFAULT NULL,
  `monto` decimal(6,2) DEFAULT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `fecha` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `multas`
--

INSERT INTO `multas` (`id`, `id_usuario`, `monto`, `motivo`, `fecha`) VALUES
(1, 'user1', 5.00, 'Retraso en devolución de libro', '2025-06-15'),
(2, 'user2', 30.00, 'Pérdida de libro', '2025-05-22'),
(3, 'user3', 3.00, 'Devolución con daño menor (páginas dobladas)', '2025-07-01'),
(4, 'user4', 10.00, 'Retraso prolongado (más de 30 días)', '2025-06-10'),
(5, 'user5', 2.00, 'Libro devuelto con rayones', '2025-06-29'),
(6, 'user6', 7.00, 'No asistencia a cita de préstamo reservado', '2025-06-20'),
(7, 'user7', 15.00, 'Pérdida y no reposición de material', '2025-05-30'),
(8, 'user8', 4.50, 'Retraso en devolución de libro', '2025-06-05'),
(9, 'user9', 9.00, 'Incumplimiento en renovación', '2025-06-25'),
(10, 'user10', 6.00, 'Daño en la portada del libro', '2025-07-02'),
(11, 'admin', 8.00, 'Retraso en devolución de libro', '2025-04-22'),
(12, 'user5', 30.00, 'Libro perdido', '2025-07-13'),
(13, 'user4', 7.00, 'Devolución tardía', '2025-07-13'),
(14, 'user3', 30.00, 'Libro perdido', '2025-07-26');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamos`
--

CREATE TABLE `prestamos` (
  `id` int(11) NOT NULL,
  `id_usuario` varchar(10) DEFAULT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `fecha_prestamo` date NOT NULL,
  `fecha_devolucion` date DEFAULT NULL,
  `estado` enum('En curso','Devuelto','Perdido') DEFAULT 'En curso'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamos`
--

INSERT INTO `prestamos` (`id`, `id_usuario`, `isbn`, `fecha_prestamo`, `fecha_devolucion`, `estado`) VALUES
(1, 'user1', '9780140449136', '2025-06-15', '2025-06-29', 'En curso'),
(2, 'user2', '9780141439600', '2025-06-18', '2025-07-02', 'Perdido'),
(3, 'user3', '9780307474278', '2025-06-20', '2025-07-04', 'Perdido'),
(4, 'user4', '9780439023481', '2025-06-22', '2025-07-06', 'Devuelto'),
(5, 'user5', '9780307949486', '2025-06-23', '2025-07-07', 'Perdido'),
(6, 'user6', '9780061120084', '2025-06-24', '2025-07-08', 'Devuelto'),
(7, 'user7', '9788491050654', '2025-06-25', '2025-07-09', 'En curso'),
(8, 'user8', '9788401337635', '2025-06-26', '2025-07-10', 'En curso'),
(9, 'user9', '9780553380163', '2025-06-27', '2025-07-11', 'En curso'),
(10, 'user10', '9788423348463', '2025-06-28', '2025-07-12', 'En curso'),
(11, 'user4', '9788423348463', '2025-06-15', '2025-07-01', 'En curso'),
(12, 'admin', '9788401337635', '2025-06-15', '2025-07-01', 'En curso'),
(13, 'user9', '9780140449136', '2025-06-19', '2025-07-19', 'En curso'),
(14, 'admin', '9780061120084', '2025-07-12', '2025-07-27', 'Devuelto'),
(15, 'admin', '9780061120084', '2025-07-12', '2025-07-27', 'Devuelto'),
(16, 'ADMIN', '9780061120084', '2025-07-12', '2025-07-27', 'Devuelto'),
(17, 'ADMIN', '9780140449136', '2025-07-12', '2025-07-27', 'En curso'),
(18, 'Ejuan', '9780307949486', '2025-07-20', '2025-08-04', 'Devuelto'),
(19, 'Ejuanito', '9786129909301', '2025-07-21', '2025-08-05', 'En curso'),
(20, 'Ehonorio', '9780307474278', '2025-07-26', '2025-08-10', 'En curso');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` varchar(10) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `rol` enum('Estudiante','Bibliotecario','Administrador') NOT NULL,
  `clave` varchar(100) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `carrera` varchar(100) DEFAULT NULL,
  `codigo_institucional` varchar(20) DEFAULT NULL,
  `anio_ingreso` varchar(20) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `cargo` varchar(50) DEFAULT NULL,
  `jornada` varchar(50) DEFAULT NULL,
  `horario_laboral` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `rol`, `clave`, `telefono`, `direccion`, `carrera`, `codigo_institucional`, `anio_ingreso`, `departamento`, `cargo`, `jornada`, `horario_laboral`) VALUES
('admin', 'Admin Principal1', 'admin@mail.com', 'Estudiante', '1234', '952624852', 'moquegua', 'EPISI', '2021204521', '2021', NULL, NULL, NULL, NULL),
('admin2', 'Teff', 'teff@gmail.com', 'Administrador', '9876', '974586578', 'AH nuevo ilo', NULL, NULL, NULL, 'Gestión Académica', 'Jefe de Área', 'Completa', NULL),
('admin3', 'Maria', 'maria@gmail.com', 'Administrador', '4321', '964123157', 'Terrazas', NULL, NULL, NULL, 'Gestión Académica', 'Jefe de Área', 'parcial', NULL),
('biblio1', 'Karla Mamani', 'karla1@gmail.com', 'Bibliotecario', '4512', '915848965', 'Av nuevo atardecer', NULL, NULL, NULL, NULL, NULL, NULL, 'mañana'),
('biblio2', 'flor Mamani', 'flor1@gmail.com', 'Bibliotecario', '7563', '951635141', 'alto ilo', NULL, NULL, NULL, NULL, NULL, NULL, 'tarde'),
('biblio3', 'Tamara', 'tamara@gmail.com', 'Bibliotecario', '9876', '985632144', 'ilo', NULL, NULL, NULL, NULL, NULL, NULL, 'tarde'),
('Ehonorio', 'Honorio Apaza', 'honorio@aymaralab.com', 'Estudiante', '1234', '965423157', 'Terrazas', 'EPIAM', '2021202452', '2021', NULL, NULL, NULL, NULL),
('Ejuan', 'Juan Carlos', 'juanCa@gmail.com', 'Estudiante', '147', '987564254', 'jr. puerto de ilo', 'EPIAM', '2020204015', '2020', NULL, NULL, NULL, NULL),
('Ejuanito', 'Juanito', 'juanito@gmail.com', 'Estudiante', '1234', '987564231', 'ilo', 'EPIM', '2019201520', '2019', NULL, NULL, NULL, NULL),
('Ekarla', 'Karla', 'karla123@gmail.com', 'Estudiante', '1254', '987564123', 'av. Guadalupe', 'EPIAM', '2024201415', '2024', NULL, NULL, NULL, NULL),
('Elucho', 'Lucho', 'lucho123@gmail.com', 'Estudiante', '12365', '985641232', 'iloilo', 'EPIAM', '2021204521', '2021', NULL, NULL, NULL, NULL),
('user1', 'Maria', 'maria@gmail.com', 'Estudiante', '1234', '953272898', 'AH Siglo XXI', 'EPIM', '2021204021', '2021', NULL, NULL, NULL, NULL),
('user10', 'Daniel Flores', 'daniel.flores@example.com', 'Estudiante', 'danielpass', '933221100', 'Av. La Marina 615', 'EPIAM', '2015201523', '2015', NULL, NULL, NULL, NULL),
('user11', 'Carlos Zapata', 'carlos@gmail.com', 'Bibliotecario', '120421', '948562175', 'Av Juliaca 451', NULL, NULL, NULL, NULL, NULL, NULL, 'mañana'),
('user12', 'Karla Argote', 'karla@gmail.com', 'Estudiante', '6352', '915248965', 'Av nuevo amanecer', 'EPISI', '2020214011', '2020', NULL, NULL, NULL, NULL),
('user2', 'flor', 'flor@gmail.com', 'Estudiante', '1452', '952635141', 'ilo', 'CONTABILIDAD', '2021204034', '2021', NULL, NULL, NULL, NULL),
('user3', 'Stefany Colque', 'stefany1gmail.com', 'Bibliotecario', '4562', '963258741', 'Nuevo ilo', NULL, NULL, NULL, NULL, NULL, NULL, 'tarde'),
('user4', 'Pedro Sánchez', 'pedro.sanchez@example.com', 'Estudiante', 'biblioteca22', '998877665', 'Av. Grau 102', 'EPISI', '2020201532', '2020', NULL, NULL, NULL, NULL),
('user5', 'Lucía Herrera', 'lucia.herrera@example.com', 'Estudiante', 'lucia2025', '955112233', 'Jr. Amazonas 500', 'EPISI', '2019204056', '2019', NULL, NULL, NULL, NULL),
('user6', 'Carlos Ríos', 'carlos.rios@example.com', 'Estudiante', 'rioscarl', '911223344', 'Calle Unión 231', 'EPIC', '2022204015', '2022', NULL, NULL, NULL, NULL),
('user7', 'Valeria Suárez', 'valeria.suarez@example.com', 'Estudiante', 'valesu22', '944556677', 'Av. Universitaria 750', 'ADMINISTRACION', '2024204056', '2024', NULL, NULL, NULL, NULL),
('user8', 'Jorge Quispe', 'jorge.quispe@example.com', 'Estudiante', 'jorge123', '977889900', 'Pasaje Olivos 333', 'ADMINISTRACION', '2021204035', '2021', NULL, NULL, NULL, NULL),
('user9', 'Camila Pinto', 'camila.pinto@example.com', 'Estudiante', 'pinto98', '922334455', 'Jr. Junín 110', 'CONTABILIDAD', '2020204033', '2020', NULL, NULL, NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `comentarios`
--
ALTER TABLE `comentarios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `isbn` (`isbn`);

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`isbn`);

--
-- Indices de la tabla `multas`
--
ALTER TABLE `multas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `isbn` (`isbn`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `comentarios`
--
ALTER TABLE `comentarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT de la tabla `multas`
--
ALTER TABLE `multas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `comentarios`
--
ALTER TABLE `comentarios`
  ADD CONSTRAINT `comentarios_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `comentarios_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `libros` (`isbn`) ON DELETE CASCADE;

--
-- Filtros para la tabla `multas`
--
ALTER TABLE `multas`
  ADD CONSTRAINT `multas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD CONSTRAINT `prestamos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `prestamos_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `libros` (`isbn`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
