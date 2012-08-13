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

public class ChosenOptions {

	private boolean allowSingleDeselect;
	private int disableSearchThreshold;
	private boolean searchContains;
	private boolean singleBackstrokeDelete;
	private int maxSelectedOptions;
	private String placeholderTextMultiple;
	private String placeholderText;
	private String placeholderTextSingle;
	private String noResultsText;

	public ChosenOptions() {
		setDefault();
	}

	private void setDefault() {
		allowSingleDeselect = false;
		disableSearchThreshold = 0;
		searchContains = false;
		singleBackstrokeDelete = false;
		maxSelectedOptions = -1;

	}

	public ChosenOptions setAllowSingleDeselect(Boolean allowSingleDeselect) {
		this.allowSingleDeselect = allowSingleDeselect;
		return this;
	}

	/**
	 * Specify if the deselection is allowed on single selects.
	 * 
	 */
	public boolean isAllowSingleDeselect() {
		return allowSingleDeselect;
	}

	public int getDisableSearchThreshold() {
		return disableSearchThreshold;
	}

	public ChosenOptions setDisableSearchThreshold(int disableSearchThreshold) {
		this.disableSearchThreshold = disableSearchThreshold;
		return this;
	}

	public boolean isSearchContains() {
		return searchContains;
	}

	public ChosenOptions setSearchContains(boolean searchContains) {
		this.searchContains = searchContains;
		return this;
	}

	public boolean isSingleBackstrokeDelete() {
		return singleBackstrokeDelete;
	}

	public ChosenOptions setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
		this.singleBackstrokeDelete = singleBackstrokeDelete;
		return this;
	}

	public int getMaxSelectedOptions() {
		return maxSelectedOptions;
	}

	public ChosenOptions setMaxSelectedOptions(int maxSelectedOptions) {
		this.maxSelectedOptions = maxSelectedOptions;
		return this;
	}

	public String getPlaceholderTextMultiple() {
		return placeholderTextMultiple;
	}

	public ChosenOptions setPlaceholderTextMultiple(String placeholderTextMultiple) {
		this.placeholderTextMultiple = placeholderTextMultiple;
		return this;
	}

	public String getPlaceholderText() {
		return placeholderText;
	}

	public ChosenOptions setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
		return this;
	}

	public String getPlaceholderTextSingle() {
		return placeholderTextSingle;
	}

	public ChosenOptions setPlaceholderTextSingle(String placeholderTextSingle) {
		this.placeholderTextSingle = placeholderTextSingle;
		return this;
	}


	public String getNoResultsText() {
		return noResultsText;
	}

	public ChosenOptions setNoResultsText(String noResultsText) {
		this.noResultsText = noResultsText;
		return this;
	}
}
