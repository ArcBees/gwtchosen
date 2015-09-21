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

import java.util.List;

import com.arcbees.chosen.client.ChosenImpl;
import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.ResultsFilter;
import com.arcbees.chosen.client.SelectParser;
import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomFilteringView implements IsWidget {
    interface Binder extends UiBinder<Widget, CustomFilteringView> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    SimplePanel serverChosen;

    private final Widget widget;

    public static class ServerSideSimulatorResultFilter implements ResultsFilter {
        private static final String[] NAMES = {
                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
                "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia",
                "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen",
                "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker",
                "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips",
                "Campbell", "Parker", "Evans", "Edwards", "Collins", "Stewart", "Sanchez", "Morris",
                "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper",
                "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez", "James",
                "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross",
                "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes",
                "Flores", "Washington", "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander",
                "Russell", "Griffin", "Diaz", "Hayes", "Myers", "Ford", "Hamilton", "Graham", "Sullivan",
                "Wallace", "Woods", "Cole", "West", "Jordan", "Owens", "Reynolds", "Fisher", "Ellis",
                "Harrison", "Gibson", "Mcdonald", "Cruz", "Marshall", "Ortiz", "Gomez", "Murray", "Freeman",
                "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter", "Hunter", "Hicks", "Crawford",
                "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon", "Ramos", "Reyes", "Burns",
                "Gordon", "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black", "Daniels", "Palmer",
                "Mills", "Nichols", "Grant", "Knight", "Ferguson", "Rose", "Stone", "Hawkins", "Dunn",
                "Perkins", "Hudson", "Spencer", "Gardner", "Stephens", "Payne", "Pierce", "Berry",
                "Matthews", "Arnold", "Wagner", "Willis", "Ray", "Watkins", "Olson", "Carroll", "Duncan",
                "Snyder", "Hart", "Cunningham", "Bradley", "Lane", "Andrews", "Ruiz", "Harper", "Fox",
                "Riley", "Armstrong", "Carpenter", "Weaver", "Greene", "Lawrence", "Elliott", "Chavez",
                "Sims", "Austin", "Peters", "Kelley", "Franklin", "Lawson"};

        private boolean initialized;

        @Override
        public void filter(final String searchText, final ChosenImpl chosen, boolean isShowing) {
            if (isShowing && initialized) {
                return; // do nothing, keep the last results
            }
            initialized = true;

            // filter the result asynchronously to simulate a call to a server
            Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    List<SelectParser.SelectItem> selectItems = chosen.getSelectItems();
                    selectItems.clear();
                    int arrayIndex = 0;
                    for (String name : NAMES) {
                        if (searchText != null && name.toUpperCase().startsWith(searchText.toUpperCase())) {
                            SelectParser.OptionItem optionItem = new SelectParser.OptionItem();
                            optionItem.setHtml("<div style='color:blue'>" + name + "</div>");
                            optionItem.setText(name);
                            optionItem.setValue(name);
                            optionItem.setArrayIndex(arrayIndex);
                            optionItem.setOptionsIndex(arrayIndex);
                            optionItem.setDomId("myOption_" + arrayIndex++);

                            selectItems.add(optionItem);
                        }
                    }

                    // warn chosen that the filtering is done.
                    chosen.rebuildResultItems();

                    // stop the repeating command
                    return false;
                }
            }, 10);
        }
    }

    public CustomFilteringView() {
        widget = binder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    ChosenOptions options = new ChosenOptions();
                    options.setResultFilter(new ServerSideSimulatorResultFilter());
                    final ChosenListBox serverChosenListBox = new ChosenListBox(false, options);
                    serverChosen.setWidget(serverChosenListBox);
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
