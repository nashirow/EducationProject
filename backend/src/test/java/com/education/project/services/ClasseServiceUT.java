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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    public void get_count_classes_should_return_4_when_no_filters() throws DataBaseException {
        Mockito.when(classeRepository.count(null)).thenReturn(4L);
        long total = classeService.getCount();
        Assertions.assertThat(total).isEqualTo(4);
    }// get_count_classes_should_return_4_when_no_filters()

    @Test
    public void get_count_classes_should_return_2_when_filter_name_is_A() throws DataBaseException {
        Mockito.when(classeRepository.count("A")).thenReturn(2L);
        long total = classeService.getCount("A");
        Assertions.assertThat(total).isEqualTo(2);
    }// get_count_classes_should_return_2_when_filter_name_is_A()

    @Test
    public void get_count_classes_should_return_4_when_filter_name_is_null() throws DataBaseException {
        Mockito.when(classeRepository.count(null)).thenReturn(4L);
        long total = classeService.getCount(null);
        Assertions.assertThat(total).isEqualTo(4);
    }// get_count_classes_should_return_2_when_filter_name_is_null()

    @Test
    public void get_classes_should_return_all_classes_when_no_filters() throws DataBaseException {
        Mockito.when(classeRepository.getClasses(null, null, null)).thenReturn(this.getClassesFromDataBase());
        List<Classe> results = classeService.getClasses(null, null, null);
        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(4);
        Classe r1 = results.get(0);
        Classe r2 = results.get(1);
        Classe r3 = results.get(2);
        Classe r4 = results.get(3);

        Assertions.assertThat(r1.getId()).isEqualTo(1);
        Assertions.assertThat(r1.getNom()).isEqualTo("6ème A");
        Assertions.assertThat(r1.getCreationDate()).isNotNull();
        Assertions.assertThat(r1.getModificationDate()).isNotNull();

        Assertions.assertThat(r2.getId()).isEqualTo(2);
        Assertions.assertThat(r2.getNom()).isEqualTo("6ème B");
        Assertions.assertThat(r2.getCreationDate()).isNotNull();
        Assertions.assertThat(r2.getModificationDate()).isNotNull();

        Assertions.assertThat(r3.getId()).isEqualTo(3);
        Assertions.assertThat(r3.getNom()).isEqualTo("5ème A");
        Assertions.assertThat(r3.getCreationDate()).isNotNull();
        Assertions.assertThat(r3.getModificationDate()).isNotNull();

        Assertions.assertThat(r4.getId()).isEqualTo(4);
        Assertions.assertThat(r4.getNom()).isEqualTo("3ème C");
        Assertions.assertThat(r4.getCreationDate()).isNotNull();
        Assertions.assertThat(r4.getModificationDate()).isNotNull();
    }// get_classes_should_return_all_classes_when_no_filters()

    @Test
    public void get_classes_should_return_all_classes_when_filter_name_is_C() throws DataBaseException {
        List<Classe> classes = this.getClassesFromDataBase();
        Classe classeC = classes.stream().filter(classe -> classe.getNom().contains("C")).findFirst().get();
        Mockito.when(classeRepository.getClasses(null, null, "C")).thenReturn(Collections.singletonList(classeC));
        List<Classe> results = classeService.getClasses(null, null, "C");
        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(1);

        Classe result = results.get(0);
        Assertions.assertThat(result.getId()).isEqualTo(4);
        Assertions.assertThat(result.getNom()).isEqualTo("3ème C");
        Assertions.assertThat(result.getCreationDate()).isNotNull();
        Assertions.assertThat(result.getModificationDate()).isNotNull();
    }// get_classes_should_return_all_classes_when_filter_name_is_C()

    @Test
    public void get_classes_should_return_all_classes_when_filter_name_is_null_or_empty_and_with_pagination() throws DataBaseException {
        final int nbClassesPerPage = 2;
        List<Classe> classes = this.getClassesFromDataBase();
        List<Classe> classesPage1 = Stream.of(classes.get(0), classes.get(1)).collect(Collectors.toList());
        Mockito.when(classeRepository.getClasses(1, nbClassesPerPage, null)).thenReturn(classesPage1);
        List<Classe> results = classeService.getClasses(1, nbClassesPerPage, null); // page 1

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(nbClassesPerPage);

        Classe r1 = results.get(0);
        Classe r2 = results.get(1);

        Assertions.assertThat(r1.getId()).isEqualTo(1);
        Assertions.assertThat(r1.getNom()).isEqualTo("6ème A");
        Assertions.assertThat(r1.getCreationDate()).isNotNull();
        Assertions.assertThat(r1.getModificationDate()).isNotNull();

        Assertions.assertThat(r2.getId()).isEqualTo(2);
        Assertions.assertThat(r2.getNom()).isEqualTo("6ème B");
        Assertions.assertThat(r2.getCreationDate()).isNotNull();
        Assertions.assertThat(r2.getModificationDate()).isNotNull();

        List<Classe> classesPage2 = Stream.of(classes.get(2), classes.get(3)).collect(Collectors.toList());
        Mockito.when(classeRepository.getClasses(2, nbClassesPerPage, "")).thenReturn(classesPage2);
        results = classeService.getClasses(2, nbClassesPerPage, ""); // page 2
        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(nbClassesPerPage);

        r1 = results.get(0);
        r2 = results.get(1);

        Assertions.assertThat(r1.getId()).isEqualTo(3);
        Assertions.assertThat(r1.getNom()).isEqualTo("5ème A");
        Assertions.assertThat(r1.getCreationDate()).isNotNull();
        Assertions.assertThat(r1.getModificationDate()).isNotNull();

        Assertions.assertThat(r2.getId()).isEqualTo(4);
        Assertions.assertThat(r2.getNom()).isEqualTo("3ème C");
        Assertions.assertThat(r2.getCreationDate()).isNotNull();
        Assertions.assertThat(r2.getModificationDate()).isNotNull();
    }// get_classes_should_return_all_classes_when_filter_name_is_null_or_empty_and_with_pagination()

    @Test
    public void get_classes_should_return_all_classes_when_filter_name_is_ème_and_with_pagination() throws DataBaseException {
        final int nbClassesPerPage = 2;
        List<Classe> classes = this.getClassesFromDataBase();
        List<Classe> classesPage1 = Stream.of(classes.get(0), classes.get(1)).collect(Collectors.toList());
        Mockito.when(classeRepository.getClasses(1, nbClassesPerPage, "ème")).thenReturn(classesPage1);
        List<Classe> results = classeService.getClasses(1, nbClassesPerPage, "ème"); // page 1

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(nbClassesPerPage);

        Classe r1 = results.get(0);
        Classe r2 = results.get(1);

        Assertions.assertThat(r1.getId()).isEqualTo(1);
        Assertions.assertThat(r1.getNom()).isEqualTo("6ème A");
        Assertions.assertThat(r1.getCreationDate()).isNotNull();
        Assertions.assertThat(r1.getModificationDate()).isNotNull();

        Assertions.assertThat(r2.getId()).isEqualTo(2);
        Assertions.assertThat(r2.getNom()).isEqualTo("6ème B");
        Assertions.assertThat(r2.getCreationDate()).isNotNull();
        Assertions.assertThat(r2.getModificationDate()).isNotNull();

        List<Classe> classesPage2 = Stream.of(classes.get(2), classes.get(3)).collect(Collectors.toList());
        Mockito.when(classeRepository.getClasses(2, nbClassesPerPage, "ème")).thenReturn(classesPage2);
        results = classeService.getClasses(2, nbClassesPerPage, "ème"); // page 2
        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).hasSize(nbClassesPerPage);

        r1 = results.get(0);
        r2 = results.get(1);

        Assertions.assertThat(r1.getId()).isEqualTo(3);
        Assertions.assertThat(r1.getNom()).isEqualTo("5ème A");
        Assertions.assertThat(r1.getCreationDate()).isNotNull();
        Assertions.assertThat(r1.getModificationDate()).isNotNull();

        Assertions.assertThat(r2.getId()).isEqualTo(4);
        Assertions.assertThat(r2.getNom()).isEqualTo("3ème C");
        Assertions.assertThat(r2.getCreationDate()).isNotNull();
        Assertions.assertThat(r2.getModificationDate()).isNotNull();
    }// get_classes_should_return_all_classes_when_filter_name_is_ème_and_with_pagination()

    @Test
    public void get_classes_should_return_empty_list_when_filter_name_is_toto_and_no_pagination() throws DataBaseException {
        Assertions.assertThat(classeService.getClasses(null, null, "toto"))
                .isNotNull()
                .isEmpty();
    }// get_classes_should_return_empty_list_when_filter_name_is_toto_and_no_pagination()

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

    private List<Classe> getClassesFromDataBase(){
        Date now = new Date();
        List<Classe> results = new ArrayList<>();
        results.add(new Classe(1, "6ème A", now, now));
        results.add(new Classe(2, "6ème B", now, now));
        results.add(new Classe(3, "5ème A", now, now));
        results.add(new Classe(4, "3ème C", now, now));
        return results;
    }// getClassesFromDataBase()

}// ClasseServiceUT
