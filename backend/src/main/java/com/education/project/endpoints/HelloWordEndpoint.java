package com.education.project.endpoints;

import com.education.project.model.Personne;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Création d'une classe contrôleur Spring.
 * Chaque page internet doit avoir un contrôleur (pour ajouter,supprimer ou modifier une matière).
 */
@RestController //Rest est un protocol respectant les appels webs/communication web. (GET,POST,PUT,DELETE)
@RequestMapping("/hello")
public class HelloWordEndpoint {

    @GetMapping("/test")
    public String hello(@RequestParam(value = "nom") String nom,@RequestParam("prenom") String prenom){
        return "hello " + nom + " " + prenom;
    }//hello()

    @GetMapping("/projet")
    public String projet(){
        return "EducationProjet v1.0";
    }//projet()

    @GetMapping("/authors")
    public String author(){
        return "Authors : Azimani Yassine & Azimani Hicham";
    }//author()

    @GetMapping("/json") //les endpoints sont toujours en minuscules
    public ResponseEntity<?> getJson(){
        List<Personne> personnes = new ArrayList<>();
        personnes.add(new Personne("Azimani", "Hicham", 26));
        personnes.add(new Personne("Azimani", "Yassine", 29));
        return new ResponseEntity<>(personnes, HttpStatus.OK); //renvoie une classe contenant ses information (ici String) + HttpStatus (renvoie un code HTTP).
    }

    @GetMapping("/personne")
    public ResponseEntity<?> getPersonne(@RequestParam("nom") String nom){
        Personne p1 = new Personne(nom,"Jack", 50);
        return new ResponseEntity<>(p1, HttpStatus.OK);
    }

}//HelloWordEndpoint
