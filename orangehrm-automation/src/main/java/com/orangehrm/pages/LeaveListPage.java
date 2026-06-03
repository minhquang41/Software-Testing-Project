package com.orangehrm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * LeaveListPage - Page Object cho module Nghỉ phép (Leave Management)
 * URL: /web/index.php/leave/viewLeaveList
 */
public class LeaveListPage extends BasePage {

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private List<WebElement> leaveRows;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    private WebElement resetButton;

    @FindBy(css = ".oxd-select-text--active")
    private List<WebElement> dropdowns;

    // Date range fields
    @FindBy(css = "input.oxd-date-input")
    private List<WebElement> dateInputs;

    public boolean isLeaveListPageDisplayed() {
        return getCurrentUrl().contains("viewLeaveList") || getCurrentUrl().contains("leave");
    }

    public int getLeaveRequestCount() {
        return leaveRows.size();
    }

    public LeaveListPage setFromDate(String date) {
        if (!dateInputs.isEmpty()) {
            type(dateInputs.get(0), date);
        }
        return this;
    }

    public LeaveListPage setToDate(String date) {
        if (dateInputs.size() > 1) {
            type(dateInputs.get(1), date);
        }
        return this;
    }

    public LeaveListPage clickSearch() {
        click(searchButton);
        waitForLoadingSpinner();
        return this;
    }

    public LeaveListPage clickReset() {
        click(resetButton);
        waitForLoadingSpinner();
        return this;
    }

    public boolean hasLeaveRecords() {
        return !leaveRows.isEmpty();
    }
}
