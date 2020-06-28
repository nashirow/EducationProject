package com.education.project.services;

import com.education.project.exceptions.ArgumentException;
import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Salle;
import com.education.project.persistence.SalleRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
                .hasMessage("La salle à insérer est obligatoire")
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

    @Test
    public void update_salle_should_success_when_name_is_beaufort() throws ArgumentException, DataBaseException {
        Salle salleToUpdate = initSalleToUpdate();
        salleToUpdate.setNom("Beaufort");
        Salle salleFromBd = initSalleToInsertFromBD();
        salleFromBd.setNom("Beaufort");
        Mockito.when(salleRepository.update(salleToUpdate)).thenReturn(Optional.of(salleFromBd));
        Optional <Salle> optSalle = salleService.updateSalle(salleToUpdate);
        Assertions.assertThat(optSalle.isPresent()).isTrue();
    }//update_salle_should_success_when_name_is_beaufort()

    @Test
    public void update_salle_should_throw_exception_when_salle_is_null(){
        Salle salleToUpdate = null;
        Assertions.assertThatThrownBy(() -> salleService.updateSalle(salleToUpdate))
                .hasMessage("La salle à mettre à jour est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_salle_should_throw_exception_when_salle_is_null()

    @Test
    public void update_salle_should_throw_exception_when_name_is_empty(){
        Salle salleToUpdate = initSalleToUpdate();
        salleToUpdate.setNom("");
        Assertions.assertThatThrownBy(() -> salleService.updateSalle(salleToUpdate))
                .hasMessage("Le nom d'une salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_salle_should_throw_exception_when_name_is_empty()

    @Test
    public void update_salle_should_throw_exception_when_name_is_null(){
        Salle salleToUpdate = initSalleToUpdate();
        salleToUpdate.setNom(null);
        Assertions.assertThatThrownBy(() -> salleService.updateSalle(salleToUpdate))
                .hasMessage("Le nom d'une salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_salle_should_throw_exception_when_name_is_null()

    @Test
    public void update_salle_should_throw_exception_when_name_already_exists() throws DataBaseException {
        Salle salleToUpdate = initSalleToUpdate();
        salleToUpdate.setNom("2");
        Mockito.when(salleRepository.isExistByName(salleToUpdate.getNom())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> salleService.updateSalle(salleToUpdate))
                .hasMessage("Le nom de la salle existe déjà en base de données")
                .isInstanceOf(ArgumentException.class);
    }//update_salle_should_throw_exception_when_name_already_exists()

    @Test
    public void update_salle_should_throw_exception_when_id_is_null(){
        Salle salleToUpdate = initSalleToUpdate();
        salleToUpdate.setId(null);
        Assertions.assertThatThrownBy(() -> salleService.updateSalle(salleToUpdate))
                .hasMessage("L'identifiant de la salle est obligatoire")
                .isInstanceOf(ArgumentException.class);
    }//update_salle_should_throw_exception_when_id_is_null()

    @Test
    public void delete_salle_should_success_when_id_is_2() throws DataBaseException {
        Mockito.when(salleRepository.delete(2)).thenReturn(true);
        boolean isDeleted = salleService.deleteSalle(2);
        Assertions.assertThat(isDeleted).isTrue();
    }//delete_salle_should_success_when_id_is_2()

    @Test
    public void delete_salle_should_success_when_id_is_30() throws DataBaseException {
        Mockito.when(salleRepository.delete(30)).thenReturn(false);
        boolean isDeleted = salleService.deleteSalle(30);
        Assertions.assertThat(isDeleted).isFalse();
    }//delete_salle_should_success_when_id_is_30()

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

    private Salle initSalleToUpdate(){
        Date now = new Date();
        Salle salle = new Salle();
        salle.setNom("Beaufort");
        salle.setId(1);
        salle.setCreationDate(new Date(1593293125));
        salle.setModificationDate(now);
        return salle;
    }//initSalleToUpdate()

    private Salle initSalleToUpdateFromBd(){
        Date now = new Date();
        Salle salle = new Salle();
        salle.setNom("2");
        salle.setId(1);
        salle.setCreationDate(new Date(1593293125));
        salle.setModificationDate(now);
        return salle;
    }
}//SalleServiceUT
