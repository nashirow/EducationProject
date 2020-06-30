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
