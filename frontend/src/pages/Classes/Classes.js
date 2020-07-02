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

/**
 * Page classes
 */
export const Classes = () => {

    const [classes, setClasses] = useState([]);
    const [errors, setErrors] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const header = ['Identifiant', 'Nom', 'Actions'];
    
    /**
     * Gestion d'une réponse HTTP négative
     * @param {Object} response 
     */
    const handleResponse = async (response) => {
        if(!response.ok){
            console.log(response);
            const json = await response.json();
            setErrors(json.erreurs);
            console.log(response);
        }
        return response;
    };

    /**
     * Successeur de ComponentDidMount
     */
    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_CLASSES}?page=${page}&nbElementsPerPage=${process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE}`, 
                    { method: 'GET', signal: abortController.signal });
                response = await handleResponse(response);
                let json = await response.json();
                setClasses(json.value.map(val => [val.id, val.nom, null]) || []);

                response = await fetch(`${process.env.REACT_APP_API_URL_COUNT_CLASSES}`, 
                    { method: 'GET', signal: abortController.signal });
                response = await handleResponse(response);
                json = await response.json();
                setTotalPages(Math.ceil(json.value/parseInt(process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE)));
            }catch(err){
                console.error(err);
                setErrors(['Impossible de se connecter au serveur : Veuillez contacter votre responsable informatique']);
            }
        };
        fetchData();

        return () => {
            abortController.abort();
        };

    }, [page, totalPages]);

    /**
     * Récupération de toutes les classes
     * @param {Integer} numPage n° de la page
     * @param {Object} optionsFetch options API Fetch
     */
    const fetchAllClasses = async (numPage, optionsFetch) => {
        try{
            let response = await fetch(`${process.env.REACT_APP_API_URL_CLASSES}?page=${numPage}&nbElementsPerPage=${process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE}`, optionsFetch);
            response = await handleResponse(response);
            let json = await response.json();
            setClasses(json.value.map(val => [val.id, val.nom, null]) || []);

            response = await fetch(`${process.env.REACT_APP_API_URL_COUNT_CLASSES}`, optionsFetch);
            response = await handleResponse(response);
            json = await response.json();
            setTotalPages(Math.ceil(json.value/parseInt(process.env.REACT_APP_TABLE_NB_ELEMENTS_PER_PAGE)));
        }catch(err){
            console.error(err);
            setErrors(['Impossible de se connecter au serveur : Veuillez contacter votre responsable informatique']);
        }
    };

    /**
     * Callback suppression d'une classe
     * @param {Integer} id Identifiant de la classe à supprimer
     */
    const deleteClasse = async (id) => {
        if(window.confirm('Confirmez-vous la suppression de la classe n°' + id + ' ?')){
            try{
                const response = await fetch(`${process.env.REACT_APP_API_URL_DELETE_CLASSE}/${id}`, { method: 'DELETE' });
                await handleResponse(response);
                await fetchAllClasses(page, { method: 'GET' });  
            }catch(err){
                console.error(err);
                setErrors(['Impossible de se connecter au serveur : Veuillez contacter votre responsable informatique']);
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
    
    return(<main id="classes">
        <Breadcrumb elements={[{label: 'Classes', link: '' }]} />
        <div className='page-actions'>
            <Button id='create-classe' to='/' label='Créer une classe' />
        </div>
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <Table id='table-classes' header={header} data={classes} 
            details='/' edit='/' delete={(id) => deleteClasse(id)}
        />
        <Pagination currentPage={page} pagesCount={totalPages} action={changePage}/>
    </main>);
};