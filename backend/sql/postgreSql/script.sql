CREATE DATABASE EducationProject;

CREATE TABLE matiere (
	id SERIAL PRIMARY KEY NOT NULL,
	nom VARCHAR(40) NOT NULL DEFAULT '',
	volumeHoraire VARCHAR(5) NULL DEFAULT '',
	description VARCHAR(255) NULL DEFAULT '',
	creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
	modificationDate TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN matiere.nom is 'C''est le nom de la matière.';
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

CREATE TABLE timeslot (
    id SERIAL PRIMARY KEY NOT NULL,
    startHour TIME NOT NULL,
    endHour TIME NOT NULL
);

COMMENT ON COLUMN timeslot.startHour is 'Heure de début';
COMMENT ON COLUMN timeslot.endHour is 'Heure de fin';

CREATE TABLE slot (
    id SERIAL PRIMARY KEY NOT NULL,
    comment VARCHAR(50),
    creationDate TIMESTAMP NOT NULL DEFAULT NOW(),
    modificationDate TIMESTAMP NOT NULL DEFAULT NOW(),
    couleurFond VARCHAR(15) NOT NULL DEFAULT '',
    couleurPolice VARCHAR(15) NOT NULL DEFAULT '',
    idTimeslot INT NOT NULL,
    idMatiere INT NOT NULL,
    idEnseignant INT NOT NULL,
    idSalle INT NOT NULL,
    FOREIGN KEY(idTimeslot) REFERENCES timeslot(id),
    FOREIGN KEY(idMatiere) REFERENCES matiere(id),
    FOREIGN KEY(idEnseignant) REFERENCES enseignant(id),
    FOREIGN KEY(idSalle) REFERENCES salle(id)
);

COMMENT ON COLUMN slot.comment is 'Commentaire du slot : groupes multiples, autres tâches etc.';
COMMENT ON COLUMN slot.couleurFond is 'C''est la couleur de fond de la matière.';
COMMENT ON COLUMN slot.couleurPolice is 'C''est la couleur de la police de la matière.';