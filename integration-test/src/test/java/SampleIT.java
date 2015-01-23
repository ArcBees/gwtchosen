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

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleIT {
    private static final String ROOT = "http://localhost:8080";

    private final WebDriver webDriver = new ChromeDriver();
    private static final int TIME_OUT_IN_SECONDS = 20;

    @Test
    public void sampleTest() throws Throwable {
        webDriver.get(ROOT);

        clickElementWithDisplayString("Ford");

        assertThat(getSelectedValueText()).isEqualTo("Ford");
    }

    @After
    public void after() {
        webDriver.quit();
    }

    private WebDriverWait webDriverWait() {
        return new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS);
    }

    private void clickElementWithDisplayString(String displayString) {
        openDropDown();

        WebElement li = webDriverWait().until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(String.format("//li[text()='%s']", displayString))));
        li.click();
    }

    private void openDropDown() {
        WebElement btn = webDriverWait().until(ExpectedConditions.elementToBeClickable(By.xpath
                ("//div[@id='chozen_container__0_chzn']/a")));

        btn.click();
    }

    private String getSelectedValueText() {
        WebElement span = webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//div[@id='chozen_container__0_chzn']/a/span")));

        return span.getText();
    }
}
