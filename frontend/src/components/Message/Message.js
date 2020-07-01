import React from 'react';

import './style.scss';

export const Message = (props) => {
    return (<div className={`message ${props.typeMessage}`}>
        {props.messages && <ul>{props.messages.map(msg => <li key={msg}>{msg}</li>)}</ul>}
    </div>)
};