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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface Resources extends ClientBundle {

    @Source({"com/arcbees/gsss/mixin/client/mixins.gss", "colors.gss", "icons/icons.gss", "chozen.gss"})
    ChozenCss css();

    @Source("icons/icons.ttf")
    DataResource iconsTtf();

    @Source("icons/icons.eot")
    DataResource iconsEot();

    @Source("icons/icons.svg")
    DataResource iconsSvg();

    @Source("icons/icons.woff")
    DataResource iconsWoff();

}
