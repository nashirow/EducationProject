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
import com.education.project.utils.NamePreparedStatement;
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

/**
 * Cette classe permet de gérer les échanges entre la base de données et les slots
 */
@Repository
public class SlotRepository {

    /**
     * C'est l'url de la base de données
     */
    private String url;

    /**
     * C'est le driver de la base de données
     */
    private String driver;

    /**
     * C'est l'identifiant de l'utilisateur de la base de données
     */
    private String user;

    /**
     * C'est le mot de passe de l'utilisateur de la base de données
     */
    private String password;

    /**
     * C'est la connection à la base de données
     */
    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(SlotRepository.class);


    public SlotRepository(@Value("${db.driver}") String driver, @Value("${db.url}") String url,
                          @Value("${db.user}") String user, @Value("${db.password}") String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            Class.forName(this.driver);
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }//SlotRepository()

    /**
     * Cette fonction permet d'insérer un slot en base de données
     *
     * @param slotToInsert Le slot à insérer
     * @return Le slot inséré
     */
    public Optional<Slot> insert(Slot slotToInsert) throws DataBaseException {
        String requestSql = "INSERT INTO slot (comment,creationDate,modificationDate,couleurFond,couleurPolice,idTimeslot,idMatiere,idJour,idEnseignant,idSalle) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, slotToInsert.getComment());
            ps.setTimestamp(2, new Timestamp(slotToInsert.getCreationDate().getTime()));
            ps.setTimestamp(3, new Timestamp(slotToInsert.getModificationDate().getTime()));
            ps.setString(4, slotToInsert.getCouleurFond());
            ps.setString(5, slotToInsert.getCouleurPolice());
            ps.setInt(6, slotToInsert.getTimeSlot().getId());
            ps.setInt(7, slotToInsert.getMatiere().getId());
            ps.setInt(8, slotToInsert.getJour().getId());
            if (slotToInsert.getEnseignant() != null && slotToInsert.getEnseignant().getId() != null) {
                ps.setInt(9, slotToInsert.getEnseignant().getId());
            } else {
                ps.setObject(9, null);
            }
            if (slotToInsert.getSalle() != null && slotToInsert.getSalle().getId() != null) {
                ps.setInt(10, slotToInsert.getSalle().getId());
            } else {
                ps.setObject(10, null);
            }
            int rowsAdded = ps.executeUpdate();
            if (rowsAdded > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                generatedKeys.next();
                slotToInsert.setId(generatedKeys.getInt(1));
                return Optional.of(slotToInsert);
            } else {
                throw new DataBaseException("Erreur technique : ");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de créer le slot {} dans la base de données", slotToInsert.toString(), e);
            throw new DataBaseException("Erreur technique : impossible de créer le slot dans la base de données.");
        }
    }//insert()

