CREATE DATABASE `EducationProject` /*!40100 COLLATE 'utf8mb4_0900_ai_ci' */

CREATE TABLE `matiere` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de la matière.',
	`couleurFond` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de fond de la matière.',
	`couleurPolice` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de la police de la matière.',
	`volumeHoraire` VARCHAR(5) NULL DEFAULT '' COMMENT 'C\'est le volume horaire d\'une matière.',
	`description` VARCHAR(255) NULL DEFAULT '' COMMENT 'C\'est la description d\'une matière.',
	`creationDate` TIMESTAMP NOT NULL DEFAULT NOW(),
	`modificationDate` TIMESTAMP NOT NULL DEFAULT NOW(),
	PRIMARY KEY (`id`)
) COLLATE='utf8mb4_0900_ai_ci';

CREATE TABLE `classe` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de la classe.',
    `creationDate` TIMESTAMP NOT NULL DEFAULT NOW(),
    `modificationDate` TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (`id`)
) COLLATE='utf8mb4_0900_ai_ci';

