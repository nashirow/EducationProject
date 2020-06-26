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
import com.education.project.model.Enseignant;
import com.education.project.persistence.EnseignantRepository;
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
public class EnseignantServiceUT {

    private EnseignantService enseignantService;

    private Enseignant enseignantToInsert;

    @Mock
    private EnseignantRepository enseignantRepository;


    @Before
    public void setup(){
        this.enseignantService = new EnseignantService(enseignantRepository);
        this.enseignantToInsert = new Enseignant("Marc","Denim");
    }//setup()

    private Enseignant initEnseignantFromBd(){
        Enseignant enseignantFromBd = new Enseignant();
        Date now = new Date();
        enseignantFromBd.setNom("Marc");
        enseignantFromBd.setPrenom("Denim");
        enseignantFromBd.setCreationDate(now);
        enseignantFromBd.setModificationDate(now);
        enseignantFromBd.setId(1);
        return enseignantFromBd;
    }//initEnseignantFromBd()

    private Enseignant initEnseignantToUpdate(){
        Enseignant enseignantToUpdate = new Enseignant();
        Date now = new Date();
        enseignantToUpdate.setNom("Marc");
        enseignantToUpdate.setPrenom("Denim");
        enseignantToUpdate.setCreationDate(new Date(1593190018));
        enseignantToUpdate.setModificationDate(now);
        enseignantToUpdate.setId(1);
        return enseignantToUpdate;
    }

    @Test
    public void insert_enseignant_should_success_when_all_fields_are_filled() throws ArgumentException, DataBaseException {
        Enseignant enseignantFromBd = initEnseignantFromBd();
        Mockito.when(enseignantRepository.insert(this.enseignantToInsert)).thenReturn(Optional.of(enseignantFromBd));
        Optional<Enseignant> optEnseignantCreated = enseignantService.insertEnseignant(this.enseignantToInsert);
        Assertions.assertThat(optEnseignantCreated.isPresent()).isTrue();
        optEnseignantCreated.ifPresent(enseignantCreated -> {
            Assertions.assertThat(enseignantCreated.getId()).isEqualTo(1);
            Assertions.assertThat(enseignantCreated.getNom()).isEqualTo(enseignantToInsert.getNom());
            Assertions.assertThat(enseignantCreated.getPrenom()).isEqualTo(enseignantToInsert.getPrenom());
            Assertions.assertThat(enseignantCreated.getCreationDate()).isNotNull();
            Assertions.assertThat(enseignantCreated.getModificationDate()).isNotNull();
            Assertions.assertThat(enseignantCreated.getCreationDate()).isEqualTo(enseignantCreated.getModificationDate());
        });
    }//insert_enseignant_should_success_when_all_fields_are_filled()

