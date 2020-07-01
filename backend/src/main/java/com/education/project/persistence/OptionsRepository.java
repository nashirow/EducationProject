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
import com.education.project.model.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Cette classe gère les échanges entre la base de données
 * et l'application au sujet des options générales de
 * l'application.
 */
@Repository
public class OptionsRepository {

    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(OptionsRepository.class);

    public OptionsRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url){
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }// OptionsRepository()

    /**
     * Cette fonction met à jour les options générales dans
     * la base de données.
     * @param fullOptions Options générales
     * @return boolean
     * @throws DataBaseException
     */
    public boolean update(Options fullOptions) throws DataBaseException {
        StringBuilder sb = new StringBuilder("UPDATE options SET ");
        String requestSql = "";
        int idxSplitPlanning = 0, idxStartHourPlanning = 0, idxEndHourPlanning = 0;
        if(fullOptions.getSplitPlanning() != null){
            idxSplitPlanning = 1;
            sb.append(" splitplanning = ?, ");
        }
        if(fullOptions.getStartHourPlanning() != null){
            idxStartHourPlanning = idxSplitPlanning == 0 ? 1 : 2;
            sb.append(" starthourplanning = ?, ");
        }
        if(fullOptions.getEndHourPlanning() != null){
            if(idxSplitPlanning == 0 && idxStartHourPlanning == 0){
                idxEndHourPlanning = 1;
            }else if(idxSplitPlanning == 0 && idxStartHourPlanning == 1){
                idxEndHourPlanning = 2;
            }else if(idxSplitPlanning == 1 && idxStartHourPlanning == 0){
                idxEndHourPlanning = 2;
            }else{
                idxEndHourPlanning = 3;
            }
            sb.append(" endhourplanning = ?, ");
        }
        requestSql = sb.toString();
        requestSql = requestSql.substring(0, requestSql.length()-2); // j'enlève la virgule en trop
        requestSql = requestSql + " WHERE id = 1";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(fullOptions.getSplitPlanning() != null){
                ps.setInt(idxSplitPlanning , fullOptions.getSplitPlanning());
            }
            if(fullOptions.getStartHourPlanning() != null){
                ps.setTime(idxStartHourPlanning , Time.valueOf(fullOptions.getStartHourPlanning()));
            }
            if(fullOptions.getEndHourPlanning() != null){
                ps.setTime(idxEndHourPlanning , Time.valueOf(fullOptions.getEndHourPlanning()));
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Impossible de mettre à jour les options générales, requête SQL : {}", requestSql, e);
            throw new DataBaseException("Impossible de mettre à jour les options générales");
        }
    }// update()

    /**
     * Récupère les options générales stockées en base de données
     * @return Options générales
     */
    public Optional<Options> getOptions() throws DataBaseException{
        try {
            String requestSql = "SELECT * FROM options WHERE id = 1";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(requestSql);
            resultSet.next();
            Options options = new Options(resultSet.getInt("splitplanning"),
                    resultSet.getTime("starthourplanning").toLocalTime(),
                    resultSet.getTime("endhourplanning").toLocalTime());
            return Optional.of(options);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de récupérer les options générales");
        }
    }// getOptions()

}// OptionsRepository
