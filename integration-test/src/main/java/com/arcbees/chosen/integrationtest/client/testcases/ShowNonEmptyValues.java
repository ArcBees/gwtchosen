/**
 * Copyright 2014 ArcBees Inc.
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

package com.arcbees.chosen.integrationtest.client.testcases;

import java.util.List;

import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This test makes sure that when null values are rendered as a non-empty string,
 * then that exact non-empty string will be displayed in the dropdown options.
 */

public class ShowNonEmptyValues extends TestCase {
    public static final Renderer<CarBrand> RENDERER = new AbstractRenderer<CarBrand>() {
        @Override
        public String render(CarBrand object) {
            if (object == null) {
                return "Placeholder for null";
            }
            return object.name();
        }
    };

    @Override
    public void run() {
        ChosenValueListBox<CarBrand> listBox = new ChosenValueListBox<CarBrand>(RENDERER);

        List<CarBrand> carBrands = Lists.newArrayList(CarBrand.values());
        carBrands.add(0, null);
        listBox.setAcceptableValues(carBrands);

        listBox.setValue(null);

        RootPanel.get().add(listBox);
    }
}
