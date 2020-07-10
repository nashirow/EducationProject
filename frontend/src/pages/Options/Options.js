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
import React, { useState, useEffect } from 'react';
import _ from 'lodash';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Message } from '../../components/Message/Message';
import { handleResponse } from '../../utils/Utils';

import './style.scss';
import { Form } from '../../components/Form/Form';

/**
 * Page Options
 */
export const Options = () => {

    const [errors, setErrors] = useState([]);
    const [confirmation, setConfirmation] = useState([]);
    const [startHourPlanning, setStartHourPlanning] = useState('');
    const [endHourPlanning, setEndHourPlanning] = useState('');
    const [splitPlanning, setSplitPlanning] = useState('');
    const [valuesSplitValid, setValuesSplitValid] = useState(['En cours de chargement...']);

    const inputForms = [
        { label: { value: 'Heure de début d\'un emploi du temps' }, type: 'text', id: 'startHourPlanning', name: 'startHourPlanning', value: startHourPlanning, action: (e) => changeOptions(e) },
        { label: { value: 'Heure de fin d\'un emploi du temps' }, type: 'text', id: 'endHourPlanning', name: 'endHourPlanning', value: endHourPlanning, action: (e) => changeOptions(e) },
        { label: { value: 'Découpage en minutes d\'un emploi du temps' }, type: 'select', id: 'splitPlanning', name: 'splitPlanning', value: splitPlanning, options: valuesSplitValid, action: (e) => changeOptions(e) },
    ];
    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-options', action: () => saveOptions() };

    /**
     * Successeur du ComponentDidMount
     */
    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_OPTIONS}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setStartHourPlanning(json.value.startHourPlanning);
                setEndHourPlanning(json.value.endHourPlanning);
                setSplitPlanning(json.value.splitPlanning);
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_OPTIONS_SPLIT_VALUES_VALID}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setValuesSplitValid(json.value.map(val => ({value: val, label: val})));
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        };

        fetchData();

        return () => {
            abortController.abort();
        };
    }, []);
    
    /**
     * Cette fonction met à jour les états de la page Options.
     * @param {Object} event Evenement déclenché par un des champs du formulaire 
     */
    const changeOptions = (event) => {
        switch(event.target.name){
            case 'startHourPlanning':
                setStartHourPlanning(event.target.value);
                break;
            case 'endHourPlanning':
                setEndHourPlanning(event.target.value);
                break;   
            case 'splitPlanning':
                setSplitPlanning(event.target.value);
                break;     
            default:
                break;    
        }
    };

    /**
     * Affiche un message de confirmation lorsque les options ont
     * été correctement mis à jour.
     */
    const showConfirmation = () => {
        setConfirmation([process.env.REACT_APP_CONFIRMATION_OPTIONS_CHANGED]);
    };

    /**
     * Efface les messages
     */
    const clearMessages = () => {
        setErrors([]);
        setConfirmation([]);
    };

    /**
     * Mise à jour des options.
     */
    const saveOptions = async () => {
        clearMessages();
        try{
            let response = await fetch(`${process.env.REACT_APP_API_URL_OPTIONS}`, 
            { 
                method: 'PUT', 
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ startHourPlanning, endHourPlanning, splitPlanning }),
            });
            let json = await response.json();
            response = await handleResponse(setErrors, response, json, showConfirmation);
        }catch(err){
            console.error(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };

    return(<main id='options'>
        <Breadcrumb elements={[{label: 'Options', link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        {!_.isEmpty(confirmation) && <Message typeMessage='confirmation' messages={confirmation} />}
        <Form params={inputForms} submitParams={submitParams} />
    </main>)
};