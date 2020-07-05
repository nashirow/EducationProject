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
import java.util.List;

/**
 * Cette classe représente un planning
 */
public class Planning {

    /**
     * Identifiant du planning
     */
    private Integer id;

    /**
     * Nom du planning
     */
    private String nom;

    /**
     * Classe pour qui le planning est créé
     */
    private Classe classe;

    /**
     * Slots composant le planning
     */
    private List<Slot> slots;

    /**
     * C'est la date de création de la matière.
     */
    private Date creationDate;

    /**
     * C'est la date de modification de la matière.
     */
    private Date modificationDate;

    /**
     * Indique si le planning doit utiliser le mercredi.
     * Par défaut, la valeur est à true.
     */
    private boolean wednesdayUsed;

    /**
     * Indique si le planning doit utiliser le samedi.
     * Par défaut, la valeur est à false.
     */
    private boolean saturdayUsed;

    public Planning() {
        this.wednesdayUsed = true;
    }// Planning()

    public Planning(String nom, Classe classe, List<Slot> slots) {
        this.nom = nom;
        this.classe = classe;
        this.slots = slots;
        this.wednesdayUsed = true;
    }// Planning()

    public Planning(Integer id, String nom, Classe classe, List<Slot> slots) {
        this.id = id;
        this.nom = nom;
        this.classe = classe;
        this.slots = slots;
        this.wednesdayUsed = true;
    }// Planning()

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

    public Classe getClasse() {
        return classe;
    }// getClasse()

    public void setClasse(Classe classe) {
        this.classe = classe;
    }// setClasse()

    public List<Slot> getSlots() {
        return slots;
    }// getSlots()

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }// setSlots()

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

    public boolean isWednesdayUsed() {
        return wednesdayUsed;
    }// isWednesdayUsed()

    public void setWednesdayUsed(boolean wednesdayUsed) {
        this.wednesdayUsed = wednesdayUsed;
    }// setWednesdayUsed()

    public boolean isSaturdayUsed() {
        return saturdayUsed;
    }// isSaturdayUsed()

    public void setSaturdayUsed(boolean saturdayUsed) {
        this.saturdayUsed = saturdayUsed;
    }// setSaturdayUsed()

}// Planning
