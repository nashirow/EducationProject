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
