/*
 * Copyright (C) 2012 Julien Dramaix
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.watopi.chosen.client;

import com.watopi.chosen.client.resources.Resources;

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

    public ChosenOptions setAllowSingleDeselect(Boolean allowSingleDeselect) {
        this.allowSingleDeselect = allowSingleDeselect;
        return this;
    }

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

    public ChosenOptions setSearchContains(boolean searchContains) {
        this.searchContains = searchContains;
        return this;
    }

    public ChosenOptions setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
        this.singleBackstrokeDelete = singleBackstrokeDelete;
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

    }
}
