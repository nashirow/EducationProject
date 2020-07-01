package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Matiere;
import com.education.project.model.Slot;
import com.education.project.model.TimeSlot;
import com.education.project.persistence.SlotRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SlotServiceUT {

    private SlotService slotService;
    private Slot slotToInsert;
    private Slot slotFromBd;

    @Mock
    private SlotRepository slotRepository;

    @Before
    public void setUp(){
        Date now = new Date();
        this.slotService = new SlotService(slotRepository);
        TimeSlot timeSlot = new TimeSlot(1,LocalTime.of(8,0),LocalTime.of(10,0));
        Matiere matiere = new Matiere("Mathématiques",null,null);
        matiere.setId(1);
        this.slotToInsert = new Slot(null,null,null,"#FF0000","#000",timeSlot,matiere);
        this.slotFromBd = this.slotToInsert;
        this.slotFromBd.setId(1);
        this.slotFromBd.setCreationDate(now);
        this.slotFromBd.setModificationDate(now);
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
    public void create_slot_should_throw_exception_when_matiere_is_null(){
        this.slotToInsert.setMatiere(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("La matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_matiere_is_null()

    @Test
    public void create_slot_should_throw_exception_when_matiere_id_does_not_exist(){
        this.slotToInsert.getMatiere().setId(null);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("L'identifiant de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_matiere_id_does_not_exist()

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
        Mockito.when(slotRepository.isExistByColorFond(this.slotToInsert)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> slotService.insertSlot(slotToInsert))
                .hasMessage("Il existe déjà un slot avec ce fond de couleur")
                .isInstanceOf(ArgumentException.class);
    }//create_slot_should_throw_exception_when_slot_fond_color_already_exists

}//SlotServiceUT()
