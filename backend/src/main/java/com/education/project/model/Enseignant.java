/*
 * Copyright 2020 Hicham AZIMANI, Yassine AZIMANI
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
 * Cette classe représente un enseignant
 */
public class Enseignant {

    /**
     * C'est l'identifiant de l'enseignant
     */
    private Integer id;

    /**
     * C'est le nom de l'enseignant
     */
    private String nom;

    /**
     * C'est le prenom de l'enseignant
     */
    private String prenom;

    /**
     * C'est la date de création de l'enseignant
     */
    private Date creationDate;

    /**
     * c'est la date de modification de l'enseignant
     */
    private Date modificationDate;

    /**
     * Constructeur par défaut
     */
    public Enseignant(){
    }//Enseignant()

    /**
     * Instancie un enseignant
     */
    public Enseignant(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }//Enseignant()

    public Enseignant(Integer id, String nom, String prenom, Date creationDate, Date modificationDate) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }//Enseignant()

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
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
        return "Enseignant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }//toString()
}//Enseignant
