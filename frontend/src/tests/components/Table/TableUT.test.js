import React from 'react';
import { mount } from 'enzyme';
import { Table } from '../../../components/Table/Table';

import '../../configure';

let wrapperTable, wrapperTableWithoutProps;

const header = ['Column A', 'Column B'];

const foot = ['Column A', 'Column B'];

const data = [['Value', 'Another Value'], ['Cool', 'Zen']];

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

});