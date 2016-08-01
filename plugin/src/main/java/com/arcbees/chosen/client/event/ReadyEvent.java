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

public class ReadyEvent extends ChosenEvent<ReadyEvent.ReadyHandler> {
    public interface ReadyHandler extends EventHandler {
        void onReady(ReadyEvent event);
    }

    public static final Type<ReadyHandler> TYPE = new Type<ReadyHandler>();

    public ReadyEvent(ChosenImpl chosen) {
        super(chosen);
    }

    public static Type<ReadyHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ReadyHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ReadyHandler handler) {
        handler.onReady(this);
    }

}
