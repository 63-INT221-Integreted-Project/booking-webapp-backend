SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

drop database booking;
CREATE SCHEMA IF NOT EXISTS `booking` DEFAULT CHARACTER SET utf8 ;
USE `booking` ;

CREATE TABLE IF NOT EXISTS `booking`.`event_categories` (
    `eventCategoryId` VARCHAR(16) NOT NULL,
    `eventCategoryName` VARCHAR(100) NULL,
    `eventCategoryDescription` VARCHAR(500) NULL,
    `eventCategoryDuration` INT NULL,
    PRIMARY KEY (`eventCategoryId`),
    UNIQUE INDEX `eventCategoryId_UNIQUE` (`eventCategoryId` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `booking`.`booking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`booking` (
    `bookingId` VARCHAR(20) NOT NULL,
    `bookingName` VARCHAR(100) NULL,
    `bookingEmail` VARCHAR(45) NULL,
    PRIMARY KEY (`bookingId`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `booking`.`events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`events` (
    `eventId` VARCHAR(20) NOT NULL,
    `eventStartTime` DATETIME NULL,
    `eventDuration` INT NULL,
    `eventNotes` VARCHAR(500) NULL,
    `eventCategoryId` VARCHAR(16) NOT NULL,
    `bookingId` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`eventId`),
    INDEX `fk_events_event_categories_idx` (`eventCategoryId` ASC) VISIBLE,
    INDEX `fk_events_booking1_idx` (`bookingId` ASC) VISIBLE,
    CONSTRAINT `fk_events_event_categories`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `booking`.`event_categories` (`eventCategoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_events_booking1`
    FOREIGN KEY (`bookingId`)
    REFERENCES `booking`.`booking` (`bookingId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


insert into event_categories values ("EC-001", "Frontend Clinic", "ปรึกษาสำหรับวิชา Clientside", 30);
insert into event_categories values ("EC-002", "Backend Clinic", "ปรึกษาสำหรับวิชา Serverside", 30);
insert into event_categories values ("EC-003", "DevOps Clinic", "ปรึกษาสำหรับวิชา DevOps", 30);

insert into booking values("B-001","ปรึกษาการทำ Norm ของ DB","thiraphat.itamonchai@mail.kmutt.ac.th");
insert into booking values("B-002","ปรึกษาการทำ Component","thiraphat.itamonchai@mail.kmutt.ac.th");
insert into booking values("B-003","ปรึกษาการทำ Repository","thiraphat.itamonchai@mail.kmutt.ac.th");
insert into booking values("B-004","ปรึกษาการทำ Component","thiraphat.itamonchai@mail.kmutt.ac.th");
insert into booking values("B-005","ปรึกษาการ ติดตั้งโปรเจค","thiraphat.itamonchai@mail.kmutt.ac.th");

insert into events (eventId, eventStartTime, eventDuration, eventNotes, eventCategoryId, bookingId)
values("E-001", "2022-04-024 17:00:00", 30, "ขออีดวีดีโอด้วย", "EC-001", "B-001");
insert into events (eventId, eventStartTime, eventDuration, eventNotes, eventCategoryId, bookingId)
values("E-002", "2022-04-025 10:00:00", 30, "ขออีดวีดีโอด้วย", "EC-001", "B-002");
insert into events (eventId, eventStartTime, eventDuration, eventNotes, eventCategoryId, bookingId)
values("E-003", "2022-04-025 10:00:00", 30, "ขออีดวีดีโอด้วย", "EC-001", "B-003");
insert into events (eventId, eventStartTime, eventDuration, eventNotes, eventCategoryId, bookingId)
values("E-004", "2022-04-023 15:00:00", 30, "ขออีดวีดีโอด้วย", "EC-001", "B-004");
insert into events (eventId, eventStartTime, eventDuration, eventNotes, eventCategoryId, bookingId)
values("E-005", "2022-04-022 12:00:00", 30, "ขออีดวีดีโอด้วย", "EC-001", "B-005");
