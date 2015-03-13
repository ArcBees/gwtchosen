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

import com.arcbees.chosen.client.resources.Resources;
import com.google.gwt.dom.client.Element;

public class ChosenOptions {
    private boolean allowSingleDeselect;
    private int disableSearchThreshold;
    private int maxSelectedOptions;
    private String noResultsText;
    private String placeholderText;
    private String placeholderTextMultiple;
    private String placeholderTextSingle;
    private Resources resources;
    private boolean searchContains;
    private boolean singleBackstrokeDelete;
    private boolean highlightSearchTerm;
    private ResultsFilter resultFilter;
    private DropdownPosition dropdownPosition;
    private Element dropdownBoundaries;
    private DropdownBoundariesProvider dropdownBoundariesProvider;

    public ChosenOptions() {
        setDefault();
    }

    public int getDisableSearchThreshold() {
        return disableSearchThreshold;
    }

    public int getMaxSelectedOptions() {
        return maxSelectedOptions;
    }

    public String getNoResultsText() {
        return noResultsText;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public String getPlaceholderTextMultiple() {
        return placeholderTextMultiple;
    }

    public String getPlaceholderTextSingle() {
        return placeholderTextSingle;
    }

    public Resources getResources() {
        return resources;
    }

    /**
     * provide the {@code ResultFilter} instance used to filter the data.
     */
    public ResultsFilter getResultFilter() {
        return resultFilter;
    }

    /**
     * Specify if the deselection is allowed on single selects.
     */
    public boolean isAllowSingleDeselect() {
        return allowSingleDeselect;
    }

    public boolean isSearchContains() {
        return searchContains;
    }

    public boolean isSingleBackstrokeDelete() {
        return singleBackstrokeDelete;
    }

    public boolean isHighlightSearchTerm() {
        return highlightSearchTerm;
    }

    public DropdownPosition getDropdownPosition() {
        return dropdownPosition;
    }

    public Element getDropdownBoundaries() {
        return dropdownBoundaries;
    }

    public DropdownBoundariesProvider getDropdownBoundariesProvider() {
        return dropdownBoundariesProvider;
    }

    public ChosenOptions setAllowSingleDeselect(Boolean allowSingleDeselect) {
        this.allowSingleDeselect = allowSingleDeselect;
        return this;
    }

    /**
     * Set the number of items needed to show and enable the search input. This option is when the Chosen component is
     * used in "multiple select" mode or when a custom ResultFilter is used.
     *
     * @param disableSearchThreshold
     */
    public ChosenOptions setDisableSearchThreshold(int disableSearchThreshold) {
        this.disableSearchThreshold = disableSearchThreshold;
        return this;
    }

    public ChosenOptions setMaxSelectedOptions(int maxSelectedOptions) {
        this.maxSelectedOptions = maxSelectedOptions;
        return this;
    }

    public ChosenOptions setNoResultsText(String noResultsText) {
        this.noResultsText = noResultsText;
        return this;
    }

    public ChosenOptions setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        return this;
    }

    public ChosenOptions setPlaceholderTextMultiple(String placeholderTextMultiple) {
        this.placeholderTextMultiple = placeholderTextMultiple;
        return this;
    }

    public ChosenOptions setPlaceholderTextSingle(String placeholderTextSingle) {
        this.placeholderTextSingle = placeholderTextSingle;
        return this;
    }

    public ChosenOptions setResources(Resources resources) {
        this.resources = resources;
        return this;
    }

    public void setResultFilter(ResultsFilter resultFilter) {
        this.resultFilter = resultFilter;
    }

    public ChosenOptions setSearchContains(boolean searchContains) {
        this.searchContains = searchContains;
        return this;
    }

    public ChosenOptions setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
        this.singleBackstrokeDelete = singleBackstrokeDelete;
        return this;
    }

    /**
     * Sets the positioning of the dropdown.
     * If set to {@link com.arcbees.chosen.client.DropdownPosition#BELOW}, dropdown will be displayed below the input
     * box.
     * If set to {@link com.arcbees.chosen.client.DropdownPosition#ABOVE}, dropdown will be displayed above the input
     * box.
     * If set to {@link com.arcbees.chosen.client.DropdownPosition#AUTO}, dropdown will be displayed below the input
     * box only if there's enough vertical space between the input box and the bottom of the
     * {@link com.arcbees.chosen.client.ChosenOptions#dropdownBoundaries}. Otherwise, the dropdown will be displayed
     * above the input box.
     *
     * If not set, it will default to {@link com.arcbees.chosen.client.DropdownPosition#BELOW}.
     */
    public ChosenOptions setDropdownPosition(DropdownPosition dropdownPosition) {
        this.dropdownPosition = dropdownPosition;
        return this;
    }

    /**
     * When {@link com.arcbees.chosen.client.ChosenOptions#dropdownPosition} is set to
     * {@link com.arcbees.chosen.client.DropdownPosition#AUTO}, this element will be used to calculate the available
     * vertical space below the dropdown.
     */
    public ChosenOptions setDropdownBoundaries(Element dropdownBoundaries) {
        this.dropdownBoundaries = dropdownBoundaries;
        return this;
    }

    /**
     * See {@link com.arcbees.chosen.client.ChosenOptions#setDropdownBoundaries(com.google.gwt.dom.client.Element)}.
     * Useful for cases when the {@link com.arcbees.chosen.client.ChosenOptions#dropdownBoundaries} cannot be defined
     * when the Chosen widget is built.
     *
     * {@link com.arcbees.chosen.client.ChosenOptions#setDropdownBoundaries(com.google.gwt.dom.client.Element)} will
     * have priority over this setting.
     */
    public ChosenOptions setDropdownBoundariesProvider(DropdownBoundariesProvider dropdownBoundariesProvider) {
        this.dropdownBoundariesProvider = dropdownBoundariesProvider;
        return this;
    }

    public void setHighlightSearchTerm(boolean highlightSearchTerm) {
        this.highlightSearchTerm = highlightSearchTerm;
    }

    private void setDefault() {
        allowSingleDeselect = false;
        disableSearchThreshold = 0;
        searchContains = false;
        singleBackstrokeDelete = false;
        maxSelectedOptions = -1;
        highlightSearchTerm = true;
        dropdownPosition = DropdownPosition.BELOW;
    }
}
