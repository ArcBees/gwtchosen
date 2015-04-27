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
import com.arcbees.chosen.client.event.MaxSelectedEvent;
import com.arcbees.chosen.client.event.ReadyEvent;
import com.arcbees.chosen.client.event.ShowingDropDownEvent;
import com.arcbees.chosen.client.event.UpdatedEvent;
import com.arcbees.chosen.client.resources.ChozenCss;
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
import com.google.gwt.query.client.Properties;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
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

public class ChosenImpl {
    public interface ChozenTemplate extends SafeHtmlTemplates {
        ChozenTemplate templates = GWT.create(ChozenTemplate.class);

        @Template("<li class=\"{1}\" id=\"{0}\"><span>{2}</span><a href=\"javascript:void(0)\" class=\"{3} {6}\" " +
                "rel=\"{4}\" data-chosen-value=\"{5}\"></a></li>")
        SafeHtml choice(String id, String searchChoiceClass, SafeHtml content,
                String searchChoiceCloseClass, String rel, String value, String iconCloseClass);

        @Template("<div id=\"{0}\" class=\"{1}\"></div>")
        SafeHtml container(String id, String cssClasses);

        @Template("<ul class=\"{0}\"><li class=\"{1}\"><input type=\"text\" value=\"{2}\" class=\"{3}\" " +
                "autocomplete=\"off\" style=\"width:25px;\"/></li></ul><div class=\"{4}\" style=\"{6}\"><ul " +
                "class=\"{5}\"></ul></div>")
        SafeHtml contentMultiple(String chznChoicesClass, String chznSearchFieldClass,
                String defaultText, String defaultClass, String chznDropClass, String chznResultClass,
                SafeStyles offsets);

        @Template("<a href=\"javascript:void(0)\" class=\"{0} {1}\"><span>{2}</span><div><b " +
                "class=\"{7}\"></b></div></a><div class=\"{3}\" style=\"{6}\"><div class=\"{4} {8}\">" +
                "<input type=\"text\" autocomplete=\"off\" /></div><ul class=\"{5}\"></ul></div>")
        SafeHtml contentSingle(String chznSingleClass, String chznDefaultClass, String defaultText,
                String dropClass, String chznSearchClass, String chznResultClass, SafeStyles offsets,
                String iconArrowClass, String iconSearchClass);

        @Template("<li id=\"{0}\" class=\"{1}\">{2}</li>")
        SafeHtml group(String id, String groupResultClass, String content);

        @Template("<li class=\"{0}\">{1}\"<span></span>\"</li>")
        SafeHtml noResults(String noResultsClass, String content);

        @Template("<li id=\"{0}\" class=\"{1}\" style=\"{2}\">{3}</li>")
        SafeHtml option(String id, String groupResultClass, SafeStyles style, String content);

        @Template("<li id=\"{0}\" class=\"{1}\" style=\"{2}\">{3}</li>")
        SafeHtml option(String id, String groupResultClass, SafeStyles safeStyles, SafeHtml htmlContent);
    }

