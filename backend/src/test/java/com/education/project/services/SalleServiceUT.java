package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Salle;
import com.education.project.persistence.SalleRepository;
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
public class SalleServiceUT {

    private SalleService salleService;
    @Mock
    private SalleRepository salleRepository;

    @Before
    public void setUp(){
        this.salleService = new SalleService(salleRepository);
    }//setUp()

    @Test
    public void insert_salle_should_success_when_name_is_B024() throws ArgumentException, DataBaseException {
        Salle salleToInsert = initSalleToInsert();
        Salle salleFromBd = initSalleToInsertFromBD();
        salleToInsert.setNom("B024");
        Mockito.when(salleRepository.insert(salleToInsert)).thenReturn(Optional.of(salleFromBd));
        Optional<Salle> optSalle = salleService.insertSalle(salleToInsert);
        Assertions.assertThat(optSalle.isPresent()).isTrue();
        optSalle.ifPresent(salle -> {
            Assertions.assertThat(salle.getNom()).isEqualTo(salleFromBd.getNom());
            Assertions.assertThat(salle.getId()).isEqualTo(salleFromBd.getId());
            Assertions.assertThat(salle.getCreationDate()).isNotNull();
            Assertions.assertThat(salle.getModificationDate()).isNotNull();
            Assertions.assertThat(salle.getCreationDate()).isEqualTo(salle.getModificationDate());
        });
    }//insert_salle_should_success_when_name_is_B024()

    @Test
    public void insert_salle_should_success_when_name_is_2() throws ArgumentException, DataBaseException {
        Salle salleToInsert = initSalleToInsert();
        salleToInsert.setNom("2");
        salleToInsert.setId(2);
        Salle salleFromBd = initSalleToInsertFromBD();
        salleFromBd.setId(2);
        salleFromBd.setNom("2");
        Mockito.when(salleRepository.insert(salleToInsert)).thenReturn(Optional.of(salleFromBd));
        Optional<Salle> optSalle = salleService.insertSalle(salleToInsert);
        Assertions.assertThat(optSalle.isPresent()).isTrue();
        optSalle.ifPresent(salle -> {
            Assertions.assertThat(salle.getId()).isEqualTo(salleFromBd.getId());
            Assertions.assertThat(salle.getNom()).isEqualTo(salleFromBd.getNom());
            Assertions.assertThat(salle.getCreationDate()).isNotNull();
            Assertions.assertThat(salle.getModificationDate()).isNotNull();
            Assertions.assertThat(salle.getCreationDate()).isEqualTo(salle.getModificationDate());
        });
    }//insert_salle_should_success_when_name_is_2()

    @Test
    public void insert_salle_should_success_when_name_is_St_victoire() throws ArgumentException, DataBaseException {
        Salle salleToInsert = initSalleToInsert();
        salleToInsert.setNom("St-victoire");
        salleToInsert.setId(3);
        Salle salleFromBd = initSalleToInsertFromBD();
        Mockito.when(salleRepository.insert(salleToInsert)).thenReturn(Optional.of(salleFromBd));
        Optional<Salle> optSalle = salleService.insertSalle(salleToInsert);
        Assertions.assertThat(optSalle.isPresent()).isTrue();
        optSalle.ifPresent(salle -> {
            Assertions.assertThat(salle.getId()).isEqualTo(salleFromBd.getId());
            Assertions.assertThat(salle.getNom()).isEqualTo(salleFromBd.getNom());
            Assertions.assertThat(salle.getCreationDate()).isNotNull();
            Assertions.assertThat(salle.getModificationDate()).isNotNull();
            Assertions.assertThat(salle.getCreationDate()).isEqualTo(salle.getModificationDate());
        });
    }//insert_salle_should_success_when_name_is_St_victoire()

    @Test
    public void insert_salle_should_throw_exception_when_name_is_null(){
        Salle salleToInsert = initSalleToInsert();
        salleToInsert.setNom(null);
        Assertions.assertThatThrownBy(() -> salleService.insertSalle(salleToInsert))
                .hasMessage("Le nom d'une salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_salle_should_throw_exception_when_name_is_null()

    @Test
    public void insert_salle_should_throw_exception_when_name_is_empty(){
        Salle salleToInsert = initSalleToInsert();
        salleToInsert.setNom("");
        Assertions.assertThatThrownBy(() -> salleService.insertSalle(salleToInsert))
                .hasMessage("Le nom d'une salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_salle_should_throw_exception_when_name_is_empty()

    @Test
    public void insert_salle_should_throw_exception_when_salle_is_null() throws ArgumentException {
        Salle salleToInsert = null;
        Assertions.assertThatThrownBy(() -> salleService.insertSalle(salleToInsert))
                .hasMessage("La salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//insert_salle_should_throw_exception_when_salle_is_null()

    @Test
    public void insert_salle_should_throw_exception_when_name_already_exists() throws DataBaseException {
        Salle salleToInsert = initSalleToInsert();
        salleToInsert.setNom("St-victoire");
        Mockito.when(salleRepository.isExistByName(salleToInsert.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> salleService.insertSalle(salleToInsert))
                .hasMessage("Le nom de la salle existe déjà en base de données")
                .isInstanceOf(ArgumentException.class);
    }//insert_salle_should_throw_exception_when_name_already_exists()

    private Salle initSalleToInsert(){
        Date now = new Date();
        Salle salle = new Salle();
        salle.setId(1);
        salle.setNom("St-victoire");
        salle.setCreationDate(now);
        salle.setModificationDate(now);
        return salle;
    }//initSalleToInsert()

    private Salle initSalleToInsertFromBD(){
        Date now = new Date();
        Salle salle = new Salle(1,"B024",now,now);
        return salle;
    }//initSalleToInsertFromBD()
}//SalleServiceUT
