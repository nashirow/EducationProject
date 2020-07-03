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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public void update_slot_should_throw_exception_when_slot_fond_color_already_exists() throws DataBaseException {
        this.slotToInsert.setCouleurFond("#fff");
        Mockito.when(slotRepository.isExistByColorFond(this.slotToInsert)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> slotService.updateSlot(slotToInsert))
                .hasMessage("Il existe déjà un slot avec ce fond de couleur")
                .isInstanceOf(ArgumentException.class);
    }//update_slot_should_throw_exception_when_slot_fond_color_already_exists
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
}//SlotServiceUT()
