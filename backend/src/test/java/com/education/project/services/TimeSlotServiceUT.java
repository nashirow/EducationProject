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
import java.util.Optional;


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

}// TimeSlotServiceUT
