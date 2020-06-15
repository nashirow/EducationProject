/*
 * Copyright 2020 Hicham AZIMANI
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
import com.education.project.model.Matiere;
import com.education.project.persistence.MatiereRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MatiereServiceUT {

    @Mock
    private MatiereRepository matiereRepository;

    private MatiereService matiereService;

    private Matiere matiereToCreate;

    @Before
    public void setup(){
         this.matiereService = new MatiereService(this.matiereRepository);
         this.matiereToCreate = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
    }//setup()

    @Test
    public void create_matiere_should_success_when_all_fields_filled() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.createMatiere(this.matiereToCreate);
        Assertions.assertThat(optMatiereCreated.isPresent()).isTrue();
        optMatiereCreated.ifPresent((matiereCreated)-> {
            Assertions.assertThat(matiereCreated).isNotNull();
            Assertions.assertThat(matiereCreated.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereCreated.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereCreated.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereCreated.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereCreated.getDescription()).isEqualTo("Prendre les élèves dyslexiques en groupe");
            Assertions.assertThat(matiereCreated.getId()).isEqualTo(1);
        });
    }//create_matiere_should_success_when_all_fields_filled()

    @Test
    public void create_matiere_should_throw_exception_when_name_is_empty(){
        this.matiereToCreate.setNom("");
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_name_is_empty()

    @Test
    public void create_matiere_should_throw_exception_when_name_is_null(){
        this.matiereToCreate.setNom(null);
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_name_is_null()

    @Test
    public void create_matiere_should_throw_exception_when_color_background_is_empty(){
        this.matiereToCreate.setCouleurFond("");
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_background_is_empty()

    @Test
    public void create_matiere_should_throw_exception_when_color_background_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_background_is_null()

    @Test
    public void create_matiere_should_throw_exception_when_color_font_is_empty(){
        this.matiereToCreate.setCouleurFond("");
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_font_is_empty()

    @Test
    public void create_matiere_should_throw_exception_when_color_font_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_font_is_null()

    @Test
    public void create_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        this.matiereToCreate.setNom(null);
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire,La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null()

    @Test
    public void create_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same(){
        this.matiereToCreate.setCouleurFond("#fff");
        this.matiereToCreate.setCouleurPolice("#fff");
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur du fond et de la police ne peuvent pas être la même")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same()

    @Test
    public void create_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal(){
        this.matiereToCreate.setCouleurFond("ff#253");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal()

    @Test
    public void create_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal(){
        this.matiereToCreate.setCouleurPolice("dd#321");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal()

    @Test
    public void create_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51(){
        this.matiereToCreate.setCouleurPolice("255,87,51");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51()

    @Test
    public void create_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p(){
        this.matiereToCreate.setCouleurPolice("11,80%,60%");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p()

    @Test
    public void create_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0(){
        this.matiereToCreate.setCouleurPolice("0,66,100,0");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0()

    @Test
    public void create_matiere_should_success_when_description_length_is_between_10_and_255() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Mathématiques");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.createMatiere(this.matiereToCreate);
        Assertions.assertThat(optMatiereCreated).isPresent();
        optMatiereCreated.ifPresent(matiereCreated -> {
            Assertions.assertThat(matiereCreated).isNotNull();
            Assertions.assertThat(matiereCreated.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereCreated.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereCreated.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereCreated.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereCreated.getDescription()).isEqualTo("Mathématiques");
            Assertions.assertThat(matiereCreated.getId()).isEqualTo(1);
        });
    }//create_matiere_should_success_when_description_length_is_between_10_and_255()

    @Test
    public void create_matiere_should_throw_exception_when_description_length_is_below_10(){
        this.matiereToCreate.setDescription("eps");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_description_length_is_below_10()

    @Test
    public void create_matiere_should_throw_exception_when_description_length_is_above_255(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300 ; ++i){
            sb.append("a");
        }
        this.matiereToCreate.setDescription(sb.toString());
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_description_length_is_above_255()

    @Test
    public void create_matiere_should_success_when_name_matiere_is_between_3_and_40() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Mathématiques");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.createMatiere(this.matiereToCreate);
        Assertions.assertThat(optMatiereCreated).isPresent();
        optMatiereCreated.ifPresent(matiereCreated -> {
            Assertions.assertThat(matiereCreated).isNotNull();
            Assertions.assertThat(matiereCreated.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereCreated.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereCreated.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereCreated.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereCreated.getDescription()).isEqualTo("Mathématiques");
            Assertions.assertThat(matiereCreated.getId()).isEqualTo(1);
        });
    }

    @Test
    public void create_matiere_should_throw_exception_when_name_matiere_is_below_3(){
        this.matiereToCreate.setNom("DS");
        Assertions.assertThatThrownBy(()-> matiereService.createMatiere((this.matiereToCreate)))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_name_matiere_is_below_3()

    @Test
    public void create_matiere_should_throw_exception_when_name_matiere_is_above_40(){
        this.matiereToCreate.setNom("Matière sciences de la vie et de la Terre Option Géologie et études des régions en pleines");
        Assertions.assertThatThrownBy(() -> matiereService.createMatiere(this.matiereToCreate))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//create_matiere_should_throw_exception_when_name_matiere_is_above_40()
}//MatiereServiceUT
