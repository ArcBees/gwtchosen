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

import com.arcbees.chosen.client.DropdownPosition;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class AutoNoBoundariesHasNotEnoughSpace extends TestCase {
    @UiTemplate("Auto.ui.xml")
    public interface Binder extends UiBinder<Widget, AutoNoBoundariesHasNotEnoughSpace> {
    }

    @UiField(provided = true)
    ChosenValueListBox listBox;

    @Override
    public void run() {
        Binder binder = GWT.create(Binder.class);
        listBox = DropdownPositionTestHelper.buildSample(DropdownPosition.AUTO, null, null);
        RootPanel.get().add(binder.createAndBindUi(this));
        $(listBox).siblings("div").css("bottom", "100px");
    }
}
