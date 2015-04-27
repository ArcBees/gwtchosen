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

import javax.annotation.Nullable;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;

import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class SimpleValueListBox extends TestCase {
    private final ChosenOptions options;
    private final boolean addNullValue;

    private ChosenValueListBox<CarBrand> listBox;

    public SimpleValueListBox() {
        this(new ChosenOptions(), false);
    }

    protected SimpleValueListBox(boolean addNullValue) {
        this(new ChosenOptions(), addNullValue);
    }

    protected SimpleValueListBox(ChosenOptions options, boolean addNullValue) {
        this.options = options;
        this.addNullValue = addNullValue;
    }

    @Override
    public void run() {
        listBox = new ChosenValueListBox<CarBrand>(getRenderer(), options);

        List<CarBrand> acceptableValues = Lists.newArrayList(CarBrand.values());
        if (addNullValue) {
            acceptableValues.add(0, null);
        }

        listBox.setAcceptableValues(acceptableValues);

        RootPanel.get().add(listBox);
    }

    @Nullable
    protected ChosenValueListBox<CarBrand> getListBox() {
        return listBox;
    }

    protected Renderer<CarBrand> getRenderer() {
        return RENDERER;
    }
}
