CREATE DATABASE `EducationProject` /*!40100 COLLATE 'utf8mb4_0900_ai_ci' */
CREATE TABLE `matiere` (
	`id` INT NOT NULL,
	`nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de la matière.',
	`couleurFond` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de fond de la matière.',
	`couleurPolice` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de la police de la matière.',
	`volumeHoraire` VARCHAR(5) NOT NULL DEFAULT '' COMMENT 'C\'est le volume horaire d\'une matière.',
	`description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'C\'est la description d\'une matière.',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
;

ALTER TABLE `matiere`
	CHANGE COLUMN `volumeHoraire` `volumeHoraire` VARCHAR(5) NULL DEFAULT '' COMMENT 'C\'est le volume horaire d\'une matière.' COLLATE 'utf8mb4_0900_ai_ci' AFTER `couleurPolice`,
	CHANGE COLUMN `description` `description` VARCHAR(255) NULL DEFAULT '' COMMENT 'C\'est la description d\'une matière.' COLLATE 'utf8mb4_0900_ai_ci' AFTER `volumeHoraire`,
	ADD COLUMN `creationDate` DATETIME NOT NULL AFTER `description`,
	ADD COLUMN `modificationDate` DATETIME NOT NULL AFTER `creationDate`;

ALTER TABLE `matiere`
    	CHANGE COLUMN `id` `id` INT(10,0) UNSIGNED NOT NULL AUTO_INCREMENT FIRST;

ALTER TABLE `matiere`
        	CHANGE COLUMN `creationDate` `creationDate` DATETIME NOT NULL DEFAULT NOW() AFTER `description`,
        	CHANGE COLUMN `modificationDate` `modificationDate` DATETIME NOT NULL DEFAULT NOW() AFTER `creationDate`;