    /**
     * Cette fonction permet de récupéré un slot en base de données grace à son identifiant passé en paramètre
     *
     * @param id Identifiant du slot à récupéré
     * @return Le slot récupéré
     */
    public Optional<Slot> findById(int id) throws DataBaseException {
        String requestSql = "SELECT s.id AS slotId, s.comment AS slotComment,s.creationDate AS slotCreationDate,s.modificationDate AS slotModificationDate," +
                " s.couleurFond AS slotCouleurFond,s.couleurPolice AS slotCouleurPolice," +
                " j.id AS jid, j.nom AS jnom, " +
                " e.id AS enseignantId,e.nom AS enseignantNom, e.prenom AS enseignantPrenom, e.creationDate AS enseignantCreationDate,e.modificationDate AS enseignantModificationDate," +
                " m.id AS matiereId,m.nom AS matiereNom,m.volumeHoraire AS matiereVolumeHoraire,m.description AS matiereDescription,m.creationDate AS matiereCreationDate,m.modificationDate AS matiereModificationDate," +
                " t.id AS timeslotId,t.startHour AS timeslotStartHour,t.endHour AS timeslotEndHour," +
                " sa.id AS salleId, sa.nom AS salleNom, sa.creationDate AS salleCreationDate, sa.modificationDate AS salleModificationDate " +
                "FROM slot s " +
                "LEFT JOIN enseignant e ON s.idEnseignant = e.id " +
                "INNER JOIN matiere m ON s.idMatiere = m.id " +
                "INNER JOIN timeslot t ON s.idTimeslot = t.id " +
                "INNER JOIN jour j ON s.idJour = j.id " +
                "LEFT JOIN salle sa ON s.idSalle = sa.id " +
                "WHERE s.id = ?;";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Slot slot = new Slot();
                Enseignant enseignant = new Enseignant(resultSet.getInt("enseignantId"), resultSet.getString("enseignantNom"), resultSet.getString("enseignantPrenom"), resultSet.getTimestamp("enseignantCreationDate"), resultSet.getTimestamp("enseignantModificationDate"));
                Matiere matiere = new Matiere(resultSet.getInt("matiereId"), resultSet.getString("matiereNom"), resultSet.getString("matiereVolumeHoraire"), resultSet.getString("matiereDescription"), resultSet.getTimestamp("matiereCreationDate"), resultSet.getTimestamp("matiereModificationDate"));
                TimeSlot timeSlot = new TimeSlot(resultSet.getInt("timeslotId"), resultSet.getTime("timeslotStartHour").toLocalTime(), resultSet.getTime("timeslotEndHour").toLocalTime());
                Salle salle = new Salle(resultSet.getInt("salleId"), resultSet.getString("salleNom"), resultSet.getTimestamp("salleCreationDate"), resultSet.getTimestamp("salleModificationDate"));
                Jour jour = new Jour(resultSet.getInt("jid"), resultSet.getString("jnom"));
                slot.setEnseignant(enseignant);
                slot.setMatiere(matiere);
                slot.setTimeSlot(timeSlot);
                slot.setJour(jour);
                slot.setSalle(salle);
                slot.setId(resultSet.getInt("slotId"));
                slot.setComment(resultSet.getString("slotComment"));
                slot.setCreationDate(resultSet.getTimestamp("slotCreationDate"));
                slot.setModificationDate(resultSet.getTimestamp("slotModificationDate"));
                slot.setCouleurFond(resultSet.getString("slotCouleurFond"));
                slot.setCouleurPolice(resultSet.getString("slotCouleurPolice"));
                return Optional.of(slot);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de récupérer le slot d'identifiant : " + id + " dans la base de données", e);
            throw new DataBaseException("Erreur technique : impossible de récupérer le slot dans la base de données");
        }
        return Optional.empty();
    }//findById()

    /**
     * Cette fonction permet de mettre à jour un slot en base de données
     *
     * @param slotToUpdate Le slot à mettre à jour en base de données
     * @return le slot mis à jour
     */
    public Optional<Slot> update(Slot slotToUpdate) throws DataBaseException {
        String requestSql = "UPDATE slot SET comment = ?, modificationDate = ?, couleurFond = ?, couleurPolice = ?," +
                " idTimeslot = ?, idMatiere = ?, idJour = ?, idEnseignant = ?, idSalle = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, slotToUpdate.getComment());
            ps.setTimestamp(2, new Timestamp(slotToUpdate.getModificationDate().getTime()));
            ps.setString(3, slotToUpdate.getCouleurFond());
            ps.setString(4, slotToUpdate.getCouleurPolice());
            ps.setInt(5, slotToUpdate.getTimeSlot().getId());
            ps.setInt(6, slotToUpdate.getMatiere().getId());
            ps.setInt(7, slotToUpdate.getJour().getId());
            if (slotToUpdate.getEnseignant() != null && slotToUpdate.getEnseignant().getId() != null) {
                ps.setInt(8, slotToUpdate.getEnseignant().getId());
            } else {
                ps.setObject(8, null);
            }
            if (slotToUpdate.getSalle() != null && slotToUpdate.getSalle().getId() != null) {
                ps.setInt(9, slotToUpdate.getSalle().getId());
            } else {
                ps.setObject(9, null);
            }
            ps.setInt(10, slotToUpdate.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return Optional.of(slotToUpdate);
            } else {
                throw new DataBaseException("Erreur technique : impossible de mettre à jour le slot dans la base de données");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de mettre le slot {} à jour dans la base de données", slotToUpdate.toString(), e);
            throw new DataBaseException("Erreur technique : impossible de mettre à jour le slot dans la base de donnée");
        }
    }//update

    /**
     * Cette fonction permet de vérifier qu'un slot avec un certain fond de couleur existe déjà en base de données
     *
     * @param slotToInsert Le slot à vérifier
     * @return boolean
     * @throws DataBaseException
     */
    public boolean isExistByColorFondAndByDiscipline(Slot slotToInsert) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM slot WHERE couleurFond = ? AND idMatiere != ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, slotToInsert.getCouleurFond());
            ps.setInt(2, slotToInsert.getMatiere().getId());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1) > 0;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de retrouver le slot possédant la couleur de fond{} en base de données ", slotToInsert.getCouleurFond(), e);
            throw new DataBaseException("Erreur technique : impossible de retrouver le slot de couleur de fond " + slotToInsert.getCouleurFond() + " en base de données");
        }
    }//isExistByColorFondAndByDiscipline()

    /**
     * Cette fonction permet de compter le nombre de slots par jour et par créneau horaire.
     *
     * @param jourId Identifiant du jour
     * @param timeSlot Créneau horaire
     * @return Nombre de slots
     * @throws DataBaseException
     */
    public long countByJour(int jourId, TimeSlot timeSlot) throws DataBaseException {
        String requestSql = "SELECT COUNT(s.id) FROM slot s WHERE s.idJour = ? AND s.idTimeslot = ? ";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, jourId);
            ps.setInt(2, timeSlot.getId());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : Impossible de compter le nombre de slots avec les paramètres (jourId, timeSlot) {}, {}", jourId, timeSlot, e);
            throw new DataBaseException("Erreur technique : Impossible de compter le nombre de slots");
        }
    }//countByJour()

    /**
     * Cette fonction permet de supprimer un slot de la base de données avec l'identifiant passé en paramètre
     *
     * @param id L'identifiant du slot à supprimer
     * @return boolean
     */
    public boolean deleteSlot(int id) throws DataBaseException {
        try {
            String requestSql = "DELETE FROM planning_has_slots WHERE idSlot = ?";
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();

            requestSql = "DELETE FROM slot WHERE id = ?";
            ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            rowsDeleted += ps.executeUpdate();
            return rowsDeleted > 1;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de supprimer le slot {} de la base de données", id, e);
            throw new DataBaseException("Erreur technique : impossible de supprimer le slot de la base de données");
        }
    }//deleteSlot()

    /**
     * Cette fonction permet de compter le nombre de slots en base de données en fonctions des paramètres (facultatifs)
     * passés en paramètres
     *
     * @param params Paramètres facultatifs liés au slot (nom/prenom de l'enseignant, de la salle, couleur de la police et du fond du slot, heure de début et de fin d'un slot)
     * @return le nombre de slots
     * @throws DataBaseException
     */
    public long countSlot(Map<String, String> params) throws DataBaseException {
        StringBuilder sb = new StringBuilder("SELECT COUNT(s.id) FROM slot s ");
        sb.append("LEFT JOIN enseignant e ON s.idEnseignant = e.id ");
        sb.append("INNER JOIN matiere m ON s.idMatiere = m.id ");
        sb.append("INNER JOIN timeslot t ON s.idTimeslot = t.id ");
        sb.append("LEFT JOIN salle sa ON s.idSalle = sa.id ");
        sb.append("INNER JOIN jour j ON s.idJour = j.id ");
        sb.append("WHERE TRUE ");
        if(params != null){
            if(params.containsKey("jour") && params.get("jour") != null && !params.get("jour").isEmpty()){
                sb.append("AND j.nom LIKE :jourNom ");
            }
            if (params.containsKey("couleurFond") && params.get("couleurFond") != null && !params.get("couleurFond").isEmpty()) {
                sb.append("AND s.couleurFond = :couleurFond ");
            }
            if (params.containsKey("couleurPolice") && params.get("couleurPolice") != null && !params.get("couleurFond").isEmpty()) {
                sb.append("AND s.couleurPolice = :couleurPolice ");
            }
            if (params.containsKey("enseignantNom") && params.get("enseignantNom") != null && !params.get("enseignantNom").isEmpty()) {
                sb.append("AND e.nom LIKE :enseignantNom ");
            }
            if (params.containsKey("enseignantPrenom") && params.get("enseignantPrenom") != null && !params.get("enseignantPrenom").isEmpty()) {
                sb.append("AND e.prenom LIKE :enseignantPrenom ");
            }
            if (params.containsKey("matiereNom") && params.get("matiereNom") != null && !params.get("matiereNom").isEmpty()) {
                sb.append("AND m.nom LIKE :matiereNom ");
            }
            if (params.containsKey("startHour") && params.get("startHour") != null && !params.get("startHour").isEmpty()) {
                sb.append("AND t.startHour = :startHour ");
            }
            if (params.containsKey("endHour") && params.get("endHour") != null && !params.get("endHour").isEmpty()) {
                sb.append("AND t.endHour = :endHour ");
            }
            if (params.containsKey("salleNom") && params.get("salleNom") != null && !params.get("salleNom").isEmpty()) {
                sb.append("AND sa.nom LIKE :salleNom ");
            }
        }

        String requestSql = sb.toString();
        try {
            NamePreparedStatement ps = new NamePreparedStatement(this.connection, requestSql);
            if(params != null){
                if(params.containsKey("jour") && params.get("jour") != null && !params.get("jour").isEmpty()){
                    ps.setString("jourNom", "%" + params.get("jour") + "%");
                }
                if (params.containsKey("couleurFond") && params.get("couleurFond") != null && !params.get("couleurFond").isEmpty()) {
                    ps.setString("couleurFond", "%" + params.get("couleurFond") + "%");
                }
                if (params.containsKey("couleurPolice") && params.get("couleurPolice") != null && !params.get("couleurPolice").isEmpty()) {
                    ps.setString("couleurPolice", "%" + params.get("couleurPolice") + "%");
                }
                if (params.containsKey("enseignantNom") && params.get("enseignantNom") != null && !params.get("enseignantNom").isEmpty()) {
                    ps.setString("enseignantNom", "%" + params.get("enseignantNom") + "%");
                }
                if (params.containsKey("enseignantPrenom") && params.get("enseignantPrenom") != null && !params.get("enseignantPrenom").isEmpty()) {
                    ps.setString("enseignantPrenom", "%" + params.get("enseignantPrenom") + "%");
                }
                if (params.containsKey("matiereNom") && params.get("matiereNom") != null && !params.get("matiereNom").isEmpty()) {
                    ps.setString("matiereNom", "%" + params.get("matiereNom") + "%");
                }
                if (params.containsKey("startHour") && params.get("startHour") != null && !params.get("startHour").isEmpty()) {
                    ps.setString("startHour", "%" + params.get("startHour") + "%");
                }
                if (params.containsKey("endHour") && params.get("endHour") != null && !params.get("endHour").isEmpty()) {
                    ps.setString("endHour", params.get("endHour"));
                }
                if (params.containsKey("salleNom") && params.get("salleNom") != null && !params.get("salleNom").isEmpty()) {
                    ps.setString("salleNom", "%" + params.get("salleNom") + "%");
                }
            }
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de compter le nombre de slots {} dans la base de données", params.toString(), e);
            throw new DataBaseException("Erreur technique : impossible de compter le nombre de slots dans la base de données");
        }
    }//countSlot()

    /**
     * Cette fonction permet de récupérer les slots en base de données en fonction des informations passées en paramètres
     * @param params informations liés aux slots
     * @return les slots récupérés
     */
    public List<Slot> getSlots(Map<String, String> params) throws DataBaseException {
        List<Slot> resultSlots = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT s.id AS slotId, s.comment AS slotComment, s.creationDate AS slotCreationDate, s.modificationDate AS slotModificationDate, ");
        sb.append("s.couleurFond AS slotCouleurFond, s.couleurPolice AS slotCouleurPolice, ");
        sb.append("j.id AS jourId, j.nom AS jourNom, e.id AS enseignantId, e.nom AS enseignantNom, e.prenom AS enseignantPrenom, e.creationDate AS enseignantCreationDate, e.modificationDate AS enseignantModificationDate, ");
        sb.append("m.id AS matiereId, m.nom AS matiereNom,m.volumeHoraire AS matiereVolumeHoraire, m.description AS matiereDescription, m.creationDate AS matiereCreationDate, m.modificationDate AS matiereModificationDate, ");
        sb.append("t.id AS timeslotId, t.startHour AS timeslotStartHour, t.endHour AS timeslotEndHour, ");
        sb.append(" sa.id AS salleId, sa.nom AS salleNom, sa.creationDate AS salleCreationDate, sa.modificationDate AS salleModificationDate ");
        sb.append("FROM slot s ");
        sb.append("LEFT JOIN enseignant e ON s.idEnseignant = e.id ");
        sb.append("INNER JOIN matiere m ON s.idMatiere = m.id ");
        sb.append("INNER JOIN timeslot t ON s.idTimeslot = t.id ");
        sb.append("LEFT JOIN salle sa ON s.idSalle = sa.id ");
        sb.append("INNER JOIN jour j ON s.idJour = j.id ");
        sb.append("WHERE TRUE ");
        if(params != null){
            if(params.containsKey("jour") && params.get("jour") != null && !params.get("jour").isEmpty()){
                sb.append("AND j.nom LIKE :jourNom ");
            }
            if (params.containsKey("couleurFond") && params.get("couleurFond") != null && !params.get("couleurFond").isEmpty()) {
                sb.append(" AND s.couleurFond = :couleurFond ");
            }
            if (params.containsKey("couleurPolice") && params.get("couleurPolice") != null && !params.get("couleurFond").isEmpty()) {
                sb.append("AND s.couleurPolice = :couleurPolice ");
            }
            if (params.containsKey("enseignantNom") && params.get("enseignantNom") != null && !params.get("enseignantNom").isEmpty()) {
                sb.append("AND e.nom LIKE :enseignantNom ");
            }
            if (params.containsKey("enseignantPrenom") && params.get("enseignantPrenom") != null && !params.get("enseignantPrenom").isEmpty()) {
                sb.append("AND e.prenom LIKE :enseignantPrenom ");
            }
            if (params.containsKey("matiereNom") && params.get("matiereNom") != null && !params.get("matiereNom").isEmpty()) {
                sb.append("AND m.nom LIKE :matiereNom ");
            }
            if (params.containsKey("startHour") && params.get("startHour") != null && !params.get("startHour").isEmpty()) {
                sb.append("AND t.startHour = :startHour ");
            }
            if (params.containsKey("endHour") && params.get("endHour") != null && !params.get("endHour").isEmpty()) {
                sb.append("AND t.endHour = :endHour ");
            }
            if (params.containsKey("salleNom") && params.get("salleNom") != null && !params.get("salleNom").isEmpty()) {
                sb.append("AND sa.nom LIKE :salleNom ");
            }
        }
        sb.append("ORDER BY s.id ASC ");
        if ((params != null && params.containsKey("page") && params.get("page") != null && !params.get("page").isEmpty()) && (params != null && params.containsKey("nbElementsPerPage") && params.get("nbElementsPerPage") != null && !params.get("nbElementsPerPage").isEmpty())) {
            sb.append("LIMIT :nbElementsPerPage OFFSET :offset");
        }
        String requestSql = sb.toString();
        try {
            NamePreparedStatement ps = new NamePreparedStatement(this.connection, requestSql);
            if(params != null){
                if(params.containsKey("jour") && params.get("jour") != null && !params.get("jour").isEmpty()){
                    ps.setString("jourNom", "%" + params.get("jour") + "%");
                }
                if (params.containsKey("couleurFond") && params.get("couleurFond") != null && !params.get("couleurFond").isEmpty()) {
                    ps.setString("couleurFond", "%" + params.get("couleurFond") + "%");
                }
                if (params.containsKey("couleurPolice") && params.get("couleurPolice") != null && !params.get("couleurPolice").isEmpty()) {
                    ps.setString("couleurPolice", "%" + params.get("couleurPolice") + "%");
                }
                if (params.containsKey("enseignantNom") && params.get("enseignantNom") != null && !params.get("enseignantNom").isEmpty()) {
                    ps.setString("enseignantNom", "%" + params.get("enseignantNom") + "%");
                }
                if (params.containsKey("enseignantPrenom") && params.get("enseignantPrenom") != null && !params.get("enseignantPrenom").isEmpty()) {
                    ps.setString("enseignantPrenom", "%" + params.get("enseignantPrenom") + "%");
                }
                if (params.containsKey("matiereNom") && params.get("matiereNom") != null && !params.get("matiereNom").isEmpty()) {
                    ps.setString("matiereNom", "%" + params.get("matiereNom") + "%");
                }
                if (params.containsKey("startHour") && params.get("startHour") != null && !params.get("startHour").isEmpty()) {
                    ps.setString("startHour",  params.get("startHour"));
                }
                if (params.containsKey("endHour") && params.get("endHour") != null && !params.get("endHour").isEmpty()) {
                    ps.setString("endHour", params.get("endHour"));
                }
                if (params.containsKey("salleNom") && params.get("salleNom") != null && !params.get("salleNom").isEmpty()) {
                    ps.setString("salleNom", "%" + params.get("salleNom") + "%");
                }
                if (params.containsKey("nbElementsPerPage") && params.get("nbElementsPerPage") != null && !params.get("nbElementsPerPage").isEmpty()) {
                    ps.setInt("nbElementsPerPage", Integer.valueOf(params.get("nbElementsPerPage")));
                }
                if (params.containsKey("page") && params.get("page") != null && !params.get("page").isEmpty() && params.containsKey("nbElementsPerPage") && params.get("nbElementsPerPage") != null && !params.get("nbElementsPerPage").isEmpty()) {
                    ps.setInt("offset", (Integer.valueOf(params.get("page")) - 1) * (Integer.valueOf(params.get("nbElementsPerPage"))));
                }
            }
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Enseignant enseignant = new Enseignant(resultSet.getInt("enseignantId"),resultSet.getString("enseignantNom"),resultSet.getString("enseignantPrenom"),resultSet.getTimestamp("enseignantCreationDate"),resultSet.getTimestamp("enseignantModificationDate"));
                Matiere matiere = new Matiere(resultSet.getInt("matiereId"),resultSet.getString("matiereNom"),resultSet.getString("matiereVolumeHoraire"),resultSet.getString("matiereDescription"),resultSet.getTimestamp("matiereCreationDate"),resultSet.getTimestamp("matiereModificationDate"));
                Salle salle = new Salle(resultSet.getInt("salleId"),resultSet.getString("salleNom"),resultSet.getTimestamp("salleCreationDate"),resultSet.getTimestamp("salleModificationDate"));
                TimeSlot timeSlot = new TimeSlot(resultSet.getInt("timeslotId"),LocalTime.parse(resultSet.getString("timeslotStartHour")),LocalTime.parse(resultSet.getString("timeslotEndHour")));
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
                resultSlots.add(slot);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur techinque : impossible de récupérer les slots {} de la base de données", params.toString(),e);
            throw new DataBaseException("Erreur technique : impossible de récupérer les slots de la base de données");
        }
        return resultSlots;
    }//getSlots()
}//SlotRepository()
