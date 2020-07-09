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
import { Button } from '../Button/Button';

import './style.scss';

/**
 * Composant formulaire permettant de générer du formulaire à l'aide de
 * paramètres.
 * @param {*} props Paramètres représentant des champs formulaires.
 * Exemples : 
 * 
 * const params = [
 *       { label: { id: 'label-nom', value: 'Nom' }, type: 'text', id: 'nom', name: 'nom', value: nom, action: (e) => updateState(e) },
 * ];
 * const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-classe', action: () => submitForm() };
 * 
 * <Form params={inputForms} submitParams={submitParams} />
 */
export const Form = (props) => {
    const params = props.params;
    const submitParams = props.submitParams;
    return(<div className='wrapper-form'>
        <form className="forms">
            {
                params && params.map((param, idx) => {
                    if(param.type === 'select'){
                        return (<div key={idx}>
                            <select key={param.id} id={param.id} name={param.name} onChange={param.action} value={param.value}> 
                                {param.options.map(val => <option key={val} value={val}>{val}</option>)}
                            </select>
                        </div>)
                    }else{
                        return (<div key={idx}>
                            { param.label && <label key={param.label.id} id={param.label.id} htmlFor={param.id}>{param.label.value}</label> }
                            <input key={param.id} type={param.type} id={param.id} name={param.name} value={param.value} onChange={param.action} />
                        </div>);
                    }
                })
            }
            {submitParams && (<div id="actions"><Button action={submitParams.action} id={submitParams.id} label={submitParams.label} /></div>) }
        </form>
    </div>);
};