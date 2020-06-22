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
import com.education.project.model.Classe;
import com.education.project.persistence.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe gère l'ensemble des fonctionnalités
 * liées à une classe.
 */
@Service
public class ClasseService {

    private ClasseRepository classeRepository;

    @Autowired
    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }// ClasseService()

    /**
     * Cette fonction permet d'insérer une classe dans l'application.
     * @param classeToInsert Classe à insérer
     * @return Classe qui a été insérée dans l'application
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Classe> insertClasse(Classe classeToInsert) throws ArgumentException, DataBaseException {
        this.checkBusiness(classeToInsert);
        Date now = new Date();
        classeToInsert.setCreationDate(now);
        classeToInsert.setModificationDate(now);
        return classeRepository.insert(classeToInsert);
    }// insertClasse()

    /**
     * Contrôle les règles métiers
     * @param classeToInsert Classe à contrôler
     * @throws ArgumentException
     * @throws DataBaseException
     */
    private void checkBusiness(Classe classeToInsert) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(classeToInsert == null){
            errors.add("La classe à insérer est obligatoire");
        }else {
            if(classeToInsert.getNom() == null || classeToInsert.getNom().isEmpty()){
                errors.add("Le nom d'une classe est obligatoire");
            }
            if(classeRepository.existsByName(classeToInsert.getNom())){
                errors.add("Une classe avec le nom " + classeToInsert.getNom() + " existe déjà dans la base de données");
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }// checkBusiness()

}// ClasseService
