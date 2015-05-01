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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Chosen for GwtQuery.
 */
public class Chosen extends GQuery {
    // A shortcut to the class
    public static final Class<Chosen> Chosen = GQuery.registerPlugin(Chosen.class,
            new Plugin<Chosen>() {
                public Chosen init(GQuery gq) {
                    return new Chosen(gq);
                }
            });

    public static String CHOSEN_DATA_KEY = "chosen";

    // Initialization
    public Chosen(GQuery gq) {
        super(gq);
    }

    /**
     * Indicate if the current browser is supported by the plugin or not.
     *
     * @return
     */
    public static boolean isSupported() {
        return GWT.<ChosenImpl>create(ChosenImpl.class).isSupported();
    }

    public Chosen chosen() {
        return chosen(new ChosenOptions(), null);
    }

    public Chosen chosen(ChosenOptions options) {
        return chosen(options, null);
    }

    public Chosen chosen(final ChosenOptions options, final EventBus eventBus) {

        for (Element e : elements()) {

            if ("select".equalsIgnoreCase(e.getTagName()) && !$(e).hasClass("chzn-done")) {

                ChosenImpl impl = GWT.create(ChosenImpl.class);
                impl.init(SelectElement.as(e), options, eventBus);
                $(e).data(CHOSEN_DATA_KEY, impl);
            }
        }
        return this;
    }

    public Chosen chosen(EventBus eventBus) {
        return chosen(new ChosenOptions(), eventBus);
    }

    public Chosen destroy() {

        for (Element e : elements()) {

            ChosenImpl impl = $(e).data(CHOSEN_DATA_KEY, ChosenImpl.class);

            if (impl != null) {
                impl.release();
                $(e).removeData(CHOSEN_DATA_KEY);
            }
        }
        return this;
    }

    public ChosenOptions options() {
        if (isEmpty()) {
            return null;
        }

        ChosenImpl impl = data(CHOSEN_DATA_KEY, ChosenImpl.class);

        return impl != null ? impl.getOptions() : null;
    }

    public Chosen update() {
        for (Element e : elements()) {
            ChosenImpl impl = $(e).data(CHOSEN_DATA_KEY, ChosenImpl.class);

            if (impl != null) {
                impl.update();
            }
        }

        return this;
    }

}
