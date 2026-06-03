package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * EmployeeListPage - Page Object cho trang danh sách nhân viên (PIM Module)
 * URL: /web/index.php/pim/viewEmployeeList
 */
public class EmployeeListPage extends BasePage {

    // ==================== WEB ELEMENTS ====================

    @FindBy(xpath = "//button[normalize-space()='Add']")
    private WebElement addEmployeeButton;

    @FindBy(css = "input[placeholder='Type for hints...']")
    private WebElement searchNameField;

    @FindBy(css = "input.oxd-input[class*='--active']:not([placeholder])")
    private WebElement searchEmployeeIdField;

    @FindBy(css = "button[type='submit'].oxd-button--secondary")
    private WebElement searchButton;

    @FindBy(css = "button[type='reset'].oxd-button--ghost")
    private WebElement resetButton;

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private List<WebElement> employeeRows;

    @FindBy(css = ".oxd-table-header-map .oxd-checkbox-wrapper")
    private WebElement selectAllCheckbox;

    @FindBy(css = ".oxd-text--span")
    private WebElement recordCount;

    // ==================== PAGE ACTIONS ====================

    /**
     * Click thêm nhân viên mới
     */
    public AddEmployeePage clickAddEmployee() {
        click(addEmployeeButton);
        logger.info("➕ Click thêm nhân viên mới");
        waitForLoadingSpinner();
        return new AddEmployeePage();
    }

    /**
     * Tìm kiếm nhân viên theo tên
     */
    public EmployeeListPage searchByName(String name) {
        type(searchNameField, name);
        logger.info("🔍 Tìm kiếm nhân viên: {}", name);
        return this;
    }

    /**
     * Tìm kiếm nhân viên theo ID
     */
    public EmployeeListPage searchByEmployeeId(String employeeId) {
        // Employee ID field - trường thứ 2 trong form
        List<WebElement> inputs = driver.findElements(By.cssSelector(".oxd-input.oxd-input--active"));
        if (inputs.size() > 1) {
            type(inputs.get(1), employeeId);
        }
        logger.info("🔍 Tìm kiếm theo ID: {}", employeeId);
        return this;
    }

    /**
     * Click nút tìm kiếm
     */
    public EmployeeListPage clickSearch() {
        click(searchButton);
        waitForLoadingSpinner();
        logger.info("🔍 Thực hiện tìm kiếm");
        return this;
    }

    /**
     * Reset bộ lọc tìm kiếm
     */
    public EmployeeListPage clickReset() {
        click(resetButton);
        waitForLoadingSpinner();
        logger.info("🔄 Reset bộ lọc");
        return this;
    }

    /**
     * Click vào nút Edit của dòng đầu tiên
     */
    public AddEmployeePage clickEditFirstEmployee() {
        if (!employeeRows.isEmpty()) {
            WebElement editBtn = employeeRows.get(0)
                .findElement(By.cssSelector("button.oxd-icon-button:nth-of-type(2)"));
            click(editBtn);
            waitForLoadingSpinner();
            logger.info("✏️ Mở form chỉnh sửa nhân viên đầu tiên");
        }
        return new AddEmployeePage();
    }

    /**
     * Xóa nhân viên đầu tiên trong danh sách
     */
    public EmployeeListPage deleteFirstEmployee() {
        if (!employeeRows.isEmpty()) {
            WebElement deleteBtn = employeeRows.get(0)
                .findElement(By.cssSelector("button.oxd-icon-button:nth-of-type(1)"));
            click(deleteBtn);
            // Xác nhận dialog xóa
            By confirmBtn = By.cssSelector(".oxd-button--label-danger");
            click(waitForPresence(confirmBtn));
            waitForLoadingSpinner();
            logger.info("🗑️ Đã xóa nhân viên đầu tiên");
        }
        return this;
    }

    // ==================== VERIFICATIONS ====================

    public boolean isEmployeeListDisplayed() {
        return getCurrentUrl().contains("viewEmployeeList");
    }

    public int getEmployeeCount() {
        return employeeRows.size();
    }

    public boolean isEmployeePresent(String employeeName) {
        return employeeRows.stream()
            .anyMatch(row -> row.getText().contains(employeeName));
    }

    public String getRecordCount() {
        try {
            By recordSpan = By.cssSelector(".oxd-text--span");
            List<WebElement> spans = driver.findElements(recordSpan);
            for (WebElement span : spans) {
                if (span.getText().contains("Record")) {
                    return span.getText();
                }
            }
        } catch (Exception e) {
            logger.warn("Không tìm thấy thông tin số bản ghi");
        }
        return "";
    }

    public boolean isNoRecordsFound() {
        By noRecord = By.cssSelector(".orangehrm-horizontal-padding span");
        try {
            WebElement el = driver.findElement(noRecord);
            return el.getText().contains("No Records Found");
        } catch (Exception e) {
            return false;
        }
    }
}
