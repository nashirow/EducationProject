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
package com.education.project.services;

import com.education.project.enums.JourEnum;
import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.*;
import com.education.project.persistence.OptionsRepository;
import com.education.project.persistence.PlanningRepository;
import com.education.project.utils.LocalTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ce service gère les fonctionnalités liées
 * au planning.
 */
@Service
public class PlanningService {

    private PlanningRepository planningRepository;

    private OptionsRepository optionsRepository;

    private final static Logger LOGGER = LogManager.getLogger(PlanningService.class);

    @Autowired
    public PlanningService(PlanningRepository planningRepository, OptionsRepository optionsRepository) {
        this.planningRepository = planningRepository;
        this.optionsRepository = optionsRepository;
    }// PlanningService()

    /**
     * Crée un planning au niveau de l'application.
     *
     * @param planningToInsert Planning à créer
     * @return Planning créé
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Planning> insertPlanning(Planning planningToInsert) throws ArgumentException, DataBaseException {
        checkBusiness(planningToInsert, false);
        Date now = new Date();
        planningToInsert.setCreationDate(now);
        planningToInsert.setModificationDate(now);
        return planningRepository.insert(planningToInsert);
    }// insertPlanning()

    /**
     * Met à jour un planning au niveau de l'application
     *
     * @param planning Planning à mettre à jour
     * @return Planning mis à jour
     * @throws ArgumentException
     * @throws DataBaseException
     */
    public Optional<Planning> updatePlanning(Planning planning) throws ArgumentException, DataBaseException {
        checkBusiness(planning, true);
        planning.setModificationDate(new Date());
        return planningRepository.update(planning);
    }// updatePlanning()

    /**
     * Contrôle des règles métiers
     *
     * @param planning Planning à contrôler
     * @param isUpdate Indique si le contrôle concerne la mise à jour d'un planning
     * @throws ArgumentException
     */
    private void checkBusiness(Planning planning, boolean isUpdate) throws ArgumentException {
        List<String> errors = new ArrayList<>();
        if (planning == null) {
            errors.add("Le planning est obligatoire");
        } else {
            if (isUpdate && planning.getId() == null) {
                errors.add("L'identifiant du planning est obligatoire");
            }
            if (planning.getClasse() == null) {
                errors.add("La classe est obligatoire");
            }
            if (planning.getClasse() != null && planning.getClasse().getId() == null) {
                errors.add("La classe est obligatoire");
            }
            if (planning.getNom() == null || planning.getNom().isEmpty()) {
                errors.add("Le nom du planning est obligatoire");
            }
            if (planning.getSlots() == null || planning.getSlots().isEmpty()) {
                errors.add("Un ou plusieurs slot(s) est/sont obligatoire(s)");
            }
        }
        if (!errors.isEmpty()) {
            throw new ArgumentException(errors);
        }
    }// checkBusiness()

    /**
     * Récupère un planning en fonction de son identifiant
     *
     * @param id Identifiant du planning à récupérer
     * @return Planning
     */
    public Optional<Planning> getPlanningById(int id) throws DataBaseException {
        return planningRepository.findById(id);
    }// getPlanningById()

    /**
     * Cette fonction permet de supprimer un planning en base de données grâce à son identifiant passé en paramètre
     *
     * @param id Identifiant du planning à supprimer
     * @return boolean
     */
    public boolean deletePlanning(Integer id) throws DataBaseException {
        return planningRepository.deletePlanning(id);
    }//deletePlanning()

    /**
     * Cette fonction permet de retourner tous les plannings dans la base de données en fonction des paramètres (facultatifs)
     *
     * @param params paramètres du planning
     * @return Liste des plannings
     */
    public List<Planning> getPlannings(Map<String, String> params) throws DataBaseException {
        return planningRepository.getPlannings(params);
    }//getPlannings()

