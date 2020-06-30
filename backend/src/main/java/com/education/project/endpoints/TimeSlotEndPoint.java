package com.education.project.endpoints;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.ResponseEndPoint;
import com.education.project.model.TimeSlot;
import com.education.project.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les créneaux horaires de l'application.
 */
@RestController
public class TimeSlotEndPoint {

    private TimeSlotService timeSlotService;

    @Autowired
    public TimeSlotEndPoint(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }// TimeSlotEndPoint()

    /**
     * Insère un créneau horaire dans l'application.
     * @param timeSlot Créneau horaire à insérer
     * @return Réponse HTTP
     */
    @PostMapping("/timeslot")
    public ResponseEntity<?> insertTimeSlot(@RequestBody TimeSlot timeSlot){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.timeSlotService.insert(timeSlot), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        }
    }//insertTimeSlot()

    /**
     * Supprime un créneau horaire dans l'application.
     * @param id Identifiant du créneau horaire à supprimer
     * @return Réponse HTTP
     */
    @DeleteMapping("/timeslot/{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable("id") Integer id){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.timeSlotService.delete(id), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//deleteTimeSlot()

    /**
     * Compte le nombre total de créneaux horaires dont dispose l'application.
     * @return Réponse HTTP
     */
    @GetMapping("/timeslot/count")
    public ResponseEntity<?> countTimeSlots(){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.timeSlotService.count(), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//countTimeSlots()

}// TimeSlotEndPoint
