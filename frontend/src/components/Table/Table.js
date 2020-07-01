import React from 'react';

import './style.scss';

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
                    {values.map(value => <td key={value}>{value}</td>)}
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