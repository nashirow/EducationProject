import React from 'react';
import { mount } from 'enzyme';
import { Classes } from '../../../pages/Classes/Classes';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Classes />);
});

describe('Testing main page Classes', () => {

    test('Classes page should have identifiant classes', () => {
        expect(wrapper.find('#classes').exists()).toBeTruthy();
    });

    test('Classes page should have table with id table-classes', () => {
        expect(wrapper.find('#classes #table-classes').exists()).toBeTruthy();
    });

});