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
package com.education.project.enums;

/**
 * Enumération représentant un jour de la semaine.
 */
public enum JourEnum {
    LUNDI(1),
    MARDI(2),
    MERCREDI(3),
    JEUDI(4),
    VENDREDI(5),
    SAMEDI(6);

    private int val;

    JourEnum(int val) {
        this.val = val;
    }// JourEnum()

    public int getVal() {
        return val;
    }// getVal()

    public static JourEnum getEnumByVal(int val) {
        for(JourEnum j : JourEnum.values()){
            if(j.getVal() == val){
                return j;
            }
        }
        return JourEnum.LUNDI;
    }// getEnumByVal()

}// JourEnum
