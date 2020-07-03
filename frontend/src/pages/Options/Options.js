import React, { useState, useEffect } from 'react';
import _ from 'lodash';
import { Breadcrumb } from '../../components/Breadcrumb/Breadcrumb';
import { Message } from '../../components/Message/Message';
import { handleResponse } from '../../utils/Utils';

/**
 * Page Options
 */
export const Options = () => {

    const [errors, setErrors] = useState([]);
    const [startHourPlanning, setStartHourPlanning] = useState('');
    const [endHourPlanning, setEndHourPlanning] = useState('');
    const [splitPlanning, setSplitPlanning] = useState('');

    /**
     * Successeur du ComponentDidMount
     */
    useEffect(() => {
        const abortController = new AbortController();
        const fetchData = async () => {
            let response = await fetch(`${process.env.REACT_APP_API_URL_OPTIONS}`, { method: 'GET', signal: abortController.signal});
            let json = await response.json();
            response = await handleResponse(setErrors, response, json);
            setStartHourPlanning(json.value.startHourPlanning);
            setEndHourPlanning(json.value.endHourPlanning);
            setSplitPlanning(json.value.splitPlanning);
        };

        fetchData();

        return () => {
            abortController.abort();
        };
    }, []);
    
    const changeOptions = (event) => {
        switch(event.target.name){
            case 'startHourPlanning':
                setStartHourPlanning(event.target.value);
                break;
            default:
                break;    
        }
    };

    return(<main id='options'>
        <Breadcrumb elements={[{label: 'Options', link: '' }]} />
        {!_.isEmpty(errors) && <Message typeMessage='errors' messages={errors} />}
        <div className='wrapper-form'>
            <form id='form-options' className='forms'>
                <div>
                    <label for='startHourPlanning'>Toto</label>
                    <input type='text' name='startHourPlanning' onChange={(e) => changeOptions(e)} value={startHourPlanning} />
                </div>
                <div>
                    <input type='text' name='endHourPlanning' onChange={(e) => changeOptions(e)} value={endHourPlanning} />
                </div>
                <div>
                    <input type='number' step={1}  name='splitPlanning' onChange={(e) => changeOptions(e)} value={splitPlanning} />
                </div>
            </form>
        </div>
    </main>)
};