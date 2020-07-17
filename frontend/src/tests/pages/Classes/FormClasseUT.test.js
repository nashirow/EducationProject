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
import { FormClasse } from '../../../pages/Classes/FormClasse';
import { BrowserRouter } from 'react-router-dom';

import '../../configure';


let wrapper; 

describe('testing page formclasse', () => {

    beforeEach(() => {
        wrapper = mount(<BrowserRouter><FormClasse /></BrowserRouter>);
    });

    test('form must be present in the page', () => {
        expect(wrapper.find('form').exists()).toBeTruthy();
    });

    test('form should have a label containing Nom', () => {
        expect(wrapper.find('#label-nom').exists()).toBeTruthy();
        expect(wrapper.find('#label-nom').text()).toBe('Nom *');
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

