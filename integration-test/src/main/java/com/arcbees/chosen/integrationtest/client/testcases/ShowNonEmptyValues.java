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

package com.arcbees.chosen.integrationtest.client.testcases;

import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;

public class ShowNonEmptyValues extends SimpleValueListBox {
    public static final Renderer<CarBrand> RENDERER = new AbstractRenderer<CarBrand>() {
        @Override
        public String render(CarBrand object) {
            if (object == null) {
                return "Placeholder for null";
            }
            return object.name();
        }
    };

    public ShowNonEmptyValues() {
        super(true /* addNullValue */);
    }

    @Override
    public void run() {
        super.run();

        getListBox().setValue(null);
    }

    @Override
    protected Renderer<CarBrand> getRenderer() {
        return RENDERER;
    }
}
