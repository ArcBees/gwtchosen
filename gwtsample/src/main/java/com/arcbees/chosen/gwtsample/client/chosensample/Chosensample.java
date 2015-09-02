package com.arcbees.chosen.gwtsample.client.chosensample;

import com.arcbees.chosen.gwtsample.client.resources.AppResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Chosensample extends Composite {

    private static ChosensampleUiBinder uiBinder = GWT.create(ChosensampleUiBinder.class);

    @UiField(provided = true)
    final AppResources res;

    @UiTemplate("Chosensample.ui.xml")
    interface ChosensampleUiBinder extends UiBinder<Widget, Chosensample> {
    }

    public Chosensample() {
        this.res = GWT.create(AppResources.class);
        res.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }
}
