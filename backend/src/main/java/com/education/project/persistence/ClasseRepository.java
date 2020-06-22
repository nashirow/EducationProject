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

    /**
     * Supprime une classe en base de données
     * @param id Identifiant de la classe à supprimer en base de données
     * @return boolean
     */
    public boolean delete(int id) throws DataBaseException {
        String requestSql = "DELETE FROM classe WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            long nbClassesDeleted = ps.executeUpdate();
            return nbClassesDeleted > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de supprimer la classe d'identifiant " + id);
        }
    }// delete()

    /**
     * Retourne une classe en fonction de son identifiant
     * @param id Identifiant de la classe
     * @return Classe
     */
    public Optional<Classe> findById(int id) throws DataBaseException {
        String requestSql = "SELECT * FROM classe WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                Timestamp creationDateFromBd = resultSet.getTimestamp("creationDate");
                Timestamp modificationDateFromBd = resultSet.getTimestamp("modificationDate");
                Classe classe = new Classe(resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        new java.util.Date(creationDateFromBd.getTime()),
                        new java.util.Date(modificationDateFromBd.getTime()));
                return Optional.of(classe);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de trouver la classe avec l'identifiant " + id);
        }
    }// findById()
    /**
     * Met à jour la classe passée en paramètre en base de données
     * @param classeToUpdate Classe à mettre à jour
     * @return Classe mis à jour
     */
    public Optional<Classe> update(Classe classeToUpdate) throws DataBaseException {
        String requestSql = "UPDATE classe SET nom = ?, modificationDate = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1, classeToUpdate.getNom());
            ps.setTimestamp(2, new Timestamp(classeToUpdate.getModificationDate().getTime()));
            ps.setInt(3, classeToUpdate.getId());
            int rowsAdded = ps.executeUpdate();
            if(rowsAdded > 0){
                return Optional.of(classeToUpdate);
            }else{
                throw new DataBaseException("Impossible de mettre à jour la classe (vérifiez votre identifiant de classe)");
            }
        } catch (SQLException e) {
            LOGGER.error("Impossible de mettre à jour la classe {}", classeToUpdate.toString(), e);
            throw new DataBaseException("Impossible de mettre à jour la classe");
        }
    }// update()

}// ClasseRepository
