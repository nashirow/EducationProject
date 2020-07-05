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
import com.education.project.model.Classe;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClasseEndPoint {

    private ClasseService classeService;

    @Autowired
    public ClasseEndPoint(ClasseService classeService){
        this.classeService = classeService;
    }// ClasseEndPoint()

    /**
     * Cet endpoint insère une classe
     * @param classe Classe à insérer
     * @return Réponse HTTP
     */
    @PostMapping("/classe")
    public ResponseEntity<?> insertClasse(@RequestBody Classe classe){
        try {
            Optional<Classe> optClasse = classeService.insertClasse(classe);
            if(optClasse.isPresent()){
                return new ResponseEntity<>(new ResponseEndPoint(optClasse.get(), null), HttpStatus.OK);
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(null, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }// insertClasse()

    /**
     * Cet endpoint supprime la classe dont l'identifiant est passé en paramètre
     * @param id Identifiant de la classe à supprimer
     * @return Réponse HTTP
     */
    @DeleteMapping("/classe/{id}")
    public ResponseEntity<?> deleteClasse(@PathVariable("id") Integer id){
        try {
            boolean result = classeService.deleteClass(id);
            return new ResponseEntity<>(new ResponseEndPoint(result, null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// deleteClasse()

    /**
     * Cet endpoint récupère toutes les informations d'une classe
     * @param id Identifiant de la classe pour laquelle on doit récupérer les informations
     * @return Réponse HTTP
     */
    @GetMapping("/classe/{id}")
    public ResponseEntity<?> getClasse(@PathVariable Integer id){
        try {
            Optional<Classe> optResult = classeService.getClasse(id);
            Classe result = optResult.orElseGet(() -> new Classe());
            return new ResponseEntity<>(new ResponseEndPoint(result, null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// getClasse()

    /**
     * Cet endpoint met à jour la classe passée en paramètre
     * @param classe Classe à mettre à jour
     * @return Réponse HTTP
     */
    @PutMapping("/classe")
    public ResponseEntity<?> updateClasse(@RequestBody Classe classe) {
        try {
            Optional<Classe> optClasse = classeService.updateClasse(classe);
            if(optClasse.isPresent()){
                return new ResponseEntity<>(new ResponseEndPoint(optClasse.get(), null), HttpStatus.OK);
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getErreurs()), HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(null, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }// updateClasse()

    /**
     * Cet endpoint retourne le nombre de classes en fonction de filtres.
     * @param name Nom de la classe
     * @return Réponse HTTP
     */
    @GetMapping("/count/classes")
    public ResponseEntity<?> countClasses(@RequestParam(value = "name", required = false) String name){
        try {
            long count = classeService.getCount(name);
            return new ResponseEntity<>(new ResponseEndPoint(count, null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// countClasses()

    /**
     * Cet endpoint retourne un ensemble de classes en fonction de filtres
     * @param name Nom de la classe / Sous-chaîne à rechercher (facultatif)
     * @param page N° de la page (facultatif)
     * @param nbElementsPerPage Nombre d'éléments par page (facultatif)
     * @return Réponse HTTP
     */
    @GetMapping("/classes")
    public ResponseEntity<?> getClasses(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "nbElementsPerPage", required = false) Integer nbElementsPerPage){
        try {
            List<Classe> classes = classeService.getClasses(page, nbElementsPerPage, name);
            return new ResponseEntity<>(new ResponseEndPoint(classes, null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// getClasses()
}// ClasseEndPoint
