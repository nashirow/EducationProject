package com.education.project.model;

public class ResponseEndPoint {
    private Object value;

    private Object erreurs;

    public ResponseEndPoint(Object value, Object erreurs) {
        this.value = value;
        this.erreurs = erreurs;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getErreurs() {
        return erreurs;
    }

    public void setErreurs(Object erreurs) {
        this.erreurs = erreurs;
    }
}//ResponseEndPoint
