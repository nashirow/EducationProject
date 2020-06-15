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
import com.education.project.model.Matiere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Cette classe gère les échanges avec la base de données concernant la classe Matiere.
 */
@Repository
public class MatiereRepository {

    private Connection connexion;

    public MatiereRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url){
        try {
            Class.forName(driver);
            this.connexion = DriverManager.getConnection(url,user,password);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }//MatiereRepository

    public void closeConnexion(){
        try {
            this.connexion.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Cette fonction injecte une matière en base de données.
     * @param matiere C'est la matière créée.
     * @return Matiere qui a été insérée dans la base de données.
     */
    public Optional<Matiere> insert(Matiere matiere) throws DataBaseException {
        String request = "INSERT INTO matiere (nom,couleurFond,couleurPolice,volumeHoraire,description,creationDate,modificationDate) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connexion.prepareStatement(request,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,matiere.getNom());
            preparedStatement.setString(2,matiere.getCouleurFond());
            preparedStatement.setString(3,matiere.getCouleurPolice());
            preparedStatement.setString(4,matiere.getVolumeHoraire());
            preparedStatement.setString(5,matiere.getDescription());
            preparedStatement.setDate(6,new java.sql.Date(matiere.getCreationDate().getTime()));
            preparedStatement.setDate(7,new java.sql.Date(matiere.getModificationDate().getTime()));
            int nbRowsAdded = preparedStatement.executeUpdate();
            if(nbRowsAdded > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                matiere.setId(resultSet.getInt(1));
                return Optional.of(matiere);
            }
            else{
                throw new DataBaseException("La création de la matière n'a pas pu être réalisée.");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DataBaseException("La création de la matière n'a pas pu être réalisée.");
        }
    }//insert()
}//MatiereRepository
