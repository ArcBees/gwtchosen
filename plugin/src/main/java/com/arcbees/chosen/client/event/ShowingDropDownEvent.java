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
package com.arcbees.chosen.client.event;

import com.arcbees.chosen.client.ChosenImpl;
import com.google.gwt.event.shared.EventHandler;

public class ShowingDropDownEvent extends ChosenEvent<ShowingDropDownEvent.ShowingDropDownHandler> {
    public interface ShowingDropDownHandler extends EventHandler {
        void onShowingDropDown(ShowingDropDownEvent event);
    }

    public static Type<ShowingDropDownHandler> TYPE = new Type<ShowingDropDownHandler>();

    public ShowingDropDownEvent(ChosenImpl chosen) {
        super(chosen);
    }

    public static Type<ShowingDropDownHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ShowingDropDownHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowingDropDownHandler handler) {
        handler.onShowingDropDown(this);
    }
}
