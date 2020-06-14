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
import com.education.project.model.Matiere;
import com.education.project.persistence.MatiereRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Matiere createMatiere(Matiere matiere) throws ArgumentException {
        checkBusinessForCreationAndUpdate(matiere);
        matiere = matiereRepository.insert(matiere);
        return matiere;
    }//createMatiere()

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
                erreurs.add("La couleur de fond est obligatoire");
            }
        }
        if(!erreurs.isEmpty()){
            throw new ArgumentException(erreurs);
        }
    }//checkBusinessForCreationAndUpdate()

}//MatiereService
