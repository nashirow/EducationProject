package com.education.project.endpoints;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.ResponseEndPoint;
import com.education.project.model.Slot;
import com.education.project.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SlotEndPoint {

    private SlotService slotService;

    @Autowired
    public SlotEndPoint(SlotService slotService) {
        this.slotService = slotService;
    }//SlotEndPoint()

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
}//SlotEndPoint
