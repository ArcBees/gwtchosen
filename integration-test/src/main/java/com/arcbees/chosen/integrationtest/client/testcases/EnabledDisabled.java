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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class EnabledDisabled extends SimpleValueListBox {
    public static final String ENABLE_DEBUG_ID = "enable";
    public static final String DISABLE_DEBUG_ID = "disable";

    @Override
    public void run() {
        super.run();

        Button enableButton = new Button("Enable");
        enableButton.ensureDebugId(ENABLE_DEBUG_ID);
        enableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getListBox().setEnabled(true);
            }
        });

        Button disableButton = new Button("Disable");
        disableButton.ensureDebugId(DISABLE_DEBUG_ID);
        disableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getListBox().setEnabled(false);
            }
        });

        RootPanel.get().add(enableButton);
        RootPanel.get().add(disableButton);
    }
}
