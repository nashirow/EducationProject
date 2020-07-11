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
import com.education.project.model.Enseignant;
import com.education.project.persistence.EnseignantRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        Enseignant enseignantToUpdate = initEnseignantToUpdate();
        enseignantToUpdate.setModificationDate(new Date(1578862575));
        Mockito.when(enseignantRepository.update(enseignantToUpdate)).thenReturn(Optional.of(enseignantToUpdate));
        Optional<Enseignant> optEnseignantUpdated = enseignantService.updateEnseignant(enseignantToUpdate);
        Assertions.assertThat(optEnseignantUpdated.isPresent());
        optEnseignantUpdated.ifPresent(enseignantUpdated -> {
            Assertions.assertThat(enseignantUpdated.getNom()).isEqualTo(enseignantToUpdate.getNom());
            Assertions.assertThat(enseignantUpdated.getPrenom()).isEqualTo(enseignantToUpdate.getPrenom());
            Assertions.assertThat(enseignantUpdated.getCreationDate()).isNotNull();
            Assertions.assertThat(enseignantUpdated.getModificationDate()).isNotNull();
            //Assertions.assertThat(enseignantUpdated.getCreationDate()).isNotEqualTo(enseignantToUpdate.getModificationDate());
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
    }// delete_enseignant_should_sucess_when_enseignant_id_is_30()

    @Test
    public void delete_enseignant_should_throw_database_exception_when_enseignant_is_used_by_slot() throws DataBaseException {
        Mockito.when(enseignantRepository.isUsedBySlots(3)).thenReturn(true);
        Assertions.assertThatCode(() -> enseignantService.deleteEnseignant(3))
                .hasMessage("Impossible de supprimer l'enseignant : L'enseignant que vous tentez de supprimer est peut-être utilisé par un ou plusieurs slot(s)")
                .isInstanceOf(DataBaseException.class);
    }// delete_enseignant_should_throw_database_exception_when_enseignant_is_used_by_slot()

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

    @Test
    public void get_enseignants_should_return_all_enseignants_when_no_filters_given() throws DataBaseException {
        List<Enseignant> enseignantsFromBd = initGetEnseignantsFromBd();
        Mockito.when(enseignantRepository.getEnseignants(null,null,null,null)).thenReturn(enseignantsFromBd);
        List<Enseignant> enseignantsToGet = enseignantService.getEnseignants(null,null,null,null);
        Assertions.assertThat(enseignantsToGet).isNotEmpty();
        Assertions.assertThat(enseignantsToGet).hasSize(4);

        Assertions.assertThat(enseignantsToGet.get(0).getId()).isEqualTo(enseignantsFromBd.get(0).getId());
        Assertions.assertThat(enseignantsToGet.get(0).getNom()).isEqualTo(enseignantsFromBd.get(0).getNom());
        Assertions.assertThat(enseignantsToGet.get(0).getPrenom()).isEqualTo(enseignantsFromBd.get(0).getPrenom());
        Assertions.assertThat(enseignantsToGet.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(0).getCreationDate()).isNotEqualTo(enseignantsToGet.get(0).getModificationDate());

        Assertions.assertThat(enseignantsToGet.get(1).getId()).isEqualTo(enseignantsFromBd.get(1).getId());
        Assertions.assertThat(enseignantsToGet.get(1).getNom()).isEqualTo(enseignantsFromBd.get(1).getNom());
        Assertions.assertThat(enseignantsToGet.get(1).getPrenom()).isEqualTo(enseignantsFromBd.get(1).getPrenom());
        Assertions.assertThat(enseignantsToGet.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(1).getCreationDate()).isNotEqualTo(enseignantsToGet.get(1).getModificationDate());

        Assertions.assertThat(enseignantsToGet.get(2).getId()).isEqualTo(enseignantsFromBd.get(2).getId());
        Assertions.assertThat(enseignantsToGet.get(2).getNom()).isEqualTo(enseignantsFromBd.get(2).getNom());
        Assertions.assertThat(enseignantsToGet.get(2).getPrenom()).isEqualTo(enseignantsFromBd.get(2).getPrenom());
        Assertions.assertThat(enseignantsToGet.get(2).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(2).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(2).getCreationDate()).isNotEqualTo(enseignantsToGet.get(2).getModificationDate());

        Assertions.assertThat(enseignantsToGet.get(3).getId()).isEqualTo(enseignantsFromBd.get(3).getId());
        Assertions.assertThat(enseignantsToGet.get(3).getNom()).isEqualTo(enseignantsFromBd.get(3).getNom());
        Assertions.assertThat(enseignantsToGet.get(3).getPrenom()).isEqualTo(enseignantsFromBd.get(3).getPrenom());
        Assertions.assertThat(enseignantsToGet.get(3).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(3).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(3).getCreationDate()).isNotEqualTo(enseignantsToGet.get(3).getModificationDate());
    }//get_enseignants_should_return_all_enseignants_when_no_filters_given()

    @Test
    public void get_enseignants_should_return_enseignant_didier_when_name_is_didier() throws DataBaseException {
        List<Enseignant> enseignantsFromBd = initGetEnseignantsFromBd();
        Mockito.when(enseignantRepository.getEnseignants("Didier",null,null,null)).thenReturn(enseignantsFromBd.subList(0,1));
        List<Enseignant> enseignantsToGet = enseignantService.getEnseignants("Didier",null,null,null);
        Assertions.assertThat(enseignantsToGet).isNotEmpty();
        Assertions.assertThat(enseignantsToGet).hasSize(1);
        Assertions.assertThat(enseignantsToGet.get(0).getId()).isEqualTo(enseignantsFromBd.get(0).getId());
        Assertions.assertThat(enseignantsToGet.get(0).getNom()).isEqualTo(enseignantsFromBd.get(0).getNom());
        Assertions.assertThat(enseignantsToGet.get(0).getPrenom()).isEqualTo(enseignantsFromBd.get(0).getPrenom());
        Assertions.assertThat(enseignantsToGet.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignantsToGet.get(0).getCreationDate()).isNotEqualTo(enseignantsToGet.get(0).getModificationDate());
    }//get_enseignants_should_return_enseignant_didier_when_name_is_didier()

    @Test
    public void get_enseignants_should_return_empty_when_last_name_is_Didier_and_first_name_is_Delavega() throws DataBaseException {
        List<Enseignant> enseignantToGet = enseignantService.getEnseignants("Didier","Delavega",null,null);
        Assertions.assertThat(enseignantToGet).isEmpty();
    }//get_enseignants_should_return_empty_when_last_name_is_Didier_and_first_name_is_Delavega()

    @Test
    public void get_enseignants_should_return_two_enseignants_with_one_page_and_two_elements_per_page_when_no_names_given() throws DataBaseException {
        List<Enseignant> enseignantsFromBd = initGetEnseignantsFromBd();
        Mockito.when(enseignantRepository.getEnseignants(null,null,1,2)).thenReturn(enseignantsFromBd.subList(0,2));
        List<Enseignant> enseignants = enseignantService.getEnseignants(null,null,1,2);
        Assertions.assertThat(enseignants.get(0).getId()).isEqualTo(enseignantsFromBd.get(0).getId());
        Assertions.assertThat(enseignants.get(0).getNom()).isEqualTo(enseignantsFromBd.get(0).getNom());
        Assertions.assertThat(enseignants.get(0).getPrenom()).isEqualTo(enseignantsFromBd.get(0).getPrenom());
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotEqualTo(enseignants.get(0).getModificationDate());

        Assertions.assertThat(enseignants.get(1).getId()).isEqualTo(enseignantsFromBd.get(1).getId());
        Assertions.assertThat(enseignants.get(1).getNom()).isEqualTo(enseignantsFromBd.get(1).getNom());
        Assertions.assertThat(enseignants.get(1).getPrenom()).isEqualTo(enseignantsFromBd.get(1).getPrenom());
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotEqualTo(enseignants.get(1).getModificationDate());
    }//get_enseignants_should_return_two_enseignants_with_one_page_and_two_elements_per_pages_when_no_names_given()

    @Test
    public void get_enseignants_should_return_two_enseignants_with_two_pages_and_two_elements_per_page_when_no_name_given() throws DataBaseException {
        List<Enseignant> enseignantsFromBd = initGetEnseignantsFromBd();
        Mockito.when(enseignantRepository.getEnseignants(null,null,2,2)).thenReturn(enseignantsFromBd.subList(2,4));
        List<Enseignant> enseignants = enseignantService.getEnseignants(null,null,2,2);
        Assertions.assertThat(enseignants).isNotEmpty();
        Assertions.assertThat(enseignants).hasSize(2);

        Assertions.assertThat(enseignants.get(0).getId()).isEqualTo(enseignantsFromBd.get(2).getId());
        Assertions.assertThat(enseignants.get(0).getNom()).isEqualTo(enseignantsFromBd.get(2).getNom());
        Assertions.assertThat(enseignants.get(0).getPrenom()).isEqualTo(enseignantsFromBd.get(2).getPrenom());
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotEqualTo(enseignants.get(0).getModificationDate());

        Assertions.assertThat(enseignants.get(1).getId()).isEqualTo(enseignantsFromBd.get(3).getId());
        Assertions.assertThat(enseignants.get(1).getNom()).isEqualTo(enseignantsFromBd.get(3).getNom());
        Assertions.assertThat(enseignants.get(1).getPrenom()).isEqualTo(enseignantsFromBd.get(3).getPrenom());
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotEqualTo(enseignants.get(1).getModificationDate());
    }//get_enseignants_should_return_two_enseignants_with_two_pages_and_two_elements_per_page_when_no_name_given()

    @Test
    public void get_enseignants_should_return_three_enseignants_with_one_page_and_three_elements_per_page_when_no_name_given() throws DataBaseException {
        List<Enseignant> enseignantFromBd = initGetEnseignantsFromBd();
        Mockito.when(enseignantRepository.getEnseignants(null,null,1,3)).thenReturn(enseignantFromBd.subList(0,3));
        List<Enseignant> enseignants = enseignantService.getEnseignants(null,null,1,3);
        Assertions.assertThat(enseignants).isNotEmpty();
        Assertions.assertThat(enseignants).hasSize(3);

        Assertions.assertThat(enseignants.get(0).getId()).isEqualTo(enseignantFromBd.get(0).getId());
        Assertions.assertThat(enseignants.get(0).getNom()).isEqualTo(enseignantFromBd.get(0).getNom());
        Assertions.assertThat(enseignants.get(0).getPrenom()).isEqualTo(enseignantFromBd.get(0).getPrenom());
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(0).getCreationDate()).isNotEqualTo(enseignants.get(0).getModificationDate());

        Assertions.assertThat(enseignants.get(1).getId()).isEqualTo(enseignantFromBd.get(1).getId());
        Assertions.assertThat(enseignants.get(1).getNom()).isEqualTo(enseignantFromBd.get(1).getNom());
        Assertions.assertThat(enseignants.get(1).getPrenom()).isEqualTo(enseignantFromBd.get(1).getPrenom());
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(1).getCreationDate()).isNotEqualTo(enseignants.get(1).getModificationDate());

        Assertions.assertThat(enseignants.get(2).getId()).isEqualTo(enseignantFromBd.get(2).getId());
        Assertions.assertThat(enseignants.get(2).getNom()).isEqualTo(enseignantFromBd.get(2).getNom());
        Assertions.assertThat(enseignants.get(2).getPrenom()).isEqualTo(enseignantFromBd.get(2).getPrenom());
        Assertions.assertThat(enseignants.get(2).getCreationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(2).getModificationDate()).isNotNull();
        Assertions.assertThat(enseignants.get(2).getCreationDate()).isNotEqualTo(enseignants.get(2).getModificationDate());
    }//get_enseignants_should_return_three_enseignants_with_one_page_and_three_elements_per_page_when_no_name_given()

    @Test
    public void count_enseignants_should_return_4_when_no_filters_filled() throws DataBaseException {
        Mockito.when(enseignantRepository.countEnseignants(null,null)).thenReturn(4L);
        long result = enseignantService.countEnseignants(null,null);
        Assertions.assertThat(result).isEqualTo(4L);
    }//count_enseignants_should_return_4_when_no_filters_filled()

    @Test
    public void count_enseignants_should_return_0_when_first_name_is_apache() throws DataBaseException {
        Mockito.when(enseignantRepository.countEnseignants("apache",null)).thenReturn(0L);
        long result = enseignantService.countEnseignants("apache",null);
        Assertions.assertThat(result).isEqualTo(0L);
    }//count_enseignants_should_return_0_when_name_is_apache()

    @Test
    public void count_enseignants_should_return_1_when_last_name_is_raoul() throws DataBaseException {
        Mockito.when(enseignantRepository.countEnseignants(null,"Raoul")).thenReturn(1L);
        long result = enseignantService.countEnseignants(null,"Raoul");
        Assertions.assertThat(result).isEqualTo(1L);
    }//count_enseignants_should_return_1_when_last_name_is_raoul()

    @Test
    public void count_enseignant_should_return_1_when_first_name_is_didier_and_last_name_is_raoul() throws DataBaseException {
        Mockito.when(enseignantRepository.countEnseignants("Didier","Raoul")).thenReturn(1L);
        long result = enseignantService.countEnseignants("Didier","Raoul");
        Assertions.assertThat(result).isEqualTo(1L);
    }//count_enseignant_should_return_1_when_first_name_is_didier_and_last_name_is_raoul()

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
        enseignantToUpdate.setNom("Marc");
        enseignantToUpdate.setPrenom("Denim");
        enseignantToUpdate.setCreationDate(new Date());
        enseignantToUpdate.setModificationDate(new Date(1580331375));
        enseignantToUpdate.setId(1);
        return enseignantToUpdate;
    }

    private List<Enseignant> initGetEnseignantsFromBd(){
        List<Enseignant> enseignants = new ArrayList<>();
        Date now = new Date();
        Date modifDate = new Date(1593190018);
        Enseignant enseignant1 = new Enseignant(1,"Didier","Raoul",now,modifDate);
        Enseignant enseignant2 = new Enseignant(2,"Alex","Dupont",now,modifDate);
        Enseignant enseignant3 = new Enseignant(3,"Charles","Dupont",now,modifDate);
        Enseignant enseignant4 = new Enseignant(4,"Charles","Phillipin",now,modifDate);
        enseignants.add(enseignant1);
        enseignants.add(enseignant2);
        enseignants.add(enseignant3);
        enseignants.add(enseignant4);
        return enseignants;
    }
}//EnseignantServiceUT
