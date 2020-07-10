/*
 * Copyright 2020 Hicham AZIMANI, Yassine AZIMANI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.education.project.persistence;

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PlanningRepository {

    private Connection connection;

    private Logger LOGGER = LogManager.getLogger(PlanningRepository.class);

    public PlanningRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url) {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }// PlanningRepository()

    /**
     * Insère le planning en base de données
     *
     * @param planningToInsert Planning à insérer
     * @return Planning inséré
     * @throws DataBaseException
     */
    public Optional<Planning> insert(Planning planningToInsert) throws DataBaseException {
        String requestSql = "INSERT INTO planning (nom, idClasse, creationDate, modificationDate) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, planningToInsert.getNom());
            ps.setInt(2, planningToInsert.getClasse().getId());
            ps.setTimestamp(3, new Timestamp(planningToInsert.getCreationDate().getTime()));
            ps.setTimestamp(4, new Timestamp(planningToInsert.getModificationDate().getTime()));
            if (ps.executeUpdate() == 0) {
                throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            Integer idGenerated = generatedKeys.getInt(1);
            planningToInsert.setId(idGenerated);

            for (Slot slot : planningToInsert.getSlots()) {
                requestSql = "INSERT INTO planning_has_slots (idPlanning, idSlot) VALUES (?, ?)";
                ps = this.connection.prepareStatement(requestSql);
                ps.setInt(1, idGenerated);
                ps.setInt(2, slot.getId());
                if (ps.executeUpdate() == 0) {
                    throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
                }
            }
            return Optional.of(planningToInsert);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
        }
    }// insert()

    /**
     * Met à jour le planning dans la base de données
     *
     * @param planning Planning à mettre à jour
     * @return Planning mis à jour
     * @throws DataBaseException
     */
    public Optional<Planning> update(Planning planning) throws DataBaseException {
        String requestSql = "UPDATE planning SET nom = ?, idClasse = ?, modificationDate = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, planning.getNom());
            ps.setInt(2, planning.getClasse().getId());
            ps.setTimestamp(3, new Timestamp(planning.getModificationDate().getTime()));
            ps.setInt(4, planning.getId());
            if (ps.executeUpdate() == 0) {
                throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
            }

            for (Slot slot : planning.getSlots()) {
                requestSql = "UPDATE planning_has_slots SET idSlot = ? WHERE idPlanning = ?";
                ps = this.connection.prepareStatement(requestSql);
                ps.setInt(1, slot.getId());
                ps.setInt(2, planning.getId());
                if (ps.executeUpdate() == 0) {
                    throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
                }
            }
            return Optional.of(planning);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
        }
    }// update()

    /**
     * Cette fonction permet de supprimer un planning de la base de données
     *
     * @param id identifiant du planning
     * @return boolean
     * @throws DataBaseException
     */
    public boolean deletePlanning(Integer id) throws DataBaseException {
        try {
            String requestSql = "DELETE FROM planning_has_slots WHERE idPlanning = ?";
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();

            requestSql = "DELETE FROM planning WHERE id = ?";
            ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            rowsDeleted += ps.executeUpdate();
            return rowsDeleted > 1;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de supprimer le planning {} de la base de données", id, e);
            throw new DataBaseException("Erreur technique : impossible de supprimer le planning de la base de données");
        }
    }//deletePlanning()

    /**
     * Récupère un planning en fonction de son identifiant
     *
     * @param id Identifiant du planning à récupérer
     * @return Planning
     */
    public Optional<Planning> findById(int id) throws DataBaseException {
        StringBuilder sb = new StringBuilder("SELECT p.id AS pid, p.nom AS pnom, p.creationDate AS pcreationDate, p.modificationDate AS pmodificationDate, p.wednesdayUsed AS pwednesdayUsed, p.saturdayUsed AS psaturdayUsed,");
        sb.append("p.idClasse AS pidClasse, c.nom AS cnom, c.creationDate AS ccreationDate, c.modificationDate AS cmodificationDate,");
        sb.append("s.id AS slotId, s.comment AS slotComment, s.creationDate AS slotCreationDate, s.modificationDate AS slotModificationDate, ");
        sb.append("s.couleurFond AS slotCouleurFond, s.couleurPolice AS slotCouleurPolice, ");
        sb.append("j.id AS jourId, j.nom AS jourNom, e.id AS enseignantId, e.nom AS enseignantNom, e.prenom AS enseignantPrenom, e.creationDate AS enseignantCreationDate, e.modificationDate AS enseignantModificationDate, ");
        sb.append("m.id AS matiereId, m.nom AS matiereNom,m.volumeHoraire AS matiereVolumeHoraire, m.description AS matiereDescription, m.creationDate AS matiereCreationDate, m.modificationDate AS matiereModificationDate, ");
        sb.append("t.id AS timeslotId, t.startHour AS timeslotStartHour, t.endHour AS timeslotEndHour, ");
        sb.append(" sa.id AS salleId, sa.nom AS salleNom, sa.creationDate AS salleCreationDate, sa.modificationDate AS salleModificationDate ");
        sb.append("FROM planning p ");
        sb.append("INNER JOIN classe c ON c.id = p.idClasse ");
        sb.append("INNER JOIN planning_has_slots phs ON phs.idPlanning = p.id ");
        sb.append("INNER JOIN slot s ON phs.idSlot = s.id ");
        sb.append("LEFT JOIN enseignant e ON s.idEnseignant = e.id ");
        sb.append("INNER JOIN matiere m ON s.idMatiere = m.id ");
        sb.append("INNER JOIN timeslot t ON s.idTimeslot = t.id ");
        sb.append("LEFT JOIN salle sa ON s.idSalle = sa.id ");
        sb.append("INNER JOIN jour j ON s.idJour = j.id ");
        sb.append("WHERE p.id = ?");

        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            Planning planning = new Planning();
            planning.setSlots(new ArrayList<>());
            while (resultSet.next()) {
                if (planning.getId() == null) {
                    planning.setId(resultSet.getInt("pid"));
                    planning.setNom(resultSet.getString("pnom"));
                    planning.setModificationDate(resultSet.getTimestamp("pmodificationDate"));
                    planning.setCreationDate(resultSet.getTimestamp("pcreationDate"));
                    Classe classe = new Classe(
                            resultSet.getInt("pidClasse"),
                            resultSet.getString("cnom"),
                            resultSet.getTimestamp("ccreationDate"),
                            resultSet.getTimestamp("cmodificationDate")
                    );
                    planning.setClasse(classe);
                    planning.setSaturdayUsed(resultSet.getBoolean("psaturdayUsed"));
                    planning.setWednesdayUsed(resultSet.getBoolean("pwednesdayUsed"));
                }
                Enseignant enseignant = new Enseignant(resultSet.getInt("enseignantId"), resultSet.getString("enseignantNom"), resultSet.getString("enseignantPrenom"), resultSet.getTimestamp("enseignantCreationDate"), resultSet.getTimestamp("enseignantModificationDate"));
                Matiere matiere = new Matiere(resultSet.getInt("matiereId"), resultSet.getString("matiereNom"), resultSet.getString("matiereVolumeHoraire"), resultSet.getString("matiereDescription"), resultSet.getTimestamp("matiereCreationDate"), resultSet.getTimestamp("matiereModificationDate"));
                Salle salle = new Salle(resultSet.getInt("salleId"), resultSet.getString("salleNom"), resultSet.getTimestamp("salleCreationDate"), resultSet.getTimestamp("salleModificationDate"));
                TimeSlot timeSlot = new TimeSlot(resultSet.getInt("timeslotId"), LocalTime.parse(resultSet.getString("timeslotStartHour")), LocalTime.parse(resultSet.getString("timeslotEndHour")));
                Jour jour = new Jour(resultSet.getInt("jourId"), resultSet.getString("jourNom"));
                Slot slot = new Slot();
                slot.setId(resultSet.getInt("slotId"));
                slot.setCouleurFond(resultSet.getString("slotCouleurFond"));
                slot.setCouleurPolice(resultSet.getString("slotCouleurPolice"));
                slot.setComment(resultSet.getString("slotComment"));
                slot.setCreationDate(resultSet.getTimestamp("slotCreationDate"));
                slot.setModificationDate(resultSet.getTimestamp("slotModificationDate"));
                slot.setEnseignant(enseignant);
                slot.setMatiere(matiere);
                slot.setSalle(salle);
                slot.setTimeSlot(timeSlot);
                slot.setJour(jour);
                planning.getSlots().add(slot);
            }
            return Optional.of(planning);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de récupérer le planning n°" + id);
        }
    }// findById()

    /**
     * Cette fonction permet de récupérer tous les plannings dans la base de données
     *
     * @return liste des plannings
     */
    public List<Planning> getPlannings(Map<String, String> params) throws DataBaseException {
        List<Planning> resultPlannings = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT p.id AS pid, p.nom AS pnom, p.creationDate AS pcreationDate, p.modificationDate AS pmodificationDate, p.wednesdayUsed AS pwednesdayUsed, p.saturdayUsed AS psaturdayUsed, ");
        sb.append("p.idClasse AS pidClasse, c.nom AS cnom, c.creationDate AS ccreationDate, c.modificationDate AS cmodificationDate ");
        sb.append("FROM planning p INNER JOIN classe c ON c.id = p.idClasse ");
        if (params != null) {
            if (params.containsKey("classeNom") && params.get("classeNom") != null && !params.get("classeNom").isEmpty()) {
                sb.append("WHERE c.nom LIKE ? ");
            }
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if (params != null) {
                if (params.containsKey("classeNom") && params.get("classeNom") != null && !params.get("classeNom").isEmpty()) {
                    ps.setString(1, "%" + params.get("classeNom") + "%");
                }
            }
            ResultSet resultSet = ps.executeQuery();

            Planning planning;
            while (resultSet.next()) {
                planning = new Planning();
                planning.setId(resultSet.getInt("pid"));
                planning.setNom(resultSet.getString("pnom"));
                planning.setModificationDate(resultSet.getTimestamp("pmodificationDate"));
                planning.setCreationDate(resultSet.getTimestamp("pcreationDate"));
                Classe classe = new Classe(resultSet.getInt("pidClasse"), resultSet.getString("cnom"), resultSet.getTimestamp("ccreationDate"), resultSet.getTimestamp("cmodificationDate"));
                planning.setClasse(classe);
                planning.setSaturdayUsed(resultSet.getBoolean("psaturdayUsed"));
                planning.setWednesdayUsed(resultSet.getBoolean("pwednesdayUsed"));
                resultPlannings.add(planning);
            }
            return resultPlannings;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de récupérer les plannings dans la base de données", e);
            throw new DataBaseException("Erreur technique impossible de récupérer les plannings dans la base de données");
        }
    }//getPlannings()

}// PlanningRepository
