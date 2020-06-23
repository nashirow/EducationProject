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
import com.education.project.model.Enseignant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;


/**
 * Cette classe permet de traiter les échanges avec la base de données en lien avec la classe enseignant.
 */
@Repository
public class EnseignantRepository {

    /**
     * Driver SQL
     */
    private String driver;

    /**
     * Url de la base de donnée
     */
    private String url;

    /**
     * mot de passe du compte utilisateur (BD)
     */
    private String password;

    /**
     * Compte utilisateur (BD)
     */
    private String user;

    /**
     * Connexion à la base de données
     */
    private Connection connexion;

    private static final Logger LOGGER = LogManager.getLogger(EnseignantRepository.class);
    public EnseignantRepository (@Value("${db.driver}") String driver, @Value("${db.url}") String url,
                                 @Value("${db.password}") String password, @Value("${db.user}") String user){
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.user = user;

        try {
            Class.forName(this.driver);
            this.connexion = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }//EnseignantRepository()


    /**
     * Cette fonction permet d'injecter un enseignant en base de données
     * @param enseignant C'est l'enseignant créée
     * @return L'enseignant qui a été créée dans la base de données
     * @throws
     */
    public Optional<Enseignant> insert(Enseignant enseignant) throws DataBaseException {
        String request = "INSERT INTO enseignant (nom,prenom,creationDate,modificationDate) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, enseignant.getNom());
            preparedStatement.setString(2, enseignant.getPrenom());
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(enseignant.getCreationDate().getTime()));
            preparedStatement.setTimestamp(4,new java.sql.Timestamp(enseignant.getModificationDate().getTime()));
            int nbRowsAdded = preparedStatement.executeUpdate();
            if(nbRowsAdded > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                enseignant.setId(resultSet.getInt(1));
                return Optional.of(enseignant);
            }
            else
            {
                throw new DataBaseException("Erreur technique : la création de l'enseignant n'a pu être réalisée");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : la création de l'enseignant n'a pas pu être réalisée.",e);
            throw new DataBaseException("Erreur technique : la création de l'enseignant n'a pu être réalisée");
        }
    }//insert()

    /**
     *Cette fonction permet de savoir si un enseignant avec
     * le nom passé en paramètre existe en base de données.
     * @param nom Nom de l'enseignant à trouver
     * @param prenom Prenom de l'enseignant à trouver
     * @return boolean
     */
    public boolean isExistByName(String nom, String prenom) throws DataBaseException {
        String request = "SELECT COUNT(id) FROM enseignant WHERE nom = ? AND prenom = ?";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request);
            preparedStatement.setString(1,nom);
            preparedStatement.setString(2,prenom);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            long countName = rs.getLong(1);
            if(countName > 0){
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : il est impossible de vérifier qu'un enseignant ayant le nom {} et le prenom {} existe",nom,prenom,e);
            throw new DataBaseException("Erreur technique : il est impossible de vérifier qu'un enseignant ayant le nom " + nom + " et prenom " + prenom + " existe");
        }
        return false;
    }//isExistByName()
}//EnseignantRepository
