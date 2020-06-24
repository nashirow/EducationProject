import React from 'react';

export const Home = () => {
    return(<main id="home">
        <div id="cta-classe">
            <i className="fa fa-superscript" aria-hidden="true" />
            Classes
        </div>
        <div id="cta-hours">
            <i className="fa fa-clock-o" aria-hidden="true" />
            Créneaux horaires
        </div>
        <div id="cta-teachers">
            <i className="fa fa-address-book" aria-hidden="true" />
            Enseignants
        </div>
    </main>);
}