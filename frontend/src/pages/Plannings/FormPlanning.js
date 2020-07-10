import React, { useState, useEffect } from 'react';
import _ from 'lodash';

import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Form } from '../../components/Form/Form';
import { handleResponse } from '../../utils/Utils';
import { Message } from '../../components/Message/Message';
import { useParams } from 'react-router-dom';

export const FormPlanning = () => {

const [nom, setNom] = useState('');
const [classe, setClasse] = useState('');
const [classes, setClasses] = useState([]);
const [slotsSelected, setSlotsSelected] = useState([]);
const [slots, setSlots] = useState([]);
const [wednesdayUsed, setWednesdayUsed] = useState(true);
const [saturdayUsed, setSaturdayUsed] = useState(false);
const [errors, setErrors] = useState([]);

const { id } = useParams();

const pageName = id ? `Mise à jour du planning n°${id}` : 'Création d\'un planning';

    const updateState = (e) => {
        switch(e.target.name){
            case 'nom':
                setNom(e.target.value);
                break;
            case 'slots':
                let tmp = slotsSelected;
                if(tmp.includes(e.target.value)){
                    tmp = tmp.filter(val => !val === e.target.value);
                }else{
                    tmp.push(e.target.value);
                }
                setSlotsSelected(tmp);
                break;
            case 'classe':
                setClasse(e.target.value);
                break;
            case 'wednesdayUsed':
                setWednesdayUsed(e.target.checked);
                break;
            case 'saturdayUsed':
                setSaturdayUsed(e.target.checked);
                break;
            default:
                break;
        };
    };
    
    const submitForm = async () => {
      const slots = slotsSelected.map(id => ({ id }) );
      let response;
        if(!id){
            response = await fetch(process.env.REACT_APP_API_URL_CREATE_PLANNING,{
                method: 'POST',
                headers: {
                   'Accept':'application/json',
                   'Content-Type':'application/json'
                },
                body: JSON.stringify({ nom, classe: { id: classe }, slots, wednesdayUsed, saturdayUsed })
            });
        }else{
            response = await fetch(process.env.REACT_APP_API_URL_UPDATE_PLANNING,{
                method: 'PUT',
                headers: {
                   'Accept':'application/json',
                   'Content-Type':'application/json'
                },
                body: JSON.stringify({ id, nom, classe: { id: classe }, slots, wednesdayUsed, saturdayUsed })
            });
        }

      let json = await response.json();
      response = await handleResponse(setErrors, response, json, () => window.location.href = process.env.REACT_APP_ENDPOINT_PLANNINGS);
    };

    /**
     * Successeur de ComponentDidMount
     */
    useEffect(() => {
        const abortController = new AbortController();
        
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_SLOTS}`, 
                    { method: 'GET', signal: abortController.signal });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                
                setSlots(json.value.map(val => ({value: val.id, label: `${val.jour.nom} ${val.timeSlot.start}-${val.timeSlot.end} ${val.matiere.nom} (Slot n°${val.id})`}) ).sort((a, b) => a.label - b.label ) || []);

                response = await fetch(`${process.env.REACT_APP_API_URL_CLASSES}`, 
                    { method: 'GET', signal: abortController.signal });
                json = await response.json();
                response = await handleResponse(setErrors, response, json);
                
                setClasses(json.value.map(val => ({value: val.id, label: val.nom}) ) || []);
                setClasse(json.value[0].id);
                if(id){ // Préchargement du formulaire si c'est une mise à jour
                    response = await fetch(`${process.env.REACT_APP_API_URL_GET_PLANNING}/${id}`, 
                    { method: 'GET', signal: abortController.signal });
                    let json = await response.json();
                    response = await handleResponse(setErrors, response, json);
                    const slotLoaded = json.value;
                    setNom(slotLoaded.nom);
                    setClasse(slotLoaded.classe.id);
                    setSaturdayUsed(slotLoaded.saturdayUsed);
                    setWednesdayUsed(slotLoaded.wednesdayUsed);
                    setSlotsSelected(slotLoaded.slots.map(slot => slot.id));
                }
            
            }catch(err){
                console.error(err);
                setErrors([process.env.REACT_APP_GENERAL_ERROR]);
            }
        };

        fetchData();
        return () => {
            abortController.abort();
        };
    }, [id]);
    
    const paramsInputForm = [
        { label: { id: 'label-nom', value: 'Nom' }, type: 'text', name: 'nom', value: nom, action: (e) => updateState(e) },
        { label: { value: 'Sélectionnez une classe ' }, type: 'select', id: 'classe', name: 'classe', value: classe, options: classes, action: (e) => updateState(e) },
        { label: { value: 'Sélectionnez un ou plusieurs slots ' }, type: 'select', multiple: true, id: 'slots', name: 'slots', checked: slotsSelected, value: slotsSelected, options: slots, action: (e) => updateState(e) },
        { label: { id: 'label-wednesday-used', value: 'Activer le mercredi ?' }, type: 'checkbox', name: 'wednesdayUsed', checked: wednesdayUsed, action: (e) => updateState(e) },
        { label: { id: 'label-saturday-used', value: 'Activer le samedi ?' }, type: 'checkbox', name: 'saturdayUsed', checked:saturdayUsed , action: (e) => updateState(e) },
    ];
    
    const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-planning', action: () => submitForm() };

  return(
      <main id="form-planning">
          <Breadcrumb elements={[{label:'Emplois du temps', link: process.env.REACT_APP_ENDPOINT_PLANNINGS}, {label:pageName, link : ''}]} />
          {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
          <Form params={paramsInputForm} submitParams={submitParams} />            
      </main>
  );  
};