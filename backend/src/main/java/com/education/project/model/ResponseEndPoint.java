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
 * Cette classe représente les réponses d'un endpoint
 */
public class ResponseEndPoint {

    /**
     * Représente le résultat renvoyé par le endpoint
     */
    private Object value;

    /**
     * Représente la liste des erreurs renvoyées par le endpoint
     */
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
