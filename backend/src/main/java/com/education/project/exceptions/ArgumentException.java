package com.education.project.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de gérer les exceptions levées quand des mauvais arguments sont passés dans une fonction.
 */
public class ArgumentException extends Exception{
    /**
     * Contient la liste des erreurs que l'exception va traiter.
     */
    private List<String> erreurs;

    public ArgumentException(List<String> erreurs){
        super(String.join(",",erreurs));
        this.erreurs = erreurs;
    }//ArgumentException()

    public List<String> getErreurs() {
        return erreurs;
    }

    public void setErreurs(List<String> erreurs) {
        this.erreurs = erreurs;
    }
}//ArgumentException
