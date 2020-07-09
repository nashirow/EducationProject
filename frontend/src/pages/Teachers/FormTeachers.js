import React, { useState } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';

export const FormTeachers = () => {

const pageName='Création d\'un enseignant';

const [nom, setNom] = useState('');

const [prenom, setPrenom] = useState('');

const [errors, setErrors] = useState([]);

    const updateState = (e) => {

        if(e.target.name === 'nom'){
            setNom(e.target.value);
        }
        else if(e.target.name === 'prenom'){
            setPrenom(e.target.value);
        }
    };
    
    const submitForm = async () => {
      let response = await fetch(process.env.REACT_APP_API_URL_CREATE_TEACHER,{
          method: 'POST',
          headers: {
             'Accept':'application/json',
             'Content-Type':'application/json'
          },
          body: JSON.stringify({ nom, prenom })
      });

      let json = await response.json();
      response = await handleResponse(setErrors, response, json, () => window.location.href='/teachers');
    };
    
    const paramsInputForm = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', name: 'nom', value: nom, action: (e) => updateState(e) },
        { label: { id: 'label-prenom', value: 'Prenom'}, type: 'text', name: 'prenom', value: prenom, action: (e) => updateState(e) }
    ];
    
    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-enseignant', action: () => submitForm() };

  return(
      <main id="form-teachers">
          <Breadcrumb elements={[{label:'Enseignants', link: '/teachers'}, {label:pageName, link : ''}]} />
          {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
          <Form params={paramsInputForm} submitParams={submitParams} />            
      </main>
  );  
};