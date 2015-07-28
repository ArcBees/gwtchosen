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

package com.arcbees.chosen.client;

import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.web.bindery.event.shared.EventBus;

public class DesktopSingleChosenImpl extends ChosenImpl {
    private Function activateAction;
    private GQuery resultSingleSelected;

    @Override
    protected void bind() {
        super.bind();

        getContainer().click(new Function() {
            @Override
            public boolean f(Event e) {
                e.preventDefault();
                return false;
            }
        });
    }

    @Override
    protected SafeHtml buildContainerHtml(String defaultText, SafeStylesBuilder ssb) {
        return ChosenTemplate.templates.contentSingle(getCss().chznSingle(),
                getCss().chznDefault(), defaultText, getCss().chznDrop(), getCss().chznSearch(), getCss().chznResults(),
                ssb.toSafeStyles(), getCss().iconArrow(), getCss().iconSearch());
    }

    protected int computeSearchFieldWidth(int ddWidth, boolean isHidden) {
        return ddWidth - getSideBorderPadding(getSearchContainer(), isHidden)
                - getSideBorderPadding(getSearchField(), isHidden);
    }

    @Override
    protected void init(SelectElement element, ChosenOptions options, EventBus eventBus) {
        activateAction = new Function() {
            @Override
            public boolean f(Event e) {
                return activateField(e);
            }
        };

        super.init(element, options, eventBus);
    }

    @Override
    protected void initSearchElement(int ddWidth, boolean isHidden) {
        super.initSearchElement(ddWidth, isHidden);

        int searchFieldWidth = computeSearchFieldWidth(ddWidth, isHidden);

        if (searchFieldWidth != -1) {
            getSearchField().css("width", searchFieldWidth + "px");
        }
    }

    protected void maybeSelectResult(Event e) {
        if (resultsShowing) {
            resultSelect(e);
        }
    }

    @Override
    protected void onTabKeydown(Event e) {
        maybeSelectResult(e);

        super.onTabKeydown(e);
    }

    @Override
    protected void resultsBuild(boolean init, String defaultText, boolean customFilter) {
        getSelectedItem().addClass(getCss().chznDefault()).find("span").text(defaultText);

        if (!customFilter && getSelectElement().getOptions().getLength() <= getOptions().getDisableSearchThreshold()) {
            getContainer().addClass(getCss().chznContainerSingleNoSearch());
        } else {
            getContainer().removeClass(getCss().chznContainerSingleNoSearch());
        }

        super.resultsBuild(init, defaultText, customFilter);
    }

    @Override
    protected void resultDeactivate(GQuery query, boolean selected) {
        super.resultDeactivate(query, selected);

        if (selected) {
            resultSingleSelected = query;
        }
    }

    @Override
    protected void setupDisabledSearchField() {
        super.setupDisabledSearchField();

        getSelectedItem().unbind("focus", activateAction);
    }

    @Override
    protected void setupEnabledSearchField() {
        super.setupEnabledSearchField();

        getSelectedItem().bind("focus", activateAction);
    }

    @Override
    protected void update() {
        resultSingleSelected = null;
        super.update();
    }

    @Override
    protected boolean beforeShowResult() {
        getSelectedItem().addClass(getCss().chznSingleWithDrop());
        if (resultSingleSelected != null) {
            resultDoHighlight(resultSingleSelected);
        }

        return true;
    }
}
