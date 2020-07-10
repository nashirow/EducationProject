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
import com.education.project.model.Classe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
            throw new DataBaseException("Erreur technique : Impossible de rechercher une classe ayant le nom " + nom);
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
                throw new DataBaseException("Erreur technique : Impossible d'insérer la classe");
            }
        } catch (SQLException e) {
            LOGGER.error("Impossible d'insérer la classe {}", classeToInsert.toString(), e);
            throw new DataBaseException("Erreur technique : Impossible d'insérer la classe");
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
            throw new DataBaseException("Erreur technique : Impossible de supprimer la classe d'identifiant " + id);
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
                return Optional.of(initClasse(resultSet));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de trouver la classe avec l'identifiant " + id);
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
                throw new DataBaseException("Erreur technique : Impossible de mettre à jour la classe (vérifiez votre identifiant de classe)");
            }
        } catch (SQLException e) {
            LOGGER.error("Impossible de mettre à jour la classe {}", classeToUpdate.toString(), e);
            throw new DataBaseException("Erreur technique : Impossible de mettre à jour la classe");
        }
    }// update()

    /**
     * Retourne le nombre total de classes
     * @param name Filtre sur le nom de la classe
     * @throws DataBaseException
     * @return nombre total de classes
     */
    public long count(String name) throws DataBaseException{
        StringBuilder sb = new StringBuilder("SELECT COUNT(id) FROM classe ");
        if(name != null && !name.isEmpty()){
            sb.append(" WHERE nom LIKE ?");
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(name != null && !name.isEmpty()){
                ps.setString(1, "%" + name + "%");
            }
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de compter le nombre de classes avec nom = " + ((name != null) ? name : ""));
        }
    }// count()

    /**
     * Retourne l'ensemble des classes en fonction de filtres.
     * @param page n° de la page à récupérer (facultatif)
     * @param nbElementsPerPage Nombre d'éléments par page (facultatif)
     * @param name Nom de la classe (facultatif)
     * @return Liste de classes
     */
    public List<Classe> getClasses(Integer page, Integer nbElementsPerPage, String name) throws DataBaseException {
        List<Classe> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM classe ");
        int indiceNom = 0;
        if(name != null && !name.isEmpty()){
            sb.append(" WHERE nom LIKE ? ");
            indiceNom = 1;
        }
        if(page != null && nbElementsPerPage != null){
            sb.append(" LIMIT ? OFFSET ? ");
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(name != null && !name.isEmpty()){
                ps.setString(indiceNom, "%" + name + "%");
            }
            if(page != null && nbElementsPerPage != null){
                ps.setInt(indiceNom+1, nbElementsPerPage);
                ps.setInt(indiceNom+2, (page-1) * nbElementsPerPage);
            }
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                results.add(initClasse(resultSet));
            }
            return results;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de récupérer les classes avec les filtres [page : " + page + ", nbElementsPerPage : " + nbElementsPerPage + ", name : " + name);
        }
    }// getClasses()

    /**
     * Initialise une classe à partir d'un resultSet.
     * @param resultSet resultSet
     * @return classe
     * @throws SQLException
     */
    private Classe initClasse(ResultSet resultSet) throws SQLException {
        Timestamp creationDateFromBd = resultSet.getTimestamp("creationDate");
        Timestamp modificationDateFromBd = resultSet.getTimestamp("modificationDate");
        return new Classe(resultSet.getInt("id"),
                resultSet.getString("nom"),
                new java.util.Date(creationDateFromBd.getTime()),
                new java.util.Date(modificationDateFromBd.getTime()));
    }// initClasse()

    /**
     * Vérifie que la classe est utilisée par d'autres plannings.
     * @param id identifiant de la classe
     * @return boolean
     * @throws DataBaseException
     */
    public boolean isUsedByPlannings(int id) throws DataBaseException{
        String requestSql = "SELECT COUNT(c.id) FROM classe c INNER JOIN planning p ON c.id = p.idClasse WHERE c.id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1) > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Erreur technique : Impossible de vérifier que la classe avec l'identifiant " + id + " soit utilisée par d'autres plannings");
        }
    }// isUsedByPlannings()

}// ClasseRepository
