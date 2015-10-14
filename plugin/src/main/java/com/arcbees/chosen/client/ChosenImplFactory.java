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

import com.google.gwt.dom.client.SelectElement;

public class ChosenImplFactory {
    // TODO: in GWT 3.0, this factory could use a system property in order to determine which version (mobile/desktop)
    // is compiled
    ChosenImpl createChosenImpl(SelectElement selectElement, ChosenOptions options) {
        boolean isMultiple = selectElement.isMultiple();
        boolean isMobile = isMobileDevice(options.getMobileViewportMaxWidth());

        if (isMobile) {
            return isMultiple ? new MobileMultipleChosenImpl() : new MobileSingleChosenImpl();
        }

        return isMultiple ? new DesktopMultipleChosenImpl() : new DesktopSingleChosenImpl();
    }

    private native boolean isMobileDevice(int maxWidth) /*-{
        var media = $wnd.matchMedia && $wnd.matchMedia("(max-width: " + maxWidth + "px)");
        return media && media.matches;
    }-*/;
}
