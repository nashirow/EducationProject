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
import { HomeCTA } from '../../../components/HomeCTA/HomeCTA';

import '../../configure';

let wrapper;


describe(('Testing Home CTA'), () => {

    beforeEach(() => {
        wrapper = mount(<HomeCTA label='toto' class='cta' id='titi' icon='fa fa-toto' />); 
    });

    test('HomeCTA must be present', () => {
        expect(wrapper.find(".home-cta").exists()).toBeTruthy();
    });

    test('HomeCTA with class cta should be have class cta', () => {
        expect(wrapper.find(".home-cta.cta").exists()).toBeTruthy();
    });

    test('HomeCTA with id titi should be have id titi', () => {
        expect(wrapper.find("#titi.home-cta").exists()).toBeTruthy();
    });

    test('HomeCTA with icon fa fa-toto should be present', () => {
        expect(wrapper.find(".home-cta .infos .fa.fa-toto").exists()).toBeTruthy();
    });

    test('HomeCTA with label toto should display toto', () => {
        expect(wrapper.find(".home-cta .infos").text()).toBe("toto");
    });

    test('HomeCTA without props should have class home-cta', () => {
        wrapper = mount(<HomeCTA />); 
        expect(wrapper.find(".home-cta").exists()).toBeTruthy();
    });

    test('HomeCTA without props should have class infos (below home-cta part)', () => {
        wrapper = mount(<HomeCTA />); 
        expect(wrapper.find(".home-cta .infos").exists()).toBeTruthy();
    });
    


});