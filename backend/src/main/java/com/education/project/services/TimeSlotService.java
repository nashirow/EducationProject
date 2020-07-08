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

    /**
     * Supprime un créneau horaire
     * @param id Identifiant du créneau horaire à supprimer
     * @return boolean
     * @throws DataBaseException
     */
    public boolean delete(int id) throws DataBaseException {
        return timeSlotRepository.delete(id);
    }// delete()

    /**
     * Contrôle des règles de gestion
     * @param ts TimeSlot
     * @throws ArgumentException
     * @throws DataBaseException
     */
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
                    if(ts.getStart().isBefore(options.getStartHourPlanning())){
                        errors.add("L'heure de début doit être postérieur ou égal à l'heure de début d'un planning");
                    }
                    if(ts.getEnd().isAfter(options.getEndHourPlanning())){
                        errors.add("L'heure de fin doit être antérieur l'heure de fin d'un planning");
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

    /**
     * Récupère l'ensemble des créneaux horaires de l'application.
     * @param page n° de la page (facultatif)
     * @param nbElementsPerPage Nombre de créneaux horaires par page (facultatif)
     * @return créneaux horaires
     */
    public List<TimeSlot> getTimeSlots(Integer page, Integer nbElementsPerPage) throws DataBaseException {
        return timeSlotRepository.findAll(page, nbElementsPerPage);
    }// getTimeSlots()

    /**
     * Retourne le nombre total de créneaux horaires stockés
     * dans l'application.
     * @return nombre total
     * @throws DataBaseException
     */
    public long count() throws DataBaseException {
        return this.timeSlotRepository.count();
    }// count()

}// TimeSlotService
