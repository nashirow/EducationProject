import React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { Message } from '../../components/Message/Message';
import { handleResponse } from '../../utils/Utils';
import { useParams } from 'react-router-dom';

export const FormTimeSlot = () => {

    const {id} = useParams();

    const [startHour, setStartHour]  = useState('08:00');

    const [endHour, setEndHour] = useState('10:00');

    const [errors, setErrors] = useState([]);

    const pageName= id ? 'Mise à jour d\'un créneau horaire' : 'Création d\'un créneau horaire';

    const updateState = (e) => {
        if(e.target.name === 'startHour'){
            setStartHour(e.target.value);
        }
        if(e.target.name === 'endHour'){
            setEndHour(e.target.value);
        }
    };

    useEffect(() => {
        const abortController = new AbortController();

        const fetchData = async () => {
            try{
                if(id){
                    let response = await fetch(`${process.env.REACT_APP_API_URL_GET_TIMESLOT}/${id}`, { method: 'GET', signal: abortController.signal});
                    let json = await response.json();
                    response = await handleResponse(setErrors, response, json);
                    console.log(json.value);
                    setStartHour(json.value.start);
                    setEndHour(json.value.end);
                }
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        };

        fetchData();
        return () => abortController.abort;
    },[]);

    const submitForm = async () => {
        setErrors([]);
        try{
            if(!id){
                let response = await fetch(process.env.REACT_APP_API_URL_CREATE_TIMESLOT,{
                    method: 'POST',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({start: startHour,end: endHour}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_TIMESLOTS);
            }
            else{
                let response = await fetch(process.env.REACT_APP_API_URL_UPDATE_TIMESLOT,{
                    method: 'PUT',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({id, start: startHour, end: endHour}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_TIMESLOTS);
            }            
        }
        catch(err){
            console.error(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }      
    };
 
    const paramInputForm = [
        {label: {id: 'label-startHour', value: 'Début du créneau horaire'}, type: 'text', name: 'startHour', mandatory: true, value: startHour, action: (e) => updateState(e) },
        {label: {id: 'label-endHour', value: 'Fin du créneau horaire'}, type: 'text', name: 'endHour', mandatory: true, value: endHour, action: (e) => updateState(e) }
    ];
    
    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-timeslot', action: () => submitForm()};

    return (
        <main id='form-timeslot'>
            <Breadcrumb elements={[{label:'Créneaux horaires', link: process.env.REACT_APP_ENDPOINT_TIMESLOTS}, {label:pageName, link : ''}]} />
            {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
            <Form params={paramInputForm} submitParams={submitParams} />
        </main>
    )
};