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
package com.watopi.chosen.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.query.client.js.JsObjectArray;

public class SelectParser {
    protected class GroupItem extends SelectItem {

        private int children = 0;
        private String label;

        public int getChildren() {
            return children;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public boolean isGroup() {
            return true;
        }

        public void setChildren(int children) {
            this.children = children;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    protected class OptionItem extends SelectItem {
        private int arrayIndex;
        private String classes;
        private boolean disabled;
        private boolean empty;
        private int groupArrayIndex;
        private String html;
        private int optionsIndex;
        private boolean selected;
        private String style;
        private String text;
        private String value;

        public int getArrayIndex() {
            return arrayIndex;
        }

        public String getClasses() {
            return classes;
        }

        public int getGroupArrayIndex() {
            return groupArrayIndex;
        }

        public String getHtml() {
            return html;
        }

        public int getOptionsIndex() {
            return optionsIndex;
        }

        public String getStyle() {
            return style;
        }

        public String getText() {
            return text;
        }

        public String getValue() {
            return value;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public boolean isEmpty() {
            return empty;
        }

        @Override
        public boolean isGroup() {
            return false;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setArrayIndex(int arrayIndex) {
            this.arrayIndex = arrayIndex;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public void setGroupArrayIndex(int groupArrayIndex) {
            this.groupArrayIndex = groupArrayIndex;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public void setOptionsIndex(int optionsIndex) {
            this.optionsIndex = optionsIndex;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    protected abstract class SelectItem {
        protected int arrayIndex;
        protected boolean disabled;
        protected String domId;

        public int getArrayIndex() {
            return arrayIndex;
        }

        public String getDomId() {
            return domId;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public boolean isEmpty() {
            return false;
        }

        public abstract boolean isGroup();

        public void setDomId(String domId) {
            this.domId = domId;
        }

    }

    private int optionsIndex;
    private JsObjectArray<SelectItem> parsed;

    public SelectParser() {
        optionsIndex = 0;
        parsed = JsObjectArray.create();

    }

    public JsObjectArray<SelectItem> parse(SelectElement select) {

        NodeList<Node> children = select.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.getItem(i);
            addNode(n);
        }

        return parsed;
    }

    private void addGroup(OptGroupElement group) {
        int position = parsed.length();

        GroupItem item = new GroupItem();
        item.arrayIndex = position;
        item.label = group.getLabel();
        item.children = 0;
        item.disabled = group.isDisabled();

        parsed.add(item);

        NodeList<Node> children = group.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.getItem(i);
            if ("OPTION".equalsIgnoreCase(n.getNodeName())) {
                addOption(OptionElement.as((Element) n), position, group.isDisabled());
            }
        }

    }

    private void addNode(Node child) {
        if (!Element.is(child)) {
            return;
        }

        Element e = Element.as(child);

        if ("OPTGROUP".equalsIgnoreCase(e.getNodeName())) {
            addGroup(OptGroupElement.as(e));
        } else if ("OPTION".equalsIgnoreCase(e.getNodeName())) {
            addOption(OptionElement.as(e), -1, false);
        }
    }

    private void addOption(OptionElement option, int groupPosition, boolean groupDisabled) {
        String optionText = option.getText();

        OptionItem item = new OptionItem();
        item.arrayIndex = parsed.length();
        item.optionsIndex = optionsIndex;

        if (optionText != null && optionText.length() > 0) {

            if (groupPosition != -1) {
                ((GroupItem) parsed.get(groupPosition)).children++;
            }

            item.value = option.getValue();
            item.text = option.getText();
            item.html = option.getInnerHTML();
            item.selected = option.isSelected();
            item.disabled = groupDisabled ? groupDisabled : option.isDisabled();
            item.groupArrayIndex = groupPosition;
            item.classes = option.getClassName();
            item.style = getCssText(option.getStyle());
            item.empty = false;

        } else {
            item.empty = true;
            item.groupArrayIndex = -1;

        }

        parsed.add(item);
        optionsIndex++;
    }

    private native String getCssText(Style s)/*-{
        return s.cssText;
    }-*/;
}
