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

    public EnseignantServiceUT() {
    }

    @Before
    public void setup(){
        this.enseignantService = new EnseignantService(enseignantRepository);
        this.enseignantToInsert = new Enseignant("Pichon","Jean");
    }//setup()

    public Enseignant initEnseignantFromBd(){
        Enseignant enseignantFromBd = new Enseignant();
        Date now = new Date();
        enseignantFromBd.setNom("Pichon");
        enseignantFromBd.setPrenom("Jean");
        enseignantFromBd.setCreationDate(now);
        enseignantFromBd.setModificationDate(now);
        enseignantFromBd.setId(1);
        return enseignantFromBd;
    }//initEnseignantFromBd()

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
        Mockito.when(enseignantRepository.isExistByName(this.enseignantToInsert.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> enseignantService.insertEnseignant(enseignantToInsert))
                .hasMessage("Le nom de l'enseignant " + this.enseignantToInsert.getNom() +" existe déjà dans la base de données.")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_enseignant_already_exists()

    @Test
    public void insert_enseignant_should_throw_exception_when_enseignant_to_insert_is_null(){
        Assertions.assertThatThrownBy(()-> enseignantService.insertEnseignant(null))
                .hasMessage("L'enseignant est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_enseignant_should_throw_exception_when_enseignant_to_insert_is_null()
}
