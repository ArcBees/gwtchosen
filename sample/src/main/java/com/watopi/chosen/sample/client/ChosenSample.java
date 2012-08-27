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
package com.watopi.chosen.sample.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.RootPanel;

import static com.watopi.chosen.client.Chosen.Chosen;

import com.watopi.chosen.client.ChosenOptions;
import com.watopi.chosen.client.gwt.ChosenListBox;

public class ChosenSample implements EntryPoint {

  public void onModuleLoad() {
    
    if (!com.watopi.chosen.client.Chosen.isSupported()){
      $("#browserWarning").show();
    }

    $(".chzn-select, .enhance").as(Chosen).chosen();
    
    $("#allowSingleDeselect").as(Chosen).chosen(new ChosenOptions().setAllowSingleDeselect(true));
    
    $("#disableSearchThreshold").as(Chosen).chosen(
        new ChosenOptions().setDisableSearchThreshold(10));
    
    $("#searchContains").as(Chosen).chosen(
        new ChosenOptions().setSearchContains(true));
    
    $("#singleBackstrokeDelete").as(Chosen).chosen(
        new ChosenOptions().setSingleBackstrokeDelete(true));
    
    $("#maxSelectedOptions").as(Chosen).chosen(
        new ChosenOptions().setMaxSelectedOptions(5));
    
    $("#noResultsText").as(Chosen).chosen(
        new ChosenOptions().setNoResultsText("Ooops, nothing was found:"));
    
    final ChosenListBox chzn = new ChosenListBox();
    chzn.addItem("item 1");
    chzn.setWidth("250px");
    
    RootPanel.get("updateChozen").add(chzn);
    
    $("#updateButton").click(new Function(){
      int i = 2;
      
      @Override
      public void f() {
        for (int j = 0; j < 100; j++){
          chzn.addItem("item "+i);
          i++;
        }
        
        chzn.update();
      }
    });
    
    
  }

}
