package com.orangehrm.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverManager - Quản lý vòng đời WebDriver
 * Sử dụng ThreadLocal để hỗ trợ chạy test song song (parallel)
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverManager() {}

    /**
     * Khởi tạo WebDriver dựa theo cấu hình browser
     */
    public static void initDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        WebDriver driver = null;

        logger.info("🚀 Khởi tạo {} driver (headless={})", browser, headless);

        // Thử tối đa 3 lần nếu Chrome không khởi động được
        int attempts = 0;
        while (driver == null && attempts < 3) {
            try {
                attempts++;
                switch (browser) {
                    case "chrome": driver = initChromeDriver(headless); break;
                    case "firefox": driver = initFirefoxDriver(headless); break;
                    default:
                        logger.warn("⚠️ Browser '{}' không hỗ trợ, dùng Chrome", browser);
                        driver = initChromeDriver(headless);
                }
            } catch (Exception e) {
                logger.warn("⚠️ Lần {} khởi tạo driver thất bại: {}", attempts, e.getMessage());
                if (attempts < 3) {
                    try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
                } else {
                    throw new RuntimeException("Không thể khởi tạo WebDriver sau 3 lần thử", e);
                }
            }
        }

        if (driver == null) {
            throw new RuntimeException("Không thể khởi tạo WebDriver sau " + attempts + " lần thử");
        }

        // Tăng timeout để xử lý server chậm
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60)); // tăng lên 60s
        driver.manage().window().maximize();

        driverThread.set(driver);
        logger.info("✅ Khởi tạo driver thành công");
    }

    private static WebDriver initChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver initFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        return new FirefoxDriver(options);
    }

    /**
     * Lấy instance WebDriver hiện tại của thread
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThread.get();
        if (driver == null) {
            throw new IllegalStateException("❌ Driver chưa được khởi tạo. Hãy gọi initDriver() trước.");
        }
        return driver;
    }

    /**
     * Đóng và giải phóng WebDriver
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            logger.info("✅ Đã đóng WebDriver");
        }
    }
}