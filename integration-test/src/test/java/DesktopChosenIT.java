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

import javax.annotation.Nullable;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.AllowSingleDeselect;
import com.arcbees.chosen.integrationtest.client.testcases.ChosenListBoxMultipleSelectAddItems;
import com.arcbees.chosen.integrationtest.client.testcases.ChosenListBoxSingleSelectAddItems;
import com.arcbees.chosen.integrationtest.client.testcases.MaxSelectedOptions;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleMultiValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SingleBackstrokeDelete;
import com.arcbees.chosen.integrationtest.client.testcases.TabNavigation;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Above;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Below;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.gwt.text.shared.Renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.AUDI;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.BMW;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.CADILLAC;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.HONDA;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.MERCEDES;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.TOYOTA;
import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class DesktopChosenIT extends ChosenIT {

    /**
     * Goal: ensure allowSingleDeselect is working properly.
     */
    @Test
    public void allowSingleDeselect() {
        // Given
        loadTestCase(new AllowSingleDeselect());
        clickOption(CADILLAC, RENDERER);

        // When
        singleDeselect();

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(AllowSingleDeselect.PLACEHOLDER);
    }

    /**
     * Tests that when
     * - we deselect an option to a multiple chosen list box.
     *
     * the dropdown with the choices aren't displayed
     */
    @Test
    public void deselectOption_dropdownNotShowing() {
        // Given
        loadTestCase(new SimpleMultiValueListBox());
        clickOption(CarBrand.AUDI, RENDERER);

        // When
        deselectOption(CarBrand.AUDI, RENDERER);

        // Then
        assertDropdownIsClosed();
    }

    /**
     * Tests that we can remove the last option with two backspaces.
     */
    @Test
    public void doubleBackstroke_removeLastOption() {
        // Given
        loadTestCase(new SimpleMultiValueListBox());
        clickOption(AUDI, RENDERER);
        clickOption(BMW, RENDERER);

        // When
        getInput().sendKeys(Keys.BACK_SPACE);

        // Then last option not disabled
        assertThat(getSelectedOptionTexts()).isEqualTo(Lists.newArrayList(RENDERER.render(AUDI), RENDERER.render(BMW)));

        // When
        getInput().sendKeys(Keys.BACK_SPACE);

        // Then
        assertThat(getSelectedOptionTexts()).isEqualTo(Lists.newArrayList(RENDERER.render(AUDI)));
    }

    /**
     * Tests that when the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.ABOVE},
     * the dropdown is displayed above the input box.
     */
    @Test
    public void dropdownPosition_above() {
        // Given
        loadTestCase(new Above());

        // When
        openDropDown();

        // Then
        assertDropdownIsAbove();
    }

    /**
     * Tests that when
     * - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.AUTO}
     * - No boundaries are set
     * - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown has enough space below
     *
     * the dropdown will be displayed below the input box.
     */
    @Test
    public void dropdownPosition_autoNoBoundariesHasEnoughSpace() {
        // Given
        loadTestCase(new AutoNoBoundariesHasEnoughSpace());

        // When
        openDropDown();

        // Then
        assertDropdownIsBelow();
    }

    /**
     * Tests that when
     * - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.AUTO}
     * - No boundaries are set
     * - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown does not have enough space below
     *
     * the dropdown will be displayed above the input box.
     */
    @Test
    public void dropdownPosition_autoNoBoundariesHasNotEnoughSpace() {
        // Given
        loadTestCase(new AutoNoBoundariesHasNotEnoughSpace());

        // When
        openDropDown();

        // Then
        assertDropdownIsAbove();

        // When
        searchOn("Audi");

        // Then
        assertDropdownIsAbove();
    }

    /**
     * Tests that when
     * - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.AUTO}
     * - Boundaries are set to particular DOM element
     * - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown has enough space below
     *
     * the dropdown will be displayed below the input box.
     */
    @Test
    public void dropdownPosition_autoWithBoundariesHasEnoughSpace() {
        // Given
        loadTestCase(new AutoWithBoundariesHasEnoughSpace());

        // When
        openDropDown();

        // Then
        assertDropdownIsBelow();
    }

    /**
     * Tests that when
     * - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.AUTO}
     * - Boundaries are set to particular DOM element
     * - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown does not have enough space below
     *
     * the dropdown will be displayed above the input box and will not move when filtering the options.
     */
    @Test
    public void dropdownPosition_autoWithBoundariesHasNotEnoughSpace() {
        // Given
        loadTestCase(new AutoWithBoundariesHasNotEnoughSpace());

        // When
        openDropDown();

        // Then
        assertDropdownIsAbove();
    }

    /**
     * Tests that when the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.BELOW},
     * the dropdown is displayed below the input box.
     */
    @Test
    public void dropdownPosition_below() {
        // Given
        loadTestCase(new Below());

        // When
        openDropDown();

        // Then
        assertDropdownIsBelow();
    }

    /**
     * Tests that we can remove the last option with one backspace of the option <code>singleBackstrokeDelete</code> is
     * enabled.
     */
    @Test
    public void singleBackstrokeDelete_removeLastOption() {
        // Given
        loadTestCase(new SingleBackstrokeDelete());

        // When
        clickOption(AUDI, RENDERER);
        clickOption(BMW, RENDERER);

        // Then
        assertThat(getSelectedOptionTexts()).isEqualTo(Lists.newArrayList(RENDERER.render(AUDI), RENDERER.render(BMW)));

        // When
        getInput().sendKeys(Keys.BACK_SPACE);

        // Then
        assertThat(getSelectedOptionTexts()).isEqualTo(Lists.newArrayList(RENDERER.render(AUDI)));
    }

    /**
     * Tests the desktop layout is displayed.
     */
    @Test
    public void singleMobile_mobileLayoutUsed() throws InterruptedException {
        // Given
        loadTestCase(new SimpleValueListBox());

        // When
        openDropDown();

        // Then
        assertDropdownIsBelow();
        assertThat(isMobileChosenComponent()).isFalse();
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));
    }

    /**
     * Tests that the values are added/selected when using update/setItemSelected.
     * See https://github.com/ArcBees/gwtchosen/issues/271
     */
    @Test
    public void chosenListBox_updateAndSelect_addsAndSelectItem() {
        // Given
        loadTestCase(new ChosenListBoxMultipleSelectAddItems());

        // When
        getElementById(ChosenListBoxMultipleSelectAddItems.BUTTON_ID).click();

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(ChosenListBoxMultipleSelectAddItems.SELECTED_VALUE);
    }

    @Test
    public void chosenListBox_updateAndSelect_removeDefaultStyle() {
        // Given
        loadTestCase(new ChosenListBoxSingleSelectAddItems());

        // When
        getElementById(ChosenListBoxSingleSelectAddItems.BUTTON_ID).click();

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(ChosenListBoxSingleSelectAddItems.SELECTED_VALUE);
        assertThat(getPlaceholder().getAttribute("class")).doesNotContain("chzn-default");
    }

    /**
     * Goal: verify that tab navigation is possible when Chosen is within a form.
     */
    @Test
    public void tabNavigation() {
        // Given
        loadTestCase(new TabNavigation());

        // When
        webDriverWait().until(elementToBeClickable(By.id("firstTextBox")));

        webDriver.switchTo().activeElement().sendKeys(Keys.TAB);
        webDriver.switchTo().activeElement().sendKeys(Keys.TAB);

        // at this point, focus is on the Chosen widget
        final String searchText = "ferr";
        webDriver.switchTo().activeElement().sendKeys(searchText);

        final WebElement inputBox = getInput();

        // Then
        webDriverWait().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(@Nullable WebDriver input) {
                String value = inputBox.getAttribute("value");
                return value != null && value.equals(searchText);
            }
        });
    }

    /**
     * Tests the <code>maxSelectedOptions</code> option.
     */
    @Test
    public void maxSelectedOptions_optionsNotSelectableAnymore() {
        // Given
        loadTestCase(new MaxSelectedOptions());

        // When
        clickOption(MERCEDES, RENDERER);
        clickOption(TOYOTA, RENDERER);
        clickOption(HONDA, RENDERER);

        openDropDown();

        // Then
        assertDropdownIsClosed();
    }

    protected void openDropDown() {
        WebElement btn;

        if (isMultipleChosenComponent()) {
            btn = getInput();
        } else { // single
            String xpath = "//div[@id='chosen_container__0_chzn']";
            btn = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));
        }
        btn.click();
    }

    private void assertDropdownIsAbove() {
        int top = getDropdownTop();

        assertThat(top).isNegative().isNotEqualTo(-9000);
    }

    /**
     * Deselect an option previously selected. Work only with multiple chosen list box.
     */
    private <T extends Enum<T>> void deselectOption(T val, Renderer<T> renderer) {
        String xpath = String.format("//li[span/text()='%s']/a", renderer.render(val));
        WebElement abbr = webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
        abbr.click();
    }
}
