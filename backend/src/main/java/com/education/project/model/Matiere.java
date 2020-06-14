package com.education.project.model;

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
     * C'est la couleur de fond de la matière.
     */
    private String couleurFond;

    /**
     * C'est la couleur de la police de la matière.
     */
    private String couleurPolice;

    /**
     * C'est le volume horaire de la matière.
     */
    private String volumeHoraire;

    /**
     * C'est la description liée à la matière.
     */
    private String descriptionMatiere;

    public Matiere() {
    }//Matiere()

    /**
     * Instancie une matière.
     * @param nom C'est le nom de la matière.
     * @param couleurFond C'est la couleur de fond de la matière.
     * @param couleurPolice C'est la couleur de la police de la matière.
     * @param volumeHoraire C'est le volume horaire de la matière.
     * @param descriptionMatiere C'est la description liée à la matière.
     */
    public Matiere(String nom, String couleurFond, String couleurPolice, String volumeHoraire, String descriptionMatiere) {
        this.nom = nom;
        this.couleurFond = couleurFond;
        this.couleurPolice = couleurPolice;
        this.volumeHoraire = volumeHoraire;
        this.descriptionMatiere = descriptionMatiere;
    }//Matiere()

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCouleurFond() {
        return couleurFond;
    }

    public void setCouleurFond(String couleurFond) {
        this.couleurFond = couleurFond;
    }

    public String getCouleurPolice() {
        return couleurPolice;
    }

    public void setCouleurPolice(String couleurPolice) {
        this.couleurPolice = couleurPolice;
    }

    public String getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setVolumeHoraire(String volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    public String getDescriptionMatiere() {
        return descriptionMatiere;
    }

    public void setDescriptionMatiere(String descriptionMatiere) {
        this.descriptionMatiere = descriptionMatiere;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}//Matiere
