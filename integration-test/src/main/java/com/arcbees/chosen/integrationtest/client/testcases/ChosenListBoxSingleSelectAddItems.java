/*
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

import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ChosenListBoxSingleSelectAddItems extends TestCase {
    public static final String BUTTON_ID = "ChosenListBoxSingleSelectAddItems-button";
    public static final String SELECTED_VALUE = "Six";

    @Override
    public void run() {
        final ChosenListBox listBox = new ChosenListBox(false);
        listBox.setWidth("200px");
        listBox.addItem("One");
        listBox.addItem("Two");
        listBox.addItem("Three");

        Button button = new Button("Add items and select Six");
        button.getElement().setId(BUTTON_ID);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                listBox.clear();
                listBox.addItem("Four");
                listBox.addItem("Five");
                listBox.addItem("Six");
                listBox.update();
                listBox.setSelectedValue(SELECTED_VALUE);
            }
        });

        RootPanel.get().add(listBox);
        RootPanel.get().add(button);
    }
}
