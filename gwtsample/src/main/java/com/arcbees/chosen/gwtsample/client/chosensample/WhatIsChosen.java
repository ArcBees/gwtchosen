package com.arcbees.chosen.gwtsample.client.chosensample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WhatIsChosen extends Composite {

    private static WhatIsChosenUiBinder uiBinder = GWT.create(WhatIsChosenUiBinder.class);

    @UiTemplate("WhatIsChosen.ui.xml")
    interface WhatIsChosenUiBinder extends UiBinder<Widget, WhatIsChosen> {
    }
}
