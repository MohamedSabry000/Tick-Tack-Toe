-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`player` (
  `id` INT NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `user` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `win_count` INT NOT NULL DEFAULT 0,
  `lose_count` INT NOT NULL DEFAULT 0,
  `points` INT NOT NULL DEFAULT 0,
  `status` ENUM('online', 'offline') NULL DEFAULT 'offline',
  `level` ENUM("beginner", "intermidiate", "expert") NULL DEFAULT 'beginner',
  `custom_id` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `user_UNIQUE` (`user` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`game` (
  `game_id` INT NOT NULL,
  `player1_id` INT NOT NULL,
  `player2_id1` INT NOT NULL,
  `status` ENUM('finished', 'paused', 'resumed') NULL DEFAULT NULL,
  `winner` VARCHAR(45) NULL DEFAULT NULL,
  `game_grid` VARCHAR(45) NULL DEFAULT NULL,
  `player1_choice` VARCHAR(45) NOT NULL,
  `player2_choice` VARCHAR(45) NOT NULL,
  `game_date` DATE NOT NULL,
  INDEX `fk_game_player_idx` (`player1_id` ASC) VISIBLE,
  INDEX `fk_game_player1_idx` (`player2_id1` ASC) VISIBLE,
  PRIMARY KEY (`game_id`),
  UNIQUE INDEX `game_id_UNIQUE` (`game_id` ASC) VISIBLE,
  UNIQUE INDEX `game_date_UNIQUE` (`game_date` ASC) VISIBLE,
  CONSTRAINT `fk_game_player`
    FOREIGN KEY (`player1_id`)
    REFERENCES `mydb`.`player` (`id`),
  CONSTRAINT `fk_game_player1`
    FOREIGN KEY (`player2_id1`)
    REFERENCES `mydb`.`player` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
