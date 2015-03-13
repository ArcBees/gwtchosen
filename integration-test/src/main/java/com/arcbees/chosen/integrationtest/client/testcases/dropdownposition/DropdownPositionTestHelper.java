/**
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.chosen.integrationtest.client.testcases.dropdownposition;

import java.util.EnumSet;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.DropdownBoundariesProvider;
import com.arcbees.chosen.client.DropdownPosition;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer;
import com.google.gwt.dom.client.Element;
import com.google.gwt.text.shared.AbstractRenderer;

public class DropdownPositionTestHelper {
    public static final AbstractRenderer<CarBrand> RENDERER = new DefaultCarRenderer();

    public static ChosenValueListBox buildSample(DropdownPosition dropdownPosition, Element dropdownBoundaries,
                                                 DropdownBoundariesProvider dropdownBoundariesProvider) {
        ChosenOptions options = new ChosenOptions();
        options.setDropdownPosition(dropdownPosition);
        options.setDropdownBoundaries(dropdownBoundaries);
        options.setDropdownBoundariesProvider(dropdownBoundariesProvider);

        return buildListbox(options);
    }

    private static ChosenValueListBox<CarBrand> buildListbox(ChosenOptions options) {
        ChosenValueListBox<CarBrand> listBox = new ChosenValueListBox<CarBrand>(RENDERER, options);

        listBox.setAcceptableValues(EnumSet.of(CarBrand.AUDI, CarBrand.BENTLEY, CarBrand.BMW));
        listBox.setValue(CarBrand.AUDI);
        return listBox;
    }
}
