package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.TimeSlot;
import com.education.project.persistence.OptionsRepository;
import com.education.project.persistence.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service gérant la gestion des créneaux horaires pour slots.
 */
@Service
public class TimeSlotService {

    private TimeSlotRepository timeSlotRepository;

    private OptionsRepository optionsRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository, OptionsRepository optionsRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.optionsRepository = optionsRepository;
    }// TimeSlotService()

    /**
     * Crée un créneau horaire dans l'application
     * @param ts créneau horaire à ajouter
     * @return Créneau horaire ajouté
     * @throws DataBaseException
     * @throws ArgumentException
     */
    public Optional<TimeSlot> insert(TimeSlot ts) throws DataBaseException, ArgumentException {
        checkBusiness(ts);
        return timeSlotRepository.insert(ts);
    }// insert()

    private void checkBusiness(TimeSlot ts) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(ts == null){
            errors.add("Créneau horaire manquant");
        }else{
            if(ts.getStart() == null || ts.getEnd() == null){
                errors.add("L'heure de début et l'heure de fin sont obligatoires");
            }else{
                if(ts.getStart().equals(ts.getEnd()) || ts.getStart().isAfter(ts.getEnd())){
                    errors.add("L'heure de début ne doit pas être supérieur ou égal à l'heure de fin");
                }
                this.optionsRepository.getOptions().ifPresent(options -> {
                    long diff = ChronoUnit.MINUTES.between(ts.getEnd(), ts.getStart());
                    if(diff % options.getSplitPlanning() != 0){
                        errors.add("L'heure de fin et l'heure de début doivent être cohérents avec le découpage du planning");
                    }
                });
                if(this.timeSlotRepository.exists(ts)){
                    errors.add("Ce créneau horaire existe déjà");
                }
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }// checkBusiness()

}// TimeSlotService
