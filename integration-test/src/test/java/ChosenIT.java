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

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.DisableSearchThreshold;
import com.arcbees.chosen.integrationtest.client.testcases.EnabledDisabled;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.IsAcceptedValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SearchContains;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleMultiValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleValueListBox;
import com.arcbees.test.ByDebugId;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.text.shared.Renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.AUDI;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.FORD;
import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public abstract class ChosenIT {
    private static final String ROOT = "http://localhost:" + System.getProperty("testPort");
    private static final int TIME_OUT_IN_SECONDS = 20;

    protected final WebDriver webDriver = new ChromeDriver();

    @After
    public void after() {
        webDriver.quit();
    }

    @Test
    public void chooseOption() throws Throwable {
        // Given
        loadTestCase(new ChooseOption());
        String fordRender = RENDERER.render(FORD);

        // When
        clickOptionWithDisplayString(fordRender);

        // Then
        assertThat(getSelectedOptionText()).isEqualTo(fordRender);
    }

    /**
     * Tests the disableSearchThreshold options.
     */
    @Test
    public void disableSearchThreshold_searchInputNotVisible() {
        // Given
        loadTestCase(new DisableSearchThreshold());

        // When
        openDropDown();

        // Then
        assertThat(getInput().isDisplayed()).isFalse();
    }

    /**
     * This test asserts that a {@link com.arcbees.chosen.client.gwt.ChosenValueListBox} can be enabled/disabled
     * successfully.
     */
    @Test
    public void enabledDisabled() {
        // Given
        loadTestCase(new EnabledDisabled());
        String disabledClassName = "com-arcbees-chosen-client-resources-ChosenCss-chzn-disabled";

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
     * This test makes sure that when null values are rendered as empty string (""),
     * then the empty string will not be displayed in the dropdown options.
     */
    @Test
    public void hideEmptyValues() {
        // Given
        loadTestCase(new HideEmptyValues());
        openDropDown();

        // Then
        Set<String> options = getOptions();
        assertThat(options).isEqualTo(CarBrand.getAllNames(RENDERER));
    }

    /**
     * Tests that the <code>searchContains</code> option is set to true, the search will match words containing the
     * query.
     */
    @Test
    public void searchContains_filterOnPartialMatch() {
        // Given
        loadTestCase(new SearchContains());

        // When
        openDropDown();

        // Then
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));

        // When
        String audi = RENDERER.render(AUDI);
        searchOn(audi.substring(1));

        Set<String> options = getOptions();

        assertThat(options.size()).isEqualTo(1);
        assertThat(options).contains(audi);
    }

    /**
     * Tests that when user enters text on the search, the component (multiple) filters the options.
     */
    @Test
    public void search_multiple_reduceOptions() {
        // Given
        loadTestCase(new SimpleMultiValueListBox());

        // When
        openDropDown();

        // Then
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));

        // When
        String audi = RENDERER.render(AUDI);
        searchOn(audi);

        Set<String> options = getOptions();

        assertThat(options.size()).isEqualTo(1);
        assertThat(options).contains(audi);
    }

    /**
     * Tests that when user enters text on the search, the component filters the options.
     */
    @Test
    public void search_single_reduceOptions() {
        // Given
        loadTestCase(new SimpleValueListBox());

        // When
        openDropDown();

        // Then
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));

        // When
        String audi = RENDERER.render(AUDI);
        searchOn(audi);

        Set<String> options = getOptions();

        assertThat(options.size()).isEqualTo(1);
        assertThat(options).contains(audi);
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
     * Tests the isAccepted is correct.
     */
    @Test
    public void testIsAccepted() {
        // Given
        loadTestCase(new IsAcceptedValueListBox());

        // When
        openDropDown();

        // Then
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));
    }

    protected void assertDropdownIsBelow() {
        int top = getDropdownTop();

        assertThat(getDropdown().isDisplayed()).isTrue();
        assertThat(top).isPositive();
    }

    protected void assertDropdownIsClosed() {
        assertThat(getDropdownTop()).isEqualTo(-9000);
    }

    protected <T extends Enum<T>> void clickOption(T val, Renderer<T> renderer) {
        clickOptionWithDisplayString(renderer.render(val));
    }

    protected WebElement getDropdown() {
        return webDriverWait().until(presenceOfElementLocated(
                By.className("com-arcbees-chosen-client-resources-ChosenCss-chzn-drop")));
    }

    protected int getDropdownTop() {
        WebElement dropdown = getDropdown();
        String topString = dropdown.getCssValue("top");

        if ("auto".equals(topString)) {
            return 0;
        }

        return (int) Double.parseDouble(topString.replaceAll("px", ""));
    }

    protected WebElement getInput() {
        String xpath = "//div[@id='chosen_container__0_chzn']//input[@type='text']";

        return webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
    }

    protected Set<String> getOptions() {
        String cssSelector = "li.com-arcbees-chosen-client-resources-ChosenCss-active-result";
        List<WebElement> options = webDriverWait().until(presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        return Sets.newHashSet(Lists.transform(options, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement input) {
                return input.getText();
            }
        }));
    }

    protected String getSelectedOptionText() {
        List<String> selectedOptions = getSelectedOptionTexts();

        return selectedOptions.isEmpty() ? null : selectedOptions.get(0);
    }

    protected List<String> getSelectedOptionTexts() {
        String xpath = "//div[@id='chosen_container__0_chzn']//span";

        List<WebElement> options = webDriverWait().until(presenceOfAllElementsLocatedBy(By.xpath(xpath)));
        return Lists.transform(options, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement element) {
                return element.getText();
            }
        });
    }

    protected boolean isMobileChosenComponent() {
        List<WebElement> multiContainer = webDriver.findElements(
                By.className("com-arcbees-chosen-client-resources-ChosenCss-chzn-mobile-container"));

        return multiContainer.size() != 0;
    }

    protected void loadTestCase(TestCase testCase) {
        webDriver.get(ROOT + "/#" + testCase.getToken());
    }

    protected abstract void openDropDown();

    protected void searchOn(String searchText) {
        getInput().clear();
        getInput().sendKeys(searchText);
    }

    protected void singleDeselect() {
        WebElement abbr = webDriverWait().until(presenceOfElementLocated(By.tagName("abbr")));
        abbr.click();
    }

    protected WebDriverWait webDriverWait() {
        return new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS);
    }

    private void clickOptionWithDisplayString(String displayString) {
        openDropDown();

        String xpath = String.format("//li[text()='%s']", displayString);

        WebElement li = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));

        li.click();
    }

    protected boolean isMultipleChosenComponent() {
        List<WebElement> multiContainer = webDriver.findElements(
                By.className("com-arcbees-chosen-client-resources-ChosenCss-chzn-container-multi"));

        return multiContainer.size() != 0;
    }
}
