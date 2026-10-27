-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: private_library
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE USER IF NOT EXISTS 'student'@'localhost' IDENTIFIED BY 'student';

CREATE DATABASE IF NOT EXISTS private_library
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON private_library.* TO 'student'@'localhost';
FLUSH PRIVILEGES;

USE private_library;

-- Remove existing tables before recreating the schema.
-- CAUTION: This replaces the tables and all their existing data.
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `writer`;
DROP TABLE IF EXISTS `genre`;
DROP TABLE IF EXISTS `bookshelf`;

--
-- Table structure for table `writer`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `writer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nationality` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `writer`
--

LOCK TABLES `writer` WRITE;
/*!40000 ALTER TABLE `writer` DISABLE KEYS */;
INSERT INTO `writer` VALUES (1,'George','Orwell','British','George Orwell'),(2,'Jane','Austen','British','Jane Austen'),(3,'Haruki','Murakami','Japanese','Haruki Murakami'),(4,'J.K.','Rowling','British','J.K. Rowling'),(5,'Fyodor','Dostoevsky','Russian','Fyodor Dostoevsky'),(6,'Agatha','Christie','British','Agatha Christie'),(8,NULL,NULL,'meowtopolis','meow meow');
/*!40000 ALTER TABLE `writer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `genre_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'Dystopian Fiction','Fiction describing an imagined society with suffering, control, or injustice.'),(2,'Classic Literature','Literary works considered important or influential over time.'),(3,'Contemporary Fiction','Modern fiction focused on present-day or recent themes.'),(4,'Fantasy','Fiction involving magic, imaginary worlds, or supernatural elements.'),(5,'Crime','Fiction involving crime, mystery, investigation, or detective work.');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookshelf`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookshelf` (
  `bookshelf_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `room` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shelf_number` int DEFAULT NULL,
  PRIMARY KEY (`bookshelf_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookshelf`
--

LOCK TABLES `bookshelf` WRITE;
/*!40000 ALTER TABLE `bookshelf` DISABLE KEYS */;
INSERT INTO `bookshelf` VALUES (1,'Living Room Shelf','Living room',1),(2,'Bedroom Shelf','Bedroom',2),(3,'Office Shelf','Office',3),(4,'Storage Box','Storage room',NULL);
/*!40000 ALTER TABLE `bookshelf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `publication_year` int DEFAULT NULL,
  `reading_status` enum('Unread','Reading','Read') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Unread',
  `writer_id` int NOT NULL,
  `genre_id` int NOT NULL,
  `bookshelf_id` int NOT NULL,
  PRIMARY KEY (`book_id`),
  KEY `fk_book_writer` (`writer_id`),
  KEY `fk_book_genre` (`genre_id`),
  KEY `fk_book_bookshelf` (`bookshelf_id`),
  CONSTRAINT `fk_book_bookshelf` FOREIGN KEY (`bookshelf_id`) REFERENCES `bookshelf` (`bookshelf_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_book_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_book_writer` FOREIGN KEY (`writer_id`) REFERENCES `writer` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'1984',1949,'Read',1,1,1),(2,'Animal Farm',1945,'Read',1,1,1),(3,'Pride and Prejudice',1813,'Unread',2,2,2),(4,'Norwegian Wood',1987,'Reading',3,3,2),(5,'Kafka on the Shore',2002,'Unread',3,3,4),(6,'Harry Potter and the Philosopher\'s Stone',1997,'Read',4,4,1),(7,'Harry Potter and the Chamber of Secrets',1998,'Unread',4,4,1),(8,'Crime and Punishment',1866,'Unread',5,2,3),(9,'The Brothers Karamazov',1880,'Unread',5,2,3),(10,'Murder on the Orient Express',1934,'Read',6,5,2),(11,'And Then There Were None',1939,'Read',6,5,2);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-25 19:55:35
