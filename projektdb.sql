-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Gostitelj: 127.0.0.1:3306
-- Čas nastanka: 02. jun 2021 ob 13.51
-- Različica strežnika: 5.7.31
-- Različica PHP: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Zbirka podatkov: `projektdb`
--

-- --------------------------------------------------------

--
-- Struktura tabele `tablice`
--

DROP TABLE IF EXISTS `tablice`;
CREATE TABLE IF NOT EXISTS `tablice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_slovenian_ci NOT NULL,
  `image` longtext COLLATE utf8_slovenian_ci,
  `tablica` longtext COLLATE utf8_slovenian_ci,
  `lng` decimal(10,6) DEFAULT NULL,
  `lat` decimal(10,6) DEFAULT NULL,
  `FK_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Odloži podatke za tabelo `tablice`
--

INSERT INTO `tablice` (`id`, `name`, `image`, `tablica`, `lng`, `lat`, `FK_user`) VALUES
(1, 'tablica1', NULL, 'tablica1.jpg', '-43.878860', '10.793100', 8),
(2, 'tablica2', NULL, 'tablica2.jpg', '14.256430', '65.048900', 8),
(3, 'tablica3', NULL, 'tablica3.jpg', '0.096910', '-145.612160', 9),
(4, 'tablica4', NULL, 'tablica4.jpg', '4.017130', '52.031740', 9),
(5, 'tablica5', NULL, 'tablica5.jpg', '65.994980', '28.826040', 9),
(6, 'tablica6', NULL, 'tablica6.jpg', '46.167495', '15.352779', 9),
(7, 'tablica7', NULL, 'tablica7.jpg', '46.167495', '15.352779', 9),
(8, 'tablica8', NULL, 'tablica8.jpg', '46.167495', '15.352779', 8),
(9, 'tablica9', NULL, 'tablica9.jpg', '15.635885', '46.559039', 9),
(10, 'tablica10', NULL, 'tablica10.jpg', '46.559039', '15.635885', 9),
(11, 'tablica11', NULL, 'tablica11.jpg', '46.559039', '15.635885', 9),
(12, 'tablica12', NULL, 'tablica12.jpg', '46.559039', '15.635885', 9),
(13, 'tablica13', NULL, 'tablica13.jpg', '46.559039', '15.635885', 9),
(14, 'tablica14', NULL, 'tablica14.jpg', '46.559039', '15.635885', 9),
(15, 'tablica15', NULL, 'tablica15.jpg', '46.559039', '15.635885', 9),
(16, 'tablica16', NULL, 'tablica16.jpg', '46.559039', '15.635885', 9),
(17, 'tablica17', NULL, 'tablica17.jpg', '46.559039', '15.635885', 9),
(18, 'tablica18', NULL, 'tablica18.jpg', '46.559039', '15.635885', 9),
(19, 'tablica19', NULL, 'tablica19.jpg', '46.559039', '15.635885', 8);

-- --------------------------------------------------------

--
-- Struktura tabele `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` text COLLATE utf8_slovenian_ci NOT NULL,
  `username` varchar(100) COLLATE utf8_slovenian_ci NOT NULL,
  `password` text COLLATE utf8_slovenian_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_slovenian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

--
-- Odloži podatke za tabelo `users`
--

INSERT INTO `users` (`id`, `fullname`, `username`, `password`, `email`) VALUES
(8, 'TES TEST', 'test', '$2y$10$75CQwiZ1mzDibRLhPOZ5tOgWWPZ1GdPirwFjbFXqGcao5xLFkLESW', 'test@gmail.com'),
(9, 'Matjaž CENTRIH', 'mcentrih', '$2y$10$PigLIVmjZ/ozHhxzeFAKz.kt.pq0UozhoHLiEm.Zmkig3atW2Xbaa', 'cent@gmail.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
