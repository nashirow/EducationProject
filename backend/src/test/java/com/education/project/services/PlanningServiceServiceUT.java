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

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.*;
import com.education.project.persistence.PlanningRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class PlanningServiceServiceUT {

    private PlanningService planningService;

    @Mock
    private PlanningRepository planningRepository;

    private Planning planningToInsert;

    private Planning planningInserted;

    private Planning planningToUpdate;

    private Planning planningToDelete;

    @Before
    public void setUp() throws DataBaseException {
        this.planningService = new PlanningService(planningRepository);
        this.planningToInsert = planningToInsert();
        this.planningInserted = planningToInsert();
        this.planningToUpdate = planningToUpdate();
        this.planningInserted.setId(1);
        this.planningToDelete = planningToDelete();
        Mockito.when(this.planningRepository.insert(this.planningToInsert)).thenReturn(Optional.of(this.planningInserted));
        Mockito.when(this.planningRepository.update(this.planningToUpdate)).thenReturn(Optional.of(this.planningToUpdate));
    }// setUp()

    @Test
    public void insert_planning_should_success_with_classe_id_and_name_and_slots_id_given() throws ArgumentException, DataBaseException {
        Optional<Planning> optPlanning = this.planningService.insertPlanning(this.planningToInsert);
        Assertions.assertThat(optPlanning).isPresent();
        optPlanning.ifPresent(planning -> {
            Assertions.assertThat(planning.getId()).isEqualTo(1);
            Assertions.assertThat(planning.getNom()).isEqualTo(this.planningToInsert.getNom());
            Assertions.assertThat(planning.getClasse()).isNotNull();
            Assertions.assertThat(planning.getClasse().getNom()).isEqualTo(this.planningToInsert.getClasse().getNom());
            Assertions.assertThat(planning.getSlots()).isNotNull();
            Assertions.assertThat(planning.getSlots()).isNotEmpty();
            Assertions.assertThat(planning.getSlots()).hasSize(this.planningToInsert.getSlots().size());
            Assertions.assertThat(planning.isSaturdayUsed()).isFalse();
            Assertions.assertThat(planning.isWednesdayUsed()).isTrue();
            for(int i = 0; i < planning.getSlots().size(); ++i){
                Slot currentSlot = planning.getSlots().get(i);
                Slot slotFromPlanningToInsert = this.planningToInsert.getSlots().get(i);
                Assertions.assertThat(currentSlot.getId()).isEqualTo(slotFromPlanningToInsert.getId());
                Assertions.assertThat(currentSlot.getCouleurFond()).isEqualTo(slotFromPlanningToInsert.getCouleurFond());
                Assertions.assertThat(currentSlot.getCouleurPolice()).isEqualTo(slotFromPlanningToInsert.getCouleurPolice());
                Assertions.assertThat(currentSlot.getModificationDate()).isEqualTo(slotFromPlanningToInsert.getModificationDate());
                Assertions.assertThat(currentSlot.getCreationDate()).isEqualTo(slotFromPlanningToInsert.getCreationDate());
                Assertions.assertThat(currentSlot.getEnseignant()).isEqualTo(slotFromPlanningToInsert.getEnseignant());
                Assertions.assertThat(currentSlot.getMatiere()).isEqualTo(slotFromPlanningToInsert.getMatiere());
                Assertions.assertThat(currentSlot.getSalle()).isEqualTo(slotFromPlanningToInsert.getSalle());
                Assertions.assertThat(currentSlot.getTimeSlot()).isEqualTo(slotFromPlanningToInsert.getTimeSlot());
                Assertions.assertThat(currentSlot.getComment()).isEqualTo(slotFromPlanningToInsert.getComment());
            }
        });
    }// insert_planning_should_success_with_classe_id_and_name_and_slots_id_given()

    @Test
    public void insert_planning_should_throw_argument_exception_when_planning_is_null() throws DataBaseException {
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(null))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le planning est obligatoire");
    }// insert_planning_should_throw_argument_exception_when_planning_is_null()

    @Test
    public void insert_planning_should_throw_argument_exception_when_classe_id_is_null() throws DataBaseException {
        Classe classe = new Classe();
        this.planningToInsert.setClasse(classe);
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(this.planningToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("La classe est obligatoire");
    }// insert_planning_should_throw_argument_exception_when_classe_id_is_null()

    @Test
    public void insert_planning_should_throw_argument_exception_when_nom_is_empty() throws DataBaseException {
        this.planningToInsert.setNom("");
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(this.planningToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom du planning est obligatoire");
    }// insert_planning_should_throw_argument_exception_when_nom_is_empty()

    @Test
    public void insert_planning_should_throw_argument_exception_when_nom_is_null() throws DataBaseException {
        this.planningToInsert.setNom(null);
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(this.planningToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom du planning est obligatoire");
    }// insert_planning_should_throw_argument_exception_when_nom_is_null()

    @Test
    public void insert_planning_should_throw_argument_exception_when_slots_is_null() throws DataBaseException {
        this.planningToInsert.setSlots(null);
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(this.planningToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Un ou plusieurs slot(s) est/sont obligatoire(s)");
    }// insert_planning_should_throw_argument_exception_when_slots_is_null()

    @Test
    public void insert_planning_should_throw_argument_exception_when_slots_is_empty() throws DataBaseException {
        this.planningToInsert.setSlots(Collections.emptyList());
        Assertions.assertThatCode(() -> this.planningService.insertPlanning(this.planningToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Un ou plusieurs slot(s) est/sont obligatoire(s)");
    }// insert_planning_should_throw_argument_exception_when_slots_is_empty()

    @Test
    public void update_planning_should_success_with_classe_id_and_name_and_slots_id_given() throws ArgumentException, DataBaseException {
        Optional<Planning> optPlanning = this.planningService.updatePlanning(this.planningToUpdate);
        Assertions.assertThat(optPlanning).isPresent();
        optPlanning.ifPresent(planning -> {
            Assertions.assertThat(planning.getId()).isEqualTo(1);
            Assertions.assertThat(planning.getNom()).isEqualTo(this.planningToUpdate.getNom());
            Assertions.assertThat(planning.getClasse()).isNotNull();
            Assertions.assertThat(planning.getClasse().getNom()).isEqualTo(this.planningToUpdate.getClasse().getNom());
            Assertions.assertThat(planning.getSlots()).isNotNull();
            Assertions.assertThat(planning.getSlots()).isNotEmpty();
            Assertions.assertThat(planning.getSlots()).hasSize(this.planningToUpdate.getSlots().size());
            Assertions.assertThat(planning.getCreationDate()).isNotEqualTo(planning.getModificationDate());
            Assertions.assertThat(planning.isSaturdayUsed()).isFalse();
            Assertions.assertThat(planning.isWednesdayUsed()).isTrue();
            for(int i = 0; i < planning.getSlots().size(); ++i){
                Slot currentSlot = planning.getSlots().get(i);
                Slot slotFromPlanningToUpdate = this.planningToUpdate.getSlots().get(i);
                Assertions.assertThat(currentSlot.getId()).isEqualTo(slotFromPlanningToUpdate.getId());
                Assertions.assertThat(currentSlot.getCouleurFond()).isEqualTo(slotFromPlanningToUpdate.getCouleurFond());
                Assertions.assertThat(currentSlot.getCouleurPolice()).isEqualTo(slotFromPlanningToUpdate.getCouleurPolice());
                Assertions.assertThat(currentSlot.getModificationDate()).isEqualTo(slotFromPlanningToUpdate.getModificationDate());
                Assertions.assertThat(currentSlot.getCreationDate()).isEqualTo(slotFromPlanningToUpdate.getCreationDate());
                Assertions.assertThat(currentSlot.getEnseignant()).isEqualTo(slotFromPlanningToUpdate.getEnseignant());
                Assertions.assertThat(currentSlot.getMatiere()).isEqualTo(slotFromPlanningToUpdate.getMatiere());
                Assertions.assertThat(currentSlot.getSalle()).isEqualTo(slotFromPlanningToUpdate.getSalle());
                Assertions.assertThat(currentSlot.getTimeSlot()).isEqualTo(slotFromPlanningToUpdate.getTimeSlot());
                Assertions.assertThat(currentSlot.getComment()).isEqualTo(slotFromPlanningToUpdate.getComment());
            }
        });
    }// update_planning_should_success_with_classe_id_and_name_and_slots_id_given()

    @Test
    public void update_planning_should_throw_argument_exception_when_planning_is_null() throws DataBaseException {
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(null))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le planning est obligatoire");
    }// update_planning_should_throw_argument_exception_when_planning_is_null()

    @Test
    public void update_planning_should_throw_argument_exception_when_planning_id_is_null() throws DataBaseException {
        this.planningToUpdate.setId(null);
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'identifiant du planning est obligatoire");
    }// update_planning_should_throw_argument_exception_when_planning_id_is_null()

    @Test
    public void update_planning_should_throw_argument_exception_when_classe_id_is_null() throws DataBaseException {
        Classe classe = new Classe();
        this.planningToUpdate.setClasse(classe);
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("La classe est obligatoire");
    }// update_planning_should_throw_argument_exception_when_classe_id_is_null()

    @Test
    public void update_planning_should_throw_argument_exception_when_nom_is_empty() throws DataBaseException {
        this.planningToUpdate.setNom("");
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom du planning est obligatoire");
    }// update_planning_should_throw_argument_exception_when_nom_is_empty()

    @Test
    public void update_planning_should_throw_argument_exception_when_nom_is_null() throws DataBaseException {
        this.planningToUpdate.setNom(null);
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom du planning est obligatoire");
    }// update_planning_should_throw_argument_exception_when_nom_is_null()

    @Test
    public void update_planning_should_throw_argument_exception_when_slots_is_null() throws DataBaseException {
        this.planningToUpdate.setSlots(null);
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Un ou plusieurs slot(s) est/sont obligatoire(s)");
    }// update_planning_should_throw_argument_exception_when_slots_is_null()

    @Test
    public void update_planning_should_throw_argument_exception_when_slots_is_empty() throws DataBaseException {
        this.planningToUpdate.setSlots(Collections.emptyList());
        Assertions.assertThatCode(() -> this.planningService.updatePlanning(this.planningToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Un ou plusieurs slot(s) est/sont obligatoire(s)");
    }// update_planning_should_throw_argument_exception_when_slots_is_empty()

    @Test
    public void delete_planning_should_success_when_id_is_1() throws DataBaseException {
        Mockito.when(planningService.deletePlanning(this.planningToDelete.getId())).thenReturn(true);
        boolean result = planningService.deletePlanning(this.planningToDelete.getId());
        Assertions.assertThat(result).isTrue();
    }//delete_planning_should_success_when_id_is_1()

    @Test
    public void delete_planning_should_success_when_id_is_400() throws DataBaseException {
        this.planningToDelete.setId(400);
        boolean result = planningService.deletePlanning(this.planningToDelete.getId());
        Assertions.assertThat(result).isFalse();
    }//delete_planning_should_success_when_id_is_400

    @Test
    public void get_planning_should_success_when_id_is_1() throws DataBaseException {
        Mockito.when(this.planningRepository.findById(1)).thenReturn(getFullPlanning());
        Optional<Planning> optPlanning = this.planningService.getPlanningById(1);
        Assertions.assertThat(optPlanning).isPresent();
        optPlanning.ifPresent(planning -> {
            Assertions.assertThat(planning.getId()).isEqualTo(1);
            Assertions.assertThat(planning.getNom()).isEqualTo("P1");
            Assertions.assertThat(planning.getClasse()).isNotNull();
            Assertions.assertThat(planning.getClasse().getNom()).isEqualTo("CM1");
            Assertions.assertThat(planning.getSlots()).isNotNull();
            Assertions.assertThat(planning.getSlots()).isNotEmpty();
            Assertions.assertThat(planning.getSlots()).hasSize(1);
            Assertions.assertThat(planning.getCreationDate()).isNotEqualTo(planning.getModificationDate());
            Assertions.assertThat(planning.isSaturdayUsed()).isFalse();
            Assertions.assertThat(planning.isWednesdayUsed()).isTrue();
            for(int i = 0; i < planning.getSlots().size(); ++i){
                Slot currentSlot = planning.getSlots().get(i);
                Slot slotFromPlanningToUpdate = this.planningToUpdate.getSlots().get(i);
                Assertions.assertThat(currentSlot.getId()).isEqualTo(1);
                Assertions.assertThat(currentSlot.getCouleurFond()).isEqualTo("#ddd");
                Assertions.assertThat(currentSlot.getCouleurPolice()).isEqualTo("#ccc");
                Assertions.assertThat(currentSlot.getModificationDate()).isNotNull();
                Assertions.assertThat(currentSlot.getCreationDate()).isNotNull();
                Assertions.assertThat(currentSlot.getCreationDate()).isNotEqualTo(currentSlot.getModificationDate());
                Assertions.assertThat(currentSlot.getEnseignant()).isNull();
                Assertions.assertThat(currentSlot.getMatiere()).isNotNull();
                Assertions.assertThat(currentSlot.getMatiere().getId()).isEqualTo(1);
                Assertions.assertThat(currentSlot.getMatiere().getNom()).isEqualTo("Français");
                Assertions.assertThat(currentSlot.getSalle()).isNull();
                Assertions.assertThat(currentSlot.getTimeSlot()).isNotNull();
                Assertions.assertThat(currentSlot.getTimeSlot().getStart().getHour()).isEqualTo(8);
                Assertions.assertThat(currentSlot.getTimeSlot().getStart().getMinute()).isEqualTo(0);
                Assertions.assertThat(currentSlot.getTimeSlot().getEnd().getHour()).isEqualTo(9);
                Assertions.assertThat(currentSlot.getTimeSlot().getEnd().getMinute()).isEqualTo(0);
                Assertions.assertThat(currentSlot.getComment()).isNull();
            }
        });
    }// get_planning_should_success_when_id_is_1()

    @Test
    public void get_planning_should_be_empty_when_id_is_2() throws DataBaseException {
        Mockito.when(this.planningRepository.findById(2)).thenReturn(Optional.empty());
        Optional<Planning> optPlanning = this.planningService.getPlanningById(2);
        Assertions.assertThat(optPlanning).isNotPresent();
    }// get_planning_should_be_empty_when_id_is_2()

    @Test
    public void get_plannings_should_return_result_when_no_filters_given() throws DataBaseException {
        List<Planning> planningsFromBd = getFullPlannings();
        Mockito.when(planningRepository.getPlannings()).thenReturn(planningsFromBd);
        List<Planning> plannings = planningService.getPlannings();
        Assertions.assertThat(plannings).isNotNull();
        Assertions.assertThat(plannings).hasSize(2);

        Assertions.assertThat(plannings.get(0)).isNotNull();
        Assertions.assertThat(plannings.get(0).getId()).isNotNull();
        Assertions.assertThat(plannings.get(0).getNom()).isEqualTo(planningsFromBd.get(0).getNom());
        Assertions.assertThat(plannings.get(0).getClasse()).isNotNull();
        Assertions.assertThat(plannings.get(0).getClasse().getNom()).isEqualTo(planningsFromBd.get(0).getClasse().getNom());
        Assertions.assertThat(plannings.get(0).getSlots()).isNotNull();
        Assertions.assertThat(plannings.get(0).getSlots()).isNotEmpty();
        Assertions.assertThat(plannings.get(0).getSlots()).hasSize(1);
        Assertions.assertThat(plannings.get(0).getCreationDate()).isNotEqualTo(plannings.get(0).getModificationDate());
        Assertions.assertThat(plannings.get(0).isWednesdayUsed()).isTrue();
        Assertions.assertThat(plannings.get(0).isSaturdayUsed()).isFalse();

        Assertions.assertThat(plannings.get(1)).isNotNull();
        Assertions.assertThat(plannings.get(1).getId()).isNotNull();
        Assertions.assertThat(plannings.get(1).getNom()).isEqualTo(planningsFromBd.get(1).getNom());
        Assertions.assertThat(plannings.get(1).getClasse()).isNotNull();
        Assertions.assertThat(plannings.get(1).getClasse().getNom()).isEqualTo(planningsFromBd.get(1).getClasse().getNom());
        Assertions.assertThat(plannings.get(1).getSlots()).isNotNull();
        Assertions.assertThat(plannings.get(1).getSlots()).isNotEmpty();
        Assertions.assertThat(plannings.get(1).getSlots()).hasSize(1);
        Assertions.assertThat(plannings.get(1).getCreationDate()).isNotEqualTo(plannings.get(1).getModificationDate());
        Assertions.assertThat(plannings.get(1).isWednesdayUsed()).isTrue();
        Assertions.assertThat(plannings.get(1).isSaturdayUsed()).isFalse();
        for(int i = 0; i < plannings.get(i).getSlots().size(); ++i){
           Slot currentSlot = plannings.get(i).getSlots().get(i);
           Slot slotFromPlanningFromBd = planningsFromBd.get(i).getSlots().get(i);
            Assertions.assertThat(currentSlot.getId()).isEqualTo(slotFromPlanningFromBd.getId());
            Assertions.assertThat(currentSlot.getCouleurFond()).isEqualTo(slotFromPlanningFromBd.getCouleurFond());
            Assertions.assertThat(currentSlot.getCouleurPolice()).isEqualTo(slotFromPlanningFromBd.getCouleurPolice());
            Assertions.assertThat(currentSlot.getModificationDate()).isNotNull();
            Assertions.assertThat(currentSlot.getCreationDate()).isNotNull();
            Assertions.assertThat(currentSlot.getCreationDate()).isNotEqualTo(currentSlot.getModificationDate());
            Assertions.assertThat(currentSlot.getEnseignant()).isNull();
            Assertions.assertThat(currentSlot.getMatiere()).isNotNull();
            Assertions.assertThat(currentSlot.getMatiere().getId()).isEqualTo(slotFromPlanningFromBd.getMatiere().getId());
            Assertions.assertThat(currentSlot.getMatiere().getNom()).isEqualTo(slotFromPlanningFromBd.getMatiere().getNom());
            Assertions.assertThat(currentSlot.getSalle()).isNull();
            Assertions.assertThat(currentSlot.getTimeSlot()).isNotNull();
            Assertions.assertThat(currentSlot.getTimeSlot().getStart().getHour()).isEqualTo(slotFromPlanningFromBd.getTimeSlot().getStart().getHour());
            Assertions.assertThat(currentSlot.getTimeSlot().getStart().getMinute()).isEqualTo(slotFromPlanningFromBd.getTimeSlot().getStart().getMinute());
            Assertions.assertThat(currentSlot.getTimeSlot().getEnd().getHour()).isEqualTo(slotFromPlanningFromBd.getTimeSlot().getEnd().getHour());
            Assertions.assertThat(currentSlot.getTimeSlot().getEnd().getMinute()).isEqualTo(slotFromPlanningFromBd.getTimeSlot().getEnd().getMinute());
            Assertions.assertThat(currentSlot.getComment()).isNull();
        }
    }//get_plannings_should_return_result_when_no_filters_given()

    private Planning planningToInsert(){
        Classe classe = new Classe(1, null, null, null);
        List<Slot> slots = Stream.of(new Slot(1), new Slot(2), new Slot(3))
                .collect(Collectors.toList());
        Planning planning = new Planning("Planning 6ème A", classe, slots);
        planning.setCreationDate(new Date(1591366583));
        return planning;
    }// planningToInsert()

    private Planning planningToUpdate(){
        Classe classe = new Classe(1, null, null, null);
        List<Slot> slots = Stream.of(new Slot(1), new Slot(2), new Slot(3))
                .collect(Collectors.toList());
        Planning planning = new Planning(1, "Planning 6ème A", classe, slots);
        planning.setCreationDate(new Date(1591366583));
        planning.setModificationDate(new Date());
        return planning;
    }// planningToUpdate()

    private Planning planningToDelete(){
        Classe classe = new Classe(1,"CE2",new Date(1593962116),new Date());
        List<Slot> slots = Stream.of(new Slot(1), new Slot(2), new Slot(3))
                .collect(Collectors.toList());
        Planning planning = new Planning(1,"Planning CE2",classe,slots);
        planning.setCreationDate(new Date(1593962116));
        planning.setModificationDate(new Date());
        return planning;
    }//planningToDelete()

    private Optional<Planning> getFullPlanning(){
        Planning planning = new Planning();
        planning.setId(1);
        planning.setClasse(new Classe(1, "CM1", new Date(1591366583), new Date()));
        planning.setCreationDate(new Date(1591366583));
        planning.setModificationDate(new Date());
        planning.setNom("P1");

        Slot s1 = new Slot(1, null, new Date(1591366583), new Date(), "#ddd", "#ccc", new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(9, 0)), null, new Matiere(1, "Français", null, null, new Date(1591366583), new Date()), null);
        s1.setJour(new Jour(1, "Lundi"));
        planning.setSlots(Collections.singletonList(s1));
        return Optional.of(planning);
    }// getFullPlanning()

    private List<Planning> getFullPlannings(){
        List<Planning> plannings = new ArrayList<>();
        Planning planning = new Planning();
        planning.setId(1);
        planning.setClasse(new Classe(1, "CM1", new Date(1591366583), new Date()));
        planning.setCreationDate(new Date(1591366583));
        planning.setModificationDate(new Date());
        planning.setNom("P1");

        Slot s1 = new Slot(1, null, new Date(1591366583), new Date(), "#ddd", "#ccc", new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(9, 0)), null, new Matiere(1, "Français", null, null, new Date(1591366583), new Date()), null);
        s1.setJour(new Jour(1, "Lundi"));
        planning.setSlots(Collections.singletonList(s1));
        plannings.add(planning);

        Planning planning1 = new Planning();
        planning1.setId(1);
        planning1.setClasse(new Classe(1, "CM1", new Date(1591366583), new Date()));
        planning1.setCreationDate(new Date(1591366583));
        planning1.setModificationDate(new Date());
        planning1.setNom("P1");

        Slot s2 = new Slot(2, null, new Date(1591366584), new Date(), "#eee", "#ggg", new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(9, 0)), null, new Matiere(2, "Mathématiques", null, null, new Date(1591366584), new Date()), null);
        s2.setJour(new Jour(2, "Mardi"));
        planning1.setSlots(Collections.singletonList(s2));
        plannings.add(planning1);
        return plannings;
    }//getFullPlannings()
}// PlanningServiceServiceUT
