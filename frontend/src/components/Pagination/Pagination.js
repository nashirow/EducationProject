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

import './style.scss';

/**
 * Génération des numéros de page de la pagination
 * @param {Integer} currentPage page courante
 * @param {Integer} pagesCount nombre total de pages
 */
const pagination = (currentPage, pagesCount) => {
    const delta = 2;
    let range = [];
    for (let i = Math.max(2, currentPage - delta); i <= Math.min(pagesCount - 1, currentPage + delta); i++) {
        range.push(i);
    }
    if (currentPage - delta > 2) {
      range.unshift("...");
    }
    if (currentPage + delta < pagesCount - 1) {
      range.push("....");
    }
    range.unshift(1);
    if(pagesCount > 1){
        range.push(pagesCount);
    }
    return range;
};

/**
 * Composant Pagination
 * @param {Object} props Propriétés du composant
 * @param {Integer} currentPage page courante
 * @param {Integer} pagesCount nombre total de pages
 * @param {Function} action Callback réagissant au clic sur un n° de page
 */
export const Pagination = ({ currentPage, pagesCount, action }) => {
    let paginationLinks = pagination(currentPage, pagesCount);
    let linksPagination = paginationLinks.map((numberpage) => 
        <li key={numberpage}  className={(numberpage === currentPage)?"active":""}>
            <span 
                onClick={() => {action((numberpage !== "..." && numberpage !== "....")?numberpage:"#");} } className={(numberpage !== "..." && numberpage !== "....")?"":"forbidden"}>
                {numberpage}
            </span>
        </li>
    );   
    let previousPage = currentPage-1;
    let nextPage = currentPage+1;
    let prevLinkPagination = (currentPage > 1)?<li key='prev'><span onClick={ () => {action(previousPage);} }> &lt;&lt; </span></li>:"";
    let nextLinkPagination = (currentPage < pagesCount)?<li key='next'><span onClick={ () => {action(nextPage);} }> &gt;&gt; </span></li>:"";
    return (<div className='pagination'>
                <ul>
                    {prevLinkPagination}
                    {linksPagination}
                    {nextLinkPagination}
                </ul>
            </div>
    );
};