import React from 'react';

import './style.scss';
import { HomeCTA } from '../../components/HomeCTA/HomeCTA';

export const Home = () => {
    return(<main id="home">
        <HomeCTA label='Classes' class='cta' id='cta-classe' icon='fa fa-superscript' />
        <HomeCTA label='Créneaux horaires' class='cta' id='cta-hours' icon='fa fa-clock-o' />
        <HomeCTA label='Enseignants' class='cta' id='cta-teachers' icon='fa fa-address-book' />
        <HomeCTA label='Matières' class='cta' id='cta-disciplines' icon='fa fa-book' />
        <HomeCTA label='Options' class='cta' id='cta-options' icon='fa fa-cog' />
        <HomeCTA label='Emplois du temps' class='cta' id='cta-planning' icon='fa fa-calendar' />
        <HomeCTA label='Salles' class='cta' id='cta-room' icon='fa fa-university' />
        <HomeCTA label='Slots' class='cta' id='cta-slots' icon='fa fa-list-alt' />
    </main>);
}