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
package com.education.project.exceptions;

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