    private static class ClientResultsFilter implements ResultsFilter {
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
                        } else if (optionContent.indexOf(" ") >= 0 || optionContent.indexOf("[") == 0) {
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

    private static final RegExp containerIdRegExp = RegExp.compile("[^\\w]", "g");
    private static final int HORIZONTAL_OFFSET = -9000;
    private static final int VERTICAL_OFFSET = -9000;
    private static final String DEFAULT_CONTAINER_ID = "chozen_container__";
    private static final Set<Class> INJECTED_RESOURCES = new HashSet<Class>();

    private static final String TABINDEX_PROPERTY = "tabindex";
    private static int idCounter;

    private GQuery $selectElement;
    private Function activateAction;
    private boolean activeField;
    private boolean allowSingleDeselect;
    private int backstrokeLength;
    private int choices;
    private Function clickTestAction;
    private GQuery container;
    private String containerId;
    private ChozenCss css;
    private List<String> selectedValues = new ArrayList<String>();
    private String defaultText;
    private GQuery dropdown;
    private EventBus eventBus;
    private int fWidth;
    private boolean isDisabled;
    private boolean isMultiple;
    private boolean isRTL;
    private boolean customFilter;
    private boolean mouseOnContainer;
    private ChosenOptions options;
    private GQuery pendingBackstroke;
    private boolean pendingDestroyClick;
    private GQuery resultHighlight;
    private GQuery resultSingleSelected;
    private String resultsNoneFound;
    private boolean resultsShowing;
    private GQuery searchChoices;
    private GQuery searchContainer;
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

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public ChosenOptions getOptions() {
        return options;
    }

    public SelectElement getSelectElement() {
        return selectElement;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * Return the highlighted result or null if the.
     */
    public GQuery getResultHighlight() {
        return resultHighlight;
    }

    /**
     * Is the plugin support the current browser ?
     *
     * @return
     */
    public boolean isSupported() {
        return true;
    }

    public void rebuildResultItems() {
        rebuildResultItems(false);
    }

    protected void init(SelectElement element, ChosenOptions options, EventBus eventBus) {
        this.selectElement = element;
        this.options = options;
        this.eventBus = eventBus;

        this.$selectElement = $(selectElement);

        setDefaultValues();

        this.isMultiple = selectElement.isMultiple();

        setDefaultText();

        setup();

        bind();

        finishSetup();
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

    protected void update() {
        if (!isMultiple()) {
            resultsResetCleanup();
        }

        setDefaultText();
        resultClearHighlight();
        resultSingleSelected = null;
        resultsBuild(false);
    }

    private boolean activateField(Event e) {
        if (!isMultiple && !activeField) {
            searchField.attr(TABINDEX_PROPERTY, selectedItem.attr(TABINDEX_PROPERTY));
            selectedItem.attr(TABINDEX_PROPERTY, -1);
        }

        container.addClass(css.chznContainerActive());
        activeField = true;

        searchField.val(searchField.val());
        searchField.focus();
        return false;
    }

    private void bind() {
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
                        public void onUpdated(UpdatedEvent event) {
                            update();
                        }
                    });
        }

        searchField.blur(new Function() {
            @Override
            public boolean f(Event e) {
                return inputBlur(e);
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

        if (isMultiple) {
            searchChoices.click(new Function() {
                @Override
                public boolean f(Event e) {
                    return choicesClick(e);
                }
            });
            searchField.focus(new Function() {
                @Override
                public boolean f(Event e) {
                    return inputFocus(e);
                }
            });
        } else {
            container.click(new Function() {
                @Override
                public boolean f(Event e) {
                    e.preventDefault();
                    return false;
                }
            });
        }
    }

    private void doSearch() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                resultsSearch();
            }
        });
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

    private void choiceBuild(OptionItem option) {
        if (isMultiple && maxSelectedOptionsReached()) {
            fireEvent(new MaxSelectedEvent(this));
            return;
        }

        String choiceId = containerId + "_c_" + option.getArrayIndex();
        choices++;
        SafeHtml html = fromTrustedString(option.getHtml());
        searchContainer.before(ChozenTemplate.templates.choice(choiceId, css.searchChoice(), html,
                css.searchChoiceClose(), "" + option.getArrayIndex(), option.getValue(), css.iconCross()).asString());
        $('#' + choiceId).find("a").click(new Function() {
            public boolean f(final Event e) {
                choiceDestroyLinkClick(e);
                return false;
            }
        });
    }

    private void choiceDestroy(GQuery link) {
        choices--;
        showSearchFieldDefault();
        if (isMultiple && choices > 0 && searchField.val() != null && searchField.val().length() < 1) {
            resultsHide();
        }

        resultDeselect(Integer.parseInt(link.attr("rel")), link.attr("data-chosen-value"));
        link.parents("li").first().remove();
    }

    private void choiceDestroyLinkClick(Event e) {
        e.preventDefault();
        if (!isDisabled) {
            pendingDestroyClick = true;
            Element target = e.getEventTarget().cast();
            choiceDestroy($(target));
        } else {
            e.stopPropagation();
        }
    }

