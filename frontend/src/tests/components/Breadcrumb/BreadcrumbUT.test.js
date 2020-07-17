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
import { Breadcrumb } from '../../../components/Breadcrumb/Breadcrumb';

import '../../configure';

let wrapper;

describe('Testing Breadcrumb component', () => {

    beforeEach(() => {
        wrapper = mount(<BrowserRouter><Breadcrumb /></BrowserRouter>);
    });

    test('Breadcrumb should have class name breadcrumb', () => {
        expect(wrapper.find('.breadcrumb').exists()).toBeTruthy();
    });

    test('Breadcrumb should always have icon home when no elements given', () => {
        expect(wrapper.find('.breadcrumb li i.fa-home').exists()).toBeTruthy();
    });

    test('Breadcrumb should display icon home / A / B when elements are given to props', () => {
        const elements = [{label: 'A', link: '/a'}, {label: 'B', link: '/b'}];
        wrapper = mount(<BrowserRouter><Breadcrumb elements={elements} /></BrowserRouter>);
        const breadcrumb = wrapper.find('.breadcrumb').html();
        expect(breadcrumb).toBe('<ul class="breadcrumb"><li><a href="/home-key"><i class="fa fa-home"></i></a></li><li class=""><a href="/a">A</a></li><li class="current">B</li></ul>');
    });

});