import React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { useParams } from 'react-router-dom';

export const FormTeachers = () => {

const {id} = useParams();

const pageName= id ? 'Mise à jour d\'un enseignant' : 'Création d\'un enseignant';

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

    useEffect(() => {
        const abortController = new AbortController();

        const fetchData = async () => {
            try{
                if(id){
                    let response = await fetch(`${process.env.REACT_APP_API_URL_GET_TEACHER}/${id}`, { method: 'GET', signal: abortController.signal});
                    let json = await response.json();
                    response = await handleResponse(setErrors, response, json);
                    setNom(json.value.nom);
                    setPrenom(json.value.prenom);
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
                let response = await fetch(process.env.REACT_APP_API_URL_CREATE_TEACHER,{
                    method: 'POST',
                    headers: {
                       'Accept':'application/json',
                       'Content-Type':'application/json',
                    },
                    body: JSON.stringify({ nom, prenom }),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_TEACHERS);
            }
            else{
                let response = await fetch(process.env.REACT_APP_API_URL_UPDATE_TEACHER,{
                    method: 'PUT',
                    headers: {
                       'Accept':'application/json',
                       'Content-Type':'application/json',
                    },
                    body: JSON.stringify({id, nom, prenom }),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_TEACHERS);
            }    
        }
        catch(err){
            console.log(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };
    
    const paramsInputForm = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', name: 'nom', mandatory: true, value: nom,  action: (e) => updateState(e) },
        { label: { id: 'label-prenom', value: 'Prenom'}, type: 'text', name: 'prenom', mandatory: true, value: prenom, action: (e) => updateState(e) }
    ];
    
    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-enseignant', action: () => submitForm() };

  return(
      <main id="form-teachers">
          <Breadcrumb elements={[{label:'Enseignants', link: process.env.REACT_APP_ENDPOINT_TEACHERS}, {label:pageName, link : ''}]} />
          {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
          <Form params={paramsInputForm} submitParams={submitParams} />            
      </main>
  );  
};