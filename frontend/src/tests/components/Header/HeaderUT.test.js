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
import { Header } from '../../../components/Header/Header';

import '../../configure';

let wrapper;


describe(('Testing Header'), () => {

    beforeEach(() => {
        wrapper = mount(<Header />);
    });
    
    test('Header must be present', () => {
        expect(wrapper.find("#header").exists()).toBeTruthy();
    });

    test('Software name must be equals to Education Project', () => {
        expect(wrapper.find("#header h1").text()).toBe("Education Project");
    });

    test('Version label must be equals to Version 0.1', () => {
        expect(wrapper.find("#header h2").text()).toBe("Version 0.1");
    });
});