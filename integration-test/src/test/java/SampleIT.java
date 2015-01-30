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

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.arcbees.chosen.integrationtest.client.TestCase;
import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.ChooseOption;
import com.arcbees.chosen.integrationtest.client.testcases.HideEmptyValues;
import com.arcbees.chosen.integrationtest.client.testcases.ShowNonEmptyValues;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.FORD;

public class SampleIT {
    private static final String ROOT = "http://localhost:8080";
    private static final int TIME_OUT_IN_SECONDS = 20;
    private final WebDriver webDriver = new ChromeDriver();

    @Test
    public void chooseOption() throws Throwable {
        loadTestCase(new ChooseOption());
        String fordRender = ChooseOption.RENDERER.render(FORD);

        clickOptionWithDisplayString(fordRender);

        assertThat(getSelectedOptionText()).isEqualTo(fordRender);
    }

    @Test
    public void hideEmptyValues() {
        loadTestCase(new HideEmptyValues());

        Set<String> options = getOptions();

        assertThat(options).isEqualTo(CarBrand.getAllNames(HideEmptyValues.RENDERER));
    }

    @Test
    public void showNonEmptyValues() {
        loadTestCase(new ShowNonEmptyValues());

        Set<String> options = getOptions();

        Set<String> allNames = CarBrand.getAllNames(ShowNonEmptyValues.RENDERER);
        allNames.add(ShowNonEmptyValues.RENDERER.render(null));
        assertThat(options).isEqualTo(allNames);
    }

    @After
    public void after() {
        webDriver.quit();
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

    private WebDriverWait webDriverWait() {
        return new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS);
    }

    private void clickOptionWithDisplayString(String displayString) {
        openDropDown();

        String xpath = String.format("//li[text()='%s']", displayString);
        WebElement li = webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));
        li.click();
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

    private void loadTestCase(TestCase testCase) {
        webDriver.get(ROOT + "/#" + testCase.getToken());
    }
}
