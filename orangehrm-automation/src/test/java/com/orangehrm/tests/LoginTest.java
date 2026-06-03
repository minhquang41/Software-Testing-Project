package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Kiểm thử tính năng Đăng nhập
 *
 * Test cases:
 * TC_LOGIN_001: Đăng nhập thành công với tài khoản Admin hợp lệ
 * TC_LOGIN_002: Đăng nhập thất bại với mật khẩu sai
 * TC_LOGIN_003: Đăng nhập thất bại với username không tồn tại
 * TC_LOGIN_004: Đăng nhập với cả hai trường để trống
 * TC_LOGIN_005: Kiểm tra logo OrangeHRM hiển thị trên trang login
 * TC_LOGIN_006: OrangeHRM không phân biệt chữ hoa/thường ở username
 * TC_LOGIN_007: Đăng xuất thành công
 * TC_LOGIN_008: Xác nhận không thể truy cập Dashboard khi chưa đăng nhập
 */
public class LoginTest extends BaseTest {

    @Test(
        description = "TC_LOGIN_001: Đăng nhập thành công với tài khoản Admin hợp lệ",
        groups = {"smoke", "login", "regression"}
    )
    public void tc_Login_001_loginSuccessWithValidCredentials() {
        logStep("Mở trang đăng nhập OrangeHRM");
        loginPage.openLoginPage();

        logStep("Xác nhận trang đăng nhập hiển thị");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Trang đăng nhập không hiển thị");

        logStep("Nhập username và password từ config (không hardcode)");
        loginPage.enterUsername(ConfigReader.getInstance().getAdminUsername());
        loginPage.enterPassword(ConfigReader.getInstance().getAdminPassword());

        logStep("Submit đăng nhập, kỳ vọng thành công");
        DashboardPage dashboard = loginPage.submitLoginSuccess();

        logStep("Xác nhận Dashboard hiển thị");
        Assert.assertTrue(dashboard.isDashboardDisplayed(),
            "Dashboard không hiển thị sau khi đăng nhập");

        logPass("TC_LOGIN_001 PASSED: Đăng nhập Admin thành công");
    }

    @Test(
        description = "TC_LOGIN_002: Đăng nhập thất bại với mật khẩu sai",
        groups = {"login", "regression", "negative"}
    )
    public void tc_Login_002_loginFailWithWrongPassword() {
        logStep("Đăng nhập với đúng username nhưng sai password");
        loginPage.loginWithInvalidCredentials(
            ConfigReader.getInstance().getAdminUsername(),
            "wrongpassword_XYZ"
        );

        logStep("Xác nhận thông báo lỗi xuất hiện");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Thông báo lỗi không xuất hiện khi nhập sai mật khẩu");

        logStep("Kiểm tra nội dung thông báo lỗi");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(
            errorMsg.toLowerCase().contains("invalid credentials"),
            "Nội dung thông báo lỗi không đúng: " + errorMsg
        );

        logPass("TC_LOGIN_002 PASSED: Hệ thống hiển thị lỗi khi mật khẩu sai");
    }

    @Test(
        description = "TC_LOGIN_003: Đăng nhập thất bại với username không tồn tại",
        groups = {"login", "regression", "negative"}
    )
    public void tc_Login_003_loginFailWithInvalidUsername() {
        logStep("Đăng nhập với username không tồn tại trong hệ thống");
        loginPage.loginWithInvalidCredentials("InvalidUser_XYZ_999", "admin123");

        logStep("Xác nhận thông báo lỗi xuất hiện");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Thông báo lỗi không xuất hiện khi username không tồn tại");

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(
            errorMsg.toLowerCase().contains("invalid credentials"),
            "Nội dung thông báo lỗi không đúng: " + errorMsg
        );

        logPass("TC_LOGIN_003 PASSED: Hệ thống từ chối username không hợp lệ");
    }

