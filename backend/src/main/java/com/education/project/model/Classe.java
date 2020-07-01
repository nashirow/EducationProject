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
 * Cette classe rerpésente une classe
 */
public class Classe {

    /**
     * Identifiant de la classe
     */
    private Integer id;

    /**
     * Nom de la classe
     */
    private String nom;

    /**
     * Date de création de la classe
     */
    private Date creationDate;

    /**
     * Date de dernière modification de la classe
     */
    private Date modificationDate;

    public Classe() {
    }// Classe()

    public Classe(Integer id, String nom, Date creationDate, Date modificationDate) {
        this.id = id;
        this.nom = nom;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }// Classe()

    public String getNom() {
        return nom;
    }// getNom()

    public void setNom(String nom) {
        this.nom = nom;
    }// setNom()

    public Integer getId() {
        return id;
    }// getId()

    public void setId(Integer id) {
        this.id = id;
    }// setId()

    public Date getCreationDate() {
        return creationDate;
    }// getCreationDate()

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }// setCreationDate()

    public Date getModificationDate() {
        return modificationDate;
    }// getModificationDate()

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }// setModificationDate()

    @Override
    public String toString() {
        return "Classe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }// toString()
}// Classe
