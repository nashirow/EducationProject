package com.education.project.model;

import java.util.Date;

public class Salle {

    private Integer id;

    private String nom;

    private Date creationDate;

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
