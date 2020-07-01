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
import com.education.project.model.Salle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe permet de traiter les échanges liés aux salles dans la base de données
 */
@Repository
public class SalleRepository{

    /**
     * SQL driver
     */
    private String driver;
    /**
     * Url de la base de donnée
     */
    private String url;

    /**
     * Identifiant du compte utilisateur BD
     */
    private String user;

    /**
     * Mot de passe du compte utilisateur BD
     */
    private String password;

    /**
     * Connexion à la base de donnée
     */
    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(SalleRepository.class);

    public SalleRepository(@Value("${db.driver}") String driver,@Value("${db.url}") String url,@Value("${db.user}") String user,
                           @Value("${db.password}") String password){
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            Class.forName(this.driver);
            this.connection = DriverManager.getConnection(this.url,this.user,this.password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }//SalleRepository()

    /**
     * Cette fonction permet de retrouver une salle grâce au nom passé en paramètre
     * @param nom Nom de la salle à chercher
     * @return boolean
     */
    public boolean isExistByName(String nom) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM salle WHERE nom = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1,nom);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long countResult = resultSet.getLong(1);
            return countResult > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(),e);
            throw new DataBaseException("Erreur technique : impossible de retrouver la salle " + nom + " en base de données");
        }
    }//isExistByName()

    /**
     * Cette fonction permet d'injecter une salle en base de données
     * @param salleToInsert Salle à injecter en base de données
     * @return Salle créee
     */
    public Optional<Salle> insert(Salle salleToInsert) throws DataBaseException {
        String requestSql = "INSERT INTO salle (nom,creationDate,modificationDate) VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,salleToInsert.getNom());
            ps.setTimestamp(2,new Timestamp(salleToInsert.getCreationDate().getTime()));
            ps.setTimestamp(3,new Timestamp(salleToInsert.getModificationDate().getTime()));
            int rowsAdded = ps.executeUpdate();
            if(rowsAdded> 0 ){
                ResultSet generatedKeys = ps.getGeneratedKeys();
                generatedKeys.next();
                salleToInsert.setId(generatedKeys.getInt(1));
                return Optional.of(salleToInsert);
            }
            else
            {
                throw new DataBaseException("Erreur technique : impossible d'insérer la salle");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible d'insérer la salle " + salleToInsert.toString() + " en base de données",e);
            throw new DataBaseException("Erreur technique : impossible d'insérer la salle en base de données");
        }
    }//insert()

    /**
     * Cette fonction permet de mettre à jour une salle en base de données
     * @param salleToUpdate La salle à mettre à jour
     * @return La salle mise à jour
     * @throws DataBaseException
     */
    public Optional<Salle> update(Salle salleToUpdate) throws DataBaseException {
        String requestSql = "UPDATE salle SET nom = ?, modificationDate = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1,salleToUpdate.getNom());
            ps.setTimestamp(2, new Timestamp(salleToUpdate.getModificationDate().getTime()));
            ps.setInt(3,salleToUpdate.getId());
            int RowsUpdated = ps.executeUpdate();
            if(RowsUpdated > 0){
                return Optional.of(salleToUpdate);
            }
            else
            {
                throw new DataBaseException("Erreur technique : impossible de mettre à jour la salle en base de données.");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur techinque : impossible de mettre à jour la salle " + salleToUpdate.toString() + " en base de données",e);
            throw new DataBaseException("Erreur technique : impossible de mettre la salle à jour en base de données");
        }
    }//update()

    /**
     * Cette fonction permet de récupérer les salles en base de données en fonction des paramètres passés (facultatif)
     * @param nom Nom de la salle à récupérer (facultatif)
     * @param page Nombre de page (facultatif)
     * @param nbElementsPerPage Nombre de salles par page (facultatif)
     * @return Les salles récupérées
     */
    public List<Salle> getSalles(String nom, Integer page, Integer nbElementsPerPage) throws DataBaseException {
        List<Salle> resultSalles = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM salle ");
        int indiceNom = 0;
        if(nom != null && !nom.isEmpty()){
            sb.append("WHERE nom LIKE ? ");
            indiceNom = 1;
        }
        if(page != null && nbElementsPerPage != null){
            sb.append("LIMIT ? OFFSET ?");
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(nom != null && !nom.isEmpty()){
                ps.setString(indiceNom, "%" + nom + "%");
            }
            if(page != null && nbElementsPerPage != null){
                ps.setInt(indiceNom+1,nbElementsPerPage);
                ps.setInt(indiceNom+2,(page-1) * nbElementsPerPage);
            }
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Salle salle = new Salle();
                salle.setId(resultSet.getInt("id"));
                salle.setNom(resultSet.getString("nom"));
                salle.setCreationDate(resultSet.getTimestamp("creationDate"));
                salle.setModificationDate(resultSet.getTimestamp("modificationDate"));
                resultSalles.add(salle);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de récuperer la salle " + nom + " dans la base de données");
            throw new DataBaseException("Erreur technique : impossible de récuperer la salle " + nom + " dans la base de données");
        }
        return resultSalles;
    }//getClasses()

    /**
     * Cette fonction permet de compter le nombre de salles en base de données avec les informations passées en paramètre (facultatif)
     * @param nom Nom de la salle à compter (facultatif)
     * @return Le nombre de salle en base de données
     * @throws DataBaseException
     */
    public long countByName(String nom) throws DataBaseException {
        StringBuilder sb = new StringBuilder("SELECT COUNT(id) FROM salle ");
        int indiceNom = 0;
        if(nom != null && !nom.isEmpty()){
            sb.append("WHERE nom LIKE ?");
            indiceNom = 1;
        }
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            if(nom != null && !nom.isEmpty()){
                ps.setString(indiceNom,"%" + nom + "%");
            }
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de compter le nombre de salles avec nom : " + ((nom != null) ?  nom : "") + " en base de données",e);
            throw new DataBaseException("Erreur technique : impossible de compter le nombre de salles en base de données");
        }
    }//countByName()

    /**
     * Cette fonction permet de supprimer une salle dont l'identifiant est passé en paramètre dans la base de données
     * @param id Identifiant de la salle à supprimer
     * @return boolean
     */
    public boolean delete(int id) throws DataBaseException {
        String requestSql = "DELETE FROM salle WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1,id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de supprimer la salle " + id + "de la base de données",e);
            throw new DataBaseException("Erreur technique : impossible de supprimer la salle " + id + " de la base de données");
        }
    }//delete()

    /**
     * Cette fonction permet de récupérer une salle avec l'identifiant passé en paramètre
     * @param id Identifiant de la salle passé en paramètre
     * @return La salle récupérée
     */
    public Optional<Salle> findById(int id) throws DataBaseException {
        String requeteSql = "SELECT * FROM salle WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requeteSql);
            ps.setInt(1,id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                Salle salle = new Salle();
                salle.setNom(resultSet.getString("nom"));
                salle.setCreationDate(resultSet.getTimestamp("creationDate"));
                salle.setModificationDate(resultSet.getTimestamp("modificationDate"));
                salle.setId(resultSet.getInt("id"));
                return Optional.of(salle);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de retrouver la salle d'identifiant " + id + " dans la base de données");
            throw new DataBaseException("Erreur technique : impossible de retrouver la salle d'identifiant " + id + " dans la base de données");
        }
        return Optional.empty();
    }//findById()
}//SalleRepository
