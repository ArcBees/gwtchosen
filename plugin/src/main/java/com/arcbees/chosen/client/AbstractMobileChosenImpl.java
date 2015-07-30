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

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

import static com.google.gwt.query.client.GQuery.$;

public class AbstractMobileChosenImpl extends DesktopSingleChosenImpl {
    private boolean isResultClick;

    @Override
    protected void bind() {
        super.bind();

        getSearchResults().mousedown(new Function() {
            @Override
            public void f() {
                searchResultMouseDown();
            }
        });

        getContainer().on("click", "i[role='close']", new Function() {
            @Override
            public void f() {
                resultsHide();
            }
        });

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
        // TODO close icon...
        return ChosenTemplate.templates.contentMobile(getCss().chznSingle(),
                getCss().chznDefault(), defaultText, getCss().chznDrop(), getCss().chznSearch(), getCss().chznResults(),
                ssb.toSafeStyles(), getCss().iconArrow(), getCss().iconCross(), getCss().chznResultsHolder());
    }

    @Override
    protected String buildDropdownWidth(int ddWidth) {
        return "auto";
    }

    @Override
    protected int calculateDropdownTop() {
        return 0;
    }

    @Override
    protected int computeSearchFieldWidth(int ddWidth, boolean isHidden) {
        return -1;
    }

    protected String getContainerClass() {
        return getCss().chznMobileContainer();
    }

    @Override
    protected void maybeSelectResult(Event e) {
        // Do nothing
    }

    @Override
    protected void resultSelect(Event e) {
        String searchValue = getSearchField().val();
        super.resultSelect(e);
        getSearchField().val(searchValue);

        searchResultsMouseOver(e);
        GQuery high = getResultHighlight();
        resultClearHighlight();

        high.removeClass(getCss().resultSelected());

        if (getOptions().isMobileAnimation()) {
            final String paddingTop = high.css("padding-top");
            final String paddingBottom = high.css("padding-bottom");
            final int speed = getOptions().getMobileAnimationSpeed();

            high.animate("height: 0, padding-top: 0, padding-bottom: 0", speed, new Function() {
                public void f() {
                    $(this).animate("height: auto, padding-top: " + paddingTop + ", padding-bottom: " + paddingBottom,
                            speed);
                    $(this).addClass(getCss().resultSelected());
                }
            });
        } else {
            high.addClass(getCss().resultSelected());
        }
    }

    @Override
    protected void resultsHide() {
        super.resultsHide();

        String topPosition = getDropdown().css("top");

        if (topPosition.equals("-9000px") && getOptions().isMobileAnimation()) {
            final int windowHeight = Window.getClientHeight();
            int speed = getOptions().getMobileAnimationSpeed();

            getDropdown()
                    .css("top", "0")
                    .css("left", "0")
                    .css("right", "0")
                    .animate("top: 1000px, left: 0, right: 0", speed * 2, new Function() {
                        public void f() {
                            getDropdown()
                                    .css("bottom", "")
                                    .css(isRTL() ? "left" : "right", "")
                                    .css("top", windowHeight + 20 + "px")
                                    .css(isRTL() ? "right" : "left", "-9000px");
                        }
                    });
        } else {
            getDropdown().css("bottom", "").css(isRTL() ? "left" : "right", "");
        }

        getDropdown().removeClass(getCss().isOpen());
        getSearchField().blur();
    }

    @Override
    protected boolean searchResultsMouseOut(Event e) {
        isResultClick = false;
        return super.searchResultsMouseOut(e);
    }

    @Override
    protected boolean searchResultsMouseUp(Event e) {
        if (!isResultClick) {
            return false;
        }

        isResultClick = false;

        return super.searchResultsMouseUp(e);
    }

    @Override
    void positionDropdownResult() {
        super.positionDropdownResult();

        if (getOptions().isMobileAnimation() && ! getDropdown().hasClass(getCss().isOpen())) {
            int windowHeight = Window.getClientHeight();
            int speed = getOptions().getMobileAnimationSpeed();
            getDropdown()
                    .css("top", windowHeight + "px")
                    .css("bottom", "0")
                    .css(isRTL() ? "left" : "right", "0")
                    .animate("top: 0px", speed * 2);
        } else {
            getDropdown().css("bottom", "0").css(isRTL() ? "left" : "right", "0");
        }

        getDropdown().addClass(getCss().isOpen());
    }

    private void searchResultMouseDown() {
        isResultClick = true;
    }
}
