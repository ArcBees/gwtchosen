package com.arcbees.chosen.integrationtest.client.testcases;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.RootPanel;

public class HideCurrentValue extends TestCase {
    public static final Renderer<CarBrand> RENDERER = new DefaultCarRenderer();

    @Override
    public void run() {
        ChosenOptions chosenOptions = new ChosenOptions();
        chosenOptions.setHideCurrentValue(true);
        ChosenValueListBox<CarBrand> listBox = new ChosenValueListBox<CarBrand>(RENDERER, chosenOptions);

        listBox.setAcceptableValues(Lists.newArrayList(CarBrand.values()));

        RootPanel.get().add(listBox);
    }
}
