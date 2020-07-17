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
import { Message } from '../../../components/Message/Message';

import '../../configure';

let wrapper;

const messages = ['message A', 'message B'];

describe('Testing Message Component', () => {

    beforeEach(() => {
        wrapper = mount(<Message messages={messages} />);
    });
    

    test('Message Component should display two messages when messages given as props', () => {
        expect(wrapper.find('.message ul li')).toHaveLength(messages.length);
        expect(wrapper.find('.message ul').html()).toBe('<ul><li>message A</li><li>message B</li></ul>');
    });

    test('Message Component should display class errors when typeMessage prop is errors', () => {
        wrapper = mount(<Message messages={messages} typeMessage='errors' />);
        expect(wrapper.find('.message.errors').exists()).toBeTruthy();
    });

    test('Message Component should display class infos when typeMessage prop is infos', () => {
        wrapper = mount(<Message messages={messages} typeMessage='infos' />);
        expect(wrapper.find('.message.infos').exists()).toBeTruthy();
    });

    test('Message Component should display class confirmation when typeMessage prop is confirmation', () => {
        wrapper = mount(<Message messages={messages} typeMessage='confirmation' />);
        expect(wrapper.find('.message.confirmation').exists()).toBeTruthy();
    });

});