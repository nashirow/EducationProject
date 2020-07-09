import React, { useState } from 'react';
import _ from 'lodash';

import { Button } from '../../components/Button/Button';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';

export const FormClasse = () => {
    const pageName='Creation d\'une classe';

    const [nom, setNom] = useState('');

    const [errors, setErrors] = useState([]);

    const redirectToClasses = () => {
        window.location.href = '/classes';
    };

    const submitForm = async () => {
        let response = await fetch(process.env.REACT_APP_API_URL_CREATE_CLASSE,{
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({nom}),
        });
        let json = await response.json();
        response = await handleResponse(setErrors, response, json, redirectToClasses);
    };

    const updateState = (e) => {
        if(e.target.name === 'nom'){
            setNom(e.target.value);
        } 
    };

    return (<main id="form-classe">
        <Breadcrumb elements={[{label:'Classes', link: '/classes'},{label: pageName, link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <div className="wrapper-form">
            <form className="forms">
                <div>
                    <label id="label-nom" htmlFor="nom">Nom</label>
                    <input type="text" id="nom" name="nom" value={nom} onChange={(e) => updateState(e)} />
                </div>
                <div id="actions">
                    <Button action={() => submitForm()} id="save-classe" label='Enregistrer' />
                </div>
            </form>
        </div>
    </main>
    );
};