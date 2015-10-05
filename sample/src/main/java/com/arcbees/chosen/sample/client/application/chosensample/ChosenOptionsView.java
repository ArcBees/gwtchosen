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

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.DropdownPosition;
import com.arcbees.chosen.sample.client.resources.ChosenCustomResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.arcbees.chosen.client.Chosen.Chosen;
import static com.google.gwt.query.client.GQuery.$;

public class ChosenOptionsView implements IsWidget {
    interface Binder extends UiBinder<Widget, ChosenOptionsView> {
    }

    private static final Binder binder = GWT.create(Binder.class);
    private static final ChosenCustomResources CUSTOM_RESOURCES = GWT.create(ChosenCustomResources.class);

    @UiField
    SelectElement allowSingleDeselect;
    @UiField
    SelectElement disableSearchThreshold;
    @UiField
    SelectElement searchContains;
    @UiField
    SelectElement singleBackstrokeDelete;
    @UiField
    SelectElement maxSelectedOptions;
    @UiField
    SelectElement noResultsText;
    @UiField
    SelectElement dropdownPosition;
    @UiField
    SelectElement dropdownResources;
    @UiField
    SelectElement mobileWidth;
    @UiField
    SelectElement mobileAnimation;
    @UiField
    SelectElement mobileSpeed;
    @UiField
    SelectElement dropdownPlaceholder;
    @UiField
    SelectElement dropdownPlaceholderSingle;
    @UiField
    SelectElement dropdownPlaceholderMultiple;
    @UiField
    SelectElement mobileOneText;
    @UiField
    SelectElement mobileManyText;

    private final Widget widget;

    public ChosenOptionsView() {
        widget = binder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    $(allowSingleDeselect).as(Chosen).chosen(
                            new ChosenOptions().setAllowSingleDeselect(true));

                    $(disableSearchThreshold).as(Chosen).chosen(
                            new ChosenOptions().setDisableSearchThreshold(10));

                    $(searchContains).as(Chosen).chosen(
                            new ChosenOptions().setSearchContains(true));

                    $(singleBackstrokeDelete).as(Chosen).chosen(
                            new ChosenOptions().setSingleBackstrokeDelete(true));

                    $(maxSelectedOptions).as(Chosen).chosen(
                            new ChosenOptions().setMaxSelectedOptions(5));

                    $(noResultsText).as(Chosen).chosen(
                            new ChosenOptions().setNoResultsText("Ooops, nothing was found:"));

                    $(dropdownPosition).as(Chosen).chosen(
                            new ChosenOptions().setDropdownPosition(DropdownPosition.ABOVE));

                    $(dropdownResources).as(Chosen).chosen(
                            new ChosenOptions().setResources(CUSTOM_RESOURCES));

                    $(dropdownPlaceholder).as(Chosen).chosen(
                            new ChosenOptions().setPlaceholderText("Well, hello!"));

                    $(dropdownPlaceholderSingle).as(Chosen).chosen(
                            new ChosenOptions().setPlaceholderTextSingle("I'm single, ladies!"));

                    $(dropdownPlaceholderMultiple).as(Chosen).chosen(
                            new ChosenOptions().setPlaceholderTextMultiple("I'm so multiple!"));

                    $(mobileWidth).as(Chosen).chosen(
                            new ChosenOptions().setMobileViewportMaxWidth(2000));

                    $(mobileAnimation).as(Chosen).chosen(
                            new ChosenOptions().setMobileAnimation(false));

                    $(mobileSpeed).as(Chosen).chosen(
                            new ChosenOptions().setMobileAnimationSpeed(1500));

                    $(mobileOneText).as(Chosen).chosen(
                            new ChosenOptions().setOneSelectedTextMultipleMobile("{} bear selected"));

                    $(mobileManyText).as(Chosen).chosen(
                            new ChosenOptions().setManySelectedTextMultipleMobile("{} bears selected"));
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
