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

public interface ResultsFilter {
    /**
     * This method is called when you user is trying to filter the list of items by entering characters in the
     * input text element.
     * <p/>
     * This method is also called when we are revealing the options panel. In this case, the boolean parameter
     * {@code isShowing} is set to true.
     *
     * @param searchText the text entered by the user
     * @param chosen     the chosen component
     * @param isShowing  indicates of this method is called because we are revealing the options.
     */
    void filter(String searchText, ChosenImpl chosen, boolean isShowing);
}
