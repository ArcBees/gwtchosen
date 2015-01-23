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

package com.arcbees.chosen.integrationtest.client;

import java.util.EnumSet;

import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.google.common.base.CaseFormat;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.RootPanel;

public class ChosenSampleIntegrationTests implements EntryPoint {
    @Override
    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();
        ChosenValueListBox<CarBrand> listBox = new ChosenValueListBox<CarBrand>(new AbstractRenderer<CarBrand>() {
            @Override
            public String render(CarBrand object) {
                return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, object.name());
            }
        });

        listBox.setAcceptableValues(EnumSet.allOf(CarBrand.class));
        listBox.setValue(CarBrand.AUDI);

        rootPanel.add(listBox);
    }
}
