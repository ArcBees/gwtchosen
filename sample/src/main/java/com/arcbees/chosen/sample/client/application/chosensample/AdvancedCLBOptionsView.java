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

package com.arcbees.chosen.sample.client.application.chosensample;

import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedCLBOptionsView implements IsWidget {
    interface Binder extends UiBinder<Widget, AdvancedCLBOptionsView> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    SimplePanel hierChosenSingle;

    private final Widget widget;

    public AdvancedCLBOptionsView() {
        widget = binder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    final ChosenListBox hcs = new ChosenListBox();
                    hcs.setPlaceholderText("Navigate to...");
                    hcs.setTabIndex(9);
                    hcs.addItem("");
                    hcs.addStyledItem("Home", "home", null);
                    hcs.addGroup("ABOUT US");
                    hcs.addStyledItemToGroup("Press Releases", "press", null, 0);
                    hcs.addStyledItemToGroup("Contact Us", "about", null, 0);
                    hcs.addGroup("PRODUCTS");
                    hcs.addStyledItemToGroup("Tera-Magic", "tm", null, 0, 1);
                    hcs.addStyledItemToGroup("Tera-Magic Pro", "tmpro", null, 1, 1);
                    // Will be inserted before "Tera-Magic Pro" and custom-styled
                    hcs.insertStyledItemToGroup("Tera-Magic Standard", "tmstd", "youAreHere", 1, 1, 1);
                    hierChosenSingle.setWidget(hcs);
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
