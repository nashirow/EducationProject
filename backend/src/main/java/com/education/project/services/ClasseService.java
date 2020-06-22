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
        this.checkBusiness(classeToInsert, false);
        Date now = new Date();
        classeToInsert.setCreationDate(now);
        classeToInsert.setModificationDate(now);
        return classeRepository.insert(classeToInsert);
    }// insertClasse()

    /**
     * Retourne la classe dont l'identifiant est passé en paramètre
     * @param id identifiant de la classe
     * @return Classe
     */
    public Optional<Classe> getClasse(int id) throws DataBaseException {
        return classeRepository.findById(id);
    }// getClasse()

    /**
     * Cette fonction permet de mettre à jour une classe
     * existante dans l'application.
     * @param classeToUpdate Classe à mettre à jour
     * @return Classe mise à jour
     */
    public Optional<Classe> updateClasse(Classe classeToUpdate) throws ArgumentException, DataBaseException {
        this.checkBusiness(classeToUpdate, true);
        Date now = new Date();
        classeToUpdate.setModificationDate(now);
        return classeRepository.update(classeToUpdate);
    }// updateClasse()

    /**
     * Contrôle les règles métiers
     * @param classe Classe à contrôler
     * @throws ArgumentException
     * @throws DataBaseException
     */
    private void checkBusiness(Classe classe, boolean isUpdate) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(classe == null){
            StringBuilder sb = new StringBuilder("La classe à ");
            sb.append(isUpdate ? "mettre à jour " : "insérer ");
            sb.append("est obligatoire");
            errors.add(sb.toString());
        }else {
            if(isUpdate && classe.getId() == null){
                errors.add("L'identifiant de la classe à mettre à jour est obligatoire");
            }
            if(classe.getNom() == null || classe.getNom().isEmpty()){
                errors.add("Le nom d'une classe est obligatoire");
            }
            if(classeRepository.existsByName(classe.getNom())){
                errors.add("Une classe avec le nom " + classe.getNom() + " existe déjà dans la base de données");
            }
        }
        if(!errors.isEmpty()){
            throw new ArgumentException(errors);
        }
    }// checkBusiness()

}// ClasseService
