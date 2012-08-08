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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

import static com.watopi.chosen.client.Chosen.Chosen;

import com.watopi.chosen.client.ChosenImpl;
import com.watopi.chosen.client.ChosenOptions;



/**
 * Example code BasePlugin plugin for GwtQuery
 */
public class ChosenSample implements EntryPoint {

  public void onModuleLoad() {
	  
	  GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
		
		public void onUncaughtException(Throwable e) {
			e.printStackTrace();
			ChosenImpl.log("An error occured : "+e);
			
		}
	});
    
	  ChosenOptions option = new ChosenOptions();
	  option.setAllowSingleDeselect(true);
	  option.setMaxSelectedOptions(5);
	  option.setNoResultsText("Oops no results here");
	  option.setPlaceholderText("Place holder for single select");
	  option.setPlaceholderTextMultiple("Place holder for multiple select");
    $(".chzn-select, .enhance").as(Chosen).chosen(option);
    //$(".chzn-select-deselect").chosen({allow_single_deselect:true});
  
    
  }
  
}
