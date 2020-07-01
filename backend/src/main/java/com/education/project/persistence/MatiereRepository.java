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

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Matiere;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe gère les échanges avec la base de données concernant la classe Matiere.
 */
@Repository
public class MatiereRepository {

    private Connection connexion;

    private static final Logger LOGGER = LogManager.getLogger(MatiereRepository.class);

    public MatiereRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url){
        try {
            Class.forName(driver);
            this.connexion = DriverManager.getConnection(url,user,password);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }//MatiereRepository

    /**
     * Cette fonction injecte une matière en base de données.
     * @param matiere C'est la matière créée.
     * @return Matiere qui a été insérée dans la base de données.
     */
    public Optional<Matiere> insert(Matiere matiere) throws DataBaseException {
        String request = "INSERT INTO matiere (nom,volumeHoraire,description,creationDate,modificationDate) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,matiere.getNom());
            preparedStatement.setString(2,matiere.getVolumeHoraire());
            preparedStatement.setString(3,matiere.getDescription());
            preparedStatement.setTimestamp(4,new java.sql.Timestamp(matiere.getCreationDate().getTime()));
            preparedStatement.setTimestamp(5,new java.sql.Timestamp(matiere.getModificationDate().getTime()));
            int nbRowsAdded = preparedStatement.executeUpdate();
            if(nbRowsAdded > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                matiere.setId(resultSet.getInt(1));
                return Optional.of(matiere);
            }
            else{
                throw new DataBaseException("Erreur technique : la création de la matière n'a pas pu être réalisée.");
            }

        } catch (SQLException e) {
            LOGGER.error("Erreur technique : la création de la matière n'a pas pu être réalisée.", e);
            throw new DataBaseException("Erreur technique : la création de la matière n'a pas pu être réalisée.");
        }
    }//insert()

    /**
     * Cette fonction permet de modifier une matière.
     * @param matiere La matière à modifier.
     * @return La matière modifiée.
     * @throws DataBaseException
     */
    public Optional<Matiere> update(Matiere matiere) throws DataBaseException {
        String request = "UPDATE matiere SET nom = ?, volumeHoraire = ?, description = ?, modificationDate = ? WHERE id = ? ";
        try{
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request);
            preparedStatement.setString(1, matiere.getNom());
            preparedStatement.setString(2, matiere.getVolumeHoraire());
            preparedStatement.setString(3, matiere.getDescription());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(matiere.getModificationDate().getTime()));
            preparedStatement.setInt(5, matiere.getId());
            int nbRowsUpdated = preparedStatement.executeUpdate();
            if(nbRowsUpdated > 0){
                return Optional.of(matiere);
            }
            else{
                throw new DataBaseException("Erreur technique : la modification de la matière n'a pas pu avoir lieu.");
            }
        }
        catch(SQLException e){
            LOGGER.error("Erreur technique : la modification de la matière n'a pas pu avoir lieu.", e);
            throw new DataBaseException("Erreur technique : la modification de la matière n'a pas pu avoir lieu.");
        }
    }//update()

    /**
     * Cette fonction permet de supprimer une matière dont l'identifiant est passé en paramètre.
     * @param id
     * @return
     */
    public boolean deleteMatiere(int id) throws DataBaseException {
        String request = "DELETE FROM matiere WHERE id = ?";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request);
            preparedStatement.setInt(1,id);
            int nbRowsDeleted = preparedStatement.executeUpdate();
            if(nbRowsDeleted > 0){
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de supprimer la matière n° {}", id, e);
            throw new DataBaseException("Erreur technique : impossible de supprimer la matière n° " + id);
        }
        return false;
    }//deleteMatiere()

    /**
     * La fonction récupère une matière en fonction de son identifiant
     * @param id L'identifiant de la matière à récupérer
     * @return la matière
     * @throws DataBaseException
     */
    public Optional<Matiere> findById(Integer id) throws DataBaseException {
        if(id != null){
            try {
                String request = "SELECT * FROM matiere WHERE id = ?";
                PreparedStatement preparedStatement = this.connexion.prepareStatement(request);
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()){
                    int idFromBd = rs.getInt("id");
                    String nomFromBd = rs.getString("nom");
                    String volumeHoraireFromBd = rs.getString("volumeHoraire");
                    String descriptionFromBd = rs.getString("description");
                    Timestamp creationDateFromBd = rs.getTimestamp("creationDate");
                    Timestamp modificationDateFromBd = rs.getTimestamp("modificationDate");
                    Matiere resultat = new Matiere(nomFromBd, volumeHoraireFromBd, descriptionFromBd);
                    resultat.setId(idFromBd);
                    resultat.setCreationDate(new java.util.Date(creationDateFromBd.getTime()));
                    resultat.setModificationDate(new java.util.Date(modificationDateFromBd.getTime()));
                    return Optional.of(resultat);
                }
            }
            catch(SQLException e){
                LOGGER.error("Erreur technique : la récupération de la matière d'identifiant {} est impossible", id, e);
                throw new DataBaseException("Erreur technique : la récupération de la matière d'identifiant " + id + " est impossible");
            }
        }
        return Optional.empty();
    }//findById()

    public boolean isExistByName(String nom) throws DataBaseException {
        String request = "SELECT COUNT(*) FROM matiere WHERE nom = ?";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request);
            preparedStatement.setString(1,nom);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            if(count > 0){
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : il est impossible de vérifier qu'une matière ayant le nom {} existe",nom ,e);
            throw new DataBaseException("Erreur technique : il est impossible de vérifier qu'une matière ayant le nom " + nom + " existe");
        }
        return false;
    }//isExistByName()

    /**
     * Récupère toutes les matières en fonction de filtres
     * @param nom Nom de la matière recherchée (optionnel)
     * @return liste de matières
     */
    public List<Matiere> findAll(String nom) throws DataBaseException {
        List<Matiere> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM matiere ");
        if(nom != null && !nom.isEmpty()){
            sb.append("WHERE nom LIKE ?");
        }
        sb.append(" ORDER BY nom ASC");
        String requestSql = sb.toString();
        try {
            PreparedStatement ps = this.connexion.prepareStatement(requestSql);
            if(nom != null && !nom.isEmpty()){
                ps.setString(1, "%" + nom + "%");
            }
            if(!ps.execute()){
                LOGGER.error("Erreur technique : impossible de récupérer les matières");
                throw new DataBaseException("Erreur technique : impossible de récupérer les matières");
            }
            ResultSet resultSet = ps.getResultSet();
            while(resultSet.next()){
                int idFromBd = resultSet.getInt("id");
                String nomFromBd = resultSet.getString("nom");
                String volumeHoraireFromBd = resultSet.getString("volumeHoraire");
                String descriptionFromBd = resultSet.getString("description");
                Timestamp creationDateFromBd = resultSet.getTimestamp("creationDate");
                Timestamp modificationDateFromBd = resultSet.getTimestamp("modificationDate");
                Matiere resultat = new Matiere(nomFromBd, volumeHoraireFromBd, descriptionFromBd);
                resultat.setId(idFromBd);
                resultat.setCreationDate(new Date(creationDateFromBd.getTime()));
                resultat.setModificationDate(new Date(modificationDateFromBd.getTime()));
                results.add(resultat);
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de récupérer les matières", e);
            throw new DataBaseException("Erreur technique : impossible de récupérer les matières");
        }
        return results;
    }// findAll()
}//MatiereRepository
