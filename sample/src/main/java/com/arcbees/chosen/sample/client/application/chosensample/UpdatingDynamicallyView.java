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
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class UpdatingDynamicallyView implements IsWidget {
    interface Binder extends UiBinder<Widget, UpdatingDynamicallyView> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    ButtonElement updateButton;
    @UiField
    SimplePanel updateChosen;

    private final Widget widget;

    public UpdatingDynamicallyView() {
        widget = binder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {

                    final ChosenListBox chzn = new ChosenListBox();
                    chzn.addItem("item 1");

                    updateChosen.setWidget(chzn);

                    $(updateButton).click(new Function() {
                        int i = 2;

                        @Override
                        public void f() {
                            for (int j = 0; j < 100; j++) {
                                chzn.addItem("item " + i);
                                i++;
                            }

                            chzn.update();
                        }
                    });
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
