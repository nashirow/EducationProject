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
     * Récupère l'ensemble des créneaux horaires de l'application
     * @param page n° de la page (facultatif)
     * @param nbElementsPerPage Nombre de créneaux horaires par page (facultatif)
     * @return Réponse HTTP
     */
    @GetMapping("/timeslots")
    public ResponseEntity<?> getTimeSlots(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "nbElementsPerPage", required = false) Integer nbElementsPerPage){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.timeSlotService.getTimeSlots(page, nbElementsPerPage), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// getTimeSlots()

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
