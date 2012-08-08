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

	private Boolean allowSingleDeselect;
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
		allowSingleDeselect = null;
		disableSearchThreshold = 0;
		searchContains = false;
		singleBackstrokeDelete = false;
		maxSelectedOptions = -1;

	}

	public void setAllowSingleDeselect(Boolean allowSingleDeselect) {
		this.allowSingleDeselect = allowSingleDeselect;
	}

	/**
	 * Specify if the deselection is allowed on single selects.
	 * 
	 */
	public Boolean isAllowSingleDeselect() {
		return allowSingleDeselect;
	}

	public int getDisableSearchThreshold() {
		return disableSearchThreshold;
	}

	public void setDisableSearchThreshold(int disableSearchThreshold) {
		this.disableSearchThreshold = disableSearchThreshold;
	}

	public boolean isSearchContains() {
		return searchContains;
	}

	public void setSearchContains(boolean searchContains) {
		this.searchContains = searchContains;
	}

	public boolean isSingleBackstrokeDelete() {
		return singleBackstrokeDelete;
	}

	public void setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
		this.singleBackstrokeDelete = singleBackstrokeDelete;
	}

	public int getMaxSelectedOptions() {
		return maxSelectedOptions;
	}

	public void setMaxSelectedOptions(int maxSelectedOptions) {
		this.maxSelectedOptions = maxSelectedOptions;
	}

	public String getPlaceholderTextMultiple() {
		return placeholderTextMultiple;
	}

	public void setPlaceholderTextMultiple(String placeholderTextMultiple) {
		this.placeholderTextMultiple = placeholderTextMultiple;
	}

	public String getPlaceholderText() {
		return placeholderText;
	}

	public void setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
	}

	public String getPlaceholderTextSingle() {
		return placeholderTextSingle;
	}

	public void setPlaceholderTextSingle(String placeholderTextSingle) {
		this.placeholderTextSingle = placeholderTextSingle;
	}

	public Boolean getAllowSingleDeselect() {
		return allowSingleDeselect;
	}

	public String getNoResultsText() {
		return noResultsText;
	}

	public void setNoResultsText(String noResultsText) {
		this.noResultsText = noResultsText;
	}
}
