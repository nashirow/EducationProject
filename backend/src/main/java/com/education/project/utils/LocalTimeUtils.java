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
package com.education.project.utils;

/**
 * Classe utilitaire pour la gestion des LocalTime.
 */
public class LocalTimeUtils {

    /**
     * Fonction utilitaire permettant de formater correctement en
     * chaîne de caractères un nombre (unité de temps) n'ayant qu'un
     * seul digit/une seule unité.
     * @param time Temps
     * @return Temps formaté
     */
    public static String formatTime(int time){
        if(time < 10){
            return "0" + time;
        }
        return String.valueOf(time);
    }// formatHour()

    /**
     * Convertit une heure au format HH:mm en minutes
     * @param strHHmm heure HH:mm
     * @return minutes
     */
    public static long hhmmToLong(String strHHmm){
        long result = 0L;
        if(strHHmm != null && !strHHmm.isEmpty()){
            String[] components = strHHmm.split(":");
            result = Long.parseLong(components[0]) * 60 + Long.parseLong(components[1]);
        }
        return result;
    }// hhmmToLong()

}// LocalTimeUtils
