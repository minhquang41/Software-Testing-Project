package com.orangehrm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object cho trang đăng nhập OrangeHRM
 * URL: /web/index.php/auth/login
 */
public class LoginPage extends BasePage {

    // ==================== WEB ELEMENTS ====================

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = ".oxd-alert-content-text")
    private WebElement errorMessage;

    @FindBy(xpath = "//img[contains(@src,'logo') or contains(@alt,'logo') or contains(@alt,'Orange') or contains(@class,'logo')] | //div[contains(@class,'login')]//img[1]")
    private WebElement orangeHrmLogo;

    @FindBy(css = ".oxd-text--h5")
    private WebElement loginTitle;

    @FindBy(css = "p.oxd-text--p")
    private WebElement forgotPasswordLink;

    // ==================== PAGE ACTIONS ====================

    /**
     * Mở trang đăng nhập
     */
    public LoginPage openLoginPage() {
        navigateTo(config.getBaseUrl() + "/auth/login");
        logger.info("📄 Mở trang đăng nhập OrangeHRM");
        return this;
    }

    /**
     * Nhập username
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        logger.info("👤 Nhập username: {}", username);
        return this;
    }

    /**
     * Nhập password
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        logger.info("🔐 Nhập password");
        return this;
    }

    /**
     * Submit khi kỳ vọng đăng nhập THÀNH CÔNG → trả về DashboardPage.
     * Chỉ gọi khi chắc chắn credentials hợp lệ.
     */
    public DashboardPage submitLoginSuccess() {
        click(loginButton);
        logger.info("🖱️ Click nút Login (expect: success)");
        waitForLoadingSpinner();
        return new DashboardPage();
    }

    /**
     * Submit khi kỳ vọng đăng nhập THẤT BẠI → ở lại LoginPage.
     * Dùng cho các test case negative (sai password, username trống, v.v.)
     */
    public LoginPage submitLoginFail() {
        click(loginButton);
        logger.info("🖱️ Click nút Login (expect: fail)");
        return this;
    }

    /**
     * Đăng nhập Admin lấy thông tin từ config.properties (không hardcode)
     */
    public DashboardPage loginAsAdmin() {
        return openLoginPage()
            .enterUsername(config.getAdminUsername())
            .enterPassword(config.getAdminPassword())
            .submitLoginSuccess();
    }

    /**
     * Đăng nhập với thông tin không hợp lệ, ở lại LoginPage
     */
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        openLoginPage();
        enterUsername(username);
        enterPassword(password);
        return submitLoginFail();
    }

    // ==================== VERIFICATIONS ====================

    public boolean isLoginPageDisplayed() {
        try {
            return isDisplayed(usernameField) && isDisplayed(loginButton);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLogoDisplayed() {
        try {
            return isDisplayed(orangeHrmLogo);
        } catch (Exception e) {
            // Thử tìm bất kỳ img nào trong vùng login
            try {
                return !driver.findElements(
                    org.openqa.selenium.By.cssSelector(".orangehrm-login-container img, .oxd-sheet img")
                ).isEmpty();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public String getErrorMessage() {
        waitForVisible(errorMessage);
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getLoginTitle() {
        return getText(loginTitle);
    }

    public boolean isUsernameFieldEmpty() {
        return usernameField.getAttribute("value").isEmpty();
    }

    public boolean isPasswordFieldEmpty() {
        return passwordField.getAttribute("value").isEmpty();
    }
}
