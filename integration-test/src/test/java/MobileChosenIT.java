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

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleValueListBox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class MobileChosenIT extends ChosenIT {
    @Before
    public void before() {
        // will trigger the mobile layout
        webDriver.manage().window().setSize(new Dimension(450, 600));
    }

    /**
     * Tests the mobile layout is displayed.
     */
    @Test
    public void singleMobile_mobileLayoutUsed() throws InterruptedException {
        // Given
        loadTestCase(new SimpleValueListBox());

        // When
        openDropDown();

        // Then
        assertMobileLayoutIsUsed();
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));
    }

    /**
     * On mobile, we should be able to close the dropdown by clicking on the icon net to the input.
     */
    @Test
    public void singleMobile_closeDropdown() throws InterruptedException {
        // Given
        loadTestCase(new SimpleValueListBox());
        openDropDown();

        // When
        closeMobileDropDown();

        // Then
        assertDropdownIsClosed();
    }

    protected void openDropDown() {
        String xpath = "//div[@id='chozen_container__0_chzn']";
        WebElement btn = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));
        btn.click();
    }

    private void closeMobileDropDown() {
        String xpath = "//div[@id='chozen_container__0_chzn']//i[@role='close']";

        WebElement closeButton = webDriverWait().until(presenceOfElementLocated(By.xpath(xpath)));

        closeButton.click();
    }

    private void assertMobileLayoutIsUsed() {
        int top = getDropdownTop();
        int bottom = getDropdownBottom();

        assertThat(top).isZero();
        assertThat(bottom).isZero();
        assertThat(isMobileChosenComponent()).isTrue();
    }

    private int getDropdownBottom() {
        WebElement dropdown = getDropdown();
        String topString = dropdown.getCssValue("bottom");
        return Integer.parseInt(topString.replaceAll("px", ""));
    }
}
