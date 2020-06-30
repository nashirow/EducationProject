import React from 'react';

import './style.scss';

export const HomeCTA = (props) => {
    return(<div className={`${props.class} home-cta`} id={props.id}>
                <div className='infos'>
                    <i className={`${props.icon}`} aria-hidden="true" />
                    {props.label || ''}
                </div>
            </div>);
};