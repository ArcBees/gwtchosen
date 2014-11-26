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

package com.arcbees.chosen.client.gwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.arcbees.chosen.client.ChosenOptions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SimpleKeyProvider;

public class MultipleChosenValueListBox<T> extends BaseChosenValueListBox<T>
        implements HasValue<List<T>>, IsEditor<TakesValueEditor<List<T>>> {
    private final Renderer<T> renderer;

    private Set<T> selectedValues;
    private TakesValueEditor<List<T>> editor;

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null 
     */
    public MultipleChosenValueListBox(Renderer<T> renderer) {
        this(renderer, new ChosenOptions());
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code options} is null
     */
    public MultipleChosenValueListBox(Renderer<T> renderer, ChosenOptions options) {
        this(renderer, new SimpleKeyProvider<T>(), options);
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code keyProvider} is null
     */
    public MultipleChosenValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
        this(renderer, keyProvider, new ChosenOptions());
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code keyProvider} is null
     * @throws java.lang.NullPointerException if {@code options} is null
     */
    public MultipleChosenValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider, ChosenOptions options) {
        super(keyProvider, options);

        Preconditions.checkNotNull(renderer);

        this.renderer = renderer;
        selectedValues = new LinkedHashSet<T>();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<T>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public TakesValueEditor<List<T>> asEditor() {
        if (editor == null) {
            editor = TakesValueEditor.of(this);
        }
        return editor;
    }

    /**
     * Get the list of selected values of the component.
     */
    @Override
    public List<T> getValue() {
        return Lists.newArrayList(selectedValues);
    }

    /**
     * Set the values of the component. This method throws an IllegalStateException if one of the values isn't part
     * of the accepted values.
     * <p/>
     * See {@link #isAccepted(Object)} in order to test if a value can be accepted by the component.
     */
    @Override
    public void setValue(List<T> values) {
        setValue(values, false);
    }

    /**
     * Set the values of the component. This method throws an IllegalStateException if one of the values isn't part
     * of the accepted values.
     * <p/>
     * See {@link #isAccepted(Object)} in order to test if a value can be accepted by the component.
     * <p/>
     * If the {@code fireEvent} is set to true, a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} will be
     * fired.
     */
    public void setValue(List<T> values, boolean fireEvent) {
        setValue(values, fireEvent, true);
    }

    /**
     * Unselect a specific value. This method does nothing if the <code>value</code> is not selected.
     */
    public void unselect(T value) {
        unselect(value, false);
    }

    /**
     * Unselect a specific value. This method does nothing if the <code>value</code> is not selected.
     * <p>If the {@code fireEvent} is set to true, a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} will
     * be fired.
     */
    public void unselect(T value, boolean fireEvent) {
         boolean removed = selectedValues.remove(value);

        if (removed) {
            updateChosenListBox();

            if (fireEvent) {
                ValueChangeEvent.fire(this, getValue());
            }
        }
    }

    /**
     * Unselect all previously selected values
     *
     */
    public void unselectAll() {
        setValue(null);
    }

    @Override
    protected ChosenListBox createChosenListBox(ChosenOptions options) {
        return new ChosenListBox(true, options);
    }

    @Override
    protected void deselectValue(T value) {
        boolean removed = selectedValues.remove(value);
        Preconditions.checkState(removed, "Value was not previously selected");

        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    protected void selectValue(T value) {
        setValue(Lists.newArrayList(value), true, false);
    }

    @Override
    protected void updateChosenListBox() {
        List<String> valueIndex = new ArrayList<String>();

        for (Iterator<T> iterator =  selectedValues.iterator(); iterator.hasNext();) {
            Object key = keyProvider.getKey(iterator.next());
            Integer index = valueKeyToIndex.get(key);

            if (index == null) {
                iterator.remove();
            } else {
                valueIndex.add("" + index);
            }
        }

        getChosenListBox().unselectAll();
        getChosenListBox().setSelectedValue(valueIndex.toArray(new String[valueIndex.size()]));
    }

    @Override
    protected void addItemToChosenListBox(T value) {
        int index = valueKeyToIndex.get(value);

        getChosenListBox().addItem(renderer.render(value), "" + index);
    }

    private void setValue(List<T> values, boolean fireEvent, boolean update) {
        selectedValues.clear();

        if (values != null) {
            checkValuesAcceptability(values);

            selectedValues.addAll(values);
        }

        if (update) {
            updateChosenListBox();
        }

        if (fireEvent) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void checkValuesAcceptability(List<T> values) {
        List<T> unacceptableValues = new ArrayList<T>();

        for (T value : values) {
            if (!isAccepted(value)) {
                unacceptableValues.add(value);
            }
        }

        if (!unacceptableValues.isEmpty()) {
            throw new IllegalStateException("These following values are not accepted by the component: " +
                    unacceptableValues);
        }
    }
}
