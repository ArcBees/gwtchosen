/**
 * Copyright 2015 ArcBees Inc.
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

package com.arcbees.chosen.client;

import com.google.gwt.dom.client.Element;

/**
 * After discussing with Julien, we agreed that this class should be removed. Instead, we're going to add 3 fields in
 * ChosenOptions:
 *
 *  1) Position (BELOW, ABOVE, AUTO)
 *  2) Element dropdownBoundaries
 *  3) Un provider de Element (type exact à déterminer) au cas ou dropdownBoundaries n'est pas dans la DOM au moment
 *  de construire les ChosenOptions
 */

public class DropdownPosition {
    public enum Position {
        BELOW, ABOVE, AUTO
    }

    private final Position position;
    private final Element boundaries;

    private DropdownPosition(Position position, Element boundaries) {
        this.position = position;
        this.boundaries = boundaries;
    }

    public static DropdownPosition above() {
        return new DropdownPosition(Position.ABOVE, null);
    }

    public static DropdownPosition below() {
        return new DropdownPosition(Position.BELOW, null);
    }

    public static DropdownPosition auto() {
        return new DropdownPosition(Position.AUTO, null);
    }

    public static DropdownPosition auto(Element boundaries) {
        return new DropdownPosition(Position.AUTO, boundaries);
    }

    public Position getPosition() {
        return position;
    }

    public Element getBoundaries() {
        return boundaries;
    }
}
