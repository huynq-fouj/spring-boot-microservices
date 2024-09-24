CREATE DATABASE  IF NOT EXISTS `kachina_profile_service` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `kachina_profile_service`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: kachina_profile_service
-- ------------------------------------------------------
-- Server version	5.7.39-log

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

--
-- Table structure for table `tbluserprofile`
--

DROP TABLE IF EXISTS `tbluserprofile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbluserprofile` (
  `id` varchar(255) NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluserprofile`
--

LOCK TABLES `tbluserprofile` WRITE;
/*!40000 ALTER TABLE `tbluserprofile` DISABLE KEYS */;
INSERT INTO `tbluserprofile` VALUES ('064232d6-dd9f-4be5-afcc-bba60a46bb89','Bac Ninh','2003-03-01','Thong','Pham','8229ed28-e213-4521-a7f7-783d56fd1423'),('0826da5c-1fb6-43d6-92a2-468ad8236e56','Vinh Phuc','2003-07-01','Thanh','Khong','74239fa5-cc43-4f66-aec3-b9bbc6161237'),('2a875910-fd7d-481e-bcaa-4d0280c41b66','Ha Noi','2003-07-01','Thanh','Khong','1462e2a8-c2b1-452b-9557-2284b2ab57a7'),('494aacc9-5a05-4f65-8d8d-94d74fd69ef5','Vinh Phuc','2003-06-09','Linh','Trieu','b8fc42ca-d78a-4df2-a673-738fb5245de8'),('49c1bb72-bb4c-497e-b8c5-1a0c32549611','Vinh Phuc','2002-12-20','Huy','Nguyen Quang','d1474d5e-9c30-4bac-853d-1bd8bd18bf56'),('6b51604b-603e-4776-a83f-5749824bd032','Vinh Phuc','2005-03-01','Hieu','Nguyen','1896c0cd-0b30-4115-8fb4-bab0dd84e50d'),('74ced4e3-45b1-4f68-85c2-1f9c95075f83','Ha Noi','2000-01-01','John','Doe','f41845de-7d41-47c3-899a-8556c997e002'),('7b635030-cf97-42ef-8541-9b0fb7721182','Vinh Phuc','2002-12-20','Huy','Nguyen Quang','ae8e4e89-a6f9-4ca2-9a40-8d1bb1b786c4'),('8d84f0dc-be39-49d9-bda4-8a49672810c9','Ha Noi','2003-07-01','Linh','Trieu','35fe89bf-5ddd-411a-8194-d293decf6e30'),('c763a3a8-4495-48de-bd06-cb7f672e8d4d','Vinh Phuc','2002-12-20','Huy','Nguyen','4fc6517b-3c3f-4e65-8796-e3400a2d45dd');
/*!40000 ALTER TABLE `tbluserprofile` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-24 12:46:37
