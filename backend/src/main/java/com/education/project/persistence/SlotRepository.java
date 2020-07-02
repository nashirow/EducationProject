package com.education.project.persistence;

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * Cette classe permet de gérer les échanges entre la base de données et les slots
 */
@Repository
public class SlotRepository {

    /**
     * C'est l'url de la base de données
     */
    private String url;

    /**
     * C'est le driver de la base de données
     */
    private String driver;

    /**
     * C'est l'identifiant de l'utilisateur de la base de données
     */
    private String user;

    /**
     * C'est le mot de passe de l'utilisateur de la base de données
     */
    private String password;

    /**
     * C'est la connection à la base de données
     */
    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(SlotRepository.class);

    public SlotRepository(@Value("${db.driver}") String driver,@Value("${db.url}") String url,
                          @Value("${db.user}") String user, @Value("${db.password}") String password){
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
    }//SlotRepository()

    /**
     * Cette fonction permet d'insérer un slot en base de données
     * @param slotToInsert Le slot à insérer
     * @return Le slot inséré
     */
    public Optional<Slot> insert(Slot slotToInsert) throws DataBaseException {
        String requestSql = "INSERT INTO slot (comment,creationDate,modificationDate,couleurFond,couleurPolice,idTimeslot,idMatiere,idEnseignant,idSalle) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,slotToInsert.getComment());
            ps.setTimestamp(2,new Timestamp(slotToInsert.getCreationDate().getTime()));
            ps.setTimestamp(3,new Timestamp(slotToInsert.getModificationDate().getTime()));
            ps.setString(4,slotToInsert.getCouleurFond());
            ps.setString(5,slotToInsert.getCouleurPolice());
            ps.setInt(6,slotToInsert.getTimeSlot().getId());
            ps.setInt(7,slotToInsert.getMatiere().getId());
            if(slotToInsert.getEnseignant() != null && slotToInsert.getEnseignant().getId() != null){
                ps.setInt(8,slotToInsert.getEnseignant().getId());
            }else{
                ps.setObject(8,null);
            }
            if(slotToInsert.getSalle() != null && slotToInsert.getSalle().getId() != null){
                ps.setInt(9,slotToInsert.getSalle().getId());
            }else{
                ps.setObject(9,null);
            }
            int rowsAdded = ps.executeUpdate();
            if(rowsAdded > 0 ){
                ResultSet generatedKeys = ps.getGeneratedKeys();
                generatedKeys.next();
                slotToInsert.setId(generatedKeys.getInt(1));
                return Optional.of(slotToInsert);
            }
            else{
                throw new DataBaseException("Erreur technique : ");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de créer le slot {} dans la base de données", slotToInsert.toString(),e);
            throw new DataBaseException("Erreur technique : impossible de créer le slot dans la base de données.");
        }
    }//insert()

    /**
     * Cette fonction permet de mettre à jour un slot en base de données
     * @param slotToUpdate Le slot à mettre à jour en base de données
     * @return le slot mis à jour
     */
    public Optional<Slot> update(Slot slotToUpdate) throws DataBaseException {
        String requestSql = "UPDATE slot SET comment = ?, modificationDate = ?, couleurFond = ?, couleurPolice = ?," +
                " idTimeslot = ?, idMatiere = ?, idEnseignant = ?, idSalle = ? WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1,slotToUpdate.getComment());
            ps.setTimestamp(2,new Timestamp(slotToUpdate.getModificationDate().getTime()));
            ps.setString(3,slotToUpdate.getCouleurFond());
            ps.setString(4,slotToUpdate.getCouleurPolice());
            ps.setInt(5,slotToUpdate.getTimeSlot().getId());
            ps.setInt(6,slotToUpdate.getMatiere().getId());
            if(slotToUpdate.getEnseignant().getId() != null){
                ps.setInt(7,slotToUpdate.getEnseignant().getId());
            }
            else {
                ps.setObject(7,null);
            }
            if(slotToUpdate.getSalle().getId() != null){
                ps.setInt(8,slotToUpdate.getSalle().getId());
            }
            else{
                ps.setObject(8,null);
            }
            ps.setInt(9,slotToUpdate.getId());
            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated > 0){
                return Optional.of(slotToUpdate);
            }else{
                throw new DataBaseException("Erreur technique : impossible de mettre à jour le slot dans la base de données");
            }
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de mettre le slot {} à jour dans la base de données", slotToUpdate.toString(),e);
            throw new DataBaseException("Erreur technique : impossible de mettre à jour le slot dans la base de donnée");
        }
    }//update

    public boolean isExistByColorFond(Slot slotToInsert) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM slot WHERE couleurFond = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setString(1,slotToInsert.getCouleurFond());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong(1) > 0;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de retrouver le slot possédant la couleur de fond{} en base de données ",slotToInsert.getCouleurFond(),e);
            throw new DataBaseException("Erreur technique : impossible de retrouver le slot de couleur de fond " + slotToInsert.getCouleurFond() + " en base de données");
        }
    }//isExistByColorFond()

    /**
     * Cette fonction permet de supprimer un slot de la base de données avec l'identifiant passé en paramètre
     * @param id L'identifiant du slot à supprimer
     * @return boolean
     */
    public boolean deleteSlot(int id) throws DataBaseException {
        String requestSql = "DELETE FROM slot WHERE id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1,id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            LOGGER.error("Erreur technique : impossible de supprimer le slot {} de la base de données", id,e);
            throw new DataBaseException("Erreur technique : impossible de supprimer le slot de la base de données");
        }
    }//deleteSlot()
}//SlotRepository()
