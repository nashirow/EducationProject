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
package com.education.project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet d'utiliser des fonctionnalités liées aux couleurs des matières.
 */
public class ColorUtils {

    private static final String hexaPatern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    /**
     * Cette fonction permet de tester si le paramètre est bien une couleur en hexadecimal.
     * @param value La valeur à tester.
     * @return boolean.
     */
    public static boolean isHex(String value){
        if(value == null){
            return false;
        }
        Pattern hexPatern = Pattern.compile(hexaPatern);
        Matcher matchPatern = hexPatern.matcher(value);
        return matchPatern.matches();
    }//isHex()
}//ColorUtils
