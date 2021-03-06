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
import { Link } from 'react-router-dom';

import './style.scss';

/**
 * Composant fil ariane
 * 
 * @param {Object} props Propriétés fil ariane
 */
export const Breadcrumb = (props) => {
    return (<ul className='breadcrumb'>
        <li><Link to={'home-key'} ><i className='fa fa-home' /></Link></li>
            {props && props.elements && 
                props.elements.map((elem,idx) => 
                    <li className={idx === props.elements.length - 1 ? 'current' : ''} 
                        key={elem.link}>
                            {idx !== props.elements.length - 1 && <Link to={elem.link}>{elem.label}</Link> }
                            {idx === props.elements.length - 1 && elem.label }
                    </li>)
            }
    </ul>);
};