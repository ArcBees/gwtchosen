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

import java.util.List;

import javax.annotation.Nullable;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;

import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class IsAcceptedValueListBox extends TestCase {

    private final ChosenOptions options;
    private ChosenValueListBox<CarBrand> listBox;

    public IsAcceptedValueListBox() {
        this.options = new ChosenOptions();
    }

    @Override
    public void run() {
        //issue #241 is happening with custom ProvidesKey. All other tests use the object jtself as a key.
        listBox = new ChosenValueListBox<CarBrand>(RENDERER, new ProvidesKey<CarBrand>() {
            @Override
            public Object getKey(final CarBrand item) {
                return item == null ? null : item.name();
            }
        }, options);

        final List<CarBrand> acceptableValues = Lists.newArrayList(CarBrand.values());

        listBox.setAcceptableValues(acceptableValues);

        //Check if the issue #241 is fixed
        if (!listBox.isAccepted(CarBrand.AUDI)) {
            throw new AssertionError("Audi is acceptable value");
        }

        RootPanel.get().add(listBox);
    }

}
