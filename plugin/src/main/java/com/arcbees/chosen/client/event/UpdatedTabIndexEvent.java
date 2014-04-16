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
package com.arcbees.chosen.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UpdatedTabIndexEvent extends GwtEvent<UpdatedTabIndexEvent.UpdatedTabIndexHandler> {
    public interface UpdatedTabIndexHandler extends EventHandler {
        void onUpdated(UpdatedTabIndexEvent event);
    }

    public static Type<UpdatedTabIndexHandler> TYPE = new Type<UpdatedTabIndexHandler>();

    public static Type<UpdatedTabIndexHandler> getType() {
        return TYPE;
    }

    public UpdatedTabIndexEvent() {
    }

    @Override
    public Type<UpdatedTabIndexHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdatedTabIndexHandler handler) {
        handler.onUpdated(this);
    }

}
