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
import com.education.project.model.Salle;
import com.education.project.services.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class SalleEndPoint {

    private SalleService salleService;

    @Autowired
    public SalleEndPoint(SalleService salleService){
        this.salleService = salleService;
    }//SalleEndPoint

    /**
     * Ce endpoint permet de créer une salle
     * @param salle La salle à créer
     * @return  Réponse HTTP
     */
    @PostMapping("/salle")
    public ResponseEntity<?> insertSalle(@RequestBody Salle salle){
        Salle result = null;
        try {
            Optional<Salle> optSalle = salleService.insertSalle(salle);
            if(optSalle.isPresent()){
                result = optSalle.get();
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()), HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
    }//insertSalle()

    /**
     * Ce endpoint permet de mettre à jour une salle
     * @param salle La salle à mettre à jour
     * @return Réponse HTTP
     */
    @PutMapping("/salle")
    public ResponseEntity<?> updateSalle(@RequestBody Salle salle){
        try {
            Optional<Salle> optSalle = salleService.updateSalle(salle);
            if(optSalle.isPresent()){
                return new ResponseEntity<>(new ResponseEndPoint(optSalle.get(),null), HttpStatus.OK);
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()),HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(null,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }//updateSalle()

    @GetMapping("/salles")
    public ResponseEntity<?> getSalles(@RequestParam (value = "nom", required = false) String nom,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "nbElementsPerPage",required = false) Integer nbElementsPerPage){
        try {
            List<Salle> resultSalles = salleService.getSalles(nom,page,nbElementsPerPage);
            return new ResponseEntity<>(new ResponseEndPoint(resultSalles,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//getSalles()

    @GetMapping("/count/salles")
    public ResponseEntity<?> countSalles(@RequestParam (value = "nom", required = false) String nom){
        try {
            long resultCount = salleService.countSalles(nom);
            return new ResponseEntity<>(new ResponseEndPoint(resultCount,null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//countSalles()

    @DeleteMapping("/salle/{id}")
    public ResponseEntity<?> deleteSalle(@PathVariable Integer id){
        try {
            boolean result = salleService.deleteSalle(id);
            return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//deleteSalle()

    @GetMapping("/salle/{id}")
    public ResponseEntity<?> getSalle(@PathVariable Integer id){
        try {
            Optional<Salle> optSalle = salleService.getSalle(id);
            Salle salle = optSalle.orElseGet(() -> new Salle());
            return new ResponseEntity<>(new ResponseEndPoint(salle,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//getSalle()
}//SalleEndPoint
