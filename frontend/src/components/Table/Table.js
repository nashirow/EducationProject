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
 * Composant Table permettant de gérer des tableaux HTML.
 * Attention : La première valeur de l'attribut data doit représenter
 * l'identifiant d'un élément. Les boutons d'actions ne s'affichent qu'avec
 * la présence du lien/callback adéquat ainsi que la présence d'un NULL (représenant
 * la valeur de la colonne actions) parmi les valeurs de props.data
 * @param {Object} props Propriétés du composant
 * @param {String} edit Lien de la page d'édition de l'élément (optionnel)
 * @param {String} details Lien de la page affichant les détails de l'élément (optionnel)
 * @param {Function} delete Callback permettant de supprimer l'élément (optionnel)
 * @param {Array} header Tableau contenant les noms des colonnes pour l'en-tête du tableau.
 * @param {Array} data Matrice contenant les lignes du tableau. Un élément de la ligne à NULL permet d'afficher les boutons d'actions.
 * @param {Array} foot Tableau contenant les noms des colonnes pour le pied du tableau (optionnel)
 */
export const Table = (props) => {
    return (<table id={props.id} className={`${props.className} table`}>
        {props.header && 
            <thead>
                <tr>{props.header.map(columnName => <th key={columnName}>{columnName}</th>)}</tr>
            </thead>
        }
        {props.data &&
            <tbody>
                {props.data.map((values, idx) => <tr key={idx}>
                    {values.map(value => {
                        if(value){
                            return (<td key={value}>{value}</td>);
                        }else{
                            return (<td className='actions' key='actions'>
                                    {props && props.edit && <Link to={`${props.edit}/${values[0]}`}><i className='fa fa-pencil' title='Modifier' /></Link> } 
                                    {props && props.delete && <i className='fa fa-trash' title='Supprimer' onClick={(e) => props.delete(values[0])} /> }
                                    {props && props.details && <Link to={`${props.details}/${values[0]}`}><i className='fa fa-list-ul' title='Consulter les détails' /></Link> }
                                </td>)
                        }
                    })}
                </tr>)}
            </tbody>
        }
        {props.foot && 
            <tfoot>
                <tr>{props.foot.map(columnName => <td key={columnName}>${columnName}</td>)}</tr>
            </tfoot>
        }
    </table>);
};