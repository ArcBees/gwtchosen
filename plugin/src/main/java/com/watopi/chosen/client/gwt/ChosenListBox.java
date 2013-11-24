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
package com.watopi.chosen.client.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.LegacyHandlerWrapper;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.watopi.chosen.client.ChosenImpl;
import com.watopi.chosen.client.ChosenOptions;
import com.watopi.chosen.client.event.ChosenChangeEvent;
import com.watopi.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.watopi.chosen.client.event.HasAllChosenHandlers;
import com.watopi.chosen.client.event.HidingDropDownEvent;
import com.watopi.chosen.client.event.HidingDropDownEvent.HidingDropDownHandler;
import com.watopi.chosen.client.event.MaxSelectedEvent;
import com.watopi.chosen.client.event.MaxSelectedEvent.MaxSelectedHandler;
import com.watopi.chosen.client.event.ReadyEvent;
import com.watopi.chosen.client.event.ReadyEvent.ReadyHandler;
import com.watopi.chosen.client.event.ShowingDropDownEvent;
import com.watopi.chosen.client.event.ShowingDropDownEvent.ShowingDropDownHandler;
import com.watopi.chosen.client.event.UpdatedEvent;
import com.watopi.chosen.client.event.UpdatedEvent.UpdatedHandler;
import com.watopi.chosen.client.resources.Resources;

import static com.google.gwt.query.client.GQuery.$;
import static com.watopi.chosen.client.Chosen.CHOSEN_DATA_KEY;
import static com.watopi.chosen.client.Chosen.Chosen;

public class  ChosenListBox extends ListBox implements HasAllChosenHandlers {

    /**
     * Indicates of the ChosenListBox is supported by the current browser. If
     * not (IE6/7), we fall back on normal select element.
     *
     * @return
     */
    public static boolean isSupported() {
        return com.watopi.chosen.client.Chosen.isSupported();
    }

    /**
     * Creates a ChosenListBox widget that wraps an existing &lt;select&gt;
     * element.
     * <p/>
     * This element must already be attached to the document. If the element is
     * removed from the document, you must call
     * {@link RootPanel#detachNow(Widget)}.
     *
     * @param element the element to be wrapped
     * @return list box
     */
    public static ChosenListBox wrap(Element element) {
        assert Document.get().getBody().isOrHasChild(element);

        ChosenListBox listBox = new ChosenListBox(element);

        listBox.onAttach();
        RootPanel.detachOnWindowClose(listBox);

        return listBox;
    }

    private static String OPTGROUP_TAG = "optgroup";

    private EventBus chznHandlerManager;
    private ChosenOptions options;
    private boolean visible = true;

    /**
     * Creates an empty chosen component in single selection mode.
     */
    public ChosenListBox() {
        this(false);
    }

    /**
     * Creates an empty chosen component in single selection mode.
     */
    public ChosenListBox(ChosenOptions options) {
        this(false, options);
    }

    /**
     * Creates an empty list box. The preferred way to enable multiple
     * selections is to use this constructor rather than
     * {@link #setMultipleSelect(boolean)}.
     *
     * @param isMultipleSelect specifies if multiple selection is enabled
     */
    public ChosenListBox(boolean isMultipleSelect) {
        this(isMultipleSelect, new ChosenOptions());
    }

    /**
     * Creates an empty list box. The preferred way to enable multiple
     * selections is to use this constructor rather than
     * {@link #setMultipleSelect(boolean)}.
     *
     * @param isMultipleSelect specifies if multiple selection is enabled
     */
    public ChosenListBox(boolean isMultipleSelect, ChosenOptions options) {
        super(Document.get().createSelectElement(isMultipleSelect));
        this.options = options;
        if (options.getResources() == null) {
            options.setResources(GWT.<Resources>create(Resources.class));
        }
    }

    protected ChosenListBox(Element element) {
        super(element);
    }

    /**
     * Deprecated, use {@link #addChosenChangeHandler(ChosenChangeHandler)}
     * instead
     */
    @Override
    @Deprecated
    public com.google.gwt.event.shared.HandlerRegistration addChangeHandler(
            final com.google.gwt.event.dom.client.ChangeHandler handler) {
        final HandlerRegistration registration = addChosenChangeHandler(new ChosenChangeHandler() {
            public void onChange(ChosenChangeEvent event) {
                handler.onChange(null);
            }
        });

        return new LegacyHandlerWrapper(registration);
    }

