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
package com.education.project.model;

import java.util.Date;

/**
 * Cette classe représente une salle
 */
public class Salle {

    /**
     * C'est l'identifiant de la salle
     */
    private Integer id;

    /**
     * C'est le nom d'une salle
     */
    private String nom;

    /**
     * C'est la date de création d'une salle
     */
    private Date creationDate;

    /**
     * C'est la date de modification d'une salle
     */
    private Date modificationDate;

    public Salle(){
    }//Salle

    public Salle(Integer id, String nom, Date creationDate, Date modificationDate) {
        this.id = id;
        this.nom = nom;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Salle(String nom) {
        this.nom = nom;
    }//Salle()

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}//Salle
