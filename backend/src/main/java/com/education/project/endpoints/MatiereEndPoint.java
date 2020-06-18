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
import com.education.project.model.Matiere;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.MatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MatiereEndPoint {

    private MatiereService matiereService;

    @Autowired
    public MatiereEndPoint(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    /**
     * Ce endpoint permet de créer une matière.
     * @param matiere La matière à créer.
     * @return Réponse HTTP.
     */
    @PostMapping("/matiere")
    public ResponseEntity<?> insertMatiere(@RequestBody Matiere matiere){
        Matiere result = null;
        try {
            Optional<Matiere> optMatiere = this.matiereService.insertMatiere(matiere);
            if(optMatiere.isPresent()){
                result = optMatiere.get();
            }
        } catch (ArgumentException e) {
            e.printStackTrace();
            ResponseEndPoint response = new ResponseEndPoint(null,e.getErreurs());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null), HttpStatus.OK);
    }//insertMatiere()

    /**
     * Ce endpoint permet de modifier une matière.
     * @param matiere La matière à modifier.
     * @return Réponse HTTP.
     */
    @PutMapping("/matiere")
    public ResponseEntity<?> updateMatiere(@RequestBody Matiere matiere){
        Matiere result = null;
        try {
            Optional<Matiere> optMatiereUpdated = this.matiereService.updateMatiere(matiere);
            if(optMatiereUpdated.isPresent()){
                result = optMatiereUpdated.get();
            }
        } catch (ArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()),HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null), HttpStatus.OK);
    }//updateMatiere()

    /**
     * Le endpoint permet de supprimer une matière
     * @param id L'identifiant de la matière à supprimer
     * @return Réponse HTTP
     */
    @DeleteMapping("/matiere/{id}")
    public ResponseEntity<?> deleteMatiere(@PathVariable int id){
        boolean result = false;
        try {
            result = this.matiereService.deleteMatiere(id);
        } catch (DataBaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
    }//deleteMatiere()

    /**
     * Ce endpoint permet de récupérer une matière grâce à son identifiant.
     * @param id Identifiant de la matière à récupérer.
     * @return Réponse HTTP.
     */
    @GetMapping("/matiere/{id}")
    public ResponseEntity<?> getMatiere(@PathVariable int id){
        Matiere result = null;
        try {
            Optional<Matiere> optMatiereRecovered = this.matiereService.getMatiere(id);
            if(optMatiereRecovered.isPresent()){
                result = optMatiereRecovered.get();
            }
        } catch (DataBaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null), HttpStatus.OK);
    }//getMatiere()

}//MatiereEndPoint
