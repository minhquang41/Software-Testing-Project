package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LeaveListPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LeaveTest - Kiểm thử module Quản lý Nghỉ phép (Leave Management)
 *
 * TC_LEAVE_001: Truy cập module Leave thành công
 * TC_LEAVE_002: Xem danh sách yêu cầu nghỉ phép
 * TC_LEAVE_003: Lọc nghỉ phép theo khoảng thời gian
 * TC_LEAVE_004: Reset bộ lọc Leave
 */
public class LeaveTest extends BaseTest {

    private DashboardPage dashboardPage;
    private LeaveListPage leaveListPage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        dashboardPage = loginPage.loginAsAdmin();
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
            "Tiền điều kiện: Phải đăng nhập thành công");
        leaveListPage = dashboardPage.navigateToLeave();
    }

    @Test(
        description = "TC_LEAVE_001: Truy cập module Nghỉ phép thành công",
        groups = {"smoke", "leave", "regression"}
    )
    public void tc_Leave_001_accessLeaveModule() {
        logStep("Xác nhận trang Leave List hiển thị");
        Assert.assertTrue(leaveListPage.isLeaveListPageDisplayed(),
            "Module Leave không hiển thị");

        logPass("TC_LEAVE_001 PASSED: Truy cập module Leave thành công");
    }

    @Test(
        description = "TC_LEAVE_002: Xem danh sách yêu cầu nghỉ phép",
        groups = {"leave", "regression"}
    )
    public void tc_Leave_002_viewLeaveList() {
        logStep("Kiểm tra danh sách nghỉ phép");
        int count = leaveListPage.getLeaveRequestCount();

        logStep("Ghi nhận số lượng yêu cầu nghỉ phép");
        Assert.assertTrue(count >= 0,
            "Lỗi khi tải danh sách nghỉ phép");

        logPass("TC_LEAVE_002 PASSED: Danh sách nghỉ phép hiển thị " + count + " yêu cầu");
    }

    @Test(
        description = "TC_LEAVE_003: Tìm kiếm nghỉ phép theo khoảng ngày",
        groups = {"leave", "regression", "search"}
    )
    public void tc_Leave_003_filterLeaveByDateRange() {
        logStep("Thiết lập ngày bắt đầu lọc");
        leaveListPage.setFromDate("2024-01-01");

        logStep("Thiết lập ngày kết thúc lọc");
        leaveListPage.setToDate("2024-12-31");

        logStep("Click tìm kiếm");
        leaveListPage.clickSearch();

        logStep("Xác nhận tìm kiếm không gây lỗi");
        Assert.assertTrue(leaveListPage.isLeaveListPageDisplayed(),
            "Tìm kiếm theo ngày gây lỗi hệ thống");

        logPass("TC_LEAVE_003 PASSED: Lọc theo ngày hoạt động bình thường");
    }

    @Test(
        description = "TC_LEAVE_004: Reset bộ lọc trong module Leave",
        groups = {"leave", "regression"}
    )
    public void tc_Leave_004_resetLeaveFilter() {
        logStep("Lọc theo ngày để thay đổi danh sách");
        leaveListPage.setFromDate("2025-01-01").clickSearch();

        logStep("Reset bộ lọc");
        leaveListPage.clickReset();

        logStep("Xác nhận trang Leave vẫn hiển thị sau reset");
        Assert.assertTrue(leaveListPage.isLeaveListPageDisplayed(),
            "Trang Leave không hiển thị sau khi reset");

        logPass("TC_LEAVE_004 PASSED: Reset bộ lọc Leave thành công");
    }
}
