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
import com.education.project.model.Classe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Cette classe permet de gérer les requêtes SQL
 * pour une classe.
 */
@Repository
public class ClasseRepository {

    /**
     * Driver SQL
     */
    private String driver;

    /**
     * Url de la base de données
     */
    private String url;

    /**
     * Mot de passe du compte utilisateur (BD)
     */
    private String password;

    /**
     * Compte utilisateur (BD)
     */
    private String user;

    /**
     * Connexion à la base de données
     */
    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(ClasseRepository.class);

    public ClasseRepository(@Value("${db.driver}") String driver,
                            @Value("${db.url}") String url,
                            @Value("${db.password}") String password,
                            @Value("${db.user}") String user) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.user = user;
        try {
            Class.forName(this.driver);
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        }catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }// ClasseRepository()

    /**
     * Cette fonction permet de savoir si une classe avec
     * le nom passé en paramètre existe en base de données.
     * @param nom Nom de la classe à trouver
     * @return boolean
     */
    public boolean existsByName(String nom) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM classe WHERE nom = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, nom);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long countClasses = resultSet.getLong(1);
            return countClasses > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de rechercher une classe ayant le nom " + nom);
        }
    }// existsByName()

    /**
     * Cette fonction insère la classe en base de données.
     * @param classeToInsert Classe à insérer
     * @return La classe insérée
     */
    public Optional<Classe> insert(Classe classeToInsert) throws DataBaseException {
        String requestSql = "INSERT INTO classe (nom, creationDate, modificationDate) VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, classeToInsert.getNom());
            ps.setTimestamp(2, new Timestamp(classeToInsert.getCreationDate().getTime()));
            ps.setTimestamp(3, new Timestamp(classeToInsert.getModificationDate().getTime()));
            int rowsAdded = ps.executeUpdate();
            if(rowsAdded > 0){
                ResultSet generatedKeys = ps.getGeneratedKeys();
                generatedKeys.next();
                classeToInsert.setId(generatedKeys.getInt(1));
                return Optional.of(classeToInsert);
            }else{
                throw new DataBaseException("Impossible d'insérer la classe");
            }
        } catch (SQLException e) {
            LOGGER.error("Impossible d'insérer la classe {}", classeToInsert.toString(), e);
            throw new DataBaseException("Impossible d'insérer la classe");
        }
    }// insert()

}// ClasseRepository
