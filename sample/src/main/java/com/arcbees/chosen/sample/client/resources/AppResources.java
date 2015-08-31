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

import com.arcbees.gsss.grid.client.GridResources;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppResources extends ClientBundle {
    interface Normalize extends CssResource {
    }

    interface Style extends CssResource {
        // CHECKSTYLE_OFF
        String container();

        String section__title();

        String banner();

        String section();

        String table();

        String header();

        String tableholder();

        String breakable();
        
        String log();
        
        String log_line();
        
        String clearLogButton();
    }

    @Source("css/normalize.gss")
    Normalize normalize();

    @Source({"com/arcbees/chosen/sample/client/resources/theme/theme.gss",
            "css/style.gss"})
    Style style();

    @Source({"com/arcbees/chosen/sample/client/resources/css/gridSettings.gss",
            "com/arcbees/gsss/grid/client/grid.gss"})
    GridResources.Grid grid();
}
