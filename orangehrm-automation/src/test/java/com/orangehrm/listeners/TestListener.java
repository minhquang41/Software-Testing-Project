package com.orangehrm.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.orangehrm.utils.ExtentReportManager;
import com.orangehrm.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener - Tự động xử lý sự kiện test:
 * - Tạo ExtentTest khi bắt đầu
 * - Chụp screenshot khi FAIL
 * - Ghi log chi tiết cho từng kết quả
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("╔════════════════════════════════════════╗");
        logger.info("║  🚀 BẮT ĐẦU TEST SUITE: {}  ║", context.getName());
        logger.info("╚════════════════════════════════════════╝");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        logger.info("▶️ Bắt đầu test: [{}]", testName);

        // Tạo test entry trong ExtentReport
        ExtentTest test = ExtentReportManager.createTest(
            testName,
            description != null ? description : "OrangeHRM Automated Test"
        );
        test.assignCategory(result.getTestClass().getName()
            .replace("com.orangehrm.tests.", ""));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;
        logger.info("✅ PASSED: [{}] - Thời gian: {}s", testName, duration);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "✅ Test PASSED - Thời gian: " + duration + "s");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("❌ FAILED: [{}]", testName);
        logger.error("   Lý do: {}", result.getThrowable().getMessage());

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            // Chụp screenshot khi fail
            try {
                byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes();
                if (screenshot.length > 0) {
                    test.fail("❌ Test FAILED",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(
                            java.util.Base64.getEncoder().encodeToString(screenshot),
                            testName + "_failure"
                        ).build()
                    );
                }
            } catch (Exception e) {
                logger.warn("Không thể đính kèm screenshot: {}", e.getMessage());
            }
            test.log(Status.FAIL, "Lỗi: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("⏭️ SKIPPED: [{}]", testName);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "⏭️ Test bị bỏ qua: "
                + (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;

        logger.info("╔════════════════════════════════════════╗");
        logger.info("║           📊 KẾT QUẢ TEST SUITE        ║");
        logger.info("╠════════════════════════════════════════╣");
        logger.info("║  Tổng số test  : {:3d}                   ║", total);
        logger.info("║  ✅ Passed      : {:3d}                   ║", passed);
        logger.info("║  ❌ Failed      : {:3d}                   ║", failed);
        logger.info("║  ⏭️ Skipped     : {:3d}                   ║", skipped);
        logger.info("╚════════════════════════════════════════╝");

        ExtentReportManager.flushReports();
    }
}