    /**
     * Génération du planning dont l'identifiant est donné en paramètre
     *
     * @param id Identifiant du planning
     * @return Planning généré en HTML
     * @throws DataBaseException
     */
    public String generatePlanning(int id) throws DataBaseException {
        StringBuilder sb = new StringBuilder();
        Optional<Planning> optPlanning = planningRepository.findById(id);
        Optional<Options> optOptions = optionsRepository.getOptions();
        if (optPlanning.isPresent() && optOptions.isPresent()) {
            Planning planning = optPlanning.get();
            Options options = optOptions.get();
            String[][] results = generateFirstColumn(planning, options);
            final int nbColumns = results[0].length;
            List<Integer> gardesFou = initGardesFou(nbColumns);
            Map<Integer, List<Slot>> slotsByDay = initSlotsByDay(planning);

            long minutesBetween;
            for (int i = 0, cursorColumn = 1; i < results.length; ++i, ++cursorColumn) { // colonne 0 déjà remplie
                if (slotsByDay.containsKey(cursorColumn)) {
                    List<Slot> slotsOfCurrentDay = slotsByDay.get(cursorColumn);

                    boolean firstTime = true;
                    for (int j = 0, prevIndex = 0; j < slotsOfCurrentDay.size(); ++prevIndex) {
                        if (firstTime) {
                            minutesBetween = getMinutesBetweenForFirstIteration(options, slotsOfCurrentDay, j);
                        } else {
                            minutesBetween = getMinutesBetweenForOtherIterations(slotsOfCurrentDay, j, prevIndex);
                        }

                        int rowspan = (int) minutesBetween / options.getSplitPlanning();
                        if (rowspan == 0) {
                            rowspan = 1;
                        }
                        if (firstTime) {
                            j = initFirstCells(options, results, gardesFou, cursorColumn, slotsOfCurrentDay, j, rowspan);
                            firstTime = false;
                        } else if (!firstTime && j < slotsOfCurrentDay.size()) { // On s'occupe de tous les éléments sauf les derniers slots
                            if (j != prevIndex || slotsOfCurrentDay.get(j - 1).getTimeSlot().getEnd().equals(slotsOfCurrentDay.get(j).getTimeSlot().getStart())) {
                                results[gardesFou.get(cursorColumn)][cursorColumn] = initSlot(slotsOfCurrentDay.get(j), rowspan);
                                ++j;
                                prevIndex = j - 1;
                            } else if (j == prevIndex) {
                                results[gardesFou.get(cursorColumn)][cursorColumn] = emptySlot(rowspan); // cellule vide
                            }
                            if (j == slotsOfCurrentDay.size()) { // On s'occupe des cellules entre le dernier slot et la dernière heure du planning
                                int k = gardesFou.get(cursorColumn) + rowspan;
                                for (; k < results.length; ++k) {
                                    results[k][cursorColumn] = "<td rowspan=\"" + 1 + "\"></td>";
                                }
                            }
                            gardesFou.set(cursorColumn, gardesFou.get(cursorColumn) + rowspan);
                        }
                    }
                }
            }

            boolean[] hasElementsByDay = new boolean[nbColumns];
            String bodyPlanning = generateBodyPlanning(results, hasElementsByDay);
            String headerPlanning = generateHeaderPlanning(hasElementsByDay);
            sb.append("<table>");
            sb.append(headerPlanning);
            sb.append(bodyPlanning);
            sb.append("</table>");
            LOGGER.info("Code HTML du planning généré : {}", sb.toString()); // debug
        }
        return sb.toString();
    }// generatePlanning()

    /**
     * Initialisation d'une cellule Slot
     * @param slot Slot
     * @param rowspan rowspan calculé
     * @return cellule slot
     */
    private String initSlot(Slot slot, int rowspan){
        StringBuilder sb = new StringBuilder();
        if(slot == null){
            return sb.toString();
        }
        StringBuilder style = new StringBuilder("style=\"");
        style.append("color : ").append(slot.getCouleurPolice()).append(";");
        style.append("background-color : ").append(slot.getCouleurFond()).append(";");
        style.append("\"");

        sb.append("<td ");
        sb.append(style);
        sb.append(" rowspan=\"");
        sb.append(rowspan);
        sb.append("\"><div>");
        sb.append(slot.getMatiere().getNom());
        sb.append("</div>");
        if(slot.getEnseignant() != null){
            sb.append("<div>");
            sb.append(slot.getEnseignant().getNom());
            sb.append(" ");
            sb.append(slot.getEnseignant().getPrenom());
            sb.append("</div>");
        }
        if(slot.getSalle() != null){
            sb.append("<div>");
            sb.append(slot.getSalle().getNom());
            sb.append("</div>");
        }
        sb.append("</td>");
        return sb.toString();
    }// initSlot()

