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
import com.education.project.persistence.SlotRepository;
import com.education.project.utils.ColorUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class SlotServiceUT {

    private SlotService slotService;
    private Slot slotToInsert;
    private Slot slotFromBd;
    private Slot slotToGet;
    private Slot slotToUpdate;
    private Slot slotToDelete;

    @Mock
    private SlotRepository slotRepository;

    @Before
    public void setUp() throws DataBaseException {
        Date now = new Date();
        this.slotService = new SlotService(slotRepository);
        TimeSlot timeSlot = new TimeSlot(1,LocalTime.of(8,0),LocalTime.of(10,0));
        Matiere matiere = new Matiere("Mathématiques",null,null);
        matiere.setId(1);
        this.slotToInsert = new Slot(null,null,null,"#FF0000","#000",timeSlot,matiere);
        this.slotToInsert.setJour(new Jour(1, "Lundi"));
        this.slotFromBd = this.slotToInsert;
        this.slotFromBd.setId(1);
        this.slotFromBd.setCreationDate(now);
        this.slotFromBd.setModificationDate(now);
        this.slotToUpdate = this.slotToInsert;
        this.slotToUpdate.setId(1);
        this.slotToUpdate.setCreationDate(new Date(1593705884));
        this.slotToUpdate.setModificationDate(now);
        this.slotToUpdate.getMatiere().setNom("Français");
        this.slotToDelete = this.slotToInsert;
        this.slotToDelete.setId(1);
        this.slotToGet = this.slotToInsert;
        this.slotToGet.setId(1);
        this.slotToGet.setCreationDate(new Date(1593705884));
        this.slotToGet.setModificationDate(now);

        Mockito.when(slotRepository.countByJour(slotToInsert.getJour().getId(), slotToInsert.getTimeSlot()))
                .thenReturn(1L);
        Mockito.when(slotRepository.countByJour(slotToUpdate.getJour().getId(), slotToUpdate.getTimeSlot()))
                .thenReturn(1L);
    }//setUp()

    @Test
    public void create_slot_should_success_when_params_are_timeslot_is_between_8_and_10_and_matiere_is_mathematiques_with_red_background_color_and_black_font_color() throws ArgumentException, DataBaseException {
        Mockito.when(slotRepository.insert(slotToInsert)).thenReturn(Optional.of(slotFromBd));
        Optional<Slot> optSlot = slotService.insertSlot(slotToInsert);
        Assertions.assertThat(optSlot).isPresent();
        optSlot.ifPresent(slot -> {
            Assertions.assertThat(slot.getId()).isEqualTo(1);
            Assertions.assertThat(slot.getCreationDate()).isNotNull();
            Assertions.assertThat(slot.getModificationDate()).isNotNull();
            Assertions.assertThat(slot.getJour()).isNotNull();
            Assertions.assertThat(slot.getJour().getId()).isEqualTo(1L);
            Assertions.assertThat(slot.getJour().getNom()).isEqualTo("Lundi");
            Assertions.assertThat(slot.getTimeSlot()).isNotNull();
            Assertions.assertThat(slot.getTimeSlot().getStart()).isEqualTo(this.slotFromBd.getTimeSlot().getStart());
            Assertions.assertThat(slot.getTimeSlot().getEnd()).isEqualTo(this.slotFromBd.getTimeSlot().getEnd());
            Assertions.assertThat(slot.getMatiere().getNom()).isEqualTo(this.slotFromBd.getMatiere().getNom());
            Assertions.assertThat(slot.getCouleurFond()).isEqualTo(this.slotFromBd.getCouleurFond());
            Assertions.assertThat(slot.getCouleurPolice()).isEqualTo(this.slotFromBd.getCouleurPolice());
            Assertions.assertThat(slot.getCreationDate()).isEqualTo(slot.getModificationDate());
        });
    }//create_slot_should_success_when_params_are_timeslot_is_between_8_and_10_and_matiere_is_mathematiques_with_red_background_color_and_black_font_color()

    @Test
    public void create_slot_should_throw_exception_when_slot_is_null(){
        this.slotToInsert = null;
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Le slot est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_slot_is_null()

    @Test
    public void create_slot_should_throw_exception_when_jour_is_null(){
        this.slotToInsert.setJour(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Le jour est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_jour_is_null()

    @Test
    public void create_slot_should_throw_exception_when_jour_id_is_null(){
        this.slotToInsert.getJour().setId(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Le jour est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_jour_id_is_null()

    @Test
    public void create_slot_should_throw_exception_when_matiere_is_null(){
        this.slotToInsert.setMatiere(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("La matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_matiere_is_null()

    @Test
    public void create_slot_should_throw_exception_when_matiere_id_is_null(){
        this.slotToInsert.getMatiere().setId(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("L'identifiant de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_matiere_id_is_null()

    @Test
    public void create_slot_should_throw_exception_when_timeslot_is_null(){
        this.slotToInsert.setTimeSlot(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Le créneau horaire est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_timeslot_is_null()

    @Test
    public void create_slot_should_throw_exception_when_timeslot_id_is_null(){
        this.slotToInsert.getTimeSlot().setId(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("L'identifiant du créneau horaire est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_timeslot_id_is_null()

    @Test
    public void create_slot_should_throw_exception_when_count_slots_with_same_timeslot_and_same_day_is_gt_2() throws DataBaseException {
        Mockito.when(slotRepository.countByJour(this.slotToInsert.getJour().getId(), this.slotToInsert.getTimeSlot()))
                .thenReturn(3L);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Le nombre de slots pour le même jour et le même créneau horaire est limité à 2 slots")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_count_slots_with_same_timeslot_and_same_day_is_gt_2()

    @Test
    public void create_slot_should_throw_exception_when_color_background_is_null(){
        this.slotToInsert.setCouleurFond(null);
        Assertions.assertThatThrownBy(()-> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_color_background_is_null()

    @Test
    public void create_slot_should_throw_exception_when_color_background_is_empty(){
        this.slotToInsert.setCouleurFond("");
        Assertions.assertThatThrownBy(()-> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_color_background_is_empty()


    @Test
    public void create_slot_should_throw_exception_when_color_font_is_null(){
        this.slotToInsert.setCouleurPolice(null);
        Assertions.assertThatThrownBy(()-> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_color_font_is_null()

    @Test
    public void create_slot_should_throw_exception_when_color_font_is_empty(){
        this.slotToInsert.setCouleurPolice("");
        Assertions.assertThatThrownBy(()-> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_color_font_is_empty()

    @Test
    public void create_slot_should_throw_exception_when_color_font_and_color_police_are_the_same(){
        this.slotToInsert.setCouleurFond("#fff");
        this.slotToInsert.setCouleurPolice("#fff");
        Assertions.assertThatThrownBy(()-> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de fond et de la police ne peuvent pas être la même")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_color_font_and_color_police_are_the_same()

    @Test
    public void create_slot_should_throw_exception_when_background_color_type_is_not_hexadecimal(){
        this.slotToInsert.setCouleurFond("ff#253");
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de fond doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_background_color_type_is_not_hexadecimal()

    @Test
    public void create_slot_should_throw_exception_when_police_color_type_is_not_hexadecimal(){
        this.slotToInsert.setCouleurPolice("dd#321");
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_police_color_type_is_not_hexadecimal()

    @Test
    public void create_slot_should_throw_exception_when_police_color_type_is_RGB_255_87_51(){
        this.slotToInsert.setCouleurPolice("255,87,51");
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_police_color_type_is_RGB_255_87_51()

    @Test
    public void create_slot_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p(){
        this.slotToInsert.setCouleurPolice("11,80%,60%");
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p()

    @Test
    public void create_slot_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0(){
        this.slotToInsert.setCouleurPolice("0,66,100,0");
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0()

    @Test
    public void create_slot_should_throw_exception_when_slot_fond_color_already_exists() throws DataBaseException {
        this.slotToInsert.setCouleurFond("#fff");
        Mockito.when(slotRepository.isExistByColorFondAndByDiscipline(this.slotToInsert)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Il existe déjà un slot avec ce fond de couleur")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_slot_fond_color_already_exists

    @Test
    public void get_slot_should_return_slot_when_id_is_1() throws DataBaseException {
        this.slotToGet.setId(1);
        Mockito.when(slotRepository.findById(this.slotToGet.getId())).thenReturn(Optional.of(this.slotFromBd));
        Optional<Slot> optSlot = slotService.findById(1);
        Assertions.assertThat(optSlot).isPresent();
        optSlot.ifPresent(slot -> {
            Assertions.assertThat(this.slotToGet).isNotNull();
            Assertions.assertThat(this.slotToGet.getId()).isNotNull();
            Assertions.assertThat(this.slotToGet.getId()).isEqualTo(this.slotFromBd.getId());
            Assertions.assertThat(this.slotToGet.getMatiere()).isNotNull();
            Assertions.assertThat(this.slotToGet.getMatiere().getId()).isNotNull();
            Assertions.assertThat(this.slotToGet.getMatiere().getId()).isEqualTo(this.slotFromBd.getMatiere().getId());
            Assertions.assertThat(this.slotToGet.getMatiere().getNom()).isEqualTo(this.slotFromBd.getMatiere().getNom());
            Assertions.assertThat(this.slotToGet.getJour()).isNotNull();
            Assertions.assertThat(this.slotToGet.getJour().getId()).isEqualTo(1L);
            Assertions.assertThat(this.slotToGet.getJour().getNom()).isEqualTo("Lundi");
            Assertions.assertThat(this.slotToGet.getTimeSlot()).isNotNull();
            Assertions.assertThat(this.slotToGet.getTimeSlot().getId()).isNotNull();
            Assertions.assertThat(this.slotToGet.getTimeSlot().getId()).isEqualTo(this.slotFromBd.getTimeSlot().getId());
            Assertions.assertThat(this.slotToGet.getTimeSlot().getStart()).isEqualTo(this.slotFromBd.getTimeSlot().getStart());
            Assertions.assertThat(this.slotToGet.getTimeSlot().getEnd()).isEqualTo(this.slotFromBd.getTimeSlot().getEnd());
            Assertions.assertThat(this.slotToGet.getCreationDate()).isNotNull();
            Assertions.assertThat(this.slotToGet.getModificationDate()).isNotNull();
            Assertions.assertThat(this.slotToGet.getCouleurFond()).isEqualTo(this.slotFromBd.getCouleurFond());
            Assertions.assertThat(this.slotToGet.getCouleurPolice()).isEqualTo(this.slotFromBd.getCouleurPolice());
        });
    }//get_slot_should_return_slot_when_id_is_1()

    @Test
    public void get_slot_should_return_empty_slot_when_id_is_30() throws DataBaseException {
        this.slotToGet.setId(30);
        Mockito.when(slotRepository.findById(this.slotToGet.getId())).thenReturn(Optional.empty());
        Optional<Slot> optSlot = slotService.findById(this.slotToGet.getId());
        Assertions.assertThat(optSlot).isEmpty();
    }//get_slot_should_return_empty_slot_when_id_is_30()
    @Test
    public void update_slot_should_success_when_params_are_timeslot_between_8_and_10_and_matiere_is_français_with_blue_background_color_and_black_font_color() throws ArgumentException, DataBaseException {
        this.slotFromBd.getMatiere().setNom("Français");
        Mockito.when(slotRepository.update(slotToUpdate)).thenReturn(Optional.of(slotFromBd));
        Optional<Slot> optSlot = slotService.updateSlot(this.slotToUpdate);
        Assertions.assertThat(optSlot).isPresent();
        optSlot.ifPresent(slot -> {
            Assertions.assertThat(this.slotToUpdate.getId()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getId()).isEqualTo(this.slotFromBd.getId());
            Assertions.assertThat(this.slotToUpdate.getMatiere()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getMatiere().getId()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getJour()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getJour().getId()).isEqualTo(1L);
            Assertions.assertThat(this.slotToUpdate.getJour().getNom()).isEqualTo("Lundi");
            Assertions.assertThat(this.slotToUpdate.getTimeSlot()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getTimeSlot().getId()).isNotNull();
            Assertions.assertThat(this.slotToUpdate.getTimeSlot().getId()).isEqualTo(this.slotFromBd.getTimeSlot().getId());
            Assertions.assertThat(this.slotToUpdate.getCouleurPolice()).isEqualTo(this.slotFromBd.getCouleurPolice());
            Assertions.assertThat(this.slotToUpdate.getCouleurFond()).isEqualTo(this.slotFromBd.getCouleurFond());
            Assertions.assertThat(this.slotToUpdate.getCreationDate()).isEqualTo(this.slotFromBd.getCreationDate());
            Assertions.assertThat(this.slotToUpdate.getModificationDate()).isEqualTo(this.slotFromBd.getModificationDate());
            Assertions.assertThat(this.slotToUpdate.getCreationDate()).isNotEqualTo(this.slotFromBd.getModificationDate());
        });
    }//update_slot_should_success_when_params_are_timeslot_between_8_and_10_and_matiere_is_français_with_blue_background_color_and_black_font_color()

    @Test
    public void update_slot_should_throw_exception_when_slot_is_null(){
        this.slotToUpdate = null;
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("Le slot est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_slot_is_null()

    @Test
    public void update_slot_should_throw_exception_when_slot_id_is_null(){
        this.slotToUpdate.setId(null);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("Le slot doit obligatoirement avoir un identifiant")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_slot_id_is_null()

    @Test
    public void update_slot_should_throw_exception_when_filters_matiere_id_and_timeslot_id_are_null(){
        this.slotToUpdate.getMatiere().setId(null);
        this.slotToUpdate.getTimeSlot().setId(null);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("L'identifiant de la matière est obligatoire,L'identifiant du créneau horaire est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_filters_matiere_and_timeslot_are_null()

    @Test
    public void update_slot_should_throw_exception_when_matiere_and_timeslot_are_null(){
        this.slotToUpdate.setMatiere(null);
        this.slotToUpdate.setTimeSlot(null);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La matière est obligatoire,Le créneau horaire est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_matiere_and_timeslot_are_null()

    @Test
    public void update_slot_should_throw_exception_when_background_color_is_null(){
        this.slotToUpdate.setCouleurFond(null);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_background_color_is_null()

    @Test
    public void update_slot_should_throw_exception_when_background_color_is_empty(){
        this.slotToUpdate.setCouleurFond("");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_background_color_is_empty()

    @Test
    public void update_slot_should_throw_exception_when_font_color_is_null(){
        this.slotToUpdate.setCouleurPolice(null);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_font_color_is_null()

    @Test
    public void update_slot_should_throw_exception_when_font_color_is_empty(){
        this.slotToUpdate.setCouleurPolice("");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_font_color_is_empty()

    @Test
    public void update_slot_should_throw_exception_when_background_color_and_font_color_are_the_same(){
        this.slotToUpdate.setCouleurPolice("#fff");
        this.slotToUpdate.setCouleurFond("#fff");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de fond et de la police ne peuvent pas être la même")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_background_color_and_font_color_are_the_same()

    @Test
    public void update_slot_should_throw_exception_when_background_color_is_not_hex(){
        this.slotToUpdate.setCouleurFond("255, 87, 51");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de fond doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_background_color_is_not_hex()

    @Test
    public void update_slot_should_throw_exception_when_font_color_is_not_hex(){
        this.slotToUpdate.setCouleurPolice("100,00,50");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToUpdate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_font_color_is_not_hex()

    @Test
    public void update_slot_should_throw_exception_when_police_color_type_is_RGB_255_87_51(){
        this.slotToInsert.setCouleurPolice("255,87,51");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_police_color_type_is_RGB_255_87_51()

    @Test
    public void update_slot_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p(){
        this.slotToInsert.setCouleurPolice("11,80%,60%");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p()

    @Test
    public void update_slot_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0(){
        this.slotToInsert.setCouleurPolice("0,66,100,0");
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(this.slotToInsert))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0()

    @Test
    public void delete_slot_should_sucess_when_id_is_1() throws DataBaseException {
        Mockito.when(slotRepository.deleteSlot(this.slotToDelete.getId())).thenReturn(true);
        boolean result = slotService.deleteSlot(this.slotToDelete.getId());
        Assertions.assertThat(result).isTrue();
    }//delete_slot_should_sucess_when_id_is_1()

    @Test
    public void delete_slot_should_success_when_id_is_30() throws DataBaseException {
        this.slotToDelete.setId(30);
        Mockito.when(slotRepository.deleteSlot(this.slotToDelete.getId())).thenReturn(false);
        boolean result = slotService.deleteSlot(this.slotToDelete.getId());
        Assertions.assertThat(result).isFalse();
    }//delete_slot_should_success_when_id_is_30()

    @Test
    public void count_slots_should_return_4L_when_no_filters_given() throws DataBaseException {
        Map<String,String> mapSlot = new HashMap<>();
        Mockito.when(slotRepository.countSlot(mapSlot)).thenReturn(4L);
        long result = slotService.countSlots(mapSlot);
        Assertions.assertThat(result).isEqualTo(4L);
    }//count_slots_should_return_4L_when_no_filters_given()

    @Test
    public void count_slots_should_return_1L_when_enseignant_last_name_is_Levfbre() throws DataBaseException {
        Map<String,String> mapSlot = new HashMap<>();
        mapSlot.put("nom","Levfbre");
        Mockito.when(slotRepository.countSlot(mapSlot)).thenReturn(1L);
        long result = slotService.countSlots(mapSlot);
        Assertions.assertThat(result).isEqualTo(1L);
    }//count_slots_should_return_1L_when_enseignant_name_is_paul()

    @Test
    public void count_slots_should_return_1L_when_enseignant_last_name_is_Levfbre_and_first_name_is_Paul() throws DataBaseException {
        Map<String,String> mapSlot = new HashMap<>();
        mapSlot.put("nom","Levfbre");
        mapSlot.put("prenom","Paul");
        Mockito.when(slotRepository.countSlot(mapSlot)).thenReturn(1L);
        long result = slotService.countSlots(mapSlot);
        Assertions.assertThat(result).isEqualTo(1L);
    }//count_slots_should_return_1L_when_enseignant_last_name_is_Levfbre_and_first_name_is_Paul()

    @Test
    public void count_slots_should_return_2L_when_enseignant_is_Levfbre_Paul_and_salle_name_is_b240() throws DataBaseException {
        Map<String,String> mapSlot = new HashMap<>();
        mapSlot.put("nom","Levfbre");
        mapSlot.put("prenom","Paul");
        mapSlot.put("nomSalle","b240");
        Mockito.when(slotRepository.countSlot(mapSlot)).thenReturn(2L);
        long result = slotService.countSlots(mapSlot);
        Assertions.assertThat(result).isEqualTo(2L);
    }//count_slots_should_return_2L_when_enseignant_is_Levfbre_Paul_and_salle_name_is_b240()

    @Test
    public void count_slots_should_return_1L_when_timeslot_starts_at_8_and_ends_at_10_and_enseignant_is_Laporte_Marc_and_matiere_is_Mathématiques_and_salle_is_B240_and_colorFond_is_fff_and_colorPolice_is_000() throws DataBaseException {
        Map<String,String> mapSlot = new HashMap<>();
        mapSlot.put("nom","Laporte");
        mapSlot.put("prenom","Marc");
        mapSlot.put("startHour","08:00");
        mapSlot.put("endHour","10:00");
        mapSlot.put("nomMatiere","Mathématiques");
        mapSlot.put("nomSalle","B240");
        mapSlot.put("couleurFond","#fff");
        mapSlot.put("couleurPolice","#000");
        Mockito.when(slotRepository.countSlot(mapSlot)).thenReturn(1L);
        long result = slotService.countSlots(mapSlot);
        Assertions.assertThat(result).isEqualTo(1L);
    }//count_slots_should_return_1L_when_timeslot_starts_at_8_and_ends_at_10_and_enseignant_is_Laporte_Marc_and_matiere_is_Mathématiques_and_salle_is_b240_and_colorFond_is_fff_and_colorPolice_is_000()

    @Test
    public void get_slots_should_return_all_results_when_no_filters_given() throws DataBaseException {
        Map<String,String> params = new HashMap<>();
        ColorUtils colorUtils = new ColorUtils();
        List<Slot> slotsFromBd = initSlotsFromBd();

        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(0,4));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(4);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getJour()).isNotNull();
        Assertions.assertThat(slots.get(0).getJour().getId()).isEqualTo(1L);
        Assertions.assertThat(slots.get(0).getJour().getNom()).isEqualTo("Lundi");

        Assertions.assertThat(slots.get(1)).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurFond()).isEqualTo(slotsFromBd.get(1).getCouleurFond());
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isEqualTo(slotsFromBd.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotEqualTo(slots.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getComment()).isEqualTo(slotsFromBd.get(1).getComment());
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotEqualTo(slots.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getCreationDate()).isEqualTo(slotsFromBd.get(1).getCreationDate());
        Assertions.assertThat(slots.get(1).getModificationDate()).isEqualTo(slotsFromBd.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getNom());
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isEqualTo(slotsFromBd.get(1).getMatiere().getNom());
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isEqualTo(slotsFromBd.get(1).getMatiere().getId());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotEqualTo(slots.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getId());
        Assertions.assertThat(slots.get(1).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isEqualTo(slotsFromBd.get(1).getSalle().getNom());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotEqualTo(slots.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getJour()).isNotNull();
        Assertions.assertThat(slots.get(1).getJour().getId()).isEqualTo(2L);
        Assertions.assertThat(slots.get(1).getJour().getNom()).isEqualTo("Mardi");

        Assertions.assertThat(slots.get(2)).isNotNull();
        Assertions.assertThat(slots.get(2).getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getId()).isEqualTo(slotsFromBd.get(2).getId());
        Assertions.assertThat(slots.get(2).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(2).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(2).getCouleurFond()).isEqualTo(slotsFromBd.get(2).getCouleurFond());
        Assertions.assertThat(slots.get(2).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(2).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(2).getCouleurPolice()).isEqualTo(slotsFromBd.get(2).getCouleurPolice());
        Assertions.assertThat(slots.get(2).getCouleurFond()).isNotEqualTo(slots.get(2).getCouleurPolice());
        Assertions.assertThat(slots.get(2).getComment()).isEqualTo(slotsFromBd.get(2).getComment());
        Assertions.assertThat(slots.get(2).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getCreationDate()).isNotEqualTo(slots.get(2).getModificationDate());
        Assertions.assertThat(slots.get(2).getCreationDate()).isEqualTo(slotsFromBd.get(2).getCreationDate());
        Assertions.assertThat(slots.get(2).getModificationDate()).isEqualTo(slotsFromBd.get(2).getModificationDate());
        Assertions.assertThat(slots.get(2).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getId()).isEqualTo(slotsFromBd.get(2).getId());
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(2).getEnseignant().getNom());
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(2).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(2).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(2).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(2).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(2).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(2).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isEqualTo(slotsFromBd.get(2).getMatiere().getNom());
        Assertions.assertThat(slots.get(2).getMatiere().getId()).isEqualTo(slotsFromBd.get(2).getMatiere().getId());
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isNotEqualTo(slots.get(2).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(2).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(2).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(2).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(2).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getId());
        Assertions.assertThat(slots.get(2).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(2).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isEqualTo(slotsFromBd.get(2).getSalle().getNom());
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isNotEqualTo(slots.get(2).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(2).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(2).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(2).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(2).getJour()).isNotNull();
        Assertions.assertThat(slots.get(2).getJour().getId()).isEqualTo(3L);
        Assertions.assertThat(slots.get(2).getJour().getNom()).isEqualTo("Mercredi");

        Assertions.assertThat(slots.get(3)).isNotNull();
        Assertions.assertThat(slots.get(3).getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getId()).isEqualTo(slotsFromBd.get(3).getId());
        Assertions.assertThat(slots.get(3).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(3).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(3).getCouleurFond()).isEqualTo(slotsFromBd.get(3).getCouleurFond());
        Assertions.assertThat(slots.get(3).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(3).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(3).getCouleurPolice()).isEqualTo(slotsFromBd.get(3).getCouleurPolice());
        Assertions.assertThat(slots.get(3).getCouleurFond()).isNotEqualTo(slots.get(3).getCouleurPolice());
        Assertions.assertThat(slots.get(3).getComment()).isEqualTo(slotsFromBd.get(3).getComment());
        Assertions.assertThat(slots.get(3).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getCreationDate()).isNotEqualTo(slots.get(3).getModificationDate());
        Assertions.assertThat(slots.get(3).getCreationDate()).isEqualTo(slotsFromBd.get(3).getCreationDate());
        Assertions.assertThat(slots.get(3).getModificationDate()).isEqualTo(slotsFromBd.get(3).getModificationDate());
        Assertions.assertThat(slots.get(3).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getId()).isEqualTo(slotsFromBd.get(3).getId());
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(3).getEnseignant().getNom());
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(3).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(3).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(3).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(3).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(3).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(3).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isEqualTo(slotsFromBd.get(3).getMatiere().getNom());
        Assertions.assertThat(slots.get(3).getMatiere().getId()).isEqualTo(slotsFromBd.get(3).getMatiere().getId());
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isNotEqualTo(slots.get(3).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(3).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(3).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(3).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(3).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getId());
        Assertions.assertThat(slots.get(3).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(3).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isEqualTo(slotsFromBd.get(3).getSalle().getNom());
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isNotEqualTo(slots.get(3).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(3).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(3).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(3).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(3).getJour()).isNotNull();
        Assertions.assertThat(slots.get(3).getJour().getId()).isEqualTo(4L);
        Assertions.assertThat(slots.get(3).getJour().getNom()).isEqualTo("Jeudi");
    }//get_slots_should_return_all_results_when_no_filters_given()

    @Test
    public void get_slots_should_return_results_when_filters_are_not_given_and_page_is_1_and_nbElementsPerPage_2() throws DataBaseException {
        ColorUtils colorUtils = new ColorUtils();
        Map<String,String> params = new HashMap<>();
        params.put("page","1");
        params.put("nbElementsPerPage","2");
        List<Slot> slotsFromBd = initSlotsFromBd();
        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(0,2));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(2);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getJour()).isNotNull();
        Assertions.assertThat(slots.get(0).getJour().getId()).isEqualTo(1L);
        Assertions.assertThat(slots.get(0).getJour().getNom()).isEqualTo("Lundi");

        Assertions.assertThat(slots.get(1)).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurFond()).isEqualTo(slotsFromBd.get(1).getCouleurFond());
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isEqualTo(slotsFromBd.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotEqualTo(slots.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getComment()).isEqualTo(slotsFromBd.get(1).getComment());
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotEqualTo(slots.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getCreationDate()).isEqualTo(slotsFromBd.get(1).getCreationDate());
        Assertions.assertThat(slots.get(1).getModificationDate()).isEqualTo(slotsFromBd.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getNom());
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isEqualTo(slotsFromBd.get(1).getMatiere().getNom());
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isEqualTo(slotsFromBd.get(1).getMatiere().getId());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotEqualTo(slots.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getId());
        Assertions.assertThat(slots.get(1).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isEqualTo(slotsFromBd.get(1).getSalle().getNom());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotEqualTo(slots.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getJour()).isNotNull();
        Assertions.assertThat(slots.get(1).getJour().getId()).isEqualTo(2L);
        Assertions.assertThat(slots.get(1).getJour().getNom()).isEqualTo("Mardi");
    }//get_slots_should_return_results_when_filters_are_not_given_and_page_is_1_and_nbElementsPerPage_2()

    @Test
    public void get_slots_should_return_results_when_filters_are_not_given_and_page_is_2_and_nbElementsPerPage_2() throws DataBaseException {
        ColorUtils colorUtils = new ColorUtils();
        Date now = new Date();
        Map<String,String> params = new HashMap<>();
        params.put("page","2");
        params.put("nbElementsPerPage","2");
        List<Slot> slotsFromBd = initSlotsFromBd();
        Enseignant enseignant2 = new Enseignant(3,"Lafarge","Michel",new Date(1593880801),now);
        Enseignant enseignant3 = new Enseignant(4,"Depuis","Audrey",new Date(1593880801),now);
        Matiere matiere2 = new Matiere(3,"Espagnol","10H00","C'est la matière : Espagnol",new Date(1593880801),now);
        Matiere matiere3 = new Matiere(4,"Gestion et finances","10H00","C'est la matière : Gestion et finances",new Date(1593880801),now);
        TimeSlot timeSlot2 = new TimeSlot(3,LocalTime.of(8,0),LocalTime.of(10,0));
        TimeSlot timeSlot3 = new TimeSlot(4,LocalTime.of(8,0),LocalTime.of(10,0));
        Salle salle2 = new Salle(3,"A3",new Date(1593880801),now);
        Salle salle3 = new Salle(4,"C4",new Date(1593880801),now);
        slotsFromBd.get(0).setId(3); slotsFromBd.get(0).setCouleurFond("#ddd"); slotsFromBd.get(0).setCouleurPolice("#111"); slotsFromBd.get(0).setComment("Penser à revoir les parents des élèves");
        slotsFromBd.get(0).setEnseignant(enseignant2); slotsFromBd.get(0).setMatiere(matiere2); slotsFromBd.get(0).setTimeSlot(timeSlot2); slotsFromBd.get(0).setSalle(salle2);
        slotsFromBd.get(1).setId(4); slotsFromBd.get(1).setCouleurFond("#ddd"); slotsFromBd.get(1).setCouleurPolice("#111"); slotsFromBd.get(1).setComment("Penser à revoir les parents des élèves");
        slotsFromBd.get(1).setEnseignant(enseignant3); slotsFromBd.get(1).setMatiere(matiere3); slotsFromBd.get(1).setTimeSlot(timeSlot3); slotsFromBd.get(1).setSalle(salle3);
        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(2,4));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(2);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getJour()).isNotNull();
        Assertions.assertThat(slots.get(0).getJour().getId()).isEqualTo(3L);
        Assertions.assertThat(slots.get(0).getJour().getNom()).isEqualTo("Mercredi");

        Assertions.assertThat(slots.get(1)).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurFond()).isEqualTo(slotsFromBd.get(1).getCouleurFond());
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isEqualTo(slotsFromBd.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotEqualTo(slots.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getComment()).isEqualTo(slotsFromBd.get(1).getComment());
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotEqualTo(slots.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getCreationDate()).isEqualTo(slotsFromBd.get(1).getCreationDate());
        Assertions.assertThat(slots.get(1).getModificationDate()).isEqualTo(slotsFromBd.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getNom());
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isEqualTo(slotsFromBd.get(1).getMatiere().getNom());
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isEqualTo(slotsFromBd.get(1).getMatiere().getId());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotEqualTo(slots.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getId());
        Assertions.assertThat(slots.get(1).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isEqualTo(slotsFromBd.get(1).getSalle().getNom());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotEqualTo(slots.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getJour()).isNotNull();
        Assertions.assertThat(slots.get(1).getJour().getId()).isEqualTo(4L);
        Assertions.assertThat(slots.get(1).getJour().getNom()).isEqualTo("Jeudi");
    }//get_slots_should_return_results_when_filters_are_not_given_and_page_is_2_and_nbElementsPerPage_2()

    @Test
    public void get_slots_should_return_results_when_filters_name_is_Laporte() throws DataBaseException {
        ColorUtils colorUtils = new ColorUtils();
        List<Slot> slotsFromBd = initSlotsFromBd();
        Map<String,String> params = new HashMap<>();
        params.put("nom","Laporte");
        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(0,1));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(1);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());

        Assertions.assertThat(slots.get(0).getJour()).isNotNull();
        Assertions.assertThat(slots.get(0).getJour().getId()).isEqualTo(1L);
        Assertions.assertThat(slots.get(0).getJour().getNom()).isEqualTo("Lundi");
    }//get_slots_should_return_results_when_filters_name_is_Laporte()

    @Test
    public void get_slots_should_return_result_when_timeslot_start_is_8_and_end_is_10() throws DataBaseException {
        ColorUtils colorUtils = new ColorUtils();
        List<Slot> slotsFromBd = initSlotsFromBd();
        Map<String,String> params = new HashMap<>();
        params.put("startHour","08:00:00");
        params.put("endHour","10:00:00");
        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(0,4));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(4);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());

        Assertions.assertThat(slots.get(1)).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurFond()).isEqualTo(slotsFromBd.get(1).getCouleurFond());
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(1).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(1).getCouleurPolice()).isEqualTo(slotsFromBd.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getCouleurFond()).isNotEqualTo(slots.get(1).getCouleurPolice());
        Assertions.assertThat(slots.get(1).getComment()).isEqualTo(slotsFromBd.get(1).getComment());
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getCreationDate()).isNotEqualTo(slots.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getCreationDate()).isEqualTo(slotsFromBd.get(1).getCreationDate());
        Assertions.assertThat(slots.get(1).getModificationDate()).isEqualTo(slotsFromBd.get(1).getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getId()).isEqualTo(slotsFromBd.get(1).getId());
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getNom());
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(1).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(1).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(1).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getMatiere().getNom()).isEqualTo(slotsFromBd.get(1).getMatiere().getNom());
        Assertions.assertThat(slots.get(1).getMatiere().getId()).isEqualTo(slotsFromBd.get(1).getMatiere().getId());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isNotEqualTo(slots.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(1).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(1).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(1).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getId());
        Assertions.assertThat(slots.get(1).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(1).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(1).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(1).getSalle().getNom()).isEqualTo(slotsFromBd.get(1).getSalle().getNom());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isNotEqualTo(slots.get(1).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(1).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(1).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(1).getSalle().getModificationDate());

        Assertions.assertThat(slots.get(2)).isNotNull();
        Assertions.assertThat(slots.get(2).getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getId()).isEqualTo(slotsFromBd.get(2).getId());
        Assertions.assertThat(slots.get(2).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(2).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(2).getCouleurFond()).isEqualTo(slotsFromBd.get(2).getCouleurFond());
        Assertions.assertThat(slots.get(2).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(2).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(2).getCouleurPolice()).isEqualTo(slotsFromBd.get(2).getCouleurPolice());
        Assertions.assertThat(slots.get(2).getCouleurFond()).isNotEqualTo(slots.get(2).getCouleurPolice());
        Assertions.assertThat(slots.get(2).getComment()).isEqualTo(slotsFromBd.get(2).getComment());
        Assertions.assertThat(slots.get(2).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getCreationDate()).isNotEqualTo(slots.get(2).getModificationDate());
        Assertions.assertThat(slots.get(2).getCreationDate()).isEqualTo(slotsFromBd.get(2).getCreationDate());
        Assertions.assertThat(slots.get(2).getModificationDate()).isEqualTo(slotsFromBd.get(2).getModificationDate());
        Assertions.assertThat(slots.get(2).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getId()).isEqualTo(slotsFromBd.get(2).getId());
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(2).getEnseignant().getNom());
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(2).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(2).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(2).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(2).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(2).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(2).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(2).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getMatiere().getNom()).isEqualTo(slotsFromBd.get(2).getMatiere().getNom());
        Assertions.assertThat(slots.get(2).getMatiere().getId()).isEqualTo(slotsFromBd.get(2).getMatiere().getId());
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isNotEqualTo(slots.get(2).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(2).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(2).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(2).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(2).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(2).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getId());
        Assertions.assertThat(slots.get(2).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(2).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(2).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(2).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(2).getSalle().getNom()).isEqualTo(slotsFromBd.get(2).getSalle().getNom());
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isNotEqualTo(slots.get(2).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(2).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(2).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(2).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(2).getSalle().getModificationDate());

        Assertions.assertThat(slots.get(3)).isNotNull();
        Assertions.assertThat(slots.get(3).getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getId()).isEqualTo(slotsFromBd.get(3).getId());
        Assertions.assertThat(slots.get(3).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(3).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(3).getCouleurFond()).isEqualTo(slotsFromBd.get(3).getCouleurFond());
        Assertions.assertThat(slots.get(3).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(3).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(3).getCouleurPolice()).isEqualTo(slotsFromBd.get(3).getCouleurPolice());
        Assertions.assertThat(slots.get(3).getCouleurFond()).isNotEqualTo(slots.get(3).getCouleurPolice());
        Assertions.assertThat(slots.get(3).getComment()).isEqualTo(slotsFromBd.get(3).getComment());
        Assertions.assertThat(slots.get(3).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getCreationDate()).isNotEqualTo(slots.get(3).getModificationDate());
        Assertions.assertThat(slots.get(3).getCreationDate()).isEqualTo(slotsFromBd.get(3).getCreationDate());
        Assertions.assertThat(slots.get(3).getModificationDate()).isEqualTo(slotsFromBd.get(3).getModificationDate());
        Assertions.assertThat(slots.get(3).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getId()).isEqualTo(slotsFromBd.get(3).getId());
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(3).getEnseignant().getNom());
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(3).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(3).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(3).getEnseignant().getCreationDate()).isEqualTo(slotsFromBd.get(3).getEnseignant().getCreationDate());
        Assertions.assertThat(slots.get(3).getEnseignant().getModificationDate()).isEqualTo(slotsFromBd.get(3).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(3).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getMatiere().getNom()).isEqualTo(slotsFromBd.get(3).getMatiere().getNom());
        Assertions.assertThat(slots.get(3).getMatiere().getId()).isEqualTo(slotsFromBd.get(3).getMatiere().getId());
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isNotEqualTo(slots.get(3).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(3).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(3).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(3).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(3).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(3).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getId());
        Assertions.assertThat(slots.get(3).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(3).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(3).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(3).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(3).getSalle().getNom()).isEqualTo(slotsFromBd.get(3).getSalle().getNom());
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isNotEqualTo(slots.get(3).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(3).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(3).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(3).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(3).getSalle().getModificationDate());
    }//get_slots_should_return_result_when_timeslot_start_is_8_and_end_is_10()

    @Test
    public void get_slots_should_return_results_when_param_salle_name_is_C4() throws DataBaseException {
        Date now = new Date();
        ColorUtils colorUtils = new ColorUtils();
        List<Slot> slotsFromBd = initSlotsFromBd();
        Enseignant enseignant3 = new Enseignant(4,"Depuis","Audrey",new Date(1593880801),now);
        Matiere matiere3 = new Matiere(4,"Gestion et finances","10H00","C'est la matière : Gestion et finances",new Date(1593880801),now);
        TimeSlot timeSlot3 = new TimeSlot(4,LocalTime.of(8,0),LocalTime.of(10,0));
        Salle salle3 = new Salle(4,"C4",new Date(1593880801),now);
        slotsFromBd.get(0).setId(4); slotsFromBd.get(0).setCouleurFond("#ddd"); slotsFromBd.get(0).setCouleurPolice("#111"); slotsFromBd.get(0).setComment("Penser à revoir les parents des élèves");
        slotsFromBd.get(0).setEnseignant(enseignant3); slotsFromBd.get(0).setMatiere(matiere3); slotsFromBd.get(0).setTimeSlot(timeSlot3); slotsFromBd.get(0).setSalle(salle3);
        Map<String,String> params = new HashMap<>();
        params.put("salleNom","C4");
        Mockito.when(slotRepository.getSlots(params)).thenReturn(slotsFromBd.subList(3,4));
        List<Slot> slots = slotService.getSlots(params);
        Assertions.assertThat(slots).isNotNull();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(1);

        Assertions.assertThat(slots.get(0)).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurFond())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurFond()).isEqualTo(slotsFromBd.get(0).getCouleurFond());
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isNotNull();
        Assertions.assertThat(colorUtils.isHex(slots.get(0).getCouleurPolice())).isTrue();
        Assertions.assertThat(slots.get(0).getCouleurPolice()).isEqualTo(slotsFromBd.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getCouleurFond()).isNotEqualTo(slots.get(0).getCouleurPolice());
        Assertions.assertThat(slots.get(0).getComment()).isEqualTo(slotsFromBd.get(0).getComment());
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getCreationDate()).isNotEqualTo(slots.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getCreationDate()).isEqualTo(slotsFromBd.get(0).getCreationDate());
        Assertions.assertThat(slots.get(0).getModificationDate()).isEqualTo(slotsFromBd.get(0).getModificationDate());
        Assertions.assertThat(slots.get(0).getEnseignant()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getId()).isEqualTo(slotsFromBd.get(0).getId());
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getNom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getNom());
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getEnseignant().getPrenom()).isEqualTo(slotsFromBd.get(0).getEnseignant().getPrenom());
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getEnseignant().getCreationDate()).isNotEqualTo(slots.get(0).getEnseignant().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getMatiere().getNom()).isEqualTo(slotsFromBd.get(0).getMatiere().getNom());
        Assertions.assertThat(slots.get(0).getMatiere().getId()).isEqualTo(slotsFromBd.get(0).getMatiere().getId());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isNotEqualTo(slots.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getCreationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getCreationDate());
        Assertions.assertThat(slots.get(0).getMatiere().getModificationDate()).isEqualTo(slotsFromBd.get(0).getMatiere().getModificationDate());
        Assertions.assertThat(slots.get(0).getTimeSlot()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getId()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getId());
        Assertions.assertThat(slots.get(0).getTimeSlot().getStart()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isNotNull();
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getTimeSlot().getEnd()).isEqualTo(slotsFromBd.get(0).getTimeSlot().getEnd());
        Assertions.assertThat(slots.get(0).getSalle()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getId()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isNotEmpty();
        Assertions.assertThat(slots.get(0).getSalle().getNom()).isEqualTo(slotsFromBd.get(0).getSalle().getNom());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isNotNull();
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isNotEqualTo(slots.get(0).getSalle().getModificationDate());
        Assertions.assertThat(slots.get(0).getSalle().getCreationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getCreationDate());
        Assertions.assertThat(slots.get(0).getSalle().getModificationDate()).isEqualTo(slotsFromBd.get(0).getSalle().getModificationDate());
    }//get_slots_should_return_results_when_param_salle_name_is_C4()

    private List<Slot> initSlotsFromBd(){
        Date now = new Date();
        List<Slot> slots = new ArrayList<>();
        Enseignant enseignant = new Enseignant(1,"Laporte","Marc",new Date(1593880801),now);
        Enseignant enseignant1 = new Enseignant(2,"Dupont","Phillipe",new Date(1593880801),now);
        Enseignant enseignant2 = new Enseignant(3,"Lafarge","Michel",new Date(1593880801),now);
        Enseignant enseignant3 = new Enseignant(4,"Depuis","Audrey",new Date(1593880801),now);
        Matiere matiere = new Matiere(1,"Français","10H00","C'est la matière : Français",new Date(1593880801),now);
        Matiere matiere1 = new Matiere(2,"Mathématiques","10H00","C'est la matière : Mathématiques",new Date(1593880801),now);
        Matiere matiere2 = new Matiere(3,"Espagnol","10H00","C'est la matière : Espagnol",new Date(1593880801),now);
        Matiere matiere3 = new Matiere(4,"Gestion et finances","10H00","C'est la matière : Gestion et finances",new Date(1593880801),now);
        TimeSlot timeSlot = new TimeSlot(1,LocalTime.of(8,0),LocalTime.of(10,0));
        TimeSlot timeSlot1 = new TimeSlot(2,LocalTime.of(8,0),LocalTime.of(10,0));
        TimeSlot timeSlot2 = new TimeSlot(3,LocalTime.of(8,0),LocalTime.of(10,0));
        TimeSlot timeSlot3 = new TimeSlot(4,LocalTime.of(8,0),LocalTime.of(10,0));
        Salle salle = new Salle(1,"A1",new Date(1593880801),now);
        Salle salle1 = new Salle(2,"A3",new Date(1593880801),now);
        Salle salle2 = new Salle(3,"A3",new Date(1593880801),now);
        Salle salle3 = new Salle(4,"C4",new Date(1593880801),now);
        Jour j1 = new Jour(1, "Lundi");
        Jour j2 = new Jour(2, "Mardi");
        Jour j3 = new Jour(3, "Mercredi");
        Jour j4 = new Jour(4, "Jeudi");
        Slot slot1 = new Slot(1,"Prendre les groupes identiques",new Date(1593880801),now,"#fff","#000",timeSlot,enseignant,matiere,salle);
        Slot slot2 = new Slot(2,"Penser à revoir les parents des élèves",new Date(1593880801),now,"#ddd","#111",timeSlot1,enseignant1,matiere1,salle1);
        Slot slot3 = new Slot(3,"Penser à revoir les parents des élèves",new Date(1593880801),now,"#ddd","#111",timeSlot2,enseignant2,matiere2,salle2);
        Slot slot4 = new Slot(4,"Penser à revoir les parents des élèves",new Date(1593880801),now,"#ddd","#111",timeSlot3,enseignant3,matiere3,salle3);

        slot1.setJour(j1);
        slot2.setJour(j2);
        slot3.setJour(j3);
        slot4.setJour(j4);

        slots.add(slot1);
        slots.add(slot2);
        slots.add(slot3);
        slots.add(slot4);
        return slots;
    }//initSlotsFromBd()
}//SlotServiceUT()
