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

import javax.annotation.Nullable;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.MultipleChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootPanel;

import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class SimpleMultiValueListBox extends TestCase {
    private final ChosenOptions options;
    private MultipleChosenValueListBox<CarBrand> listBox;

    public SimpleMultiValueListBox() {
        this(new ChosenOptions());
    }

    protected SimpleMultiValueListBox(ChosenOptions options) {
        this.options = options;
    }

    @Override
    public void run() {
        listBox = new MultipleChosenValueListBox<CarBrand>(RENDERER, options);

        listBox.getElement().getStyle().setWidth(500, Unit.PX);

        listBox.setAcceptableValues(Lists.newArrayList(CarBrand.values()));

        RootPanel.get().add(listBox);
    }

    @Nullable
    protected MultipleChosenValueListBox<CarBrand> getListBox() {
        return listBox;
    }
}
