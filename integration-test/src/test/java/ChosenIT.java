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

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer;
import com.arcbees.chosen.integrationtest.client.testcases.AllowSingleDeselect;
import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.EnabledDisabled;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleMultiValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.TabNavigation;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Above;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoNoBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.AutoWithBoundariesHasNotEnoughSpace;
import com.arcbees.chosen.integrationtest.client.testcases.dropdownposition.Below;
import com.arcbees.test.ByDebugId;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.text.shared.Renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.CADILLAC;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.FORD;

public class ChosenIT {
    private static final String ROOT = "http://localhost:" + System.getProperty("testPort");
    private static final int TIME_OUT_IN_SECONDS = 20;
    private final WebDriver webDriver = new ChromeDriver();

    @Test
    public void chooseOption() throws Throwable {
        // Given
        loadTestCase(new ChooseOption());
        String fordRender = ChooseOption.RENDERER.render(FORD);

        // When
        clickOptionWithDisplayString(fordRender);

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(fordRender);
    }

    /**
     * This test makes sure that when null values are rendered as empty string (""),
     * then the empty string will not be displayed in the dropdown options.
     */
    @Test
    public void hideEmptyValues() throws InterruptedException {
        // Given
        loadTestCase(new HideEmptyValues());
        openDropDown();

        // Then
        Set<String> options = getOptions();
        assertThat(options).isEqualTo(CarBrand.getAllNames(HideEmptyValues.RENDERER));
    }

    /**
     * This test makes sure that when null values are rendered as a non-empty string,
     * then that exact non-empty string will be displayed in the dropdown options.
     */
    @Test
    public void showNonEmptyValues() {
        // Given
        loadTestCase(new ShowNonEmptyValues());
        openDropDown();

        // Then
        Set<String> options = getOptions();
        Set<String> allNames = CarBrand.getAllNames(ShowNonEmptyValues.RENDERER);
        allNames.add(ShowNonEmptyValues.RENDERER.render(null));
        assertThat(options).isEqualTo(allNames);
    }

    /**
     * Goal: ensure allowSingleDeselect is working properly.
     */
    @Test
    public void allowSingleDeselect() {
        // Given
        loadTestCase(new AllowSingleDeselect());
        clickOption(CADILLAC, AllowSingleDeselect.RENDERER);

        // When
        deselect();

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(AllowSingleDeselect.PLACEHOLDER);
    }

    /**
     * Goal: verify that tab navigation is possible when Chosen is within a form.
     */
    @Test
    public void tabNavigation() throws InterruptedException {
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
     * This test asserts that a {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} can be enabled/disabled
     * successfully.
     */
    @Test
    public void enabledDisabled() {
        // Given
        loadTestCase(new EnabledDisabled());
        String disabledClassName = "com-arcbees-chosen-client-resources-ChozenCss-chzn-disabled";

        // When
        WebElement disableButton = webDriverWait().until(presenceOfElementLocated(
                ByDebugId.id(EnabledDisabled.DISABLE_DEBUG_ID)));
        disableButton.click();

        // Then
        webDriverWait().until(presenceOfElementLocated(
                By.className(disabledClassName)));

        // When
        WebElement enableButton = webDriverWait().until(presenceOfElementLocated(
                ByDebugId.id(EnabledDisabled.ENABLE_DEBUG_ID)));
        enableButton.click();

        // Then
        try {
            int quickTimeout = 1; // we don't want this test to wait for too long
            new WebDriverWait(webDriver, quickTimeout).until(presenceOfElementLocated(
                    By.className(disabledClassName)));
            fail("The ChosenValueListBox shouldn't be enabled at this point");
        } catch (TimeoutException e) {
            // success, element should be absent from DOM
        }
    }

    /**
     * Tests that when the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.ABOVE},
     * the dropdown is displayed above the input box.
     */
    @Test
    public void dropdownPosition_above() {
        // Given
        loadTestCase(new Above());

        // When
        openDropDown();

        // When
        assertDropdownIsAbove();
    }

    /**
     * Tests that when the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.BELOW},
     * the dropdown is displayed below the input box.
     */
    @Test
    public void dropdownPosition_below() {
        // Given
        loadTestCase(new Below());

        // When
        openDropDown();

        // When
        assertDropdownIsBelow();
    }

    /**
     * Tests that when
     *  - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.AUTO}
     *  - No boundaries are set
     *  - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown has enough space below
     *
     * the dropdown will be displayed below the input box.
     */
    @Test
    public void dropdownPosition_autoNoBoundariesHasEnoughSpace() {
        // Given
        loadTestCase(new AutoNoBoundariesHasEnoughSpace());

        // When
        openDropDown();

        // When
        assertDropdownIsBelow();
    }

    /**
     * Tests that when
     *  - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.AUTO}
     *  - No boundaries are set
     *  - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown does not have enough space below
     *
     * the dropdown will be displayed above the input box.
     */
    @Test
    public void dropdownPosition_autoNoBoundariesHasNotEnoughSpace() {
        // Given
        loadTestCase(new AutoNoBoundariesHasNotEnoughSpace());

        // When
        openDropDown();

        // When
        assertDropdownIsAbove();
    }

    /**
     * Tests that when
     *  - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.AUTO}
     *  - Boundaries are set to particular DOM element
     *  - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown has enough space below
     *
     * the dropdown will be displayed below the input box.
     */
    @Test
    public void dropdownPosition_autoWithBoundariesHasEnoughSpace() {
        // Given
        loadTestCase(new AutoWithBoundariesHasEnoughSpace());

        // When
        openDropDown();

        // When
        assertDropdownIsBelow();
    }

    /**
     * Tests that when
     *  - the dropdown is set to {@link com.arcbees.chosen.client.DropdownPosition.Position.AUTO}
     *  - Boundaries are set to particular DOM element
     *  - the {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} dropdown does not have enough space below
     *
     * the dropdown will be displayed above the input box.
     */
    @Test
    public void dropdownPosition_autoWithBoundariesHasNotEnoughSpace() {
        // Given
        loadTestCase(new AutoWithBoundariesHasNotEnoughSpace());

        // When
        openDropDown();

        // When
        assertDropdownIsAbove();
    }

    /**
     * Tests that when
     *  - we deselect an option to a multiple chosen list box.
     *
     * the dropdown with the choices aren't displayed
     */
    @Test
    public void deselectOption_dropdownNotShowing() {
        // Given
        loadTestCase(new SimpleMultiValueListBox());
        clickOption(CarBrand.AUDI, new DefaultCarRenderer());

        // When
        deselectOption(CarBrand.AUDI, new DefaultCarRenderer());

        // Then
        assertDropdownIsClosed();
    }

    @After
    public void after() {
        webDriver.quit();
    }

    private WebDriverWait webDriverWait() {
        return new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS);
    }

