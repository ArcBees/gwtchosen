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

public class ChosenChangeEvent extends ChosenEvent<ChosenChangeEvent.ChosenChangeHandler> {

    public interface ChosenChangeHandler extends EventHandler {
        void onChange(ChosenChangeEvent event);
    }

    public static Type<ChosenChangeHandler> TYPE = new Type<ChosenChangeHandler>();

    private final int index;
    private final boolean selection;
    private final String value;

    public ChosenChangeEvent(String value, int index, ChosenImpl chosen) {
        this(value, index, true, chosen);
    }

    public ChosenChangeEvent(String value, int index, boolean selection, ChosenImpl chosen) {
        super(chosen);

        this.value = value;
        this.index = index;
        this.selection = selection;
    }

    public static Type<ChosenChangeHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ChosenChangeHandler> getAssociatedType() {
        return TYPE;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelection() {
        return selection;
    }

    @Override
    protected void dispatch(ChosenChangeHandler handler) {
        handler.onChange(this);
    }
}
