import React, { useState } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';

export const FormSlot = () => {

    const pageName= 'Création d\'un slot';

    const [comment, setComment] = useState('');
    
    const [couleurFond, setCouleurFond] = useState('');

    const [couleurPolice, setCouleurPolice] = useState('');

    const [timeSlot, setTimeSlot] = useState('');

    const [enseignant, setEnseignant] = useState('');

    const [matiere, setMatiere] = useState('');

    const [salle, setSalle] = useState('');

    const [errors, setErrors] = useState([]);

    const updateState = (e) => {
        if(e.target.name === 'comment'){
            setComment(e.target.value);
        }
        else if(e.target.name === 'couleurFond'){
            setCouleurFond(e.target.value);
        }
        else if(e.target.name === 'couleurPolice'){
            setCouleurPolice(e.target.value);
        }
        else if(e.target.name === 'timeSlot'){
            setTimeSlot(e.target.value);
        }
        else if(e.target.name === 'enseignant'){
            setEnseignant(e.target.value);
        }
        else if(e.target.name === 'matiere'){
            setMatiere(e.target.value);
        }
        else if(e.target.name === 'salle'){
            setSalle(e.target.value);
        }
    };

    const submitForm = async () => {
        try{
            let response = await fetch(process.env.REACT_APP_API_URL_CREATE_SLOT,{
                method: 'POST',
                headers:{
                    'Accept':'application/json',
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({comment, couleurFond, couleurPolice, timeSlot, enseignant, matiere, salle})
            });
            let json = await response.json();
            response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_SLOTS)
        }
        catch(err){
            console.log(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };
 
    const paramInputForm = [
        {label: {id:'label-comment', value:'Commentaire'}, type: 'text', name:'comment', value: comment, action: (e) => updateState(e) },
        {label: {id:'label-couleurFond', value:'Couleur du fond'}, type: 'text', name:'couleurFond', value: couleurFond, action: (e) => updateState(e) },
        {label: {id:'label-couleurPolice', value:'Couleur de la police'}, type: 'text', name:'couleurPolice', value: couleurPolice, action: (e) => updateState(e) },
        {label: {id:'label-timeSlot', value:'Créneau horaire'}, type: 'text', name:'timeSlot', value: timeSlot, action: (e) => updateState(e) },
        {label: {id:'label-enseignant', value:'Enseignant'}, type: 'text', name:'enseignant', value: enseignant, action: (e) => updateState(e) },
        {label: {id:'label-matière', value:'Matière'}, type: 'text', name:'matière', value: matiere, action: (e) => updateState(e) },
        {label: {id:'label-salle', value:'Salle'}, type: 'text', name:'salle', value: salle, action: (e) => updateState(e) }
    ];

    const submitParam = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-slot', action: () => submitForm() };

    return (<main id='form-slot'>
            <Breadcrumb elements={[{label:'Créneaux horaires', link: process.env.REACT_APP_ENDPOINT_SLOTS}, {label:pageName, link : ''}]} />
            {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
            <Form params={paramInputForm} submitParams={submitParam} />
            </main>);
}