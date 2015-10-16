/*
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.arcbees.chosen.client.SelectParser.GroupItem;
import com.arcbees.chosen.client.SelectParser.OptionItem;
import com.arcbees.chosen.client.SelectParser.SelectItem;
import com.arcbees.chosen.client.event.ChosenChangeEvent;
import com.arcbees.chosen.client.event.ChosenEvent;
import com.arcbees.chosen.client.event.HidingDropDownEvent;
import com.arcbees.chosen.client.event.ReadyEvent;
import com.arcbees.chosen.client.event.ShowingDropDownEvent;
import com.arcbees.chosen.client.event.UpdatedEvent;
import com.arcbees.chosen.client.resources.ChosenCss;
import com.arcbees.chosen.client.resources.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.safehtml.shared.SafeHtmlUtils.fromTrustedString;

public abstract class ChosenImpl {
    static final int BACKSPACE = 8;
    static final String TABINDEX_PROPERTY = "tabindex";

    private static final int TAB = 9;
    private static final int ENTER = 13;
    private static final int SHIFT = 16;
    private static final int CTRL = 17;
    private static final int ESCAPE = 27;
    private static final int UP_ARROW = 38;
    private static final int DOWN_ARROW = 40;
    private static final int LEFT_WINDOW_KEY = 91;
    private static final RegExp containerIdRegExp = RegExp.compile("[^\\w]", "g");
    private static final int HORIZONTAL_OFFSET = -9000;
    private static final int VERTICAL_OFFSET = -9000;
    private static final String DEFAULT_CONTAINER_ID = "chosen_container__";
    private static final Set<Class> INJECTED_RESOURCES = new HashSet<Class>();
    private static int idCounter;

    protected boolean activeField;
    // TODO
    protected int choices;
    protected Function clickTestAction;
    protected boolean resultsShowing;
    protected GQuery searchChoices;
    protected GQuery searchContainer;
    private GQuery $selectElement;
    private boolean allowSingleDeselect;
    private GQuery container;
    private String containerId;
    private ChosenCss css;
    private List<String> selectedValues = new ArrayList<String>();
    private String defaultText;
    private GQuery dropdown;
    private EventBus eventBus;
    private int fWidth;
    private boolean isDisabled;
    private boolean isRTL;
    private boolean customFilter;
    private boolean mouseOnContainer;
    private ChosenOptions options;
    private GQuery resultHighlight;
    private String resultsNoneFound;
    private GQuery searchField;
    private GQuery searchResults;
    private SelectElement selectElement;
    private List<SelectItem> selectItems;
    private GQuery selectedItem;
    private HandlerRegistration updateEventHandlerRegistration;
    private ResultsFilter resultsFilter;

    public GQuery getContainer() {
        return container;
    }

    public String getCurrentValue() {
        if (!selectedValues.isEmpty()) {
            return selectedValues.get(selectedValues.size() - 1);
        }

        return null;
    }

    public ChosenOptions getOptions() {
        return options;
    }

    /**
     * Return the highlighted result or null if the.
     */
    public GQuery getResultHighlight() {
        return resultHighlight;
    }

    public SelectElement getSelectElement() {
        return selectElement;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public boolean isMultiple() {
        return false;
    }

    public void rebuildResultItems() {
        rebuildResultItems(false);
    }

    protected boolean activateField() {
        activeTabIndexProperty();

        container.addClass(css.chznContainerActive());
        activeField = true;

        searchField.val(searchField.val());
        searchField.focus();
        return false;
    }

    protected void activeTabIndexProperty() {
        if (!activeField) {
            searchField.attr(TABINDEX_PROPERTY, selectedItem.attr(TABINDEX_PROPERTY));
            selectedItem.attr(TABINDEX_PROPERTY, -1);
        }
    }

    protected void bind() {
        container.mousedown(new Function() {
            @Override
            public boolean f(Event e) {
                return containerMouseDown(e);
            }
        });

        container.mouseup(new Function() {
            @Override
            public boolean f(Event e) {
                return containerMouseUp(e);
            }
        });

        container.mouseenter(new Function() {
            @Override
            public boolean f(Event e) {
                mouseOnContainer = true;
                return false;
            }
        });

        container.mouseleave(new Function() {
            @Override
            public boolean f(Event e) {
                mouseOnContainer = false;
                return false;
            }
        });

        searchResults.mouseup(new Function() {
            @Override
            public boolean f(Event e) {
                return searchResultsMouseUp(e);
            }
        });

        searchResults.mouseover(new Function() {
            @Override
            public boolean f(Event e) {
                return searchResultsMouseOver(e);
            }
        });

        searchResults.mouseout(new Function() {
            @Override
            public boolean f(Event e) {
                return searchResultsMouseOut(e);
            }
        });

        if (eventBus != null) {
            updateEventHandlerRegistration =
                    eventBus.addHandler(UpdatedEvent.getType(), new UpdatedEvent.UpdatedHandler() {
                        public void onUpdated() {
                            update();
                        }
                    });
        }

        searchField.blur(new Function() {
            @Override
            public boolean f(Event e) {
                return inputBlur();
            }
        });

        searchField.keyup(new Function() {
            @Override
            public boolean f(Event e) {
                return keyupChecker(e);
            }
        });

        searchField.keydown(new Function() {
            @Override
            public boolean f(Event e) {
                return keydownChecker(e);
            }
        });

        searchField.on("paste", new Function() {
            @Override
            public void f() {
                doSearch();
            }
        });

        searchField.on("drop", new Function() {
            @Override
            public void f() {
                doSearch();
            }
        });
    }

    protected abstract SafeHtml buildContainerHtml(String defaultText, SafeStylesBuilder ssb);

    protected String buildDropdownWidth(int ddWidth) {
        return ddWidth + "px";
    }

    protected StringBuilder buildOptionStyleClass(OptionItem option) {
        StringBuilder classes = new StringBuilder();

        if (option.isSelected()) {
            classes.append(css.resultSelected()).append(" ");
        }

        if (option.getGroupArrayIndex() != -1) {
            classes.append(css.groupOption()).append(" ");
        }

        if (option.getClasses() != null) {
            classes.append(option.getClasses());
        }

        return classes;
    }

    protected int calculateDropdownTop() {
        int ddTop;
        DropdownPosition dropdownPosition = options.getDropdownPosition();

        switch (dropdownPosition) {
            case ABOVE:
                ddTop = positionAbove();
                break;
            case AUTO:
                if (container.hasClass(css.resultAbove())) {
                    // if dropdown is already above, let it there.
                    ddTop = positionAbove();
                } else if (options.getDropdownBoundaries() == null && options.getDropdownBoundariesProvider() == null) {
                    ddTop = positionRelativeToWindow();
                } else {
                    ddTop = positionRelativeToBoundaries();
                }
                break;
            case BELOW:
            default:
                ddTop = positionBelow();
                break;
        }

        return ddTop;
    }

    protected void choiceDestroy(GQuery link) {
        choices--;
        showSearchFieldDefault(defaultText);

        if (isMultiple() && choices > 0 && searchField.val() != null && searchField.val().length() < 1) {
            resultsHide();
        }

        resultDeselect(Integer.parseInt(link.attr("rel")), link.attr("data-chosen-value"));
        link.parents("li").first().remove();
    }

    protected boolean containerMouseDown(Event e) {
        if (isDisabled) {
            return true;
        }

        Element mouseDownTarget = e.getEventTarget().cast();
        GQuery $e = $(mouseDownTarget);

        boolean targetCloseLink = $e.hasClass(css.searchChoiceClose());

        if (!resultsShowing) {
            e.stopPropagation();
        }

        if (!targetCloseLink) {
            containerMouseDownImpl(e, $e);
        }

        return false;
    }

    protected void containerMouseDownImpl(Event e, GQuery element) {
        if (!activeField) {
            $(document).click(clickTestAction);
            resultsShow();
        } else if (!element.isEmpty() && (element.get(0) == selectedItem.get(0)
                || element.parents("a." + css.chznSingle()).length() > 0)) {
            e.preventDefault();
            resultsToggle();
        }

        if (!element.hasClass(css.activeResult())) {
            activateField();
        }
    }

    protected void deactiveTabIndexProperty() {
        selectedItem.attr(TABINDEX_PROPERTY, searchField.attr(TABINDEX_PROPERTY));
        searchField.attr(TABINDEX_PROPERTY, "-1");
    }

    protected void fireEvent(ChosenEvent<?> event) {
        if (eventBus != null) {
            eventBus.fireEvent(event);
        }
    }

    protected String getContainerClass() {
        return css.chznContainer();
    }

    protected String getContainerId() {
        return containerId;
    }

    protected ChosenCss getCss() {
        return css;
    }

    protected GQuery getDropdown() {
        return dropdown;
    }

    protected GQuery getSearchChoices() {
        return searchChoices;
    }

    protected void setSearchChoices(GQuery searchChoices) {
        this.searchChoices = searchChoices;
    }

    protected GQuery getSearchContainer() {
        return searchContainer;
    }

    protected void setSearchContainer(GQuery searchContainer) {
        this.searchContainer = searchContainer;
    }

    protected GQuery getSearchField() {
        return searchField;
    }

    protected GQuery getSearchResults() {
        return searchResults;
    }

    protected GQuery getSelectedItem() {
        return selectedItem;
    }

    protected void setSelectedItem(GQuery selectedItem) {
        this.selectedItem = selectedItem;
    }

    protected int getSideBorderPadding(GQuery elmt, boolean isHidden) {
        if (isHidden) {
            // bug in gquery when one parent of the element is hidden
            return (int) (elmt.cur("padding-left", true) + elmt.cur("padding-right",
                    true) + elmt.cur("border-left-width", true) + elmt.cur("border-right-width", true));
        }
        return elmt.outerWidth() - elmt.width();
    }

    protected void init(SelectElement element, ChosenOptions options, EventBus eventBus) {
        this.selectElement = element;
        this.options = options;
        this.eventBus = eventBus;

        $selectElement = $(selectElement);

        setDefaultValues();

        setDefaultText();

        setup();

        bind();

        finishSetup();
    }

    protected void initSearchElement(int ddWidth, boolean isHidden) {
        setSearchContainer(getContainer().find("div." + getCss().chznSearch()).first());
        setSelectedItem(getContainer().find("." + getCss().chznSingle()).first());
    }

    protected boolean isRTL() {
        return isRTL;
    }

    protected boolean isDisabled() {
        return isDisabled;
    }

    protected boolean keydownChecker(Event e) {
        int stroke = e.getKeyCode();
        searchFieldScale(fWidth);

        switch (stroke) {
            case TAB:
                onTabKeydown(e);
                break;

            case ENTER:
                if (resultsShowing) {
                    e.preventDefault();
                    return false;
                }
                return true;

            case UP_ARROW:
                e.preventDefault();
                keyupArrow();
                return false;

            case DOWN_ARROW:
                this.keydownArrow();
                return false;
        }

        return true;
    }

    protected boolean maxSelectedOptionsReached() {
        return options.getMaxSelectedOptions() != -1 && options.getMaxSelectedOptions() <= choices;
    }

    protected void onKeydownBackstroke() {
        resultClearHighlight();
        resultsSearch();
    }

    protected void onTabKeydown(Event e) {
        mouseOnContainer = false;
    }

    protected int positionBelow() {
        return container.outerHeight() - 1;
    }

    protected GQuery querySelectedResults() {
        return searchResults.find("." + css.resultSelected() + "." + css.activeResult());
    }

    protected void release() {
        if (updateEventHandlerRegistration != null) {
            updateEventHandlerRegistration.removeHandler();
            updateEventHandlerRegistration = null;
        }
        // empty the searchResult to speed up the container.remove()
        if (searchResults != null) {
            searchResults.html("");
        }

        // remove method clean listener and cie
        container.remove();

        $selectElement.removeClass(css.chznDone(), "chzn-done").show();
    }

    protected void resetSelectedItem() {
        selectedItem.find("span").text(defaultText);
        selectedItem.addClass(css.chznDefault());
    }

    protected void resultSelect(Event e) {
        if (resultHighlight != null) {
            GQuery high = resultHighlight;

            resultClearHighlight();

            resultDeactivate(high, true);

            high.addClass(css.resultSelected());

            OptionItem item = getOptionItem(high);
            item.setSelected(true);
            OptionElement option = selectElement.getOptions().getItem(item.getOptionsIndex());
            if (option != null) {
                option.setSelected(true);
            }

            addChoice(item);

            winnowResultsClear();

            String oldValue = getCurrentValue();
            String newValue = item.getValue();

            selectedValues.add(newValue);

            onResultSelected(item, newValue, oldValue, e.getMetaKey());

            searchFieldScale(fWidth);
        }
    }

    protected OptionItem getOptionItem(GQuery result) {
        String highId = result.attr("id");
        int position = Integer.parseInt(highId.substring(highId.lastIndexOf("_") + 1));
        return (OptionItem) selectItems.get(position);
    }

    protected void onResultSelected(OptionItem item, String newValue, String oldValue, boolean metaKeyPressed) {
        fireChosenChangeEventIfNotEqual(item, newValue, oldValue);

        resultsHide();
    }

    protected void fireChosenChangeEventIfNotEqual(OptionItem item, String newValue, String oldValue) {
        if (oldValue == null || !oldValue.equals(newValue)) {
            fireEvent(new ChosenChangeEvent(newValue, item.getArrayIndex(), this));
        }
    }

    protected void addChoice(OptionItem item) {
        selectedItem.find("span").text(item.getText());
        if (allowSingleDeselect) {
            singleDeselectControlBuild();
        }

        selectedValues.clear();
    }

    protected void resultsBuild(boolean init) {
        resultsBuild(init, defaultText, customFilter);
    }

    protected void resultsBuild(boolean init, String defaultText, boolean customFilter) {
        selectItems = new SelectParser().parse(selectElement);

        rebuildResultItems(init);
    }

    protected void resultsHide() {
        if (!resultsShowing) {
            return;
        }

        if (selectedItem != null) {
            selectedItem.removeClass(css.chznSingleWithDrop());
        }

        resultClearHighlight();

        fireEvent(new HidingDropDownEvent(this));

        dropdown.css(isRTL ? "right" : "left", HORIZONTAL_OFFSET + "px");
        dropdown.css("top", VERTICAL_OFFSET + "px");

        container.removeClass(css.resultAbove());

        resultsShowing = false;
    }

    protected void resultsResetCleanup() {
        if (selectedItem != null) {
            selectedItem.find("abbr").remove();
        }
    }

    protected void resultsSearch() {
        if (resultsShowing) {
            winnowResults(resultsShowing);
        } else {
            resultsShow();
        }
    }

    protected boolean resultsShow() {
        if (!beforeShowResult()) {
            return false;
        }

        fireEvent(new ShowingDropDownEvent(this));

        resultsShowing = true;

        searchField.val(searchField.val());

        winnowResults(true);

        positionDropdownResult();

        searchField.focus();

        return true;
    }

    protected boolean beforeShowResult() {
        return true;
    }

    protected void searchFieldScale(int fWidth) {
        // do nothing
    }

    protected boolean searchResultsMouseOut(Event e) {
        Element targetEl = e.getEventTarget().cast();
        GQuery $e = $(targetEl);

        if ($e.hasClass(css.activeResult()) || $e.parents("." + css.activeResult()).length() > 0) {
            resultClearHighlight();
        }

        return false;
    }

    protected boolean searchResultsMouseUp(Event e) {
        Element targetEvent = e.getEventTarget().cast();
        GQuery $e = $(targetEvent);

        GQuery target =
                $e.hasClass(css.activeResult()) ? $e : $e.parents("." + css.activeResult()).first();
        if (!target.isEmpty()) {
            resultHighlight = target;
            resultSelect(e);
        }

        return false;
    }

    protected void setTabIndexProperty(String tabIndexProperty) {
        selectedItem.attr(TABINDEX_PROPERTY, tabIndexProperty);
        searchField.attr(TABINDEX_PROPERTY, -1);
    }

    protected void setupDisabledSearchField() {
        container.addClass(css.chznDisabled());
        InputElement.as(searchField.get(0)).setDisabled(true);
    }

    protected void setupEnabledSearchField() {
        container.removeClass(css.chznDisabled());
        InputElement.as(searchField.get(0)).setDisabled(false);
    }

    protected boolean shouldActivateResult(GQuery result) {
        return true;
    }

    protected void showSearchFieldDefault(String defaultText) {
        searchField.val("");
        searchField.removeClass(css.defaultClass());
    }

    protected void update() {
        setDefaultText();
        resultClearHighlight();
        resultsBuild(false);
    }

    void noResults(String terms) {
        GQuery noResults =
                $(ChosenTemplate.templates.noResults(css.noResults(), resultsNoneFound).asString());
        noResults.find("span").html(terms);

        searchResults.append(noResults);
    }

    void positionDropdownResult() {
        int ddTop = calculateDropdownTop();
        if (ddTop < 0) {
            dropdown.prepend(searchResults);
            container.addClass(css.resultAbove());
        }

        dropdown.css("top", ddTop + "px").css(isRTL ? "right" : "left", "0");
    }

    void resultActivate(GQuery query) {
        query.addClass(css.activeResult());
    }

    void resultClearHighlight() {
        if (resultHighlight != null) {
            resultHighlight.removeClass(css.highlighted());
            resultHighlight = null;
        }
    }

    void resultDeactivate(GQuery query) {
        resultDeactivate(query, false);
    }

    boolean searchResultsMouseOver(Event e) {
        Element targetEl = e.getEventTarget().cast();
        GQuery $e = $(targetEl);

        GQuery target =
                $e.hasClass(css.activeResult()) ? $e : $e.parents("." + css.activeResult()).first();
        if (!target.isEmpty()) {
            resultDoHighlight(target);
        }

        return false;
    }

    protected void resultDeactivate(GQuery query, boolean selected) {
        if (!selected) {
            query.removeClass(getCss().activeResult(), getCss().foundResult());
        } else {
            searchResults.find("." + css.resultSelected()).removeClass(css.resultSelected());
            selectedItem.removeClass(css.chznDefault());
        }
    }

    void winnowResultsSetHighlight() {
        if (resultHighlight == null) {
            GQuery selectedResults = querySelectedResults();

            GQuery doHigh =
                    selectedResults != null && selectedResults.length() > 0 ? selectedResults.first()
                            : getFirstActive();

            if (doHigh != null) {
                resultDoHighlight(doHigh);
            }
        }
    }

    private void activateFirstResult() {
        GQuery firstActive = getActiveResults().first();
        resultDoHighlight(firstActive);
    }

    private void activateLastResult() {
        GQuery lastActive = getActiveResults().last();
        resultDoHighlight(lastActive);
    }

    private void blurTest() {
        if (!activeField && container.hasClass(css.chznContainerActive())) {
            closeField();
        }
    }

    private String buildContainerId() {
        String id;
        String selectElementId = selectElement.getId();

        if (selectElementId != null && selectElementId.length() > 0) {
            id = containerIdRegExp.replace(selectElementId, "_");
        } else {
            id = generateContainerId();
            selectElement.setId(id);
        }

        id += "_chzn";

        return id;
    }

    protected void closeField() {
        $(document).unbind("click", clickTestAction);

        deactiveTabIndexProperty();

        activeField = false;

        resultsHide();

        container.removeClass(css.chznContainerActive());
        winnowResultsClear();

        showSearchFieldDefault(defaultText);
        searchFieldScale(fWidth);
    }

    private boolean containerMouseUp(Event e) {
        Element target = e.getEventTarget().cast();

        GQuery $e = $(target);

        if (!$e.isEmpty() && "ABBR".equalsIgnoreCase($e.get(0).getNodeName()) && !isDisabled) {
            resultsReset();
            return false;
        }

        return true;
    }

    private SafeHtml createOption(OptionItem item) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.append(fromTrustedString("<option value='")).appendEscaped(item.getValue())
                .append(fromTrustedString("'"));

        if (item.isSelected()) {
            builder.append(fromTrustedString(" selected"));
        }

        if (item.isDisabled()) {
            builder.append(fromTrustedString(" disabled"));
        }

        builder.append(fromTrustedString(">")).appendEscaped(item.getText());
        builder.append(fromTrustedString("</option>"));

        return builder.toSafeHtml();
    }

    private void deselect(String value) {
        List<String> newValues = new ArrayList<String>();
        boolean found = false;
        for (String s : selectedValues) {
            if (found || !s.equals(value)) {
                newValues.add(s);
            } else {
                // in case of the same item has been selected many times
                found = true;
            }
        }

        selectedValues = newValues;
    }

    private void doSearch() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                resultsSearch();
            }
        });
    }

    private void finishSetup() {
        $selectElement.addClass(css.chznDone(), "chzn-done");
    }

    private String generateContainerId() {
        String id = DEFAULT_CONTAINER_ID + idCounter++;

        while (!$("#" + id).isEmpty()) {
            idCounter++;
            id = DEFAULT_CONTAINER_ID + idCounter;
        }
        return id;
    }

    private GQuery getActiveResults() {
        return searchResults.find("li." + css.activeResult());
    }

    private GQuery getFirstActive() {
        for (Element element : searchResults.elements()) {
            GQuery gq = $(element);
            if (gq.hasClass(css.activeResult())) {
                return gq;
            }
        }
        return $();
    }

    private boolean inputBlur() {
        if (!mouseOnContainer) {
            activeField = false;
            Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
                public boolean execute() {
                    blurTest();
                    return false;
                }
            }, 100);
        }
        return false;
    }

    private boolean isDetached(GQuery element) {
        return element.parents().filter("body").isEmpty();
    }

    private boolean isNotResultHighlighted() {
        return resultHighlight == null || isDetached(resultHighlight);
    }

    private void keydownArrow() {
        if (isNotResultHighlighted()) {
            activateFirstResult();
        } else if (resultsShowing) {
            // TODO should be replaced by :
            // GQuery nextSibling = resultHighlight.nextAll("li."+css.activeResult()).first();
            // but performance is bad... See http://code.google.com/p/gwtquery/issues/detail?id=146

            GQuery nextSibling = resultHighlight.next();

            while (!nextSibling.isEmpty() && !nextSibling.is("li." + css.activeResult())) {
                nextSibling = nextSibling.next();
            }

            resultDoHighlight(nextSibling);
        }

        if (!resultsShowing) {
            resultsShow();
        }
    }

    private void keyupArrow() {
        if (!resultsShowing) {
            resultsShow();
        } else if (isNotResultHighlighted()) {
            activateLastResult();
        } else if (resultHighlight != null) {
            // TODO should be replaced by :
            // GQuery prevSibs = resultHighlight.prevAll("li." + css.activeResult());
            // but performance is bad... See http://code.google.com/p/gwtquery/issues/detail?id=146

            GQuery prevSibling = resultHighlight.prev();

            while (!prevSibling.isEmpty() && !prevSibling.is("li." + css.activeResult())) {
                prevSibling = prevSibling.prev();
            }

            if (prevSibling.length() > 0) {
                resultDoHighlight(prevSibling.first());
            } else {
                if (choices > 0) {
                    resultsHide();
                }

                resultClearHighlight();
            }
        }
    }

    private boolean keyupChecker(Event e) {
        int stroke = e.getKeyCode();

        searchFieldScale(fWidth);

        switch (stroke) {
            case BACKSPACE:
                onKeydownBackstroke();
                break;

            case ENTER:
                if (resultsShowing) {
                    resultSelect(e);
                }
                return true;
            case ESCAPE:
                if (resultsShowing) {
                    resultsHide();
                }
                return false;
            case TAB:
            case SHIFT:
            case CTRL:
            case UP_ARROW:
            case DOWN_ARROW:
            case LEFT_WINDOW_KEY:
                // do nothing
                break;
            default:
                resultsSearch();
                break;
        }

        return true;
    }

    private void noResultClear() {
        searchResults.find("." + css.noResults()).remove();
    }

    private int positionAbove() {
        return -dropdown.outerHeight();
    }

    private int positionRelativeToBoundaries() {
        Element dropdownBoundaries = options.getDropdownBoundaries();
        if (dropdownBoundaries == null) {
            dropdownBoundaries = options.getDropdownBoundariesProvider().getDropdownBoundaries();
        }
        GQuery ddContainer = $(dropdownBoundaries);
        int ddContainerOffsetTop = ddContainer.offset().top;
        int containerOffsetTop = container.offset().top;
        int spaceAbove = containerOffsetTop - ddContainerOffsetTop;

        int spaceBelow = ddContainer.outerHeight() - spaceAbove - container.outerHeight();
        int ddHeight = dropdown.outerHeight();
        return spaceBelow < ddHeight ? positionAbove() : positionBelow();
    }

    private int positionRelativeToWindow() {
        int ddHeight = dropdown.outerHeight();
        int spaceBelow = Window.getClientHeight() - container.offset().top - container.outerHeight();
        return spaceBelow < ddHeight ? positionAbove() : positionBelow();
    }

    private void rebuildResultItems(boolean init) {
        SafeHtmlBuilder content = new SafeHtmlBuilder();
        SafeHtmlBuilder optionsHtml = new SafeHtmlBuilder();

        selectedValues = new ArrayList<String>();

        for (SelectItem item : selectItems) {
            if (item.isGroup()) {
                SafeHtml result = resultAddGroup((GroupItem) item);
                if (result != null) {
                    content.append(result);
                }
            } else {
                OptionItem optionItem = (OptionItem) item;

                if (optionItem.isEmpty()) {
                    continue;
                }

                if (customFilter) {
                    optionsHtml.append(createOption(optionItem));
                }

                SafeHtml optionHtml = resultAddOption(optionItem);
                if (optionHtml != null) {
                    content.append(optionHtml);
                }

                if (optionItem.isSelected()) {
                    addChoice(optionItem);

                    selectedValues.add(optionItem.getValue());
                }
            }
        }

        isDisabled = selectElement.isDisabled();
        if (isDisabled) {
            setupDisabledSearchField();
        } else {
            setupEnabledSearchField();
        }

        if (init) {
            showSearchFieldDefault(defaultText);
            searchFieldScale(fWidth);
        }
        if (customFilter) {
            // keep the html select element synchronized with the new result.
            $selectElement.html(optionsHtml.toSafeHtml().asString());
        }
        searchResults.html(content.toSafeHtml().asString());

        if (resultsShowing) {
            positionDropdownResult();
        }
    }

    private SafeHtml resultAddGroup(GroupItem group) {
        if (!group.isDisabled()) {
            group.domId = containerId + "_g_" + group.getArrayIndex();
            return ChosenTemplate.templates.group(group.domId, css.groupResult(), group.getLabel());
        } else {
            return null;
        }
    }

    private SafeHtml resultAddOption(OptionItem option) {
        if (!option.isDisabled()) {
            option.domId = containerId + "_o_" + option.getArrayIndex();

            StringBuilder classes = buildOptionStyleClass(option);

            SafeStyles safeStyles = SafeStylesUtils.fromTrustedString(option.getStyle());
            if (option.getHtml() != null && !option.getHtml().trim().isEmpty()) {
                SafeHtml html = fromTrustedString(option.getHtml());
                return ChosenTemplate.templates.option(option.getDomId(), classes.toString().trim(), safeStyles, html);
            } else {
                return ChosenTemplate.templates.option(option.getDomId(), classes.toString().trim(), safeStyles,
                        option.getText());
            }
        }
        return null;
    }

    private void resultDeselect(int index, String value) {
        if (index < selectItems.size()) {
            OptionItem item = (OptionItem) selectItems.get(index);

            if (item.getValue().equals(value)) {
                item.setSelected(false);

                // select option in original element
                OptionElement option = selectElement.getOptions().getItem(item.getOptionsIndex());
                if (option != null) {
                    option.setSelected(false);
                }

                $("#" + containerId + "_o_" + index).removeClass(css.resultSelected()).addClass(
                        css.activeResult()).show();
            }
        }

        resultClearHighlight();
        winnowResults(false);

        deselect(value);

        fireEvent(new ChosenChangeEvent(value, index, false, this));

        searchFieldScale(fWidth);
    }

    protected void resultDoHighlight(GQuery el) {
        if (el == null || el.length() == 0 || isDetached(el)) {
            return;
        }

        resultClearHighlight();

        resultHighlight = el;
        el.addClass(css.highlighted());

        int searchResultHeight = searchResults.innerHeight();
        int visibleTop = searchResults.scrollTop();
        int visibleBottom = searchResultHeight + visibleTop;

        int highTop = resultHighlight.position().top + searchResults.scrollTop();
        int highBottom = highTop + resultHighlight.outerHeight();

        if (highBottom >= visibleBottom) {
            int toScroll = highBottom - searchResultHeight;
            searchResults.scrollTop(toScroll > 0 ? toScroll : 0);
        } else if (highTop < visibleTop) {
            searchResults.scrollTop(highTop);
        }
    }

    private void resultsReset() {
        OptionElement firstoption = selectElement.getOptions().getItem(0);
        selectedValues = new ArrayList<String>();

        if (firstoption != null) {
            firstoption.setSelected(true);
            selectedValues.add(firstoption.getValue());
        }

        resetSelectedItem();

        showSearchFieldDefault(defaultText);
        resultsResetCleanup();

        fireEvent(new ChosenChangeEvent(null, 0, this));

        if (activeField) {
            resultsHide();
        }
    }

    private void resultsToggle() {
        if (resultsShowing) {
            resultsHide();
        } else {
            resultsShow();
        }
    }

    private void setDefaultText() {
        String dataPlaceHolder = selectElement.getAttribute("data-placeholder");

        if (dataPlaceHolder != null && dataPlaceHolder.length() > 0) {
            defaultText = dataPlaceHolder;
        } else if (isMultiple()) {
            if (options.getPlaceholderTextMultiple() != null) {
                defaultText = options.getPlaceholderTextMultiple();
            } else if (options.getPlaceholderText() != null) {
                defaultText = options.getPlaceholderText();
            } else {
                defaultText = "Select Some Options";
            }
        } else {
            if (options.getPlaceholderTextSingle() != null) {
                defaultText = options.getPlaceholderTextSingle();
            } else if (options.getPlaceholderText() != null) {
                defaultText = options.getPlaceholderText();
            } else {
                defaultText = "Select an Option";
            }
        }

        String dataNoResultsText = selectElement.getAttribute("data-no_results_text");
        if (dataNoResultsText != null && dataNoResultsText.length() > 0) {
            resultsNoneFound = dataNoResultsText;
        } else if (options.getNoResultsText() != null) {
            resultsNoneFound = options.getNoResultsText();
        } else {
            resultsNoneFound = "No results match";
        }
    }

    private void setDefaultValues() {
        clickTestAction = new Function() {
            @Override
            public boolean f(Event e) {
                return testActiveClick(e);
            }
        };

        activeField = false;
        mouseOnContainer = false;
        resultsShowing = false;

        NodeList<OptionElement> optionsList = selectElement.getOptions();
        allowSingleDeselect =
                options.isAllowSingleDeselect() && optionsList.getLength() > 0
                        && "".equals(optionsList.getItem(0).getText());

        choices = 0;

        if (options.getResources() != null) {
            css = options.getResources().css();
        } else {
            css = GWT.<Resources>create(Resources.class).css();
        }

        Class<?> resourceClass = options.getResources() != null ? options.getResources().getClass() : Resources.class;

        if (!INJECTED_RESOURCES.contains(resourceClass)) {
            StyleInjector.inject(css.getText(), true);
            INJECTED_RESOURCES.add(resourceClass);
        }

        resultsFilter = options.getResultFilter();

        customFilter = resultsFilter != null;

        if (!customFilter) {
            resultsFilter = new ClientResultsFilter();
        }
    }

    private void setTabIndex() {
        String tabIndexProperty = $selectElement.attr(TABINDEX_PROPERTY);
        if (tabIndexProperty != null && tabIndexProperty.length() > 0) {
            $selectElement.attr(TABINDEX_PROPERTY, -1);

            setTabIndexProperty(tabIndexProperty);
        }
    }

    private void setup() {
        boolean isHidden = false;
        containerId = buildContainerId();
        fWidth = $selectElement.outerWidth();

        isRTL = LocaleInfo.getCurrentLocale().isRTL() || $selectElement.hasClass("chzn-rtl");
        // Temporary fix. IIf the select element is inside a hidden container
        // GQuery cannot get the size of the select element.
        if (fWidth == 0) {
            $("body").append("<div id='gwt_chosen_temp_div' style='display:block;position:absolute;" + (isRTL ?
                    "right" : "left") + ":-9000px; visibility:hidden'> </div>");
            GQuery tempDiv = $("#gwt_chosen_temp_div");
            tempDiv.append($selectElement.clone());

            fWidth = tempDiv.children("select").outerWidth();

            tempDiv.remove();
            isHidden = fWidth > 0;
        }

        String containerClass = getContainerClass();

        String cssClasses = isRTL ? containerClass + " " + css.chznRtl() : containerClass;

        // recopy classes present on the select element
        cssClasses += " " + selectElement.getClassName();

        GQuery containerTemp =
                $(ChosenTemplate.templates.container(containerId, cssClasses).asString()).width(fWidth);

        final SafeStylesBuilder ssb = new SafeStylesBuilder();
        if (isRTL) {
            ssb.right(HORIZONTAL_OFFSET, Style.Unit.PX);
        } else {
            ssb.left(HORIZONTAL_OFFSET, Style.Unit.PX);
        }
        ssb.top(VERTICAL_OFFSET, Style.Unit.PX);

        containerTemp.html(buildContainerHtml(defaultText, ssb).asString());

        // insert container after the select elements
        $selectElement.hide().after(containerTemp);
        container = $("#" + containerId);
        container.addClass(isMultiple() ? css.chznContainerMulti() : css.chznContainerSingle());

        dropdown = container.find("div." + css.chznDrop()).first();
        int ddWidth = fWidth - getSideBorderPadding(dropdown, isHidden);

        dropdown.css("width", buildDropdownWidth(ddWidth));

        searchField = container.find("input").first();
        searchResults = container.find("ul." + css.chznResults()).first();
        searchFieldScale(fWidth);

        initSearchElement(ddWidth, isHidden);

        resultsBuild(true);

        setTabIndex();

        fireEvent(new ReadyEvent(this));
    }

    private void singleDeselectControlBuild() {
        if (allowSingleDeselect && selectedItem.find("abbr").isEmpty()) {
            selectedItem.find("span").first().after(
                    "<abbr class=\"" + css.searchChoiceClose() + " " + css.iconCross() + "\"></abbr>");
        }
    }

    private boolean testActiveClick(Event e) {
        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        if ($e.parents("#" + containerId).length() > 0) {
            activeField = true;
        } else {
            closeField();
        }
        return true;
    }

    private void winnowResults(boolean isShowing) {
        noResultClear();

        String searchText = defaultText.equals(searchField.val()) ? "" : searchField.val().trim();
        searchText = SafeHtmlUtils.htmlEscape(searchText);

        resultsFilter.filter(searchText, this, isShowing);
    }

    private void winnowResultsClear() {
        searchField.val("");
        GQuery lis = searchResults.find("li");

        for (Element li : lis.elements()) {
            GQuery $li = $(li);
            if ($li.hasClass(css.groupResult())) {
                $li.css("display", "");
            } else if (shouldActivateResult($li)) {
                resultActivate($li);
            }
            $li.removeClass(css.foundResult());
        }
    }
}
