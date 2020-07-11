import  React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Form } from '../../components/Form/Form';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { useParams } from 'react-router-dom';

export const FormMatiere = () => {

    const { id } = useParams();

    const [nom, setNom] = useState('');

    const [description, setDescription] = useState('');

    const [volumeHoraire, setVolumeHoraire] = useState('');

    const [errors, setErrors] = useState([]);

    const pageName = id ? 'Mise à jour d\'une matière' : 'Création d\'une matière';

    const updateState = (e) => {
        if(e.target.name === 'nom'){
            setNom(e.target.value);
        }
        if(e.target.name === 'description'){
            setDescription(e.target.value);
        }
        if(e.target.name === 'volume-horaire'){
            setVolumeHoraire(e.target.value);
        }
    };

    useEffect(() => {
        const abortController = new AbortController();

        const fetchData = async () => {
            try{
                if(id){
                    let response = await fetch(`${process.env.REACT_APP_API_URL_GET_DISCIPLINE}/${id}`, { method: 'GET', signal: abortController.signal});
                    let json = await response.json();
                    response = await handleResponse(setErrors, response, json);
                    setNom(json.value.nom);
                    setDescription(json.value.description);
                    setVolumeHoraire(json.value.volumeHoraire);
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
                let response = await fetch(process.env.REACT_APP_API_URL_CREATE_DISCIPLINE,{
                    method: 'POST',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({nom,description,volumeHoraire}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_DISCIPLINES)
            }
            else{
                let response = await fetch(process.env.REACT_APP_API_URL_UPDATE_DISCIPLINE,{
                    method: 'PUT',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify({id,nom,description,volumeHoraire}),
                });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_DISCIPLINES)
            }
        }
        catch(err){
            console.log(err);
            setErrors([process.env.REACT_APP_GENERAL_ERROR]);
        }
    };

    const paramsInputForm = [
        { label: { id: 'label-nom', value: 'Nom'}, type: 'text', name: 'nom', mandatory: true, value: nom, action: (e) => updateState(e) },
        { label: {id: 'label-description', value: 'Description'}, type: 'text', name: 'description', value: description, action: (e) => updateState(e) },
        { label: {id: 'label-volumeHoraire', value:'Volume horaire'}, type: 'text',  placeholder: 'Exemple : 12:00', name: 'volume-horaire', value: volumeHoraire, action: (e) => updateState(e) }
    ];

    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-matiere', action: () => submitForm() };

    return (
        <main id='form-matiere'>
            <Breadcrumb elements={[{label:'Matières', link: process.env.REACT_APP_ENDPOINT_DISCIPLINES}, {label:pageName, link : ''}]} />
            {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
            <Form params={paramsInputForm} submitParams={submitParams}/>
        </main>
    );
};