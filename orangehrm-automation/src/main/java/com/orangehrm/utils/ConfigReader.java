package com.orangehrm.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Đọc thông tin cấu hình từ file config.properties
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    private ConfigReader() {
        loadProperties();
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
            logger.info("✅ Đã load cấu hình từ: {}", CONFIG_PATH);
        } catch (IOException e) {
            logger.error("❌ Không thể đọc file cấu hình: {}", CONFIG_PATH);
            throw new RuntimeException("Không tìm thấy file config.properties");
        }
    }

    public String getBaseUrl() { return getProperty("base.url"); }
    public String getAdminUsername() { return getProperty("admin.username"); }
    public String getAdminPassword() { return getProperty("admin.password"); }
    public String getBrowser() { return getProperty("browser"); }
    public boolean isHeadless() { return Boolean.parseBoolean(getProperty("headless")); }
    public int getImplicitWait() { return Integer.parseInt(getProperty("implicit.wait")); }
    public int getExplicitWait() { return Integer.parseInt(getProperty("explicit.wait")); }
    public int getPageLoadTimeout() { return Integer.parseInt(getProperty("page.load.timeout")); }
    public String getScreenshotPath() { return getProperty("screenshot.path"); }
    public String getReportPath() { return getProperty("report.path"); }
    public String getReportName() { return getProperty("report.name"); }
    public String getTestDataPath() { return getProperty("test.data.path"); }

    private String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Key không tồn tại: " + key);
        return value.trim();
    }
}
