/*
 * Copyright 2020 Hicham AZIMANI
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
import com.education.project.model.Salle;
import com.education.project.persistence.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalleService {

    private SalleRepository salleRepository;

    @Autowired
    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }//SalleService()

    /**
     * Cette fonction permet de créer une salle en base de données
     * @param salleToInsert
     * @return
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Salle> insertSalle(Salle salleToInsert) throws ArgumentException, DataBaseException {
        checkBusiness(salleToInsert, false);
        Date now = new Date();
        salleToInsert.setCreationDate(now);
        salleToInsert.setModificationDate(now);
        return salleRepository.insert(salleToInsert);
    }//insertSalle()

    public Optional<Salle> updateSalle(Salle salleToUpdate) throws ArgumentException, DataBaseException {
        checkBusiness(salleToUpdate,true);
        Date now = new Date();
        salleToUpdate.setModificationDate(now);
        return salleRepository.update(salleToUpdate);
    }//updateSalle()

    /**
     * Cette fonction permet de récupérer la liste des salles en base de données grâce aux informations passés en paramètres
     * @param nom Nom de la salle
     * @param page Nombre de page
     * @param nbElementsPerPage Nombre de salles par page souhaités
     * @return La liste des salles récupérées
     * @throws DataBaseException
     */
    public List<Salle> getSalles(String nom, Integer page, Integer nbElementsPerPage) throws DataBaseException {
        return salleRepository.getSalles(nom,page,nbElementsPerPage);
    }//getSalles()

    /**
     * Cette fonction permet de vérifier les règles métiers liées aux salles
     * @param salle La salle à vérifier
     * @param isUpdate boolean true si la salle est à mettre à jour
     * @throws ArgumentException
     * @throws DataBaseException
     */
    private void checkBusiness(Salle salle, boolean isUpdate) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(salle == null){
            StringBuilder sb = new StringBuilder("La salle à ");
            sb.append(isUpdate ? "mettre à jour " : "insérer ");
            sb.append("est obligatoire");
            errors.add(sb.toString());
        }
        else{
            if(isUpdate && salle.getId() == null){
                errors.add("L'identifiant de la salle est obligatoire");
            }
            if(salle.getNom() == null || salle.getNom().isEmpty()){
                errors.add("Le nom d'une salle est obligatoire");
            }
            if(salle.getNom() != null && salleRepository.isExistByName(salle.getNom())){
                errors.add("Le nom de la salle existe déjà en base de données");
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }//checkBusiness()

    public long countSalles(String nom) throws DataBaseException {
        return salleRepository.countByName(nom);
    }

    /**
     * Cette fonction permet de supprimer une salle avec son identifiant passé en paramètre
     * @param id Identifiant de la salle à supprimer
     * @return boolean
     */
    public boolean deleteSalle(int id) throws DataBaseException {
        return salleRepository.delete(id);
    }//deleteSalle()

    public Optional<Salle> getSalle(int id) throws DataBaseException {
        return salleRepository.findById(id);
    }//getSalle()
}//SalleService
