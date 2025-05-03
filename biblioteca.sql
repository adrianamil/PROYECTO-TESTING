-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-05-2025 a las 19:59:11
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `biblioteca`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `almacen`
--

CREATE TABLE `almacen` (
  `isbn_libro` int(11) NOT NULL,
  `id_biblioteca` int(11) NOT NULL,
  `stock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `almacen`
--

INSERT INTO `almacen` (`isbn_libro`, `id_biblioteca`, `stock`) VALUES
(100001, 1001, 4),
(100001, 1002, 8),
(100001, 1003, 1),
(100002, 1001, 4),
(100002, 1002, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `biblioteca_almacen`
--

CREATE TABLE `biblioteca_almacen` (
  `id_biblioteca` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `biblioteca_almacen`
--

INSERT INTO `biblioteca_almacen` (`id_biblioteca`, `nombre`, `direccion`) VALUES
(1001, 'Biblioteca San Camilo', 'San Camilo 504'),
(1002, 'Biblioteca Cajita', 'Av Peru 102'),
(1003, 'Biblioteca Sol', 'Av. Peru 201');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `dni_cliente` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `apellido` varchar(20) NOT NULL,
  `direccion` varchar(20) NOT NULL,
  `telefono` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`dni_cliente`, `nombre`, `apellido`, `direccion`, `telefono`) VALUES
(72504388, 'Adriana', 'Ticona', 'Casa 3', 0),
(72568983, 'Miguel', 'Mamani', 'Casa 123', 994533621),
(72748259, 'Cesar', 'Chambi', 'Casa 2', 0),
(74471743, 'Gabriel', 'Paricahua', 'Casa 4', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libro`
--

CREATE TABLE `libro` (
  `isbn` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `editorial` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `libro`
--

INSERT INTO `libro` (`isbn`, `nombre`, `editorial`) VALUES
(100001, 'Peter Pan', 'Coquito'),
(100002, 'Don Quijote', 'Coquito'),
(100003, 'El gato con botas', 'Cisco'),
(100004, 'El zorro', 'ABC'),
(100005, 'Programar en Java 123', 'Montesinos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamo`
--

CREATE TABLE `prestamo` (
  `id_prestamo` int(11) NOT NULL,
  `dni_cliente` int(11) NOT NULL,
  `isbn_libro` int(11) NOT NULL,
  `id_biblioteca` int(11) NOT NULL,
  `fecha_prestamo` date NOT NULL,
  `fecha_devolucion_esperada` date NOT NULL,
  `fecha_devolucion_real` date DEFAULT NULL,
  `estado` enum('activo','devuelto','atrasado') DEFAULT 'activo',
  `multa` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamo`
--

INSERT INTO `prestamo` (`id_prestamo`, `dni_cliente`, `isbn_libro`, `id_biblioteca`, `fecha_prestamo`, `fecha_devolucion_esperada`, `fecha_devolucion_real`, `estado`, `multa`) VALUES
(1, 72568983, 100001, 1001, '2025-04-27', '2025-05-03', '2025-05-30', 'devuelto', 135.00),
(2, 72568983, 100002, 1002, '2025-04-28', '2025-04-30', '2025-04-30', 'devuelto', 0.00),
(3, 72568983, 100001, 1001, '2025-04-29', '2025-05-02', '2025-04-30', 'devuelto', 0.00),
(4, 72568983, 100001, 1001, '2025-04-30', '2025-05-02', '2025-04-02', 'devuelto', 0.00),
(5, 72568983, 100002, 1002, '2025-05-01', '2025-05-12', '2025-05-01', 'devuelto', 0.00),
(6, 72568983, 100002, 1002, '2025-04-30', '2025-05-15', '2025-05-02', 'devuelto', 0.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `almacen`
--
ALTER TABLE `almacen`
  ADD PRIMARY KEY (`isbn_libro`,`id_biblioteca`),
  ADD KEY `id_biblioteca` (`id_biblioteca`);

--
-- Indices de la tabla `biblioteca_almacen`
--
ALTER TABLE `biblioteca_almacen`
  ADD PRIMARY KEY (`id_biblioteca`),
  ADD UNIQUE KEY `id_biblioteca` (`id_biblioteca`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`dni_cliente`),
  ADD UNIQUE KEY `dni_cliente` (`dni_cliente`);

--
-- Indices de la tabla `libro`
--
ALTER TABLE `libro`
  ADD PRIMARY KEY (`isbn`),
  ADD UNIQUE KEY `isbn` (`isbn`);

--
-- Indices de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD PRIMARY KEY (`id_prestamo`),
  ADD KEY `dni_cliente` (`dni_cliente`),
  ADD KEY `isbn_libro` (`isbn_libro`,`id_biblioteca`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id_prestamo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `almacen`
--
ALTER TABLE `almacen`
  ADD CONSTRAINT `almacen_ibfk_1` FOREIGN KEY (`isbn_libro`) REFERENCES `libro` (`isbn`) ON DELETE CASCADE,
  ADD CONSTRAINT `almacen_ibfk_2` FOREIGN KEY (`id_biblioteca`) REFERENCES `biblioteca_almacen` (`id_biblioteca`) ON DELETE CASCADE;

--
-- Filtros para la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`dni_cliente`) REFERENCES `cliente` (`dni_cliente`) ON DELETE CASCADE,
  ADD CONSTRAINT `prestamo_ibfk_2` FOREIGN KEY (`isbn_libro`,`id_biblioteca`) REFERENCES `almacen` (`isbn_libro`, `id_biblioteca`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
