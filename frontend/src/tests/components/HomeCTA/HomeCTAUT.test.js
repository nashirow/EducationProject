import React from 'react';
import { mount } from 'enzyme';
import { HomeCTA } from '../../../components/HomeCTA/HomeCTA';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<HomeCTA label='toto' class='cta' id='titi' icon='fa fa-toto' />); 
});

describe(('Testing Home CTA'), () => {

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