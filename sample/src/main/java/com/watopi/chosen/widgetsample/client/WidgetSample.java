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
package com.watopi.chosen.widgetsample.client;

import static com.google.gwt.query.client.GQuery.$;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import com.watopi.chosen.client.event.ChosenChangeEvent;
import com.watopi.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.watopi.chosen.client.event.HidingDropDownEvent;
import com.watopi.chosen.client.event.HidingDropDownEvent.HidingDropDownHandler;
import com.watopi.chosen.client.event.MaxSelectedEvent;
import com.watopi.chosen.client.event.MaxSelectedEvent.MaxSelectedHandler;
import com.watopi.chosen.client.event.ReadyEvent;
import com.watopi.chosen.client.event.ReadyEvent.ReadyHandler;
import com.watopi.chosen.client.event.ShowingDropDownEvent;
import com.watopi.chosen.client.event.ShowingDropDownEvent.ShowingDropDownHandler;
import com.watopi.chosen.client.gwt.ChosenListBox;

public class WidgetSample implements EntryPoint {

  private static final class MyEventHandlers implements ChosenChangeHandler, HidingDropDownHandler,
      ShowingDropDownHandler, MaxSelectedHandler, ReadyHandler {

    private String elementId;

    public MyEventHandlers(String id) {
      this.elementId = id;

    }

    private void log(String eventName, String additional) {
      $("#log").append(
          "<span class=\"log-line\">" + eventName + " fired by <em>" + elementId + "</em> "
              + additional + "</span>").scrollTop($("#log").get(0).getScrollHeight());
    }

    public void onReady(ReadyEvent event) {
      log("ReadyEvent", "");

    }

    public void onMaxSelected(MaxSelectedEvent event) {
      log("MaxSelectedEvent", "");
    }

    public void onShowingDropDown(ShowingDropDownEvent event) {
      log("ShowingDropDownEvent", "");

    }

    public void onHidingDropdown(HidingDropDownEvent event) {
      log("HidingDropDownEvent", "");

    }

    public void onChange(ChosenChangeEvent event) {
      String additional = (event.isSelection() ? ": selection of " : ": deselection of ") + event.getValue();
      log("ChangeEvent on",  additional);

    }

  }
  
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
  
  private static String[] teamsGroup = new String[] {
	  "NFC EAST", "NFC NORTH", "NFC SOUTH", "NFC WEST","AFC EAST", "AFC NORTH", "AFC SOUTH", "AFC WEST"
  };
  
  private static String[] teams = new String[] {
      "Dallas Cowboys", "New York Giants", "Philadelphia Eagles", "Washington Redskins",
      "Chicago Bears", "Detroit Lions", "Green Bay Packers", "Minnesota Vikings",
      "Atlanta Falcons", "Carolina Panthers", "New Orleans Saints", "Tampa Bay Buccaneers",
      "Arizona Cardinals", "St. Louis Rams", "San Francisco 49ers", "Seattle Seahawks",
      "Buffalo Bills", "Miami Dolphins", "New England Patriots", "New York Jets",
      "Baltimore Ravens", "Cincinnati Bengals", "Cleveland Browns", "Pittsburgh Steelers",
      "Houston Texans", "Indianapolis Colts", "Jacksonville Jaguars", "Tennessee Titans",
      "Denver Broncos", "Kansas City Chiefs", "Oakland Raiders", "San Diego Chargers"};

  @UiTemplate("View.ui.xml")
  interface MyUiBinder extends UiBinder<Widget, WidgetSample> {
  }

  @UiField
  ChosenListBox countriesChosen;

  @UiField(provided = true)
  ChosenListBox teamChosen;

  public void onModuleLoad() {
    
    if (!ChosenListBox.isSupported()){
      $("#browserWarning").show();
    }
    
    teamChosen = new ChosenListBox(true);
    

    Widget w = uiBinder.createAndBindUi(this);
    init();
    RootPanel.get("view").add(w);
  }

  private void bind() {
    MyEventHandlers teamEventHandler = new MyEventHandlers("multiple ChosenListBox");
    teamChosen.addChosenChangeHandler(teamEventHandler);
    teamChosen.addHidingDropDownHandler(teamEventHandler);
    teamChosen.addMaxSelectedHandler(teamEventHandler);
    teamChosen.addShowingDropDownHandler(teamEventHandler);
    teamChosen.addReadyHandler(teamEventHandler);

    MyEventHandlers countryEventHandler = new MyEventHandlers("single ChosenListBox");
    countriesChosen.addChosenChangeHandler(countryEventHandler);
    countriesChosen.addHidingDropDownHandler(countryEventHandler);
    countriesChosen.addMaxSelectedHandler(countryEventHandler);
    countriesChosen.addShowingDropDownHandler(countryEventHandler);
    countriesChosen.addReadyHandler(countryEventHandler);

    $("#clearLogButton").click(new Function() {
      @Override
      public void f() {
        $("#log").empty();
      }
    });
  }

  private void init() {

	teamChosen.addGroup(teamsGroup[0]);
	  
    // init options for teamchosen
	int i = 0;
    for (String team : teams) {
      if (i % 4 == 0){
        teamChosen.addGroup(teamsGroup[i/4]);
      }
      teamChosen.addItemToGroup(team);
    }

    // init default place holder text
    teamChosen.setPlaceholderText("Choose your favourite teams...");

    teamChosen.setWidth("300px");

    bind();
  }

}
