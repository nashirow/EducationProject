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
import com.education.project.model.Slot;
import com.education.project.persistence.SlotRepository;
import com.education.project.utils.ColorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Cette classe gère les fonctionnalitées liées aux slots
 */
@Service
public class SlotService {

    private SlotRepository slotRepository;

    private ColorUtils colorUtils;

    /**
     * Nombre de slots maximum pour le même créneau horaire et le
     * même jour. (Utile pour la division d'une classe en groupes)
     */
    private final static int MAX_SLOTS_IN_SAME_TIMESLOT_AND_SAME_DAY = 2;

    @Autowired
    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
        this.colorUtils = new ColorUtils();
    }//SlotService()

    /**
     * Cette fonction permet d'insérer un slot en base de données
     *
     * @param slotToInsert Le slot à insérer
     * @return le slot inséré
     */
    public Optional<Slot> insertSlot(Slot slotToInsert) throws ArgumentException, DataBaseException {
        checkBusiness(slotToInsert, false);
        Date now = new Date();
        slotToInsert.setCreationDate(now);
        slotToInsert.setModificationDate(now);
        return slotRepository.insert(slotToInsert);
    }//insertSlot()

    /**
     * Cette fonction permet de récupérer un slot avec l'identifiant passé en paramètre
     * @param id Identifiant du slot à récupéré
     * @return le slot récupéré
     */
    public Optional<Slot> findById(int id) throws DataBaseException {
        return slotRepository.findById(id);
    }//findById()

    /**
     * Cette fonction permet de mettre à jour un slot
     * @param slotToUpdate Le slot à mettre à jour
     * @return retourne le slot mis à jour
     */
    public Optional<Slot> updateSlot(Slot slotToUpdate) throws ArgumentException, DataBaseException {
        checkBusiness(slotToUpdate,true);
        slotToUpdate.setModificationDate(new Date());
        return slotRepository.update(slotToUpdate);
    }//updateSlot()

    /**
     * Cette fonction permet de supprimer un slot d'identifiant passé en paramètre
     * @param id L'identifiant du slot à supprimer
     * @return boolean
     */
    public boolean deleteSlot(int id) throws DataBaseException {
        return slotRepository.deleteSlot(id);
    }//deleteSlot()

    /**
     * Cette fonction permet de vérifier les règles métiers liées aux slots
     * @param slotToInsert Le slot à vérifier
     * @param isUpdate boolean true si le slot à vérifier est à update
     * @throws ArgumentException
     * @throws DataBaseException
     */
    private void checkBusiness(Slot slotToInsert, boolean isUpdate) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if (slotToInsert == null) {
            errors.add("Le slot est obligatoire");
        } else {
            if (slotToInsert.getId() == null && isUpdate) {
                errors.add("Le slot doit obligatoirement avoir un identifiant");
            } else {
                if (slotToInsert.getMatiere() == null) {
                    errors.add("La matière est obligatoire");
                }
                if (slotToInsert.getMatiere() != null && slotToInsert.getMatiere().getId() == null) {
                    errors.add("L'identifiant de la matière est obligatoire");
                }
                if(slotToInsert.getJour() == null || slotToInsert.getJour().getId() == null){
                    errors.add("Le jour est obligatoire");
                }
                if (slotToInsert.getTimeSlot() == null) {
                    errors.add("Le créneau horaire est obligatoire");
                }
                if (slotToInsert.getTimeSlot() != null && slotToInsert.getTimeSlot().getId() == null) {
                    errors.add("L'identifiant du créneau horaire est obligatoire");
                }
                if (slotToInsert.getCouleurFond() == null || slotToInsert.getCouleurFond().isEmpty()) {
                    errors.add("La couleur de fond est obligatoire");
                }
                if (slotToInsert.getCouleurPolice() == null || slotToInsert.getCouleurPolice().isEmpty()) {
                    errors.add("La couleur de la police est obligatoire");
                }
                if (slotToInsert.getCouleurFond() != null && slotToInsert.getCouleurPolice() != null && slotToInsert.getCouleurPolice().equals(slotToInsert.getCouleurFond())) {
                    errors.add("La couleur de fond et de la police ne peuvent pas être la même");
                }
                if (slotToInsert.getCouleurFond() != null && !slotToInsert.getCouleurFond().isEmpty() && !colorUtils.isHex(slotToInsert.getCouleurFond())) {
                    errors.add("La couleur de fond doit être au format hexadécimal");
                }
                if (slotToInsert.getCouleurPolice() != null && !slotToInsert.getCouleurPolice().isEmpty() && !colorUtils.isHex(slotToInsert.getCouleurPolice())) {
                    errors.add("La couleur de la police doit être au format hexadécimal");
                }
                if (slotRepository.isExistByColorFond(slotToInsert)) {
                    errors.add("Il existe déjà un slot avec ce fond de couleur");
                }
                if(slotToInsert.getJour() != null && slotToInsert.getJour().getId() != null && slotToInsert.getTimeSlot() != null && slotRepository.countByJour(slotToInsert.getJour().getId(), slotToInsert.getTimeSlot()) > MAX_SLOTS_IN_SAME_TIMESLOT_AND_SAME_DAY){
                    errors.add("Le nombre de slots pour le même jour et le même créneau horaire est limité à " + MAX_SLOTS_IN_SAME_TIMESLOT_AND_SAME_DAY + " slots");
                }
            }
        }
        if(!errors.isEmpty()) {
            throw new ArgumentException(errors);
        }
    }//checkBusiness()

    public long countSlots(Map<String,String> mapSlot) throws DataBaseException {
        return slotRepository.countSlot(mapSlot);
    }//countSlots()

    /**
     * Cette fonction permet de récupérer tous les slots en base de données en fonction des informations passées en paramètres
     * @param params paramètres liés au slot
     * @return les slots récupérés
     */
    public List<Slot> getSlots(Map<String, String> params) throws DataBaseException {
        return slotRepository.getSlots(params);
    }//getSlots
}//SlotService

