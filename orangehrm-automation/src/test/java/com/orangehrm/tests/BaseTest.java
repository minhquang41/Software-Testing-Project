package com.orangehrm.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.orangehrm.listeners.TestListener;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.DriverManager;
import com.orangehrm.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * BaseTest - Lớp cha cơ sở cho tất cả test class
 * Quản lý setup/teardown WebDriver và ExtentReports
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        loginPage = new LoginPage();
        logger.info("🔧 Setup hoàn tất - WebDriver đã sẵn sàng");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
        logger.info("🧹 Teardown hoàn tất - WebDriver đã đóng");
    }

    /**
     * Ghi bước test vào ExtentReport
     */
    protected void logStep(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.INFO, "📋 " + message);
        }
        logger.info("📋 {}", message);
    }

    /**
     * Ghi thông tin pass vào ExtentReport
     */
    protected void logPass(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "✅ " + message);
        }
        logger.info("✅ {}", message);
    }

    /**
     * Ghi thông tin fail vào ExtentReport
     */
    protected void logFail(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, "❌ " + message);
        }
        logger.error("❌ {}", message);
    }
}


