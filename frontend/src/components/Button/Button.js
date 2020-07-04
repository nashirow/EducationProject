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
 * Prépare le rendu du bouton avec l'encapsulation d'un lien href
 * @param {Object} props 
 */
const renderButtonWithLinks = (props) => {
    return (<Link to='/'>
        {props && props.label}
    </Link>);
};

/**
 * Prépare le rendu classique du bouton
 * @param {Object} props 
 */
const renderButton = (props) => {
    return (props && props.label);
};

/**
 * Composant Bouton
 * @param {Object} props Propriétés bouton
 */
export const Button = (props) => {
    return (
        <div id={props.id} className='button' onClick={props.action}>
            {props && props.to && !props.action && renderButtonWithLinks(props)}
            {props && !props.to && !props.action && renderButton(props)}
            {props && !props.to && props.action && renderButton(props)}
        </div>
        );
};