    @Test(
        description = "TC_LOGIN_004: Đăng nhập với cả hai trường để trống",
        groups = {"login", "regression", "negative"}
    )
    public void tc_Login_004_loginWithEmptyCredentials() {
        logStep("Mở trang đăng nhập");
        loginPage.openLoginPage();

        logStep("Bỏ trống cả username và password, submit thất bại");
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.submitLoginFail();

        logStep("Xác nhận vẫn ở trang đăng nhập (không chuyển sang Dashboard)");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Hệ thống phải giữ nguyên trang login khi cả hai trường trống");

        logPass("TC_LOGIN_004 PASSED: Hệ thống không cho phép đăng nhập khi thông tin trống");
    }

    @Test(
        description = "TC_LOGIN_005: Các thành phần UI trang login hiển thị đầy đủ",
        groups = {"login", "smoke"}
    )
    public void tc_Login_005_verifyLoginPageUIElements() {
        logStep("Mở trang đăng nhập");
        loginPage.openLoginPage();

        logStep("Xác nhận form đăng nhập hiển thị đầy đủ (username + password + button)");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Form đăng nhập không hiển thị đầy đủ");

        logStep("Xác nhận tiêu đề trang login");
        String title = loginPage.getLoginTitle();
        Assert.assertNotNull(title, "Tiêu đề trang login không tồn tại");
        Assert.assertFalse(title.isEmpty(), "Tiêu đề trang login trống");

        logPass("TC_LOGIN_005 PASSED: Trang đăng nhập hiển thị đầy đủ các thành phần UI, tiêu đề: " + title);
    }

    @Test(
        description = "TC_LOGIN_006: OrangeHRM không phân biệt chữ hoa/thường ở username ('admin' = 'Admin')",
        groups = {"login", "regression"}
    )
    public void tc_Login_006_loginUsernameIsCaseInsensitive() {
        logStep("Đăng nhập với username chữ thường 'admin' (OrangeHRM không phân biệt hoa/thường)");
        // OrangeHRM demo thực tế: 'admin' và 'Admin' đều đăng nhập được
        loginPage.openLoginPage();
        loginPage.enterUsername("admin");
        loginPage.enterPassword(ConfigReader.getInstance().getAdminPassword());
        DashboardPage dashboard = loginPage.submitLoginSuccess();

        logStep("Xác nhận đăng nhập thành công → Dashboard hiển thị");
        Assert.assertTrue(dashboard.isDashboardDisplayed(),
            "OrangeHRM phải cho phép đăng nhập với username không phân biệt hoa/thường");

        logPass("TC_LOGIN_006 PASSED: Đăng nhập với 'admin' (chữ thường) thành công");
    }

    @Test(
        description = "TC_LOGIN_007: Đăng xuất thành công và quay về trang login",
        groups = {"login", "smoke", "regression"}
    )
    public void tc_Login_007_logoutSuccessfully() {
        logStep("Đăng nhập với tài khoản Admin từ config");
        DashboardPage dashboard = loginPage.loginAsAdmin();

        logStep("Xác nhận đang ở Dashboard");
        Assert.assertTrue(dashboard.isDashboardDisplayed(),
            "Tiền điều kiện thất bại: không vào được Dashboard");

        logStep("Thực hiện đăng xuất");
        loginPage = dashboard.logout();

        logStep("Xác nhận đã quay về trang đăng nhập");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Sau khi đăng xuất phải quay về trang login");

        logPass("TC_LOGIN_007 PASSED: Đăng xuất thành công");
    }

    @Test(
        description = "TC_LOGIN_008: Không thể truy cập Dashboard trực tiếp khi chưa đăng nhập",
        groups = {"login", "security", "regression"}
    )
    public void tc_Login_008_accessDashboardWithoutLogin() {
        logStep("Trực tiếp truy cập URL Dashboard mà không qua đăng nhập");
        loginPage.openLoginPage();
        loginPage.navigateTo(
            ConfigReader.getInstance().getBaseUrl() + "/dashboard/index"
        );

        logStep("Xác nhận bị redirect về trang đăng nhập");
        boolean redirectedToLogin = loginPage.getCurrentUrl().contains("login")
            || loginPage.getCurrentUrl().contains("auth");

        Assert.assertTrue(redirectedToLogin,
            "Lỗi bảo mật: Hệ thống cho phép truy cập Dashboard không qua xác thực!");

        logPass("TC_LOGIN_008 PASSED: Dashboard được bảo vệ, redirect về login đúng cách");
    }
}
