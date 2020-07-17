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
import { BrowserRouter } from 'react-router-dom';
import { mount } from 'enzyme';
import { Home } from '../../../pages/Home/Home';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<BrowserRouter><Home /></BrowserRouter>);
});

describe('Testing Home page', () => {

    test('CTA Classe must be present', () => {
        expect(wrapper.find('#cta-classe').exists()).toBeTruthy();
    });

    test('CTA Classe must have text Classes', () => {
        expect(wrapper.find('#cta-classe .infos').text()).toBe('Classes');
    });

    test('CTA Classe must have icon fa-superscript', () => {
        expect(wrapper.find('#cta-classe i.fa-superscript').exists()).toBeTruthy();
    });

    test('CTA Hours must be present', () => {
        expect(wrapper.find('#cta-hours').exists()).toBeTruthy();
    });

    test('CTA Hours must have text Créneaux horaires', () => {
        expect(wrapper.find('#cta-hours .infos').text()).toBe('Créneaux horaires');
    });

    test('CTA Hours must have icon fa-clock-o', () => {
        expect(wrapper.find('#cta-hours i.fa-clock-o').exists()).toBeTruthy();
    });

    test('CTA Teachers must be present', () => {
        expect(wrapper.find('#cta-teachers').exists()).toBeTruthy();
    });

    test('CTA Teachers must have text Enseignants', () => {
        expect(wrapper.find('#cta-teachers .infos').text()).toBe('Enseignants');
    });

    test('CTA Teachers must have icon fa-address-book', () => {
        expect(wrapper.find('#cta-teachers i.fa-address-book').exists()).toBeTruthy();
    });

    test('CTA Disciplines must be present', () => {
        expect(wrapper.find('#cta-disciplines').exists()).toBeTruthy();
    });

    test('CTA Disciplines must have text Matières', () => {
        expect(wrapper.find('#cta-disciplines .infos').text()).toBe('Matières');
    });

    test('CTA Disciplines must have icon fa-book', () => {
        expect(wrapper.find('#cta-disciplines i.fa-book').exists()).toBeTruthy();
    });

    test('CTA Options must be present', () => {
        expect(wrapper.find('#cta-options').exists()).toBeTruthy();
    });

    test('CTA Options must have text Options', () => {
        expect(wrapper.find('#cta-options .infos').text()).toBe('Options');
    });

    test('CTA Options must have icon fa-cog', () => {
        expect(wrapper.find('#cta-options i.fa-cog').exists()).toBeTruthy();
    });

    test('CTA Planning must be present', () => {
        expect(wrapper.find('#cta-planning').exists()).toBeTruthy();
    });

    test('CTA Planning must have text Emplois du temps', () => {
        expect(wrapper.find('#cta-planning .infos').text()).toBe('Emplois du temps');
    });

    test('CTA Planning must have icon fa-calendar', () => {
        expect(wrapper.find('#cta-planning i.fa-calendar').exists()).toBeTruthy();
    });

    test('CTA Room must be present', () => {
        expect(wrapper.find('#cta-room').exists()).toBeTruthy();
    });

    test('CTA Room must have text Salles', () => {
        expect(wrapper.find('#cta-room .infos').text()).toBe('Salles');
    });

    test('CTA Room must have icon fa-university', () => {
        expect(wrapper.find('#cta-room i.fa-university').exists()).toBeTruthy();
    });

    test('CTA Slots must be present', () => {
        expect(wrapper.find('#cta-slots').exists()).toBeTruthy();
    });

    test('CTA Slots must have text Slots', () => {
        expect(wrapper.find('#cta-slots .infos').text()).toBe('Slots');
    });

    test('CTA Slots must have icon fa-list-alt', () => {
        expect(wrapper.find('#cta-slots i.fa-list-alt').exists()).toBeTruthy();
    });

});