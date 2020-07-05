import React, { useEffect, useState } from 'react';
import _ from 'lodash';

import { Details } from '../../../components/Details/Details';
import { useParams } from 'react-router-dom';
import { handleResponse } from '../../../utils/Utils';
import { Breadcrumb } from '../../../components/Breadcrumb/Breadcrumb';
import { Message } from '../../../components/Message/Message';

import '../style.scss';

/**
 * Page détails d'un enseignant
 */
export const DetailsTeacher = () => {

    const [errors, setErrors] = useState([]);
    const [teacher, setTeacher] = useState({});
    const { id } = useParams();

    /**
     * Successeur ComponentDidMount
     */
    useEffect(() => {

        const abortController = new AbortController();
        
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_GET_TEACHER}/${id}`, 
                    { method: 'GET', signal: abortController.signal });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);;
                setTeacher(json.value);
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

    return(<main id='details-teacher'>
        <Breadcrumb elements={[{label: `Enseignants`, link: '/teachers' }, {label: `Détails de l'enseignant' n° ${id}`, link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <div className='wrapper-details'>
            <Details data={teacher} />
        </div>
    </main>);
};