    private void deselect() {
        WebElement abbr = webDriverWait().until(presenceOfElementLocated(By.tagName("abbr")));
        abbr.click();
    }

    /**
     * Deselect an option previously selected. Work only with multiple chosen list box.
     */
    private <T extends Enum<T>> void deselectOption(T val,  Renderer<T> renderer) {
        String xpath = String.format("//li[span/text()='%s']/a", renderer.render(val));
        WebElement abbr = webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
        abbr.click();
    }

    private Set<String> getOptions() {
        String xpath = "//ul/li";
        List<WebElement> options = webDriverWait().until(presenceOfAllElementsLocatedBy(By.xpath(xpath)));
        return Sets.newHashSet(Lists.transform(options, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement input) {
                return input.getText();
            }
        }));
    }

    private <T extends Enum<T>> void clickOption(T val, Renderer<T> renderer) {
        clickOptionWithDisplayString(renderer.render(val));
    }

    private void clickOptionWithDisplayString(String displayString) {
        openDropDown();

        String xpath = String.format("//li[text()='%s']", displayString);
        WebElement li = webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
        li.click();

        assertThat(getSelectedOptionText()).isEqualTo(displayString);
    }

    private void openDropDown() {
        String xpath = "//div[@id='chozen_container__0_chzn']";
        WebElement btn = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));

        btn.click();
    }

    private String getSelectedOptionText() {
        String xpath = "//div[@id='chozen_container__0_chzn']//span[1]";
        WebElement span = webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        return span.getText();
    }

    private WebElement getInput() {
        String xpath = "//div[@class='com-arcbees-chosen-client-resources-ChozenCss-chzn-search " +
                "icon_search']/input[@type='text']";

        return webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
    }

    private void loadTestCase(TestCase testCase) {
        webDriver.get(ROOT + "/#" + testCase.getToken());
    }

    private WebElement getDropdown() {
        return webDriverWait().until(presenceOfElementLocated(
                By.className("com-arcbees-chosen-client-resources-ChozenCss-chzn-drop")));
    }

    private int getDropdownTop() {
        WebElement dropdown = getDropdown();
        String topString = dropdown.getCssValue("top");
        return Integer.parseInt(topString.replaceAll("px", ""));
    }

    private void assertDropdownIsBelow() {
        int top = getDropdownTop();

        assertThat(top).isPositive();
    }

    private void assertDropdownIsAbove() {
        int top = getDropdownTop();

        assertThat(top).isNegative().isNotEqualTo(-9000);
    }

    private void assertDropdownIsClosed() {
        assertThat(getDropdownTop()).isEqualTo(-9000);
    }
}
