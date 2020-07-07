import React, { useEffect, useState } from 'react';
import _ from 'lodash';

import { useParams } from 'react-router-dom';
import { handleResponse } from '../../../utils/Utils';
import { Breadcrumb } from '../../../components/Breadcrumb/Breadcrumb';
import { Message } from '../../../components/Message/Message';

import '../style.scss';

/**
 * Page détails d'un emploi du temps
 */
export const DetailsPlanning = () => {

    const [errors, setErrors] = useState([]);
    const [warnings, setWarnings] = useState([]);
    const [planning, setPlanning] = useState({});
    const { id } = useParams();

    /**
     * Successeur ComponentDidMount
     */
    useEffect(() => {

        const abortController = new AbortController();
        
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_GET_PLANNING_GENERATED}/${id}`, 
                    { method: 'GET', signal: abortController.signal });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);;
                setPlanning(json.value);
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

    return(<main id='details-planning'>
        <Breadcrumb elements={[{label: `Emplois du temps`, link: '/plannings' }, {label: `Détails de l'emploi du temps n° ${id}`, link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        {!_.isEmpty(warnings) && <Message typeMessage='warnings' messages={warnings} />}
    </main>);
};