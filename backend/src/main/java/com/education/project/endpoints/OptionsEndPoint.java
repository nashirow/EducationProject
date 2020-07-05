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
import com.education.project.model.Options;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;


@RestController
public class OptionsEndPoint {

    private OptionsService optionsService;

    @Autowired
    public OptionsEndPoint(OptionsService optionsService) {
        this.optionsService = optionsService;
    }// OptionsEndPoint()

    /**
     * Cet endpoint modifie les options générales de l'application
     * @return Réponse HTTP
     */
    @PutMapping("/options")
    public ResponseEntity<?> changeOptions(@RequestBody Options options){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.optionsService.changeOptions(options), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        }
    }// getOptions()

    /**
     * Cet endpoint retourne les options générales de l'application
     * @return Réponse HTTP
     */
    @GetMapping("/options")
    public ResponseEntity<?> getOptions(){
        try {
            return new ResponseEntity<>(new ResponseEndPoint(this.optionsService.getOptions(), null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// getOptions()

    /**
     * Cet endpoint retourne les valeurs de découpage de l'emploi du temps
     * qui peuvent être utilisées.
     * @return Réponse HTTP
     */
    @GetMapping("/options/choicesSplitTime")
    public ResponseEntity<?> getChoicesForSplitTime(){
        return new ResponseEntity<>(new ResponseEndPoint(this.optionsService.getValuesAcceptedForSplitValue(), null), HttpStatus.OK);
    }// getChoicesForSplitTime()

}// OptionsEndPoint
