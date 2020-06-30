package com.education.project.persistence;

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.TimeSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class TimeSlotRepository {

    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(TimeSlotRepository.class);

    public TimeSlotRepository(@Value("${db.user}") String user, @Value("${db.password}") String password, @Value("${db.driver}") String driver, @Value("${db.url}") String url){
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }// TimeSlotRepository()

    /**
     * Insère un créneau horaire en base de données.
     * @param ts Créneau horaire
     * @return Créneau horaire ajouté en base de données
     * @throws DataBaseException
     */
    public Optional<TimeSlot> insert(TimeSlot ts) throws DataBaseException {
        String requestSql = "INSERT INTO timeslot (startHour, endHour) VALUES (?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql, Statement.RETURN_GENERATED_KEYS);
            ps.setTime(1, Time.valueOf(ts.getStart()));
            ps.setTime(2, Time.valueOf(ts.getEnd()));
            int nbRowsAdded = ps.executeUpdate();
            if(nbRowsAdded > 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.next();
                ts.setId(resultSet.getInt(1));
                return Optional.of(ts);
            }else{
                throw new DataBaseException("Impossible de créer un nouveau créneau horaire : " + ts.toString());
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de créer un nouveau créneau horaire : " + ts.toString());
        }
    }// insert()

    /**
     * Vérifie si le créneau horaire passé en paramètre existe en base de données.
     * @param ts Créneau horaire à vérifier
     * @return boolean
     */
    public boolean exists(TimeSlot ts) throws DataBaseException {
        String requestSql = "SELECT COUNT(id) FROM timeslot WHERE startHour = ? AND endHour = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setTime(1, Time.valueOf(ts.getStart()));
            ps.setTime(2, Time.valueOf(ts.getEnd()));
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            return count > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de vérifier si le créneau horaire " + ts.toString() + " existe en base de données");
        }
    }// exists()

    /**
     * Supprime un créneau horaire dans la base de données.
     * @param id Identifiant du créneau horaire à supprimer
     * @return boolean
     * @throws DataBaseException
     */
    public boolean delete(int id) throws DataBaseException {
        String requestSql = "DELETE FROM timeslot WHERE id = ?";
        try{
            PreparedStatement ps = this.connection.prepareStatement(requestSql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }catch(SQLException e){
            LOGGER.error(e.getMessage(), e);
            throw new DataBaseException("Impossible de supprimer le créneau horaire n°" + id);
        }
    }// delete()
}// TimeSlotRepository
