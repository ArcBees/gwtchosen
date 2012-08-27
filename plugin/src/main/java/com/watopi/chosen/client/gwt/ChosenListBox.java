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

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

import static com.watopi.chosen.client.Chosen.Chosen;

import com.watopi.chosen.client.ChosenOptions;
import com.watopi.chosen.client.event.ChosenChangeEvent;
import com.watopi.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.watopi.chosen.client.event.HidingDropDownEvent;
import com.watopi.chosen.client.event.HidingDropDownEvent.HidingDropDownHandler;
import com.watopi.chosen.client.event.MaxSelectedEvent;
import com.watopi.chosen.client.event.MaxSelectedEvent.MaxSelectedHandler;
import com.watopi.chosen.client.event.ReadyEvent;
import com.watopi.chosen.client.event.ReadyEvent.ReadyHandler;
import com.watopi.chosen.client.event.ShowingDropDownEvent;
import com.watopi.chosen.client.event.ShowingDropDownEvent.ShowingDropDownHandler;
import com.watopi.chosen.client.event.UpdatedEvent;



public class ChosenListBox extends ListBox {

  /**
   * Creates a ChosenListBox widget that wraps an existing &lt;select&gt; element.
   * 
   * This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
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
  
  /**
   * Indicates of the ChosenListBox is supported by the current browser.
   * If not (IE6/7), we fall back on normal select element.
   * @return
   */
  public static boolean isSupported(){
    return com.watopi.chosen.client.Chosen.isSupported();
  }
  private EventBus chznHandlerManager;
  
  private ChosenOptions options;

  /**
   * Creates an empty chosen component in single selection mode.
   */
  public ChosenListBox() {
    this(false);
  }

  /**
   * Creates an empty list box. The preferred way to enable multiple selections is to use this
   * constructor rather than {@link #setMultipleSelect(boolean)}.
   * 
   * @param isMultipleSelect specifies if multiple selection is enabled
   */
  public ChosenListBox(boolean isMultipleSelect) {
   this(isMultipleSelect, new ChosenOptions());
  }
  
  /**
   * Creates an empty list box. The preferred way to enable multiple selections is to use this
   * constructor rather than {@link #setMultipleSelect(boolean)}.
   * 
   * @param isMultipleSelect specifies if multiple selection is enabled
   */
  public ChosenListBox(boolean isMultipleSelect, ChosenOptions options) {
    super(Document.get().createSelectElement(isMultipleSelect));
    this.options = options;
  }

  protected ChosenListBox(Element element) {
    super(element);
  }

  /**
   * Deprecated, use {@link #addChosenChangeHandler(ChosenChangeHandler)} instead
   */
  @Override
  @Deprecated
  public com.google.gwt.event.shared.HandlerRegistration addChangeHandler(final com.google.gwt.event.dom.client.ChangeHandler handler) {
    final HandlerRegistration registration = addChosenChangeHandler(new ChosenChangeHandler() {
      public void onChange(ChosenChangeEvent event) {
        handler.onChange(null);
      }
    });
    
    return new com.google.gwt.event.shared.HandlerRegistration(){
      public void removeHandler() {
        registration.removeHandler();
      }
    };
  }

  public HandlerRegistration addChosenChangeHandler(ChosenChangeHandler handler) {
    return ensureChosenHandlers().addHandler(ChosenChangeEvent.getType(), handler);
  }
  public HandlerRegistration addHidingDropDownHandler(HidingDropDownHandler handler) {
    return ensureChosenHandlers().addHandler(HidingDropDownEvent.getType(), handler);
  }

  public HandlerRegistration addMaxSelectedHandler(MaxSelectedHandler handler) {
    return ensureChosenHandlers().addHandler(MaxSelectedEvent.getType(), handler);
  }

  public HandlerRegistration addReadyHandler(ReadyHandler handler) {
    return ensureChosenHandlers().addHandler(ReadyEvent.getType(), handler);
  }

  public HandlerRegistration addShowingDropDownHandler(ShowingDropDownHandler handler) {
    return ensureChosenHandlers().addHandler(ShowingDropDownEvent.getType(), handler);
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
   * Specify if the deselection is allowed on single selects.
   * 
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

  public void setAllowSingleDeselect(boolean allowSingleDeselect) {
    options.setAllowSingleDeselect(allowSingleDeselect);
  }

  public void setDisableSearchThreshold(int disableSearchThreshold) {
    options.setDisableSearchThreshold(disableSearchThreshold);
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

  public void setSingleBackstrokeDelete(boolean singleBackstrokeDelete) {
     options.setSingleBackstrokeDelete(singleBackstrokeDelete);
  }

  /**
   * Use this method to update the chosen list box (i.e. after insertion or removal of options)
   */
  public void update() {
    ensureChosenHandlers().fireEvent(new UpdatedEvent());
  }

  protected final <H extends EventHandler> HandlerRegistration addChosenHandler(H handler,
      Type<H> type) {
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
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    $(getElement()).as(Chosen).destroy();
  }

}
