/*
 * Copyright 2020 Hicham AZIMANI
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
import com.education.project.model.TimeSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeSlotRepository {

    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(TimeSlotRepository.class);

    public TimeSlotRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url){
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }// TimeSlotRepository()

    /**
     * Insère un créneau horaire en base de données.
     * @param ts Créneau horaire
     * @return Créneau horaire ajouté en base de données
     * @throws DataBaseException
     */
    public Optional<TimeSlot> insert(TimeSlot ts) throws DataBaseException {
        String requestSql = "INSERT INTO timeslot (startHour, endHour) VALUES (?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setTime(1, Time.valueOf(ts.getStart()));
            ps.setTime(2, Time.valueOf(ts.getEnd()));
            int nbRowsAdded = ps.executeUpdate();
            if(nbRowsAdded > 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.next();
                ts.setId(resultSet.getInt(1));
                return Optional.of(ts);
            }else{
                throw new DataBaseException("Erreur technique : Impossible de créer un nouveau créneau horaire : " + ts.toString());
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de créer un nouveau créneau horaire : " + ts.toString());
        }
    }// insert()

    /**
     * Vérifie si le créneau horaire passé en paramètre existe en base de données.
     * @param ts Créneau horaire à vérifier
     * @return boolean
     */
    public boolean exists(TimeSlot ts) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM timeslot WHERE startHour = ? AND endHour = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setTime(1, Time.valueOf(ts.getStart()));
            ps.setTime(2, Time.valueOf(ts.getEnd()));
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            return count > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de vérifier si le créneau horaire " + ts.toString() + " existe en base de données");
        }
    }// exists()

    /**
     * Compte le nombre de créneaux horaires disponibles.
     * @return total
     * @throws DataBaseException
     */
    public long count() throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM timeslot";
        try {
            Statement s = this.connection.createStatement();
            ResultSet resultSet = s.executeQuery(requestSql);
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de compter le nombre total de créneaux horaires");
        }
    }// count()

    /**
     * Supprime un créneau horaire dans la base de données.
     * @param id Identifiant du créneau horaire à supprimer
     * @return boolean
     * @throws DataBaseException
     */
    public boolean delete(int id) throws DataBaseException {
        String requestSql = "DELETE FROM timeslot WHERE id = ?";
        try{
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }catch(SQLException e){
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de supprimer le créneau horaire n°" + id);
        }
    }// delete()

    /**
     * Récupère les créneaux horaires stockés dans la base de données
     * @param page n° de la page (facultatif)
     * @param nbElementsPerPage Nombre de créneaux horaires par page (facultatif)
     * @return créneaux horaires
     * @throws DataBaseException
     */
    public List<TimeSlot> findAll(Integer page, Integer nbElementsPerPage) throws DataBaseException {
        List<TimeSlot> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM timeslot");
        if(page != null && nbElementsPerPage != null){
            sb.append(" LIMIT ? OFFSET ? ");
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(page != null && nbElementsPerPage != null){
                ps.setInt(1, nbElementsPerPage);
                ps.setInt(2, (page - 1) * nbElementsPerPage);
            }
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                results.add(
                    new TimeSlot(resultSet.getInt("id"),
                                 resultSet.getTime("starthour").toLocalTime(),
                                 resultSet.getTime("endhour").toLocalTime())
                );
            }
            return results;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur Technique : Impossible de récupérer les créneaux horaires");
        }
    }// findAll()
}// TimeSlotRepository
