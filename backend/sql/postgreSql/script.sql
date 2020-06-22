CREATE DATABASE EducationProject;

CREATE TABLE matiere (
	id SERIAL PRIMARY KEY NOT NULL,
	nom VARCHAR(40) NOT NULL DEFAULT '',
	couleurFond VARCHAR(15) NOT NULL DEFAULT '',
	couleurPolice VARCHAR(15) NOT NULL DEFAULT '',
	volumeHoraire VARCHAR(5) NULL DEFAULT '',
	description VARCHAR(255) NULL DEFAULT '',
	creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
	modificationDate TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN matiere.nom is 'C''est le nom de la matière.';
COMMENT ON COLUMN matiere.couleurFond is 'C''est la couleur de fond de la matière.';
COMMENT ON COLUMN matiere.couleurPolice is 'C''est la couleur de la police de la matière.';
COMMENT ON COLUMN matiere.volumeHoraire is 'C''est le volume horaire d''une matière.';
COMMENT ON COLUMN matiere.description is 'C''est la description d''une matière.';

CREATE TABLE classe (
    id SERIAL PRIMARY KEY NOT NULL,
    nom VARCHAR(40) NOT NULL DEFAULT '',
    creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
    modificationDate TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN classe.nom is 'C''est le nom de la classe.';