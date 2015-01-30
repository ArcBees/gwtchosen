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

import java.util.HashMap;
import java.util.Map;

import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.HideCurrentValue;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
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
        registerTestCase(new HideCurrentValue());
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
