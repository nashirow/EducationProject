package com.education.project.persistence;

import com.education.project.model.Matiere;
import org.springframework.stereotype.Repository;

/**
 * Cette classe gère les échanges avec la base de données concernant la classe Matiere.
 */
@Repository
public class MatiereRepository {

    public MatiereRepository(){
    }//MatiereRepository

    /**
     * Cette fonction injecte une matière en base de données.
     * @param matiere C'est la matière créée.
     * @return Matiere qui a été insérée dans la base de données.
     */
    public Matiere insert(Matiere matiere) {
        return matiere;
    }//insert()

}//MatiereRepository
