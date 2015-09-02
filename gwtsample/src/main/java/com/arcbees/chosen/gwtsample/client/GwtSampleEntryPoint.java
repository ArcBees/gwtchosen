package com.arcbees.chosen.gwtsample.client;

import com.arcbees.chosen.gwtsample.client.chosensample.Chosensample;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtSampleEntryPoint implements EntryPoint {
  
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new Chosensample());
  }
}
