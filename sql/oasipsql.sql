SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
    SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
    SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


drop database oasipdb;
CREATE SCHEMA IF NOT EXISTS `oasipdb` DEFAULT CHARACTER SET utf8 ;
USE `oasipdb` ;

    -- -----------------------------------------------------
    -- Table `mydb`.`eventCategory`
    -- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oasipdb`.`event_category` (
                                                          `eventCategoryId` INT NOT NULL AUTO_INCREMENT,
                                                          `eventCategoryName` VARCHAR(100) NOT NULL,
    `eventCategoryDescription` VARCHAR(500) NULL,
    `eventDuration` INT NOT NULL,
    PRIMARY KEY (`eventCategoryId`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oasipdb`.`event` (
                                                 `eventId` INT NOT NULL AUTO_INCREMENT,
                                                 `bookingName` VARCHAR(100) NOT NULL,
    `bookingEmail` VARCHAR(50) NOT NULL,
    `eventStartTime` DATETIME NOT NULL,
    `eventDuration` INT NOT NULL,
    `eventNotes` VARCHAR(500) NULL,
    `eventCategoryId` INT,
    PRIMARY KEY (`eventId`),
    INDEX `fk_event_eventCategory_idx` (`eventCategoryId` ASC),
    CONSTRAINT `fk_event_eventCategory`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `oasipdb`.`event_category` (`eventCategoryId`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `oasipdb`.`user`(
                                               `userId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                               `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    `createdOn` DATETIME(4) DEFAULT CURRENT_TIMESTAMP(4),
    `updatedOn` DATETIME(4) DEFAULT CURRENT_TIMESTAMP(4) ON UPDATE CURRENT_TIMESTAMP(4)
    );


SET SQL_MODE=@OLD_SQL_MODE;
    SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
    SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
