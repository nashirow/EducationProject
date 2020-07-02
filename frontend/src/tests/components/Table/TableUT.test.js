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
import { mount } from 'enzyme';
import { Table } from '../../../components/Table/Table';
import { BrowserRouter } from 'react-router-dom';

import '../../configure';

let wrapperTable, wrapperTableWithoutProps;

const header = ['Column A', 'Column B'];

const foot = ['Column A', 'Column B'];

const data = [['Value', 'Another Value'], ['Cool', 'Zen']];

const dataWithActions = [['Value', 'Another Value', null], ['Cool', 'Zen', null]];

beforeEach(() => {
    wrapperTable = mount(<Table id='table-classes' className='table-classes' 
                                header={header} foot={foot} data={data} />);
    wrapperTableWithoutProps = mount(<Table />);
});

describe('Testing component Table', () => {

    test('Component table should have id present when prop id is given', () => {
        expect(wrapperTable.find('table#table-classes').exists()).toBeTruthy();
    });

    test('Component table shouldn\'t have id present when prop id is undefined', () => {
        expect(wrapperTableWithoutProps.find('table#table-classes').exists()).toBeFalsy();
    });

    test('Component table should have class present when prop class is given', () => {
        expect(wrapperTable.find('table.table-classes').exists()).toBeTruthy();
    });

    test('Component table shouldn\'t have class present when prop class is undefined', () => {
        expect(wrapperTableWithoutProps.find('table.table-classes').exists()).toBeFalsy();
    });

    test('Component table should have thead when prop header is given', () => {
        expect(wrapperTable.find('table thead').exists()).toBeTruthy();
        expect(wrapperTable.find('table thead tr th').exists()).toBeTruthy();
        expect(wrapperTable.find('table thead tr th')).toHaveLength(header.length);
    });

    test('Component table shouldn\'t have thead present when prop header is undefined', () => {
        expect(wrapperTableWithoutProps.find('table thead').exists()).toBeFalsy();
    });

    test('Component table should have tfoot when prop foot is given', () => {
        expect(wrapperTable.find('table tfoot').exists()).toBeTruthy();
        expect(wrapperTable.find('table tfoot tr td').exists()).toBeTruthy();
        expect(wrapperTable.find('table tfoot tr td')).toHaveLength(header.length);
    });

    test('Component table shouldn\'t have tfoot present when prop foot is undefined', () => {
        expect(wrapperTableWithoutProps.find('table tfoot').exists()).toBeFalsy();
    });

    test('Component table should have tbody when prop data is given', () => {
        expect(wrapperTable.find('table tbody').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td')).toHaveLength(4);
    });

    test('Component table shouldn\'t have tbody present when prop data is undefined', () => {
        expect(wrapperTableWithoutProps.find('table tbody').exists()).toBeFalsy();
    });

    test('Component table should display column actions if one of values given is null', () => {
        wrapperTable = mount(<Table id='table-classes' className='table-classes' 
                                header={header} foot={foot} data={dataWithActions} />);
        expect(wrapperTable.find('table tbody tr td.actions').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-pencil').exists()).toBeFalsy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-trash').exists()).toBeFalsy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-list-ul').exists()).toBeFalsy();
    });

    test('Component table should display actions buttons if one of values given is null and callbacks (edit, delete, details) given', () => {
        wrapperTable = mount(<BrowserRouter><Table id='table-classes' className='table-classes' 
                                header={header} foot={foot} data={dataWithActions} 
                                edit='/a' delete={() => alert()} details='/b'
                                /></BrowserRouter>);
        expect(wrapperTable.find('table tbody tr td.actions').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-pencil').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-trash').exists()).toBeTruthy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-list-ul').exists()).toBeTruthy();
    });

    test('Component table should not display actions buttons if values aren\'t empty and callbacks (edit, delete, details) given', () => {
        wrapperTable = mount(<BrowserRouter><Table id='table-classes' className='table-classes' 
                                header={header} foot={foot} data={data} 
                                edit='/a' delete={() => alert()} details='/b'
                                /></BrowserRouter>);
        expect(wrapperTable.find('table tbody tr td.actions').exists()).toBeFalsy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-pencil').exists()).toBeFalsy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-trash').exists()).toBeFalsy();
        expect(wrapperTable.find('table tbody tr td.actions .fa-list-ul').exists()).toBeFalsy();
    });

});