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

import java.util.List;

import javax.annotation.Nullable;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.MultipleChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.Car;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Style;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;

public class MultiValueListBoxWithCustomKeyProvider extends TestCase {

    public static class CarKeyProvider implements ProvidesKey<Car> {
        @Override
        public Object getKey(Car item) {
            return item.getId();
        }
    }

    private final ChosenOptions options;
    private MultipleChosenValueListBox<Car> listBox;

    public MultiValueListBoxWithCustomKeyProvider() {
        this(new ChosenOptions());
    }

    protected MultiValueListBoxWithCustomKeyProvider(ChosenOptions options) {
        this.options = options;
    }

    @Override
    public void run() {
        listBox = new MultipleChosenValueListBox<Car>(new AbstractRenderer<Car>() {
            @Override
            public String render(Car object) {
                return object.getName().name();
            }
        }, new CarKeyProvider(),options);

        listBox.getElement().getStyle().setWidth(500, Style.Unit.PX);
        List<Car> cars = getAvailableCars();
        listBox.setAcceptableValues(cars);
        List<Car> selectedCars = Lists.newArrayList(cars.get(5),cars.get(6));
        RootPanel.get().add(listBox);
        listBox.setValue(selectedCars);
    }

    @Nullable
    protected MultipleChosenValueListBox<Car> getListBox() {
        return listBox;
    }

    protected List<Car> getAvailableCars() {
        List<Car> cars = Lists.newArrayList();
        CarBrand[] carBrands = CarBrand.values();
        for (int i = 0; i < carBrands.length; i++) {
            cars.add(new Car(i, CarBrand.values()[i]));
        }
        return cars;
    }
}
