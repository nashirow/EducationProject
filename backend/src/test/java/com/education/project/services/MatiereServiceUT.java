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
import com.education.project.model.Matiere;
import com.education.project.persistence.MatiereRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class MatiereServiceUT {

    private MatiereRepository matiereRepository;
    private MatiereService matiereService;
    private Matiere matiereToCreate;

    @Before
    public void setup(){
         this.matiereRepository = Mockito.spy(new MatiereRepository());
         this.matiereService = new MatiereService(this.matiereRepository);
         this.matiereToCreate = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
    }//setup()

    @Test
    public void create_matiere_should_success_when_all_fields_filled() throws ArgumentException {
        Matiere matiereFromBd = new Matiere("matiere","#fff","#ddd","1H30","Prendre les élèves dyslexiques en groupe");
        matiereFromBd.setId(1);
        Mockito.when(matiereRepository.insert(Mockito.any(Matiere.class))).thenReturn(matiereFromBd);
        Matiere matiereCreated = matiereService.createMatiere(this.matiereToCreate);
        Assertions.assertThat(matiereCreated).isNotNull();
        Assertions.assertThat(matiereCreated.getNom()).isEqualTo("matiere");
        Assertions.assertThat(matiereCreated.getCouleurFond()).isEqualTo("#fff");
        Assertions.assertThat(matiereCreated.getCouleurPolice()).isEqualTo("#ddd");
        Assertions.assertThat(matiereCreated.getVolumeHoraire()).isEqualTo("1H30");
        Assertions.assertThat(matiereCreated.getDescriptionMatiere()).isEqualTo("Prendre les élèves dyslexiques en groupe");
        Assertions.assertThat(matiereCreated.getId()).isEqualTo(1);
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
}//MatiereServiceUT
