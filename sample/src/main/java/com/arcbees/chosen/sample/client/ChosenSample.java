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

package com.arcbees.chosen.sample.client;

import java.util.ArrayList;
import java.util.List;

import com.arcbees.chosen.client.ChosenImpl;
import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.ResultsFilter;
import com.arcbees.chosen.client.SelectParser.OptionItem;
import com.arcbees.chosen.client.SelectParser.SelectItem;
import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.arcbees.chosen.client.gwt.MultipleChosenValueListBox;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

import static com.arcbees.chosen.client.Chosen.Chosen;
import static com.google.gwt.query.client.GQuery.$;

public class ChosenSample implements EntryPoint {
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
            Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
                @Override
                public boolean execute() {
                    List<SelectItem> selectItems = chosen.getSelectItems();
                    selectItems.clear();
                    int arrayIndex = 0;
                    for (String name : NAMES) {
                        if (searchText != null && name.toUpperCase().startsWith(searchText.toUpperCase())) {
                            OptionItem optionItem = new OptionItem();
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

    public void onModuleLoad() {

        if (!com.arcbees.chosen.client.Chosen.isSupported()) {
            $("#browserWarning").show();
        }

        $(".chzn-select, .enhance").as(Chosen).chosen();

        $("#allowSingleDeselect").as(Chosen).chosen(new ChosenOptions().setAllowSingleDeselect(true));

        $("#disableSearchThreshold").as(Chosen).chosen(
                new ChosenOptions().setDisableSearchThreshold(10));

        $("#searchContains").as(Chosen).chosen(
                new ChosenOptions().setSearchContains(true));

        $("#singleBackstrokeDelete").as(Chosen).chosen(
                new ChosenOptions().setSingleBackstrokeDelete(true));

        $("#maxSelectedOptions").as(Chosen).chosen(
                new ChosenOptions().setMaxSelectedOptions(5));

        $("#noResultsText").as(Chosen).chosen(
                new ChosenOptions().setNoResultsText("Ooops, nothing was found:"));

        final ChosenListBox chzn = new ChosenListBox();
        chzn.addItem("item 1");
        chzn.setWidth("250px");

        RootPanel.get("updateChozen").add(chzn);

        $("#updateButton").click(new Function() {
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

        final ChosenListBox hcs = new ChosenListBox();
        hcs.setWidth("350px");
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
        RootPanel.get("hierChozenSingle").add(hcs);

        // custom filter
        ChosenOptions options = new ChosenOptions();
        options.setResultFilter(new ServerSideSimulatorResultFilter());
        final ChosenListBox serverChosenListBox = new ChosenListBox(false, options);
        RootPanel.get("serverChozen").add(serverChosenListBox);

        class Test {
            String display;

            public Test(String display) {
                this.display = display;
            }

            @Override
            public boolean equals(Object obj) {
                return display.equals(((Test)obj).display);
            }

            @Override
            public int hashCode() {
                return display.hashCode();
            }

            @Override
            public String toString() {
                return display;
            }
        }

        final List<Test> firstList = Lists.newArrayList(new Test("test1"), new Test("test2"), new Test("test3"),
                new Test("test4"));
        final List<Test> secondList = Lists.newArrayList(new Test("test5"), new Test("test6"), new Test("test7"));
        List<Test> all = new ArrayList<Test>();
        all.addAll(firstList);
        all.addAll(secondList);

        final MultipleChosenValueListBox<Test> testMultipleChosenValueListBox = new MultipleChosenValueListBox<Test>(new AbstractRenderer<Test>() {

            @Override
            public String render(Test test) {
                return test != null ? test.display : "";
            }
        });

        testMultipleChosenValueListBox.setAcceptableValues(all);

        final List<Test> current = new ArrayList<Test>(firstList);
        testMultipleChosenValueListBox.setValue(current);

        RootPanel.get().add(testMultipleChosenValueListBox);

        Button exch = new Button("setValue");
        exch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                boolean first = current.size() == firstList.size();
                current.clear();

                if (first) {
                    current.addAll(secondList);
                } else {
                    current.addAll(firstList);
                }
                testMultipleChosenValueListBox.setValue(current);
            }
        });

        Button clear = new Button("add values");
        clear.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                testMultipleChosenValueListBox.addValues(Lists.newArrayList(new Test("added1"), new Test("added2")));
            }
        });

        Button removeFirst = new Button("add value");
        removeFirst.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                testMultipleChosenValueListBox.addValue(new Test("added"));
            }
        });

        Button removeLast = new Button("remove value");
        removeLast.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                testMultipleChosenValueListBox.removeValue(new Test("added"));
            }
        });

        Button getValues = new Button("remove values");
        getValues.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                testMultipleChosenValueListBox.removeValues(Lists.newArrayList(new Test("added1"), new Test("added2")));
            }
        });

        RootPanel.get().add(clear);
        RootPanel.get().add(exch);
        RootPanel.get().add(removeFirst);
        RootPanel.get().add(removeLast);
        RootPanel.get().add(getValues);
    }
}
