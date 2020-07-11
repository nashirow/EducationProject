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
import com.education.project.model.Jour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de gérer les requêtes SQL en lien avec les jours
 */
@Repository
public class DayRepository {

    /**
     * Driver SQL
     */
    private String driver;

    /**
     * URL de la base de données
     */
    private String url;

    /**
     * Nom d'utilisateur de la BD
     */
    private String user;

    /**
     * Mot de passe de l'utilisateur de la BD
     */
    private String password;

    private Connection connection;

    private final static Logger LOGGER = LogManager.getLogger(DayRepository.class);

    public DayRepository(@Value("${db.driver}") String driver,
                         @Value("${db.url}") String url,
                         @Value("${db.user}") String user,
                         @Value("${db.password}") String password){

        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            Class.forName(this.driver);
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }//DayRepository()

    /**
     * Cette fonction permet de récupérer la liste des jours en base de données
     * @return Liste des jours
     * @throws DataBaseException
     */
    public List<Jour> getDays() throws DataBaseException {
        List<Jour> jours = new ArrayList<>();
        String requestSql = "SELECT * FROM jour";
        try {
            Statement ps = this.connection.createStatement();
            ResultSet resultSet = ps.executeQuery(requestSql);
            while(resultSet.next()){
                Jour jour = new Jour();
                jour.setId(resultSet.getInt("id"));
                jour.setNom(resultSet.getString("nom"));
                jours.add(jour);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de récupérer les jours de la base de données",e);
            throw new DataBaseException("Erreur technique : impossible de récupérer les jours de la base de données");
        }
        return jours;
    }//getDays()
}//DayRepository()
