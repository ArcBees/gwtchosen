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

package com.arcbees.chosen.sample.client.resources;

import com.arcbees.chosen.client.resources.ChosenCss;
import com.arcbees.chosen.client.resources.Resources;

public interface ChosenCustomResources extends Resources {
    interface CustomChosenCss extends ChosenCss {
    }

    @Override
    @Source({"com/arcbees/gsss/mixin/client/mixins.gss",
            "com/arcbees/chosen/client/resources/icons/icons.gss",
            "css/chosenCustomColors.gss",
            "com/arcbees/chosen/client/resources/css/chosen.gss",
            "com/arcbees/chosen/client/resources/css/mobile.gss"})
    CustomChosenCss css();
}
