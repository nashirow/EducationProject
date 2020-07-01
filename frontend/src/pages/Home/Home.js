import React from 'react';

import { HomeCTA } from '../../components/HomeCTA/HomeCTA';
import { Link } from 'react-router-dom';

import './style.scss';

export const Home = () => {
    return(<main id="home">
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Classes' class='cta' id='cta-classe' icon='fa fa-superscript' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Créneaux horaires' class='cta' id='cta-hours' icon='fa fa-clock-o' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Enseignants' class='cta' id='cta-teachers' icon='fa fa-address-book' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Matières' class='cta' id='cta-disciplines' icon='fa fa-book' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Options' class='cta' id='cta-options' icon='fa fa-cog' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Emplois du temps' class='cta' id='cta-planning' icon='fa fa-calendar' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Salles' class='cta' id='cta-room' icon='fa fa-university' />
        </Link>
        <Link className='home-cta' to='/classes'>
            <HomeCTA label='Slots' class='cta' id='cta-slots' icon='fa fa-list-alt' />
        </Link>
    </main>);
};