    private boolean choicesClick(Event e) {
        e.preventDefault();

        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        if (activeField
                && !($e.hasClass(css.searchChoice()) || !$e.parents("." + css.searchChoice()).isEmpty())
                && !resultsShowing) {
            resultsShow();
        }
        return true;
    }

    private void clearBackstroke() {
        if (pendingBackstroke != null) {
            pendingBackstroke.removeClass(css.searchChoiceFocus());
        }
        pendingBackstroke = null;
    }

    private void closeField() {
        $(document).unbind("click", clickTestAction);

        if (!isMultiple) {
            selectedItem.attr(TABINDEX_PROPERTY, searchField.attr(TABINDEX_PROPERTY));
            searchField.attr(TABINDEX_PROPERTY, "-1");
        }

        activeField = false;

        resultsHide();

        container.removeClass(css.chznContainerActive());
        winnowResultsClear();
        clearBackstroke();

        showSearchFieldDefault();
        searchFieldScale();
    }

    private boolean containerMouseDown(Event e) {
        if (isDisabled) {
            return true;
        }
        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        boolean targetCloseLink = $e.hasClass(css.searchChoiceClose());

        if (!resultsShowing) {
            e.stopPropagation();
        }

        if (!pendingDestroyClick && !targetCloseLink) {
            if (!activeField) {
                if (isMultiple) {
                    searchField.val("");
                }
                $(document).click(clickTestAction);
                resultsShow();
            } else if (!isMultiple && !$e.isEmpty()
                    && ($e.get(0) == selectedItem.get(0) || $e.parents("a." + css.chznSingle()).length() > 0)) {
                e.preventDefault();
                resultsToggle();
            }

            activateField(e);
        } else {
            pendingDestroyClick = false;
        }

        return false;
    }

    private boolean containerMouseUp(Event e) {
        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        if (!$e.isEmpty() && "ABBR".equalsIgnoreCase($e.get(0).getNodeName()) && !isDisabled) {
            resultsReset(e);
            return false;
        }

        return true;
    }

    private void finishSetup() {
        $selectElement.addClass(css.chznDone(), "chzn-done");
    }

    private void fireEvent(ChosenEvent<?> event) {
        if (eventBus != null) {
            eventBus.fireEvent(event);
        }
    }

    private String generateContainerId() {
        String id = DEFAULT_CONTAINER_ID + idCounter++;

        while (!$("#" + id).isEmpty()) {
            idCounter++;
            id = DEFAULT_CONTAINER_ID + idCounter;
        }
        return id;
    }

    private int getSideBorderPadding(GQuery elmt, boolean isHidden) {
        if (isHidden) {
            // bug in gquery when one parent of the element is hidden
            return (int) (elmt.cur("padding-left", true) + elmt.cur("padding-right",
                    true) + elmt.cur("border-left-width", true) + elmt.cur("border-right-width", true));
        }
        return elmt.outerWidth() - elmt.width();
    }

