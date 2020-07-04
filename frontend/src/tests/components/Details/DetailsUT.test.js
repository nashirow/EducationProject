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
import { Details } from '../../../components/Details/Details';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Details />);
});

describe('Testing Details component', () => {

    test('Details should have classname details', () => {
        expect(wrapper.find('.details').exists()).toBeTruthy();
    });

    test('Details should be empty when no data given', () => {
        expect(wrapper.find('.details').html()).toBe('<div class="details"><ul></ul></div>');
    });

    test('Details should display some lines when some data given', () => {
        const data = {name: 'toto', age: 10};
        wrapper = mount(<Details data={data} />);
        expect(wrapper.find('.details ul li')).toHaveLength(2);
    });

});