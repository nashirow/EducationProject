/*
 * Copyright 2020 Hicham AZIMANI, Yassine AZIMANI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { isKeyDate, renderKey } from '../../utils/Utils';

import './style.scss';
import moment from 'moment';

const renderObject = (obj) => {
    const rows = Object.entries(obj).map(([key, value]) => {
        return (<li key={key}><label htmlFor={key}>{renderKey(key)} :</label>
            <span id={key}>
                {renderPrimaryType(key, value)}
            </span>
        </li>);
    });
    return (<ul className='row-details-object'>{rows}</ul>);
};

const renderPrimaryType = (key, value) => {
    return (isKeyDate(key) ? moment(value).format(process.env.REACT_APP_DATE_FORMAT) : (value || '-'));
};

/**
 * Composant Details permettant d'afficher les détails d'une notion.
 * 
 * @param {Object} props Paramètres du composant
 */
export const Details = (props) => {
    return (<div className='details'>
        <ul>
            {props && props.data 
                && Object.entries(props.data).map(([key, value]) => 
                {
                    return (<li key={key}><label htmlFor={key}>{renderKey(key)} :</label>
                        <span id={key}>
                            {(typeof value === 'string' || typeof value ==='number') && renderPrimaryType(key, value)}
                            {value instanceof Object && renderObject(value)}
                        </span>
                    </li>);
                })
            }
        </ul>
    </div>);
};