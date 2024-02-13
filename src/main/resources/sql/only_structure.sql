CREATE DATABASE  IF NOT EXISTS `dbo_kundu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dbo_kundu`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dbo_kundu
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `tar_assists`
--

DROP TABLE IF EXISTS `tar_assists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tar_assists` (
  `id_assist` int NOT NULL AUTO_INCREMENT,
  `fe_register` date NOT NULL,
  `quiz_realized` bit(1) DEFAULT b'0',
  `attendance` bit(1) DEFAULT b'0',
  `fk_member_id` int NOT NULL,
  `fk_session_id` int NOT NULL,
  PRIMARY KEY (`id_assist`),
  KEY `fk_TAR_ASSISTS_TMV_MEMBERS1_idx` (`fk_member_id`),
  KEY `fk_TAR_ASSISTS_TAX_SESSIONS1_idx` (`fk_session_id`),
  CONSTRAINT `fk_TAR_ASSISTS_TAX_SESSIONS11` FOREIGN KEY (`fk_session_id`) REFERENCES `tax_sessions` (`id_session`),
  CONSTRAINT `fk_TAR_ASSISTS_TMV_MEMBERS11` FOREIGN KEY (`fk_member_id`) REFERENCES `tmv_members` (`id_member`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tar_users_space`
--

DROP TABLE IF EXISTS `tar_users_space`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tar_users_space` (
  `id_user_space` int NOT NULL AUTO_INCREMENT,
  `uuid_agora` varchar(175) DEFAULT NULL,
  `user_id` int NOT NULL,
  `space_id` int NOT NULL,
  PRIMARY KEY (`id_user_space`),
  KEY `fk_tar_users_channel_tsg_users1_idx` (`user_id`),
  KEY `fk_tar_users_channel_tax_channels1_idx` (`space_id`),
  CONSTRAINT `fk_tar_users_channel_tsg_users1` FOREIGN KEY (`user_id`) REFERENCES `tsg_users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tat_val_quiz`
--

DROP TABLE IF EXISTS `tat_val_quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tat_val_quiz` (
  `id_val_quiz` int NOT NULL AUTO_INCREMENT,
  `title_lesson` varchar(85) NOT NULL,
  `code_session` varchar(45) NOT NULL,
  `questions` varchar(45) NOT NULL,
  `answers` varchar(45) NOT NULL,
  `xp_gained` int NOT NULL,
  `points_group` int NOT NULL,
  `fk_member_id` int NOT NULL,
  PRIMARY KEY (`id_val_quiz`),
  KEY `fk_tat_val_quiz_tmv_members1_idx` (`fk_member_id`),
  CONSTRAINT `fk_tat_val_quiz_tmv_members1` FOREIGN KEY (`fk_member_id`) REFERENCES `tmv_members` (`id_member`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_books`
--

DROP TABLE IF EXISTS `tax_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_books` (
  `id_book` int NOT NULL,
  `no_book` varchar(50) NOT NULL,
  `description` varchar(1250) DEFAULT NULL,
  `phase` int NOT NULL DEFAULT '0',
  `code` varchar(50) NOT NULL,
  `is_selected` bit(1) DEFAULT NULL,
  `pages` int DEFAULT NULL,
  PRIMARY KEY (`id_book`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_follows`
--

DROP TABLE IF EXISTS `tax_follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_follows` (
  `id_follow` int NOT NULL AUTO_INCREMENT,
  `fe_follow` date NOT NULL,
  `fk_follower_id` int NOT NULL,
  `fk_followed_id` int NOT NULL,
  PRIMARY KEY (`id_follow`),
  KEY `fk_TAX_FOLLOWS_TMA_PEOPLE1_idx` (`fk_follower_id`),
  KEY `fk_TAX_FOLLOWS_TMA_PEOPLE2_idx` (`fk_followed_id`),
  CONSTRAINT `fk_TAX_FOLLOWS_TMA_PEOPLE1` FOREIGN KEY (`fk_follower_id`) REFERENCES `tma_people` (`id_person`),
  CONSTRAINT `fk_TAX_FOLLOWS_TMA_PEOPLE2` FOREIGN KEY (`fk_followed_id`) REFERENCES `tma_people` (`id_person`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_invitations`
--

DROP TABLE IF EXISTS `tax_invitations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_invitations` (
  `id_invitation` int NOT NULL AUTO_INCREMENT,
  `reserve_email` varchar(75) DEFAULT NULL,
  `fe_invitation` date DEFAULT NULL,
  `fk_user_id` int NOT NULL,
  PRIMARY KEY (`id_invitation`),
  KEY `fk_TAX_INVITATIONS_TSG_USERS1_idx` (`fk_user_id`),
  CONSTRAINT `fk_TAX_INVITATIONS_TSG_USERS1` FOREIGN KEY (`fk_user_id`) REFERENCES `tsg_users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_lessons`
--

DROP TABLE IF EXISTS `tax_lessons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_lessons` (
  `id_lesson` int NOT NULL,
  `no_lesson` varchar(100) NOT NULL,
  `verse` longtext NOT NULL,
  `desc_content` longtext NOT NULL,
  `reflection` longtext,
  `code` varchar(50) NOT NULL,
  `is_selected` tinyint(1) NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`id_lesson`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_lesson_book` (`book_id`),
  CONSTRAINT `fk_lesson_book` FOREIGN KEY (`book_id`) REFERENCES `tax_books` (`id_book`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_sessions`
--

DROP TABLE IF EXISTS `tax_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_sessions` (
  `id_session` int NOT NULL AUTO_INCREMENT,
  `co_lesson` varchar(45) NOT NULL,
  `fe_execution` date NOT NULL,
  `es_session` int DEFAULT '0' COMMENT '0 = unrealized 1=on 2=off',
  `fk_group_id` int NOT NULL,
  PRIMARY KEY (`id_session`),
  KEY `fk_TAX_SESSIONS_TMA_GROUPS1_idx` (`fk_group_id`),
  CONSTRAINT `fk_TAX_SESSIONS_TMA_GROUPS1` FOREIGN KEY (`fk_group_id`) REFERENCES `tma_groups` (`id_group`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_spaces`
--

DROP TABLE IF EXISTS `tax_spaces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tax_spaces` (
  `id_space` int NOT NULL AUTO_INCREMENT,
  `no_space` varchar(55) NOT NULL,
  `desc_space` varchar(175) NOT NULL DEFAULT '¡Bienvenido a mi espacio!',
  `fe_creation` datetime NOT NULL,
  `co_space` varchar(45) NOT NULL,
  `token` varchar(245) DEFAULT NULL,
  `status` bit(1) DEFAULT b'1',
  `moderator_id` int NOT NULL,
  PRIMARY KEY (`id_space`),
  KEY `fk_tax_channels_tsg_users1_idx` (`moderator_id`),
  CONSTRAINT `fk_tax_channels_tsg_users1` FOREIGN KEY (`moderator_id`) REFERENCES `tsg_users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tma_entities`
--

DROP TABLE IF EXISTS `tma_entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tma_entities` (
  `id_entity` int NOT NULL AUTO_INCREMENT,
  `no_entity` varchar(255) NOT NULL,
  `co_alias` varchar(10) DEFAULT NULL,
  `url_image` varchar(255) NOT NULL,
  `fe_instance` date NOT NULL,
  `type_entity` varchar(75) NOT NULL,
  `fk_father_id` int DEFAULT NULL,
  PRIMARY KEY (`id_entity`),
  KEY `fk_TMA_ENTITIES_TMA_ENTITIES1_idx` (`fk_father_id`),
  CONSTRAINT `fk_TMA_ENTITIES_TMA_ENTITIES1` FOREIGN KEY (`fk_father_id`) REFERENCES `tma_entities` (`id_entity`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tma_events`
--

DROP TABLE IF EXISTS `tma_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tma_events` (
  `id_event` int NOT NULL AUTO_INCREMENT,
  `no_event` varchar(255) NOT NULL,
  `no_place` varchar(255) NOT NULL,
  `type_event` varchar(45) NOT NULL,
  `fe_execution` datetime NOT NULL,
  `dc_event` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT b'1',
  `fk_entity_id` int DEFAULT NULL,
  PRIMARY KEY (`id_event`),
  KEY `fk_TMA_EVENTS_TMA_ENTITIES1_idx` (`fk_entity_id`),
  CONSTRAINT `fk_TMA_EVENTS_TMA_ENTITIES1` FOREIGN KEY (`fk_entity_id`) REFERENCES `tma_entities` (`id_entity`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tma_groups`
--

DROP TABLE IF EXISTS `tma_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tma_groups` (
  `id_group` int NOT NULL AUTO_INCREMENT,
  `no_group` varchar(75) NOT NULL,
  `co_group` varchar(45) NOT NULL,
  `dc_lema` varchar(265) NOT NULL,
  `dc_verse` varchar(275) NOT NULL,
  `url_song` varchar(265) NOT NULL,
  `no_tag` varchar(45) DEFAULT NULL,
  `url_image` varchar(255) NOT NULL,
  `nu_phase` int DEFAULT '4',
  `nu_tier` int DEFAULT '1',
  `nu_points` int DEFAULT '0',
  `fe_instance` date NOT NULL,
  `fk_entity_id` int DEFAULT NULL,
  PRIMARY KEY (`id_group`),
  UNIQUE KEY `co_group_UNIQUE` (`co_group`),
  KEY `fk_TMA_GROUPS_TMA_ENTITIES1_idx` (`fk_entity_id`),
  CONSTRAINT `fk_TMA_GROUPS_TMA_ENTITIES1` FOREIGN KEY (`fk_entity_id`) REFERENCES `tma_entities` (`id_entity`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tma_people`
--

DROP TABLE IF EXISTS `tma_people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tma_people` (
  `id_person` int NOT NULL AUTO_INCREMENT,
  `no_person` varchar(45) NOT NULL,
  `nu_phone` varchar(12) NOT NULL,
  `url_avatar` varchar(45) NOT NULL,
  `co_kundu` varchar(15) NOT NULL,
  `dc_biography` varchar(275) DEFAULT NULL,
  `nu_experience` int DEFAULT '0',
  `fe_birthday` date DEFAULT NULL,
  `fe_join_to_kundu` date DEFAULT NULL,
  `fk_user_id` int NOT NULL,
  PRIMARY KEY (`id_person`),
  UNIQUE KEY `co_kundu_UNIQUE` (`co_kundu`),
  UNIQUE KEY `nu_phone_UNIQUE` (`nu_phone`),
  KEY `fk_TMA_PEOPLE_TSG_USERS_idx` (`fk_user_id`),
  CONSTRAINT `fk_TMA_PEOPLE_TSG_USERS` FOREIGN KEY (`fk_user_id`) REFERENCES `tsg_users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmv_members`
--

DROP TABLE IF EXISTS `tmv_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tmv_members` (
  `id_member` int NOT NULL AUTO_INCREMENT,
  `fe_instance` date NOT NULL,
  `fk_person_id` int NOT NULL,
  `fk_group_id` int NOT NULL,
  PRIMARY KEY (`id_member`),
  KEY `fk_TMV_MEMBERS_TMA_PEOPLE1_idx` (`fk_person_id`),
  KEY `fk_TMV_MEMBERS_TMA_GROUPS1_idx` (`fk_group_id`),
  CONSTRAINT `fk_TMV_MEMBERS_TMA_GROUPS1` FOREIGN KEY (`fk_group_id`) REFERENCES `tma_groups` (`id_group`),
  CONSTRAINT `fk_TMV_MEMBERS_TMA_PEOPLE1` FOREIGN KEY (`fk_person_id`) REFERENCES `tma_people` (`id_person`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tsg_token`
--

DROP TABLE IF EXISTS `tsg_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tsg_token` (
  `id_token` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expired` bit(1) DEFAULT NULL,
  `revoked` bit(1) DEFAULT NULL,
  `token_type` varchar(45) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id_token`),
  UNIQUE KEY `token_UNIQUE` (`token`),
  KEY `fk_TSG_TOKEN_TSG_USERS1_idx` (`user_id`),
  CONSTRAINT `fk_TSG_TOKEN_TSG_USERS1` FOREIGN KEY (`user_id`) REFERENCES `tsg_users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=616 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tsg_users`
--

DROP TABLE IF EXISTS `tsg_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tsg_users` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `no_username` varchar(45) NOT NULL,
  `kt_password` varchar(245) NOT NULL,
  `di_email` varchar(45) NOT NULL,
  `co_secure_pass` int DEFAULT NULL,
  `role` varchar(75) NOT NULL,
  `fe_last_connect` datetime NOT NULL,
  `is_connect` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-13  9:42:21
