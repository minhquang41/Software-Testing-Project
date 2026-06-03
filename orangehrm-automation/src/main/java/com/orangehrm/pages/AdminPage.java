package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * AdminPage - Page Object cho trang quản lý người dùng hệ thống
 * URL: /web/index.php/admin/viewSystemUsers
 */
public class AdminPage extends BasePage {

    @FindBy(css = "button.oxd-button--secondary[class*='add']")
    private WebElement addUserButton;

    @FindBy(css = "input.oxd-input--active")
    private WebElement searchUsernameField;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Reset']")
    private WebElement resetButton;

    public boolean isAdminPageDisplayed() {
        return getCurrentUrl().contains("viewSystemUsers");
    }

    public AdminPage searchByUsername(String username) {
        type(searchUsernameField, username);
        click(searchButton);
        waitForLoadingSpinner();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        logger.info("🔍 Tìm kiếm user: {}", username);
        return this;
    }

    public AdminPage clickReset() {
        click(resetButton);
        waitForLoadingSpinner();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return this;
    }

    public int getUserCount() {
        // Dùng findElements trực tiếp để tránh stale reference
        return driver.findElements(
            By.cssSelector(".oxd-table-body .oxd-table-row")
        ).size();
    }

    public int getUserCountAfterSearch() {
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        return driver.findElements(
            By.cssSelector(".oxd-table-body .oxd-table-row")
        ).size();
    }

    public boolean isUserPresent(String username) {
        return driver.findElements(By.cssSelector(".oxd-table-body .oxd-table-row"))
            .stream().anyMatch(row -> row.getText().contains(username));
    }

    public boolean isNoRecordFound() {
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        try {
            By noRecord = By.xpath("//*[contains(text(),'No Records Found')]");
            if (!driver.findElements(noRecord).isEmpty()) return true;
        } catch (Exception ignored) {}
        return driver.getPageSource().contains("No Records Found");
    }
}