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
package com.education.project.model;

import java.util.List;

/**
 * Planning généré
 */
public class PlanningGenerated {

    /**
     * Identifiant du planning généré
     */
    private int id;

    /**
     * Planning généré en html
     */
    private String contentHtml;

    /**
     * Avertissements liés au non respect de certaines règles métiers
     */
    private List<String> warnings;

    public PlanningGenerated() {
    }// PlanningGenerated()

    public PlanningGenerated(int id, String contentHtml, List<String> warnings) {
        this.id = id;
        this.contentHtml = contentHtml;
        this.warnings = warnings;
    }// PlanningGenerated()

    public int getId() {
        return id;
    }// getId()

    public void setId(int id) {
        this.id = id;
    }// setId()

    public String getContentHtml() {
        return contentHtml;
    }// getContentHtml()

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }//setContentHtml()

    public List<String> getWarnings() {
        return warnings;
    }// getWarnings()

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }// setWarnings()
}// PlanningGenerated
