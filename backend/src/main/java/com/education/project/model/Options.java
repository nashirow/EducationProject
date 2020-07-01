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
package com.education.project.model;

import java.time.LocalTime;

/**
 * Cette classe représente toutes les options de l'application.
 */
public class Options {

    /**
     * Temps de découpage du planning.
     * Valeurs possibles : 15 minutes, 20 minutes, 30 minutes et 60 minutes (1H).
     * Par défaut, sa valeur est à 60 minutes.
     */
    private Integer splitPlanning;

    /**
     * Heure de début du planning. Par défaut, l'heure est à 8H00.
     */
    private LocalTime startHourPlanning;

    /**
     * Heure de fin du planning. Par défaut, l'heure est à 17H00.
     */
    private LocalTime endHourPlanning;

    public Options(Integer splitPlanning, LocalTime startHourPlanning, LocalTime endHourPlanning) {
        this.splitPlanning = splitPlanning;
        this.startHourPlanning = startHourPlanning;
        this.endHourPlanning = endHourPlanning;
    }// Options()

    public Integer getSplitPlanning() {
        return splitPlanning;
    }// getSplitPlanning()

    public void setSplitPlanning(Integer splitPlanning) {
        this.splitPlanning = splitPlanning;
    }// setSplitPlanning()

    public LocalTime getStartHourPlanning() {
        return startHourPlanning;
    }// getStartHourPlanning()

    public void setStartHourPlanning(LocalTime startHourPlanning) {
        this.startHourPlanning = startHourPlanning;
    }// setStartHourPlanning()

    public LocalTime getEndHourPlanning() {
        return endHourPlanning;
    }// getEndHourPlanning()

    public void setEndHourPlanning(LocalTime endHourPlanning) {
        this.endHourPlanning = endHourPlanning;
    }// setEndHourPlanning()

}// Options
