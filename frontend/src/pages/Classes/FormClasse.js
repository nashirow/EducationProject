import React, { useState } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { Form } from '../../components/Form/Form';

export const FormClasse = () => {

    const pageName='Creation d\'une classe';

    const [nom, setNom] = useState('');
    const [errors, setErrors] = useState([]);

    const inputForms = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', id: 'nom', name: 'nom', mandatory: true, value: nom, action: (e) => updateState(e) },
    ];

    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-classe', action: () => submitForm() };

    const redirectToClasses = () => {
        window.location.href = process.env.REACT_APP_ENDPOINT_CLASSES;
    };

    const submitForm = async () => {
        setErrors([]);
        try{
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