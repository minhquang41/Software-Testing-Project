package com.orangehrm.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils - Tiện ích chụp và lưu screenshot
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = ConfigReader.getInstance().getScreenshotPath();

    /**
     * Chụp và lưu screenshot với tên dựa theo test case
     * @param testName tên test case
     * @return đường dẫn file screenshot (để đính kèm vào report)
     */
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            // Tạo thư mục nếu chưa có
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Chụp screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("📸 Đã chụp screenshot: {}", filePath);
            return destFile.getAbsolutePath();

        } catch (IOException e) {
            logger.error("❌ Không thể lưu screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Chụp screenshot và trả về dưới dạng byte array (cho ExtentReport)
     */
    public static byte[] captureScreenshotAsBytes() {
        try {
            WebDriver driver = DriverManager.getDriver();
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("❌ Không thể chụp screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }
}
