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
package com.watopi.chosen.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.watopi.chosen.client.ChosenImpl;

public class ChosenChangeEvent extends ChosenEvent<ChosenChangeEvent.ChosenChangeHandler> {

    public interface ChosenChangeHandler extends EventHandler {
        void onChange(ChosenChangeEvent event);
    }

    public static Type<ChosenChangeHandler> getType() {
        return TYPE;
    }

    public static Type<ChosenChangeHandler> TYPE = new Type<ChosenChangeHandler>();

    private final int index;
    private final boolean selection;
    private final String value;

    public ChosenChangeEvent(String value, int index, ChosenImpl chosen) {
        this(value, index, true, chosen);
    }

    public ChosenChangeEvent(String value, int index, boolean selection, ChosenImpl chosen) {
        super(chosen);

        this.value = value;
        this.index = index;
        this.selection = selection;
    }

    @Override
    public Type<ChosenChangeHandler> getAssociatedType() {
        return TYPE;
    }

    public String getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSelection() {
        return selection;
    }

    @Override
    protected void dispatch(ChosenChangeHandler handler) {
        handler.onChange(this);
    }

}
