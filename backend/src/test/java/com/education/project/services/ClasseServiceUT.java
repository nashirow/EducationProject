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
import com.education.project.model.Classe;
import com.education.project.persistence.ClasseRepository;
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
public class ClasseServiceUT {

    private ClasseService classeService;

    @Mock
    private ClasseRepository classeRepository;

    @Before
    public void setUp(){
        this.classeService = new ClasseService(classeRepository);
    }// setUp()

    @Test
    public void insert_classe_should_success_when_classe_is_filled_with_name() throws ArgumentException, DataBaseException {
        Classe classeFromBd = initClasseFromDataBase();
        Classe classeToInsert = initClasseToInsert();
        Mockito.when(classeRepository.insert(classeToInsert)).thenReturn(Optional.of(classeFromBd));
        Optional<Classe> optClasseInserted = classeService.insertClasse(classeToInsert);
        Assertions.assertThat(optClasseInserted).isPresent();
        optClasseInserted.ifPresent(classe -> {
            Assertions.assertThat(classe.getNom()).isEqualTo(classeToInsert.getNom());
            Assertions.assertThat(classe.getId()).isEqualTo(1);
            Assertions.assertThat(classe.getCreationDate()).isNotNull();
            Assertions.assertThat(classe.getModificationDate()).isNotNull();
            Assertions.assertThat(classe.getCreationDate()).isEqualTo(classe.getModificationDate());
        });
    }// insert_classe_should_success_when_classe_is_filled_with_name()