    public HandlerRegistration addChosenChangeHandler(
            ChosenChangeHandler handler) {
        return ensureChosenHandlers().addHandler(ChosenChangeEvent.getType(),
                handler);
    }

    /**
     * Adds a group at the end of the list box.
     *
     * @param label the text of the group to be added
     */
    public void addGroup(String label) {
        insertGroup(label, -1);
    }

    /**
     * Adds a group at the end of the list box.
     *
     * @param label the text of the group to be added
     * @param groupId the id for the optgroup element
     */
    public void addGroup(String label, String groupId) {
        insertGroup(label, groupId, -1);
    }

    public HandlerRegistration addHidingDropDownHandler(
            HidingDropDownHandler handler) {
        return ensureChosenHandlers().addHandler(HidingDropDownEvent.getType(),
                handler);
    }
    
    /**
     * Appends an item to the end of the list, adding the supplied class name to its class attribute. Equivalent to
     * calling {@code addStyledItem(label, value, className, 0)}.
     * 
     * @param label the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @see #addStyledItem(String, String, String, int)
     */
    public void addStyledItem(String label, String value, String className) {
        addStyledItem(label, value, className, 0);
    }

    /**
     * Appends an item to the end of the list, adding the supplied class name to its class attribute. Specifying a
     * non-zero {@code indentLevel} will pad the item from the left by a fixed distance applied {@code indentLevel}
     * times.
     * <p>
     * For example, a call:
     * <p>
     * {@code 
     * addStyledItem("My Item", "item1", "highlighted", 1);
     * }
     * <p>
     * will result in the addition to the end of the {@code <select>} element of:
     * <p>
     * {@code
     * <option value="item1" class="highlighted" style="padding-left: 15px;" >My Item</option>
     * }
     * 
     * @param label the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @param indentLevel the number of times to indent the item from the left (pass 0 for no indentation)
     */
    public void addStyledItem(String label, String value, String className, int indentLevel) {
        if (indentLevel < 0) {
            throw new IllegalArgumentException("[indentLevel] must be non-negative.");
        }
        GQuery $selectElem = $(getElement());
        OptionElement option = Document.get().createOptionElement();
        option.setValue(value);
        option.setText(label);
        if (!(className == null || className.trim().isEmpty())) {
            option.addClassName(className);
        }
        if (indentLevel > 0) {
            int leftPadding = options.getResources().css().indent() * indentLevel;
            option.setAttribute("style", "padding-left: " + leftPadding + "px;");
        }
        $selectElem.append(option);
    }

    /**
     * Adds an item to the last optgroup of the list box.
     *
     * @param item the text of the item to be added
     */
    public void addItemToGroup(String item) {
        insertItemToGroup(item, -1, -1);
    }

    /**
     * Adds an item to the an optgroup of the list box.
     *
     * @param item       the text of the item to be added
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void addItemToGroup(String item, int groupIndex) {
        insertItemToGroup(item, groupIndex, -1);
    }

    /**
     * Adds an item to the last optgroup of the list box.
     *
     * @param item the text of the item to be added
     */
    public void addItemToGroup(String item, String value) {
        insertItemToGroup(item, value, -1, -1);
    }

    /**
     * Adds an item to the an optgroup of the list box.
     *
     * @param item       the text of the item to be added
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void addItemToGroup(String item, String value, int groupIndex) {
        insertItemToGroup(item, value, groupIndex, -1);
    }
    
    /**
     * Adds an item to the group specified by its index.
     * 
     * @param label the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @param groupIndex index of the group to add the item to
     */
    public void addStyledItemToGroup(String label, String value, String className, int groupIndex) {
        addStyledItemToGroup(label, value, className, 0, groupIndex);
    }

