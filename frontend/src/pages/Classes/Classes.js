import React, { useEffect, useState } from 'react';
import _ from 'lodash';

import { Table } from '../../components/Table/Table';
import { Message } from '../../components/Message/Message';

import './style.scss';



export const Classes = () => {

    const [classes, setClasses] = useState([]);
    const [errors, setErrors] = useState([]);
    const header = ['Identifiant', 'Nom', 'Actions'];
    
    const handleResponse = (response) => {
        if(!response.ok){
            setErrors();
        }
        return response;
    };


    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {
            try{
                // URL => Fichier properties
                let response = await fetch('http://localhost:8080/classes?page=1&nbElementsPerPage=10', { method: 'GET', signal: abortController.signal });
                response = handleResponse(response);
                const json = await response.json();
                setClasses(json.value.map(val => [val.id, val.nom, null]) || []);
            }catch(err){
                console.error(err);
                setErrors(['Impossible de se connecter au serveur : Veuillez contacter votre responsable informatique']);
            }
        };
        fetchData();

        return () => {
            abortController.abort();
        };

    }, []);

    return(<main id="classes">
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <Table id='table-classes' header={header} data={classes} />
    </main>);
};