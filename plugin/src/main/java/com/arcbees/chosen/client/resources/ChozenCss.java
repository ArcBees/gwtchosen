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

package com.arcbees.chosen.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface ChozenCss extends CssResource {

    @ClassName("active-result")
    String activeResult();

    @ClassName("chzn-choices")
    String chznChoices();

    @ClassName("chzn-container")
    String chznContainer();

    @ClassName("chzn-container-active")
    String chznContainerActive();

    @ClassName("chzn-container-multi")
    String chznContainerMulti();

    @ClassName("chzn-container-single")
    String chznContainerSingle();

    @ClassName("chzn-container-single-nosearch")
    String chznContainerSingleNoSearch();

    @ClassName("chzn-default")
    String chznDefault();

    @ClassName("chzn-disabled")
    String chznDisabled();

    @ClassName("chzn-done")
    String chznDone();

    @ClassName("chzn-drop")
    String chznDrop();

    @ClassName("chzn-mobile-container")
    String chznMobileContainer();

    @ClassName("chzn-results")
    String chznResults();

    @ClassName("chzn-rtl")
    String chznRtl();

    @ClassName("chzn-search")
    String chznSearch();

    @ClassName("chzn-single")
    String chznSingle();

    @ClassName("chzn-single-with-drop")
    String chznSingleWithDrop();

    @ClassName("default")
    String defaultClass();

    @ClassName("found-result")
    String foundResult();

    @ClassName("group-option")
    String groupOption();

    @ClassName("group-result")
    String groupResult();

    @ClassName("highlighted")
    String highlighted();

    @ClassName("icon_arrow")
    String iconArrow();

    @ClassName("icon_cross")
    String iconCross();

    @ClassName("icon_search")
    String iconSearch();

    int indent();

    @ClassName("no-results")
    String noResults();

    @ClassName("result-above")
    String resultAbove();

    @ClassName("result-selected")
    String resultSelected();

    @ClassName("search-choice")
    String searchChoice();

    @ClassName("search-choice-close")
    String searchChoiceClose();

    @ClassName("search-choice-focus")
    String searchChoiceFocus();

    @ClassName("search-field")
    String searchField();

    @ClassName("chzn-results-holder")
    String chznResultsHolder();
}
