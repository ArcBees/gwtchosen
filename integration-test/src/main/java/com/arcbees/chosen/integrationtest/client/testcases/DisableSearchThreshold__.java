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

package com.arcbees.chosen.integrationtest.client.testcases;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;

public class DisableSearchThreshold extends SimpleValueListBox {
    public DisableSearchThreshold() {
        super(createChosenOption(), false);
    }

    private static ChosenOptions createChosenOption() {
        ChosenOptions chosenOptions = new ChosenOptions();
        chosenOptions.setDisableSearchThreshold(CarBrand.values().length + 1);
        return chosenOptions;
    }
}