    private boolean inputBlur(Event e) {
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

    private boolean isDetached(GQuery element) {
        return element.parents().filter("body").isEmpty();
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

    private void keydownBackstroke() {
        if (pendingBackstroke != null) {
            choiceDestroy(pendingBackstroke.find("a").first());
            clearBackstroke();
        } else {
            pendingBackstroke = searchContainer.siblings("li." + css.searchChoice()).last();
            if (options.isSingleBackstrokeDelete()) {
                keydownBackstroke();
            } else {
                pendingBackstroke.addClass(css.searchChoiceFocus());
            }
        }
    }

    private boolean keydownChecker(Event e) {
        int stroke = e.getKeyCode();
        searchFieldScale();

        if (stroke != 8 && pendingBackstroke != null) {
            clearBackstroke();
        }

        switch (stroke) {
            case 8: // backspace
                backstrokeLength = searchField.val().length();
                break;

            case 9: // tab
                if (resultsShowing && !isMultiple) {
                    resultSelect(e);
                }
                mouseOnContainer = false;
                break;

            case 13: // enter
                if (resultsShowing) {
                    e.preventDefault();
                    return false;
                }
                return true;

            case 38: // up arrow
                e.preventDefault();
                keyupArrow();
                return false;

            case 40: // down arrow
                this.keydownArrow();
                return false;
        }

        return true;
    }

    private void keyupArrow() {
        if (!resultsShowing && !isMultiple) {
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

    private void activateLastResult() {
        GQuery lastActive = getActiveResults().last();
        resultDoHighlight(lastActive);
    }

    private void activateFirstResult() {
        GQuery firstActive = getActiveResults().first();
        resultDoHighlight(firstActive);
    }

    private GQuery getActiveResults() {
        return searchResults.find("li." + css.activeResult());
    }

    private boolean isNotResultHighlighted() {
        return resultHighlight == null || isDetached(resultHighlight);
    }

    private boolean keyupChecker(Event e) {
        int stroke = e.getKeyCode();

        searchFieldScale();

        switch (stroke) {
            case 8: // backspace
                if (isMultiple && backstrokeLength < 1 && choices > 0) {
                    keydownBackstroke();
                } else if (pendingBackstroke == null) {
                    resultClearHighlight();
                    resultsSearch();
                }
                break;

            case 13: // enter
                if (resultsShowing) {
                    resultSelect(e);
                }
                return true;
            case 27: // escape
                if (resultsShowing) {
                    resultsHide();
                }
                return false;
            case 9:
            case 38:
            case 40:
            case 16:
            case 91:
            case 17:
                // do nothing
                break;
            default:
                resultsSearch();
                break;
        }

        return true;
    }

    private boolean maxSelectedOptionsReached() {
        return options.getMaxSelectedOptions() != -1 && options.getMaxSelectedOptions() <= choices;
    }

    private void noResultClear() {
        searchResults.find("." + css.noResults()).remove();
    }

    private void noResults(String terms) {
        GQuery noResults =
                $(ChozenTemplate.templates.noResults(css.noResults(), resultsNoneFound).asString());
        noResults.find("span").html(terms);

        searchResults.append(noResults);
    }

    private void resultActivate(GQuery query) {
        query.addClass(css.activeResult());
    }

    private SafeHtml resultAddGroup(GroupItem group) {
        if (!group.isDisabled()) {
            group.domId = containerId + "_g_" + group.getArrayIndex();
            return ChozenTemplate.templates.group(group.domId, css.groupResult(), group.getLabel());
        } else {
            return null;
        }
    }

    private SafeHtml resultAddOption(OptionItem option) {
        if (!option.isDisabled()) {
            option.domId = containerId + "_o_" + option.getArrayIndex();

            StringBuilder classes = new StringBuilder();

            if (!(option.isSelected() && isMultiple)) {
                classes.append(css.activeResult()).append(" ");
            }

            if (option.isSelected()) {
                classes.append(css.resultSelected()).append(" ");
            }

            if (option.getGroupArrayIndex() != -1) {
                classes.append(css.groupOption()).append(" ");
            }

            if (option.getClasses() != null) {
                classes.append(option.getClasses());
            }

            SafeStyles safeStyles = SafeStylesUtils.fromTrustedString(option.getStyle());
            if (option.getHtml() != null && !option.getHtml().trim().isEmpty()) {
                SafeHtml html = fromTrustedString(option.getHtml());
                return ChozenTemplate.templates.option(option.getDomId(), classes.toString().trim(), safeStyles, html);
            } else {
                return ChozenTemplate.templates.option(option.getDomId(), classes.toString().trim(), safeStyles,
                        option.getText());
            }
        }
        return null;
    }

    private void resultClearHighlight() {
        if (resultHighlight != null) {
            resultHighlight.removeClass(css.highlighted());
            resultHighlight = null;
        }
    }

    private void resultDeactivate(GQuery query) {
        query.removeClass(css.activeResult(), css.foundResult());
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

        searchFieldScale();
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

    private void resultDoHighlight(GQuery el) {
        if (el == null || el.length() == 0 || isDetached(el)) {
            return;
        }

        resultClearHighlight();

        resultHighlight = el;

        resultHighlight.addClass(css.highlighted());

        int maxHeight = (int) searchResults.cur("maxHeight", true);
        int visibleTop = searchResults.scrollTop();
        int visibleBottom = maxHeight + visibleTop;

        int highTop = resultHighlight.position().top + searchResults.scrollTop();
        int highBottom = highTop + resultHighlight.outerHeight();

        if (highBottom >= visibleBottom) {
            int toScroll = highBottom - maxHeight;
            searchResults.scrollTop(toScroll > 0 ? toScroll : 0);
        } else if (highTop < visibleTop) {
            searchResults.scrollTop(highTop);
        }
    }

    private void resultSelect(Event e) {
        if (resultHighlight != null) {
            GQuery high = resultHighlight;
            String highId = high.attr("id");

            resultClearHighlight();

            if (isMultiple) {
                resultDeactivate(high);
            } else {
                searchResults.find("." + css.resultSelected()).removeClass(css.resultSelected());
                resultSingleSelected = high;
                selectedItem.removeClass(css.chznDefault());
            }

            high.addClass(css.resultSelected());

            int position = Integer.parseInt(highId.substring(highId.lastIndexOf("_") + 1));
            OptionItem item = (OptionItem) selectItems.get(position);
            item.setSelected(true);
            OptionElement option = selectElement.getOptions().getItem(item.getOptionsIndex());
            if (option != null) {
                option.setSelected(true);
            }

            if (isMultiple) {
                choiceBuild(item);
            } else {
                selectedItem.find("span").text(item.getText());
                if (allowSingleDeselect) {
                    singleDeselectControlBuild();
                }
            }

            if (!e.getMetaKey() || !isMultiple) {
                resultsHide();
            }

            searchField.val("");

            String oldValue = getCurrentValue();
            String newValue = item.getValue();

            if (!isMultiple()) {
                selectedValues.clear();
            }

            selectedValues.add(newValue);

            if (isMultiple || oldValue == null || !oldValue.equals($selectElement.val())) {
                fireEvent(new ChosenChangeEvent(newValue, position, this));
            }

            searchFieldScale();
        }
    }

    private void resultsBuild(boolean init) {
        selectItems = new SelectParser().parse(selectElement);

        if (isMultiple && choices > 0) {
            searchChoices.find("li." + css.searchChoice()).remove();
            choices = 0;
        } else if (!isMultiple) {
            selectedItem.addClass(css.chznDefault()).find("span").text(defaultText);

            if (!customFilter && selectElement.getOptions().getLength() <= options.getDisableSearchThreshold()) {
                container.addClass(css.chznContainerSingleNoSearch());
            } else {
                container.removeClass(css.chznContainerSingleNoSearch());
            }
        }

        rebuildResultItems(init);
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
                    if (isMultiple) {
                        choiceBuild(optionItem);
                    } else {
                        selectedItem.removeClass(css.chznDefault()).find("span").text(optionItem.getText());
                        if (allowSingleDeselect) {
                            singleDeselectControlBuild();
                        }

                        selectedValues.clear();
                    }

                    selectedValues.add(optionItem.getValue());
                }
            }
        }

        searchFieldDisabled();

        if (init) {
            showSearchFieldDefault();
            searchFieldScale();
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

    private void resultsHide() {
        if (!resultsShowing) {
            return;
        }

        if (!isMultiple) {
            selectedItem.removeClass(css.chznSingleWithDrop());
        }

        resultClearHighlight();

        fireEvent(new HidingDropDownEvent(this));

        dropdown.css(isRTL ? "right" : "left", HORIZONTAL_OFFSET + "px");
        dropdown.css("top", VERTICAL_OFFSET + "px");

        container.removeClass(css.resultAbove());

        resultsShowing = false;
    }

    private void resultsReset(Event e) {
        OptionElement firstoption = selectElement.getOptions().getItem(0);
        selectedValues = new ArrayList<String>();

        if (firstoption != null) {
            firstoption.setSelected(true);
            selectedValues.add(firstoption.getValue());
        }

        selectedItem.find("span").text(defaultText);
        if (!isMultiple) {
            selectedItem.addClass(css.chznDefault());
        }

        showSearchFieldDefault();
        resultsResetCleanup();

        fireEvent(new ChosenChangeEvent(null, 0, this));
        if (activeField) {
            resultsHide();
        }
    }

    private void resultsResetCleanup() {
        selectedItem.find("abbr").remove();
    }

    private void resultsSearch() {
        if (resultsShowing) {
            winnowResults(resultsShowing);
        } else {
            resultsShow();
        }
    }

    private boolean resultsShow() {
        if (!isMultiple) {
            selectedItem.addClass(css.chznSingleWithDrop());
            if (resultSingleSelected != null) {
                resultDoHighlight(resultSingleSelected);
            }
        } else if (maxSelectedOptionsReached()) {
            fireEvent(new MaxSelectedEvent(this));
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

    private void positionDropdownResult() {
        int ddTop = calculateDropdownTop();
        if (ddTop < 0) {
            dropdown.prepend(searchResults);
            container.addClass(css.resultAbove());
        }

        dropdown.css("top", ddTop + "px").css(isRTL ? "right" : "left", "0");
    }

    private int calculateDropdownTop() {
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

    private int positionAbove() {
        return -dropdown.outerHeight();
    }

    private int positionBelow() {
        return isMultiple ? container.outerHeight() : container.outerHeight() - 1;
    }

    private void resultsToggle() {
        if (resultsShowing) {
            resultsHide();
        } else {
            resultsShow();
        }
    }

    private void searchFieldDisabled() {
        isDisabled = selectElement.isDisabled();
        if (isDisabled) {
            container.addClass(css.chznDisabled());
            InputElement.as(searchField.get(0)).setDisabled(true);
            if (!isMultiple) {
                selectedItem.unbind("focus", activateAction);
            }
        } else {
            container.removeClass(css.chznDisabled());
            InputElement.as(searchField.get(0)).setDisabled(false);
            if (!isMultiple) {
                selectedItem.bind("focus", activateAction);
            }
        }
    }

    private void searchFieldScale() {
        if (!isMultiple) {
            return;
        }

        StringBuilder styleBlock =
                new StringBuilder("position:absolute; " + (isRTL ? "right" : "left") + ": -1000px; top: -1000px; " +
                        "visibility:hidden;");
        String[] styleToCopy =
                {"font-size", "font-style", "font-weight", "font-family", "line-height", "text-transform",
                        "letter-spacing"};

        for (String style : styleToCopy) {
            styleBlock.append(style).append(':').append(searchField.css(style)).append(";");
        }

        GQuery div = $("<div />").attr("style", styleBlock.toString()).text(searchField.val());
        $("body").append(div);

        int w = div.width() + 25;
        div.remove();

        if (w > fWidth - 10) {
            w = fWidth - 10;
        }

        searchField.css("width", w + "px");
    }

    private boolean searchResultsMouseOut(Event e) {
        Element targetEl = e.getEventTarget().cast();
        GQuery $e = $(targetEl);

        if ($e.hasClass(css.activeResult()) || $e.parents("." + css.activeResult()).length() > 0) {
            resultClearHighlight();
        }

        return false;
    }

    private boolean searchResultsMouseOver(Event e) {
        Element targetEl = e.getEventTarget().cast();
        GQuery $e = $(targetEl);

        GQuery target =
                $e.hasClass(css.activeResult()) ? $e : $e.parents("." + css.activeResult()).first();
        if (!target.isEmpty()) {
            resultDoHighlight(target);
        }

        return false;
    }

    private boolean searchResultsMouseUp(Event e) {
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

    private void setDefaultText() {
        String dataPlaceHolder = selectElement.getAttribute("data-placeholder");

        if (dataPlaceHolder != null && dataPlaceHolder.length() > 0) {
            defaultText = dataPlaceHolder;
        } else if (isMultiple) {
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

        activateAction = new Function() {
            @Override
            public boolean f(Event e) {
                return activateField(e);
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

            if (isMultiple) {
                searchField.attr(TABINDEX_PROPERTY, tabIndexProperty);
            } else {
                selectedItem.attr(TABINDEX_PROPERTY, tabIndexProperty);
                searchField.attr(TABINDEX_PROPERTY, -1);
            }
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

        String cssClasses = isRTL ? css.chznContainer() + " " + css.chznRtl() : css.chznContainer();

        // recopy classes present on the select element
        cssClasses += " " + selectElement.getClassName();

        GQuery containerTemp =
                $(ChozenTemplate.templates.container(containerId, cssClasses).asString()).width(fWidth);

        final SafeStylesBuilder ssb = new SafeStylesBuilder();
        if (isRTL) {
            ssb.right(HORIZONTAL_OFFSET, Style.Unit.PX);
        } else {
            ssb.left(HORIZONTAL_OFFSET, Style.Unit.PX);
        }
        ssb.top(VERTICAL_OFFSET, Style.Unit.PX);

        if (isMultiple) {
            containerTemp.html(ChozenTemplate.templates.contentMultiple(css.chznChoices(),
                    css.searchField(), defaultText, css.defaultClass(), css.chznDrop(), css.chznResults(),
                    ssb.toSafeStyles())
                    .asString());
        } else {
            containerTemp.html(ChozenTemplate.templates.contentSingle(css.chznSingle(),
                    css.chznDefault(), defaultText, css.chznDrop(), css.chznSearch(), css.chznResults(),
                    ssb.toSafeStyles(), css.iconArrow(), css.iconSearch())
                    .asString());
        }

        // insert container after the select elements
        $selectElement.hide().after(containerTemp);
        container = $("#" + containerId);
        container.addClass(isMultiple ? css.chznContainerMulti() : css.chznContainerSingle());

        dropdown = container.find("div." + css.chznDrop()).first();
        int ddWidth = fWidth - getSideBorderPadding(dropdown, isHidden);
        dropdown.css(Properties.create("{\"width\": " + ddWidth + "px}"));

        searchField = container.find("input").first();
        searchResults = container.find("ul." + css.chznResults()).first();
        searchFieldScale();

        if (isMultiple) {
            searchChoices = container.find("ul." + css.chznChoices()).first();
            searchContainer = container.find("li." + css.searchField()).first();
        } else {
            searchContainer = container.find("div." + css.chznSearch()).first();
            selectedItem = container.find("." + css.chznSingle()).first();
            int searchFieldWidth =
                    ddWidth - getSideBorderPadding(searchContainer, isHidden)
                            - getSideBorderPadding(searchField, isHidden);
            searchField.css("width", searchFieldWidth + "px");
        }

        resultsBuild(true);

        setTabIndex();

        fireEvent(new ReadyEvent(this));
    }

    private void showSearchFieldDefault() {
        if (isMultiple && choices < 1 && !activeField) {
            searchField.val(defaultText);
            searchField.addClass(css.defaultClass());
        } else {
            searchField.val("");
            searchField.removeClass(css.defaultClass());
        }
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
            } else if (!isMultiple || !$li.hasClass(css.resultSelected())) {
                resultActivate($li);
            }
            $li.removeClass(css.foundResult());
        }
    }

    private void winnowResultsSetHighlight() {
        if (resultHighlight == null) {
            GQuery selectedResults =
                    !isMultiple ? searchResults.find("." + css.resultSelected() + "." + css.activeResult())
                            : null;

            GQuery doHigh =
                    selectedResults != null && selectedResults.length() > 0 ? selectedResults.first()
                            : getFirstActive();

            if (doHigh != null) {
                resultDoHighlight(doHigh);
            }
        }
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
}
