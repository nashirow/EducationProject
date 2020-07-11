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

import com.education.project.exceptions.DataBaseException;
import com.education.project.model.Jour;
import com.education.project.persistence.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cette classe contient les fonctionnalités liées aux jours
 */
@Service
public class DayService {

    private DayRepository dayRepository;

    @Autowired
    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }//DayService()

    /**
     * Cette fonction permet de récupérer les jours en base de données
     * @return Liste des jours
     * @throws DataBaseException
     */
    public List<Jour> getDays() throws DataBaseException {
        return dayRepository.getDays();
    }//getDays()
}//DayService
