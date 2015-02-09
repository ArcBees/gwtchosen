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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.AllowSingleDeselect;
import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.HideCurrentValue;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.TabNavigation;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.text.shared.Renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.CADILLAC;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.FORD;

public class ChosenIT {
    private static final String ROOT = "http://localhost:8080";
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
    public void hideEmptyValues() {
        // Given
        loadTestCase(new HideEmptyValues());

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

        // Then
        Set<String> options = getOptions();
        Set<String> allNames = CarBrand.getAllNames(ShowNonEmptyValues.RENDERER);
        allNames.add(ShowNonEmptyValues.RENDERER.render(null));
        assertThat(options).isEqualTo(allNames);
    }

    @Test
    public void hideCurrentValue() {
        // Given
        loadTestCase(new HideCurrentValue());

        // When
        openDropDown();

        // Then
        WebElement dropDown = getDropDown();
        assertThat(dropDown.getCssValue("top")).isEqualTo("0px");
    }

    /**
     * Goal: ensure allowSingleDeselect is working properly.
     */
    @Test
    public void allowSingleDeselect() {
        loadTestCase(new AllowSingleDeselect());
        clickOption(CADILLAC, AllowSingleDeselect.RENDERER);

        deselect();

        assertThat(getSelectedOptionText()).isEqualTo(AllowSingleDeselect.PLACEHOLDER);
    }

    /**
     * Goal: verify that tab navigation is possible when Chosen is within a form.
     */
    @Test
    public void tabNavigation() throws InterruptedException {
        loadTestCase(new TabNavigation());

        webDriverWait().until(elementToBeClickable(By.id("firstTextBox")));

        webDriver.switchTo().activeElement().sendKeys(Keys.TAB);
        webDriver.switchTo().activeElement().sendKeys(Keys.TAB);

        // at this point, focus is on the Chosen widget
        final String searchText = "ferr";
        webDriver.switchTo().activeElement().sendKeys(searchText);

        final WebElement inputBox = getInput();

        webDriverWait().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(@Nullable WebDriver input) {
                String value = inputBox.getAttribute("value");
                return value != null && value.equals(searchText);
            }
        });
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

    private WebElement getDropDown() {
        String xpath = "//div[@class='com-arcbees-chosen-client-resources-ChozenCss-chzn-drop']";

        return webDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    private void openDropDown() {
        String xpath = "//div[@id='chozen_container__0_chzn']/a";
        WebElement btn = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));

        btn.click();
    }

    private String getSelectedOptionText() {
        String xpath = "//div[@id='chozen_container__0_chzn']/a/span";
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
}
