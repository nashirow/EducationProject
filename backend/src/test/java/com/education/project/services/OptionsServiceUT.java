package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Options;
import com.education.project.persistence.OptionsRepository;
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
public class OptionsServiceUT {

    private OptionsService optionsService;

    @Mock
    private OptionsRepository optionsRepository;

    private Options fullOptions;

    @Before
    public void setUp() throws DataBaseException {
        this.optionsService = new OptionsService(optionsRepository);
        this.fullOptions = new Options(60, LocalTime.of(9, 0), LocalTime.of(19, 0));
        Mockito.when(optionsRepository.update(this.fullOptions)).thenReturn(true);
    }// setUp()

    @Test
    public void change_options_should_success_when_split_planning_is_60_and_start_hour_is_9am_and_end_hour_is_19pm() throws ArgumentException, DataBaseException {
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_split_planning_is_60_and_start_hour_is_9am_and_end_hour_is_19pm()

    @Test
    public void change_options_should_return_false_when_no_arguments_given() throws ArgumentException, DataBaseException {
        this.fullOptions = new Options(null, null, null);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isFalse();
    }// change_options_should_return_false_when_no_arguments_given()

    @Test
    public void change_options_should_throw_argument_exception_when_options_is_null(){
        this.fullOptions = new Options(null, null, null);
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(null))
                    .isInstanceOf(ArgumentException.class)
                    .hasMessage("Options is missing");
    }// change_options_should_throw_argument_exception_when_options_is_null()

    @Test
    public void change_options_should_success_when_split_planning_is_30_and_start_hour_is_9am_and_end_hour_is_19pm() throws ArgumentException, DataBaseException {
        this.fullOptions.setSplitPlanning(30);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_split_planning_is_30_and_start_hour_is_9am_and_end_hour_is_19pm()

    @Test
    public void change_options_should_success_when_split_planning_is_20_and_start_hour_is_9am_and_end_hour_is_19pm() throws ArgumentException, DataBaseException {
        this.fullOptions.setSplitPlanning(20);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_split_planning_is_20_and_start_hour_is_9am_and_end_hour_is_19pm()

    @Test
    public void change_options_should_success_when_split_planning_is_15_and_start_hour_is_9am_and_end_hour_is_19pm() throws ArgumentException, DataBaseException {
        this.fullOptions.setSplitPlanning(15);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_split_planning_is_15_and_start_hour_is_9am_and_end_hour_is_19pm()

    @Test
    public void change_options_should_success_when_split_planning_is_60() throws ArgumentException, DataBaseException {
        this.fullOptions.setStartHourPlanning(null);
        this.fullOptions.setEndHourPlanning(null);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_split_planning_is_60()

    @Test
    public void change_options_should_success_when_start_hour_is_9am_and_existant_end_hour_is_19pm() throws ArgumentException, DataBaseException {
        this.fullOptions.setSplitPlanning(null);
        this.fullOptions.setEndHourPlanning(null);
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_start_hour_is_9am_and_existant_end_hour_is_19pm()

    @Test
    public void change_options_should_success_when_end_hour_is_15pm_and_existant_start_hour_is_5am() throws DataBaseException, ArgumentException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(18,0));
        this.fullOptions.setStartHourPlanning(null);
        this.fullOptions.setEndHourPlanning(LocalTime.of(15, 0));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        Assertions.assertThatCode(() -> this.optionsService.changeOptions(this.fullOptions)).doesNotThrowAnyException();
        Assertions.assertThat(this.optionsService.changeOptions(this.fullOptions)).isTrue();
    }// change_options_should_success_when_end_hour_is_15pm_and_existant_start_hour_is_5am()

    @Test
    public void change_options_should_throw_argument_exception_when_split_planning_is_45(){
        this.fullOptions.setSplitPlanning(45);
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le découpage du planning ne peut se faire qu'avec les valeurs suivantes (exprimées en minutes) : [15, 20, 30, 60]");
    }// change_options_should_throw_argument_exception_when_split_planning_is_45()

    @Test
    public void change_options_should_throw_argument_exception_when_start_hour_is_gte_end_hour(){
        this.fullOptions.setStartHourPlanning(LocalTime.of(22, 0));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
    }// change_options_should_throw_argument_exception_when_start_hour_is_gte_end_hour()

    @Test
    public void change_options_should_throw_argument_exception_when_start_hour_is_gte_existant_end_hour() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        this.fullOptions.setStartHourPlanning(LocalTime.of(18, 0));
        this.fullOptions.setEndHourPlanning(null);
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
    }// change_options_should_throw_argument_exception_when_start_hour_is_gte_existant_end_hour()

    @Test
    public void change_options_should_throw_argument_exception_when_end_hour_is_lte_existant_start_hour() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        this.fullOptions.setStartHourPlanning(null);
        this.fullOptions.setEndHourPlanning(LocalTime.of(3, 0));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure du début du planning ne doit pas être supérieur ou égal à l'heure de fin du planning");
    }// change_options_should_throw_argument_exception_when_end_hour_is_lte_existant_start_hour()

    @Test
    public void change_options_should_throw_argument_exception_when_start_hour_and_end_hour_doesnt_respect_split_planning_time(){
        this.fullOptions.setStartHourPlanning(LocalTime.of(8, 11));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
    }// change_options_should_throw_argument_exception_when_start_hour_and_end_hour_doesnt_respect_split_planning_time()

    @Test
    public void change_options_should_throw_argument_exception_when_start_hour_and_existant_end_hour_doesnt_respect_split_planning_time() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        this.fullOptions.setEndHourPlanning(null);
        this.fullOptions.setStartHourPlanning(LocalTime.of(5, 23));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
    }// change_options_should_throw_argument_exception_when_start_hour_and_existant_end_hour_doesnt_respect_split_planning_time()

    @Test
    public void change_options_should_throw_argument_exception_when_end_hour_and_existant_start_hour_doesnt_respect_split_planning_time() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        this.fullOptions.setStartHourPlanning(null);
        this.fullOptions.setEndHourPlanning(LocalTime.of(5, 23));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));
        Assertions.assertThatThrownBy(() -> this.optionsService.changeOptions(this.fullOptions))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'heure de fin du planning et son heure de début doivent être cohérents avec le découpage du planning");
    }// change_options_should_throw_argument_exception_when_end_hour_and_existant_start_hour_doesnt_respect_split_planning_time()

    /**
     * En dehors des problèmes réseaux / infrastructure, cette fonction doit
     * toujours retourner les options générales de l'application, elle ne doit
     * jamais faillir.
     */
    @Test
    public void get_options_should_always_success() throws DataBaseException {
        Options optionsFromBD = new Options(60, LocalTime.of(5, 0), LocalTime.of(15,0));
        Mockito.when(optionsRepository.getOptions()).thenReturn(Optional.of(optionsFromBD));

        Optional<Options> options = optionsService.getOptions();
        Assertions.assertThat(options).isPresent();
        options.ifPresent(option -> {
            Assertions.assertThat(option.getSplitPlanning()).isEqualTo(optionsFromBD.getSplitPlanning());
            Assertions.assertThat(option.getStartHourPlanning()).isEqualTo(optionsFromBD.getStartHourPlanning());
            Assertions.assertThat(option.getEndHourPlanning()).isEqualTo(optionsFromBD.getEndHourPlanning());
        });
    }// get_options_should_always_success()

}// OptionsServiceUT
