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
import com.education.project.model.Enseignant;
import com.education.project.persistence.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * La classe permet de créer des fonctionnalités liées aux enseignants
 */
@Service
public class EnseignantService {

    private EnseignantRepository enseignantRepository;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository){
        this.enseignantRepository = enseignantRepository;
    }


    /**
     * Cette fonction permet de créer un enseignant dans la base de données
     * @param enseignant enseignant à créer
     * @return L'enseignant crée
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Enseignant> insertEnseignant(Enseignant enseignant) throws ArgumentException, DataBaseException {
        checkBusiness(enseignant);
        enseignant.setCreationDate(new Date());
        enseignant.setModificationDate(new Date());
        enseignant.setId(1);
        return enseignantRepository.insert(enseignant);
    }//insertEnseignant()

    /**
     * Cette fonction permet de contrôler les règles métiers liées aux enseignants
     * @param enseignant L'enseignant à vérifier
     * @throws DataBaseException
     * @throws ArgumentException
     */
    public void checkBusiness(Enseignant enseignant) throws DataBaseException, ArgumentException {
        List<String> errors = new ArrayList<>();
        if(enseignant == null){
            errors.add("L'enseignant est obligatoire");
        }
        else{
            if(enseignant.getNom() == null || enseignant.getNom().isEmpty()){
                errors.add("Le nom de l'enseignant est obligatoire");
            }
            if(enseignantRepository.isExistByName(enseignant.getNom())){
                errors.add("Le nom de l'enseignant " + enseignant.getNom() +" existe déjà dans la base de données.");
            }
            if(enseignant.getPrenom() == null || enseignant.getPrenom().isEmpty()){
                errors.add("Le prénom de l'enseignant est obligatoire");
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }
}//EnseignantService
