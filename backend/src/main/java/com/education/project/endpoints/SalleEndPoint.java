package com.education.project.endpoints;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.ResponseEndPoint;
import com.education.project.model.Salle;
import com.education.project.services.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    }
}//SalleEndPoint
