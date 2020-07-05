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
 * Cette classe représente un slot
 */
public class Slot {

    /**
     * C'est l'identifiant du slot
     */
    private Integer id;

    /**
     * C'est le commentaire du slot
     */
    private String comment;

    /**
     * C'est la date de création du slot
     */
    private Date creationDate;

    /**
     * C'est la date de modification du slot
     */
    private Date modificationDate;

    /**
     * C'est la couleur de fond de la matière.
     */
    private String couleurFond;

    /**
     * C'est la couleur de la police de la matière.
     */
    private String couleurPolice;

    /**
     * C'est le créneau horaire du slot
     */
    private TimeSlot timeSlot;

    /**
     * C'est l'enseignant du slot
     */
    private Enseignant enseignant;

    /**
     * C'est la matière du slot
     */
    private Matiere matiere;

    /**
     * C'est la salle du slot
     */
    private Salle salle;

    /**
     * Plannings possédant ce slot. Cette variable est utile
     * pour savoir pourquoi il n'est pas possible de supprimer
     * un slot.
     */
    private List<Planning> plannings;

    /**
     * Jour pour lequel le slot est programmé.
     */
    private Jour jour;

    /**
     * Constructeur
     */
    public Slot() {
    }//Slot()

    public Slot(Integer id) {
        this.id = id;
    }// Slot()

    public Slot(Integer id, String comment, Date creationDate, Date modificationDate, String couleurFond, String couleurPolice, TimeSlot timeSlot, Enseignant enseignant, Matiere matiere, Salle salle) {
        this.id = id;
        this.comment = comment;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.couleurFond = couleurFond;
        this.couleurPolice = couleurPolice;
        this.timeSlot = timeSlot;
        this.enseignant = enseignant;
        this.matiere = matiere;
        this.salle = salle;
    }//Slot()

    public Slot(Integer id, Date creationDate, Date modificationDate, String couleurFond, String couleurPolice, TimeSlot timeSlot, Matiere matiere) {
        this.id = id;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.couleurFond = couleurFond;
        this.couleurPolice = couleurPolice;
        this.timeSlot = timeSlot;
        this.matiere = matiere;
    }//Slot()

    public Integer getId() {
        return id;
    }//getId()

    public void setId(Integer id) {
        this.id = id;
    }//setId()

    public String getComment() {
        return comment;
    }//getComment()

    public void setComment(String comment) {
        this.comment = comment;
    }//setComment()

    public Date getCreationDate() {
        return creationDate;
    }//getCreationDate()

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }//setCreationDate()

    public Date getModificationDate() {
        return modificationDate;
    }//getModificationDate()

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }//setModificationDate()

    public String getCouleurFond() {
        return couleurFond;
    }//getCouleurFond()

    public void setCouleurFond(String couleurFond) {
        this.couleurFond = couleurFond;
    }//setCouleurFond()

    public String getCouleurPolice() {
        return couleurPolice;
    }//getCouleurPolice()

    public void setCouleurPolice(String couleurPolice) {
        this.couleurPolice = couleurPolice;
    }//setCouleurPolice()

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }//getTimeSlot()

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }//setTimeSlot()

    public Enseignant getEnseignant() {
        return enseignant;
    }//getEnseignant()

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }//setEnseignant()

    public Matiere getMatiere() {
        return matiere;
    }//getMatiere()

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }//setMatiere()

    public Salle getSalle() {
        return salle;
    }//getSalle()

    public void setSalle(Salle salle) {
        this.salle = salle;
    }//setSalle()

    public List<Planning> getPlannings() {
        return plannings;
    }// getPlannings()

    public void setPlannings(List<Planning> plannings) {
        this.plannings = plannings;
    }// setPlannings()

    public Jour getJour() {
        return jour;
    }// getJour()

    public void setJour(Jour jour) {
        this.jour = jour;
    }// setJour()

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", couleurFond='" + couleurFond + '\'' +
                ", couleurPolice='" + couleurPolice + '\'' +
                ", timeSlot=" + timeSlot +
                ", enseignant=" + enseignant +
                ", matiere=" + matiere +
                ", salle=" + salle +
                ", plannings=" + plannings +
                ", jour=" + jour +
                '}';
    }// toString()

}//Slot
