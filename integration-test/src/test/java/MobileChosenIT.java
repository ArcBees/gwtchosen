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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.arcbees.chosen.integrationtest.client.domain.CarBrand;
import com.arcbees.chosen.integrationtest.client.testcases.ChosenListBoxMultipleSelectAddItems;
import com.arcbees.chosen.integrationtest.client.testcases.MaxSelectedOptions;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleMultiValueListBox;
import com.arcbees.chosen.integrationtest.client.testcases.SimpleValueListBox;
import com.google.common.base.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.HONDA;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.MERCEDES;
import static com.arcbees.chosen.integrationtest.client.domain.CarBrand.TOYOTA;
import static com.arcbees.chosen.integrationtest.client.domain.DefaultCarRenderer.RENDERER;

public class MobileChosenIT extends ChosenIT {
    private static final String IS_OPEN = "com-arcbees-chosen-client-resources-ChosenCss-is-open";

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
        assertDropdownIsOpenWithMobileLayout();
        assertThat(getOptions()).isEqualTo(CarBrand.getAllNames(RENDERER));
    }

    /**
     * On mobile, we should be able to close the dropdown by clicking on the icon net to the input.
     */
    @Test
    public void multipleMobile_closeDropdown() throws InterruptedException {
        // Given
        loadTestCase(new SimpleMultiValueListBox());
        openDropDown();

        // When
        closeMobileDropDown();

        // Then
        assertDropdownIsClosed();
    }

    /**
     * On mobile, we display the number of selected items.
     */
    @Test
    public void multipleMobile_displayNumberOfSelectedItem() throws InterruptedException {
        // Given
        loadTestCase(new SimpleMultiValueListBox());
        openDropDown();

        // When
        clickOption(MERCEDES, RENDERER);
        clickOption(TOYOTA, RENDERER);

        closeMobileDropDown();

        // Then
        assertThat(getSelectedOptionTexts()).hasSize(1);
        assertThat(getSelectedOptionTexts().get(0)).isEqualTo("2 items selected");
    }

    /**
     * Tests the <code>maxSelectedOptions</code> option. With mobile layout, we should be able to open the dropdown
     * in order to deselect items.
     */
    @Test
    public void maxSelectedOptions_optionsNotSelectableAnymore() {
        // Given
        loadTestCase(new MaxSelectedOptions());
        clickOption(MERCEDES, RENDERER);
        clickOption(TOYOTA, RENDERER);
        clickOption(HONDA, RENDERER);
        closeMobileDropDown();

        // When
        openDropDown();

        // Then
        assertDropdownIsOpenWithMobileLayout();
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
        assertThat(getSelectedOptionText()).isEqualTo("1 item selected");
    }

    @Override
    protected void assertDropdownIsClosed() {
        webDriverWait().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                try {
                    return webDriver.findElement(By.className(IS_OPEN)) == null;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });

        assertThat(getDropdown().getAttribute("class")).doesNotContain(IS_OPEN);
    }

    protected void openDropDown() {
        String xpath = "//div[@id='chosen_container__0_chzn']";
        WebElement btn = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));
        btn.click();

        waitUntilDropdownIsOpened();
    }

    private void closeMobileDropDown() {
        String xpath = "//div[@id='chosen_container__0_chzn']//i[@role='close']";

        WebElement closeButton = webDriverWait().until(elementToBeClickable(By.xpath(xpath)));

        closeButton.click();
    }

    private void assertDropdownIsOpenWithMobileLayout() {
        waitUntilDropdownIsOpened();

        int top = getDropdownTop();
        int bottom = getDropdownBottom();

        assertThat(top).isZero();
        assertThat(bottom).isZero();
        assertThat(isMobileChosenComponent()).isTrue();
    }

    private void waitUntilDropdownIsOpened() {
        webDriverWait().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return getDropdownTop() == 0;
            }
        });
    }

    private int getDropdownBottom() {
        WebElement dropdown = getDropdown();
        String bottomString = dropdown.getCssValue("bottom");

        if ("auto".equals(bottomString)) {
            return 0;
        }

        return Integer.parseInt(bottomString.replaceAll("px", ""));
    }
}
