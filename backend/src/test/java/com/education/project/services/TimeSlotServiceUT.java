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
import com.education.project.model.Options;
import com.education.project.model.TimeSlot;
import com.education.project.persistence.OptionsRepository;
import com.education.project.persistence.TimeSlotRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(MockitoJUnitRunner.class)
public class TimeSlotServiceUT {

    private TimeSlotService timeSlotService;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private OptionsRepository optionsRepository;

    private TimeSlot ts;

    @Before
    public void setUp() throws DataBaseException {
        this.timeSlotService = new TimeSlotService(timeSlotRepository, optionsRepository);
        this.ts = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(10, 0));
        TimeSlot tsFromBD = new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(10, 0));
        Mockito.when(timeSlotRepository.insert(this.ts)).thenReturn(Optional.of(tsFromBD));
    }// setUp()

    @Test
    public void insert_time_slot_should_success_with_start_hour_lt_end_hour() throws DataBaseException, ArgumentException {
        Optional<TimeSlot> optResult = this.timeSlotService.insert(this.ts);
        Assertions.assertThat(optResult).isPresent();
        optResult.ifPresent(result -> {
            Assertions.assertThat(result.getId()).isEqualTo(1);
            Assertions.assertThat(result.getStart()).isEqualTo(ts.getStart());
            Assertions.assertThat(result.getEnd()).isEqualTo(ts.getEnd());
        });
    }// insert_time_slot_should_success_with_start_hour_lt_end_hour()

    @Test
    public void insert_time_slot_should_throw_argument_exception_with_start_hour_gte_end_hour() {
        TimeSlot ts = new TimeSlot(LocalTime.of(19, 0), LocalTime.of(17, 0));
        Assertions.assertThatCode(() -> this.timeSlotService.insert(ts))
                .hasMessage("L'heure de début ne doit pas être supérieur ou égal à l'heure de fin")
                .isInstanceOf(ArgumentException.class);
    }// insert_time_slot_should_throw_argument_exception_with_start_hour_gte_end_hour()

    @Test
    public void insert_time_slot_should_throw_argument_exception_when_time_slot_is_null() {
        Assertions.assertThatCode(() -> this.timeSlotService.insert(null))
                .hasMessage("Créneau horaire manquant")
                .isInstanceOf(ArgumentException.class);
    }// insert_time_slot_should_throw_argument_exception_when_time_slot_is_null()

    @Test
    public void insert_time_slot_should_throw_argument_exception_when_time_slot_is_already_exists() throws DataBaseException {
        Mockito.when(this.timeSlotRepository.exists(this.ts)).thenReturn(true);
        Assertions.assertThatCode(() -> this.timeSlotService.insert(this.ts))
                .hasMessage("Ce créneau horaire existe déjà")
                .isInstanceOf(ArgumentException.class);
    }// insert_time_slot_should_throw_argument_exception_when_time_slot_is_already_exists()

    @Test
    public void insert_time_slot_should_throw_argument_exception_when_start_hour_is_null_or_end_hour_is_null() {
        Assertions.assertThatCode(() -> this.timeSlotService.insert(new TimeSlot(null, LocalTime.of(17, 0))))
                .hasMessage("L'heure de début et l'heure de fin sont obligatoires")
                .isInstanceOf(ArgumentException.class);
        Assertions.assertThatCode(() -> this.timeSlotService.insert(new TimeSlot(LocalTime.of(17, 0), null)))
                .hasMessage("L'heure de début et l'heure de fin sont obligatoires")
                .isInstanceOf(ArgumentException.class);
        Assertions.assertThatCode(() -> this.timeSlotService.insert(new TimeSlot(null, null)))
                .hasMessage("L'heure de début et l'heure de fin sont obligatoires")
                .isInstanceOf(ArgumentException.class);
    }// insert_time_slot_should_throw_argument_exception_when_start_hour_is_null_or_end_hour_is_null()

    @Test
    public void insert_time_slot_should_throw_argument_exception_when_start_hour_and_end_hour_dont_respect_split_planning_time_when_split_is_1() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        TimeSlot ts = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(8, 30));
        Assertions.assertThatCode(() -> this.timeSlotService.insert(ts))
                .hasMessage("L'heure de fin et l'heure de début doivent être cohérents avec le découpage du planning")
                .isInstanceOf(ArgumentException.class);
    }// insert_time_slot_should_throw_argument_exception_when_start_hour_and_end_hour_dont_respect_split_planning_time_when_split_is_1()

    @Test
    public void count_time_slots_should_return_2() throws DataBaseException {
        Mockito.when(this.timeSlotRepository.count()).thenReturn(2L);
        Assertions.assertThat(this.timeSlotService.count()).isEqualTo(2);
    }// count_time_slots_should_return_2()

    @Test
    public void delete_time_slot_should_return_true_if_id_is_1() throws DataBaseException {
        Mockito.when(timeSlotRepository.delete(1)).thenReturn(true);
        Assertions.assertThat(timeSlotService.delete(1)).isTrue();
    }//delete_time_slot_should_return_true_if_id_is_1()

    @Test
    public void delete_time_slot_should_return_false_if_id_is_2() throws DataBaseException {
        Assertions.assertThat(timeSlotService.delete(1)).isFalse();
    }//delete_time_slot_should_return_false_if_id_is_2()

    @Test
    public void get_time_slots_should_return_2_time_slots_without_pagination() throws DataBaseException{
       List<TimeSlot> mockTimeSlots = mockTimeSlotsFromDataBase();
        Mockito.when(timeSlotRepository.findAll(null, null)).thenReturn(mockTimeSlots);
        List<TimeSlot> timeSlotList = timeSlotService.getTimeSlots(null, null);
        Assertions.assertThat(timeSlotList).isNotEmpty();
        Assertions.assertThat(timeSlotList).isNotNull();
        Assertions.assertThat(timeSlotList).hasSize(2);
        for(int i = 0; i < 2; ++i){
            Assertions.assertThat(timeSlotList.get(i).getId()).isEqualTo(mockTimeSlots.get(i).getId());
            Assertions.assertThat(timeSlotList.get(i).getStart()).isEqualTo(mockTimeSlots.get(i).getStart());
            Assertions.assertThat(timeSlotList.get(i).getEnd()).isEqualTo(mockTimeSlots.get(i).getEnd());
        }
    }// get_time_slots_should_return_2_time_slots_without_pagination()

    @Test
    public void get_time_slots_should_return_2_time_slots_with_pagination_1_element_per_page() throws DataBaseException{
        List<TimeSlot> mockTimeSlots = mockTimeSlotsFromDataBase();
        Mockito.when(timeSlotRepository.findAll(1, 1)).thenReturn(mockTimeSlots.subList(0, 1));
        List<TimeSlot> timeSlotList = timeSlotService.getTimeSlots(1, 1);
        Assertions.assertThat(timeSlotList).isNotEmpty();
        Assertions.assertThat(timeSlotList).isNotNull();
        Assertions.assertThat(timeSlotList).hasSize(1);
        Assertions.assertThat(timeSlotList.get(0).getId()).isEqualTo(mockTimeSlots.get(0).getId());
        Assertions.assertThat(timeSlotList.get(0).getStart()).isEqualTo(mockTimeSlots.get(0).getStart());
        Assertions.assertThat(timeSlotList.get(0).getEnd()).isEqualTo(mockTimeSlots.get(0).getEnd());

        Mockito.when(timeSlotRepository.findAll(2, 1)).thenReturn(mockTimeSlots.subList(1, 2));
        timeSlotList = timeSlotService.getTimeSlots(2, 1);
        Assertions.assertThat(timeSlotList).isNotEmpty();
        Assertions.assertThat(timeSlotList).isNotNull();
        Assertions.assertThat(timeSlotList).hasSize(1);
        Assertions.assertThat(timeSlotList.get(0).getId()).isEqualTo(mockTimeSlots.get(1).getId());
        Assertions.assertThat(timeSlotList.get(0).getStart()).isEqualTo(mockTimeSlots.get(1).getStart());
        Assertions.assertThat(timeSlotList.get(0).getEnd()).isEqualTo(mockTimeSlots.get(1).getEnd());
    }// get_time_slots_should_return_2_time_slots_without_pagination()

    private List<TimeSlot> mockTimeSlotsFromDataBase(){
        return Stream.of(new TimeSlot(1, LocalTime.of(8, 0), LocalTime.of(9, 0)),
                new TimeSlot(2, LocalTime.of(9, 0), LocalTime.of(12, 0))
                ).collect(Collectors.toList());
    }// mockTimeSlotsFromDataBase()

}// TimeSlotServiceUT
