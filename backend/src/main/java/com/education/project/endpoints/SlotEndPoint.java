package com.education.project.endpoints;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.ResponseEndPoint;
import com.education.project.model.Slot;
import com.education.project.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class SlotEndPoint {

    private SlotService slotService;

    @Autowired
    public SlotEndPoint(SlotService slotService) {
        this.slotService = slotService;
    }//SlotEndPoint()

    /**
     * Ce endpoint permet d'insérer un slot en base de données
     * @param slotToInsert Le slot à insérer
     * @return Réponse HTTP
     */
    @PostMapping("/slot")
    public ResponseEntity<?> insertSlot(@RequestBody Slot slotToInsert){
        Slot result = null;
        try {
            Optional<Slot> optSlot = slotService.insertSlot(slotToInsert);
            if(optSlot.isPresent()){
                    result = optSlot.get();
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()),HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(result,null), HttpStatus.OK);
    }//insertSlot()

    /**
     * Ce endpoint permet de récupérer un slot en base de données grâce à l'identifiant passé en paramètre
     * @param id L'identifiant du slot à récupérer
     * @return Réponse HTTP
     */
    @GetMapping("/slot/{id}")
    public ResponseEntity<?> getSlot(@PathVariable Integer id){
        try {
            Optional<Slot> optSlot = slotService.findById(id);
            return new ResponseEntity<>(new ResponseEndPoint(optSlot.orElseGet( () -> new Slot()),null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//getSlot()

    /**
     * Ce endpoint permet de mettre à jour un slot en base de données
     * @param slotToInsert Le slot à mettre à jour
     * @return Réponse HTTP
     */
    @PutMapping("/slot")
    public ResponseEntity<?> updateSlot(@RequestBody Slot slotToInsert){
        try {
            Optional<Slot> optSlot = slotService.updateSlot(slotToInsert);
            if(optSlot.isPresent()){
                return new ResponseEntity<>(new ResponseEndPoint(optSlot.get(),null),HttpStatus.OK);
            }
        } catch (ArgumentException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getErreurs()),HttpStatus.BAD_REQUEST);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseEndPoint(null,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }//updateSlot()

    /**
     * Ce endpoint permet de supprimer un slot de la base de données
     * @param id L'identifiant du slot
     * @return Réponse HTTP
     */
    @DeleteMapping("/slot/{id}")
    public ResponseEntity<?> deleteSlot(@PathVariable Integer id){
        try {
            boolean result = slotService.deleteSlot(id);
            return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//deleteSlot()

    /**
     * Ce endpoint permet de compter le nombre de slots en base de données à l'aide de paramètres (facultatifs)
     * @param params Les paramètres (facultatifs : nom/prenom enseignant, nom d'une matière/salle, début/fin d'un horaire, couleur de fond/police d'un slot)
     * La liste des clefs utilisés pour les paramètres
     * enseignantNom : c'est le nom de l'enseignant
     * enseignantPrenom : c'est le prenom de l'enseignant
     * matiereNom : c'est le nom de la matière
     * startHour : c'est l'heure de début du créneau horaire
     * endHour : c'est l'heure de fin du créneau horaire
     * couleurFond : c'est la couleur de fond du slot
     * couleurPolice : c'est la couleur de police du slot
     * salleNom : c'est le nom de la salle
     * @return Réponse HTTP
     */
    @GetMapping("/count/slots")
    public ResponseEntity<?> countSlots(@RequestParam Map<String,String> params){
        try {
            long result = slotService.countSlots(params);
            return new ResponseEntity<>(new ResponseEndPoint(result,null),HttpStatus.OK);
        } catch (DataBaseException e) {
            return new ResponseEntity<>(new ResponseEndPoint(null,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//countSlots()
}//SlotEndPoint
