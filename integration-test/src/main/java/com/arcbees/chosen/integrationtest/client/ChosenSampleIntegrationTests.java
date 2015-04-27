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

package com.arcbees.chosen.integrationtest.client;

import java.util.HashMap;
import java.util.Map;

import com.arcbees.chosen.integrationtest.client.testcases.AllowSingleDeselect;
import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.DisableSearchThreshold;
import com.arcbees.chosen.integrationtest.client.testcases.EnabledDisabled;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.MaxSelectedOptions;
import com.arcbees.chosen.integrationtest.client.testcases.SearchContains;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleMultiValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SingleBackstrokeDelete;
import com.arcbees.chosen.integrationtest.client.testcases.TabNavigation;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Above;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Below;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

public class ChosenSampleIntegrationTests implements EntryPoint, ValueChangeHandler<String> {
    private final Map<String, TestCase> testCaseMap;

    public ChosenSampleIntegrationTests() {
        testCaseMap = new HashMap<String, TestCase>();
        registerTestCase(new ChooseOption());
        registerTestCase(new HideEmptyValues());
        registerTestCase(new ShowNonEmptyValues());
        registerTestCase(new AllowSingleDeselect());
        registerTestCase(new TabNavigation());
        registerTestCase(new EnabledDisabled());
        registerTestCase(new Above());
        registerTestCase(new Below());
        registerTestCase(new AutoNoBoundariesHasEnoughSpace());
        registerTestCase(new AutoNoBoundariesHasNotEnoughSpace());
        registerTestCase(new AutoWithBoundariesHasEnoughSpace());
        registerTestCase(new AutoWithBoundariesHasNotEnoughSpace());
        registerTestCase(new SimpleMultiValueListBox());
        registerTestCase(new SimpleValueListBox());
        registerTestCase(new DisableSearchThreshold());
        registerTestCase(new SearchContains());
        registerTestCase(new MaxSelectedOptions());
        registerTestCase(new SingleBackstrokeDelete());
    }

    @Override
    public void onModuleLoad() {
        History.addValueChangeHandler(this);
        History.fireCurrentHistoryState();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        RootPanel.get().clear();
        String token = event.getValue();
        TestCase testCase = testCaseMap.get(token);
        testCase.run();
    }

    private void registerTestCase(TestCase testCase) {
        assert !testCaseMap.containsKey(testCase.getToken());

        testCaseMap.put(testCase.getToken(), testCase);
    }
}
