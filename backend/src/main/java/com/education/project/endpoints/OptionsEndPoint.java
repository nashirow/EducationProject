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
            return new ResponseEntity<>(new ResponseEndPoint(null, e.getMessage()), HttpStatus.BAD_REQUEST);
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

}// OptionsEndPoint
