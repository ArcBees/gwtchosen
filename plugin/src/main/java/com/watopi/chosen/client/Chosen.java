/*
 * Copyright (C) 2012 Julien Dramaix
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.watopi.chosen.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Chosen for GwtQuery
 */
public class Chosen extends GQuery {

    /**
     * Indicate if the current browser is supported by the plugin or not.
     *
     * @return
     */
    public static boolean isSupported() {
        return GWT.<ChosenImpl>create(ChosenImpl.class).isSupported();
    }

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
        return chosen(null, eventBus);

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
