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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;

public class SelectParser {
    public static class GroupItem extends SelectItem {

        private int children;
        private String label;

        public int getChildren() {
            return children;
        }

        public void setChildren(int children) {
            this.children = children;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public boolean isGroup() {
            return true;
        }
    }

    public static class OptionItem extends SelectItem {
        private int arrayIndex;
        private String classes;
        private boolean disabled;
        private boolean empty;
        private int groupArrayIndex;
        private String html;
        private int optionsIndex;
        private boolean selected;
        private String style = "";
        private String text;
        private String value;

        public int getArrayIndex() {
            return arrayIndex;
        }

        public void setArrayIndex(int arrayIndex) {
            this.arrayIndex = arrayIndex;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public int getGroupArrayIndex() {
            return groupArrayIndex;
        }

        public void setGroupArrayIndex(int groupArrayIndex) {
            this.groupArrayIndex = groupArrayIndex;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public int getOptionsIndex() {
            return optionsIndex;
        }

        public void setOptionsIndex(int optionsIndex) {
            this.optionsIndex = optionsIndex;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        @Override
        public boolean isGroup() {
            return false;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public abstract static class SelectItem {
        protected int arrayIndex;
        protected boolean disabled;
        protected String domId;

        public int getArrayIndex() {
            return arrayIndex;
        }

        public String getDomId() {
            return domId;
        }

        public void setDomId(String domId) {
            this.domId = domId;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public boolean isEmpty() {
            return false;
        }

        public abstract boolean isGroup();
    }

    private final List<SelectItem> parsed;

    private int optionsIndex;

    public SelectParser() {
        optionsIndex = 0;
        parsed = new ArrayList<SelectItem>();
    }

    public List<SelectItem> parse(SelectElement select) {

        NodeList<Node> children = select.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.getItem(i);
            addNode(n);
        }

        return parsed;
    }

    private void addGroup(OptGroupElement group) {
        int position = parsed.size();

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
        item.arrayIndex = parsed.size();
        item.optionsIndex = optionsIndex;

        if (optionText != null && optionText.length() > 0) {

            if (groupPosition != -1) {
                ((GroupItem) parsed.get(groupPosition)).children++;
            }

            item.value = option.getValue();

            item.text = option.getLabel();
            if (isNullOrEmpty(item.text)) {
                item.text = option.getText();
            }

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

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
