CREATE DATABASE `EducationProject` /*!40100 COLLATE 'utf8mb4_0900_ai_ci' */

CREATE TABLE `matiere` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de la matière.',
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

CREATE TABLE `enseignant` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de l\'enseignant' COLLATE 'utf8mb4_0900_ai_ci',
	`prenom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le prénom de l\'enseignant' COLLATE 'utf8mb4_0900_ai_ci',
	`creationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modificationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci';

CREATE TABLE `salle` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`nom` VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'C\'est le nom de la salle' COLLATE 'utf8mb4_0900_ai_ci',
	`creationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modificationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
;

CREATE TABLE `options` (
    `id` INT NOT NULL,
    `splitPlanning` INT NOT NULL DEFAULT 60 COMMENT 'Temps de découpage du planning' COLLATE 'utf8mb4_0900_ai_ci',
    `startHourPlanning` TIME NOT NULL DEFAULT '08:00:00' COMMENT 'Heure de début du planning' COLLATE 'utf8mb4_0900_ai_ci',
    `endHourPlanning` TIME NOT NULL DEFAULT '17:00:00' COMMENT 'Heure de fin du planning' COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`)
)COLLATE='utf8mb4_0900_ai_ci';

INSERT INTO `options` (`id`, `splitplanning`, `starthourplanning`, `endhourplanning`) VALUES (1, 60, '08:00:00', '17:00:00');

CREATE TABLE `timeslot` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `startHour` TIME NOT NULL COMMENT 'Heure de début' COLLATE 'utf8mb4_0900_ai_ci',
    `endHour` TIME NOT NULL COMMENT 'Heure de fin' COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`)
)COLLATE='utf8mb4_0900_ai_ci';

CREATE TABLE `jour`(
    `id` INT PRIMARY KEY NOT NULL,
    `nom` VARCHAR(20) NOT NULL
);

INSERT INTO `jour` (`id`, `nom`) VALUES (1, 'Lundi');
INSERT INTO `jour` (`id`, `nom`) VALUES (2, 'Mardi');
INSERT INTO `jour` (`id`, `nom`) VALUES (3, 'Mercredi');
INSERT INTO `jour` (`id`, `nom`) VALUES (4, 'Jeudi');
INSERT INTO `jour` (`id`, `nom`) VALUES (5, 'Vendredi');
INSERT INTO `jour` (`id`, `nom`) VALUES (6, 'Samedi');
INSERT INTO `jour` (`id`, `nom`) VALUES (7, 'Dimanche');

CREATE TABLE `slot` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `comment` VARCHAR(50) COMMENT 'Le commentaire peut désigner différents groupes (ou tâches) dans un slot',
    `creationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modificationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `couleurFond` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de fond de la matière.',
    `couleurPolice` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'C\'est la couleur de la police de la matière.',
    `idTimeslot` INT NOT NULL,
    `idMatiere` INT NOT NULL,
    `idEnseignant` INT,
    `idSalle` INT,
    `idJour` INT NOT NULL,
    FOREIGN KEY (`idTimeslot`) REFERENCES `timeslot`(`id`),
    FOREIGN KEY (`idMatiere`) REFERENCES `matiere`(`id`),
    FOREIGN KEY (`idEnseignant`) REFERENCES `enseignant`(`id`),
    FOREIGN KEY (`idSalle`) REFERENCES `salle`(`id`),
    FOREIGN KEY (`idJour`) REFERENCES `jour`(`id`),
    PRIMARY KEY (`id`)
)COLLATE='utf8mb4_0900_ai_ci';

CREATE TABLE `planning` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `nom` VARCHAR(50) NOT NULL,
    `creationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modificationDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `wednesdayUsed` TINYINT(1) NOT NULL DEFAULT 1,
    `saturdayUsed` TINYINT(1) NOT NULL DEFAULT 0,
    `idClasse` INT NOT NULL,
    `idJour` INT NOT NULL,
    FOREIGN KEY(`idClasse`) REFERENCES `classe`(`id`),
    FOREIGN KEY(`idJour`) REFERENCES `jour`(`id`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `planning_has_slots`(
    `idPlanning` INT NOT NULL,
    `idSlot` INT NOT NULL,
    PRIMARY KEY(`idPlanning`, `idSlot`),
    FOREIGN KEY(`idPlanning`) REFERENCES `planning`(`id`),
    FOREIGN KEY(`idSlot`) REFERENCES `slot`(`id`)
);