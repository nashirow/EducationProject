package com.education.project.endpoints;

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Jour;
import com.education.project.model.ResponseEndPoint;
import com.education.project.services.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cette classe contient les endpoints permettant de traiter les requêtes liées aux jours
 */
@RestController
public class DayEndPoint {
    private DayService dayService;

    @Autowired
    public DayEndPoint(DayService dayService) {
        this.dayService = dayService;
    }//DayEndPoint

    /**
     * Ce endpoint permet de récupérer les jours de l'application
     * @return Réponse HTTP
     */
    @GetMapping("/jours")
    public ResponseEntity<?> getDays(){
        try {
            List<Jour> resultJours = dayService.getDays();
            return new ResponseEntity<>(new ResponseEndPoint(resultJours,null), HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//getDays()
}//DayEndPoint
