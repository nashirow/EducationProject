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

import java.util.Date;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MatiereServiceUT {

    @Mock
    private MatiereRepository matiereRepository;

    private MatiereService matiereService;

    private Matiere matiereToCreate;

    private Matiere matiereToUpdate;

    @Before
    public void setup(){
         this.matiereService = new MatiereService(this.matiereRepository);
         this.matiereToCreate = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
         this.matiereToUpdate = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
         this.matiereToUpdate.setId(1);
         this.matiereToUpdate.setCreationDate(new Date());
         this.matiereToUpdate.setModificationDate(new Date());
    }//setup()

    @Test
    public void insert_matiere_should_success_when_all_fields_filled() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.insertMatiere(this.matiereToCreate);
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
    }//insert_matiere_should_success_when_all_fields_filled()

    @Test
    public void insert_matiere_should_throw_exception_when_name_is_empty(){
        this.matiereToCreate.setNom("");
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_name_is_empty()

    @Test
    public void insert_matiere_should_throw_exception_when_name_is_null(){
        this.matiereToCreate.setNom(null);
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_name_is_null()

    @Test
    public void insert_matiere_should_throw_exception_when_color_background_is_empty(){
        this.matiereToCreate.setCouleurFond("");
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_background_is_empty()

    @Test
    public void insert_matiere_should_throw_exception_when_color_background_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_background_is_null()

    @Test
    public void insert_matiere_should_throw_exception_when_color_font_is_empty(){
        this.matiereToCreate.setCouleurFond("");
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_font_is_empty()

    @Test
    public void insert_matiere_should_throw_exception_when_color_font_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_font_is_null()

    @Test
    public void insert_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null(){
        this.matiereToCreate.setCouleurFond(null);
        this.matiereToCreate.setNom(null);
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("Le nom de la matière est obligatoire,La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null()

    @Test
    public void insert_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same(){
        this.matiereToCreate.setCouleurFond("#fff");
        this.matiereToCreate.setCouleurPolice("#fff");
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur du fond et de la police ne peuvent pas être la même")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same()

    @Test
    public void insert_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal(){
        this.matiereToCreate.setCouleurFond("ff#253");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de fond doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal()

    @Test
    public void insert_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal(){
        this.matiereToCreate.setCouleurPolice("dd#321");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal()

    @Test
    public void insert_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51(){
        this.matiereToCreate.setCouleurPolice("255,87,51");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51()

    @Test
    public void insert_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p(){
        this.matiereToCreate.setCouleurPolice("11,80%,60%");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p()

    @Test
    public void insert_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0(){
        this.matiereToCreate.setCouleurPolice("0,66,100,0");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0()

    @Test
    public void insert_matiere_should_success_when_description_length_is_between_10_and_255() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Mathématiques");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.insertMatiere(this.matiereToCreate);
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
    }//insert_matiere_should_success_when_description_length_is_between_10_and_255()

    @Test
    public void insert_matiere_should_throw_exception_when_description_length_is_below_10(){
        this.matiereToCreate.setDescription("eps");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_description_length_is_below_10()

    @Test
    public void insert_matiere_should_throw_exception_when_description_length_is_above_255(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300 ; ++i){
            sb.append("a");
        }
        this.matiereToCreate.setDescription(sb.toString());
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_description_length_is_above_255()

    @Test
    public void insert_matiere_should_success_when_name_matiere_is_between_3_and_40() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Mathématiques");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereCreated = matiereService.insertMatiere(this.matiereToCreate);
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
    public void insert_matiere_should_throw_exception_when_name_matiere_is_below_3(){
        this.matiereToCreate.setNom("DS");
        Assertions.assertThatThrownBy(()-> matiereService.insertMatiere((this.matiereToCreate)))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_name_matiere_is_below_3()

    @Test
    public void insert_matiere_should_throw_exception_when_name_matiere_is_above_40(){
        this.matiereToCreate.setNom("Matière sciences de la vie et de la Terre Option Géologie et études des régions en pleines");
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//insert_matiere_should_throw_exception_when_name_matiere_is_above_40()

    @Test
    public void update_matiere_should_success_when_matiere_is_modified() throws DataBaseException, ArgumentException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        matiereFromBd.setModificationDate(new Date());
        Mockito.when(matiereRepository.update(Mockito.any(Matiere.class))).thenReturn(Optional.of(matiereFromBd));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereModified = matiereService.updateMatiere(this.matiereToUpdate);
        Assertions.assertThat(optMatiereModified).isPresent();
        optMatiereModified.ifPresent( matiereModifier -> {
            Assertions.assertThat(matiereModifier).isNotNull();
            Assertions.assertThat(matiereModifier.getId()).isEqualTo(1);
            Assertions.assertThat(matiereModifier.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereModifier.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereModifier.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereModifier.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereModifier.getDescription()).isEqualTo("la classe à modifier");
            Assertions.assertThat(matiereModifier.getCreationDate().getTime()).isNotEqualTo(matiereModifier.getModificationDate().getTime());
        });
    }//update_matiere_should_success_when_matiere_is_modified()

    @Test
    public void update_matiere_should_throw_exception_when_matiere_id_is_not_filled(){
        this.matiereToUpdate.setId(null);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La modification de la matière est impossible : l'identifiant n'est pas renseigné.")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_matiere_id_is_not_filled()

    @Test
    public void update_matiere_should_throw_exception_when_name_matiere_is_null() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        matiereFromBd.setModificationDate(new Date());
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setNom(null);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_name_matiere_is_null()

    @Test
    public void update_matiere_should_throw_exception_when_matiere_is_empty() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        matiereFromBd.setModificationDate(new Date());
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setNom("");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("Le nom de la matière est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_matiere_is_empty()

    @Test
    public void update_matiere_should_throw_exception_when_background_color_is_null() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurFond(null);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_background_color_is_null()

    @Test
    public void update_matiere_should_throw_exception_when_background_color_is_empty() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurFond("");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de fond est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_background_color_is_empty()

    @Test
    public void update_matiere_should_throw_exception_when_color_font_is_empty() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_color_font_is_empty()

    @Test
    public void update_matiere_should_throw_exception_when_color_font_is_null() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice(null);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_color_font_is_null()

    @Test
    public void update_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice(null);
        this.matiereToUpdate.setCouleurFond(null);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de fond est obligatoire,La couleur de la police est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_color_font_is_null_and_name_is_null()

    @Test
    public void update_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("#ddd");
        this.matiereToUpdate.setCouleurFond("#ddd");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur du fond et de la police ne peuvent pas être la même")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_color_font_and_color_police_are_the_same()

    @Test
    public void update_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurFond("123");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de fond doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_background_color_type_is_not_hexadecimal()

    @Test
    public void update_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("123");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_police_color_type_is_not_hexadecimal()

    @Test
    public void update_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("255,87,51");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_police_color_type_is_RGB_255_87_51()

    @Test
    public void update_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("10,80%,60%");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_police_color_type_is_HSL_10_80_p_60_p()

    @Test
    public void update_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setCouleurPolice("0,66,100,0");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La couleur de la police doit être au format hexadécimal")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_police_color_type_is_CMYK_0_66_100_0()

    @Test
    public void update_matiere_should_success_when_description_length_is_between_10_and_255() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        Mockito.when(matiereRepository.update(this.matiereToUpdate)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setDescription("la classe à modifier");
        Optional<Matiere> optMatiereUpdated = matiereService.updateMatiere(this.matiereToUpdate);
        Assertions.assertThat(optMatiereUpdated).isPresent();
        optMatiereUpdated.ifPresent(matiereUpdated -> {
            Assertions.assertThat(matiereUpdated).isNotNull();
            Assertions.assertThat(matiereUpdated.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereUpdated.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereUpdated.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereUpdated.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereUpdated.getDescription()).isEqualTo("la classe à modifier");
            Assertions.assertThat(matiereUpdated.getId()).isEqualTo(1);
        });
    }//update_matiere_should_success_when_description_length_is_between_10_and_255()

    @Test
    public void update_matiere_should_throw_exception_when_description_length_is_below_10() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setDescription("C'est ok");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_description_length_is_below_10()

    @Test
    public void update_matiere_should_throw_exception_when_description_length_is_above_255() throws DataBaseException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300 ; ++i){
            sb.append("a");
        }
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setDescription(sb.toString());
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("La description doit être comprise entre 10 et 255 caractères")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_description_length_is_above_255()

    @Test
    public void update_matiere_should_success_when_name_matiere_is_between_3_and_40() throws ArgumentException, DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        Mockito.when(matiereRepository.update(this.matiereToUpdate)).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optMatiereModified = matiereService.updateMatiere(this.matiereToUpdate);
        Assertions.assertThat(optMatiereModified).isPresent();
        optMatiereModified.ifPresent(matiereUpdated -> {
            Assertions.assertThat(matiereUpdated).isNotNull();
            Assertions.assertThat(matiereUpdated.getNom()).isEqualTo("matiere");
            Assertions.assertThat(matiereUpdated.getCouleurFond()).isEqualTo("#fff");
            Assertions.assertThat(matiereUpdated.getCouleurPolice()).isEqualTo("#ddd");
            Assertions.assertThat(matiereUpdated.getVolumeHoraire()).isEqualTo("1H30");
            Assertions.assertThat(matiereUpdated.getDescription()).isEqualTo("la classe à modifier");
            Assertions.assertThat(matiereUpdated.getId()).isEqualTo(1);
        });
    }//update_matiere_should_success_when_name_matiere_is_between_3_and_40()

    @Test
    public void update_matiere_should_throw_exception_when_name_matiere_is_below_3() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setNom("DS");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_name_matiere_is_below_3()

    @Test
    public void update_matiere_should_throw_exception_when_name_matiere_is_above_40() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date(1592149308));
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        this.matiereToUpdate.setNom("Cours de sciences de la vie et de la Terre : Option géographie et études des paysages et montagnes.");
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("Le nom d'une matière doit contenir entre 3 et 40 caractères")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_name_matiere_is_above_40()

    @Test
    public void delete_matiere_should_success_when_matiere_id_is_1() throws DataBaseException {
        Mockito.when(matiereRepository.deleteMatiere(1)).thenReturn(true);
        boolean result = matiereService.deleteMatiere(1);
        Assertions.assertThat(result).isTrue();
    }//delete_matiere_should_success_when_matiere_id_is_1()

    @Test
    public void delete_matiere_should_return_false_when_matiere_id_is_20() throws DataBaseException {
        Mockito.when(matiereRepository.deleteMatiere(20)).thenReturn(false);
        boolean result = matiereService.deleteMatiere(20);
        Assertions.assertThat(matiereService.deleteMatiere(20)).isFalse();
    }//delete_matiere_should_return_false_when_matiere_id_is_20()

    @Test
    public void creation_matiere_should_throw_exception_when_matiere_name_already_exists() throws DataBaseException {
        Mockito.when(matiereRepository.isExistByName(this.matiereToCreate.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> matiereService.insertMatiere(this.matiereToCreate))
                .hasMessage("Cette matière existe déjà")
                .isInstanceOf(ArgumentException.class);
    }//creation_matiere_should_throw_exception_when_matiere_name_already_exists()

    @Test
    public void update_matiere_should_throw_exception_when_matiere_name_already_exists() throws DataBaseException {
        Mockito.when(matiereRepository.isExistByName(this.matiereToUpdate.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> matiereService.updateMatiere(this.matiereToUpdate))
                .hasMessage("Cette matière existe déjà")
                .isInstanceOf(ArgumentException.class);
    }//update_matiere_should_throw_exception_when_matiere_name_already_exists()

    @Test
    public void get_matiere_should_success_when_matiere_id_is_1() throws DataBaseException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","la classe à modifier");
        matiereFromBd.setId(1);
        matiereFromBd.setCreationDate(new Date());
        matiereFromBd.setModificationDate(new Date());
        Mockito.when(matiereRepository.findById(1)).thenReturn(Optional.of(matiereFromBd));
        Optional<Matiere> optResult = matiereService.getMatiere(1);
        Assertions.assertThat(optResult).isPresent();
        optResult.ifPresent((result) -> {
            Assertions.assertThat(result.getId()).isEqualTo(1);
            Assertions.assertThat(result.getNom()).isEqualTo(matiereFromBd.getNom());
            Assertions.assertThat(result.getCouleurFond()).isEqualTo(matiereFromBd.getCouleurFond());
            Assertions.assertThat(result.getCouleurPolice()).isEqualTo(matiereFromBd.getCouleurPolice());
            Assertions.assertThat(result.getVolumeHoraire()).isEqualTo(matiereFromBd.getVolumeHoraire());
            Assertions.assertThat(result.getCreationDate()).isEqualTo(matiereFromBd.getCreationDate());
            Assertions.assertThat(result.getModificationDate()).isEqualTo(matiereFromBd.getModificationDate());
        });
    }//get_matiere_should_success_when_matiere_id_is_1

    @Test
    public void get_matiere_should_return_empty_when_matiere_id_is_20() throws DataBaseException {
        Mockito.when(matiereRepository.findById(20)).thenReturn(Optional.empty());
        Optional<Matiere> matFromBd = matiereService.getMatiere(20);
        Assertions.assertThat(matFromBd).isNotPresent();
    }//get_matiere_should_return_empty_when_matiere_id_is_20()
}//MatiereServiceUT
