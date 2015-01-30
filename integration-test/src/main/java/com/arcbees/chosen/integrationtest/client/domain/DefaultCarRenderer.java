package com.arcbees.chosen.integrationtest.client.domain;

import com.google.gwt.text.shared.AbstractRenderer;

public class DefaultCarRenderer extends AbstractRenderer<CarBrand> {
    @Override
    public String render(CarBrand object) {
        if (object == null) {
            return "";
        }
        return object.name();
    }
}
