package com.orangehrm.tests;

import com.orangehrm.pages.AdminPage;
import com.orangehrm.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * AdminTest - Kiểm thử module Admin - Quản lý người dùng hệ thống
 *
 * TC_ADMIN_001: Truy cập module Admin thành công
 * TC_ADMIN_002: Xem danh sách người dùng hệ thống
 * TC_ADMIN_003: Tìm kiếm người dùng theo username
 * TC_ADMIN_004: Tìm kiếm người dùng không tồn tại
 * TC_ADMIN_005: Reset bộ lọc tìm kiếm Admin
 */
public class AdminTest extends BaseTest {

    private DashboardPage dashboardPage;
    private AdminPage adminPage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        dashboardPage = loginPage.loginAsAdmin();
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
            "Tiền điều kiện: Cần đăng nhập thành công");
        adminPage = dashboardPage.navigateToAdmin();
    }

    @Test(
        description = "TC_ADMIN_001: Truy cập module Admin - Quản lý người dùng",
        groups = {"smoke", "admin", "regression"}
    )
    public void tc_Admin_001_accessAdminModule() {
        logStep("Xác nhận module Admin đã mở");
        Assert.assertTrue(adminPage.isAdminPageDisplayed(),
            "Module Admin không hiển thị");

        logPass("TC_ADMIN_001 PASSED: Module Admin truy cập thành công");
    }

    @Test(
        description = "TC_ADMIN_002: Danh sách người dùng hệ thống hiển thị",
        groups = {"admin", "regression"}
    )
    public void tc_Admin_002_systemUsersListDisplayed() {
        logStep("Kiểm tra danh sách người dùng");
        int userCount = adminPage.getUserCount();

        logStep("Xác nhận có ít nhất 1 người dùng (Admin)");
        Assert.assertTrue(userCount >= 1,
            "Danh sách người dùng trống");

        logPass("TC_ADMIN_002 PASSED: Danh sách hiển thị " + userCount + " người dùng");
    }

    @Test(
        description = "TC_ADMIN_003: Tìm kiếm người dùng 'Admin' theo username",
        groups = {"admin", "regression", "search"}
    )
    public void tc_Admin_003_searchUserByUsername() {
        logStep("Tìm kiếm người dùng với username 'Admin'");
        adminPage.searchByUsername("Admin");

        logStep("Xác nhận Admin xuất hiện trong kết quả");
        Assert.assertTrue(adminPage.isUserPresent("Admin"),
            "Người dùng 'Admin' không xuất hiện trong kết quả tìm kiếm");

        logPass("TC_ADMIN_003 PASSED: Tìm thấy người dùng 'Admin'");
    }

    @Test(
        description = "TC_ADMIN_004: Tìm kiếm người dùng không tồn tại",
        groups = {"admin", "regression", "search", "negative"}
    )
    public void tc_Admin_004_searchNonExistentUser() {
        logStep("Tìm kiếm username không tồn tại");
        adminPage.searchByUsername("NONEXISTENT_USER_XYZ_" + System.currentTimeMillis());

        logStep("Xác nhận không có kết quả");
        boolean noResults = adminPage.getUserCountAfterSearch() == 0
            || adminPage.isNoRecordFound();
        Assert.assertTrue(noResults,
            "Hệ thống hiển thị kết quả khi username không tồn tại");

        logPass("TC_ADMIN_004 PASSED: Tìm kiếm đúng khi không có kết quả");
    }

    @Test(
        description = "TC_ADMIN_005: Reset bộ lọc tìm kiếm trong module Admin",
        groups = {"admin", "regression"}
    )
    public void tc_Admin_005_resetAdminSearchFilter() {
        logStep("Tìm kiếm 'Admin' để lọc danh sách");
        adminPage.searchByUsername("Admin");
        int filteredCount = adminPage.getUserCount();

        logStep("Reset bộ lọc");
        adminPage.clickReset();
        int afterResetCount = adminPage.getUserCount();

        logStep("Xác nhận sau reset hiển thị nhiều hơn hoặc bằng khi đang lọc");
        Assert.assertTrue(afterResetCount >= filteredCount,
            "Sau reset phải hiển thị ít nhất bằng số kết quả khi lọc");

        logPass("TC_ADMIN_005 PASSED: Reset bộ lọc thành công (" 
            + filteredCount + " → " + afterResetCount + " users)");
    }
}
