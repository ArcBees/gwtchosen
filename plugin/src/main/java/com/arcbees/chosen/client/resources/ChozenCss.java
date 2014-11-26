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
package com.arcbees.chosen.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface ChozenCss extends CssResource {

  @ClassName("active-result")
  public String activeResult();

  @ClassName("found-result")
  public String foundResult();

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

  @ClassName("icon_cross")
  public String iconCross();

  @ClassName("icon_arrow")
  public String iconArrow();

  @ClassName("icon_search")
  public String iconSearch();

  public int indent();
}
