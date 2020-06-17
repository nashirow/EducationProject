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
import com.education.project.model.Matiere;
import com.education.project.persistence.MatiereRepository;
import com.education.project.utils.ColorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * La classe permet d'utiliser les fonctionnalités liées à une matière.
 */
@Service
public class MatiereService {
    /**
     * Cette classe me permet de réaliser les requêtes liées à la matière (SQL) sur la base de données.
     */
    private MatiereRepository matiereRepository;

    @Autowired
    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }//MatiereService()

    /**
     * Cette fonction permet de créer une matière.
     * @param matiere la matière à créer.
     * @return La matière créée.
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
     * Cette fonction permet de modifier une matière.
     * @param matiere La matière à modifier.
     * @return La matière modifiée.
     * @throws ArgumentException
     */
    public Optional<Matiere> updateMatiere(Matiere matiere) throws ArgumentException, DataBaseException {
        Optional<Matiere> matiereFromBd = matiereRepository.findById(matiere.getId());
        checkBusinessForCreationAndUpdate(matiere);
        if(matiereFromBd.isPresent()){
            Matiere matFromBd = matiereFromBd.get();
            matFromBd.setNom(matiere.getNom());
            matFromBd.setCouleurFond(matiere.getCouleurFond());
            matFromBd.setCouleurPolice(matiere.getCouleurPolice());
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
     * La fonction permet de vérifier les règles métiers.
     * @param matiere La matière à vérifier.
     * @throws ArgumentException
     */
    public void checkBusinessForCreationAndUpdate(Matiere matiere) throws ArgumentException {
        List<String> erreurs = new ArrayList<>();
        if(matiere == null){
            erreurs.add("La matière est obligatoire");
        }
        else{
            if (matiere.getNom() == null || matiere.getNom().isEmpty()) {
                erreurs.add("Le nom de la matière est obligatoire");
            }
            if (matiere.getCouleurFond() == null || matiere.getCouleurFond().isEmpty()) {
                erreurs.add("La couleur de fond est obligatoire");
            }
            if (matiere.getCouleurPolice() == null || matiere.getCouleurPolice().isEmpty()) {
                erreurs.add("La couleur de la police est obligatoire");
            }
            if(matiere.getCouleurFond() != null && matiere.getCouleurPolice() != null && matiere.getCouleurFond().equals(matiere.getCouleurPolice())){
                erreurs.add("La couleur du fond et de la police ne peuvent pas être la même");
            }
            if(matiere.getCouleurFond() != null && !matiere.getCouleurFond().isEmpty() && !ColorUtils.isHex(matiere.getCouleurFond())){
                erreurs.add("La couleur de fond doit être au format hexadécimal");
            }
            if(matiere.getCouleurPolice() != null && !matiere.getCouleurPolice().isEmpty() && !ColorUtils.isHex(matiere.getCouleurPolice())){
                erreurs.add("La couleur de la police doit être au format hexadécimal");
            }
            if(matiere.getDescription() != null && (matiere.getDescription().length() < 10 || matiere.getDescription().length() > 255)){
                erreurs.add("La description doit être comprise entre 10 et 255 caractères");
            }
            if(matiere.getNom() != null && !matiere.getNom().isEmpty() && (matiere.getNom().length() < 3 || matiere.getNom().length() > 40)){
                erreurs.add("Le nom d'une matière doit contenir entre 3 et 40 caractères");
            }
        }
        if(!erreurs.isEmpty()){
            throw new ArgumentException(erreurs);
        }
    }//checkBusinessForCreationAndUpdate()
}//MatiereService
