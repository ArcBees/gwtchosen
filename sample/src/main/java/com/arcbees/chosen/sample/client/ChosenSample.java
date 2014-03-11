/**
 * Copyright 2014 ArcBees Inc.
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
package com.arcbees.chosen.sample.client;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.RootPanel;

import static com.arcbees.chosen.client.Chosen.Chosen;
import static com.google.gwt.query.client.GQuery.$;

public class ChosenSample implements EntryPoint {

    public void onModuleLoad() {

        if (!com.arcbees.chosen.client.Chosen.isSupported()){
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

        final ChosenListBox hcs = new ChosenListBox();
        hcs.setWidth( "350px" );
        hcs.setPlaceholderText( "Navigate to..." );
        hcs.setTabIndex( 9 );
        hcs.addItem( "" );
        hcs.addStyledItem( "Home", "home", null );
        hcs.addGroup( "ABOUT US" );
        hcs.addStyledItemToGroup( "Press Releases", "press", null, 0 );
        hcs.addStyledItemToGroup( "Contact Us", "about", null, 0 );
        hcs.addGroup( "PRODUCTS" );
        hcs.addStyledItemToGroup( "Tera-Magic", "tm", null, 0, 1 );
        hcs.addStyledItemToGroup( "Tera-Magic Pro", "tmpro", null, 1, 1 );
        // Will be inserted before "Tera-Magic Pro" and custom-styled
        hcs.insertStyledItemToGroup( "Tera-Magic Standard", "tmstd", "youAreHere", 1, 1, 1 );
        RootPanel.get( "hierChozenSingle" ).add( hcs );
    }

}
