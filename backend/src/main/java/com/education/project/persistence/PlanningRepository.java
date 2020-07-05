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
import com.education.project.model.Planning;
import com.education.project.model.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class PlanningRepository {

    private Connection connection;

    private Logger LOGGER = LogManager.getLogger(PlanningRepository.class);

    public PlanningRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url) {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }// PlanningRepository()

    /**
     * Insère le planning en base de données
     * @param planningToInsert Planning à insérer
     * @return Planning inséré
     * @throws DataBaseException
     */
    public Optional<Planning> insert(Planning planningToInsert) throws DataBaseException{
        String requestSql = "INSERT INTO planning (nom, idClasse, creationDate, modificationDate) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, planningToInsert.getNom());
            ps.setInt(2, planningToInsert.getClasse().getId());
            ps.setTimestamp(3, new Timestamp(planningToInsert.getCreationDate().getTime()));
            ps.setTimestamp(4, new Timestamp(planningToInsert.getModificationDate().getTime()));
            if(ps.executeUpdate() == 0){
                throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            Integer idGenerated = generatedKeys.getInt(1);
            planningToInsert.setId(idGenerated);

            for(Slot slot : planningToInsert.getSlots()) {
                requestSql = "INSERT INTO planning_has_slots (idPlanning, idSlot) VALUES (?, ?)";
                ps = this.connection.prepareStatement(requestSql);
                ps.setInt(1, idGenerated);
                ps.setInt(2, slot.getId());
                if (ps.executeUpdate() == 0) {
                    throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
                }
            }
            return Optional.of(planningToInsert);
        }catch(SQLException e){
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Il est impossible de créér le planning");
        }
    }// insert()

    /**
     * Met à jour le planning dans la base de données
     * @param planning Planning à mettre à jour
     * @return Planning mis à jour
     * @throws DataBaseException
     */
    public Optional<Planning> update(Planning planning) throws DataBaseException{
        String requestSql = "UPDATE planning SET nom = ?, idClasse = ?, modificationDate = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, planning.getNom());
            ps.setInt(2, planning.getClasse().getId());
            ps.setTimestamp(3, new Timestamp(planning.getModificationDate().getTime()));
            ps.setInt(4, planning.getId());
            if(ps.executeUpdate() == 0){
                throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
            }

            for(Slot slot : planning.getSlots()) {
                requestSql = "UPDATE planning_has_slots SET idSlot = ? WHERE idPlanning = ?";
                ps = this.connection.prepareStatement(requestSql);
                ps.setInt(1, slot.getId());
                ps.setInt(2, planning.getId());
                if (ps.executeUpdate() == 0) {
                    throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
                }
            }
            return Optional.of(planning);
        }catch(SQLException e){
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Il est impossible de mettre à jour le planning");
        }
    }// update()
}// PlanningRepository