    /**
     * Initialisation d'une cellule vide
     * @param rowspan rowspan calculé
     * @return cellule vide
     */
    private String emptySlot(int rowspan){
        StringBuilder sb = new StringBuilder("<td rowspan=\"");
        sb.append(rowspan);
        sb.append("\"></td>");
        return sb.toString();
    }// emptySlot()

    /**
     * Initialisation des premières cellules.
     *
     * @param options
     * @param results
     * @param gardesFou
     * @param cursorColumn
     * @param slotsOfCurrentDay
     * @param j
     * @param rowspan
     * @return indice incrémenté (ou non)
     */
    private int initFirstCells(Options options, String[][] results, List<Integer> gardesFou, int cursorColumn, List<Slot> slotsOfCurrentDay, int j, int rowspan) {
        if (options.getStartHourPlanning().equals(slotsOfCurrentDay.get(j).getTimeSlot().getStart())) {
            results[gardesFou.get(cursorColumn)][cursorColumn] = initSlot(slotsOfCurrentDay.get(j), rowspan);
            ++j;
        } else {
            results[gardesFou.get(cursorColumn)][cursorColumn] = emptySlot(rowspan); // cellule vide
        }
        gardesFou.set(cursorColumn, gardesFou.get(cursorColumn) + rowspan);
        return j;
    }// initFirstCells()

    /**
     * Récupération de la différence de minutes uniquement pour la première itération.
     *
     * @param options           Options de l'application
     * @param slotsOfCurrentDay Slots du jour courant
     * @param j                 indice du slot courant
     * @return différence de minutes
     */
    private long getMinutesBetweenForFirstIteration(Options options, List<Slot> slotsOfCurrentDay, int j) {
        long minutesBetween;
        if (options.getStartHourPlanning().equals(slotsOfCurrentDay.get(j).getTimeSlot().getStart())) {
            minutesBetween = ChronoUnit.MINUTES.between(slotsOfCurrentDay.get(j).getTimeSlot().getStart(), slotsOfCurrentDay.get(j).getTimeSlot().getEnd());
        } else {
            minutesBetween = ChronoUnit.MINUTES.between(options.getStartHourPlanning(), slotsOfCurrentDay.get(j).getTimeSlot().getStart());
        }
        return minutesBetween;
    }// getMinutesBetweenForFirstIteration()

    /**
     * Récupération de la différence de minutes pour toutes les autres itérations (sauf la première).
     *
     * @param slotsOfCurrentDay Slots du jour courant
     * @param j                 indice du slot courant
     * @param prevIndex         précédent indice (boucle)
     * @return différence de minutes
     */
    private long getMinutesBetweenForOtherIterations(List<Slot> slotsOfCurrentDay, int j, int prevIndex) {
        long minutesBetween;
        if (j != prevIndex || slotsOfCurrentDay.get(j - 1).getTimeSlot().getEnd().equals(slotsOfCurrentDay.get(j).getTimeSlot().getStart())) {
            minutesBetween = ChronoUnit.MINUTES.between(slotsOfCurrentDay.get(j).getTimeSlot().getStart(), slotsOfCurrentDay.get(j).getTimeSlot().getEnd());
        } else {
            minutesBetween = ChronoUnit.MINUTES.between(slotsOfCurrentDay.get(j - 1).getTimeSlot().getEnd(), slotsOfCurrentDay.get(j).getTimeSlot().getStart());
        }
        return minutesBetween;
    }// getMinutesBetweenForOtherIterations()

    /**
     * Génération de la première colonne du planning affichant les
     * créneaux horaires.
     *
     * @param planning Planning
     * @param options  Options de l'application
     * @return Matrice qui contiendra tout le planning (corps) généré plus tard.
     */
    private String[][] generateFirstColumn(Planning planning, Options options) {
        LocalTime cursor = options.getStartHourPlanning();
        int nbRows = 0, nbCols = 7; // col vide + lundi, mardi, mercredi, jeudi, vendredi, samedi
        while (!cursor.equals(options.getEndHourPlanning())) {
            ++nbRows;
            cursor = cursor.plusMinutes(options.getSplitPlanning());
        }
        if (!planning.isWednesdayUsed()) {
            --nbCols;
        }
        if (!planning.isSaturdayUsed()) {
            --nbCols;
        }
        String[][] matrice = new String[nbRows][nbCols];
        int i = 0;
        cursor = options.getStartHourPlanning();
        while (!cursor.equals(options.getEndHourPlanning())) {
            matrice[i][0] = LocalTimeUtils.formatTime(cursor.getHour()) + ":" + LocalTimeUtils.formatTime(cursor.getMinute()) + " - ";
            cursor = cursor.plusMinutes(options.getSplitPlanning());
            matrice[i][0] = "<td>" + matrice[i][0] + LocalTimeUtils.formatTime(cursor.getHour()) + ":" + LocalTimeUtils.formatTime(cursor.getMinute()) + "</td>";
            ++i;
        }
        return matrice;
    }// generateFirstColumn()

