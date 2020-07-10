import React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { useParams } from 'react-router-dom';

export const FormSlot = () => {

    const {id} = useParams();

    const pageName= id ? 'Mise à jour d\'un slot' : 'Création d\'un slot';

    const [comment, setComment] = useState('');
    
    const [couleurFond, setCouleurFond] = useState('');

    const [couleurPolice, setCouleurPolice] = useState('');

    const [timeSlot, setTimeSlot] = useState('');

    const [enseignant, setEnseignant] = useState('');

    const [matiere, setMatiere] = useState('');

    const [salle, setSalle] = useState('');

    const [jour, setJour] = useState('');

    const [salles, setSalles] = useState([]);

    const [enseignants, setEnseignants] = useState([]);

    const [matieres, setMatieres] = useState([]);

    const [timeSlots, setTimeSlots] = useState([]);

    const [jours, setJours] = useState([]);

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
        else if(e.target.name === 'jour'){
            setJour(e.target.value);
        }
    };

    const submitForm = async () => {
        setErrors([]);
        try{
            if(!id){
                let response = await fetch(process.env.REACT_APP_API_URL_CREATE_SLOT,{
                    method: 'POST',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({comment, couleurFond, couleurPolice, timeSlot: {id: timeSlot}, enseignant: {id: enseignant}, matiere: {id: matiere}, salle: {id: salle}, jour: {id: jour}}),
                });
    
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_SLOTS)
            }
            else{
                let response = await fetch(process.env.REACT_APP_API_URL_UPDATE_SLOT,{
                    method: 'PUT',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({id,comment, couleurFond, couleurPolice, timeSlot: {id: timeSlot}, enseignant: {id: enseignant}, matiere: {id: matiere}, salle: {id: salle}, jour: {id: jour}}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_SLOTS)
            }  
        }
        catch(err){
            console.log(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };
 
    const paramInputForm = [
        {label: {id:'label-comment', value:'Commentaire'}, type: 'text', name:'comment', value: comment, action: (e) => updateState(e) },
        {label: {id:'label-couleurFond', value:'Couleur du fond'}, type: 'text', placeholder: 'Exemple : #fff', name:'couleurFond', mandatory: true, value: couleurFond, action: (e) => updateState(e) },
        {label: {id:'label-couleurPolice', value:'Couleur de la police'}, type: 'text', placeholder: 'Exemple : #000', name:'couleurPolice', mandatory: true, value: couleurPolice, action: (e) => updateState(e) },
        {label: {id:'label-timeSlot', value:'Créneau horaire'}, type: 'select', name:'timeSlot', value: timeSlot, mandatory: true, options: timeSlots, action: (e) => updateState(e) },
        {label: {id:'label-enseignant', value:'Enseignant'}, type: 'select', name:'enseignant', value: enseignant, mandatory: true, options: enseignants, action: (e) => updateState(e) },
        {label: {id:'label-matiere', value:'Matière'}, type: 'select', name:'matiere', value: matiere, mandatory: true, options: matieres, action: (e) => updateState(e) },
        {label: {id:'label-salle', value:'Salle'}, type: 'select', name:'salle', value: salle, mandatory: true, options: salles, action: (e) => updateState(e) },
        {label: {id:'label-jour', value:'Jour'}, type: 'select', name:'jour', value: jour, mandatory: true, options: jours, action: (e) => updateState(e) }
    ];

    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_ROOMS}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setSalles(json.value.map(val => ({value: val.id, label: val.nom})));
                setSalle(json.value[0].id);
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_TEACHERS}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setEnseignants(json.value.map(val => ({value: val.id, label: `${val.nom} ${val.prenom}`})));
                setEnseignant(json.value[0].id);
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_DISCIPLINES}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setMatieres(json.value.map(val => ({value: val.id, label: `${val.nom}`})));
                setMatiere(json.value[0].id);
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_TIMESLOTS}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setTimeSlots(json.value.map(val => ({value: val.id, label: `${val.start} - ${val.end}`})));
                setTimeSlot(json.value[0].id);
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }

            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_DAYS}`, { method: 'GET', signal: abortController.signal});
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setJours(json.value.map(val => ({value: val.id, label: val.nom})));
                setJour(json.value[0].id);
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

    const submitParam = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-slot', action: () => submitForm() };

    return (<main id='form-slot'>
            <Breadcrumb elements={[{label:'Créneaux horaires', link: process.env.REACT_APP_ENDPOINT_SLOTS}, {label:pageName, link : ''}]} />
            {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
            <Form params={paramInputForm} submitParams={submitParam} />
            </main>);
}