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

package com.arcbees.chosen.integrationtest.client.testcases;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;

public class HideCurrentValue extends TestCase {
    public static final Renderer<CarBrand> RENDERER = new DefaultCarRenderer();

    @Override
    public void run() {
        ChosenOptions chosenOptions = new ChosenOptions();
        chosenOptions.setHideCurrentValue(true);
        ChosenValueListBox<CarBrand> listBox = new ChosenValueListBox<CarBrand>(RENDERER, chosenOptions);

        listBox.setAcceptableValues(Lists.newArrayList(CarBrand.values()));

        RootPanel.get().add(listBox);
    }
}