    /**
     * Génération de l'en-tête du planning. Seuls les colonnes ayant au minimum
     * une valeur apparaîtront.
     *
     * @param hasElementsByDay Compteur d'éléments renseignés par jour
     * @return en-tête planning
     */
    private String generateHeaderPlanning(boolean[] hasElementsByDay) {
        StringBuilder sb = new StringBuilder();
        sb.append("<thead><tr>");
        if (hasElementsByDay != null) {
            for (int i = 0; i < hasElementsByDay.length; ++i) {
                if (hasElementsByDay[i]) {
                    String colName = "";
                    if (i > 0) {
                        colName = JourEnum.getEnumByVal(i).toString();
                    }
                    sb.append("<th>");
                    sb.append(colName);
                    sb.append("</th>");
                }
            }
        }
        sb.append("</tr></thead>");
        return sb.toString();
    }// generateHeaderPlanning()

    /**
     * Génération du corps du planning à partir de la matrice résultat.
     *
     * @param results          matrice
     * @param hasElementsByDay Compteur d'éléments renseignés par jour
     * @return corps du planning
     */
    private String generateBodyPlanning(String results[][], boolean[] hasElementsByDay) {
        StringBuilder sb = new StringBuilder();
        if (results != null && hasElementsByDay != null) {
            for (int i = 0; i < results.length; ++i) {
                sb.append("<tr>");
                for (int j = 0; j < results[0].length; ++j) {
                    String elem = results[i][j];
                    if (elem != null) {
                        hasElementsByDay[j] = true;
                        sb.append(elem);
                        sb.append(" ");
                    }
                }
                sb.append("</tr>");
            }
        }
        return sb.toString();
    }// generateBodyPlanning()

    private Map<Integer, List<Slot>> initSlotsByDay(Planning planning) {
        Map<Integer, List<Slot>> slotsByDay = new HashMap<>();
        if (planning != null) {
            Comparator<Slot> compareSlot = Comparator
                    .comparing(Slot::getJour, Comparator.comparing(Jour::getId))
                    .thenComparing(Slot::getTimeSlot, Comparator.comparing(TimeSlot::getStart));
            List<Slot> slots = planning.getSlots();
            slots.sort(compareSlot);
            slotsByDay.put(JourEnum.LUNDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.LUNDI.getVal()).collect(Collectors.toList()));
            slotsByDay.put(JourEnum.MARDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.MARDI.getVal()).collect(Collectors.toList()));
            if (planning.isWednesdayUsed()) {
                slotsByDay.put(JourEnum.MERCREDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.MERCREDI.getVal()).collect(Collectors.toList()));
            }
            slotsByDay.put(JourEnum.JEUDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.JEUDI.getVal()).collect(Collectors.toList()));
            slotsByDay.put(JourEnum.VENDREDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.VENDREDI.getVal()).collect(Collectors.toList()));
            if (planning.isSaturdayUsed()) {
                slotsByDay.put(JourEnum.SAMEDI.getVal(), slots.stream().filter(s -> s.getJour().getId() == JourEnum.SAMEDI.getVal()).collect(Collectors.toList()));
            }
        }
        return slotsByDay;
    }// initSlotsByDay()

    /**
     * Initialisation des gardes fous. Un garde fou est utilisé comme une sorte de marque page
     * pour chacun des jours du planning afin de faciliter la construction de la matrice résultat
     * représentant le corps du planning.
     *
     * @param nbColumns Nombre de colonnes du planning
     * @return gardes fous
     */
    private List<Integer> initGardesFou(int nbColumns) {
        List<Integer> gardesFou = new ArrayList<>();
        for (int i = 0; i < nbColumns; ++i) {
            gardesFou.add(0);
        }
        return gardesFou;
    }// initGardesFou()

}// PlanningService
