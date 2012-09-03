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
package com.watopi.chosen.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface ChozenCss extends CssResource {

  @ClassName("active-result")
  public String activeResult();

  @ClassName("chzn-choices")
  public String chznChoices();

  @ClassName("chzn-container")
  public String chznContainer();

  @ClassName("chzn-container-active")
  public String chznContainerActive();

  @ClassName("chzn-container-multi")
  public String chznContainerMulti();

  @ClassName("chzn-container-single")
  public String chznContainerSingle();

  @ClassName("chzn-container-single-nosearch")
  public String chznContainerSingleNoSearch();

  @ClassName("chzn-default")
  public String chznDefault();

  @ClassName("chzn-disabled")
  public String chznDisabled();

  @ClassName("chzn-done")
  public String chznDone();

  @ClassName("chzn-drop")
  public String chznDrop();

  @ClassName("chzn-results")
  public String chznResults();

  @ClassName("chzn-results-scroll")
  public String chznResultsScroll();

  @ClassName("chzn-results-scroll-down")
  public String chznResultsScrollDown();

  @ClassName("chzn-results-scroll-up")
  public String chznResultsScrollUp();

  @ClassName("chzn-rtl")
  public String chznRtl();

  @ClassName("chzn-search")
  public String chznSearch();

  @ClassName("chzn-single")
  public String chznSingle();

  @ClassName("chzn-single-with-drop")
  public String chznSingleWithDrop();

  @ClassName("default")
  public String defaultClass();

  @ClassName("group-option")
  public String groupOption();

  @ClassName("group-result")
  public String groupResult();

  @ClassName("highlighted")
  public String highlighted();

  @ClassName("no-results")
  public String noResults();

  @ClassName("result-selected")
  public String resultSelected();

  @ClassName("search-choice")
  public String searchChoice();

  @ClassName("search-choice-close")
  public String searchChoiceClose();

  @ClassName("search-choice-focus")
  public String searchChoiceFocus();

  @ClassName("search-field")
  public String searchField();
}
