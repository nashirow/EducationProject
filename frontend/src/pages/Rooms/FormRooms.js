import React, { useState } from 'react';
import _ from 'lodash';

import { Message } from '../../components/Message/Message';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';

export const FormRooms = () => {

    const pageName='Création d\'une salle';

    const [nom, setNom] = useState('');
    const [errors, setErrors] = useState([]);

    const updateState = (e)  => {
        if(e.target.name === 'nom'){
            setNom(e.target.value);
        }
    };

    const submitForm = async () => {
        let response = await fetch(process.env.REACT_APP_API_URL_CREATE_ROOM,{
            method: 'POST',
            headers:{
                'Accept':'application/json',
                'Content-Type':'application/json'
            },
            body: JSON.stringify({nom}) 
        });
        let json = await response.json();
        response = await handleResponse(setErrors, response, json, () => window.location.href='/rooms');
    };

    const paramsInputForm = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', name: 'nom', value: nom, action: (e) => updateState(e) },
    ];

    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-salle', action: () => submitForm() };

    return(
        <main id='form-rooms'>
            <Breadcrumb elements={[{label:'Salles', link: '/rooms'}, {label:pageName, link : ''}]} />
            {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
            <Form params={paramsInputForm} submitParams={submitParams}/>
        </main>  
    );
};