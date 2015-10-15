package com.arcbees.chosen.integrationtest.client.testcases;

import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.arcbees.chosen.integrationtest.client.TestCase;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ChosenListBoxMultipleSelectAddItems extends TestCase {
    public static final String BUTTON_ID = "ChosenListBoxMultipleSelectAddItems-button";
    public static final String SELECTED_VALUE = "Six";

    @Override
    public void run() {
        final ChosenListBox listBox = new ChosenListBox(true);
        listBox.setWidth("200px");
        listBox.addItem("One");
        listBox.addItem("Two");
        listBox.addItem("Three");

        Button button = new Button("Add items and select Six");
        button.getElement().setId(BUTTON_ID);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                listBox.addItem("Four");
                listBox.addItem("Five");
                listBox.addItem("Six");
                listBox.update();
                listBox.setSelectedValue(SELECTED_VALUE);
            }
        });

        RootPanel.get().add(listBox);
        RootPanel.get().add(button);
    }
}
