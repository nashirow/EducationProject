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
import com.education.project.model.Classe;
import com.education.project.model.Planning;
import com.education.project.model.Slot;
import com.education.project.persistence.PlanningRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Before
    public void setUp() throws DataBaseException {
        this.planningService = new PlanningService(planningRepository);
        this.planningToInsert = planningToInsert();
        this.planningInserted = planningToInsert();
        this.planningToUpdate = planningToUpdate();
        this.planningInserted.setId(1);
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

}// PlanningServiceServiceUT
