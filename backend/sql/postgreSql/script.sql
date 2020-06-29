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

CREATE TABLE enseignant (
	id SERIAL PRIMARY KEY NOT NULL,
	nom VARCHAR(40) NOT NULL DEFAULT '',
	prenom VARCHAR(40) NOT NULL DEFAULT '',
	creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
	modificationDate TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN enseignant.nom is 'C''est le nom de l''enseignant';
COMMENT ON COLUMN enseignant.prenom is 'C''est le prénom de l''enseignant';

CREATE TABLE salle (
	id SERIAL PRIMARY KEY NOT NULL,
	nom VARCHAR(40) NOT NULL DEFAULT '',
	creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
	modificationDate TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN salle.nom is 'C''est le nom de la salle';

CREATE TABLE options (
    id INTEGER PRIMARY KEY NOT NULL,
    splitPlanning INTEGER NOT NULL DEFAULT 60,
    startHourPlanning TIME NOT NULL DEFAULT '08:00:00',
    endHourPlanning TIME NOT NULL DEFAULT '17:00:00'
);

COMMENT ON COLUMN options.splitPlanning is 'Temps de découpage du planning';
COMMENT ON COLUMN options.startHourPlanning is 'Heure de début du planning';
COMMENT ON COLUMN options.endHourPlanning is 'Heure de fin du planning';

INSERT INTO options(id, splitplanning, starthourplanning, endhourplanning) VALUES (1, 60, '08:00:00', '17:00:00');