import React from 'react';
import { mount } from 'enzyme';
import { Home } from '../../../pages/Home/Home';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Home />);
})

describe('Testing Home page', () => {

    test('CTA Classe must be present', () => {
        expect(wrapper.find('#cta-classe').exists()).toBeTruthy();
    });

    test('CTA Classe must have text Classes', () => {
        expect(wrapper.find('#cta-classe').text()).toBe('Classes');
    });

    test('CTA Classe must have icon fa-superscript', () => {
        expect(wrapper.find('#cta-classe i.fa-superscript').exists()).toBeTruthy();
    });

    test('CTA Hours must be present', () => {
        expect(wrapper.find('#cta-hours').exists()).toBeTruthy();
    });

    test('CTA Hours must have text Créneaux horaires', () => {
        expect(wrapper.find('#cta-hours').text()).toBe('Créneaux horaires');
    });

    test('CTA Hours must have icon fa-clock-o', () => {
        expect(wrapper.find('#cta-hours i.fa-clock-o').exists()).toBeTruthy();
    });

    test('CTA Teachers must be present', () => {
        expect(wrapper.find('#cta-teachers').exists()).toBeTruthy();
    });

    test('CTA Teachers must have text Enseignants', () => {
        expect(wrapper.find('#cta-teachers').text()).toBe('Enseignants');
    });

    test('CTA Teachers must have icon fa-address-book', () => {
        expect(wrapper.find('#cta-teachers i.fa-address-book').exists()).toBeTruthy();
    });

    
});