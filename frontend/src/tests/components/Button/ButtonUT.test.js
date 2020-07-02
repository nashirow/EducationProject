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
import { Button } from '../../../components/Button/Button';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<BrowserRouter><Button /></BrowserRouter>);
});

describe('Testing Button Component', () => {

    test('Button component should have a class button', () => {
        expect(wrapper.find('.button').exists()).toBeTruthy();
    });

    test('Button component should display label when prop label is given', () => {
        wrapper = mount(<BrowserRouter><Button label='Bouton' /></BrowserRouter>);
        expect(wrapper.find('.button').text()).toBe('Bouton');
    });

    test('Button component should display label when prop label is given (Link version)', () => {
        wrapper = mount(<BrowserRouter><Button to='/' label='Bouton' /></BrowserRouter>);
        expect(wrapper.find('.button').text()).toBe('Bouton');
    });

    test('Button component should have a id when no prop id is given', () => {
        wrapper = mount(<BrowserRouter><Button to='/' id='toto' /></BrowserRouter>);
        expect(wrapper.find('#toto.button').exists()).toBeTruthy();
    });

    test('Button component should not have a link when no props given', () => {
        wrapper = mount(<BrowserRouter><Button /></BrowserRouter>);
        expect(wrapper.find('.button a').exists()).toBeFalsy();
    });

    test('Button component should have a link when prop to (href) is given', () => {
        wrapper = mount(<BrowserRouter><Button to='/' /></BrowserRouter>);
        expect(wrapper.find('.button a').exists()).toBeTruthy();
    });

    test('Button component should not have a link when prop action is given', () => {
        wrapper = mount(<BrowserRouter><Button action={() => console.log('test')} /></BrowserRouter>);
        expect(wrapper.find('.button a').exists()).toBeFalsy();
        wrapper = mount(<BrowserRouter><Button to='/' action={() => console.log('test')} /></BrowserRouter>);
        expect(wrapper.find('.button a').exists()).toBeFalsy();
    });

});