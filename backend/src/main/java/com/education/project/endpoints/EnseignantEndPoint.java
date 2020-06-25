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
import com.education.project.model.Enseignant;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EnseignantEndPoint {

    private EnseignantService enseignantService;

    @Autowired
    public EnseignantEndPoint(EnseignantService enseignantService){
        this.enseignantService = enseignantService;
    }//EnseignantEndPoint

    /**
     * Ce endpoint permet de créer un enseignant
     * @param enseignant L'enseignant à créer
     * @return Réponse HTTP
     */
    @PostMapping("/enseignant")
    public ResponseEntity<?> insertEnseignant(@RequestBody Enseignant enseignant){
        Enseignant result = null;
        try {
            Optional<Enseignant> optEnseignant = this.enseignantService.insertEnseignant(enseignant);
            if(optEnseignant.isPresent()){
                result = optEnseignant.get();
            }
        } catch (ArgumentException e) {
            ResponseEndPoint reponse = new ResponseEndPoint(null,e.getErreurs());
            return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null), HttpStatus.OK);
    }//insertEnseignant()

    /**
     * Ce endpoint permet de mettre à jour un enseignant
     * @param enseignant L'enseignant à mettre à jour
     * @return Réponse HTTP
     */
    @PutMapping("/enseignant")
    public ResponseEntity<?> updateEnseignant(@RequestBody Enseignant enseignant){
        try {
            Optional<Enseignant> optEnseignantToUpdate = enseignantService.updateEnseignant(enseignant);
            if(optEnseignantToUpdate.isPresent()){
                return new ResponseEntity<>(new ResponseEndPoint(optEnseignantToUpdate.get(),null),HttpStatus.OK);
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()),HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(null,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }//updateEnseignant()
}

