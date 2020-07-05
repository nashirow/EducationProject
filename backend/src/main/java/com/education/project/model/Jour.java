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

/**
 * Classe représentant un jour.
 */
public class Jour {

    /**
     * Identifiant du jour
     */
    private Integer id;

    /**
     * Nom du jour
     */
    private String nom;

    public Jour() {
    }// Jour()

    public Jour(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }// Jour()

    public Integer getId() {
        return id;
    }// getId()

    public void setId(Integer id) {
        this.id = id;
    }// setId()

    public String getNom() {
        return nom;
    }// getNom()

    public void setNom(String nom) {
        this.nom = nom;
    }// setNom()

}// Jour
