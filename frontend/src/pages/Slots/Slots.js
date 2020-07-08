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
import React, { useEffect, useState } from 'react';
import _ from 'lodash';

import { Table } from '../../components/Table/Table';
import { Message } from '../../components/Message/Message';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Button } from '../../components/Button/Button';
import { Pagination } from '../../components/Pagination/Pagination';

import './style.scss';
import { handleResponse } from '../../utils/Utils';

/**
 * Page slots
 */
export const Slots = () => {

    const [slots, setSlots] = useState([]);
    const [errors, setErrors] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const header = ['Identifiant', 'Matière', 'Heure début', 'Heure fin', 'Actions'];

    /**
     * Successeur de ComponentDidMount
     */
    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_SLOTS}?page=${page}&nbElementsPerPage=${process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE}`, 
                    { method: 'GET', signal: abortController.signal });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                
                setSlots(json.value.map(val => [val.id, val.matiere.nom, val.timeSlot.start, val.timeSlot.end, null]) || []);

                response = await fetch(`${process.env.REACT_APP_API_URL_COUNT_SLOTS}`, 
                    { method: 'GET', signal: abortController.signal });
                json = await response.json();
                response = await handleResponse(setErrors, response, json);
                
                setTotalPages(Math.ceil(json.value/parseInt(process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE)));
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        };
        fetchData();

        return () => {
            abortController.abort();
        };

    }, [page, totalPages]);

    /**
     * Récupération de toutes les slots
     * @param {Integer} numPage n° de la page
     * @param {Object} optionsFetch options API Fetch
     */
    const fetchAllClasses = async (numPage, optionsFetch) => {
        try{
            let response = await fetch(`${process.env.REACT_APP_API_URL_SLOTS}?page=${numPage}&nbElementsPerPage=${process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE}`, optionsFetch);
            let json = await response.json();
            response = await handleResponse(setErrors, response, json);
            
            setSlots(json.value.map(val => [val.id, val.matiere.nom, val.timeSlot.start, val.timeSlot.end, null]) || []);

            response = await fetch(`${process.env.REACT_APP_API_URL_COUNT_SLOTS}`, optionsFetch);
            json = await response.json();
            response = await handleResponse(setErrors, response, json);
            
            setTotalPages(Math.ceil(json.value/parseInt(process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE)));
        }catch(err){
            console.error(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };

    /**
     * Callback suppression d'un slot
     * @param {Integer} id Identifiant du slot à supprimer
     */
    const deleteSlot = async (id) => {
        if(window.confirm('Confirmez-vous la suppression du slot n°' + id + ' ?')){
            try{
                const response = await fetch(`${process.env.REACT_APP_API_URL_DELETE_SLOT}/${id}`, { method: 'DELETE' });
                let json = await response.json()
                await handleResponse(setErrors, response, json);
                await fetchAllClasses(page, { method: 'GET' });  
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        }
    };

    /**
     * Callback pagination
     * @param {Integer} numPage Numéro de la page à atteindre
     */
    const changePage = (numPage) => {
        setPage(numPage);
        fetchAllClasses(numPage, { method: 'GET' }); 
    };
    
    return(<main id="slots">
        <Breadcrumb elements={[{label: 'Slots', link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <div className='page-actions'>
            <Button id='create-slot' to='/' label='Créer un slot' />
        </div>
        <Table id='table-slots' header={header} data={slots} 
            details={process.env.REACT_APP_ENDPOINT_DETAILS_SLOT} edit='/' delete={(id) => deleteSlot(id)}
        />
        <Pagination currentPage={page} pagesCount={totalPages} action={changePage}/>
    </main>);
};