package com.education.project.model;

import java.time.LocalTime;

public class TimeSlot {

    /**
     * Heure de début du créneau horaire.
     */
    private LocalTime start;

    /**
     * Heure de fin du créneau horaire.
     */
    private LocalTime end;

    public TimeSlot(LocalTime start, LocalTime end) {
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

}// TimeSlot