    @Test
    public void insert_classe_should_throw_argument_exception_when_name_is_null(){
        Classe classeToInsert = new Classe();
        Assertions.assertThatThrownBy(() -> classeService.insertClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom d'une classe est obligatoire");
    }// insert_classe_should_throw_argument_exception_when_name_is_null()

    @Test
    public void insert_classe_should_throw_argument_exception_when_name_is_empty(){
        Classe classeToInsert = new Classe();
        classeToInsert.setNom("");
        Assertions.assertThatThrownBy(() -> classeService.insertClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom d'une classe est obligatoire");
    }// insert_classe_should_throw_argument_exception_when_name_is_empty()

    @Test
    public void insert_classe_should_throw_argument_exception_when_classe_is_null(){
        Classe classeToInsert = null;
        Assertions.assertThatThrownBy(() -> classeService.insertClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("La classe à insérer est obligatoire");
    }// insert_classe_should_throw_argument_exception_when_classe_is_null()

    @Test
    public void insert_classe_should_throw_argument_exception_when_classe_to_insert_is_already_exists() throws DataBaseException {
        Classe classeToInsert = new Classe();
        classeToInsert.setNom("6ème A");
        Mockito.when(classeRepository.existsByName(classeToInsert.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> classeService.insertClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Une classe avec le nom " + classeToInsert.getNom() + " existe déjà dans la base de données");
    }// insert_classe_should_throw_argument_exception_when_classe_to_insert_is_already_exists()

    @Test
    public void insert_classe_should_throw_database_exception_when_database_meet_error() throws DataBaseException {
        Classe classeToInsert = new Classe();
        classeToInsert.setNom("6ème A");
        Mockito.doThrow(new DataBaseException("Impossible de rechercher une classe ayant le nom " + classeToInsert.getNom()))
                .when(classeRepository)
                .existsByName(classeToInsert.getNom());

        Assertions.assertThatThrownBy(() -> classeService.insertClasse(classeToInsert))
                .isInstanceOf(DataBaseException.class)
                .hasMessage("Impossible de rechercher une classe ayant le nom " + classeToInsert.getNom());
    }// insert_classe_should_throw_database_exception_when_database_meet_error()

    @Test
    public void delete_classe_should_success_when_id_1_is_given() throws DataBaseException {
        Mockito.when(classeRepository.delete(1)).thenReturn(true);
        boolean isDeleted = classeService.deleteClass(1);
        Assertions.assertThat(isDeleted).isTrue();
    }// delete_classe_should_success_when_id_1_is_given()

    @Test
    public void delete_classe_should_success_when_id_890_is_given() throws DataBaseException {
        Mockito.when(classeRepository.delete(890)).thenReturn(false);
        boolean isDeleted = classeService.deleteClass(890);
        Assertions.assertThat(isDeleted).isFalse();
    }// delete_classe_should_success_when_id_890_is_given()

    @Test
    public void get_classe_should_return_result_when_id_is_1() throws DataBaseException {
        Classe classeFromBd = initClasseFromDataBase();
        classeFromBd.setModificationDate(new Date(1592848921));
        Mockito.when(classeRepository.findById(1)).thenReturn(Optional.of(classeFromBd));
        Optional<Classe> optResult = classeService.getClasse(1);
        Assertions.assertThat(optResult).isPresent();
        optResult.ifPresent(classe -> {
            Assertions.assertThat(classe.getNom()).isEqualTo("6ème A");
            Assertions.assertThat(classe.getId()).isEqualTo(1);
            Assertions.assertThat(classe.getModificationDate()).isNotNull();
            Assertions.assertThat(classe.getCreationDate()).isNotNull();
            Assertions.assertThat(classe.getModificationDate()).isNotEqualTo(classe.getCreationDate());
        });
    }// get_classe_should_return_result_when_id_is_1()

    @Test
    public void get_classe_should_return_empty_result_when_id_is_10() throws DataBaseException {
        Mockito.when(classeRepository.findById(10)).thenReturn(Optional.empty());
        Optional<Classe> optResult = classeService.getClasse(10);
        Assertions.assertThat(optResult).isNotPresent();
    }// get_classe_should_return_empty_result_when_id_is_10()

    @Test
    public void update_classe_should_success_when_classe_is_filled_with_name_and_id() throws ArgumentException, DataBaseException {
        Classe classeToUpdate = initClasseToUpdate();
        Mockito.when(classeRepository.update(classeToUpdate)).thenReturn(Optional.of(classeToUpdate));
        Optional<Classe> optClasseUpdated = classeService.updateClasse(classeToUpdate);
        Assertions.assertThat(optClasseUpdated).isPresent();
        optClasseUpdated.ifPresent(classe -> {
            Assertions.assertThat(classe.getNom()).isEqualTo(classeToUpdate.getNom());
            Assertions.assertThat(classe.getId()).isEqualTo(1);
            Assertions.assertThat(classe.getCreationDate()).isNotNull();
            Assertions.assertThat(classe.getModificationDate()).isNotNull();
            Assertions.assertThat(classe.getCreationDate()).isNotEqualTo(classe.getModificationDate());
        });
    }// update_classe_should_success_when_classe_is_filled_with_name_and_id()

    @Test
    public void update_classe_should_throw_argument_exception_when_name_is_null(){
        Classe classeToUpdate = initClasseToUpdate();
        classeToUpdate.setNom(null);
        Assertions.assertThatThrownBy(() -> classeService.updateClasse(classeToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom d'une classe est obligatoire");
    }// update_classe_should_throw_argument_exception_when_name_is_null()

    @Test
    public void update_classe_should_throw_argument_exception_when_name_is_empty(){
        Classe classeToUpdate = initClasseToUpdate();
        classeToUpdate.setNom("");
        Assertions.assertThatThrownBy(() -> classeService.updateClasse(classeToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Le nom d'une classe est obligatoire");
    }// update_classe_should_throw_argument_exception_when_name_is_empty()

    @Test
    public void update_classe_should_throw_argument_exception_when_classe_id_is_null(){
        Classe classeToInsert = initClasseToUpdate();
        classeToInsert.setId(null);
        Assertions.assertThatThrownBy(() -> classeService.updateClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("L'identifiant de la classe à mettre à jour est obligatoire");
    }// update_classe_should_throw_argument_exception_when_classe_id_is_null()

    @Test
    public void update_classe_should_throw_argument_exception_when_classe_is_null(){
        Classe classeToInsert = null;
        Assertions.assertThatThrownBy(() -> classeService.updateClasse(classeToInsert))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("La classe à mettre à jour est obligatoire");
    }// update_classe_should_throw_argument_exception_when_classe_is_null()

    @Test
    public void update_classe_should_throw_argument_exception_when_classe_to_update_is_already_exists() throws DataBaseException {
        Classe classeToUpdate = initClasseToUpdate();
        Mockito.when(classeRepository.existsByName(classeToUpdate.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> classeService.updateClasse(classeToUpdate))
                .isInstanceOf(ArgumentException.class)
                .hasMessage("Une classe avec le nom " + classeToUpdate.getNom() + " existe déjà dans la base de données");
    }// update_classe_should_throw_argument_exception_when_classe_to_update_is_already_exists()

    private Classe initClasseFromDataBase(){
        Date now = new Date();
        Classe classeFromBd = new Classe();
        classeFromBd.setNom("6ème A");
        classeFromBd.setModificationDate(now);
        classeFromBd.setCreationDate(now);
        classeFromBd.setId(1);
        return classeFromBd;
    }// initClasseFromDataBase()

    private Classe initClasseToInsert(){
        Classe classeToInsert = new Classe();
        classeToInsert.setNom("6ème A");
        return classeToInsert;
    }// initClasseToInsert()

    private Classe initClasseToUpdate(){
        Classe classeToUpdate = new Classe();
        classeToUpdate.setId(1);
        classeToUpdate.setNom("5ème A");
        classeToUpdate.setCreationDate(new Date(1592848921));
        classeToUpdate.setModificationDate(new Date(1592852521));
        return classeToUpdate;
    }// initClasseToUpdate()
}// ClasseServiceUT
