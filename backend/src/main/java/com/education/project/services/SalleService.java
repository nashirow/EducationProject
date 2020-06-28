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
     * Cette fonction permet de vérifier les règles métiers liées aux salles
     * @param salle La salle à vérifier
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

    /**
     * Cette fonction permet de supprimer une salle avec son identifiant passé en paramètre
     * @param id Identifiant de la salle à supprimer
     * @return boolean
     */
    public boolean deleteSalle(int id) throws DataBaseException {
        return salleRepository.delete(id);
    }//deleteSalle()
}//SalleService
