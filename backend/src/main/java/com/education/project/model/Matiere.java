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
 * Cette classe représente une matière.
 */
public class Matiere {

    /**
     *  C'est l'identifiant unique de la matière.
     */
    private Integer id;

    /**
     * C'est le nom de la matière.
     */
    private String nom;

    /**
     * C'est le volume horaire de la matière.
     */
    private String volumeHoraire;

    /**
     * C'est la description liée à la matière.
     */
    private String description;

    /**
     * C'est la date de création de la matière.
     */
    private Date creationDate;

    /**
     * C'est la date de modification de la matière.
     */
    private Date modificationDate;


    public Matiere() {
    }//Matiere()

    /**
     * Instancie une matière.
     * @param nom C'est le nom de la matière.
     * @param volumeHoraire C'est le volume horaire de la matière.
     * @param description C'est la description liée à la matière.
     */
    public Matiere(String nom, String volumeHoraire, String description) {
        this.nom = nom;
        this.volumeHoraire = volumeHoraire;
        this.description = description;
    }//Matiere()

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setVolumeHoraire(String volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}//Matiere
