import React, { useEffect, useState } from 'react';
import _ from 'lodash';

import { Details } from '../../../components/Details/Details';
import { useParams } from 'react-router-dom';
import { handleResponse } from '../../../utils/Utils';
import { Breadcrumb } from '../../../components/Breadcrumb/Breadcrumb';
import { Message } from '../../../components/Message/Message';

import '../style.scss';

/**
 * Page détails d'un slot
 */
export const DetailsSlot = () => {

    const [errors, setErrors] = useState([]);
    const [slot, setSlot] = useState({});
    const { id } = useParams();

    /**
     * Successeur ComponentDidMount
     */
    useEffect(() => {

        const abortController = new AbortController();
        
        const fetchData = async () => {
            try{
                let response = await fetch(`${process.env.REACT_APP_API_URL_GET_SLOT}/${id}`, 
                    { method: 'GET', signal: abortController.signal });
                let json = await response.json();
                response = await handleResponse(setErrors, response, json);
                setSlot(json.value);
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

    return(<main id='details-slot'>
        <Breadcrumb elements={[{label: `Slots`, link: '/slots' }, {label: `Détails du slot n° ${id}`, link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <div className='wrapper-details'>
            <Details data={slot} />
        </div>
    </main>);
};