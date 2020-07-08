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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    /**
     * Ce endpoint permet de supprimer un planning en base de données grace à l'identifiant passée en paramètre
     * @param id identifiant du planning à supprimer
     * @return Réponse HTTP
     */
    @DeleteMapping("/planning/{id}")
    public ResponseEntity<?> deletePlanning(@PathVariable("id") Integer id){
        try {
            boolean result = planningService.deletePlanning(id);
            return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//deletePlanning()

    /**
     * Cet endpoint permet de récupérer toutes les informations d'un planning
     * en fonction de son identifiant.
     * @param id Identifiant du planning
     * @return planning
     */
    @GetMapping("/planning/{id}")
    public ResponseEntity<?> getPlanningById(@PathVariable("id") int id){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.planningService.getPlanningById(id), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// getPlanningById()

    /**
     * Ce endpoint permet de récupérer tous les plannings de l'application en fonction des paramètres passés
     * @param params paramètres d'un planning
     * classeNom : Nom de la classe d'un planning (facultatif)
     * page : N° de la page (facultatif)
     * nbElementsPerPage : Nombre d'éléments par page (facultatif)
     * @return Réponse HTTP
     */
    @GetMapping("/plannings")
    public ResponseEntity<?> getPlannings(@RequestParam Map<String,String> params){
        try {
            List<Planning> resultPlannings = planningService.getPlannings(params);
            return new ResponseEntity<>(new ResponseEndPoint(resultPlannings,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//getPlannings()

    /**
     * Ce endpoint permet de générer un planning à partir de son identifiant.
     * @param id Identifiant du planning à générer
     * @return Réponse HTTP
     */
    @GetMapping("/planning/generate/{id}")
    public ResponseEntity<?> generatePlanning(@PathVariable("id") Integer id){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(planningService.generatePlanning(id),null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// generatePlanning()

}// PlanningEndPoint
