package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Options;
import com.education.project.persistence.OptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Ce service gère les fonctionnalités des options de l'application.
 */
@Service
public class OptionsService {

    private OptionsRepository optionsRepository;

    private List<Integer> valuesSplitPlanningAccepted;

    @Autowired
    public OptionsService(OptionsRepository optionsRepository){
        this.optionsRepository = optionsRepository;
        this.valuesSplitPlanningAccepted = Stream.of(15, 20, 30, 60).collect(Collectors.toList());
    }// OptionsService()

    /**
     * Cette fonction permet de modifier les options générales
     * de l'application.
     * @param fullOptions Nouvelles valeurs des options générales
     * @return boolean
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public boolean changeOptions(Options fullOptions) throws ArgumentException, DataBaseException {
        boolean result = checkBusiness(fullOptions);
        return result && optionsRepository.update(fullOptions);
    }// changeOptions()

    /**
     * Récupère les options générales de l'applications
     * @return options
     * @throws DataBaseException
     */
    public Optional<Options> getOptions() throws DataBaseException {
        return optionsRepository.getOptions();
    }// getOptions()

    private boolean checkBusiness(Options fullOptions) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(fullOptions == null){
            errors.add("Options is missing");
        }else{
            Optional<Options> currentOptions = optionsRepository.getOptions();
            if(fullOptions.getSplitPlanning() == null && fullOptions.getStartHourPlanning() == null &&
                fullOptions.getEndHourPlanning() == null){
                return false;
            }
            if(fullOptions.getSplitPlanning() != null && !this.valuesSplitPlanningAccepted.contains(fullOptions.getSplitPlanning())){
                errors.add("Le découpage du planning ne peut se faire qu'avec les valeurs suivantes (exprimées en minutes) : " + this.valuesSplitPlanningAccepted.toString());
            }
            if(fullOptions.getStartHourPlanning() != null && fullOptions.getEndHourPlanning() != null){
                if(fullOptions.getStartHourPlanning().equals(fullOptions.getEndHourPlanning())
                        || fullOptions.getStartHourPlanning().isAfter(fullOptions.getEndHourPlanning())){
                    errors.add("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
                }
                long diff = ChronoUnit.MINUTES.between(fullOptions.getEndHourPlanning(), fullOptions.getStartHourPlanning());
                for(Integer valueAccepted : this.valuesSplitPlanningAccepted){
                    if(diff % valueAccepted != 0){
                        errors.add("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
                        break;
                    }
                }
            }
            if(fullOptions.getStartHourPlanning() != null && fullOptions.getEndHourPlanning() == null){
                if(currentOptions.isPresent()){
                    Options options = currentOptions.get();
                    LocalTime currentEndHourPlanning = options.getEndHourPlanning();
                    if(fullOptions.getStartHourPlanning().equals(currentEndHourPlanning)
                            || fullOptions.getStartHourPlanning().isAfter(currentEndHourPlanning)){
                        errors.add("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
                    }
                    long diff = ChronoUnit.MINUTES.between(currentEndHourPlanning, fullOptions.getStartHourPlanning());
                    for(Integer valueAccepted : this.valuesSplitPlanningAccepted){
                        if(diff % valueAccepted != 0){
                            errors.add("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
                            break;
                        }
                    }
                }
            }
            if(fullOptions.getEndHourPlanning() != null && fullOptions.getStartHourPlanning() == null){
                if(currentOptions.isPresent()){
                    Options options = currentOptions.get();
                    LocalTime currentStartHourPlanning = options.getStartHourPlanning();
                    if(fullOptions.getEndHourPlanning().equals(currentStartHourPlanning)
                            || fullOptions.getEndHourPlanning().isBefore(currentStartHourPlanning)){
                        errors.add("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
                    }
                    long diff = ChronoUnit.MINUTES.between(fullOptions.getEndHourPlanning(), currentStartHourPlanning);
                    for(Integer valueAccepted : this.valuesSplitPlanningAccepted){
                        if(diff % valueAccepted != 0){
                            errors.add("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
                            break;
                        }
                    }
                }
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
        return true;
    }// checkBusiness()

}// OptionsService
