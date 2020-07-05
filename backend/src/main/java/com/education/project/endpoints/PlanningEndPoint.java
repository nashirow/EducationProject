/*
 * Copyright 2020 Hicham AZIMANI, Yassine AZIMANI
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
import com.education.project.model.Planning;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PlanningEndPoint {

    private PlanningService planningService;

    @Autowired
    public PlanningEndPoint(PlanningService planningService) {
        this.planningService = planningService;
    }// PlanningEndPoint()

    /**
     * Cet endpoint permet la création d'un planning dans l'application.
     * @param planning Planning à créer
     * @return Réponse HTTP
     */
    @PostMapping("/planning")
    public ResponseEntity<?> insertPlanning(@RequestBody Planning planning){
        try {
            Optional<Planning> result = this.planningService.insertPlanning(planning);
            return new ResponseEntity<>(new ResponseEndPoint(result, null), HttpStatus.OK);
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// insertPlanning()

    /**
     * Cet endpoint permet la mise à jour d'un planning dans l'application.
     * @param planning Planning à créer
     * @return Réponse HTTP
     */
    @PutMapping("/planning")
    public ResponseEntity<?> updatePlanning(@RequestBody Planning planning){
        try {
            Optional<Planning> result = this.planningService.updatePlanning(planning);
            return new ResponseEntity<>(new ResponseEndPoint(result, null), HttpStatus.OK);
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// updatePlanning()

}// PlanningEndPoint
