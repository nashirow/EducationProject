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
import com.education.project.model.Matiere;
import com.education.project.persistence.MatiereRepository;
import com.education.project.utils.LocalTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * La classe permet d'utiliser les fonctionnalités liées à une matière
 */
@Service
public class MatiereService {
    /**
     * Cette classe me permet de réaliser les requêtes liées à la matière (SQL) sur la base de données
     */
    private MatiereRepository matiereRepository;

    @Autowired
    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }//MatiereService()

    /**
     * Cette fonction permet de créer une matière
     * @param matiere la matière à créer
     * @return La matière créée
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Matiere> insertMatiere(Matiere matiere) throws ArgumentException, DataBaseException {
        checkBusinessForCreationAndUpdate(matiere);
        matiere.setCreationDate(new Date());
        matiere.setModificationDate(new Date());
        return matiereRepository.insert(matiere);
    }//insertMatiere()

    /**
     * Cette fonction permet de modifier une matière
     * @param matiere La matière à modifier
     * @return La matière modifiée.
     * @throws ArgumentException
     */
    public Optional<Matiere> updateMatiere(Matiere matiere) throws ArgumentException, DataBaseException {
        Optional<Matiere> matiereFromBd = matiereRepository.findById(matiere.getId());
        checkBusinessForCreationAndUpdate(matiere);
        if(matiereFromBd.isPresent()){
            Matiere matFromBd = matiereFromBd.get();
            matFromBd.setNom(matiere.getNom());
            if(matiere.getVolumeHoraire() != null){
                matFromBd.setVolumeHoraire(matiere.getVolumeHoraire());
            }
            if(matiere.getDescription() != null){
                matFromBd.setDescription(matiere.getDescription());
            }
            matiere.setModificationDate(new Date());
            return matiereRepository.update(matiere);
        }
        List<String> errors = new ArrayList<>();
        errors.add("La modification de la matière est impossible : l'identifiant n'est pas renseigné.");
        throw new ArgumentException(errors);
    }//updateMatiere()

    /**
     * La fonction permet de supprimer une matière grâce à son identifiant
     * @param id L'identifiant de la matière à supprimer
     * @return boolean
     */
    public boolean deleteMatiere(int id) throws DataBaseException {
        if(matiereRepository.isUsedBySlots(id)){
            throw new DataBaseException("Impossible de supprimer la matière : La matière que vous tentez de supprimer est peut-être utilisée par un ou plusieurs slot(s)");
        }
        return matiereRepository.deleteMatiere(id);
    }//deleteMatiere()

    /**
     * La fonction permet de récupérer une matière par son identifiant
     * @param id L'identifiant de la matière à récupérer
     * @return boolean
     */
    public Optional<Matiere> getMatiere(int id) throws DataBaseException {
        return  matiereRepository.findById(id);
    }//getMatiere()

    /**
     * Cette fonction permet de récupérer un ensemble de matières grâce à un nom et la couleur de police passés en paramètre
     * @param nom Le nom de la matière ou expression incomplète (optionnel)
     * @param page n° de la page (optionnel)
     * @param nbElementsPerPage Nombre d'éléments par page (optionnel)
     * @return Liste de matières
     */
    public List<Matiere> getMatieres(String nom, Integer page, Integer nbElementsPerPage) throws DataBaseException {
        return matiereRepository.findAll(nom, page, nbElementsPerPage);
    }//getMatieres()

    /**
     * Cette fonction permet de compter un ensemble de matières grâce à un nom et la couleur de police passés en paramètre
     * @param nom Le nom de la matière ou expression incomplète (optionnel)
     * @return nombre de matières
     */
    public long countMatieres(String nom) throws DataBaseException {
        return matiereRepository.count(nom);
    }// countMatieres()

    /**
     * La fonction permet de vérifier les règles métiers.
     * @param matiere La matière à vérifier.
     * @throws ArgumentException
     */
    public void checkBusinessForCreationAndUpdate(Matiere matiere) throws ArgumentException, DataBaseException {
        List<String> erreurs = new ArrayList<>();
        if(matiere == null){
            erreurs.add("La matière est obligatoire");
        }
        else{
            if (matiere.getNom() == null || matiere.getNom().isEmpty()) {
                erreurs.add("Le nom de la matière est obligatoire");
            }
            if(matiereRepository.isExistByName(matiere.getNom())){
                erreurs.add("Cette matière existe déjà");
            }
            if(matiere.getDescription() != null && !matiere.getDescription().isEmpty() && (matiere.getDescription().length() < 10 || matiere.getDescription().length() > 255)){
                erreurs.add("La description doit être comprise entre 10 et 255 caractères");
            }
            if(matiere.getNom() != null && !matiere.getNom().isEmpty() && (matiere.getNom().length() < 3 || matiere.getNom().length() > 40)){
                erreurs.add("Le nom d'une matière doit contenir entre 3 et 40 caractères");
            }
            if(matiere.getVolumeHoraire() != null && !matiere.getVolumeHoraire().isEmpty() && !LocalTimeUtils.checkStringIsFormattedForLocalTime(matiere.getVolumeHoraire())){
                erreurs.add("Le volume horaire hebdomadaire de la matière doit respecter le format HH:mm");
            }
        }
        if(!erreurs.isEmpty()){
            throw new ArgumentException(erreurs);
        }
    }//checkBusinessForCreationAndUpdate()
}//MatiereService
