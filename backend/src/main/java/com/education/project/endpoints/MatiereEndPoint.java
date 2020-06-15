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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MatiereEndPoint {

    private MatiereService matiereService;

    @Autowired
    public MatiereEndPoint(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    /**
     *
     * @param matiere
     * @return
     */
    @PostMapping("/matiere")
    public ResponseEntity<?> createMatiere(@RequestBody Matiere matiere){
        Matiere resultat = null;
        try {
            Optional<Matiere> optMatiere = this.matiereService.createMatiere(matiere);
            if(optMatiere.isPresent()){
                resultat = optMatiere.get();
            }
        } catch (ArgumentException e) {
            e.printStackTrace();
            ResponseEndPoint response = new ResponseEndPoint(null,e.getErreurs());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resultat, HttpStatus.OK);
    }//createMatiere()

}//MatiereEndPoint
