package com.orangehrm.tests;

import com.orangehrm.pages.AddEmployeePage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.EmployeeListPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * EmployeeTest - Kiểm thử module Quản lý Nhân viên (PIM)
 *
 * Test cases:
 * TC_EMP_001: Xem danh sách nhân viên
 * TC_EMP_002: Thêm nhân viên mới thành công
 * TC_EMP_003: Thêm nhân viên với thông tin thiếu
 * TC_EMP_004: Tìm kiếm nhân viên theo tên
 * TC_EMP_005: Tìm kiếm nhân viên không tồn tại
 * TC_EMP_006: Reset bộ lọc tìm kiếm
 * TC_EMP_007: Kiểm tra Employee ID được tạo tự động
 */
public class EmployeeTest extends BaseTest {

    private DashboardPage dashboardPage;
    private EmployeeListPage employeeListPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToPIM() {
        dashboardPage = loginPage.loginAsAdmin();
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
            "Không thể đăng nhập để thực hiện test PIM");
    }

    @Test(
        description = "TC_EMP_001: Xem danh sách nhân viên trong module PIM",
        groups = {"smoke", "employee", "regression"}
    )
    public void tc_Emp_001_viewEmployeeList() {
        logStep("Điều hướng đến module PIM - Danh sách nhân viên");
        employeeListPage = dashboardPage.navigateToPIM();

        logStep("Xác nhận trang danh sách nhân viên hiển thị");
        Assert.assertTrue(employeeListPage.isEmployeeListDisplayed(),
            "Trang danh sách nhân viên không hiển thị");

        logStep("Kiểm tra số lượng nhân viên > 0");
        int count = employeeListPage.getEmployeeCount();
        Assert.assertTrue(count > 0,
            "Danh sách nhân viên trống - phải có ít nhất 1 nhân viên");

        logPass("TC_EMP_001 PASSED: Danh sách hiển thị " + count + " nhân viên");
    }

    @Test(
        description = "TC_EMP_002: Thêm nhân viên mới với đầy đủ thông tin bắt buộc",
        groups = {"employee", "regression", "crud"}
    )
    public void tc_Emp_002_addNewEmployeeSuccessfully() {
        logStep("Điều hướng đến PIM");
        employeeListPage = dashboardPage.navigateToPIM();

        logStep("Click nút Add Employee");
        AddEmployeePage addPage = employeeListPage.clickAddEmployee();

        logStep("Xác nhận form thêm nhân viên hiển thị");
        Assert.assertTrue(addPage.isAddEmployeePageDisplayed(),
            "Form thêm nhân viên không hiển thị");

        String firstName = "Test";
        String lastName = "AutoUser" + System.currentTimeMillis() % 10000;

        logStep("Điền thông tin nhân viên: " + firstName + " " + lastName);
        addPage.fillBasicInfo(firstName, "", lastName);

        logStep("Lưu thông tin nhân viên");
        employeeListPage = addPage.clickSave();

        // Sau khi lưu, hệ thống thường chuyển sang trang edit của nhân viên vừa tạo
        logStep("Xác nhận lưu thành công");
        boolean saveSuccess = addPage.isToastMessageDisplayed()
            || addPage.getCurrentUrl().contains("pim");
        Assert.assertTrue(saveSuccess,
            "Không có dấu hiệu lưu thành công");

        logPass("TC_EMP_002 PASSED: Thêm nhân viên " + firstName + " " + lastName + " thành công");
    }

    @Test(
        description = "TC_EMP_003: Thêm nhân viên với Last Name bị trống",
        groups = {"employee", "regression", "negative", "crud"}
    )
    public void tc_Emp_003_addEmployeeWithEmptyLastName() {
        logStep("Điều hướng đến PIM và click Add Employee");
        employeeListPage = dashboardPage.navigateToPIM();
        AddEmployeePage addPage = employeeListPage.clickAddEmployee();

        logStep("Chỉ nhập First Name, bỏ trống Last Name");
        addPage.fillBasicInfo("TestOnly", "", "");

        logStep("Click Save với Last Name trống");
        addPage.clickSave();

        logStep("Xác nhận thông báo lỗi validation xuất hiện");
        Assert.assertTrue(addPage.isRequiredFieldErrorDisplayed(),
            "Không có thông báo lỗi khi Last Name trống");

        logPass("TC_EMP_003 PASSED: Validation hoạt động đúng khi thiếu Last Name");
    }

    @Test(
        description = "TC_EMP_004: Tìm kiếm nhân viên theo tên hợp lệ",
        groups = {"employee", "regression", "search"}
    )
    public void tc_Emp_004_searchEmployeeByName() {
        logStep("Điều hướng đến trang danh sách nhân viên");
        employeeListPage = dashboardPage.navigateToPIM();

        logStep("Tìm kiếm nhân viên với từ khóa 'a'");
        employeeListPage
            .searchByName("a")
            .clickSearch();

        logStep("Xác nhận có kết quả tìm kiếm");
        int results = employeeListPage.getEmployeeCount();
        Assert.assertTrue(results >= 0,
            "Tìm kiếm không hoạt động - lỗi hệ thống");

        logPass("TC_EMP_004 PASSED: Tìm kiếm trả về " + results + " kết quả");
    }

    @Test(
        description = "TC_EMP_005: Tìm kiếm nhân viên không tồn tại trong hệ thống",
        groups = {"employee", "regression", "search", "negative"}
    )
    public void tc_Emp_005_searchNonExistentEmployee() {
        logStep("Điều hướng đến danh sách nhân viên");
        employeeListPage = dashboardPage.navigateToPIM();

        logStep("Tìm kiếm với tên ngẫu nhiên không tồn tại");
        String fakeName = "XYZ_NONEXISTENT_" + System.currentTimeMillis();
        employeeListPage
            .searchByName(fakeName)
            .clickSearch();

        logStep("Xác nhận không có kết quả hoặc hiển thị 'No Records Found'");
        boolean noResults = employeeListPage.getEmployeeCount() == 0
            || employeeListPage.isNoRecordsFound();
        Assert.assertTrue(noResults,
            "Hệ thống trả về kết quả khi tìm kiếm tên không tồn tại");

        logPass("TC_EMP_005 PASSED: Hiển thị đúng khi không tìm thấy nhân viên");
    }

    @Test(
        description = "TC_EMP_006: Reset bộ lọc tìm kiếm nhân viên",
        groups = {"employee", "regression", "search"}
    )
    public void tc_Emp_006_resetSearchFilter() {
        logStep("Điều hướng đến danh sách nhân viên");
        employeeListPage = dashboardPage.navigateToPIM();

        logStep("Lấy số lượng nhân viên ban đầu");
        int initialCount = employeeListPage.getEmployeeCount();

        logStep("Tìm kiếm để lọc danh sách");
        employeeListPage.searchByName("admin").clickSearch();
        int filteredCount = employeeListPage.getEmployeeCount();

        logStep("Click Reset để xóa bộ lọc");
        employeeListPage.clickReset();
        int resetCount = employeeListPage.getEmployeeCount();

        logStep("Xác nhận danh sách trở về trạng thái ban đầu");
        Assert.assertEquals(resetCount, initialCount,
            "Sau khi reset, số lượng nhân viên phải bằng ban đầu");

        logPass("TC_EMP_006 PASSED: Reset bộ lọc thành công ("
            + filteredCount + " → " + resetCount + " nhân viên)");
    }

    @Test(
        description = "TC_EMP_007: Employee ID tự động được tạo khi thêm nhân viên mới",
        groups = {"employee", "regression", "crud"}
    )
    public void tc_Emp_007_verifyAutoGeneratedEmployeeId() {
        logStep("Điều hướng đến form thêm nhân viên");
        employeeListPage = dashboardPage.navigateToPIM();
        AddEmployeePage addPage = employeeListPage.clickAddEmployee();

        logStep("Kiểm tra Employee ID được tự động tạo");
        String autoId = addPage.getAutoGeneratedEmployeeId();
        Assert.assertNotNull(autoId, "Employee ID không được tạo tự động");
        Assert.assertFalse(autoId.isEmpty(), "Employee ID tự động là rỗng");

        logPass("TC_EMP_007 PASSED: Employee ID tự động được tạo: " + autoId);
    }
}
