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
        checkBusiness(salleToInsert);
        Date now = new Date();
        salleToInsert.setCreationDate(now);
        salleToInsert.setModificationDate(now);
        return salleRepository.insert(salleToInsert);
    }//insertSalle()

    /**
     * Cette fonction permet de vérifier les règles métiers liées aux salles
     * @param salle La salle à vérifier
     * @throws ArgumentException
     * @throws DataBaseException
     */
    private void checkBusiness(Salle salle) throws ArgumentException, DataBaseException {
        List<String> errors = new ArrayList<>();
        if(salle == null){
            errors.add("La salle est obligatoire");
        }
        else{
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
}//SalleService
