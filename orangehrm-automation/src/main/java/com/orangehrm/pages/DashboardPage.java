package com.orangehrm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * DashboardPage - Page Object cho trang Dashboard OrangeHRM
 * URL: /web/index.php/dashboard/index
 */
public class DashboardPage extends BasePage {

    // ==================== WEB ELEMENTS ====================

    @FindBy(css = ".oxd-userdropdown-name")
    private WebElement userNameDisplay;

    @FindBy(css = ".oxd-main-menu-item--name")
    private List<WebElement> menuItems;

    @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
    private WebElement dashboardTitle;

    @FindBy(css = ".oxd-userdropdown-tab")
    private WebElement userDropdownButton;

    @FindBy(css = "a[href*='logout']")
    private WebElement logoutOption;

    @FindBy(css = ".orangehrm-dashboard-widget")
    private List<WebElement> dashboardWidgets;

    // ==================== PAGE ACTIONS ====================

    /**
     * Click vào menu item theo tên
     */
    public DashboardPage clickMenuByName(String menuName) {
        menuItems.stream()
            .filter(item -> item.getText().equalsIgnoreCase(menuName))
            .findFirst()
            .ifPresent(item -> {
                click(item);
                logger.info("📌 Click menu: {}", menuName);
            });
        return this;
    }

    /**
     * Điều hướng đến module PIM (Quản lý nhân viên)
     */
    public EmployeeListPage navigateToPIM() {
        driver.navigate().to(config.getBaseUrl() + "/pim/viewEmployeeList");
        logger.info("📌 Điều hướng đến module PIM");
        waitForLoadingSpinner();
        return new EmployeeListPage();
    }

    /**
     * Điều hướng đến module Leave (Nghỉ phép)
     */
    public LeaveListPage navigateToLeave() {
        driver.navigate().to(config.getBaseUrl() + "/leave/viewLeaveList");
        logger.info("📌 Điều hướng đến module Leave");
        waitForLoadingSpinner();
        return new LeaveListPage();
    }

    /**
     * Điều hướng đến module Admin
     */
    public AdminPage navigateToAdmin() {
        driver.navigate().to(config.getBaseUrl() + "/admin/viewSystemUsers");
        logger.info("📌 Điều hướng đến module Admin");
        waitForLoadingSpinner();
        return new AdminPage();
    }

    /**
     * Đăng xuất
     */
    public LoginPage logout() {
        click(userDropdownButton);
        waitForVisible(logoutOption);
        click(logoutOption);
        logger.info("🚪 Đã đăng xuất");
        return new LoginPage();
    }

    // ==================== VERIFICATIONS ====================

    public boolean isDashboardDisplayed() {
        try {
            waitForVisible(userNameDisplay);
            return getCurrentUrl().contains("dashboard");
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedInUsername() {
        return getText(userNameDisplay);
    }

    public String getDashboardTitle() {
        return getText(dashboardTitle);
    }

    public int getWidgetCount() {
        return dashboardWidgets.size();
    }

    public boolean isMenuItemPresent(String menuName) {
        return menuItems.stream()
            .anyMatch(item -> item.getText().equalsIgnoreCase(menuName));
    }
}
