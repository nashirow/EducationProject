package com.education.project.utils;

public class LocalTimeUtils {

    public static String formatTime(int time){
        if(time < 10){
            return "0" + time;
        }
        return String.valueOf(time);
    }// formatHour()
}
