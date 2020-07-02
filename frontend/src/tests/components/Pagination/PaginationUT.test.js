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
import { Pagination } from '../../../components/Pagination/Pagination';

import '../../configure';

let wrapper;

beforeEach(() => {
    wrapper = mount(<Pagination currentPage={1} pagesCount={2} action={() => {}} />);
});

describe('Testing Message Component', () => {

    test('Pagination with 2 pages at the current page 1 should display 2 pages and the page 1 should have class active', () => {
        const html = wrapper.find('.pagination').html();
        expect(html).toBe('<div class="pagination"><ul><li class="active"><span class="">1</span></li><li class=""><span class="">2</span></li><li><span> &gt;&gt; </span></li></ul></div>');
    });

    test('Pagination with 2 pages at the current page 2 should display 2 pages and the page 2 should have class active', () => {
        wrapper = mount(<Pagination currentPage={2} pagesCount={2} action={() => {}} />);
        const html = wrapper.find('.pagination').html();
        console.log(html);
        expect(html).toBe('<div class="pagination"><ul><li><span> &lt;&lt; </span></li><li class=""><span class="">1</span></li><li class="active"><span class="">2</span></li></ul></div>');
    });

});
