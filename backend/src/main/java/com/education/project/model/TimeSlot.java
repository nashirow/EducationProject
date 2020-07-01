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

public class TimeSlot {

    private Integer id;

    /**
     * Heure de début du créneau horaire.
     */
    private LocalTime start;

    /**
     * Heure de fin du créneau horaire.
     */
    private LocalTime end;

    public TimeSlot() {
    }// TimeSlot()

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }// TimeSlot()

    public TimeSlot(Integer id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }// TimeSlot()

    public LocalTime getStart() {
        return start;
    }// getStart()

    public void setStart(LocalTime start) {
        this.start = start;
    }// setStart()

    public LocalTime getEnd() {
        return end;
    }// getEnd

    public void setEnd(LocalTime end) {
        this.end = end;
    }// setEnd()

    public Integer getId() {
        return id;
    }// getId()

    public void setId(Integer id) {
        this.id = id;
    }// setId()

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                '}';
    }// toString()

}// TimeSlot
