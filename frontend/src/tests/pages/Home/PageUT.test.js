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
import { Classes } from '../../../pages/Classes/Classes';

import '../../configure';
import { BrowserRouter } from 'react-router-dom';

let wrapper;

beforeEach(() => {
    wrapper = mount(<BrowserRouter><Classes /></BrowserRouter>);
});

describe('Testing main page Classes', () => {

    test('Classes page should have identifiant classes', () => {
        expect(wrapper.find('#classes').exists()).toBeTruthy();
    });

    test('Classes page should have table with id table-classes', () => {
        expect(wrapper.find('#classes #table-classes').exists()).toBeTruthy();
    });

});