    @Test
    public void insert_enseignant_should_throw_exception_when_last_name_field_is_empty(){
        this.enseignantToInsert.setNom("");
        Assertions.assertThatThrownBy(()-> enseignantService.insertEnseignant(this.enseignantToInsert))
                .hasMessage("Le nom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_last_name_field_is_empty()

    @Test
    public void insert_enseignant_should_throw_exception_when_last_name_field_is_null(){
        this.enseignantToInsert.setNom(null);
        Assertions.assertThatThrownBy(()-> enseignantService.insertEnseignant(this.enseignantToInsert))
                .hasMessage("Le nom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_last_name_field_is_null()

    @Test
    public void insert_enseignant_should_throw_exception_when_first_name_is_empty(){
        this.enseignantToInsert.setPrenom("");
        Assertions.assertThatThrownBy(() -> enseignantService.insertEnseignant(this.enseignantToInsert))
                .hasMessage("Le prénom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_first_name_is_empty()

    @Test
    public void insert_enseignant_should_throw_exception_when_first_name_is_null(){
        this.enseignantToInsert.setPrenom(null);
        Assertions.assertThatThrownBy(() -> enseignantService.insertEnseignant(this.enseignantToInsert))
                .hasMessage("Le prénom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_first_name_is_null()

    @Test
    public void insert_enseignant_should_throw_exception_when_enseignant_already_exists() throws ArgumentException, DataBaseException {
        Mockito.when(enseignantRepository.isExistByName(this.enseignantToInsert.getNom(),this.enseignantToInsert.getPrenom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> enseignantService.insertEnseignant(enseignantToInsert))
                .hasMessage("L'enseignant : " + this.enseignantToInsert.getNom() + " " + this.enseignantToInsert.getPrenom() + " existe déjà dans la base de données.")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_enseignant_already_exists()

    @Test
    public void insert_enseignant_should_throw_exception_when_enseignant_to_insert_is_null(){
        Assertions.assertThatThrownBy(()-> enseignantService.insertEnseignant(null))
                .hasMessage("L'enseignant à insérer est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_enseignant_to_insert_is_null()

    @Test
    public void update_enseignant_should_success_when_enseignant_last_name_and_first_name_are_modified() throws ArgumentException, DataBaseException {
        Enseignant enseignantFromBd = initEnseignantFromBd();
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        Mockito.when(enseignantRepository.update(enseignantToUpdate)).thenReturn(Optional.of(enseignantFromBd));
        Optional<Enseignant> optEnseignantUpdated = enseignantService.updateEnseignant(enseignantToUpdate);
        Assertions.assertThat(optEnseignantUpdated.isPresent());
        optEnseignantUpdated.ifPresent(enseignantUpdated -> {
            Assertions.assertThat(enseignantUpdated.getNom()).isEqualTo(enseignantToUpdate.getNom());
            Assertions.assertThat(enseignantUpdated.getPrenom()).isEqualTo(enseignantToUpdate.getPrenom());
            Assertions.assertThat(enseignantUpdated.getCreationDate()).isNotNull();
            Assertions.assertThat(enseignantUpdated.getModificationDate()).isNotNull();
            Assertions.assertThat(enseignantUpdated.getCreationDate()).isNotEqualTo(enseignantToUpdate.getModificationDate());
            Assertions.assertThat(enseignantUpdated.getId()).isEqualTo(enseignantToUpdate.getId());
        });
    }//update_enseignant_should_success_when_enseignant_last_name_and_first_name_are_modified()

    @Test
    public void update_enseignant_should_throw_exception_when_last_name_is_null(){
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        enseignantToUpdate.setNom(null);
        Assertions.assertThatThrownBy(()-> enseignantService.updateEnseignant(enseignantToUpdate))
                .hasMessage("Le nom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_enseignant_should_throw_exception_when_last_name_is_null()

    @Test
    public void update_enseignant_should_throw_exception_when_last_name_field_is_empty(){
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        enseignantToUpdate.setNom("");
        Assertions.assertThatThrownBy(() -> enseignantService.updateEnseignant(enseignantToUpdate))
                .hasMessage("Le nom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_enseignant_should_throw_exception_when_last_name_field_is_empty()

    @Test
    public void update_enseignant_should_throw_exception_when_first_name_field_is_null(){
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        enseignantToUpdate.setPrenom(null);
        Assertions.assertThatThrownBy(() -> enseignantService.updateEnseignant(enseignantToUpdate))
                .hasMessage("Le prénom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_enseignant_should_throw_exception_when_first_name_field_is_null()

    @Test
    public void update_enseignant_should_throw_exception_when_first_name_is_empty(){
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        enseignantToUpdate.setPrenom("");
        Assertions.assertThatThrownBy(() -> enseignantService.updateEnseignant(enseignantToUpdate))
                .hasMessage("Le prénom de l'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    public void update_enseignant_should_throw_exception_when_enseignant_to_update_already_exists() throws ArgumentException, DataBaseException {
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        Mockito.when(enseignantRepository.isExistByName(enseignantToUpdate.getNom(),enseignantToUpdate.getPrenom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> enseignantService.updateEnseignant(enseignantToUpdate))
                .hasMessage("L'enseignant : " + enseignantToUpdate.getNom() + " " + enseignantToUpdate.getPrenom() + " existe déjà dans la base de données.");
    }//update_enseignant_should_throw_exception_when_enseignant_to_update_already_exists()

    @Test
    public void update_enseignant_should_throw_exception_when_enseignant_to_update_is_null(){
        Assertions.assertThatThrownBy(() -> enseignantService.updateEnseignant(null))
                .hasMessage("L'enseignant à mettre à jour est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_enseignant_should_throw_exception_when_enseignant_to_update_is_null()

    @Test
    public void delete_enseignant_should_success_when_enseignant_id_is_2() throws DataBaseException {
        Mockito.when(enseignantRepository.delete(2)).thenReturn(true);
        boolean isDeleted = enseignantService.deleteEnseignant(2);
        Assertions.assertThat(isDeleted).isTrue();
    }//delete_enseignant_should_success_when_enseignant_id_is_2()

    @Test
    public void delete_enseignant_should_sucess_when_enseignant_id_is_30() throws DataBaseException {
        Mockito.when(enseignantRepository.delete(30)).thenReturn(false);
        boolean isDeleted = enseignantService.deleteEnseignant(30);
        Assertions.assertThat(isDeleted).isFalse();
    }

    @Test
    public void get_enseignant_should_return_result_when_id_is_2() throws DataBaseException {
        Enseignant enseignantFromBd = initEnseignantFromBd();
        enseignantFromBd.setId(2);
        enseignantFromBd.setCreationDate(new Date(1593190018));
        Mockito.when(enseignantRepository.findById(2)).thenReturn(Optional.of(enseignantFromBd));
        Optional<Enseignant> optResult = enseignantService.getEnseignant(2);
        Assertions.assertThat(optResult).isPresent();
        optResult.ifPresent(result -> {
            Assertions.assertThat(result.getNom()).isEqualTo("Marc");
            Assertions.assertThat(result.getPrenom()).isEqualTo("Denim");
            Assertions.assertThat(result.getCreationDate()).isNotNull();
            Assertions.assertThat(result.getModificationDate()).isNotNull();
            Assertions.assertThat(result.getCreationDate()).isNotEqualTo(result.getModificationDate());
            Assertions.assertThat(result.getId()).isEqualTo(2);
        });
    }//get_enseignant_should_return_result_when_id_is_2()

    @Test
    public void get_enseignant_should_return_empty_enseignant_if_id_does_not_exist() throws DataBaseException {
        Optional<Enseignant> optResult = enseignantService.getEnseignant(30);
        Assertions.assertThat(optResult).isNotPresent();
    }//get_enseignant_should_return_empty_enseignant_if_id_does_not_exist()


}
