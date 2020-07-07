package com.education.project.enums;

public enum JourEnum {
    LUNDI(1),
    MARDI(2),
    MERCREDI(3),
    JEUDI(4),
    VENDREDI(5),
    SAMEDI(6);

    private int val;

    private JourEnum(int val) {
        this.val = val;
    }// JourEnum()

    public int getVal() {
        return val;
    }// getVal()

}// JourEnum