    /**
     * @param label the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @param indentLevel the number of times to indent the item from the left (pass 0 for no indentation) 
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void addStyledItemToGroup(String label, String value, String className, int indentLevel, int groupIndex) {
        insertStyledItemToGroup(label, value, className, null /* dir */, indentLevel, groupIndex, -1);
    }

    public HandlerRegistration addMaxSelectedHandler(MaxSelectedHandler handler) {
        return ensureChosenHandlers().addHandler(MaxSelectedEvent.getType(),
                handler);
    }

    public HandlerRegistration addReadyHandler(ReadyHandler handler) {
        return ensureChosenHandlers().addHandler(ReadyEvent.getType(), handler);
    }

    public HandlerRegistration addShowingDropDownHandler(
            ShowingDropDownHandler handler) {
        return ensureChosenHandlers().addHandler(
                ShowingDropDownEvent.getType(), handler);
    }

    public HandlerRegistration addUpdatedHandler(UpdatedHandler handler) {
        return ensureChosenHandlers().addHandler(
                UpdatedEvent.getType(), handler);
    }

    @Override
    public void clear() {
        clear(true);
    }

    public void clear(boolean update) {
        $(getElement()).html("");
        if (update){
            update();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        update();
    }

    public void forceRedraw() {
        $(getElement()).as(Chosen).destroy()
                .chosen(options, ensureChosenHandlers());
    }

    public GQuery getChosenElement() {
        ChosenImpl impl = $(getElement()).data(CHOSEN_DATA_KEY,
                ChosenImpl.class);
        if (impl != null) {
            return impl.getContainer();
        }
        return $();
    }

    public int getDisableSearchThreshold() {
        return options.getDisableSearchThreshold();
    }

    public int getMaxSelectedOptions() {
        return options.getMaxSelectedOptions();
    }

    public String getNoResultsText() {
        return options.getNoResultsText();
    }

    public String getPlaceholderText() {
        return options.getPlaceholderText();
    }

    public String getPlaceholderTextMultiple() {
        return options.getPlaceholderTextMultiple();
    }

    public String getPlaceholderTextSingle() {
        return options.getPlaceholderTextSingle();
    }

    /**
     * Return the value of the first selected option if any. Returns false otherwise.
     * In case of multiple ChosenListBox, please use {@link #getValues()} instead.
     * @return
     */
    public String getValue(){
        int selectedIndex = getSelectedIndex();

        return selectedIndex != -1 ? getValue(selectedIndex) : null;
    }

    /**
     * Return the values of all selected options in an array.
     * Usefull to know which options are selected in case of multiple ChosenListBox
     * @return
     */
    public String[] getValues() {
        if (!isMultipleSelect()){
            return new String[]{getValue()};
        }

        JsArrayString values = JsArrayString.createArray().cast();
        NodeList<OptionElement> options = SelectElement.as(getElement()).getOptions();
        for (int i = 0; i < options.getLength(); i++){
            OptionElement option = options.getItem(i);
            if (option.isSelected()){
                values.push(option.getValue());
            }
        }

        String[] result = new String[values.length()];
        for (int i = 0; i < values.length(); i++){
            result[i] = values.get(i);
        }

        return result;
    }

    /**
     * Insert a group to the list box.
     *
     * @param label the text of the group to be added
     * @param index the index at which to insert it
     */
    public void insertGroup(String label, int index) {
        insertGroup(label, null, index);
    }

    /**
     * Insert a group to the list box.
     *
     * @param label the text of the group to be added
     * @param id the id of the optgroup element
     * @param index the index at which to insert it
     */
    public void insertGroup(String label, String id, int index) {
        GQuery optGroup = $("<optgroup></optgroup>").attr("label", label);
        if (id != null){
            optGroup.attr("id", id);
        }
        GQuery select = $(getElement());

        int itemCount = SelectElement.as(getElement()).getLength();

        if (index < 0 || index > itemCount) {
            select.append(optGroup);
        } else {
            GQuery before = select.children().eq(index);
            before.before(optGroup);
        }
    }

    /**
     * Adds an item to the an optgroup of the list box. If no optgroup exists,
     * the item will be add at the end ot the list box.
     *
     * @param item       the text of the item to be added
     * @param value      the value of the item to be added
     * @param itemIndex  the index inside the optgroup at which to insert the item
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void insertItemToGroup(String item, Direction dir, String value, int groupIndex, int itemIndex) {
        insertStyledItemToGroup(item, value, null /* className */, dir, 0, groupIndex, itemIndex);
    }

    /**
     * Adds an item to the an optgroup of the list box. If no optgroup exists,
     * the item will be add at the end ot the list box.
     *
     * @param item       the text of the item to be added
     * @param itemIndex  the index inside the optgroup at which to insert the item
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void insertItemToGroup(String item, int groupIndex, int itemIndex) {
        insertItemToGroup(item, null, item, groupIndex, itemIndex);

    }

    /**
     * Adds an item to the an optgroup of the list box. If no optgroup exists,
     * the item will be add at the end ot the list box.
     *
     * @param item       the text of the item to be added
     * @param value      the value of the item to be added
     * @param itemIndex  the index inside the optgroup at which to insert the item
     * @param groupIndex the index of the optGroup where the item will be inserted
     */
    public void insertItemToGroup(String item, String value, int groupIndex,
            int itemIndex) {
        insertItemToGroup(item, null, value, groupIndex, itemIndex);
    }
    
    /**
     * @param item the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @param groupIndex the index of the optgroup where the item will be inserted
     * @param itemIndex the index inside the optgroup at which to insert the item
     */
    public void insertStyledItemToGroup(String item, String value, String className, int groupIndex, int itemIndex) {
        insertStyledItemToGroup(item, value, className, 0, groupIndex, itemIndex);
    }

    /**
     * @param item the item label to display to the user
     * @param value the value of the item, meaningful in the context of an HTML form
     * @param className the class name to add to this item (pass {@code null} to add no class name)
     * @param indentLevel the number of times to indent the item from the left (pass 0 for no indentation)
     * @param groupIndex the index of the optgroup where the item will be inserted
     * @param itemIndex the index inside the optgroup at which to insert the item
     */
    public void insertStyledItemToGroup(String item, String value, String className, int indentLevel, int groupIndex,
            int itemIndex) {
        insertStyledItemToGroup(item, value, className, null /* dir */, indentLevel, groupIndex, itemIndex);
    }
    
    /**
     * Inserts an item into a group at the specified location. Additionally, the item can have an extra class name as
     * well as indent level assigned to it.
     * 
     * @param item the item label to display
     * @param value the value of the item in the HTML form context
     * @param className the class name to append to the option (pass {@code null} to append no class name)
     * @param dir allows specifying an RTL, LTR or inherited direction ({@code null} is LTR)
     * @param indentLevel the number of times to indent the item from the left (pass 0 for no indentation)
     * @param groupIndex the index of the group to insert the item into (if out of bounds, the last group will be used)
     * @param itemIndex the index of the item within a group (if out of bounds, item will be placed last in the group)
     */
    public void insertStyledItemToGroup(String item, String value, String className, Direction dir, int indentLevel,
            int groupIndex, int itemIndex) {
        if (indentLevel < 0) {
            throw new IllegalArgumentException("[indentLevel] must be non-negative.");
        }
        // -----
        GQuery optgroupList = $(OPTGROUP_TAG, getElement());

        int groupCount = optgroupList.size();

        if (groupCount == 0) {
            // simply insert the item to the listbox
            insertItem(item, dir, value, itemIndex);
            return;
        }

        if (groupIndex < 0 || groupIndex > groupCount - 1) {
            groupIndex = groupCount - 1;
        }

        GQuery optgroup = optgroupList.eq(groupIndex);

        OptionElement option = Document.get().createOptionElement();
        setOptionText(option, item, dir);
        option.setValue(value);
        if (!(className == null || className.trim().isEmpty())) {
            option.addClassName(className);
        }
        if (indentLevel > 0) {
            // Calculate total indentation, not forgetting that being in a group is adding one extra indent step
            int leftPadding = options.getResources().css().indent() * (indentLevel + 1);
            option.setAttribute("style", "padding-left: " + leftPadding + "px;");
        }

        Element optGroupElement = optgroup.get(0);
        int itemCount = optGroupElement.getChildCount();

        if (itemIndex < 0 || itemIndex > itemCount - 1) {
            optgroup.append(option);
        } else {
            GQuery before = $(optGroupElement.getChild(itemIndex));
            before.before(option);
        }
    }

    /**
     * Specify if the deselection is allowed on single selects.
     */
    public boolean isAllowSingleDeselect() {
        return options.isAllowSingleDeselect();
    }

    public boolean isSearchContains() {
        return options.isSearchContains();
    }

    public boolean isSingleBackstrokeDelete() {
        return options.isSingleBackstrokeDelete();
    }

    public void removeGroup(int index){
        $(OPTGROUP_TAG, getElement()).eq(index).remove();
        update();
    }

    /**
     * Remove the optgroup (and the children options) by id.
     * To set an id to an optgroup, use {@link #insertGroup(String, String, int)} or {@link #addGroup(String, String)}
     * @param id
     */
    public void removeGroupById(String id){
        $("#"+id, getElement()).remove();
        update();
    }

    /**
     * Remove all optgroup (and the children options) with a label matching <code>label</code> argument
     * @param label
     */
    public void removeGroupByLabel(String label){
        $(OPTGROUP_TAG + "[label='" + label + "']", getElement()).remove();
        update();
    }

    public void setAllowSingleDeselect(boolean allowSingleDeselect) {
        options.setAllowSingleDeselect(allowSingleDeselect);
    }

    public void setDisableSearchThreshold(int disableSearchThreshold) {
        options.setDisableSearchThreshold(disableSearchThreshold);
    }

    @Override
    public void setFocus(boolean focused) {
        GQuery focusElement = getFocusableElement();
        if (focused) {
            focusElement.focus();
        } else {
            focusElement.blur();
        }
    }

    public void setMaxSelectedOptions(int maxSelectedOptions) {
        options.setMaxSelectedOptions(maxSelectedOptions);
    }

    public void setNoResultsText(String noResultsText) {
        options.setNoResultsText(noResultsText);
    }

    public void setPlaceholderText(String placeholderText) {
        options.setPlaceholderText(placeholderText);
    }

    public void setPlaceholderTextMultiple(String placeholderTextMultiple) {
        options.setPlaceholderTextMultiple(placeholderTextMultiple);
    }

    public void setPlaceholderTextSingle(String placeholderTextSingle) {
        options.setPlaceholderTextSingle(placeholderTextSingle);
    }

    public void setSearchContains(boolean searchContains) {
        options.setSearchContains(searchContains);
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        update();
    }

    public void setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
        options.setSingleBackstrokeDelete(singleBackstrokeDelete);
    }

    public void setHighlightSearchTerm(boolean highlightSearchTerm) {
        options.setHighlightSearchTerm(highlightSearchTerm);
    }

    /**
     * Select all options with value present in <code>values</code> array and update the component.
     * @param values
     */
    public void setSelectedValue(String... values) {
        for (String value : values){
            Element element = $("option[value='" + value + "']", this).get(0);

            if (element != null) {
                OptionElement.as(element).setSelected(true);
            }
        }
        update();
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;

        if (isSupported()) {
            GQuery chosenElement = getChosenElement();
            if (visible) {
                chosenElement.show();
            } else {
                chosenElement.hide();
            }
        } else {
            super.setVisible(visible);
        }
    }

    /**
     * Use this method to update the chosen list box (i.e. after insertion or
     * removal of options)
     */
    public void update() {
        ensureChosenHandlers().fireEvent(new UpdatedEvent());
    }

    protected final <H extends EventHandler> HandlerRegistration addChosenHandler(
            H handler, Type<H> type) {
        return ensureChosenHandlers().addHandler(type, handler);
    }

    protected EventBus ensureChosenHandlers() {
        return chznHandlerManager == null ? chznHandlerManager = new SimpleEventBus()
                : chznHandlerManager;
    }

    protected EventBus getChosenHandlerManager() {
        return chznHandlerManager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        $(getElement()).as(Chosen).chosen(options, ensureChosenHandlers());
        setVisible(visible);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        $(getElement()).as(Chosen).destroy();
    }

    private GQuery getFocusableElement() {
        GQuery chosen = getChosenElement();
        GQuery focusableElement = chosen.children("a");
        if (focusableElement.isEmpty()) {
            focusableElement = chosen.find("input");
        }

        return focusableElement;
    }

}
