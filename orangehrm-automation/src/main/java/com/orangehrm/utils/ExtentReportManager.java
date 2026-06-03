package com.orangehrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReportManager - Quản lý ExtentReports cho toàn bộ test suite
 * Sinh báo cáo HTML chi tiết với screenshot khi thất bại
 */
public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Khởi tạo ExtentReports (gọi 1 lần trước khi bắt đầu suite)
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = config.getReportPath() + "OrangeHRM_TestReport_" + timestamp + ".html";

            // Tạo thư mục reports nếu chưa có
            new java.io.File(config.getReportPath()).mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("OrangeHRM Automation Test Report");
            sparkReporter.config().setReportName("🍊 OrangeHRM - Báo Cáo Kiểm Thử Tự Động");
            sparkReporter.config().setTimelineEnabled(true);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Ứng dụng", "OrangeHRM Demo");
            extent.setSystemInfo("URL", config.getBaseUrl());
            extent.setSystemInfo("Trình duyệt", config.getBrowser().toUpperCase());
            extent.setSystemInfo("Môi trường", "Demo / Staging");
            extent.setSystemInfo("Người thực hiện", System.getProperty("user.name"));
            extent.setSystemInfo("Thời gian", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

            logger.info("📊 Đã khởi tạo ExtentReports: {}", reportPath);
        }
        return extent;
    }

    /**
     * Tạo test mới trong báo cáo
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThread.set(test);
        return test;
    }

    /**
     * Lấy ExtentTest của thread hiện tại
     */
    public static ExtentTest getTest() {
        return testThread.get();
    }

    /**
     * Ghi kết quả cuối cùng và đóng báo cáo
     */
    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            logger.info("📊 Đã xuất báo cáo HTML");
        }
    }
}
