package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DashboardTest - Kiểm thử Dashboard sau khi đăng nhập
 *
 * TC_DASH_001: Xác nhận Dashboard hiển thị sau đăng nhập
 * TC_DASH_002: Kiểm tra tên người dùng hiển thị đúng
 * TC_DASH_003: Kiểm tra menu điều hướng hiển thị đủ
 * TC_DASH_004: Kiểm tra widgets trên Dashboard
 */
public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        dashboardPage = loginPage.loginAsAdmin();
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
            "Tiền điều kiện: Đăng nhập thất bại");
    }

    @Test(
        description = "TC_DASH_001: Dashboard hiển thị đúng sau khi đăng nhập thành công",
        groups = {"smoke", "dashboard"}
    )
    public void tc_Dash_001_dashboardDisplayedAfterLogin() {
        logStep("Xác nhận URL chứa 'dashboard'");
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("dashboard"),
            "URL không phải dashboard");

        logPass("TC_DASH_001 PASSED: Dashboard hiển thị đúng");
    }

    @Test(
        description = "TC_DASH_002: Tên người dùng Admin hiển thị trên Dashboard",
        groups = {"smoke", "dashboard"}
    )
    public void tc_Dash_002_adminUsernameDisplayed() {
        logStep("Lấy tên người dùng hiển thị trên thanh menu");
        String username = dashboardPage.getLoggedInUsername();

        logStep("Xác nhận tên người dùng không rỗng");
        Assert.assertNotNull(username, "Không lấy được tên người dùng");
        Assert.assertFalse(username.isEmpty(), "Tên người dùng hiển thị trống");

        logPass("TC_DASH_002 PASSED: Tên người dùng hiển thị: " + username);
    }

    @Test(
        description = "TC_DASH_003: Menu điều hướng hiển thị đầy đủ các module",
        groups = {"smoke", "dashboard", "regression"}
    )
    public void tc_Dash_003_navigationMenuDisplayed() {
        logStep("Kiểm tra menu Admin hiển thị");
        Assert.assertTrue(dashboardPage.isMenuItemPresent("Admin"),
            "Menu Admin không hiển thị");

        logStep("Kiểm tra menu PIM hiển thị");
        Assert.assertTrue(dashboardPage.isMenuItemPresent("PIM"),
            "Menu PIM không hiển thị");

        logStep("Kiểm tra menu Leave hiển thị");
        Assert.assertTrue(dashboardPage.isMenuItemPresent("Leave"),
            "Menu Leave không hiển thị");

        logStep("Kiểm tra menu Time hiển thị");
        Assert.assertTrue(dashboardPage.isMenuItemPresent("Time"),
            "Menu Time không hiển thị");

        logPass("TC_DASH_003 PASSED: Tất cả menu chính hiển thị đầy đủ");
    }

    @Test(
        description = "TC_DASH_004: Widgets trên Dashboard hiển thị",
        groups = {"dashboard", "regression"}
    )
    public void tc_Dash_004_dashboardWidgetsDisplayed() {
        logStep("Kiểm tra số lượng widget trên Dashboard");
        int widgetCount = dashboardPage.getWidgetCount();
        Assert.assertTrue(widgetCount > 0,
            "Dashboard không có widget nào");

        logPass("TC_DASH_004 PASSED: Dashboard hiển thị " + widgetCount + " widget(s)");
    }
}
