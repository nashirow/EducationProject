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
import { Form } from '../../../components/Form/Form';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Form />);
});

describe('Testing component Form', () => {

    test('Form component should have classe .wrapper-form', () => {
        const isWrapperFormExists = wrapper.find('div.wrapper-form').exists();
        expect(isWrapperFormExists).toBeTruthy();
    });

    test('Form component should have classe .forms under .wrapper-form', () => {
        const isWrapperFormExists = wrapper.find('div.wrapper-form form.forms').exists();
        expect(isWrapperFormExists).toBeTruthy();
    });

    test('Form component without id doesn\'t render id', () => {
        const isWrapperFormExists = wrapper.find('#form-toto').exists();
        expect(isWrapperFormExists).toBeFalsy();
    });

    test('Form component with id should render id form', () => {
        wrapper = mount(<Form id='form-toto' />);
        const isWrapperFormExists = wrapper.find('#form-toto').exists();
        expect(isWrapperFormExists).toBeTruthy();
    });

    test('Form component should display form with input and label for nom, input and label for password,a submit button', () => {
        const params = [
            { label: { id: 'label-nom', value: 'Nom' }, type: 'text', id: 'nom', name: 'nom', value: '', action: () => { console.log() } },
            { label: { id: 'label-password', value: 'Mot de passe' }, type: 'password', id: 'password', name: 'password', value: '', action: () => { console.log() } },
        ];
        const submitParams = { type: 'button-submit-form', label: 'Enregistrer', id: 'save-classe', action: () => { console.log() } };
        wrapper = mount(<Form params={params} submitParams={submitParams} id='form-test' />);
        const htmlFormGenerated = wrapper.find('#form-test').html();
        expect(htmlFormGenerated).toBe('<div class="wrapper-form"><form class="forms"><div><label id="label-nom" for="nom">Nom</label><input type="text" id="nom" name="nom" value=""></div><div><label id="label-password" for="password">Mot de passe</label><input type="password" id="password" name="password" value=""></div><div id="actions"><div id="save-classe" class="button">Enregistrer</div></div></form></div>');
    });

});

