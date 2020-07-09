import React from 'react';
import { mount } from 'enzyme';
import { FormClasse } from '../../../pages/Classes/FormClasse';
import { BrowserRouter } from 'react-router-dom';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<BrowserRouter><FormClasse /></BrowserRouter>);
}); 

describe('testing page formclasse', () => {
    
    test('form must be present in the page', () => {
        expect(wrapper.find('form').exists()).toBeTruthy();
    });

    test('form should have a label containing Nom', () => {
        expect(wrapper.find('#label-nom').exists()).toBeTruthy();
        expect(wrapper.find('#label-nom').text()).toBe('Nom');
    });

    test('form should have an input', () => {
        expect(wrapper.find('#nom').exists()).toBeTruthy();
        expect(wrapper.find('#nom[type="text"]').exists()).toBeTruthy();
        expect(wrapper.find('#nom[name="nom"]').exists()).toBeTruthy();
    });

    test('submit button should be present with id #save-classe', () => {
        expect(wrapper.find('#save-classe').exists()).toBeTruthy();
    });
});

