package com.education.project.persistence;

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Salle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
}//SalleRepository
