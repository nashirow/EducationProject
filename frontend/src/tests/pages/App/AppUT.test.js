import React from 'react';
import { mount } from 'enzyme';

import App from '../../../App';
import '../../configure';
import { Header } from '../../../components/Header/Header';

let wrapper;

beforeEach(() => {
    wrapper = mount(<App />);
});

describe('Testing App', () => {
    test('Header must be present', () => {
        expect(wrapper.find('#header').exists()).toBeTruthy();
    });
});