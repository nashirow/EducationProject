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

import { HomeCTA } from '../../components/HomeCTA/HomeCTA';
import { Link } from 'react-router-dom';

import './style.scss';

/**
 * Page d'accueil de l'application
 */
export const Home = () => {
    return(<main id="home">
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_CLASSES}>
            <HomeCTA label='Classes' class='cta' id='cta-classe' icon='fa fa-superscript' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_TIMESLOTS}>
            <HomeCTA label='Créneaux horaires' class='cta' id='cta-hours' icon='fa fa-clock-o' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_TEACHERS}>
            <HomeCTA label='Enseignants' class='cta' id='cta-teachers' icon='fa fa-address-book' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_DISCIPLINES}>
            <HomeCTA label='Matières' class='cta' id='cta-disciplines' icon='fa fa-book' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_OPTIONS}>
            <HomeCTA label='Options' class='cta' id='cta-options' icon='fa fa-cog' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_PLANNINGS}>
            <HomeCTA label='Emplois du temps' class='cta' id='cta-planning' icon='fa fa-calendar' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_ROOMS}>
            <HomeCTA label='Salles' class='cta' id='cta-room' icon='fa fa-university' />
        </Link>
        <Link className='home-cta' to={process.env.REACT_APP_ENDPOINT_SLOTS}>
            <HomeCTA label='Slots' class='cta' id='cta-slots' icon='fa fa-list-alt' />
        </Link>
    </main>);
};