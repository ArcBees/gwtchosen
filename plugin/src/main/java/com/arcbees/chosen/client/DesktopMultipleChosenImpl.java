/**
 * Copyright 2014 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.chosen.client;

import com.arcbees.chosen.client.SelectParser.OptionItem;
import com.arcbees.chosen.client.event.MaxSelectedEvent;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.safehtml.shared.SafeHtmlUtils.fromTrustedString;

public class DesktopMultipleChosenImpl extends ChosenImpl {
    private static final int BACKSPACE = 8;

    private GQuery pendingBackstroke;
    private int backstrokeLength;
    private boolean pendingDestroyClick;

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    protected void activeTabIndexProperty() {
        // Do nothing
    }

    @Override
    protected void bind() {
        super.bind();

        getSearchChoices().click(new Function() {
            @Override
            public boolean f(Event e) {
                return choicesClick(e);
            }
        });
        getSearchField().focus(new Function() {
            @Override
            public boolean f(Event e) {
                return inputFocus(e);
            }
        });
    }

    @Override
    protected SafeHtml buildContainerHtml(String defaultText, SafeStylesBuilder ssb) {
        return ChosenTemplate.templates.contentMultiple(getCss().chznChoices(),
                getCss().searchField(), defaultText, getCss().defaultClass(), getCss().chznDrop(),
                getCss().chznResults(), ssb.toSafeStyles());
    }

    @Override
    protected StringBuilder buildOptionStyleClass(OptionItem option) {
        StringBuilder classes = super.buildOptionStyleClass(option);

        if (!(option.isSelected())) {
            classes.append(" ").append(getCss().activeResult());
        }

        return classes;
    }

    protected void addChoice(OptionItem option) {
        if (maxSelectedOptionsReached()) {
            fireEvent(new MaxSelectedEvent(this));
        } else {
            String choiceId = getContainerId() + "_c_" + option.getArrayIndex();
            choices++;
            SafeHtml html = fromTrustedString(option.getHtml());
            searchContainer.before(ChosenTemplate.templates.choice(choiceId, getCss().searchChoice(), html,
                    getCss().searchChoiceClose(), "" + option.getArrayIndex(), option.getValue(),
                    getCss().iconCross()).asString());
            $('#' + choiceId).find("a").click(new Function() {
                public boolean f(final Event e) {
                    choiceDestroyLinkClick(e);
                    return false;
                }
            });
        }
    }

    @Override
    protected boolean beforeShowResult() {
        if (maxSelectedOptionsReached()) {
            fireEvent(new MaxSelectedEvent(this));
            return false;
        }
        return true;
    }

    @Override
    protected void containerMouseDownImpl(Event e, GQuery element) {
        if (pendingDestroyClick) {
            pendingDestroyClick = false;
            return;
        }

        if (!activeField) {
            getSearchField().val("");
            $(document).click(clickTestAction);
            resultsShow();
        }

        activateField();
    }

    @Override
    protected void deactiveTabIndexProperty() {
        // Do nothing
    }

    @Override
    protected void resultDeactivate(GQuery query, boolean selected) {
        query.removeClass(getCss().activeResult(), getCss().foundResult());
    }

    @Override
    protected void initSearchElement(int ddWidth, boolean isHidden) {
        setSearchChoices(getContainer().find("ul." + getCss().chznChoices()).first());
        setSearchContainer(getContainer().find("li." + getCss().searchField()).first());
    }

    @Override
    protected boolean keydownChecker(Event e) {
        if (e.getKeyCode() != BACKSPACE) {
            clearBackstroke();
        } else {
            backstrokeLength = getSearchField().val().length();
        }

        return super.keydownChecker(e);
    }

    @Override
    protected void onKeydownBackstroke() {
        if (backstrokeLength < 1 && choices > 0) {
            if (pendingBackstroke != null) {
                choiceDestroy(pendingBackstroke.find("a").first());
                clearBackstroke();
            } else {
                pendingBackstroke = getSearchContainer().siblings("li." + getCss().searchChoice()).last();
                if (getOptions().isSingleBackstrokeDelete()) {
                    onKeydownBackstroke();
                } else {
                    pendingBackstroke.addClass(getCss().searchChoiceFocus());
                }
            }
        } else if (pendingBackstroke == null) {
            resultClearHighlight();
            resultsSearch();
        }
    }

    @Override
    protected void onResultSelected(OptionItem item, String newValue, String oldValue, boolean metaKeyPressed) {
        if (!metaKeyPressed) {
            resultsHide();
        }

        fireChosenChangeEventIfNotEqual(item, newValue, oldValue);
    }

    @Override
    protected int positionBelow() {
        return getContainer().outerHeight();
    }

    @Override
    protected GQuery querySelectedResults() {
        return null;
    }

    @Override
    protected void resetSelectedItem() {
    }

    @Override
    protected void resultsBuild(boolean init, String defaultText, boolean customFilter) {
        if (choices > 0) {
            getSearchChoices().find("li." + getCss().searchChoice()).remove();
            choices = 0;
        }
        super.resultsBuild(init, defaultText, customFilter);
    }

    @Override
    protected void searchFieldScale(int fWidth) {
        StringBuilder styleBlock =
                new StringBuilder("position:absolute; " + (isRTL() ? "right" : "left") + ": -1000px; top: -1000px; " +
                        "visibility:hidden;");
        String[] styleToCopy =
                {"font-size", "font-style", "font-weight", "font-family", "line-height", "text-transform",
                        "letter-spacing"};

        for (String style : styleToCopy) {
            styleBlock.append(style).append(':').append(getSearchField().css(style)).append(";");
        }

        GQuery div = $("<div />").attr("style", styleBlock.toString()).text(getSearchField().val());
        $("body").append(div);

        int w = div.width() + 25;
        div.remove();

        if (w > fWidth - 10) {
            w = fWidth - 10;
        }

        getSearchField().css("width", w + "px");
    }

    @Override
    protected void setTabIndexProperty(String tabIndexProperty) {
        getSearchField().attr(TABINDEX_PROPERTY, tabIndexProperty);
    }

    @Override
    protected boolean shouldActivateResult(GQuery result) {
        return !result.hasClass(getCss().resultSelected());
    }

    @Override
    protected void showSearchFieldDefault(String defaultText) {
        if (choices < 1 && !activeField) {
            getSearchField().val(defaultText);
            getSearchField().addClass(getCss().defaultClass());
        } else {
            super.showSearchFieldDefault(defaultText);
        }
    }

    @Override
    protected void update() {
        resultsResetCleanup();

        super.update();
    }

    private boolean choicesClick(Event e) {
        e.preventDefault();

        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        if (activeField
                && !($e.hasClass(getCss().searchChoice()) || !$e.parents("." + getCss().searchChoice()).isEmpty())
                && !resultsShowing) {
            resultsShow();
        }
        return true;
    }

    private void clearBackstroke() {
        if (pendingBackstroke != null) {
            pendingBackstroke.removeClass(getCss().searchChoiceFocus());
        }
        pendingBackstroke = null;
    }

    private boolean inputFocus(final Event e) {
        if (!activeField) {
            Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
                public boolean execute() {
                    containerMouseDown(e);
                    return false;
                }
            }, 50);
        }
        return false;
    }

    private void choiceDestroyLinkClick(Event e) {
        e.preventDefault();
        if (!isDisabled()) {
            pendingDestroyClick = true;
            Element target = e.getEventTarget().cast();
            choiceDestroy($(target));
        } else {
            e.stopPropagation();
        }
    }
}
