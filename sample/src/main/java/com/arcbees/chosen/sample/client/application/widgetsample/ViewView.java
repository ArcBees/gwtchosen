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

package com.arcbees.chosen.sample.client.application.widgetsample;

import com.arcbees.chosen.client.event.ChosenChangeEvent;
import com.arcbees.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.arcbees.chosen.client.event.HidingDropDownEvent.HidingDropDownHandler;
import com.arcbees.chosen.client.event.MaxSelectedEvent.MaxSelectedHandler;
import com.arcbees.chosen.client.event.ReadyEvent.ReadyHandler;
import com.arcbees.chosen.client.event.ShowingDropDownEvent.ShowingDropDownHandler;
import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.client.gwt.MultipleChosenValueListBox;
import com.arcbees.chosen.sample.client.resources.AppResources;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class ViewView implements IsWidget {
    @UiField
    static AppResources res;

    private enum Choices {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHT, NINTH, TENTH;

        public String getLiteral() {
            String name = name();
            return name.charAt(0) + name.substring(1, name.length()).toLowerCase();
        }
    }

    private static class ChoiceRenderer extends AbstractRenderer<Choices> {
        @Override
        public String render(Choices choices) {
            return choices != null ? choices.getLiteral() : "";
        }
    }

    private static final class MyEventHandlers implements ChosenChangeHandler, HidingDropDownHandler,
            ShowingDropDownHandler, MaxSelectedHandler, ReadyHandler, ValueChangeHandler {

        private final String elementId;

        public MyEventHandlers(String id) {
            this.elementId = id;
        }

        private void log(String eventName, String additional) {
            $("#log").append(
                    "<span class=\"" + res.style().log_line() + "\">" +
                            eventName + " fired by <em>" + elementId + "</em> " + additional +
                            "</span>"
            ).scrollTop($("#log").get(0).getScrollHeight());
        }

        public void onReady() {
            log("ReadyEvent", "");
        }

        public void onMaxSelected() {
            log("MaxSelectedEvent", "");
        }

        public void onShowingDropDown() {
            log("ShowingDropDownEvent", "");
        }

        public void onHidingDropdown() {
            log("HidingDropDownEvent", "");
        }

        public void onChange(ChosenChangeEvent event) {
            String additional = (event.isSelection() ? ": selection of " : ": deselection of ") + event.getValue();
            log("ChangeEvent on", additional);
        }

        @Override
        public void onValueChange(ValueChangeEvent valueChangeEvent) {
            log("ValueChangeEvent on", "" + valueChangeEvent.getValue());
        }
    }

    private static final String[] teamsGroup = new String[]{
            "NFC EAST", "NFC NORTH", "NFC SOUTH", "NFC WEST", "AFC EAST", "AFC NORTH", "AFC SOUTH", "AFC WEST"
    };

    private static final String[] teams = new String[]{
            "Dallas Cowboys", "New York Giants", "Philadelphia Eagles", "Washington Redskins",
            "Chicago Bears", "Detroit Lions", "Green Bay Packers", "Minnesota Vikings",
            "Atlanta Falcons", "Carolina Panthers", "New Orleans Saints", "Tampa Bay Buccaneers",
            "Arizona Cardinals", "St. Louis Rams", "San Francisco 49ers", "Seattle Seahawks",
            "Buffalo Bills", "Miami Dolphins", "New England Patriots", "New York Jets",
            "Baltimore Ravens", "Cincinnati Bengals", "Cleveland Browns", "Pittsburgh Steelers",
            "Houston Texans", "Indianapolis Colts", "Jacksonville Jaguars", "Tennessee Titans",
            "Denver Broncos", "Kansas City Chiefs", "Oakland Raiders", "San Diego Chargers"};

    interface Binder extends UiBinder<Widget, ViewView> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    ChosenListBox countriesChosen;
    @UiField(provided = true)
    ChosenListBox teamChosen;
    @UiField(provided = true)
    ChosenValueListBox<Choices> chosenValueListBox;
    @UiField(provided = true)
    MultipleChosenValueListBox<Choices> multipleChosenValueListBox;

    private final Widget widget;

    public ViewView() {
        teamChosen = new ChosenListBox(true);
        chosenValueListBox = new ChosenValueListBox<Choices>(new ChoiceRenderer());
        multipleChosenValueListBox = new MultipleChosenValueListBox<Choices>(new ChoiceRenderer());

        widget = binder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    $("#clearLogButton").click(new Function() {
                        @Override
                        public void f() {
                            $("#log").empty();
                        }
                    });
                }
            }
        });

        init();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void bind() {
        MyEventHandlers teamEventHandler = new MyEventHandlers("multiple ChosenListBox");
        teamChosen.addChosenChangeHandler(teamEventHandler);
        teamChosen.addHidingDropDownHandler(teamEventHandler);
        teamChosen.addMaxSelectedHandler(teamEventHandler);
        teamChosen.addShowingDropDownHandler(teamEventHandler);
        teamChosen.addReadyHandler(teamEventHandler);

        MyEventHandlers countryEventHandler = new MyEventHandlers("single ChosenListBox");
        countriesChosen.addChosenChangeHandler(countryEventHandler);
        countriesChosen.addHidingDropDownHandler(countryEventHandler);
        countriesChosen.addMaxSelectedHandler(countryEventHandler);
        countriesChosen.addShowingDropDownHandler(countryEventHandler);
        countriesChosen.addReadyHandler(countryEventHandler);

        MyEventHandlers valueListBoxHandler = new MyEventHandlers("single ValueChosenListBox");
        chosenValueListBox.addValueChangeHandler(valueListBoxHandler);

        MyEventHandlers multipleValueListBoxHandler = new MyEventHandlers("Multiple ValueChosenListBox");
        multipleChosenValueListBox.addValueChangeHandler(multipleValueListBoxHandler);
    }

    private void init() {
        teamChosen.addGroup(teamsGroup[0]);

        // init options for teamchosen
        int i = 0;
        for (String team : teams) {
            if (i % 4 == 0) {
                teamChosen.addGroup(teamsGroup[i / 4]);
            }
            teamChosen.addItemToGroup(team);
            i++;
        }

        // init default place holder text
        teamChosen.setPlaceholderText("Choose your favourite teams...");

        teamChosen.setWidth("300px");

        chosenValueListBox.setAcceptableValues(Lists.newArrayList(Choices.values()));
        chosenValueListBox.setValue(Choices.THIRD);

        multipleChosenValueListBox.setAcceptableValues(Lists.newArrayList(Choices.values()));
        multipleChosenValueListBox.setValue(Lists.newArrayList(Choices.FIFTH, Choices.FIRST, Choices.FOURTH));

        bind();
    }
}
