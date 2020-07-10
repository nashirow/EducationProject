import React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { Form } from '../../components/Form/Form';
import { useParams } from 'react-router-dom';

export const FormClasse = () => {

    const { id } = useParams();
    
    const pageName= id ? 'Mise à jour d\'une classe' : 'Creation d\'une classe';

    const [nom, setNom] = useState('');

    const [errors, setErrors] = useState([]);

    const inputForms = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', id: 'nom', name: 'nom', mandatory: true, value: nom, action: (e) => updateState(e) },
    ];

    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-classe', action: () => submitForm() };

    const redirectToClasses = () => {
        window.location.href = process.env.REACT_APP_ENDPOINT_CLASSES;
    };

    useEffect(() => {
        const abortController = new AbortController();

        const fetchData = async () => {
            try{
                if(id){
                    let response = await fetch(`${process.env.REACT_APP_API_URL_GET_CLASSE}/${id}`, { method: 'GET', signal: abortController.signal});
                    let json = await response.json();
                    response = await handleResponse(setErrors, response, json);
                    setNom(json.value.nom);
                }
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        };

        fetchData();
        return () => abortController.abort;
    },[id]);

    const submitForm = async () => {
        setErrors([]);
        try{
            if(!id){
                let response = await fetch(process.env.REACT_APP_API_URL_CREATE_CLASSE,{
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({nom}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, redirectToClasses);
            }
            else{
                let response = await fetch(process.env.REACT_APP_API_URL_UPDATE_CLASSE,{
                    method: 'PUT',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({id, nom}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, redirectToClasses);
            } 
        }
        catch(err){
            console.log(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }  
    };

    const updateState = (e) => {
        if(e.target.name === 'nom'){
            setNom(e.target.value);
        } 
    };

    return (<main id="form-classe">
        <Breadcrumb elements={[{label:'Classes', link: process.env.REACT_APP_ENDPOINT_CLASSES},{label: pageName, link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <Form params={inputForms} submitParams={submitParams} />
    </main>
    );
};