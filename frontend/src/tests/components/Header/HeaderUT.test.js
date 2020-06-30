import React from 'react';
import { mount } from 'enzyme';
import { Header } from '../../../components/Header/Header';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Header />);
});

describe(('Testing Header'), () => {
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