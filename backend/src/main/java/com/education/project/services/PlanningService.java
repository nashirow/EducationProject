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
import com.education.project.model.Planning;
import com.education.project.persistence.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Ce service gère les fonctionnalités liées
 * au planning.
 */
@Service
public class PlanningService {

    private PlanningRepository planningRepository;

    @Autowired
    public PlanningService(PlanningRepository planningRepository) {
        this.planningRepository = planningRepository;
    }// PlanningService()

    /**
     * Crée un planning au niveau de l'application.
     * @param planningToInsert Planning à créer
     * @return Planning créé
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Planning> insertPlanning(Planning planningToInsert) throws ArgumentException, DataBaseException {
        checkBusiness(planningToInsert, false);
        Date now = new Date();
        planningToInsert.setCreationDate(now);
        planningToInsert.setModificationDate(now);
        return planningRepository.insert(planningToInsert);
    }// insertPlanning()

    /**
     * Met à jour un planning au niveau de l'application
     * @param planning Planning à mettre à jour
     * @return Planning mis à jour
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Planning> updatePlanning(Planning planning) throws ArgumentException, DataBaseException {
        checkBusiness(planning, true);
        planning.setModificationDate(new Date());
        return planningRepository.update(planning);
    }// updatePlanning()

    /**
     * Contrôle des règles métiers
     * @param planning Planning à contrôler
     * @param isUpdate Indique si le contrôle concerne la mise à jour d'un planning
     * @throws ArgumentException
     */
    private void checkBusiness(Planning planning, boolean isUpdate) throws ArgumentException{
        List<String> errors = new ArrayList<>();
        if(planning == null){
            errors.add("Le planning est obligatoire");
        }else{
            if(isUpdate && planning.getId() == null){
                errors.add("L'identifiant du planning est obligatoire");
            }
            if(planning.getClasse() == null){
                errors.add("La classe est obligatoire");
            }
            if(planning.getClasse() != null && planning.getClasse().getId() == null){
                errors.add("La classe est obligatoire");
            }
            if(planning.getNom() == null || planning.getNom().isEmpty()){
                errors.add("Le nom du planning est obligatoire");
            }
            if(planning.getSlots() == null || planning.getSlots().isEmpty()){
                errors.add("Un ou plusieurs slot(s) est/sont obligatoire(s)");
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }// checkBusiness()

}// PlanningService
