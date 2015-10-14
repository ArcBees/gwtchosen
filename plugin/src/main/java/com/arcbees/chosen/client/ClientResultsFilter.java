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

package com.arcbees.chosen.client;

import java.util.List;

import com.arcbees.chosen.client.SelectParser.OptionItem;
import com.arcbees.chosen.client.SelectParser.SelectItem;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.regexp.shared.RegExp;

import static com.google.gwt.query.client.GQuery.$;

class ClientResultsFilter implements ResultsFilter {
    private static final RegExp regExpChars = RegExp.compile("[-[\\]{}()*+?.,\\\\^$|#\\s]", "g");

    @Override
    public void filter(String searchText, ChosenImpl chosen, boolean isShowing) {
        ChosenOptions options = chosen.getOptions();

        // TODO should be part of this object
        String regexAnchor = options.isSearchContains() ? "" : "^";
        // escape reg exp special chars
        String escapedSearchText = regExpChars.replace(searchText, "\\$&");

        RegExp regex = RegExp.compile(regexAnchor + escapedSearchText, "i");
        RegExp zregex = RegExp.compile("(" + escapedSearchText + ")", "i");

        int results = 0;

        List<SelectItem> selectItems = chosen.getSelectItems();

        for (SelectItem item : selectItems) {
            if (item.isDisabled() || item.isEmpty()) {
                continue;
            }

            if (item.isGroup()) {
                $('#' + item.getDomId()).css("display", "none");
            } else {
                OptionItem option = (OptionItem) item;

                if (!(chosen.isMultiple() && option.isSelected())) {
                    boolean found = false;
                    String resultId = option.getDomId();
                    GQuery result = $("#" + resultId);
                    String optionContent = option.getHtml();
                    if (optionContent == null || optionContent.trim().isEmpty()) {
                        optionContent = option.getText();
                    }

                    if (regex.test(optionContent)) {
                        found = true;
                        results++;
                    } else if (optionContent.contains(" ") || optionContent.indexOf("[") == 0) {
                        String[] parts = optionContent.replaceAll("\\[|\\]", "").split(" ");
                        for (String part : parts) {
                            if (regex.test(part)) {
                                found = true;
                                results++;
                            }
                        }
                    }

                    if (found) {
                        String text;
                        if (searchText.length() > 0) {
                            text = zregex.replace(optionContent, "<em>$1</em>");
                        } else {
                            text = optionContent;
                        }

                        result.html(text);
                        chosen.resultActivate(result);

                        if (option.getGroupArrayIndex() != -1) {
                            $("#" + selectItems.get(option.getGroupArrayIndex()).getDomId()).css("display",
                                    "list-item");
                        }
                    } else {
                        if (chosen.getResultHighlight() != null
                                && resultId.equals(chosen.getResultHighlight().attr("id"))) {
                            chosen.resultClearHighlight();
                        }
                        chosen.resultDeactivate(result);
                    }
                }
            }
        }

        if (results < 1 && !searchText.isEmpty()) {
            chosen.noResults(searchText);
        } else {
            chosen.winnowResultsSetHighlight();
        }

        if (isShowing) {
            chosen.positionDropdownResult();
        }
    }
}
