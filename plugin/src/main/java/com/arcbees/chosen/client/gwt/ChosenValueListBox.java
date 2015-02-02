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

package com.arcbees.chosen.client.gwt;

import java.util.Objects;

import com.arcbees.chosen.client.ChosenOptions;
import com.google.common.base.Preconditions;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SimpleKeyProvider;

public class ChosenValueListBox<T> extends BaseChosenValueListBox<T> implements IsEditor<TakesValueEditor<T>>,
        HasValue<T> {
    private final Renderer<T> renderer;

    private TakesValueEditor<T> editor;
    private T value;

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     */
    public ChosenValueListBox(Renderer<T> renderer) {
        this(renderer, new ChosenOptions());
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code options} is null
     */
    public ChosenValueListBox(Renderer<T> renderer, ChosenOptions options) {
        this(renderer, new SimpleKeyProvider<T>(), options);
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code keyProvider} is null
     */
    public ChosenValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
        this(renderer, keyProvider, new ChosenOptions());
    }

    /**
     * @throws java.lang.NullPointerException if {@code renderer} is null
     * @throws java.lang.NullPointerException if {@code keyProvider} is null
     * @throws java.lang.NullPointerException if {@code options} is null
     */
    public ChosenValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider, ChosenOptions options) {
        super(keyProvider, options);

        Preconditions.checkNotNull(renderer);

        this.renderer = renderer;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public TakesValueEditor<T> asEditor() {
        if (editor == null) {
            editor = TakesValueEditor.of(this);
        }
        return editor;
    }

    /**
     * Get the selected value of the component
     *
     * @return
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * Set the value of the component. This method throws an IllegalStateException if the value isn't  part of the
     * accepted values.
     * <p/>
     * You can only pass {#code null} to reset the value of the component if and only if @{code null} is in the
     * accepted values list.
     * <p/>
     * See {@link #isAccepted(Object)} in order to test if a value can be accepted by the component.
     */
    @Override
    public void setValue(T newValue) {
        setValue(newValue, false);
    }

    /**
     * Set the value of the component. This method throws an IllegalStateException if the value isn't  part of the
     * accepted values.
     * <p/>
     * See {@link #isAccepted(Object)} in order to test if a value can be accepted by the component.
     * <p/>
     * If the {@code fireEvent} is set to true, a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} will be
     * fired.
     */
    @Override
    public void setValue(T newValue, boolean fireEvents) {
        setValue(newValue, fireEvents, true);
    }

    @Override
    protected void deselectValue(T value) {
        // never happen in single selection mode
    }

    @Override
    protected void selectValue(T value) {
        setValue(value, true, false);
    }

    @Override
    protected ChosenListBox createChosenListBox(ChosenOptions options) {
        return new ChosenListBox(options);
    }

    @Override
    protected void updateChosenListBox() {
        Object key = keyProvider.getKey(value);
        Integer index = valueKeyToIndex.get(key);

        if (index != null) {
            getChosenListBox().setSelectedIndex(index);
        } else {
            value = null; // the value is not in the accepted values list anymore.
            getChosenListBox().setSelectedIndex(-1);
        }
    }

    @Override
    protected void addItemToChosenListBox(T value) {
        getChosenListBox().addItem(renderer.render(value));
    }

    private void setValue(T newValue, boolean fireEvents, boolean update) {
        Preconditions.checkState(isAccepted(newValue), "This value is not in the acceptable values " +
                "list of this component");

        if (Objects.equals(newValue, value)) {
            return;
        }

        T before = value;
        value = newValue;

        if (update) {
            updateChosenListBox();
        }

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, before, value);
        }
    }
}
