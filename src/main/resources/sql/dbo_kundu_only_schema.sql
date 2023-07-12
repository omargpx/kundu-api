-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dbo_kundu
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dbo_kundu
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dbo_kundu` DEFAULT CHARACTER SET utf8mb4 ;
USE `dbo_kundu` ;

-- -----------------------------------------------------
-- Table `dbo_kundu`.`TSG_USERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TSG_USERS` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `no_username` VARCHAR(45) NOT NULL,
  `kt_password` VARCHAR(245) NOT NULL,
  `di_email` VARCHAR(45) NOT NULL,
  `co_secure_pass` INT NULL,
  `role` VARCHAR(75) NOT NULL,
  `fe_last_connect` DATETIME NOT NULL,
  PRIMARY KEY (`id_user`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TMA_PEOPLE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TMA_PEOPLE` (
  `id_person` INT NOT NULL AUTO_INCREMENT,
  `no_person` VARCHAR(45) NOT NULL,
  `nu_phone` VARCHAR(12) NULL,
  `url_avatar` VARCHAR(45) NOT NULL,
  `co_kundu` VARCHAR(15) NOT NULL,
  `dc_biography` VARCHAR(275) NULL,
  `nu_experience` INT NULL DEFAULT 0,
  `fe_birthday` DATE NULL,
  `fe_join_to_kundu` DATE NULL,
  `fk_user_id` INT NOT NULL,
  PRIMARY KEY (`id_person`),
  UNIQUE INDEX `nu_phone_UNIQUE` (`nu_phone` ASC) VISIBLE,
  UNIQUE INDEX `co_kundu_UNIQUE` (`co_kundu` ASC) VISIBLE,
  INDEX `fk_TMA_PEOPLE_TSG_USERS_idx` (`fk_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_TMA_PEOPLE_TSG_USERS`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `dbo_kundu`.`TSG_USERS` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TAX_FOLLOWS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TAX_FOLLOWS` (
  `id_follow` INT NOT NULL AUTO_INCREMENT,
  `fe_follow` DATE NOT NULL,
  `fk_follower_id` INT NOT NULL,
  `fk_following` INT NOT NULL,
  PRIMARY KEY (`id_follow`),
  INDEX `fk_TAX_FOLLOWS_TMA_PEOPLE1_idx` (`fk_follower_id` ASC) VISIBLE,
  INDEX `fk_TAX_FOLLOWS_TMA_PEOPLE2_idx` (`fk_following` ASC) VISIBLE,
  CONSTRAINT `fk_TAX_FOLLOWS_TMA_PEOPLE1`
    FOREIGN KEY (`fk_follower_id`)
    REFERENCES `dbo_kundu`.`TMA_PEOPLE` (`id_person`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TAX_FOLLOWS_TMA_PEOPLE2`
    FOREIGN KEY (`fk_following`)
    REFERENCES `dbo_kundu`.`TMA_PEOPLE` (`id_person`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TMA_ENTITIES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TMA_ENTITIES` (
  `id_entity` INT NOT NULL AUTO_INCREMENT,
  `no_entity` VARCHAR(255) NOT NULL,
  `co_alias` VARCHAR(10) NULL,
  `url_image` VARCHAR(255) NOT NULL,
  `fe_instance` VARCHAR(45) NOT NULL,
  `type_entity` VARCHAR(75) NOT NULL,
  `fk_father_id` INT NULL,
  PRIMARY KEY (`id_entity`),
  INDEX `fk_TMA_ENTITIES_TMA_ENTITIES1_idx` (`fk_father_id` ASC) VISIBLE,
  CONSTRAINT `fk_TMA_ENTITIES_TMA_ENTITIES1`
    FOREIGN KEY (`fk_father_id`)
    REFERENCES `dbo_kundu`.`TMA_ENTITIES` (`id_entity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TMA_GROUPS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TMA_GROUPS` (
  `id_group` INT NOT NULL AUTO_INCREMENT,
  `no_group` VARCHAR(75) NOT NULL,
  `co_group` VARCHAR(45) NOT NULL,
  `dc_lema` VARCHAR(265) NOT NULL,
  `dc_verse` VARCHAR(275) NOT NULL,
  `url_song` VARCHAR(265) NOT NULL,
  `no_tag` VARCHAR(45) NULL,
  `url_image` VARCHAR(45) NOT NULL,
  `nu_phase` INT NULL DEFAULT 4,
  `nu_tier` INT NULL DEFAULT 1,
  `nu_points` INT NULL DEFAULT 0,
  `fe_instance` DATE NOT NULL,
  `fk_entity_id` INT NULL,
  PRIMARY KEY (`id_group`),
  UNIQUE INDEX `co_group_UNIQUE` (`co_group` ASC) VISIBLE,
  INDEX `fk_TMA_GROUPS_TMA_ENTITIES1_idx` (`fk_entity_id` ASC) VISIBLE,
  CONSTRAINT `fk_TMA_GROUPS_TMA_ENTITIES1`
    FOREIGN KEY (`fk_entity_id`)
    REFERENCES `dbo_kundu`.`TMA_ENTITIES` (`id_entity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TMA_EVENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TMA_EVENTS` (
  `id_event` INT NOT NULL AUTO_INCREMENT,
  `no_event` VARCHAR(255) NOT NULL,
  `no_place` VARCHAR(255) NOT NULL,
  `url_image` VARCHAR(45) NOT NULL,
  `fe_execution` DATETIME NOT NULL,
  `dc_event` VARCHAR(255) NULL,
  `fk_entity_id` INT NULL,
  PRIMARY KEY (`id_event`),
  INDEX `fk_TMA_EVENTS_TMA_ENTITIES1_idx` (`fk_entity_id` ASC) VISIBLE,
  CONSTRAINT `fk_TMA_EVENTS_TMA_ENTITIES1`
    FOREIGN KEY (`fk_entity_id`)
    REFERENCES `dbo_kundu`.`TMA_ENTITIES` (`id_entity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TMV_MEMBERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TMV_MEMBERS` (
  `id_member` INT NOT NULL AUTO_INCREMENT,
  `fe_instance` DATE NOT NULL,
  `fk_person_id` INT NOT NULL,
  `fk_group_id` INT NOT NULL,
  PRIMARY KEY (`id_member`),
  INDEX `fk_TMV_MEMBERS_TMA_PEOPLE1_idx` (`fk_person_id` ASC) VISIBLE,
  INDEX `fk_TMV_MEMBERS_TMA_GROUPS1_idx` (`fk_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_TMV_MEMBERS_TMA_PEOPLE1`
    FOREIGN KEY (`fk_person_id`)
    REFERENCES `dbo_kundu`.`TMA_PEOPLE` (`id_person`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TMV_MEMBERS_TMA_GROUPS1`
    FOREIGN KEY (`fk_group_id`)
    REFERENCES `dbo_kundu`.`TMA_GROUPS` (`id_group`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TAX_SESSIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TAX_SESSIONS` (
  `id_session` INT NOT NULL AUTO_INCREMENT,
  `co_lesson` VARCHAR(45) NOT NULL,
  `fe_execution` DATE NOT NULL,
  `es_session` BIT NULL,
  `fk_group_id` INT NOT NULL,
  PRIMARY KEY (`id_session`),
  INDEX `fk_TAX_SESSIONS_TMA_GROUPS1_idx` (`fk_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_TAX_SESSIONS_TMA_GROUPS1`
    FOREIGN KEY (`fk_group_id`)
    REFERENCES `dbo_kundu`.`TMA_GROUPS` (`id_group`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TAR_ASSISTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TAR_ASSISTS` (
  `id_assist` INT NOT NULL AUTO_INCREMENT,
  `fe_register` DATE NOT NULL,
  `fk_member_id` INT NOT NULL,
  `fk_session_id` INT NOT NULL,
  PRIMARY KEY (`id_assist`),
  INDEX `fk_TAR_ASSISTS_TMV_MEMBERS1_idx` (`fk_member_id` ASC) VISIBLE,
  INDEX `fk_TAR_ASSISTS_TAX_SESSIONS1_idx` (`fk_session_id` ASC) VISIBLE,
  CONSTRAINT `fk_TAR_ASSISTS_TMV_MEMBERS1`
    FOREIGN KEY (`fk_member_id`)
    REFERENCES `dbo_kundu`.`TMV_MEMBERS` (`id_member`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TAR_ASSISTS_TAX_SESSIONS1`
    FOREIGN KEY (`fk_session_id`)
    REFERENCES `dbo_kundu`.`TAX_SESSIONS` (`id_session`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_kundu`.`TSG_TOKEN`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_kundu`.`TSG_TOKEN` (
  `id_token` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(45) NOT NULL,
  `expired` BIT NULL,
  `revoked` BIT NULL,
  `tokenType` VARCHAR(45) NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id_token`),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC) VISIBLE,
  INDEX `fk_TSG_TOKEN_TSG_USERS1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_TSG_TOKEN_TSG_USERS1`
    FOREIGN KEY (`user_id`)
    REFERENCES `dbo_kundu`.`TSG_USERS` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
