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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public void get_salles_should_return_all_results_when_no_filters_given(){
        List<Salle> salles = salleService.getSalles(null,null,null);
        Assertions.assertThat(salles).isNotEmpty();
        Assertions.assertThat(salles).hasSize(4);

        Assertions.assertThat(salles.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(salles.get(0).getNom()).isEqualTo("B240");
        Assertions.assertThat(salles.get(0).getCreationDate()).isNotNull();
        Assertions.assertThat(salles.get(0).getModificationDate()).isNotNull();
        Assertions.assertThat(salles.get(0).getCreationDate()).isNotEqualTo(salles.get(0).getModificationDate());

        Assertions.assertThat(salles.get(1).getId()).isEqualTo(2);
        Assertions.assertThat(salles.get(1).getNom()).isEqualTo("Amphithéâtre B2");
        Assertions.assertThat(salles.get(1).getCreationDate()).isNotNull();
        Assertions.assertThat(salles.get(1).getModificationDate()).isNotNull();
        Assertions.assertThat(salles.get(1).getCreationDate()).isNotEqualTo(salles.get(1).getModificationDate());

        Assertions.assertThat(salles.get(2).getId()).isEqualTo(3);
        Assertions.assertThat(salles.get(2).getNom()).isEqualTo("Amphithéâtre A4");
        Assertions.assertThat(salles.get(2).getCreationDate()).isNotNull();
        Assertions.assertThat(salles.get(2).getModificationDate()).isNotNull();
        Assertions.assertThat(salles.get(2).getCreationDate()).isNotEqualTo(salles.get(2).getModificationDate());

        Assertions.assertThat(salles.get(3).getId()).isEqualTo(4);
        Assertions.assertThat(salles.get(3).getNom()).isEqualTo("B0-08");
        Assertions.assertThat(salles.get(3).getCreationDate()).isNotNull();
        Assertions.assertThat(salles.get(3).getModificationDate()).isNotNull();
        Assertions.assertThat(salles.get(3).getCreationDate()).isNotEqualTo(salles.get(3).getModificationDate());
    }//get_salles_should_return_all_results_when_no_filters_given()


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

    @Test
    public void get_salle_should_return_salle_when_id_is_1() throws DataBaseException {
        Salle salleFromBd = initSalleFromBd();
        Mockito.when(salleRepository.findById(1)).thenReturn(Optional.of(salleFromBd));
        Optional<Salle> optSalle = salleService.getSalle(1);
        Assertions.assertThat(optSalle.isPresent()).isTrue();
        optSalle.ifPresent(salle ->{
            Assertions.assertThat(salle.getId()).isEqualTo(salleFromBd.getId());
            Assertions.assertThat(salle.getNom()).isEqualTo(salleFromBd.getNom());
            Assertions.assertThat(salle.getCreationDate()).isNotNull();
            Assertions.assertThat(salle.getModificationDate()).isNotNull();
            Assertions.assertThat(salle.getCreationDate()).isNotEqualTo(salle.getModificationDate());
        });
    }//get_salle_should_return_salle_when_id_is_1()

    @Test
    public void get_salle_should_return_empty_salle_when_id_is_30() throws DataBaseException {
        Mockito.when(salleRepository.findById(30)).thenReturn(Optional.empty());
        Optional <Salle> optSalle = salleService.getSalle(30);
        Assertions.assertThat(optSalle).isNotPresent();
    }//get_salle_should_return_empty_salle_when_id_is_30()

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

    private List<Salle> initSalles(){
        List<Salle> salle = new ArrayList<>();
        Date now = new Date();
        Salle salle1 = new Salle(1,"B240",new Date(1593358317),now);
        Salle salle2 = new Salle(2,"Amphithéâtre B2", new Date(1593358317), now);
        Salle salle3 = new Salle(3,"Amphithéâtre A4", new Date(1593358317), now);
        Salle salle4 = new Salle(4,"B0-08", new Date(1593358317), now);
        salle.add(salle1); salle.add(salle2); salle.add(salle3); salle.add(salle4);
        return salle;
    }//initSalles()

    private Salle initSalleFromBd(){
        Date now = new Date();
        Salle salle = new Salle();
        salle.setNom("B243");
        salle.setCreationDate(new Date(1593358317));
        salle.setModificationDate(now);
        salle.setId(1);
        return salle;
    }//initSalleFromBd()
}//SalleServiceUT
