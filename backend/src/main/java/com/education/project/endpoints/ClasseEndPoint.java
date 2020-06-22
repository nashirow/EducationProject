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

import java.util.Optional;

@RestController
public class ClasseEndPoint {

    private ClasseService classeService;

    @Autowired
    public ClasseEndPoint(ClasseService classeService){
        this.classeService = classeService;
    }// ClasseEndPoint()

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

}// ClasseEndPoint
