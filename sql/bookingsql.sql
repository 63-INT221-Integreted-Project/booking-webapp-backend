-- MySQL Script generated by MySQL Workbench
-- Fri Apr 22 14:08:59 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema booking
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema booking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `booking` DEFAULT CHARACTER SET utf8 ;
USE `booking` ;

-- -----------------------------------------------------
-- Table `booking`.`event_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`event_categories` (
  `eventCategoryId` VARCHAR(16) NOT NULL,
  `eventCategoryName` VARCHAR(45) NULL,
  `eventCategoryDescription` VARCHAR(45) NULL,
  `eventDuration` VARCHAR(45) NULL,
  PRIMARY KEY (`eventCategoryId`),
  UNIQUE INDEX `eventCategoryId_UNIQUE` (`eventCategoryId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `booking`.`events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`events` (
  `eventId` VARCHAR(16) NOT NULL,
  `bookingName` VARCHAR(45) NULL,
  `bookingEmail` VARCHAR(45) NULL,
  `eventStartTime` VARCHAR(45) NULL,
  `eventNotes` TEXT(500) NULL,
  `eventCategoryId` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`eventId`, `eventCategoryId`),
  INDEX `fk_events_event_categories_idx` (`eventCategoryId` ASC) VISIBLE,
  CONSTRAINT `fk_events_event_categories`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `booking`.`event_categories` (`eventCategoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



insert event_categories value("EC-001","Backend Clinic", "for give an advisor from serverside techer","2022-3-31T01:30:00.000-05:00");
insert event_categories value("EC-002","Frontend Clinic", "for give an advisor from client techer","2022-3-31T01:30:00.000-05:00");

insert events value("E-001","จองปรึกษาออกแบบ Repo", "thiraphat.itamonchai@mail.kmutt.ac.th","2022-3-31T01:30:00.000-05:00", "เข้าปรึกษาพร้อมทีม ขออัดวีดีโอระหว่างปรึกษา","EC-002");

