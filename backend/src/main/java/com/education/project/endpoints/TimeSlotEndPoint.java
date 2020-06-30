package com.education.project.endpoints;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.ResponseEndPoint;
import com.education.project.model.TimeSlot;
import com.education.project.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * Insert un créneau horaire dans l'application.
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

}// TimeSlotEndPoint
